package com.ebay.sojourner.ubd.operators.parser;

import java.util.LinkedHashSet;

/**
 * @author kofeng
 *
 * @param <Source>
 */
public abstract class RecordParser<Source, Target,Configuration, Context> implements Parser<Source, Target,Configuration, Context> {
    
    protected LinkedHashSet<FieldParser<Source, Target,Configuration,  Context>> fieldParsers = new LinkedHashSet<FieldParser<Source, Target,Configuration,  Context>>();

    public abstract void initFieldParsers();
    
    public void init(Configuration configuration, Context context) throws Exception {
        for (FieldParser<Source, Target, Configuration, Context> parser : fieldParsers) {
            parser.init(configuration,context);
        }
    }
    
    
    public void parse(Source source, Target target) throws Exception {
        for (FieldParser<Source, Target,Configuration,  Context> parser : fieldParsers) {
            parser.parse(source, target);
        }
    }

    public void addFieldParser(FieldParser<Source, Target,Configuration,  Context> parser) {
        if (!fieldParsers.contains(parser)) {
            fieldParsers.add(parser);
        } else {
            throw new RuntimeException("Duplicate Parser!!  ");
        }
    }
}
