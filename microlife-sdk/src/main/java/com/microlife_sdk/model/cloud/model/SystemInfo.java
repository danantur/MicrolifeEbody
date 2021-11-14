// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.cloud.model;

public class SystemInfo
{
    public int code;
    public String info;
    public String api_key;
    
    public int getCode() {
        return this.code;
    }
    
    public void setCode(final int code) {
        this.code = code;
    }
    
    public String getInfo() {
        return this.info;
    }
    
    public void setInfo(final String info) {
        this.info = info;
    }
    
    public String getApi_key() {
        return this.api_key;
    }
    
    public void setApi_key(final String api_key) {
        this.api_key = api_key;
    }
    
    @Override
    public String toString() {
        return "SystemInfo{code=" + this.code + ", info='" + this.info + '\'' + ", api_key='" + this.api_key + '\'' + '}';
    }
}
