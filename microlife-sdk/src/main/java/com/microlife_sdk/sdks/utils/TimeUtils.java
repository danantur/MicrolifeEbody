// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils
{
    private static final String TAG = "TimeUtils";
    private static Date curDate;
    
    public static String getCurrentTime1() {
        final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeUtils.curDate.setTime(System.currentTimeMillis());
        return formatter1.format(TimeUtils.curDate);
    }
    
    public static String formatTime1(final long time) {
        final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeUtils.curDate.setTime(time);
        return formatter1.format(TimeUtils.curDate);
    }
    
    public static Date parseFormatter1Time(final String timeStr) {
        try {
            final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter1.parse(timeStr);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
    
    public static String getCurrentTime2() {
        final SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        TimeUtils.curDate.setTime(System.currentTimeMillis());
        return formatter2.format(TimeUtils.curDate);
    }
    
    static {
        TimeUtils.curDate = new Date(System.currentTimeMillis());
    }
}
