package com.microlife_sdk.model.abcdef;

import com.microlife_sdk.ideabuslibrary.util.BaseUtils;

// 
// Decompiled by Procyon v0.5.36
// 

public class e
{
    public StringBuilder a;
    
    public e(final String str) {
        this.a = new StringBuilder(str);
    }
    
    public int c(final int end) {
        final int int1 = Integer.parseInt(this.a.substring(0, end), 16);
        this.a.delete(0, end);
        return int1;
    }
    
    public String a(final int end) {
        final String substring = this.a.substring(0, end);
        this.a.delete(0, end);
        return substring;
    }
    
    public String b(final int n) {
        final String a = this.a(n);
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < a.length()) {
            final StringBuilder sb2 = sb;
            final String s = a;
            final int beginIndex = i;
            sb2.append((char)Integer.parseInt(s.substring(beginIndex, i = beginIndex + 2), 16));
        }
        return sb.toString();
    }
    
    public String a(int i, final boolean b) {
        int length;
        if (this.a.length() > i) {
            length = i;
        }
        else {
            length = this.a.length();
        }
        BaseUtils.printLog("d", "dd", "hexString.length(): " + this.a.length() + " retLength: " + length);
        final String substring = this.a.substring(0, length);
        final StringBuilder sb = new StringBuilder();
        char[] charArray;
        for (int length2 = (charArray = substring.toCharArray()).length, j = 0; j < length2; ++j) {
            sb.append(Integer.toHexString(charArray[j]));
        }
        int n;
        String str;
        for (n = i - substring.length(), i = 0; i < n; ++i) {
            if (b) {
                str = "00";
            }
            else {
                str = "20";
            }
            sb.append(str);
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.a.toString();
    }
    
    public int a() {
        return this.a.length();
    }
}
