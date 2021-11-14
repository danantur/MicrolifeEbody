// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.bean.scale;

public class ScaleUserInfo
{
    private static ScaleUserInfo instance;
    private String userId;
    private int sex;
    private int height;
    private int roleType;
    private int age;
    private float weight;
    private int impedance;
    
    public static ScaleUserInfo getScaleUserInfo() {
        if (ScaleUserInfo.instance == null) {
            synchronized (ScaleUserInfo.class) {
                ScaleUserInfo.instance = new ScaleUserInfo();
            }
        }
        return ScaleUserInfo.instance;
    }
    
    private ScaleUserInfo() {
        this.impedance = 500;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(final String userId) {
        this.userId = userId;
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
    
    public int getAge() {
        return this.age;
    }
    
    public void setAge(final int age) {
        this.age = age;
    }
    
    public float getWeight() {
        return this.weight;
    }
    
    public void setWeight(final float weight) {
        this.weight = weight;
    }
    
    public int getImpedance() {
        return this.impedance;
    }
    
    public void setImpedance(final int impedance) {
        this.impedance = impedance;
    }
    
    @Override
    public String toString() {
        return "ScaleUserInfo{userId='" + this.userId + '\'' + ", sex=" + this.sex + ", height=" + this.height + ", roleType=" + this.roleType + ", age=" + this.age + ", weight=" + this.weight + ", impedance=" + this.impedance + '}';
    }
}
