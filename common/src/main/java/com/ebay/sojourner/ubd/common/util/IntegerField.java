package com.ebay.sojourner.ubd.common.util;

import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class IntegerField {

  public IntegerField() {
    super();
  }

  public static String parse(String field) {
    // if source is null, returns null
    if (field == null) {
      return null;
    }
    // if source is all non-num, returns 0, verified in Fermat
    if (Pattern.matches("\\D+", field)) {
      return "0";
    }
    field = field.trim();
    // if source is a number, return field
    if (Pattern.matches("\\d+", field)) {
      return field;
    }

    field = RegexReplace.replace(field, "[a-zA-Z_} {~!@#$%^&*)(+=]+", "", 1, 0, 'i');
    if (StringUtils.isBlank(field)) {
      return "0";
    }
    String destField = null;

    if (field.charAt(0) == '-') {
      destField = "-" + RegexReplace.replace(field.substring(1), "(\\D)+", "", 1, 0, 'i');
    } else {
      destField = RegexReplace.replace(field, "(\\D)+", "", 1, 0, 'i');
    }

    return destField;
  }

  public static Long getIntVal(String field) {
    Long fieldIntVal = null;
    try {
      if (StringUtils.isNotBlank(field)) {
        fieldIntVal = Long.parseLong(field.trim());
      }
    } catch (NumberFormatException e) {
      log.error("Error", e);
    }
    return fieldIntVal;
  }
}
