package com.ebay.sojourner.ubd.common.rule;

import java.util.Set;

public interface AttributeRule<T,Y> extends Rule<T> {


    Set<Integer> getBotFlag(T t, Y y);
}
