package com.microlife_sdk.model.abcdef;

import java.math.BigInteger;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.text.NumberFormat;
import android.util.Log;

// 
// Decompiled by Procyon v0.5.36
// 

public class f
{
    public static float a = 0.0f;
    public static int b = 0;
    public static int c = 0;
    public static float d = 0.0f;
    public static boolean e;
    public static boolean f = true;
    public static boolean g = true;
    
    public static void a(final String s, final String s2, final String s3) {
        int n = 0;
        if (s.equals("i")) {
            n = 4;
        }
        else if (s.equals("d")) {
            n = 3;
        }
        else if (s.equals("e")) {
            n = 6;
        }
        if (f) {
            Log.println(n, s2, s3);
        }
    }
    
    public static float a(final float n) {
        return n * a;
    }
    
    public static float b(final float n) {
        return n / a;
    }
    
    public static String a(final String s) {
        if (s.length() < 2) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            final StringBuilder sb2 = sb;
            final int beginIndex = i;
            sb2.append((char)Integer.parseInt(s.substring(beginIndex, i = beginIndex + 2), 16));
        }
        return sb.toString();
    }
    
    public static String a(final String obj, final int minimumIntegerDigits) {
        final NumberFormat instance = NumberFormat.getInstance();
        final NumberFormat numberFormat;
        (numberFormat = instance).setGroupingUsed(false);
        numberFormat.setMinimumIntegerDigits(minimumIntegerDigits);
        return instance.format(obj);
    }
    
    public static String b(final int n, final int minimumIntegerDigits) {
        final NumberFormat instance = NumberFormat.getInstance();
        final NumberFormat numberFormat;
        (numberFormat = instance).setGroupingUsed(false);
        numberFormat.setMinimumIntegerDigits(minimumIntegerDigits);
        return instance.format(n);
    }
    
    public static String a() {
        final Calendar instance = Calendar.getInstance();
        return instance.get(1) + "/" + b(instance.get(2) + 1, 2) + "/" + b(instance.get(5), 2) + " " + b(instance.get(11), 2) + ":" + b(instance.get(12), 2) + ":" + b(instance.get(13), 2);
    }
    
    public static String b(final byte[] array) {
        final StringBuilder sb = new StringBuilder("");
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; ++i) {
                final String hexString;
                if ((hexString = Integer.toHexString(array[i] & 0xFF)).length() < 2) {
                    sb.append(0);
                }
                sb.append(hexString);
            }
            return sb.toString().toUpperCase();
        }
        return null;
    }
    
    public static String c(final byte[] array) {
        final StringBuilder sb = new StringBuilder("");
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toString(array[i]));
            }
            return sb.toString();
        }
        return null;
    }
    
    public static String a(final byte[] bytes) {
        final String s = "";
        String s2;
        try {
            s2 = new String(bytes, "ISO8859-1");
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            s2 = s;
        }
        return s2;
    }
    
    public static byte[] d(final String s) {
        final char[] charArray;
        final int n;
        final byte[] array = new byte[n = (charArray = s.toCharArray()).length / 2];
        for (int i = 0; i < n; ++i) {
            final char[] array2 = charArray;
            final int n2 = i * 2;
            int n3;
            if ((n3 = (Character.digit(array2[n2], 16) << 4 | Character.digit(charArray[n2 + 1], 16))) > 127) {
                n3 -= 256;
            }
            array[i] = (byte)n3;
        }
        return array;
    }
    
    public static String a(final int i) {
        final StringBuilder sb = new StringBuilder(Integer.toHexString(i));
        while (sb.length() < 2) {
            sb.insert(0, "0");
        }
        return sb.toString().toUpperCase();
    }
    
    public static String b(final String val, final int n) {
        StringBuilder insert;
        for (insert = new StringBuilder(new BigInteger(val, 16).toString(2)); insert.length() < n; insert = insert.insert(0, "0")) {}
        return insert.toString();
    }
    
    public static String c(final String val) {
        StringBuilder insert;
        for (insert = new StringBuilder(new BigInteger(val, 2).toString(16)); insert.length() < 2; insert = insert.insert(0, "0")) {}
        return insert.toString().toUpperCase();
    }
    
    public static int b(final String s) {
        return Integer.parseInt(s, 2);
    }
    
    public static String a(final int i, final int n) {
        StringBuilder insert;
        for (insert = new StringBuilder(Integer.toBinaryString(i)); insert.length() < n; insert = insert.insert(0, "0")) {}
        return insert.toString();
    }
}
