package com.ebay.sojourner.ubd.common.model;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class GuidAttribute implements Attribute<UbiSession>, Serializable {

    private String guid;
    @Getter
    private int absEventCount=0;

    public void feed(UbiSession ubiSession, int botFlag, boolean isNeeded) {
        if(isNeeded)
            absEventCount+=ubiSession.getAbsEventCnt();
    }

    public GuidAttribute() {
    }

    @Override
    public void revert(UbiSession ubiSession, int botFlag) {

    }

    public void clear()
    {
        guid=null;
        absEventCount=0;
    }

    @Override
    public void clear(int botFlag) {
        guid=null;
        absEventCount=0;
    }
}
