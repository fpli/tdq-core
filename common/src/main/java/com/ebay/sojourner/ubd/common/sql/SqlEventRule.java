package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.rule.Rule;
import com.google.common.collect.ImmutableList;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.config.CalciteConnectionConfigImpl;
import org.apache.calcite.interpreter.Interpreter;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.schema.ScalarFunction;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.impl.ScalarFunctionImpl;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.util.ChainedSqlOperatorTable;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;
import org.apache.log4j.Logger;

public class SqlEventRule implements Rule<UbiEvent> {

  private static final Logger LOGGER = Logger.getLogger(SqlEventRule.class);

  private String sql;
  private SojReflectiveDataSource dataSource;
  private SojDataContext dataContext;
  private RelNode relTree;
  private Interpreter interpreter;

  public SqlEventRule(String sql) {
    this.sql = sql;
    setupContext();
    interpret(sql);
  }

  private void setupContext() {
    SchemaPlus rootSchema = Frameworks.createRootSchema(true);
    rootSchema.setCacheEnabled(false);

    // Add data source
    SojReflectiveDataSource dataSource = new SojReflectiveDataSource();
    this.dataSource = dataSource;
    rootSchema.add("soj", new ReflectiveSchema(dataSource));

    // Add functions
    boolean upCase = false;
    Class clazz = UdfManager.class;
    for (Map.Entry<String, ScalarFunction> entry : ScalarFunctionImpl.createAll(clazz).entries()) {
      String name = entry.getKey();
      if (upCase) {
        name = name.toUpperCase(Locale.ROOT);
      }
      rootSchema.add(name, entry.getValue());
    }

    rootSchema.add("square", ScalarFunctionImpl.create(UdfManager.SquareFunction.class, "eval"));

    // Create planner
    CalciteCatalogReader reader =
        new CalciteCatalogReader(
            CalciteSchema.from(rootSchema),
            ImmutableList.of(),
            new JavaTypeFactoryImpl(),
            new CalciteConnectionConfigImpl(new Properties()));

    SqlOperatorTable opTable = ChainedSqlOperatorTable.of(SqlStdOperatorTable.instance(), reader);

    final FrameworkConfig config =
        Frameworks.newConfigBuilder()
            .parserConfig(SqlParser.Config.DEFAULT)
            .defaultSchema(rootSchema)
            .operatorTable(opTable)
            .build();
    Planner planner = Frameworks.getPlanner(config);

    // Create data context
    dataContext = new SojDataContext(planner, rootSchema);
  }

  private void interpret(String sql) {
    try {
      Planner planner = dataContext.getPlanner();
      long beforeParse = System.nanoTime();
      SqlNode parseTree = planner.parse(sql);
      SqlNode validated = planner.validate(parseTree);
      RelNode relTree = planner.rel(validated).rel;
      long afterRel = System.nanoTime();
      this.relTree = relTree;
      this.interpreter = new Interpreter(dataContext, relTree);
      long end = System.nanoTime();
      // printDuration("parse to rel", beforeParse, afterRel);
      // printDuration("interpreter.new*", afterRel, end);
    } catch (Exception e) {
      throw new RuntimeException("SQL rule cannot be interpreted.", e);
    }
  }

  public String getSql() {
    return sql;
  }

  @Override
  public void init() {}

  @Override
  public int getBotFlag(UbiEvent ubiEvent) {
    long start = System.nanoTime();
    int botFlag = 0;
    try {
      dataSource.updateData(
          new SojReflectiveEvent.Builder()
              .agentInfo(ubiEvent.getAgentInfo())
              .icfBinary(ubiEvent.getIcfBinary())
              .build());
      long beforeCreateInterpreter = System.nanoTime();
      interpreter = new Interpreter(dataContext, relTree);
      long beforeInterpreterFirst = System.nanoTime();
      if (interpreter.any()) {
        botFlag = (int) interpreter.first()[0];
      }
      long end = System.nanoTime();
      // printDuration("interpreter.new", beforeCreateInterpreter, beforeInterpreterFirst);
      // printDuration("interpreter.first", beforeInterpreterFirst, end);
      // printDuration("getBotFlag", start, end);
    } catch (Exception e) {
      botFlag = 0;
      LOGGER.warn("Fail to get bot flag.", e);
    }
    return botFlag;
  }
}
