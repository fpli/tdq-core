package com.ebay.sojourner.ubd.rt.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ebay.sojourner.ubd.common.model.ClientData;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.RheosHeader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RawEventGenerator {

    public static List<RawEvent> getRawEventList( String fileName ) throws IOException {

        InputStream resourceAsStream = RawEventGenerator.class.getResourceAsStream(fileName);
        InputStreamReader ir = new InputStreamReader(resourceAsStream);
        BufferedReader br = new BufferedReader(ir);
        String line = null;
        List<RawEvent> rawEvents = new ArrayList<>();
        RawEvent rawEvent = null;
        while ((line = br.readLine()) != null) {
            rawEvent = new RawEvent();
            JSONObject obj = JSON.parseObject(line);
            RheosHeader rheosHeader = obj.getObject("rheosHeader", RheosHeader.class);
            Map<String, String> sojA = obj.getObject("sojA", Map.class);
            Map<String, String> sojC = obj.getObject("sojC", Map.class);
            Map<String, String> sojK = obj.getObject("sojK", Map.class);
            ClientData clientData = obj.getObject("clientData", ClientData.class);
            rawEvent.setClientData(clientData);
            rawEvent.setRheosHeader(rheosHeader);
            rawEvent.setSojA(sojA);
            rawEvent.setSojC(sojC);
            rawEvent.setSojK(sojK);
            rawEvents.add(rawEvent);
        }

        return rawEvents;
    }

    public static void main( String[] args ) throws IOException {

        System.out.println(getRawEventList("/SourceData").size());
    }
}
