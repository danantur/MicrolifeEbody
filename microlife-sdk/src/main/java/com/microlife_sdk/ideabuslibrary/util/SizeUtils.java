package com.microlife_sdk.ideabuslibrary.util;

import android.content.Context;

public class SizeUtils {
   public static int dp2px(Context context, float dpValue) {
      float scale = context.getResources().getDisplayMetrics().density;
      return (int)(dpValue * scale + 0.5F);
   }

   public static int px2dp(Context context, float pxValue) {
      float scale = context.getResources().getDisplayMetrics().density;
      return (int)(pxValue / scale + 0.5F);
   }
}
