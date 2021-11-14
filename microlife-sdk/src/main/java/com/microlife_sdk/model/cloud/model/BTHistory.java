// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.cloud.model;

import java.util.List;

public class BTHistory
{
    public int code;
    public String info;
    public int total_num;
    public String api;
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
    
    public String getApi() {
        return this.api;
    }
    
    public void setApi(final String api) {
        this.api = api;
    }
    
    public List<DataBean> getData() {
        return this.data;
    }
    
    public void setData(final List<DataBean> data) {
        this.data = data;
    }
    
    public static class DataBean
    {
        public int bt_id;
        public float body_temp;
        public String date;
        public String photo;
        public String note;
        public String recording;
        public String recording_time;
        
        public int getBt_id() {
            return this.bt_id;
        }
        
        public void setBt_id(final int bt_id) {
            this.bt_id = bt_id;
        }
        
        public float getBody_temp() {
            return this.body_temp;
        }
        
        public void setBody_temp(final float body_temp) {
            this.body_temp = body_temp;
        }
        
        public String getDate() {
            return this.date;
        }
        
        public void setDate(final String date) {
            this.date = date;
        }
        
        public String getPhoto() {
            return this.photo;
        }
        
        public void setPhoto(final String photo) {
            this.photo = photo;
        }
        
        public String getNote() {
            return this.note;
        }
        
        public void setNote(final String note) {
            this.note = note;
        }
        
        public String getRecording() {
            return this.recording;
        }
        
        public void setRecording(final String recording) {
            this.recording = recording;
        }
        
        public String getRecording_time() {
            return this.recording_time;
        }
        
        public void setRecording_time(final String recording_time) {
            this.recording_time = recording_time;
        }
    }
}
