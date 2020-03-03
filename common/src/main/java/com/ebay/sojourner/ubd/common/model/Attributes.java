package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class Attributes {

  public boolean isCustRule;
  public boolean isWeb_ee;
  public boolean isSofe;
  public boolean isHalf;
  public boolean isEbxRef;
  public boolean isAbvar;
  public boolean isTest;
  public boolean isTuiAbtest;
  public boolean isEpr;
  public boolean isPgV;
  public boolean isM2g;

  public void setVaules(
      boolean isCustRule,
      boolean isWeb_ee,
      boolean isSofe,
      boolean isHalf,
      boolean isEbxRef,
      boolean isAbvar,
      boolean isTest,
      boolean isTuiAbtest,
      boolean isEpr,
      boolean isPgV,
      boolean isM2g) {
    this.isCustRule = isCustRule;
    this.isWeb_ee = isWeb_ee;
    this.isSofe = isSofe;
    this.isHalf = isHalf;
    this.isEbxRef = isEbxRef;
    this.isAbvar = isAbvar;
    this.isTest = isTest;
    this.isTuiAbtest = isTuiAbtest;
    this.isEpr = isEpr;
    this.isPgV = isPgV;
    this.isM2g = isM2g;
  }

  public void reset() {
    this.isCustRule = false;
    this.isWeb_ee = false;
    this.isSofe = false;
    this.isHalf = false;
    this.isEbxRef = false;
    this.isAbvar = false;
    this.isTest = false;
    this.isTuiAbtest = false;
    this.isEpr = false;
    this.isPgV = false;
    this.isM2g = false;
  }
}
