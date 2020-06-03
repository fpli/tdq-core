package com.ebay.sojourner.business.ubd.parser;

import java.util.LinkedHashSet;

/**
 * @author kofeng
 */
public abstract class RecordParser<Source, Target> implements Parser<Source, Target> {

  protected LinkedHashSet<FieldParser<Source, Target>> fieldParsers = new LinkedHashSet<>();

  public abstract void initFieldParsers();

  public void init() throws Exception {
    for (FieldParser<Source, Target> parser : fieldParsers) {
      parser.init();
    }
  }

  public void parse(Source source, Target target) throws Exception {
  }

  public void addFieldParser(FieldParser<Source, Target> parser) {
    if (!fieldParsers.contains(parser)) {
      fieldParsers.add(parser);
    } else {
      throw new RuntimeException("Duplicate Parser!!  ");
    }
  }
}
