package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SessionAccumulator implements Serializable{

    private UbiSession ubiSession;

    public SessionAccumulator()
    {
        this.ubiSession = new UbiSession();

    }

}