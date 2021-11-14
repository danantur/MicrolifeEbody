// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.cloud.model;

import java.util.List;

public class BTEventList
{
    public int code;
    public String info;
    public int total_num;
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
    
    public int getTotal_num() {
        return this.total_num;
    }
    
    public void setTotal_num(final int total_num) {
        this.total_num = total_num;
    }
    
    public List<DataBean> getData() {
        return this.data;
    }
    
    public void setData(final List<DataBean> data) {
        this.data = data;
    }
    
    public static class DataBean
    {
        public int event_code;
        public String event;
        public String type;
        public String event_time;
        
        public int getEvent_code() {
            return this.event_code;
        }
        
        public void setEvent_code(final int event_code) {
            this.event_code = event_code;
        }
        
        public String getEvent() {
            return this.event;
        }
        
        public void setEvent(final String event) {
            this.event = event;
        }
        
        public String getType() {
            return this.type;
        }
        
        public void setType(final String type) {
            this.type = type;
        }
        
        public String getEvent_time() {
            return this.event_time;
        }
        
        public void setEvent_time(final String event_time) {
            this.event_time = event_time;
        }
    }
}
