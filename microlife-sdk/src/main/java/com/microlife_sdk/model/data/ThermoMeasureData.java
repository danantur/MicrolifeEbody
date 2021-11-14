// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

import com.microlife_sdk.model.abcdef.*;

public class ThermoMeasureData
{
    public static final int BODY_MODE = 0;
    public static final int OBJECT_MODE = 1;
    public static final int ERROR_FOR_AMBIENT_H = 128;
    public static final int ERROR_FOR_AMBIENT_L = 129;
    public static final int ERROR_FOR_BODY_H = 130;
    public static final int ERROR_FOR_BODY_L = 131;
    public float ambientTemperature;
    public float measureTemperature;
    public int mode;
    public int minute;
    public int month;
    public int day;
    public int hour;
    public int flagErr;
    public int flagFever;
    public int errorCode;
    public int year;
    
    public float getAmbientTemperature() {
        return this.ambientTemperature;
    }
    
    public void setAmbientTemperature(final float ambientTemperature) {
        this.ambientTemperature = ambientTemperature;
    }
    
    public float getMeasureTemperature() {
        return this.measureTemperature;
    }
    
    public void setMeasureTemperature(final float measureTemperature) {
        this.measureTemperature = measureTemperature;
    }
    
    public int getYear() {
        return this.year;
    }
    
    public void setYear(final int year) {
        this.year = year;
    }
    
    public int getErrorCode() {
        return this.errorCode;
    }
    
    public void setErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }
    
    public int getMinute() {
        return this.minute;
    }
    
    public void setMinute(final int minute) {
        this.minute = minute;
    }
    
    public int getMonth() {
        return this.month;
    }
    
    public void setMonth(final int month) {
        this.month = month;
    }
    
    public int getDay() {
        return this.day;
    }
    
    public void setDay(final int day) {
        this.day = day;
    }
    
    public int getHour() {
        return this.hour;
    }
    
    public void setHour(final int hour) {
        this.hour = hour;
    }
    
    public int getFlagErr() {
        return this.flagErr;
    }
    
    public void setFlagErr(final int flagErr) {
        this.flagErr = flagErr;
    }
    
    public int getFlagFever() {
        return this.flagFever;
    }
    
    public void setFlagFever(final int flagFever) {
        this.flagFever = flagFever;
    }
    
    public int getMode() {
        return this.mode;
    }
    
    public void setMode(final int mode) {
        this.mode = mode;
    }
    
    public void importHexString(final String s) {
        final e e = new e(s);
        final int c = e.c(4);
        final int c2 = e.c(4);
        final int c3 = e.c(2);
        final int c4 = e.c(2);
        final int c5 = e.c(2);
        final int c6;
        final int n = c6 = e.c(2);
        this.flagErr = (c6 & 0x80) >> 7;
        this.flagFever = (n & 0x40) >> 6;
        if (this.flagErr == 1 && this.flagFever == 0) {
            this.errorCode = c6;
        }
        else {
            final int minute = c5;
            final int n2 = c4;
            final int n3 = c3;
            final int n4 = c4;
            final int n5 = c3;
            final int n6 = c2;
            final int n7 = c2;
            final int n8 = c;
            this.year = (c6 & 0x3F);
            this.ambientTemperature = n8 / 100.0f;
            this.mode = (n7 & 0x8000) >> 15;
            this.measureTemperature = (n6 & 0x7FFF) / 100.0f;
            this.month = ((n4 & 0xC0) >> 6 | (n5 & 0xC0) >> 4);
            this.day = (n3 & 0x3F);
            this.hour = (n2 & 0x3F);
            this.minute = minute;
        }
    }
    
    @Override
    public String toString() {
        return "ThermoMeasureData{ambientTemperature=" + this.ambientTemperature + ", measureTemperature=" + this.measureTemperature + ", mode=" + this.mode + ", minute=" + this.minute + ", month=" + this.month + ", day=" + this.day + ", hour=" + this.hour + ", flagErr=" + this.flagErr + ", flagFever=" + this.flagFever + ", errorCode=" + this.errorCode + ", year=" + this.year + "}";
    }
}
