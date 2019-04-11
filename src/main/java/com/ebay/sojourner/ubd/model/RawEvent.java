package com.ebay.sojourner.ubd.model;

import java.util.Map;

public class RawEvent {

    private Map<String, String> rheosHeader;
    private Map<String, String> sojA;
    private Map<String, String> sojK;
    private Map<String, String> sojC;
    private Map<String, String> clientData;

    public RawEvent(
            Map<String, String> rheosHeader,
            Map<String, String> sojA,
            Map<String, String> sojK,
            Map<String, String> sojC,
            Map<String, String> clientData) {
        this.rheosHeader = rheosHeader;
        this.sojA = sojA;
        this.sojK = sojK;
        this.sojC = sojC;
        this.clientData = clientData;
    }

    public Map<String, String> getRheosHeader() {
        return rheosHeader;
    }

    public void setRheosHeader(Map<String, String> rheosHeader) {
        this.rheosHeader = rheosHeader;
    }

    public Map<String, String> getSojA() {
        return sojA;
    }

    public void setSojA(Map<String, String> sojA) {
        this.sojA = sojA;
    }

    public Map<String, String> getSojK() {
        return sojK;
    }

    public void setSojK(Map<String, String> sojK) {
        this.sojK = sojK;
    }

    public Map<String, String> getSojC() {
        return sojC;
    }

    public void setSojC(Map<String, String> sojC) {
        this.sojC = sojC;
    }

    public Map<String, String> getClientData() {
        return clientData;
    }

    public void setClientData(Map<String, String> clientData) {
        this.clientData = clientData;
    }
}
