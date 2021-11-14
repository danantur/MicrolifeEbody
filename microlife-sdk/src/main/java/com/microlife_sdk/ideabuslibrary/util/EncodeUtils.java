package com.microlife_sdk.ideabuslibrary.util;

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class EncodeUtils {
   private EncodeUtils() {
      throw new UnsupportedOperationException("error");
   }

   public static String urlEncode(String input) {
      return urlEncode(input, "UTF-8");
   }

   public static String urlEncode(String input, String charset) {
      try {
         return URLEncoder.encode(input, charset);
      } catch (UnsupportedEncodingException var3) {
         return input;
      }
   }

   public static String urlDncode(String input) {
      return urlDecode(input, "UTF-8");
   }

   public static String urlDecode(String input, String charset) {
      try {
         return URLDecoder.decode(input, charset);
      } catch (UnsupportedEncodingException var3) {
         return input;
      }
   }

   public static String base64Encode(String input) {
      return base64Encode(input.getBytes());
   }

   public static String base64Encode(byte[] input) {
      return Base64.encodeToString(input, 0);
   }

   public static String base64Decode(String input) {
      return new String(Base64.decode(input, 0));
   }
}
