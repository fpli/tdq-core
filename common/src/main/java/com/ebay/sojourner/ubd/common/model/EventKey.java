/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.ebay.sojourner.ubd.common.model;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class EventKey extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"EventKey\",\"namespace\":\"com.ebay.sojourner.ubd.common.model\",\"fields\":[{\"name\":\"guid\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"]},{\"name\":\"sessionSkey\",\"type\":[\"long\",\"null\"]},{\"name\":\"seqNum\",\"type\":[\"int\",\"null\"]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.String guid;
  @Deprecated public java.lang.Long sessionSkey;
  @Deprecated public java.lang.Integer seqNum;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public EventKey() {}

  /**
   * All-args constructor.
   */
  public EventKey(java.lang.String guid, java.lang.Long sessionSkey, java.lang.Integer seqNum) {
    this.guid = guid;
    this.sessionSkey = sessionSkey;
    this.seqNum = seqNum;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return guid;
    case 1: return sessionSkey;
    case 2: return seqNum;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: guid = (java.lang.String)value$; break;
    case 1: sessionSkey = (java.lang.Long)value$; break;
    case 2: seqNum = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'guid' field.
   */
  public java.lang.String getGuid() {
    return guid;
  }

  /**
   * Sets the value of the 'guid' field.
   * @param value the value to set.
   */
  public void setGuid(java.lang.String value) {
    this.guid = value;
  }

  /**
   * Gets the value of the 'sessionSkey' field.
   */
  public java.lang.Long getSessionSkey() {
    return sessionSkey;
  }

  /**
   * Sets the value of the 'sessionSkey' field.
   * @param value the value to set.
   */
  public void setSessionSkey(java.lang.Long value) {
    this.sessionSkey = value;
  }

  /**
   * Gets the value of the 'seqNum' field.
   */
  public java.lang.Integer getSeqNum() {
    return seqNum;
  }

  /**
   * Sets the value of the 'seqNum' field.
   * @param value the value to set.
   */
  public void setSeqNum(java.lang.Integer value) {
    this.seqNum = value;
  }

  /** Creates a new EventKey RecordBuilder */
  public static com.ebay.sojourner.ubd.common.model.EventKey.Builder newBuilder() {
    return new com.ebay.sojourner.ubd.common.model.EventKey.Builder();
  }
  
  /** Creates a new EventKey RecordBuilder by copying an existing Builder */
  public static com.ebay.sojourner.ubd.common.model.EventKey.Builder newBuilder(com.ebay.sojourner.ubd.common.model.EventKey.Builder other) {
    return new com.ebay.sojourner.ubd.common.model.EventKey.Builder(other);
  }
  
  /** Creates a new EventKey RecordBuilder by copying an existing EventKey instance */
  public static com.ebay.sojourner.ubd.common.model.EventKey.Builder newBuilder(com.ebay.sojourner.ubd.common.model.EventKey other) {
    return new com.ebay.sojourner.ubd.common.model.EventKey.Builder(other);
  }
  
  /**
   * RecordBuilder for EventKey instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<EventKey>
    implements org.apache.avro.data.RecordBuilder<EventKey> {

    private java.lang.String guid;
    private java.lang.Long sessionSkey;
    private java.lang.Integer seqNum;

    /** Creates a new Builder */
    private Builder() {
      super(com.ebay.sojourner.ubd.common.model.EventKey.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.ebay.sojourner.ubd.common.model.EventKey.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.guid)) {
        this.guid = data().deepCopy(fields()[0].schema(), other.guid);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.sessionSkey)) {
        this.sessionSkey = data().deepCopy(fields()[1].schema(), other.sessionSkey);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.seqNum)) {
        this.seqNum = data().deepCopy(fields()[2].schema(), other.seqNum);
        fieldSetFlags()[2] = true;
      }
    }
    
    /** Creates a Builder by copying an existing EventKey instance */
    private Builder(com.ebay.sojourner.ubd.common.model.EventKey other) {
            super(com.ebay.sojourner.ubd.common.model.EventKey.SCHEMA$);
      if (isValidValue(fields()[0], other.guid)) {
        this.guid = data().deepCopy(fields()[0].schema(), other.guid);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.sessionSkey)) {
        this.sessionSkey = data().deepCopy(fields()[1].schema(), other.sessionSkey);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.seqNum)) {
        this.seqNum = data().deepCopy(fields()[2].schema(), other.seqNum);
        fieldSetFlags()[2] = true;
      }
    }

    /** Gets the value of the 'guid' field */
    public java.lang.String getGuid() {
      return guid;
    }
    
    /** Sets the value of the 'guid' field */
    public com.ebay.sojourner.ubd.common.model.EventKey.Builder setGuid(java.lang.String value) {
      validate(fields()[0], value);
      this.guid = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'guid' field has been set */
    public boolean hasGuid() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'guid' field */
    public com.ebay.sojourner.ubd.common.model.EventKey.Builder clearGuid() {
      guid = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'sessionSkey' field */
    public java.lang.Long getSessionSkey() {
      return sessionSkey;
    }
    
    /** Sets the value of the 'sessionSkey' field */
    public com.ebay.sojourner.ubd.common.model.EventKey.Builder setSessionSkey(java.lang.Long value) {
      validate(fields()[1], value);
      this.sessionSkey = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'sessionSkey' field has been set */
    public boolean hasSessionSkey() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'sessionSkey' field */
    public com.ebay.sojourner.ubd.common.model.EventKey.Builder clearSessionSkey() {
      sessionSkey = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'seqNum' field */
    public java.lang.Integer getSeqNum() {
      return seqNum;
    }
    
    /** Sets the value of the 'seqNum' field */
    public com.ebay.sojourner.ubd.common.model.EventKey.Builder setSeqNum(java.lang.Integer value) {
      validate(fields()[2], value);
      this.seqNum = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'seqNum' field has been set */
    public boolean hasSeqNum() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'seqNum' field */
    public com.ebay.sojourner.ubd.common.model.EventKey.Builder clearSeqNum() {
      seqNum = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    public EventKey build() {
      try {
        EventKey record = new EventKey();
        record.guid = fieldSetFlags()[0] ? this.guid : (java.lang.String) defaultValue(fields()[0]);
        record.sessionSkey = fieldSetFlags()[1] ? this.sessionSkey : (java.lang.Long) defaultValue(fields()[1]);
        record.seqNum = fieldSetFlags()[2] ? this.seqNum : (java.lang.Integer) defaultValue(fields()[2]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
