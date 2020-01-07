package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class LoadRawEventAndExpect {
    private static final Logger logger = Logger.getLogger(LoadRawEventAndExpect.class);
    private static RawEvent rawEvent = null;
    private static Object expectResult = null;
    private static HashMap<RawEvent, Object> hashMap = null;

    public static HashMap<RawEvent, Object> getRawEventAndExpect(HashMap map, String parser, String caseItem) {

        if (StringUtils.isBlank(parser) & StringUtils.isBlank(caseItem)) {
            logger.error("the parser or caseItem is blank!!!");
        }

        if (map.isEmpty()) {
            logger.error("the map is empty!!!");
        }

        if(hashMap == null){
            hashMap = new HashMap<>();
        }

        HashMap<String, Object> map1 = TypeTransUtil.ObjectToHashMap(map.get(parser));
        HashMap<String, Object> map2 = TypeTransUtil.ObjectToHashMap(map1.get(caseItem));

        rawEvent = InitRawEvent.initRawEvent(map2,parser);
        expectResult = map2.get(ParserConstants.EXPECTRESULT);

        hashMap.put(rawEvent,expectResult);

        return hashMap;
    }
}