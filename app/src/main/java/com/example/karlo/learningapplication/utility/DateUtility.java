package com.example.karlo.learningapplication.utility;

import com.example.karlo.learningapplication.commons.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtility {

    public static String getNowInIsoFormat() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = getIsoFormatter();
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public static SimpleDateFormat getIsoFormatter() {
        return new SimpleDateFormat(Constants.ISO_DATE_FORMAT, Locale.ENGLISH);
    }
}
