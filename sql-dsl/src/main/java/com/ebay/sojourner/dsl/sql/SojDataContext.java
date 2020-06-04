package com.ebay.sojourner.dsl.sql;

import org.apache.calcite.DataContext;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.tools.Planner;

public class SojDataContext implements DataContext {

  private final Planner planner;
  private SchemaPlus rootSchema;

  SojDataContext(Planner planner, SchemaPlus rootSchema) {
    this.planner = planner;
    this.rootSchema = rootSchema;
  }

  public Planner getPlanner() {
    return this.planner;
  }

  public SchemaPlus getRootSchema() {
    return rootSchema;
  }

  public JavaTypeFactory getTypeFactory() {
    return (JavaTypeFactory) planner.getTypeFactory();
  }

  public QueryProvider getQueryProvider() {
    return null;
  }

  public Object get(String name) {
    return null;
  }
}
