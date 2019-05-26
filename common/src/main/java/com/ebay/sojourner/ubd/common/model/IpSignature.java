package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class IpSignature implements Serializable{

    private Integer botFlag;

    private String clientIp;


}
