package com.microlife_sdk.ideabuslibrary.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class ScreenUtils {
   public static int getScreenWidth(Context mContext) {
      WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
      DisplayMetrics displayMetrics = new DisplayMetrics();
      windowManager.getDefaultDisplay().getMetrics(displayMetrics);
      return displayMetrics.widthPixels;
   }

   public static int getScreenHeight(Context mContext) {
      WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
      DisplayMetrics displayMetrics = new DisplayMetrics();
      windowManager.getDefaultDisplay().getMetrics(displayMetrics);
      return displayMetrics.heightPixels;
   }

   public static void SetFullScreen(Activity mActivity) {
      mActivity.requestWindowFeature(1);
      mActivity.getWindow().setFlags(1024, 1024);
   }

   public static int getActionBarHeight(Activity mActivity) {
      TypedValue typedValue = new TypedValue();
      return mActivity.getTheme().resolveAttribute(16843499, typedValue, true) ? TypedValue.complexToDimensionPixelSize(typedValue.data, mActivity.getResources().getDisplayMetrics()) : 0;
   }
}
