/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.ebay.tdq.common.model;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class RawEvent extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 1125166674080248541L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"RawEvent\",\"namespace\":\"com.ebay.tdq.common.model\",\"fields\":[{\"name\":\"ingestTime\",\"type\":\"long\"},{\"name\":\"sojTimestamp\",\"type\":\"long\"},{\"name\":\"eventTimestamp\",\"type\":\"long\"},{\"name\":\"processTimestamp\",\"type\":\"long\"},{\"name\":\"sojA\",\"type\":[\"null\",{\"type\":\"map\",\"values\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"avro.java.string\":\"String\"}]},{\"name\":\"sojK\",\"type\":[\"null\",{\"type\":\"map\",\"values\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"avro.java.string\":\"String\"}]},{\"name\":\"sojC\",\"type\":[\"null\",{\"type\":\"map\",\"values\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"avro.java.string\":\"String\"}]},{\"name\":\"clientData\",\"type\":[\"null\",{\"type\":\"map\",\"values\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"avro.java.string\":\"String\"}]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<RawEvent> ENCODER =
      new BinaryMessageEncoder<RawEvent>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<RawEvent> DECODER =
      new BinaryMessageDecoder<RawEvent>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<RawEvent> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<RawEvent> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<RawEvent>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this RawEvent to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a RawEvent from a ByteBuffer. */
  public static RawEvent fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public long ingestTime;
  @Deprecated public long sojTimestamp;
  @Deprecated public long eventTimestamp;
  @Deprecated public long processTimestamp;
  @Deprecated public java.util.Map<java.lang.String,java.lang.String> sojA;
  @Deprecated public java.util.Map<java.lang.String,java.lang.String> sojK;
  @Deprecated public java.util.Map<java.lang.String,java.lang.String> sojC;
  @Deprecated public java.util.Map<java.lang.String,java.lang.String> clientData;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public RawEvent() {}

  /**
   * All-args constructor.
   * @param ingestTime The new value for ingestTime
   * @param sojTimestamp The new value for sojTimestamp
   * @param eventTimestamp The new value for eventTimestamp
   * @param processTimestamp The new value for processTimestamp
   * @param sojA The new value for sojA
   * @param sojK The new value for sojK
   * @param sojC The new value for sojC
   * @param clientData The new value for clientData
   */
  public RawEvent(java.lang.Long ingestTime, java.lang.Long sojTimestamp, java.lang.Long eventTimestamp, java.lang.Long processTimestamp, java.util.Map<java.lang.String,java.lang.String> sojA, java.util.Map<java.lang.String,java.lang.String> sojK, java.util.Map<java.lang.String,java.lang.String> sojC, java.util.Map<java.lang.String,java.lang.String> clientData) {
    this.ingestTime = ingestTime;
    this.sojTimestamp = sojTimestamp;
    this.eventTimestamp = eventTimestamp;
    this.processTimestamp = processTimestamp;
    this.sojA = sojA;
    this.sojK = sojK;
    this.sojC = sojC;
    this.clientData = clientData;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return ingestTime;
    case 1: return sojTimestamp;
    case 2: return eventTimestamp;
    case 3: return processTimestamp;
    case 4: return sojA;
    case 5: return sojK;
    case 6: return sojC;
    case 7: return clientData;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: ingestTime = (java.lang.Long)value$; break;
    case 1: sojTimestamp = (java.lang.Long)value$; break;
    case 2: eventTimestamp = (java.lang.Long)value$; break;
    case 3: processTimestamp = (java.lang.Long)value$; break;
    case 4: sojA = (java.util.Map<java.lang.String,java.lang.String>)value$; break;
    case 5: sojK = (java.util.Map<java.lang.String,java.lang.String>)value$; break;
    case 6: sojC = (java.util.Map<java.lang.String,java.lang.String>)value$; break;
    case 7: clientData = (java.util.Map<java.lang.String,java.lang.String>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'ingestTime' field.
   * @return The value of the 'ingestTime' field.
   */
  public java.lang.Long getIngestTime() {
    return ingestTime;
  }

  /**
   * Sets the value of the 'ingestTime' field.
   * @param value the value to set.
   */
  public void setIngestTime(java.lang.Long value) {
    this.ingestTime = value;
  }

  /**
   * Gets the value of the 'sojTimestamp' field.
   * @return The value of the 'sojTimestamp' field.
   */
  public java.lang.Long getSojTimestamp() {
    return sojTimestamp;
  }

  /**
   * Sets the value of the 'sojTimestamp' field.
   * @param value the value to set.
   */
  public void setSojTimestamp(java.lang.Long value) {
    this.sojTimestamp = value;
  }

  /**
   * Gets the value of the 'eventTimestamp' field.
   * @return The value of the 'eventTimestamp' field.
   */
  public java.lang.Long getEventTimestamp() {
    return eventTimestamp;
  }

  /**
   * Sets the value of the 'eventTimestamp' field.
   * @param value the value to set.
   */
  public void setEventTimestamp(java.lang.Long value) {
    this.eventTimestamp = value;
  }

  /**
   * Gets the value of the 'processTimestamp' field.
   * @return The value of the 'processTimestamp' field.
   */
  public java.lang.Long getProcessTimestamp() {
    return processTimestamp;
  }

  /**
   * Sets the value of the 'processTimestamp' field.
   * @param value the value to set.
   */
  public void setProcessTimestamp(java.lang.Long value) {
    this.processTimestamp = value;
  }

  /**
   * Gets the value of the 'sojA' field.
   * @return The value of the 'sojA' field.
   */
  public java.util.Map<java.lang.String,java.lang.String> getSojA() {
    return sojA;
  }

  /**
   * Sets the value of the 'sojA' field.
   * @param value the value to set.
   */
  public void setSojA(java.util.Map<java.lang.String,java.lang.String> value) {
    this.sojA = value;
  }

  /**
   * Gets the value of the 'sojK' field.
   * @return The value of the 'sojK' field.
   */
  public java.util.Map<java.lang.String,java.lang.String> getSojK() {
    return sojK;
  }

  /**
   * Sets the value of the 'sojK' field.
   * @param value the value to set.
   */
  public void setSojK(java.util.Map<java.lang.String,java.lang.String> value) {
    this.sojK = value;
  }

  /**
   * Gets the value of the 'sojC' field.
   * @return The value of the 'sojC' field.
   */
  public java.util.Map<java.lang.String,java.lang.String> getSojC() {
    return sojC;
  }

  /**
   * Sets the value of the 'sojC' field.
   * @param value the value to set.
   */
  public void setSojC(java.util.Map<java.lang.String,java.lang.String> value) {
    this.sojC = value;
  }

  /**
   * Gets the value of the 'clientData' field.
   * @return The value of the 'clientData' field.
   */
  public java.util.Map<java.lang.String,java.lang.String> getClientData() {
    return clientData;
  }

  /**
   * Sets the value of the 'clientData' field.
   * @param value the value to set.
   */
  public void setClientData(java.util.Map<java.lang.String,java.lang.String> value) {
    this.clientData = value;
  }

  /**
   * Creates a new RawEvent RecordBuilder.
   * @return A new RawEvent RecordBuilder
   */
  public static com.ebay.tdq.common.model.RawEvent.Builder newBuilder() {
    return new com.ebay.tdq.common.model.RawEvent.Builder();
  }

  /**
   * Creates a new RawEvent RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new RawEvent RecordBuilder
   */
  public static com.ebay.tdq.common.model.RawEvent.Builder newBuilder(com.ebay.tdq.common.model.RawEvent.Builder other) {
    return new com.ebay.tdq.common.model.RawEvent.Builder(other);
  }

  /**
   * Creates a new RawEvent RecordBuilder by copying an existing RawEvent instance.
   * @param other The existing instance to copy.
   * @return A new RawEvent RecordBuilder
   */
  public static com.ebay.tdq.common.model.RawEvent.Builder newBuilder(com.ebay.tdq.common.model.RawEvent other) {
    return new com.ebay.tdq.common.model.RawEvent.Builder(other);
  }

  /**
   * RecordBuilder for RawEvent instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<RawEvent>
    implements org.apache.avro.data.RecordBuilder<RawEvent> {

    private long ingestTime;
    private long sojTimestamp;
    private long eventTimestamp;
    private long processTimestamp;
    private java.util.Map<java.lang.String,java.lang.String> sojA;
    private java.util.Map<java.lang.String,java.lang.String> sojK;
    private java.util.Map<java.lang.String,java.lang.String> sojC;
    private java.util.Map<java.lang.String,java.lang.String> clientData;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.ebay.tdq.common.model.RawEvent.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.ingestTime)) {
        this.ingestTime = data().deepCopy(fields()[0].schema(), other.ingestTime);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.sojTimestamp)) {
        this.sojTimestamp = data().deepCopy(fields()[1].schema(), other.sojTimestamp);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.eventTimestamp)) {
        this.eventTimestamp = data().deepCopy(fields()[2].schema(), other.eventTimestamp);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.processTimestamp)) {
        this.processTimestamp = data().deepCopy(fields()[3].schema(), other.processTimestamp);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.sojA)) {
        this.sojA = data().deepCopy(fields()[4].schema(), other.sojA);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.sojK)) {
        this.sojK = data().deepCopy(fields()[5].schema(), other.sojK);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.sojC)) {
        this.sojC = data().deepCopy(fields()[6].schema(), other.sojC);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.clientData)) {
        this.clientData = data().deepCopy(fields()[7].schema(), other.clientData);
        fieldSetFlags()[7] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing RawEvent instance
     * @param other The existing instance to copy.
     */
    private Builder(com.ebay.tdq.common.model.RawEvent other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.ingestTime)) {
        this.ingestTime = data().deepCopy(fields()[0].schema(), other.ingestTime);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.sojTimestamp)) {
        this.sojTimestamp = data().deepCopy(fields()[1].schema(), other.sojTimestamp);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.eventTimestamp)) {
        this.eventTimestamp = data().deepCopy(fields()[2].schema(), other.eventTimestamp);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.processTimestamp)) {
        this.processTimestamp = data().deepCopy(fields()[3].schema(), other.processTimestamp);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.sojA)) {
        this.sojA = data().deepCopy(fields()[4].schema(), other.sojA);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.sojK)) {
        this.sojK = data().deepCopy(fields()[5].schema(), other.sojK);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.sojC)) {
        this.sojC = data().deepCopy(fields()[6].schema(), other.sojC);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.clientData)) {
        this.clientData = data().deepCopy(fields()[7].schema(), other.clientData);
        fieldSetFlags()[7] = true;
      }
    }

    /**
      * Gets the value of the 'ingestTime' field.
      * @return The value.
      */
    public java.lang.Long getIngestTime() {
      return ingestTime;
    }

    /**
      * Sets the value of the 'ingestTime' field.
      * @param value The value of 'ingestTime'.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder setIngestTime(long value) {
      validate(fields()[0], value);
      this.ingestTime = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'ingestTime' field has been set.
      * @return True if the 'ingestTime' field has been set, false otherwise.
      */
    public boolean hasIngestTime() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'ingestTime' field.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder clearIngestTime() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'sojTimestamp' field.
      * @return The value.
      */
    public java.lang.Long getSojTimestamp() {
      return sojTimestamp;
    }

    /**
      * Sets the value of the 'sojTimestamp' field.
      * @param value The value of 'sojTimestamp'.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder setSojTimestamp(long value) {
      validate(fields()[1], value);
      this.sojTimestamp = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'sojTimestamp' field has been set.
      * @return True if the 'sojTimestamp' field has been set, false otherwise.
      */
    public boolean hasSojTimestamp() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'sojTimestamp' field.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder clearSojTimestamp() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'eventTimestamp' field.
      * @return The value.
      */
    public java.lang.Long getEventTimestamp() {
      return eventTimestamp;
    }

    /**
      * Sets the value of the 'eventTimestamp' field.
      * @param value The value of 'eventTimestamp'.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder setEventTimestamp(long value) {
      validate(fields()[2], value);
      this.eventTimestamp = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'eventTimestamp' field has been set.
      * @return True if the 'eventTimestamp' field has been set, false otherwise.
      */
    public boolean hasEventTimestamp() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'eventTimestamp' field.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder clearEventTimestamp() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'processTimestamp' field.
      * @return The value.
      */
    public java.lang.Long getProcessTimestamp() {
      return processTimestamp;
    }

    /**
      * Sets the value of the 'processTimestamp' field.
      * @param value The value of 'processTimestamp'.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder setProcessTimestamp(long value) {
      validate(fields()[3], value);
      this.processTimestamp = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'processTimestamp' field has been set.
      * @return True if the 'processTimestamp' field has been set, false otherwise.
      */
    public boolean hasProcessTimestamp() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'processTimestamp' field.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder clearProcessTimestamp() {
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'sojA' field.
      * @return The value.
      */
    public java.util.Map<java.lang.String,java.lang.String> getSojA() {
      return sojA;
    }

    /**
      * Sets the value of the 'sojA' field.
      * @param value The value of 'sojA'.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder setSojA(java.util.Map<java.lang.String,java.lang.String> value) {
      validate(fields()[4], value);
      this.sojA = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'sojA' field has been set.
      * @return True if the 'sojA' field has been set, false otherwise.
      */
    public boolean hasSojA() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'sojA' field.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder clearSojA() {
      sojA = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'sojK' field.
      * @return The value.
      */
    public java.util.Map<java.lang.String,java.lang.String> getSojK() {
      return sojK;
    }

    /**
      * Sets the value of the 'sojK' field.
      * @param value The value of 'sojK'.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder setSojK(java.util.Map<java.lang.String,java.lang.String> value) {
      validate(fields()[5], value);
      this.sojK = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'sojK' field has been set.
      * @return True if the 'sojK' field has been set, false otherwise.
      */
    public boolean hasSojK() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'sojK' field.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder clearSojK() {
      sojK = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
      * Gets the value of the 'sojC' field.
      * @return The value.
      */
    public java.util.Map<java.lang.String,java.lang.String> getSojC() {
      return sojC;
    }

    /**
      * Sets the value of the 'sojC' field.
      * @param value The value of 'sojC'.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder setSojC(java.util.Map<java.lang.String,java.lang.String> value) {
      validate(fields()[6], value);
      this.sojC = value;
      fieldSetFlags()[6] = true;
      return this;
    }

    /**
      * Checks whether the 'sojC' field has been set.
      * @return True if the 'sojC' field has been set, false otherwise.
      */
    public boolean hasSojC() {
      return fieldSetFlags()[6];
    }


    /**
      * Clears the value of the 'sojC' field.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder clearSojC() {
      sojC = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /**
      * Gets the value of the 'clientData' field.
      * @return The value.
      */
    public java.util.Map<java.lang.String,java.lang.String> getClientData() {
      return clientData;
    }

    /**
      * Sets the value of the 'clientData' field.
      * @param value The value of 'clientData'.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder setClientData(java.util.Map<java.lang.String,java.lang.String> value) {
      validate(fields()[7], value);
      this.clientData = value;
      fieldSetFlags()[7] = true;
      return this;
    }

    /**
      * Checks whether the 'clientData' field has been set.
      * @return True if the 'clientData' field has been set, false otherwise.
      */
    public boolean hasClientData() {
      return fieldSetFlags()[7];
    }


    /**
      * Clears the value of the 'clientData' field.
      * @return This builder.
      */
    public com.ebay.tdq.common.model.RawEvent.Builder clearClientData() {
      clientData = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RawEvent build() {
      try {
        RawEvent record = new RawEvent();
        record.ingestTime = fieldSetFlags()[0] ? this.ingestTime : (java.lang.Long) defaultValue(fields()[0]);
        record.sojTimestamp = fieldSetFlags()[1] ? this.sojTimestamp : (java.lang.Long) defaultValue(fields()[1]);
        record.eventTimestamp = fieldSetFlags()[2] ? this.eventTimestamp : (java.lang.Long) defaultValue(fields()[2]);
        record.processTimestamp = fieldSetFlags()[3] ? this.processTimestamp : (java.lang.Long) defaultValue(fields()[3]);
        record.sojA = fieldSetFlags()[4] ? this.sojA : (java.util.Map<java.lang.String,java.lang.String>) defaultValue(fields()[4]);
        record.sojK = fieldSetFlags()[5] ? this.sojK : (java.util.Map<java.lang.String,java.lang.String>) defaultValue(fields()[5]);
        record.sojC = fieldSetFlags()[6] ? this.sojC : (java.util.Map<java.lang.String,java.lang.String>) defaultValue(fields()[6]);
        record.clientData = fieldSetFlags()[7] ? this.clientData : (java.util.Map<java.lang.String,java.lang.String>) defaultValue(fields()[7]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<RawEvent>
    WRITER$ = (org.apache.avro.io.DatumWriter<RawEvent>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<RawEvent>
    READER$ = (org.apache.avro.io.DatumReader<RawEvent>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
