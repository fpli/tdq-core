package com.ebay.sojourner.dsl.sql;

import com.ebay.sojourner.common.model.rule.RuleDefinition;
import com.google.common.collect.ImmutableCollection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.calcite.adapter.enumerable.EnumerableConvention;
import org.apache.calcite.adapter.enumerable.EnumerableInterpretable;
import org.apache.calcite.adapter.enumerable.EnumerableRel;
import org.apache.calcite.adapter.enumerable.EnumerableRel.Prefer;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.runtime.Bindable;
import org.apache.calcite.schema.ScalarFunction;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.impl.ScalarFunctionImpl;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParser.ConfigBuilder;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.tools.Programs;

public abstract class AbstractSQLRule<T, R, OUT> implements SQLRule<T, OUT> {

  protected final String ROOT_SCHEMA = "soj";

  protected RuleDefinition rule;
  protected SojDataContext dataContext;
  protected Bindable<OUT> bindable;
  protected Enumerable<OUT> enumerable;
  protected SingleRecordDataSource<R> dataSource;

  public AbstractSQLRule(RuleDefinition ruleDefinition) {
    this.rule = ruleDefinition;

    // Create root schema, add data source and functions
    SchemaPlus rootSchema = Frameworks.createRootSchema(true);
    rootSchema.setCacheEnabled(false);

    dataSource = this.getDataSource();
    // Add data source
    rootSchema.add(ROOT_SCHEMA, new ReflectiveSchema(dataSource));

    // Add udf
    Class<UdfManager> udfManager = UdfManager.class;
    ImmutableCollection<Entry<String, ScalarFunction>> entries =
        ScalarFunctionImpl.createAll(udfManager).entries();
    for (Map.Entry<String, ScalarFunction> entry : entries) {
      String name = entry.getKey();
      rootSchema.add(name, entry.getValue());
    }

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

    this.compile();
  }

  protected abstract SingleRecordDataSource<R> getDataSource();

  @Override
  public void compile() {
    try {
      Planner planner = dataContext.getPlanner();
      SqlNode parseTree = planner.parse(rule.getContent());
      SqlNode validated = planner.validate(parseTree);
      RelNode relTree = planner.rel(validated).rel;
      RelTraitSet traitSet = planner.getEmptyTraitSet().replace(EnumerableConvention.INSTANCE);
      RelNode optimized = planner.transform(0, traitSet, relTree);
      Map<String, Object> internalParameters = new LinkedHashMap<>();
      Prefer prefer = EnumerableRel.Prefer.ARRAY;

      bindable = EnumerableInterpretable
          .toBindable(internalParameters, null, (EnumerableRel) optimized, prefer);

      enumerable = bindable.bind(dataContext);
    } catch (Exception e) {
      throw new RuntimeException("Failed to compile SQL to class: " + rule.getContent(), e);
    }
  }
}
