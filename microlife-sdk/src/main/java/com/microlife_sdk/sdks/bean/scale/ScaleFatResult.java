// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.bean.scale;

public class ScaleFatResult
{
    private int userId;
    private float fat;
    private float weight;
    private int resistance;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int weekOfYear;
    private boolean isKGUnit;
    private boolean isSuspectedData;
    
    public ScaleFatResult() {
        this.isSuspectedData = false;
    }
    
    public void setUnitIsKG(final boolean isKGUnit) {
        this.isKGUnit = isKGUnit;
    }
    
    public boolean getUnitIsKG() {
        return this.isKGUnit;
    }
    
    public boolean isSuspectedData() {
        return this.isSuspectedData;
    }
    
    public void setSuspectedData(final boolean isSuspectedData) {
        this.isSuspectedData = isSuspectedData;
    }
    
    public int getUserId() {
        return this.userId;
    }
    
    public void setUserId(final int userId) {
        this.userId = userId;
    }
    
    public float getFat() {
        return this.fat;
    }
    
    public void setFat(final float fat) {
        this.fat = fat;
    }
    
    public float getWeight() {
        return this.weight;
    }
    
    public void setWeight(final float weight) {
        this.weight = weight;
    }
    
    public int getResistance() {
        return this.resistance;
    }
    
    public void setResistance(final int resistance) {
        this.resistance = resistance;
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
    
    public int getWeekOfYear() {
        return this.weekOfYear;
    }
    
    public void setWeekOfYear(final int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("fat measure test result.");
        sb.append("test time:");
        sb.append(this.year).append("-").append(this.month).append("-").append(this.day);
        sb.append(" ").append(this.hour).append(":").append(this.minute).append(":").append(this.second);
        sb.append(", fat:").append(this.fat);
        sb.append(", weight:").append(this.weight);
        sb.append(",resistance:").append(this.resistance);
        sb.append(",is suspectedData:");
        sb.append(this.isSuspectedData);
        return sb.toString();
    }
}
