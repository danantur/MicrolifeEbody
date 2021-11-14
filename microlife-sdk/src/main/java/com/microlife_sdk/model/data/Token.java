// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

public class Token
{
    public int code;
    public String info;
    public String access_token;
    public String refresh_token;
    
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
    
    public String getAccess_token() {
        return this.access_token;
    }
    
    public void setAccess_token(final String access_token) {
        this.access_token = access_token;
    }
    
    public String getRefresh_token() {
        return this.refresh_token;
    }
    
    public void setRefresh_token(final String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
