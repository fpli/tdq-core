package com.ebay.sojourner.ubd.rt.util;

import lombok.Data;

@Data
public class HdfsConfig {

  private String sinkParentPath;
  private String sinkEventPath;
  private String sinkSessionPath;
  private String sinkSignaturePath;
  private String sinkLateEventPath;
  private String sinkJetstreamEventPath;
  private String sinkJetstreamSessionPath;
  private String sinkIntermediateSessionPath;
}
