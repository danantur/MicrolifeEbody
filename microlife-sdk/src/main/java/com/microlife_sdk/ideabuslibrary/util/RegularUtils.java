package com.microlife_sdk.ideabuslibrary.util;

import android.text.TextUtils;
import java.util.regex.Pattern;

public class RegularUtils {
   private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
   private static final String REGEX_PASSWORD = "^(?!.*[^a-zA-Z0-9])(?=.*\\d)(?=.*[a-zA-Z]).{6,12}$";

   public static boolean isMatch(String regex, String string) {
      return !TextUtils.isEmpty(string) && Pattern.matches(regex, string);
   }

   public static boolean isEmail(String email) {
      return isMatch("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email);
   }

   public static boolean isPassword(String password) {
      return isMatch("^(?!.*[^a-zA-Z0-9])(?=.*\\d)(?=.*[a-zA-Z]).{6,12}$", password);
   }
}
