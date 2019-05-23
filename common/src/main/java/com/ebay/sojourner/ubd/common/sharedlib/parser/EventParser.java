package com.ebay.sojourner.ubd.common.sharedlib.parser;


import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;

public class EventParser extends RecordParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
    
    public EventParser(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        initFieldParsers();
        init(conf,runtimeContext);
    }

    @Override
    public void initFieldParsers() {
        if (this.fieldParsers.isEmpty()) {
            // Keep insert order to reuse existed field normalization result
            addFieldParser(new TimestampParser());
            addFieldParser(new CiidParser());
            addFieldParser(new ClickIdParser());
            addFieldParser(new CookiesParser());
            addFieldParser(new FlagsParser());
            addFieldParser(new ItemIdParser());
            addFieldParser(new PageIdParser());
            addFieldParser(new RdtParser());
            addFieldParser(new RefererParser());
            addFieldParser(new ReferrerHashParser());
            addFieldParser(new ReguParser());
            addFieldParser(new ServerParser());
            addFieldParser(new SiidParser());
            addFieldParser(new SiteIdParser());
            addFieldParser(new SqrParser());
            addFieldParser(new UserIdParser());
            addFieldParser(new AgentInfoParser());
            addFieldParser(new ClientIPParser());
            addFieldParser(new IdentityParser());
            addFieldParser(new IFrameParser());
            // Finding Flag should after Page Id
            addFieldParser(new FindingFlagParser());
            addFieldParser(new StaticPageTypeParser());
            //add appid for iphone data filter
            addFieldParser(new AppIdParser());
            // new metrics
            addFieldParser(new CobrandParser());
            addFieldParser(new PartialValidPageParser());
        }        
    }
}
