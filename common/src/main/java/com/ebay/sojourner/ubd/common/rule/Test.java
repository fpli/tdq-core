package com.ebay.sojourner.ubd.common.rule;

public class Test {
    public static void main(String[] args) {
        int icfType = 0;
        String string = Long.toBinaryString(Long.parseLong("1200", 16));
        String[] splited = string.split("");

        for (int i=0;i<splited.length;i++) {
            if ((splited[i].equals("1")) && ((splited.length - i) == 13)) {
                icfType = 13;
                break;
            } else if ((splited[i].equals("1")) && ((splited.length - i) == 2)) {

            }
        }
        System.out.println(icfType);
    }
}
