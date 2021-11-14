// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

public class User
{
    public int NO;
    public String ID;
    public int age;
    
    public void setNO(final int no) {
        this.NO = no;
    }
    
    public int getNO() {
        return this.NO;
    }
    
    public void setID(final String id) {
        this.ID = id;
    }
    
    public String getID() {
        return this.ID;
    }
    
    public void setAge(final int age) {
        this.age = age;
    }
    
    public int getAge() {
        return this.age;
    }
    
    @Override
    public String toString() {
        return "User{NO=" + this.NO + ", ID='" + this.ID + '\'' + ", age=" + this.age + "}";
    }
}
