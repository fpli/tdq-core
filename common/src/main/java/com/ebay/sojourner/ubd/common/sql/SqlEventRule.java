package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.rule.Rule;
import java.util.Locale;
import java.util.Map;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.schema.ScalarFunction;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.impl.ScalarFunctionImpl;
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

  public static SqlEventRule of(String sql) {
    return new SqlCompilerEventRule(sql);
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
    final FrameworkConfig config =
        Frameworks.newConfigBuilder()
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
