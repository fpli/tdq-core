package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.rule.Rule;
import com.google.common.collect.ImmutableList;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.config.CalciteConnectionConfigImpl;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.schema.ScalarFunction;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.impl.ScalarFunctionImpl;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.util.ChainedSqlOperatorTable;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.tools.Programs;
import org.apache.log4j.Logger;

public abstract class SqlEventRule implements Rule<UbiEvent> {

  protected static final Logger LOGGER = Logger.getLogger(SqlEventRule.class);
  protected SojReflectiveDataSource dataSource;
  protected SojDataContext dataContext;
  private String sql;

  public SqlEventRule(String sql) {
    this.sql = sql;
    prepareDataContext();
    prepareSql(sql);
  }

  public String getSql() {
    return sql;
  }

  @Override
  public void init() {
  }

  private void prepareDataContext() {
    // Create root schema, add data source and functions
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
            .programs(Programs.standard())
            .defaultSchema(rootSchema)
            .operatorTable(opTable)
            .build();
    Planner planner = Frameworks.getPlanner(config);

    // Create data context with planner and root schema
    dataContext = new SojDataContext(planner, rootSchema);
  }

  protected abstract void prepareSql(String sql);

  @Override
  public abstract int getBotFlag(UbiEvent event);
}
