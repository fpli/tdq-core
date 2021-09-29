package com.ebay.tdq.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author juntzhang
 */
public class InstanceUtils {

  public static Object newInstance(Constructor<?> varargCtor, Object[] operands)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    return varargCtor.newInstance(operands);
  }
}
