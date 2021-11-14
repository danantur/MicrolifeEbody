// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class NocturnalModeDRecord
{
    public static final String ZERO_CURRENT_DATA = "00000000000000";
    public static final int CURRENT_DATA_LENGTH = 14;
    public int index;
    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public List<CurrentAndMData> MData;
    
    public NocturnalModeDRecord() {
        this.MData = new ArrayList<CurrentAndMData>();
    }
    
    public int getDay() {
        return this.day;
    }
    
    public int getHour() {
        return this.hour;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public int getMinute() {
        return this.minute;
    }
    
    public int getMonth() {
        return this.month;
    }
    
    public int getYear() {
        return this.year;
    }
    
    public List<CurrentAndMData> getMData() {
        return this.MData;
    }
    
    public void setDay(final int day) {
        this.day = day;
    }
    
    public void setHour(final int hour) {
        this.hour = hour;
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    public void setMData(final List<CurrentAndMData> mData) {
        this.MData = mData;
    }
    
    public void setMinute(final int minute) {
        this.minute = minute;
    }
    
    public void setMonth(final int month) {
        this.month = month;
    }
    
    public void setYear(final int year) {
        this.year = year;
    }
    
    @Override
    public String toString() {
        return "DRecord{  index=" + this.index + ", year=" + this.year + ", month=" + this.month + ", day=" + this.day + ", hour=" + this.hour + ", minute=" + this.minute + ", MData=" + Arrays.toString(this.MData.toArray()) + "}";
    }
}
