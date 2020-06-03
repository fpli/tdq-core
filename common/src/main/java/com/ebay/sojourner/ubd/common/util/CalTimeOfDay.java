package com.ebay.sojourner.ubd.common.util;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.TimeZone;

public class CalTimeOfDay implements Serializable {

  public static final int SECOND_IN_MS = 1000;
  public static final int MINUTE_IN_MS = 60 * SECOND_IN_MS;
  public static final int HOUR_IN_MS = 60 * MINUTE_IN_MS;
  public static final int DAY_IN_MS = 24 * HOUR_IN_MS;
  static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
  static final byte[] DIGITS_BYTES = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
  static final byte COLON_BYTE = ':';
  static final byte PERIOD_BYTE = '.';
  private static final long serialVersionUID = 2748950948918226181L;
  //    private static final TimeZone s_defaultTimeZone = TimeZone.getDefault();
  private static final TimeZone s_defaultTimeZone = TimeZone.getTimeZone("MST");
  private long m_time;

  public CalTimeOfDay(long timeMs) {
    setTime(timeMs);
  }

  public CalTimeOfDay() {
    now();
  }

  private static long getLocalTime(final long utcTime) {
    final int offset = s_defaultTimeZone.getOffset(utcTime);
    final long localTime = utcTime + offset;
    return localTime;
  }

  private static int getDayOffset(final long utcTime) {
    final long localTime = getLocalTime(utcTime);
    final long normalizedLocalTime = localTime < 0 ? 0 : localTime;
    final int dayOffset = (int) (normalizedLocalTime % DAY_IN_MS);
    return dayOffset;
  }

  private static void formatTime(final long utcTime, final StringBuilder buf) {
    final int dayOffset = getDayOffset(utcTime);
    int v;

    buf.ensureCapacity(buf.length() + 11);

    v = dayOffset / HOUR_IN_MS;
    buf.append(DIGITS[v / 10]);
    buf.append(DIGITS[v % 10]);
    buf.append(':');

    v = (dayOffset % HOUR_IN_MS) / MINUTE_IN_MS;
    buf.append(DIGITS[v / 10]);
    buf.append(DIGITS[v % 10]);
    buf.append(':');

    v = (dayOffset % MINUTE_IN_MS) / SECOND_IN_MS;
    buf.append(DIGITS[v / 10]);
    buf.append(DIGITS[v % 10]);
    buf.append('.');

    v = (dayOffset % SECOND_IN_MS) / 10;
    buf.append(DIGITS[v / 10]);
    buf.append(DIGITS[v % 10]);
  }

  public void now() {
    setTime(System.currentTimeMillis());
  }

  public long getTime() {
    return m_time;
  }

  public void setTime(long time) {
    if (time < 0) {
      m_time = 0;
    } else {
      m_time = time;
    }
  }

  public void putInBuffer(final ByteBuffer buf) {
    final int dayOffset = getDayOffset(m_time);
    int v;

    v = dayOffset / HOUR_IN_MS;
    buf.put(DIGITS_BYTES[v / 10]);
    buf.put(DIGITS_BYTES[v % 10]);
    buf.put(COLON_BYTE);

    v = (dayOffset % HOUR_IN_MS) / MINUTE_IN_MS;
    buf.put(DIGITS_BYTES[v / 10]);
    buf.put(DIGITS_BYTES[v % 10]);
    buf.put(COLON_BYTE);

    v = (dayOffset % MINUTE_IN_MS) / SECOND_IN_MS;
    buf.put(DIGITS_BYTES[v / 10]);
    buf.put(DIGITS_BYTES[v % 10]);
    buf.put(PERIOD_BYTE);

    v = (dayOffset % SECOND_IN_MS) / 10;
    buf.put(DIGITS_BYTES[v / 10]);
    buf.put(DIGITS_BYTES[v % 10]);

  }

  public final void putInBuffer(final StringBuilder buf) {
    formatTime(m_time, buf);
  }

  public final StringBuilder asString() {
    final StringBuilder buf = new StringBuilder(11);
    putInBuffer(buf);
    return buf;
  }

  @Override
  public final String toString() {
    return asString().toString();
  }
}
