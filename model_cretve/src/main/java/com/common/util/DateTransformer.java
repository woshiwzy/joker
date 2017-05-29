package com.common.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTransformer {
    public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";

    public static String dateTransformBetweenTimeZone(Date sourceDate, DateFormat formatter,
                                                      TimeZone sourceTimeZone, TimeZone targetTimeZone) {
        Long targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset() + targetTimeZone.getRawOffset();
        return DateTransformer.getTime(new Date(targetTime), formatter);
    }

    public static String getTime(Date date, DateFormat formatter) {
        return formatter.format(date);
    }


}
