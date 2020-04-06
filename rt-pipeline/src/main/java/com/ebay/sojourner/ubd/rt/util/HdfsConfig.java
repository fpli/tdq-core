package com.ebay.sojourner.ubd.rt.util;

import java.util.List;
import lombok.Data;

@Data
public class HdfsConfig {

  private String sinkPath;
  private String lkpPath;
  private List<String> lkpFileName;
}
