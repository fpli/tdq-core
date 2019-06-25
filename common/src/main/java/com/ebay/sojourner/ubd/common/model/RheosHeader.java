/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.ebay.sojourner.ubd.common.model;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class RheosHeader extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"RheosHeader\",\"namespace\":\"com.ebay.sojourner.ubd.common.model\",\"fields\":[{\"name\":\"eventCreateTimestamp\",\"type\":\"long\"},{\"name\":\"eventSentTimestamp\",\"type\":\"long\"},{\"name\":\"schemaId\",\"type\":\"int\"},{\"name\":\"eventId\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}]},{\"name\":\"producerId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public long eventCreateTimestamp;
  @Deprecated public long eventSentTimestamp;
  @Deprecated public int schemaId;
  @Deprecated public java.lang.String eventId;
  @Deprecated public java.lang.String producerId;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public RheosHeader() {}

  /**
   * All-args constructor.
   */
  public RheosHeader(java.lang.Long eventCreateTimestamp, java.lang.Long eventSentTimestamp, java.lang.Integer schemaId, java.lang.String eventId, java.lang.String producerId) {
    this.eventCreateTimestamp = eventCreateTimestamp;
    this.eventSentTimestamp = eventSentTimestamp;
    this.schemaId = schemaId;
    this.eventId = eventId;
    this.producerId = producerId;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return eventCreateTimestamp;
    case 1: return eventSentTimestamp;
    case 2: return schemaId;
    case 3: return eventId;
    case 4: return producerId;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: eventCreateTimestamp = (java.lang.Long)value$; break;
    case 1: eventSentTimestamp = (java.lang.Long)value$; break;
    case 2: schemaId = (java.lang.Integer)value$; break;
    case 3: eventId = (java.lang.String)value$; break;
    case 4: producerId = (java.lang.String)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'eventCreateTimestamp' field.
   */
  public java.lang.Long getEventCreateTimestamp() {
    return eventCreateTimestamp;
  }

  /**
   * Sets the value of the 'eventCreateTimestamp' field.
   * @param value the value to set.
   */
  public void setEventCreateTimestamp(java.lang.Long value) {
    this.eventCreateTimestamp = value;
  }

  /**
   * Gets the value of the 'eventSentTimestamp' field.
   */
  public java.lang.Long getEventSentTimestamp() {
    return eventSentTimestamp;
  }

  /**
   * Sets the value of the 'eventSentTimestamp' field.
   * @param value the value to set.
   */
  public void setEventSentTimestamp(java.lang.Long value) {
    this.eventSentTimestamp = value;
  }

  /**
   * Gets the value of the 'schemaId' field.
   */
  public java.lang.Integer getSchemaId() {
    return schemaId;
  }

  /**
   * Sets the value of the 'schemaId' field.
   * @param value the value to set.
   */
  public void setSchemaId(java.lang.Integer value) {
    this.schemaId = value;
  }

  /**
   * Gets the value of the 'eventId' field.
   */
  public java.lang.String getEventId() {
    return eventId;
  }

  /**
   * Sets the value of the 'eventId' field.
   * @param value the value to set.
   */
  public void setEventId(java.lang.String value) {
    this.eventId = value;
  }

  /**
   * Gets the value of the 'producerId' field.
   */
  public java.lang.String getProducerId() {
    return producerId;
  }

  /**
   * Sets the value of the 'producerId' field.
   * @param value the value to set.
   */
  public void setProducerId(java.lang.String value) {
    this.producerId = value;
  }

  /** Creates a new RheosHeader RecordBuilder */
  public static com.ebay.sojourner.ubd.common.model.RheosHeader.Builder newBuilder() {
    return new com.ebay.sojourner.ubd.common.model.RheosHeader.Builder();
  }
  
  /** Creates a new RheosHeader RecordBuilder by copying an existing Builder */
  public static com.ebay.sojourner.ubd.common.model.RheosHeader.Builder newBuilder(com.ebay.sojourner.ubd.common.model.RheosHeader.Builder other) {
    return new com.ebay.sojourner.ubd.common.model.RheosHeader.Builder(other);
  }
  
  /** Creates a new RheosHeader RecordBuilder by copying an existing RheosHeader instance */
  public static com.ebay.sojourner.ubd.common.model.RheosHeader.Builder newBuilder(com.ebay.sojourner.ubd.common.model.RheosHeader other) {
    return new com.ebay.sojourner.ubd.common.model.RheosHeader.Builder(other);
  }
  
  /**
   * RecordBuilder for RheosHeader instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<RheosHeader>
    implements org.apache.avro.data.RecordBuilder<RheosHeader> {

    private long eventCreateTimestamp;
    private long eventSentTimestamp;
    private int schemaId;
    private java.lang.String eventId;
    private java.lang.String producerId;

    /** Creates a new Builder */
    private Builder() {
      super(com.ebay.sojourner.ubd.common.model.RheosHeader.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.ebay.sojourner.ubd.common.model.RheosHeader.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.eventCreateTimestamp)) {
        this.eventCreateTimestamp = data().deepCopy(fields()[0].schema(), other.eventCreateTimestamp);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.eventSentTimestamp)) {
        this.eventSentTimestamp = data().deepCopy(fields()[1].schema(), other.eventSentTimestamp);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.schemaId)) {
        this.schemaId = data().deepCopy(fields()[2].schema(), other.schemaId);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.eventId)) {
        this.eventId = data().deepCopy(fields()[3].schema(), other.eventId);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.producerId)) {
        this.producerId = data().deepCopy(fields()[4].schema(), other.producerId);
        fieldSetFlags()[4] = true;
      }
    }
    
    /** Creates a Builder by copying an existing RheosHeader instance */
    private Builder(com.ebay.sojourner.ubd.common.model.RheosHeader other) {
            super(com.ebay.sojourner.ubd.common.model.RheosHeader.SCHEMA$);
      if (isValidValue(fields()[0], other.eventCreateTimestamp)) {
        this.eventCreateTimestamp = data().deepCopy(fields()[0].schema(), other.eventCreateTimestamp);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.eventSentTimestamp)) {
        this.eventSentTimestamp = data().deepCopy(fields()[1].schema(), other.eventSentTimestamp);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.schemaId)) {
        this.schemaId = data().deepCopy(fields()[2].schema(), other.schemaId);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.eventId)) {
        this.eventId = data().deepCopy(fields()[3].schema(), other.eventId);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.producerId)) {
        this.producerId = data().deepCopy(fields()[4].schema(), other.producerId);
        fieldSetFlags()[4] = true;
      }
    }

    /** Gets the value of the 'eventCreateTimestamp' field */
    public java.lang.Long getEventCreateTimestamp() {
      return eventCreateTimestamp;
    }
    
    /** Sets the value of the 'eventCreateTimestamp' field */
    public com.ebay.sojourner.ubd.common.model.RheosHeader.Builder setEventCreateTimestamp(long value) {
      validate(fields()[0], value);
      this.eventCreateTimestamp = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'eventCreateTimestamp' field has been set */
    public boolean hasEventCreateTimestamp() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'eventCreateTimestamp' field */
    public com.ebay.sojourner.ubd.common.model.RheosHeader.Builder clearEventCreateTimestamp() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'eventSentTimestamp' field */
    public java.lang.Long getEventSentTimestamp() {
      return eventSentTimestamp;
    }
    
    /** Sets the value of the 'eventSentTimestamp' field */
    public com.ebay.sojourner.ubd.common.model.RheosHeader.Builder setEventSentTimestamp(long value) {
      validate(fields()[1], value);
      this.eventSentTimestamp = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'eventSentTimestamp' field has been set */
    public boolean hasEventSentTimestamp() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'eventSentTimestamp' field */
    public com.ebay.sojourner.ubd.common.model.RheosHeader.Builder clearEventSentTimestamp() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'schemaId' field */
    public java.lang.Integer getSchemaId() {
      return schemaId;
    }
    
    /** Sets the value of the 'schemaId' field */
    public com.ebay.sojourner.ubd.common.model.RheosHeader.Builder setSchemaId(int value) {
      validate(fields()[2], value);
      this.schemaId = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'schemaId' field has been set */
    public boolean hasSchemaId() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'schemaId' field */
    public com.ebay.sojourner.ubd.common.model.RheosHeader.Builder clearSchemaId() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'eventId' field */
    public java.lang.String getEventId() {
      return eventId;
    }
    
    /** Sets the value of the 'eventId' field */
    public com.ebay.sojourner.ubd.common.model.RheosHeader.Builder setEventId(java.lang.String value) {
      validate(fields()[3], value);
      this.eventId = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'eventId' field has been set */
    public boolean hasEventId() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'eventId' field */
    public com.ebay.sojourner.ubd.common.model.RheosHeader.Builder clearEventId() {
      eventId = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'producerId' field */
    public java.lang.String getProducerId() {
      return producerId;
    }
    
    /** Sets the value of the 'producerId' field */
    public com.ebay.sojourner.ubd.common.model.RheosHeader.Builder setProducerId(java.lang.String value) {
      validate(fields()[4], value);
      this.producerId = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'producerId' field has been set */
    public boolean hasProducerId() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'producerId' field */
    public com.ebay.sojourner.ubd.common.model.RheosHeader.Builder clearProducerId() {
      producerId = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    public RheosHeader build() {
      try {
        RheosHeader record = new RheosHeader();
        record.eventCreateTimestamp = fieldSetFlags()[0] ? this.eventCreateTimestamp : (java.lang.Long) defaultValue(fields()[0]);
        record.eventSentTimestamp = fieldSetFlags()[1] ? this.eventSentTimestamp : (java.lang.Long) defaultValue(fields()[1]);
        record.schemaId = fieldSetFlags()[2] ? this.schemaId : (java.lang.Integer) defaultValue(fields()[2]);
        record.eventId = fieldSetFlags()[3] ? this.eventId : (java.lang.String) defaultValue(fields()[3]);
        record.producerId = fieldSetFlags()[4] ? this.producerId : (java.lang.String) defaultValue(fields()[4]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
