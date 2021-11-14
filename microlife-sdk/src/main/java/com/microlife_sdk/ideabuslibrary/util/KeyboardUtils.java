package com.microlife_sdk.ideabuslibrary.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtils {
   public static void hideSoftInput(Context mContext, EditText editText) {
      editText.setFocusable(false);
      editText.setFocusableInTouchMode(false);
      InputMethodManager inputMethodManager = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 2);
   }

   public static void showSoftInput(Context mContext, EditText editText) {
      editText.setFocusableInTouchMode(true);
      editText.requestFocus();
      InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.showSoftInput(editText, 1);
   }
}
