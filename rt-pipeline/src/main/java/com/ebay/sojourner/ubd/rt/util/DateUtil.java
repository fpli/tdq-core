package com.ebay.sojourner.ubd.rt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    /**
     * Convert date to timestamp
     *
     * @param date
     * @param format e.g yyyy-MM-dd HH:mm:ss
     * @return long value in milliseconds
     * @throws ParseException
     */
    public static long dateToStamp(String dateStr, String format)
            throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-7"));
        Date date = simpleDateFormat.parse(dateStr);
        return date.getTime();
    }


    /**
     * Convert timestamp to date
     *
     * @param ts
     * @param format e.g yyyy-MM-dd HH:mm:ss
     * @return date in string
     */
    public static String stampToDate(long ts, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-7"));//utc
        Date date = new Date(ts);
        return simpleDateFormat.format(date);
    }


    public static String formatDate(String dateStr, String format_in, String format_out)
            throws ParseException {

        SimpleDateFormat in_sdf = new SimpleDateFormat(format_in, Locale.US);
        SimpleDateFormat out_sdf = new SimpleDateFormat(format_out, Locale.US);
        Date date = in_sdf.parse(dateStr);
        return out_sdf.format(date);
    }

    /**
     * Get hour for a specified timestamp
     *
     * @param ts
     * @return int value for hour
     */
    public static long addHourAndGetTime(long ts,int amount) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(ts));
        cal.add(Calendar.HOUR, amount);

        return cal.getTimeInMillis();
    }



    /**
     * Get related date by adding some amount days to current date
     *
     * @param days   - amount of days
     * @param format e.g yyyy-MM-dd HH:mm:ss
     * @return date in string
     */
    public static String getRelatedDate(int days, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-7"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, days);
        return simpleDateFormat.format(cal.getTime());
    }
}
