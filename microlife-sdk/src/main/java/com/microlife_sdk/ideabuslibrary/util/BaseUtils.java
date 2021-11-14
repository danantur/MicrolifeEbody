package com.microlife_sdk.ideabuslibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class BaseUtils {
   private static boolean isPrintLog = true;
   public static float density = 0.0F;
   public static int screenWidth = 0;
   public static int screenHeight = 0;
   public static float screenScale = 0.0F;
   public static boolean isTabletScaleSize;
   private static Toast toast = null;

   public static boolean matcherEmail(String email) {
      return Patterns.EMAIL_ADDRESS.matcher(email).matches();
   }

   public static String convertUtf8ToString(byte[] bytes) {
      String asd = "";

      try {
         asd = new String(bytes, "UTF-8");
      } catch (UnsupportedEncodingException var3) {
         var3.printStackTrace();
      }

      return asd;
   }

   public static void showToast(Activity mActivity, String text) {
      if (toast == null) {
         toast = Toast.makeText(mActivity, text, Toast.LENGTH_SHORT);
      } else {
         toast.setText(text);
      }

      toast.show();
   }

   public static int convertStringToInt(String str) {
      return convertStringToInt(str, 10);
   }

   public static int convertStringToInt(String str, int radix) {
      int i = 0;

      try {
         i = Integer.parseInt(str, radix);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return i;
   }

   public static SharedPreferences getSharedPref(Context context, String sharedPrefName) {
      return context.getSharedPreferences(sharedPrefName, 0);
   }

   public static Editor getSharePrefEditor(Context context, String sharedPrefName) {
      return context.getSharedPreferences(sharedPrefName, 0).edit();
   }

   public static boolean isNetworkAvailable(Context context) {
      ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
      if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
         return activeNetworkInfo.isAvailable();
      } else {
         return false;
      }
   }

   public static String getFillString(int i, int length, String fillStr) {
      StringBuilder str = new StringBuilder(String.valueOf(i));

      while(str.length() < length) {
         str.insert(0, fillStr);
      }

      return str.toString();
   }

   public static void setIsPrintLog(boolean b) {
      isPrintLog = b;
   }

   public static void printLog(String type, String tag, String msg) {
      byte priority = 0;
      if (type.equals("i")) {
         priority = 4;
      } else if (type.equals("d")) {
         priority = 3;
      } else if (type.equals("e")) {
         priority = 6;
      }

      if (isPrintLog) {
         Log.println(priority, tag, msg);
      }

   }

   public static String getScaleToString(float value, int scale) {
      return getScaleDecimal(value, scale).toString();
   }

   public static float getScaleToFloat(float value, int scale) {
      return getScaleDecimal(value, scale).floatValue();
   }

   private static BigDecimal getScaleDecimal(float value, int scale) {
      return (new BigDecimal((double)value)).setScale(scale, RoundingMode.HALF_UP);
   }

   public static float divide(float value, float divideValue, int scale) {
      BigDecimal bigDecimal = (new BigDecimal((double)value)).divide(new BigDecimal((double)divideValue), scale, RoundingMode.HALF_UP);
      return bigDecimal.floatValue();
   }

   public static String getStringPlace(String num, int place) {
      NumberFormat nf = NumberFormat.getInstance();
      nf.setGroupingUsed(false);
      nf.setMinimumIntegerDigits(place);
      return nf.format(num);
   }

   public static String getStringPlace(int num, int place) {
      return getStringPlace(String.valueOf(num), place);
   }

   public static String getDateTime() {
      Calendar c = Calendar.getInstance();
      int year = c.get(1);
      int month = c.get(2) + 1;
      int day = c.get(5);
      int hour = c.get(11);
      int minute = c.get(12);
      int second = c.get(13);
      String date = year + "/" + getStringPlace(month, 2) + "/" + getStringPlace(day, 2) + " " + getStringPlace(hour, 2) + ":" + getStringPlace(minute, 2) + ":" + getStringPlace(second, 2);
      return date;
   }

   public static float convertDpToPixel(float dp) {
      float px = dp * density;
      return px;
   }

   public static float convertPixelToDp(float pixel) {
      float dp = pixel / density;
      return dp;
   }

   public static void setAppLocale(Resources resources, Locale locale) {
      Configuration config = resources.getConfiguration();
      DisplayMetrics dm = resources.getDisplayMetrics();
      config.setLocale(locale);
      resources.updateConfiguration(config, dm);
   }

   public static int getTabletScalSize(int size, float scale) {
      if (isTabletScaleSize) {
         float scaleMultiple = 2.9F - screenScale;
         return (int)((float)size * scale * scaleMultiple);
      } else {
         return size;
      }
   }

   public static byte[] convertDrawableToByteArray(Resources res, int rid) {
      byte[] data = null;
      InputStream is = res.openRawResource(rid);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      try {
         byte[] buffer = new byte[1024];

         int len;
         while((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
         }

         data = baos.toByteArray();
      } catch (IOException var19) {
         var19.printStackTrace();
      } finally {
         try {
            is.close();
         } catch (IOException var18) {
            var18.printStackTrace();
         }

         try {
            baos.close();
         } catch (IOException var17) {
            var17.printStackTrace();
         }

      }

      return data;
   }

   public static Bitmap decodeBitmapFromRid(Resources resources, int rid, int inSampleSize) {
      if (rid == 0) {
         return null;
      } else {
         Options options = getBitmapOptions(inSampleSize);
         InputStream is = resources.openRawResource(rid);
         return BitmapFactory.decodeStream(is, (Rect)null, options);
      }
   }

   public static Bitmap decodeBitmapFromByteArray(byte[] byteArray, int inSampleSize) {
      if (byteArray == null) {
         return null;
      } else {
         Options options = getBitmapOptions(inSampleSize);
         return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
      }
   }

   private static Options getBitmapOptions(int inSampleSize) {
      Options options = new Options();
      options.inPreferredConfig = Config.RGB_565;
      options.inPurgeable = true;
      options.inInputShareable = true;
      options.inSampleSize = inSampleSize;
      return options;
   }

   public static String convertBytesToHexString(byte[] src) {
      StringBuilder stringBuilder = new StringBuilder("");
      if (src != null && src.length > 0) {
         for(int i = 0; i < src.length; ++i) {
            int v = src[i] & 255;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
               stringBuilder.append(0);
            }

            stringBuilder.append(hv);
         }

         return stringBuilder.toString();
      } else {
         return null;
      }
   }

   public static String convertBytesToString(byte[] src) {
      StringBuilder stringBuilder = new StringBuilder("");
      if (src != null && src.length > 0) {
         for(int i = 0; i < src.length; ++i) {
            int v = src[i];
            String hv = Integer.toString(v);
            stringBuilder.append(hv);
         }

         return stringBuilder.toString();
      } else {
         return null;
      }
   }

   public static String convertBytesToAscii(byte[] src) {
      String asciiStr = "";

      try {
         asciiStr = new String(src, "ISO8859-1");
      } catch (UnsupportedEncodingException var3) {
         var3.printStackTrace();
      }

      return asciiStr;
   }

   public static byte[] convertHexToByteArray(String hexString) {
      char[] hex = hexString.toCharArray();
      int length = hex.length / 2;
      byte[] rawData = new byte[length];

      for(int i = 0; i < length; ++i) {
         int high = Character.digit(hex[i * 2], 16);
         int low = Character.digit(hex[i * 2 + 1], 16);
         int value = high << 4 | low;
         if (value > 127) {
            value -= 256;
         }

         rawData[i] = (byte)value;
      }

      return rawData;
   }

   public static String convertDecimalToHex(int decimal, int digit) {
      StringBuilder hexStr = new StringBuilder(Integer.toHexString(decimal));

      while(hexStr.length() < digit) {
         hexStr.insert(0, "0");
      }

      return hexStr.toString().toUpperCase();
   }

   public static String convertHexToBinary(String hex, int bitCount) {
      StringBuilder binaryStr = new StringBuilder((new BigInteger(hex, 16)).toString(2));

      while(binaryStr.length() < bitCount) {
         binaryStr.insert(0, "0");
      }

      return binaryStr.toString();
   }

   public static String convertBinaryToHex(String binary) {
      StringBuilder hexStr = new StringBuilder((new BigInteger(binary, 2)).toString(16));

      while(hexStr.length() < 2) {
         hexStr.insert(0, "0");
      }

      return hexStr.toString().toUpperCase();
   }

   public static int convertBinaryToDecimal(String binary) {
      return Integer.parseInt(binary, 2);
   }

   public static String convertBecimalToBinary(int decimal, int bitCount) {
      StringBuilder binaryStr = new StringBuilder(Integer.toBinaryString(decimal));

      while(binaryStr.length() < bitCount) {
         binaryStr.insert(0, "0");
      }

      return binaryStr.toString();
   }

   public static void showToast(Context mContext, String message, int duration) {
      Toast.makeText(mContext, message, duration).show();
   }

   public static void showToast(Context mContext, String message) {
      Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
   }

   public static String md5(String plainText) {
      String result = "";

      try {
         MessageDigest md = MessageDigest.getInstance("MD5");
         md.update(plainText.getBytes());
         byte[] b = md.digest();
         StringBuffer buf = new StringBuffer("");

         for(int offset = 0; offset < b.length; ++offset) {
            int i = b[offset];
            if (i < 0) {
               i += 256;
            }

            if (i < 16) {
               buf.append("0");
            }

            buf.append(Integer.toHexString(i));
         }

         result = buf.toString().toLowerCase();
         buf.toString().substring(8, 24);
      } catch (NoSuchAlgorithmException var7) {
         var7.printStackTrace();
      }

      return result;
   }
}
