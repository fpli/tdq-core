package com.ebay.sojourner.ubd.common.parsertest;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.CobrandParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CobrandParserTest {
    private static final Logger logger = Logger.getLogger(CobrandParserTest.class);

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static CobrandParser cobrandParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeClass
    public static void initParser() {
        parser = Constants.COBRAND;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testCobrandParser1() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(123);
        caseItem = Constants.CASE1;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser2() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setAppId(1281);
        caseItem = Constants.CASE2;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser3() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setAppId(1232);
        caseItem = Constants.CASE3;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser4() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setAppId(2736);
        caseItem = Constants.CASE4;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser5() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(1605657);
        ubiEvent.setAgentInfo("");
        caseItem = Constants.CASE5;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser6() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(1605657);
        ubiEvent.setAgentInfo("HTC");
        caseItem = Constants.CASE6;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser7() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(1605657);
        ubiEvent.setAgentInfo("iPhone");
        caseItem = Constants.CASE7;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser8() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(1605657);
        ubiEvent.setAgentInfo("Windows NT 6.2");
        caseItem = Constants.CASE8;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser9() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(1605657);
        ubiEvent.setAgentInfo("Mozilla/5.0 ");
        caseItem = Constants.CASE9;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser10() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(5938);
        ubiEvent.setAgentInfo("HTC");
        caseItem = Constants.CASE10;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser11() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(5938);
        ubiEvent.setAgentInfo("Mozilla/5.0 ");
        caseItem = Constants.CASE11;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser12() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(1468662);
        ubiEvent.setAgentInfo("Mozilla/5.0 ");
        caseItem = Constants.CASE12;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser13() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(1468662);
        ubiEvent.setAgentInfo("HTC");
        caseItem = Constants.CASE13;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser14() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(1499158);
        caseItem = Constants.CASE14;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser15() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(1695768);
        ubiEvent.setAgentInfo("HTC");
        caseItem = Constants.CASE15;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser16() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(1695768);
        ubiEvent.setAgentInfo("Mozilla/5.0 ");
        caseItem = Constants.CASE16;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser17() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(123);
        ubiEvent.setApplicationPayload("pn=505");
        caseItem = Constants.CASE17;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser18() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(123);
        ubiEvent.setApplicationPayload("pn=506");
        caseItem = Constants.CASE18;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser19() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(123);
        ubiEvent.setApplicationPayload("pn=502");
        ubiEvent.setAgentInfo("HTC");
        caseItem = Constants.CASE19;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser20() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(123);
        ubiEvent.setApplicationPayload("pn=502");
        ubiEvent.setAgentInfo("Mozilla/5.0 ");
        caseItem = Constants.CASE20;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser21() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(123);
        ubiEvent.setApplicationPayload("pn=507");
        caseItem = Constants.CASE21;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }

    @Test
    public void testCobrandParser22() {
        cobrandParser = new CobrandParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(123);
        ubiEvent.setApplicationPayload("pn=ebay");
        ubiEvent.setAgentInfo("HTC");
        caseItem = Constants.CASE22;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                cobrandParser.init();
                cobrandParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
            }
        }catch (Exception e){
            logger.error("cobrand test fail!!!");
        }
    }
}
