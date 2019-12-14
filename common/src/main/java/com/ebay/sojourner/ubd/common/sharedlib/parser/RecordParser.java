package com.ebay.sojourner.ubd.common.sharedlib.parser;

import java.util.LinkedHashSet;

/**
 * @author kofeng
 *
 * @param <Source>
 */
public abstract class RecordParser<Source, Target> implements Parser<Source, Target> {
    
    protected LinkedHashSet<FieldParser<Source, Target>> fieldParsers = new LinkedHashSet<FieldParser<Source, Target>>();

    public abstract void initFieldParsers();
    
    public void init() throws Exception {
        for (FieldParser<Source, Target> parser : fieldParsers) {
            parser.init();
        }
    }

//    public void parse(Source source, Target target) throws Exception {
//
//    }

    public void parse(Source source, Target target) throws Exception {
        for (FieldParser<Source, Target> parser : fieldParsers) {
//            long start = System.nanoTime();
            parser.parse(source, target);
//            long end = System.nanoTime();
//            map.get(parser.getClass().getSimpleName()).add(end - start);
        }
    }

    public void addFieldParser(FieldParser<Source, Target> parser) {
        if (!fieldParsers.contains(parser)) {
            fieldParsers.add(parser);
        } else {
            throw new RuntimeException("Duplicate Parser!!  ");
        }
    }
}
