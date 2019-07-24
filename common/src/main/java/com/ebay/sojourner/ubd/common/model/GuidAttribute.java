package com.ebay.sojourner.ubd.common.model;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class GuidAttribute implements Attribute<UbiSession>, Serializable {
    private String clientIp;
    @Getter
    private int absEventCount=0;

    public void feed(UbiSession ubiSession) {
        absEventCount+=ubiSession.getAbsEventCnt();
    }
}
