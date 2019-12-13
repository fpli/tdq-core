package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class IpSignature implements Signature,Serializable{

    private Map<String,Set<Integer>> ipBotSignature = new HashMap<>();

    public IpSignature() {
    }
}
