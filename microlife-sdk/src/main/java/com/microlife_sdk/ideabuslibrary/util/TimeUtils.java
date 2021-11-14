package com.microlife_sdk.ideabuslibrary.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
   public static String getCurrentTime(String format) {
      Date date = new Date();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
      return simpleDateFormat.format(date);
   }

   public static int compareDate(String date1, String date2, String format) {
      SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());

      try {
         Date dt1 = dateFormat.parse(date1);
         Date dt2 = dateFormat.parse(date2);
         if (dt1.getTime() > dt2.getTime()) {
            return 1;
         } else {
            return dt1.getTime() < dt2.getTime() ? -1 : 0;
         }
      } catch (Exception var6) {
         var6.printStackTrace();
         return 0;
      }
   }
}
