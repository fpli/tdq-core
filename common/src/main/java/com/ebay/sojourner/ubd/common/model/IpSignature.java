package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class IpSignature implements Signature,Serializable{

    private Set<Integer> botFlag = new HashSet<Integer>();

    private String clientIp;

    public IpSignature() {
    }
}
