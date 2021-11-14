// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.cloud.model;

import java.util.List;

public class MailList
{
    public int code;
    public String info;
    public List<DataBean> data;
    
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
    
    public List<DataBean> getData() {
        return this.data;
    }
    
    public void setData(final List<DataBean> data) {
        this.data = data;
    }
    
    public static class DataBean
    {
        public int mail_id;
        public String name;
        public String email;
        
        public int getMail_id() {
            return this.mail_id;
        }
        
        public void setMail_id(final int mail_id) {
            this.mail_id = mail_id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public void setName(final String name) {
            this.name = name;
        }
        
        public String getEmail() {
            return this.email;
        }
        
        public void setEmail(final String email) {
            this.email = email;
        }
    }
}
