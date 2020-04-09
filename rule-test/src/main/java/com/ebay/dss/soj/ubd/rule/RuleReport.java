package com.ebay.dss.soj.ubd.rule;

public interface RuleReport {

  void trigger(String sqlContent);

  RuleReportResult getResult(String sqlContent);
}