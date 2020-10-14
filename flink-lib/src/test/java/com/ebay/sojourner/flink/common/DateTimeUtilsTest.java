package com.ebay.sojourner.flink.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.junit.Assert;
import org.junit.Test;

public class DateTimeUtilsTest {

  public static final LocalDateTime SOME_MIDNIGHT =
      LocalDateTime.of(LocalDate.of(2020, 9, 1), LocalTime.MIDNIGHT);
  public static final ZoneOffset ZONE_OFFSET_MST = ZoneOffset.ofHours(-7);

  @Test
  public void testIsMidnight_1() {
    Assert.assertTrue(DateTimeUtils.isMidnight(SOME_MIDNIGHT.toEpochSecond(ZoneOffset.UTC) * 1000));
    Assert.assertFalse(
        DateTimeUtils.isMidnight(SOME_MIDNIGHT.plusHours(2).toEpochSecond(ZoneOffset.UTC) * 1000));
  }

  @Test
  public void testIsMidnight_2() {
    Assert
        .assertTrue(DateTimeUtils
            .isMidnight(SOME_MIDNIGHT.toEpochSecond(ZONE_OFFSET_MST) * 1000, Time.hours(7)));
    Assert.assertFalse(
        DateTimeUtils.isMidnight(SOME_MIDNIGHT.plusHours(2).toEpochSecond(ZONE_OFFSET_MST) * 1000,
            Time.hours(7)));
  }

  @Test
  public void testMidnightBetween_IllegaArgs_1() {
    try {
      DateTimeUtils.midnightsBetween(2, 1, 0);
    } catch (IllegalArgumentException e) {
      Assert.assertTrue(e.getMessage().equals("Start must be smaller than end."));
    }
  }

  @Test
  public void testMidnightBetween_IllegaArgs_2() {
    try {
      DateTimeUtils.midnightsBetween(1, 1, 0);
    } catch (IllegalArgumentException e) {
      Assert.assertTrue(e.getMessage().equals("Start must be smaller than end."));
    }
  }

  @Test
  public void testMidnightBetween_NoOffset_1() {
    long start = SOME_MIDNIGHT.toEpochSecond(ZoneOffset.UTC) * 1000;
    long end = SOME_MIDNIGHT.plusDays(1).toEpochSecond(ZoneOffset.UTC) * 1000;
    List<Long> midnights = DateTimeUtils.midnightsBetween(start, end);

    // start is inclusive while end is exclusive
    Assert.assertEquals(1, midnights.size());
    Assert.assertEquals(start, midnights.get(0).longValue());
  }

  @Test
  public void testMidnightBetween_NoOffset_2() {
    long start = SOME_MIDNIGHT.plusHours(21).toEpochSecond(ZoneOffset.UTC) * 1000;
    long end = SOME_MIDNIGHT.plusDays(1).plusHours(3).toEpochSecond(ZoneOffset.UTC) * 1000;
    List<Long> midnights = DateTimeUtils.midnightsBetween(start, end);

    Assert.assertEquals(1, midnights.size());
    Assert.assertEquals(SOME_MIDNIGHT.plusDays(1).toEpochSecond(ZoneOffset.UTC) * 1000,
        midnights.get(0).longValue());
  }

  @Test
  public void testMidnightBetween_NoOffset_3() {
    long start = SOME_MIDNIGHT.plusHours(2).toEpochSecond(ZoneOffset.UTC) * 1000;
    long end = SOME_MIDNIGHT.plusDays(3).plusHours(1).toEpochSecond(ZoneOffset.UTC) * 1000;
    List<Long> midnights = DateTimeUtils.midnightsBetween(start, end);

    Assert.assertEquals(3, midnights.size());
    Assert.assertEquals(SOME_MIDNIGHT.plusDays(1).toEpochSecond(ZoneOffset.UTC) * 1000,
        midnights.get(0).longValue());
    Assert.assertEquals(SOME_MIDNIGHT.plusDays(2).toEpochSecond(ZoneOffset.UTC) * 1000,
        midnights.get(1).longValue());
    Assert.assertEquals(SOME_MIDNIGHT.plusDays(3).toEpochSecond(ZoneOffset.UTC) * 1000,
        midnights.get(2).longValue());
  }

  @Test
  public void testMidnightBetween_WithOffset_1() {
    long start = SOME_MIDNIGHT.toEpochSecond(ZONE_OFFSET_MST) * 1000;
    long end = SOME_MIDNIGHT.plusDays(1).toEpochSecond(ZONE_OFFSET_MST) * 1000;
    List<Long> midnights = DateTimeUtils.midnightsBetween(start, end, Time.hours(7));

    // start is inclusive while end is exclusive
    Assert.assertEquals(1, midnights.size());
    Assert.assertEquals(start, midnights.get(0).longValue());
  }

  @Test
  public void testMidnightBetween_WithOffset_2() {
    long start = SOME_MIDNIGHT.plusHours(21).toEpochSecond(ZONE_OFFSET_MST) * 1000;
    long end = SOME_MIDNIGHT.plusDays(1).plusHours(3).toEpochSecond(ZONE_OFFSET_MST) * 1000;
    List<Long> midnights = DateTimeUtils.midnightsBetween(start, end, Time.hours(7));

    Assert.assertEquals(1, midnights.size());
    Assert.assertEquals(SOME_MIDNIGHT.plusDays(1).toEpochSecond(ZONE_OFFSET_MST) * 1000,
        midnights.get(0).longValue());
  }

  @Test
  public void testMidnightBetween_WithOffset_3() {
    long start = SOME_MIDNIGHT.plusHours(2).toEpochSecond(ZONE_OFFSET_MST) * 1000;
    long end = SOME_MIDNIGHT.plusDays(3).plusHours(1).toEpochSecond(ZONE_OFFSET_MST) * 1000;
    List<Long> midnights = DateTimeUtils.midnightsBetween(start, end, Time.hours(7));

    Assert.assertEquals(3, midnights.size());
    Assert.assertEquals(SOME_MIDNIGHT.plusDays(1).toEpochSecond(ZONE_OFFSET_MST) * 1000,
        midnights.get(0).longValue());
    Assert.assertEquals(SOME_MIDNIGHT.plusDays(2).toEpochSecond(ZONE_OFFSET_MST) * 1000,
        midnights.get(1).longValue());
    Assert.assertEquals(SOME_MIDNIGHT.plusDays(3).toEpochSecond(ZONE_OFFSET_MST) * 1000,
        midnights.get(2).longValue());
  }
}
