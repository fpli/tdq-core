package com.ebay.sojourner.ubd.model;

public class RheosHeader {
    private long eventCreateTimestamp;
    private long eventSentTimestamp;
    private String producerId;
    private int schemaId;
    private String eventId;

    public RheosHeader() {

    }

    public RheosHeader(long eventCreateTimestamp, long eventSentTimestamp, String producerId,
                       int schemaId, String eventId) {
        this.eventCreateTimestamp = eventCreateTimestamp;
        this.eventSentTimestamp = eventSentTimestamp;
        this.producerId = producerId;
        this.schemaId = schemaId;
        this.eventId = eventId;
    }

    public long getEventCreateTimestamp() {
        return eventCreateTimestamp;
    }

    public void setEventCreateTimestamp(long eventCreateTimestamp) {
        this.eventCreateTimestamp = eventCreateTimestamp;
    }

    public long getEventSentTimestamp() {
        return eventSentTimestamp;
    }

    public void setEventSentTimestamp(long eventSentTimestamp) {
        this.eventSentTimestamp = eventSentTimestamp;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public int getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(int schemaId) {
        this.schemaId = schemaId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
