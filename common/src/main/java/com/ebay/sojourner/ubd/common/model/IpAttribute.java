package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class IpAttribute implements Attribute,Serializable {
    private String clientIp;
    private int singleClickSessionCount;
}
