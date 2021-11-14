package com.microlife_sdk.ideabuslibrary.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TypefaceTextView extends TextView {
   public TypefaceTextView(Context context) {
      super(context);
   }

   public TypefaceTextView(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   public void setTypeface(Context context, String fontsPath, int style) {
      Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontsPath);
      this.setTypeface(typeface, style);
   }

   public void setTypeface(Context context, String fontsPath) {
      this.setTypeface(context, fontsPath, 0);
   }
}
