package com.example.karlo.sstconference.utility;

import com.example.karlo.sstconference.commons.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtility {

    public static final long MINUTE = 60;
    public static final long HOUR = 3600;
    public static final long DAY = 86400;

    public static final TimeZone TIME_ZONE_GMT = TimeZone.getTimeZone("GMT");

    public static String getNowInIsoFormat() {
        TimeZone tz = TIME_ZONE_GMT;
        DateFormat df = getIsoFormatter();
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public static String getDateInIsoFormat(Date date) {
        TimeZone tz = TIME_ZONE_GMT;
        DateFormat df = getIsoFormatter();
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static Date getDateInGMT(Date date) {
        return stringToIsoDate(getDateInIsoFormat(date));
    }

    public static Date getNowInGMT() {
        return stringToIsoDate(getNowInIsoFormat());
    }

    public static Date stringToIsoDate(String date) {
        try {
            return getIsoFormatter().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static long getDateDifferenceInSeconds(Date firstDate, Date secondDate) {
        if (firstDate.before(secondDate)) {
            return (secondDate.getTime() - firstDate.getTime()) / 1000;
        } else {
            return (firstDate.getTime() - secondDate.getTime()) / 1000;
        }
    }

    public static SimpleDateFormat getIsoFormatter() {
        return new SimpleDateFormat(Constants.ISO_DATE_FORMAT, Locale.ENGLISH);
    }
}
