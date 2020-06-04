package com.ebay.sojourner.dsl.sql;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.rule.Rule;
import java.util.Locale;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.schema.ScalarFunction;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.impl.ScalarFunctionImpl;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParser.ConfigBuilder;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.tools.Programs;
import org.apache.log4j.Logger;

@Data
@EqualsAndHashCode(of = {"ruleId"}, callSuper = false)
public abstract class SqlEventRule implements Rule<UbiEvent> {

  protected static final Logger LOGGER = Logger.getLogger(SqlEventRule.class);
  protected SojReflectiveDataSource dataSource;
  protected SojDataContext dataContext;
  private String sql;
  private long ruleId;
  private int version;
  private String category;

  public SqlEventRule(String sql) {
    this.sql = sql;
    prepareDataContext();
    prepareSql(sql);
  }

  public SqlEventRule(String sql, long ruleId, int version, String category) {
    this.sql = sql;
    this.ruleId = ruleId;
    this.version = version;
    this.category = category;
    prepareDataContext();
    prepareSql(sql);
  }

  public static SqlEventRule of(String sql) {
    return new SqlCompilerEventRule(sql);
  }

  public static SqlEventRule of(String sql, long ruleId, int version, String category) {
    return new SqlCompilerEventRule(sql, ruleId, version, category);
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
    ConfigBuilder parserConfigBuilder = SqlParser.configBuilder().setCaseSensitive(false);
    final FrameworkConfig config =
        Frameworks.newConfigBuilder()
            .parserConfig(parserConfigBuilder.build())
            .programs(Programs.standard())
            .defaultSchema(rootSchema)
            .build();
    Planner planner = Frameworks.getPlanner(config);

    // Create data context with planner and root schema
    dataContext = new SojDataContext(planner, rootSchema);
  }

  protected abstract void prepareSql(String sql);

  @Override
  public abstract int getBotFlag(UbiEvent event);
}
