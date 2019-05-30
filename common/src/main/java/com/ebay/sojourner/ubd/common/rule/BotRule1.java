package com.ebay.sojourner.ubd.common.rule;


import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElement;

import java.util.Map;
import java.util.regex.Pattern;

public class BotRule1 implements Rule {
//    private int botFlag = 0;
    private static final Pattern pattern = Pattern.compile(".*bot[^a-z0-9_-].*|.*bot$|.*spider.*|.*crawl.*|.*ktxn.*", Pattern.CASE_INSENSITIVE);
//    private boolean findValidEvent = false;
//    private boolean first = true;

    @Override
    public  void init()
    {


    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
        int botFlag =0;
        if (checkFlag(sessionAccumulator.getUbiSession().getBotCondition().get(this.getClass().getName()),1)) {
            botFlag = detectSpiderAgent(event);
            if (isValidEvent(event)) {
             //   findValidEvent = true;
                setFlag(sessionAccumulator.getUbiSession().getBotCondition(),this.getClass().getName(),2);
            }
            setFlag(sessionAccumulator.getUbiSession().getBotCondition(),this.getClass().getName(),1);
        } else if (!checkFlag(sessionAccumulator.getUbiSession().getBotCondition().get(this.getClass().getName()),2) && isValidEvent(event)) {
            if (detectSpiderAgent(event) != 0) {
                botFlag = BotRules.SPIDER_BOT_FLAG;
            }
            setFlag(sessionAccumulator.getUbiSession().getBotCondition(),this.getClass().getName(),2);
        }
        setBotrules(sessionAccumulator.getUbiSession().getBotsingunature(),this.getClass().getName(),botFlag);

    }



    private boolean checkFlag(Integer value,Integer position)
    {
        if(value==null)
        {
            return false;
        }
        else
        {
            return (value >>>position)%2==0;
        }

    }
    private void setFlag(Map<String,Integer> botConditions, String keyName, Integer value)
    {
        if(keyName!=null)
        {
            Integer keyValue = botConditions.get(keyName);
            botConditions.put(keyName,keyValue+value);
        }


    }
    private void setBotrules(Map<String,Integer> botrules, String keyName, Integer value)
    {
        if(keyName!=null)
        {

            botrules.put(keyName,value);
        }


    }

    private int detectSpiderAgent(UbiEvent event) {
        String agentInfo = event.getAgentInfo();
        if (agentInfo != null && pattern.matcher(agentInfo).matches()) {
            return BotRules.SPIDER_BOT_FLAG;
        } else {
            return BotRules.NON_BOT_FLAG;
        }
    }

    boolean isValidEvent(UbiEvent event) {
        return (event.getIframe() != null && event.getRdt() != null && event.getIframe() == 0 && event.getRdt() == 0);
    }

    @Override
    public void reset() {

    }

    @Override
    public int getBotFlag() {
        return 1;
    }
    @Override
    public  int getBotFlag(UbiSession ubiSession)
    {
        return 1;
    }

}
