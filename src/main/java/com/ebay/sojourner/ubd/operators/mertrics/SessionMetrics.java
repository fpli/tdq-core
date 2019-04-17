package com.ebay.sojourner.ubd.operators.mertrics;


import com.ebay.sojourner.ubd.model.UbiEvent;

public class SessionMetrics extends RecordMetrics<UbiEvent, UbiEvent> {

    public SessionMetrics()  {
        initFieldMetrics();
    }
    
    @Override
    public void initFieldMetrics() {

        addFieldMetrics(new SingleClickFlagMetrics());
        addFieldMetrics(new AgentIPMetrics());
        addFieldMetrics(new AgentStringMetrics());
    }
}
