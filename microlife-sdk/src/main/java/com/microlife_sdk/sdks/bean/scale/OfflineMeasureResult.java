// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.bean.scale;

public class OfflineMeasureResult
{
    public static final String TAG = "ScaleMeasureResult";
    public String userId;
    public int age;
    public int sex;
    public int height;
    public int roleType;
    public String measureTime;
    public float resistance;
    public float fat;
    public float weight;
    public float waterRate;
    public float bmr;
    public float visceralFat;
    public float muscleVolume;
    public float skeletalMuscle;
    public float boneVolume;
    public float bmi;
    public float protein;
    public float bodyScore;
    public float bodyAge;
    public float heartRate;
    public String bluetoothName;
    public String bluetoothAddress;
    public String weightUnit;
    public String fatUnit;
    public boolean isSuspectedData;
    
    public OfflineMeasureResult() {
        this.weightUnit = "kg";
        this.fatUnit = "%";
        this.isSuspectedData = false;
    }
    
    public boolean isSuspectedData() {
        return this.isSuspectedData;
    }
    
    public void setSuspectedData(final boolean suspectedData) {
        this.isSuspectedData = suspectedData;
    }
    
    public String getMeasureTime() {
        return this.measureTime;
    }
    
    public void setMeasureTime(final String measureTime) {
        this.measureTime = measureTime;
    }
    
    public float getResistance() {
        return this.resistance;
    }
    
    public void setResistance(final float resistance) {
        this.resistance = resistance;
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
    
    public float getWaterRate() {
        return this.waterRate;
    }
    
    public void setWaterRate(final float waterRate) {
        this.waterRate = waterRate;
    }
    
    public float getBmr() {
        return this.bmr;
    }
    
    public void setBmr(final float bmr) {
        this.bmr = bmr;
    }
    
    public float getVisceralFat() {
        return this.visceralFat;
    }
    
    public void setVisceralFat(final float visceralFat) {
        this.visceralFat = visceralFat;
    }
    
    public float getMuscleVolume() {
        return this.muscleVolume;
    }
    
    public void setMuscleVolume(final float muscleVolume) {
        this.muscleVolume = muscleVolume;
    }
    
    public float getBoneVolume() {
        return this.boneVolume;
    }
    
    public void setBoneVolume(final float boneVolume) {
        this.boneVolume = boneVolume;
    }
    
    public float getBmi() {
        return this.bmi;
    }
    
    public void setBmi(final float bmi) {
        this.bmi = bmi;
    }
    
    public float getProtein() {
        return this.protein;
    }
    
    public void setProtein(final float protein) {
        this.protein = protein;
    }
    
    public String getWeightUnit() {
        return this.weightUnit;
    }
    
    public void setWeightUnit(final String weightUnit) {
        this.weightUnit = weightUnit;
    }
    
    public String getFatUnit() {
        return this.fatUnit;
    }
    
    public void setFatUnit(final String fatUnit) {
        this.fatUnit = fatUnit;
    }
    
    public float getSkeletalMuscle() {
        return this.skeletalMuscle;
    }
    
    public void setSkeletalMuscle(final float skeletalMuscle) {
        this.skeletalMuscle = skeletalMuscle;
    }
    
    public float getBodyScore() {
        return this.bodyScore;
    }
    
    public void setBodyScore(final float bodyScore) {
        this.bodyScore = bodyScore;
    }
    
    public float getBodyAge() {
        return this.bodyAge;
    }
    
    public void setBodyAge(final float bodyAge) {
        this.bodyAge = bodyAge;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(final String userId) {
        this.userId = userId;
    }
    
    public int getAge() {
        return this.age;
    }
    
    public void setAge(final int age) {
        this.age = age;
    }
    
    public int getSex() {
        return this.sex;
    }
    
    public void setSex(final int sex) {
        this.sex = sex;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public int getRoleType() {
        return this.roleType;
    }
    
    public void setRoleType(final int roleType) {
        this.roleType = roleType;
    }
    
    public float getHeartRate() {
        return this.heartRate;
    }
    
    public void setHeartRate(final float heartRate) {
        this.heartRate = heartRate;
    }
    
    public String getBluetoothName() {
        return this.bluetoothName;
    }
    
    public void setBluetoothName(final String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }
    
    public String getBluetoothAddress() {
        return this.bluetoothAddress;
    }
    
    public void setBluetoothAddress(final String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }
    
    @Override
    public String toString() {
        return "OfflineMeasureResult{userId='" + this.userId + '\'' + ", age=" + this.age + ", sex=" + this.sex + ", height=" + this.height + ", roleType=" + this.roleType + ", measureTime='" + this.measureTime + '\'' + ", resistance=" + this.resistance + ", fat=" + this.fat + ", weight=" + this.weight + ", waterRate=" + this.waterRate + ", bmr=" + this.bmr + ", visceralFat=" + this.visceralFat + ", muscleVolume=" + this.muscleVolume + ", skeletalMuscle=" + this.skeletalMuscle + ", boneVolume=" + this.boneVolume + ", bmi=" + this.bmi + ", protein=" + this.protein + ", bodyScore=" + this.bodyScore + ", bodyAge=" + this.bodyAge + ", heartRate=" + this.heartRate + ", bluetoothName='" + this.bluetoothName + '\'' + ", bluetoothAddress='" + this.bluetoothAddress + '\'' + ", weightUnit='" + this.weightUnit + '\'' + ", fatUnit='" + this.fatUnit + '\'' + ", isSuspectedData=" + this.isSuspectedData + '}';
    }
}
