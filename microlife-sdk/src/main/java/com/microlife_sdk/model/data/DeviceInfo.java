// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

import java.util.Arrays;

public class DeviceInfo
{
    public String ID;
    public String connectType;
    public int[] errHappendTimes;
    public int measurementTimes;
    public Boolean isTimeReady;
    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;
    public Boolean openNocturnal;
    public String sn;
    
    public DeviceInfo() {
        this.errHappendTimes = new int[16];
    }
    
    public void setID(final String id) {
        this.ID = id;
    }
    
    public String getID() {
        return this.ID;
    }
    
    public void setConnectType(final String connectType) {
        this.connectType = connectType;
    }
    
    public String getConnectType() {
        return this.connectType;
    }
    
    public int getErrHappendTimes(final int n) {
        return this.errHappendTimes[n];
    }
    
    public void setErrHappendTimes(final int n, final int n2) {
        this.errHappendTimes[n] = n2;
    }
    
    public void setMeasurementTimes(final int measurementTimes) {
        this.measurementTimes = measurementTimes;
    }
    
    public int getMeasurementTimes() {
        return this.measurementTimes;
    }
    
    public void setisTimeReady(final Boolean isTimeReady) {
        this.isTimeReady = isTimeReady;
    }
    
    public boolean getisTimeReady() {
        return this.isTimeReady;
    }
    
    public void setOpenNocturnal(final Boolean openNocturnal) {
        this.openNocturnal = openNocturnal;
    }
    
    public boolean getopenNocturnal() {
        return this.openNocturnal;
    }
    
    public int getYear() {
        return this.year;
    }
    
    public void setYear(final int year) {
        this.year = year;
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
    
    public int getMinute() {
        return this.minute;
    }
    
    public void setMinute(final int minute) {
        this.minute = minute;
    }
    
    public int getSecond() {
        return this.second;
    }
    
    public void setSecond(final int second) {
        this.second = second;
    }
    
    public String getSn() {
        return this.sn;
    }
    
    public void setSn(final String sn) {
        this.sn = sn;
    }
    
    @Override
    public String toString() {
        return "DeviceInfo{ID='" + this.ID + '\'' + ", errHappendTimes=" + Arrays.toString(this.errHappendTimes) + ", measurementTimes=" + this.measurementTimes + ", isTimeReady=" + this.isTimeReady + ", openNocturnal=" + this.openNocturnal + ", year=" + this.year + ", month=" + this.month + ", day=" + this.day + ", hour=" + this.hour + ", minute=" + this.minute + ", second=" + this.second + ", sn=" + this.sn + "}";
    }
}
