/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.ebay.sojourner.common.model;

import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class BotSignature extends org.apache.avro.specific.SpecificRecordBase implements
    org.apache.avro.specific.SpecificRecord {

  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse(
      "{\"type\":\"record\",\"name\":\"BotSignature\",\"namespace\":\"com.ebay.sojourner.common"
          + ".model\",\"fields\":[{\"name\":\"type\",\"type\":[{\"type\":\"string\",\"avro.java"
          + ".string\":\"String\"},\"null\"]},{\"name\":\"userAgent\",\"type\":[\"null\","
          + "{\"type\":\"record\",\"name\":\"AgentHash\",\"fields\":[{\"name\":\"agentHash1\","
          + "\"type\":\"long\"},{\"name\":\"agentHash2\",\"type\":\"long\"}]}],\"default\":null},"
          + "{\"name\":\"ip\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"guid\","
          + "\"type\":[\"null\",{\"type\":\"record\",\"name\":\"Guid\","
          + "\"fields\":[{\"name\":\"guid1\",\"type\":\"long\"},{\"name\":\"guid2\","
          + "\"type\":\"long\"}]}],\"default\":null},{\"name\":\"botFlags\",\"type\":[\"null\","
          + "{\"type\":\"array\",\"items\":\"int\"}],\"default\":null},"
          + "{\"name\":\"expirationTime\",\"type\":[\"null\",\"long\"]},"
          + "{\"name\":\"isGeneration\",\"type\":[\"null\",\"boolean\"]},{\"name\":\"category\","
          + "\"type\":[\"null\",\"int\"]},{\"name\":\"generationTime\",\"type\":[\"null\","
          + "\"long\"]}]}");
  private static final long serialVersionUID = 7913047988375141742L;
  private static SpecificData MODEL$ = new SpecificData();
  private static final BinaryMessageEncoder<BotSignature> ENCODER =
      new BinaryMessageEncoder<BotSignature>(MODEL$, SCHEMA$);
  private static final BinaryMessageDecoder<BotSignature> DECODER =
      new BinaryMessageDecoder<BotSignature>(MODEL$, SCHEMA$);
  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<BotSignature>
      WRITER$ = (org.apache.avro.io.DatumWriter<BotSignature>) MODEL$.createDatumWriter(SCHEMA$);
  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<BotSignature>
      READER$ = (org.apache.avro.io.DatumReader<BotSignature>) MODEL$.createDatumReader(SCHEMA$);
  @Deprecated
  public java.lang.String type;
  @Deprecated
  public com.ebay.sojourner.common.model.AgentHash userAgent;
  @Deprecated
  public java.lang.Integer ip;
  @Deprecated
  public com.ebay.sojourner.common.model.Guid guid;
  @Deprecated
  public java.util.List<java.lang.Integer> botFlags;
  @Deprecated
  public java.lang.Long expirationTime;
  @Deprecated
  public java.lang.Boolean isGeneration;
  @Deprecated
  public java.lang.Integer category;
  @Deprecated
  public java.lang.Long generationTime;

  /**
   * Default constructor.  Note that this does not initialize fields to their default values from
   * the schema.  If that is desired then one should use <code>newBuilder()</code>.
   */
  public BotSignature() {
  }

  /**
   * All-args constructor.
   *
   * @param type The new value for type
   * @param userAgent The new value for userAgent
   * @param ip The new value for ip
   * @param guid The new value for guid
   * @param botFlags The new value for botFlags
   * @param expirationTime The new value for expirationTime
   * @param isGeneration The new value for isGeneration
   * @param category The new value for category
   * @param generationTime The new value for generationTime
   */
  public BotSignature(java.lang.String type, com.ebay.sojourner.common.model.AgentHash userAgent,
      java.lang.Integer ip, com.ebay.sojourner.common.model.Guid guid,
      java.util.List<java.lang.Integer> botFlags, java.lang.Long expirationTime,
      java.lang.Boolean isGeneration, java.lang.Integer category, java.lang.Long generationTime) {
    this.type = type;
    this.userAgent = userAgent;
    this.ip = ip;
    this.guid = guid;
    this.botFlags = botFlags;
    this.expirationTime = expirationTime;
    this.isGeneration = isGeneration;
    this.category = category;
    this.generationTime = generationTime;
  }

  public static org.apache.avro.Schema getClassSchema() {
    return SCHEMA$;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<BotSignature> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link
   * SchemaStore}.
   *
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<BotSignature> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<BotSignature>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Deserializes a BotSignature from a ByteBuffer.
   */
  public static BotSignature fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /**
   * Creates a new BotSignature RecordBuilder.
   *
   * @return A new BotSignature RecordBuilder
   */
  public static com.ebay.sojourner.common.model.BotSignature.Builder newBuilder() {
    return new com.ebay.sojourner.common.model.BotSignature.Builder();
  }

  /**
   * Creates a new BotSignature RecordBuilder by copying an existing Builder.
   *
   * @param other The existing builder to copy.
   * @return A new BotSignature RecordBuilder
   */
  public static com.ebay.sojourner.common.model.BotSignature.Builder newBuilder(
      com.ebay.sojourner.common.model.BotSignature.Builder other) {
    return new com.ebay.sojourner.common.model.BotSignature.Builder(other);
  }

  /**
   * Creates a new BotSignature RecordBuilder by copying an existing BotSignature instance.
   *
   * @param other The existing instance to copy.
   * @return A new BotSignature RecordBuilder
   */
  public static com.ebay.sojourner.common.model.BotSignature.Builder newBuilder(
      com.ebay.sojourner.common.model.BotSignature other) {
    return new com.ebay.sojourner.common.model.BotSignature.Builder(other);
  }

  /**
   * Serializes this BotSignature to a ByteBuffer.
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  public org.apache.avro.Schema getSchema() {
    return SCHEMA$;
  }

  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
      case 0:
        return type;
      case 1:
        return userAgent;
      case 2:
        return ip;
      case 3:
        return guid;
      case 4:
        return botFlags;
      case 5:
        return expirationTime;
      case 6:
        return isGeneration;
      case 7:
        return category;
      case 8:
        return generationTime;
      default:
        throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value = "unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
      case 0:
        type = (java.lang.String) value$;
        break;
      case 1:
        userAgent = (com.ebay.sojourner.common.model.AgentHash) value$;
        break;
      case 2:
        ip = (java.lang.Integer) value$;
        break;
      case 3:
        guid = (com.ebay.sojourner.common.model.Guid) value$;
        break;
      case 4:
        botFlags = (java.util.List<java.lang.Integer>) value$;
        break;
      case 5:
        expirationTime = (java.lang.Long) value$;
        break;
      case 6:
        isGeneration = (java.lang.Boolean) value$;
        break;
      case 7:
        category = (java.lang.Integer) value$;
        break;
      case 8:
        generationTime = (java.lang.Long) value$;
        break;
      default:
        throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'type' field.
   *
   * @return The value of the 'type' field.
   */
  public java.lang.String getType() {
    return type;
  }

  /**
   * Sets the value of the 'type' field.
   *
   * @param value the value to set.
   */
  public void setType(java.lang.String value) {
    this.type = value;
  }

  /**
   * Gets the value of the 'userAgent' field.
   *
   * @return The value of the 'userAgent' field.
   */
  public com.ebay.sojourner.common.model.AgentHash getUserAgent() {
    return userAgent;
  }

  /**
   * Sets the value of the 'userAgent' field.
   *
   * @param value the value to set.
   */
  public void setUserAgent(com.ebay.sojourner.common.model.AgentHash value) {
    this.userAgent = value;
  }

  /**
   * Gets the value of the 'ip' field.
   *
   * @return The value of the 'ip' field.
   */
  public java.lang.Integer getIp() {
    return ip;
  }

  /**
   * Sets the value of the 'ip' field.
   *
   * @param value the value to set.
   */
  public void setIp(java.lang.Integer value) {
    this.ip = value;
  }

  /**
   * Gets the value of the 'guid' field.
   *
   * @return The value of the 'guid' field.
   */
  public com.ebay.sojourner.common.model.Guid getGuid() {
    return guid;
  }

  /**
   * Sets the value of the 'guid' field.
   *
   * @param value the value to set.
   */
  public void setGuid(com.ebay.sojourner.common.model.Guid value) {
    this.guid = value;
  }

  /**
   * Gets the value of the 'botFlags' field.
   *
   * @return The value of the 'botFlags' field.
   */
  public java.util.List<java.lang.Integer> getBotFlags() {
    return botFlags;
  }

  /**
   * Sets the value of the 'botFlags' field.
   *
   * @param value the value to set.
   */
  public void setBotFlags(java.util.List<java.lang.Integer> value) {
    this.botFlags = value;
  }

  /**
   * Gets the value of the 'expirationTime' field.
   *
   * @return The value of the 'expirationTime' field.
   */
  public java.lang.Long getExpirationTime() {
    return expirationTime;
  }

  /**
   * Sets the value of the 'expirationTime' field.
   *
   * @param value the value to set.
   */
  public void setExpirationTime(java.lang.Long value) {
    this.expirationTime = value;
  }

  /**
   * Gets the value of the 'isGeneration' field.
   *
   * @return The value of the 'isGeneration' field.
   */
  public java.lang.Boolean getIsGeneration() {
    return isGeneration;
  }

  /**
   * Sets the value of the 'isGeneration' field.
   *
   * @param value the value to set.
   */
  public void setIsGeneration(java.lang.Boolean value) {
    this.isGeneration = value;
  }

  /**
   * Gets the value of the 'category' field.
   *
   * @return The value of the 'category' field.
   */
  public java.lang.Integer getCategory() {
    return category;
  }

  /**
   * Sets the value of the 'category' field.
   *
   * @param value the value to set.
   */
  public void setCategory(java.lang.Integer value) {
    this.category = value;
  }

  /**
   * Gets the value of the 'generationTime' field.
   *
   * @return The value of the 'generationTime' field.
   */
  public java.lang.Long getGenerationTime() {
    return generationTime;
  }

  /**
   * Sets the value of the 'generationTime' field.
   *
   * @param value the value to set.
   */
  public void setGenerationTime(java.lang.Long value) {
    this.generationTime = value;
  }

  @Override
  public void writeExternal(java.io.ObjectOutput out)
      throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @Override
  public void readExternal(java.io.ObjectInput in)
      throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  /**
   * RecordBuilder for BotSignature instances.
   */
  public static class Builder extends
      org.apache.avro.specific.SpecificRecordBuilderBase<BotSignature>
      implements org.apache.avro.data.RecordBuilder<BotSignature> {

    private java.lang.String type;
    private com.ebay.sojourner.common.model.AgentHash userAgent;
    private com.ebay.sojourner.common.model.AgentHash.Builder userAgentBuilder;
    private java.lang.Integer ip;
    private com.ebay.sojourner.common.model.Guid guid;
    private com.ebay.sojourner.common.model.Guid.Builder guidBuilder;
    private java.util.List<java.lang.Integer> botFlags;
    private java.lang.Long expirationTime;
    private java.lang.Boolean isGeneration;
    private java.lang.Integer category;
    private java.lang.Long generationTime;

    /**
     * Creates a new Builder
     */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     *
     * @param other The existing Builder to copy.
     */
    private Builder(com.ebay.sojourner.common.model.BotSignature.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.type)) {
        this.type = data().deepCopy(fields()[0].schema(), other.type);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.userAgent)) {
        this.userAgent = data().deepCopy(fields()[1].schema(), other.userAgent);
        fieldSetFlags()[1] = true;
      }
      if (other.hasUserAgentBuilder()) {
        this.userAgentBuilder = com.ebay.sojourner.common.model.AgentHash
            .newBuilder(other.getUserAgentBuilder());
      }
      if (isValidValue(fields()[2], other.ip)) {
        this.ip = data().deepCopy(fields()[2].schema(), other.ip);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.guid)) {
        this.guid = data().deepCopy(fields()[3].schema(), other.guid);
        fieldSetFlags()[3] = true;
      }
      if (other.hasGuidBuilder()) {
        this.guidBuilder = com.ebay.sojourner.common.model.Guid.newBuilder(other.getGuidBuilder());
      }
      if (isValidValue(fields()[4], other.botFlags)) {
        this.botFlags = data().deepCopy(fields()[4].schema(), other.botFlags);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.expirationTime)) {
        this.expirationTime = data().deepCopy(fields()[5].schema(), other.expirationTime);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.isGeneration)) {
        this.isGeneration = data().deepCopy(fields()[6].schema(), other.isGeneration);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.category)) {
        this.category = data().deepCopy(fields()[7].schema(), other.category);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.generationTime)) {
        this.generationTime = data().deepCopy(fields()[8].schema(), other.generationTime);
        fieldSetFlags()[8] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing BotSignature instance
     *
     * @param other The existing instance to copy.
     */
    private Builder(com.ebay.sojourner.common.model.BotSignature other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.type)) {
        this.type = data().deepCopy(fields()[0].schema(), other.type);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.userAgent)) {
        this.userAgent = data().deepCopy(fields()[1].schema(), other.userAgent);
        fieldSetFlags()[1] = true;
      }
      this.userAgentBuilder = null;
      if (isValidValue(fields()[2], other.ip)) {
        this.ip = data().deepCopy(fields()[2].schema(), other.ip);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.guid)) {
        this.guid = data().deepCopy(fields()[3].schema(), other.guid);
        fieldSetFlags()[3] = true;
      }
      this.guidBuilder = null;
      if (isValidValue(fields()[4], other.botFlags)) {
        this.botFlags = data().deepCopy(fields()[4].schema(), other.botFlags);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.expirationTime)) {
        this.expirationTime = data().deepCopy(fields()[5].schema(), other.expirationTime);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.isGeneration)) {
        this.isGeneration = data().deepCopy(fields()[6].schema(), other.isGeneration);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.category)) {
        this.category = data().deepCopy(fields()[7].schema(), other.category);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.generationTime)) {
        this.generationTime = data().deepCopy(fields()[8].schema(), other.generationTime);
        fieldSetFlags()[8] = true;
      }
    }

    /**
     * Gets the value of the 'type' field.
     *
     * @return The value.
     */
    public java.lang.String getType() {
      return type;
    }

    /**
     * Sets the value of the 'type' field.
     *
     * @param value The value of 'type'.
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder setType(java.lang.String value) {
      validate(fields()[0], value);
      this.type = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
     * Checks whether the 'type' field has been set.
     *
     * @return True if the 'type' field has been set, false otherwise.
     */
    public boolean hasType() {
      return fieldSetFlags()[0];
    }


    /**
     * Clears the value of the 'type' field.
     *
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder clearType() {
      type = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
     * Gets the value of the 'userAgent' field.
     *
     * @return The value.
     */
    public com.ebay.sojourner.common.model.AgentHash getUserAgent() {
      return userAgent;
    }

    /**
     * Sets the value of the 'userAgent' field.
     *
     * @param value The value of 'userAgent'.
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder setUserAgent(
        com.ebay.sojourner.common.model.AgentHash value) {
      validate(fields()[1], value);
      this.userAgentBuilder = null;
      this.userAgent = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
     * Checks whether the 'userAgent' field has been set.
     *
     * @return True if the 'userAgent' field has been set, false otherwise.
     */
    public boolean hasUserAgent() {
      return fieldSetFlags()[1];
    }

    /**
     * Gets the Builder instance for the 'userAgent' field and creates one if it doesn't exist yet.
     *
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.AgentHash.Builder getUserAgentBuilder() {
      if (userAgentBuilder == null) {
        if (hasUserAgent()) {
          setUserAgentBuilder(com.ebay.sojourner.common.model.AgentHash.newBuilder(userAgent));
        } else {
          setUserAgentBuilder(com.ebay.sojourner.common.model.AgentHash.newBuilder());
        }
      }
      return userAgentBuilder;
    }

    /**
     * Sets the Builder instance for the 'userAgent' field
     *
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder setUserAgentBuilder(
        com.ebay.sojourner.common.model.AgentHash.Builder value) {
      clearUserAgent();
      userAgentBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'userAgent' field has an active Builder instance
     *
     * @return True if the 'userAgent' field has an active Builder instance
     */
    public boolean hasUserAgentBuilder() {
      return userAgentBuilder != null;
    }

    /**
     * Clears the value of the 'userAgent' field.
     *
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder clearUserAgent() {
      userAgent = null;
      userAgentBuilder = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
     * Gets the value of the 'ip' field.
     *
     * @return The value.
     */
    public java.lang.Integer getIp() {
      return ip;
    }

    /**
     * Sets the value of the 'ip' field.
     *
     * @param value The value of 'ip'.
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder setIp(java.lang.Integer value) {
      validate(fields()[2], value);
      this.ip = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
     * Checks whether the 'ip' field has been set.
     *
     * @return True if the 'ip' field has been set, false otherwise.
     */
    public boolean hasIp() {
      return fieldSetFlags()[2];
    }


    /**
     * Clears the value of the 'ip' field.
     *
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder clearIp() {
      ip = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
     * Gets the value of the 'guid' field.
     *
     * @return The value.
     */
    public com.ebay.sojourner.common.model.Guid getGuid() {
      return guid;
    }

    /**
     * Sets the value of the 'guid' field.
     *
     * @param value The value of 'guid'.
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder setGuid(
        com.ebay.sojourner.common.model.Guid value) {
      validate(fields()[3], value);
      this.guidBuilder = null;
      this.guid = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
     * Checks whether the 'guid' field has been set.
     *
     * @return True if the 'guid' field has been set, false otherwise.
     */
    public boolean hasGuid() {
      return fieldSetFlags()[3];
    }

    /**
     * Gets the Builder instance for the 'guid' field and creates one if it doesn't exist yet.
     *
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.Guid.Builder getGuidBuilder() {
      if (guidBuilder == null) {
        if (hasGuid()) {
          setGuidBuilder(com.ebay.sojourner.common.model.Guid.newBuilder(guid));
        } else {
          setGuidBuilder(com.ebay.sojourner.common.model.Guid.newBuilder());
        }
      }
      return guidBuilder;
    }

    /**
     * Sets the Builder instance for the 'guid' field
     *
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder setGuidBuilder(
        com.ebay.sojourner.common.model.Guid.Builder value) {
      clearGuid();
      guidBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'guid' field has an active Builder instance
     *
     * @return True if the 'guid' field has an active Builder instance
     */
    public boolean hasGuidBuilder() {
      return guidBuilder != null;
    }

    /**
     * Clears the value of the 'guid' field.
     *
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder clearGuid() {
      guid = null;
      guidBuilder = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
     * Gets the value of the 'botFlags' field.
     *
     * @return The value.
     */
    public java.util.List<java.lang.Integer> getBotFlags() {
      return botFlags;
    }

    /**
     * Sets the value of the 'botFlags' field.
     *
     * @param value The value of 'botFlags'.
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder setBotFlags(
        java.util.List<java.lang.Integer> value) {
      validate(fields()[4], value);
      this.botFlags = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
     * Checks whether the 'botFlags' field has been set.
     *
     * @return True if the 'botFlags' field has been set, false otherwise.
     */
    public boolean hasBotFlags() {
      return fieldSetFlags()[4];
    }


    /**
     * Clears the value of the 'botFlags' field.
     *
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder clearBotFlags() {
      botFlags = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
     * Gets the value of the 'expirationTime' field.
     *
     * @return The value.
     */
    public java.lang.Long getExpirationTime() {
      return expirationTime;
    }

    /**
     * Sets the value of the 'expirationTime' field.
     *
     * @param value The value of 'expirationTime'.
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder setExpirationTime(
        java.lang.Long value) {
      validate(fields()[5], value);
      this.expirationTime = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
     * Checks whether the 'expirationTime' field has been set.
     *
     * @return True if the 'expirationTime' field has been set, false otherwise.
     */
    public boolean hasExpirationTime() {
      return fieldSetFlags()[5];
    }


    /**
     * Clears the value of the 'expirationTime' field.
     *
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder clearExpirationTime() {
      expirationTime = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
     * Gets the value of the 'isGeneration' field.
     *
     * @return The value.
     */
    public java.lang.Boolean getIsGeneration() {
      return isGeneration;
    }

    /**
     * Sets the value of the 'isGeneration' field.
     *
     * @param value The value of 'isGeneration'.
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder setIsGeneration(
        java.lang.Boolean value) {
      validate(fields()[6], value);
      this.isGeneration = value;
      fieldSetFlags()[6] = true;
      return this;
    }

    /**
     * Checks whether the 'isGeneration' field has been set.
     *
     * @return True if the 'isGeneration' field has been set, false otherwise.
     */
    public boolean hasIsGeneration() {
      return fieldSetFlags()[6];
    }


    /**
     * Clears the value of the 'isGeneration' field.
     *
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder clearIsGeneration() {
      isGeneration = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /**
     * Gets the value of the 'category' field.
     *
     * @return The value.
     */
    public java.lang.Integer getCategory() {
      return category;
    }

    /**
     * Sets the value of the 'category' field.
     *
     * @param value The value of 'category'.
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder setCategory(
        java.lang.Integer value) {
      validate(fields()[7], value);
      this.category = value;
      fieldSetFlags()[7] = true;
      return this;
    }

    /**
     * Checks whether the 'category' field has been set.
     *
     * @return True if the 'category' field has been set, false otherwise.
     */
    public boolean hasCategory() {
      return fieldSetFlags()[7];
    }


    /**
     * Clears the value of the 'category' field.
     *
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder clearCategory() {
      category = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    /**
     * Gets the value of the 'generationTime' field.
     *
     * @return The value.
     */
    public java.lang.Long getGenerationTime() {
      return generationTime;
    }

    /**
     * Sets the value of the 'generationTime' field.
     *
     * @param value The value of 'generationTime'.
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder setGenerationTime(
        java.lang.Long value) {
      validate(fields()[8], value);
      this.generationTime = value;
      fieldSetFlags()[8] = true;
      return this;
    }

    /**
     * Checks whether the 'generationTime' field has been set.
     *
     * @return True if the 'generationTime' field has been set, false otherwise.
     */
    public boolean hasGenerationTime() {
      return fieldSetFlags()[8];
    }


    /**
     * Clears the value of the 'generationTime' field.
     *
     * @return This builder.
     */
    public com.ebay.sojourner.common.model.BotSignature.Builder clearGenerationTime() {
      generationTime = null;
      fieldSetFlags()[8] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BotSignature build() {
      try {
        BotSignature record = new BotSignature();
        record.type = fieldSetFlags()[0] ? this.type : (java.lang.String) defaultValue(fields()[0]);
        if (userAgentBuilder != null) {
          record.userAgent = this.userAgentBuilder.build();
        } else {
          record.userAgent = fieldSetFlags()[1] ? this.userAgent
              : (com.ebay.sojourner.common.model.AgentHash) defaultValue(fields()[1]);
        }
        record.ip = fieldSetFlags()[2] ? this.ip : (java.lang.Integer) defaultValue(fields()[2]);
        if (guidBuilder != null) {
          record.guid = this.guidBuilder.build();
        } else {
          record.guid = fieldSetFlags()[3] ? this.guid
              : (com.ebay.sojourner.common.model.Guid) defaultValue(fields()[3]);
        }
        record.botFlags = fieldSetFlags()[4] ? this.botFlags
            : (java.util.List<java.lang.Integer>) defaultValue(fields()[4]);
        record.expirationTime =
            fieldSetFlags()[5] ? this.expirationTime : (java.lang.Long) defaultValue(fields()[5]);
        record.isGeneration =
            fieldSetFlags()[6] ? this.isGeneration : (java.lang.Boolean) defaultValue(fields()[6]);
        record.category =
            fieldSetFlags()[7] ? this.category : (java.lang.Integer) defaultValue(fields()[7]);
        record.generationTime =
            fieldSetFlags()[8] ? this.generationTime : (java.lang.Long) defaultValue(fields()[8]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

}
