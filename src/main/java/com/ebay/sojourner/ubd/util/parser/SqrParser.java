package com.ebay.sojourner.ubd.util.parser;


import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.util.sojlib.RegexReplace;
import com.ebay.sojourner.ubd.util.sojlib.SOJURLDecodeEscape;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class SqrParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
    private static final Logger log = Logger.getLogger(SqrParser.class);
    private static final String S_QR_TAG = "sQr";
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) {
        Map<String, String> map = new HashMap<>();

        map.putAll(rawEvent.getSojA());
        map.putAll(rawEvent.getSojK());
        map.putAll(rawEvent.getSojC());
        String sqr =null;
        if (StringUtils.isNotBlank(map.get(S_QR_TAG))) {
            sqr=map.get(S_QR_TAG);
        }
        try {
            if (StringUtils.isNotBlank(sqr)) {
                try{
                    String sqrUtf8 = URLDecoder.decode(sqr, "UTF-8");
                    if(sqrUtf8.length() <= 4096){
                        ubiEvent.setSqr(URLDecoder.decode(sqr,"UTF-8"));
                    } else {
                        ubiEvent.setSqr(URLDecoder.decode(sqr,"UTF-8").substring(0,4096));
                    }

                }catch (UnsupportedEncodingException e){
                    String replacedChar = RegexReplace.replace(sqr.replace('+', ' '),".%[^0-9a-fA-F].?.", "", 1, 0, 'i');

                    String replacedCharUtf8 = SOJURLDecodeEscape.decodeEscapes(replacedChar, '%');
                    if(replacedCharUtf8.length() <= 4096){
                        ubiEvent.setSqr(SOJURLDecodeEscape.decodeEscapes(replacedChar, '%'));
                    }else {
                        ubiEvent.setSqr(SOJURLDecodeEscape.decodeEscapes(replacedChar, '%').substring(0,4096));
                    }

                }
            }
        } catch (Exception e) {
            log.debug("Parsing Sqr failed, format incorrect: " + sqr);
        }
    }

    @Override
    public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        // nothing to do
    }
}
