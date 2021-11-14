// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

import com.microlife_sdk.model.abcdef.e;

public class EBodyMeasureData
{
    public int unit;
    public float weight;
    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;
    public int althleteLevel;
    public int gender;
    public int age;
    public int height;
    public float fat;
    public float water;
    public float muscle;
    public float bone;
    public float visceraFat;
    public float kcal;
    public float bmi;
    
    public void importHexString(final String s) {
        new e(s);
    }
    
    public int getUnit() {
        return this.unit;
    }
    
    public void setUnit(final int unit) {
        this.unit = unit;
    }
    
    public float getWeight() {
        return this.weight;
    }
    
    public void setWeight(final float weight) {
        this.weight = weight;
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
    
    public int getAlthleteLevel() {
        return this.althleteLevel;
    }
    
    public void setAlthleteLevel(final int althleteLevel) {
        this.althleteLevel = althleteLevel;
    }
    
    public int getGender() {
        return this.gender;
    }
    
    public void setGender(final int gender) {
        this.gender = gender;
    }
    
    public int getAge() {
        return this.age;
    }
    
    public void setAge(final int age) {
        this.age = age;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public float getFat() {
        return this.fat;
    }
    
    public void setFat(final float fat) {
        this.fat = fat;
    }
    
    public float getWater() {
        return this.water;
    }
    
    public void setWater(final float water) {
        this.water = water;
    }
    
    public float getMuscle() {
        return this.muscle;
    }
    
    public void setMuscle(final float muscle) {
        this.muscle = muscle;
    }
    
    public float getBone() {
        return this.bone;
    }
    
    public void setBone(final float bone) {
        this.bone = bone;
    }
    
    public float getVisceraFat() {
        return this.visceraFat;
    }
    
    public void setVisceraFat(final float visceraFat) {
        this.visceraFat = visceraFat;
    }
    
    public float getKcal() {
        return this.kcal;
    }
    
    public void setKcal(final float kcal) {
        this.kcal = kcal;
    }
    
    public void setBmi(final float bmi) {
        this.bmi = bmi;
    }
    
    public float getBMI() {
        return this.bmi;
    }
    
    @Override
    public String toString() {
        return "EBodyMeasureData{unit=" + this.unit + ", weight=" + this.weight + ", year=" + this.year + ", month=" + this.month + ", day=" + this.day + ", hour=" + this.hour + ", minute=" + this.minute + ", second=" + this.second + ", althleteLevel=" + this.althleteLevel + ", gender=" + this.gender + ", age=" + this.age + ", bmi=" + this.bmi + ", fat=" + this.fat + ", water=" + this.water + ", muscle=" + this.muscle + ", bone=" + this.bone + ", visceraFat=" + this.visceraFat + ", kcal=" + this.kcal + "}";
    }
}
