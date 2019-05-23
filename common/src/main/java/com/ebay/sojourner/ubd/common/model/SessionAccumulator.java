package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SessionAccumulator implements Serializable{
    private UbiEvent ubiEvent;
    private UbiSession ubiSession;

    public SessionAccumulator()
    {
        this.ubiEvent=new UbiEvent();
        this.ubiSession = new UbiSession();
    }

}