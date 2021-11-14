// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.library;

import android.util.DisplayMetrics;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.app.Activity;
import com.microlife_sdk.model.abcdef.f;

public class CustomActivity extends Activity
{
    public static SharedPreferences a;
    public static SharedPreferences.Editor b;
    
    private void a() {
        if (CustomActivity.a == null || CustomActivity.b == null) {
            CustomActivity.a = this.getApplicationContext().getSharedPreferences("ting_color_data", 0);
            CustomActivity.b = CustomActivity.a.edit();
        }
    }
    
    public static void a(final String s, final int n) {
        final SharedPreferences.Editor b;
        if ((b = CustomActivity.b) != null) {
            b.putInt(s, n);
            CustomActivity.b.commit();
        }
    }
    
    public static int b(final String s, final int n) {
        final SharedPreferences a;
        if ((a = CustomActivity.a) != null) {
            return a.getInt(s, n);
        }
        return 0;
    }
    
    public static void a(final String s, final float n) {
        final SharedPreferences.Editor b;
        if ((b = CustomActivity.b) != null) {
            b.putFloat(s, n);
            CustomActivity.b.commit();
        }
    }
    
    public static float b(final String s, final float n) {
        final SharedPreferences a;
        if ((a = CustomActivity.a) != null) {
            return a.getFloat(s, n);
        }
        return 0.0f;
    }
    
    public static void a(final String s, final String s2) {
        final SharedPreferences.Editor b;
        if ((b = CustomActivity.b) != null) {
            b.putString(s, s2);
            CustomActivity.b.commit();
        }
    }
    
    public static String b(final String s, final String s2) {
        final SharedPreferences a;
        if ((a = CustomActivity.a) != null) {
            return a.getString(s, s2);
        }
        return "";
    }
    
    public static void a(final String s, final boolean b) {
        final SharedPreferences.Editor b2;
        if ((b2 = CustomActivity.b) != null) {
            b2.putBoolean(s, b);
            CustomActivity.b.commit();
        }
    }
    
    public static boolean b(final String s, final boolean b) {
        final SharedPreferences a;
        return (a = CustomActivity.a) != null && a.getBoolean(s, b);
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.a();
        if (f.a == 0.0f) {
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            final DisplayMetrics displayMetrics2 = displayMetrics;
            new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics2);
            f.a = displayMetrics.density;
            f.b = displayMetrics.widthPixels;
            f.c = displayMetrics.heightPixels;
            final int b = f.b;
            int c;
            int b2;
            if (b > (c = f.c)) {
                final int n = b;
                b2 = c;
                c = n;
            }
            else {
                b2 = f.b;
            }
            f.d = c / (float)b2;
            f.e = (f.d >= 1.4f);
        }
    }
    
    public void onDestroy() {
        super.onDestroy();
    }
}
