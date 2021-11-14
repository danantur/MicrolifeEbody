// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.cloud.model;

import java.util.List;

public class BPMHistory
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
        public int bpm_id;
        public int sys;
        public int dia;
        public int pul;
        public String date;
        public String user_id;
        public int afib;
        public int pad;
        public int mode;
        public int man;
        public int cuffokr;
        public String photo;
        public String note;
        public String recording;
        public String recording_time;
        
        public int getBpm_id() {
            return this.bpm_id;
        }
        
        public void setBpm_id(final int bpm_id) {
            this.bpm_id = bpm_id;
        }
        
        public int getSys() {
            return this.sys;
        }
        
        public void setSys(final int sys) {
            this.sys = sys;
        }
        
        public int getDia() {
            return this.dia;
        }
        
        public void setDia(final int dia) {
            this.dia = dia;
        }
        
        public int getPul() {
            return this.pul;
        }
        
        public void setPul(final int pul) {
            this.pul = pul;
        }
        
        public String getDate() {
            return this.date;
        }
        
        public void setDate(final String date) {
            this.date = date;
        }
        
        public String getUser_id() {
            return this.user_id;
        }
        
        public void setUser_id(final String user_id) {
            this.user_id = user_id;
        }
        
        public int getAfib() {
            return this.afib;
        }
        
        public void setAfib(final int afib) {
            this.afib = afib;
        }
        
        public int getPad() {
            return this.pad;
        }
        
        public void setPad(final int pad) {
            this.pad = pad;
        }
        
        public int getMode() {
            return this.mode;
        }
        
        public void setMode(final int n) {
            this.mode = n;
            this.man = n;
        }
        
        public int getMan() {
            return this.man;
        }
        
        public void setMan(final int man) {
            this.man = man;
        }
        
        public int getCuffokr() {
            return this.cuffokr;
        }
        
        public void setCuffokr(final int cuffokr) {
            this.cuffokr = cuffokr;
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
