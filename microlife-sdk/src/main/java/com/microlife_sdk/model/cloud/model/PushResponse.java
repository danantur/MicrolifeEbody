// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.cloud.model;

import java.util.List;

public class PushResponse
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
        public int push_id;
        public String device_type;
        public String title;
        public String content;
        public String link_url;
        public int is_forever;
        public String start_time;
        public String end_time;
        
        public int getPush_id() {
            return this.push_id;
        }
        
        public void setPush_id(final int push_id) {
            this.push_id = push_id;
        }
        
        public String getDevice_type() {
            return this.device_type;
        }
        
        public void setDevice_type(final String device_type) {
            this.device_type = device_type;
        }
        
        public String getTitle() {
            return this.title;
        }
        
        public void setTitle(final String title) {
            this.title = title;
        }
        
        public String getContent() {
            return this.content;
        }
        
        public void setContent(final String content) {
            this.content = content;
        }
        
        public String getLink_url() {
            return this.link_url;
        }
        
        public void setLink_url(final String link_url) {
            this.link_url = link_url;
        }
        
        public int getIs_forever() {
            return this.is_forever;
        }
        
        public void setIs_forever(final int is_forever) {
            this.is_forever = is_forever;
        }
        
        public String getStart_time() {
            return this.start_time;
        }
        
        public void setStart_time(final String start_time) {
            this.start_time = start_time;
        }
        
        public String getEnd_time() {
            return this.end_time;
        }
        
        public void setEnd_time(final String end_time) {
            this.end_time = end_time;
        }
    }
}
