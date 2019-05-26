package com.ebay.sojourner.ubd.common.rule;

public abstract  class Rule {
    public abstract void init();
    public  abstract void collect();
    public abstract int getBotFlag();
    public abstract void clear();


}
