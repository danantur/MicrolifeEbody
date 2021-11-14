// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.cloud.model;

import java.util.List;

public class WeightHistory
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
        public int weight_id;
        public float weight;
        public float bmi;
        public float body_fat;
        public float water;
        public float skeleton;
        public float muscle;
        public float bmr;
        public float organ_fat;
        public String date;
        public String photo;
        public String note;
        public String recording;
        public String recording_time;
        
        public int getWeight_id() {
            return this.weight_id;
        }
        
        public void setWeight_id(final int weight_id) {
            this.weight_id = weight_id;
        }
        
        public float getWeight() {
            return this.weight;
        }
        
        public void setWeight(final float weight) {
            this.weight = weight;
        }
        
        public float getBmi() {
            return this.bmi;
        }
        
        public void setBmi(final float bmi) {
            this.bmi = bmi;
        }
        
        public float getBody_fat() {
            return this.body_fat;
        }
        
        public void setBody_fat(final float body_fat) {
            this.body_fat = body_fat;
        }
        
        public float getWater() {
            return this.water;
        }
        
        public void setWater(final float water) {
            this.water = water;
        }
        
        public float getSkeleton() {
            return this.skeleton;
        }
        
        public void setSkeleton(final float skeleton) {
            this.skeleton = skeleton;
        }
        
        public float getMuscle() {
            return this.muscle;
        }
        
        public void setMuscle(final float muscle) {
            this.muscle = muscle;
        }
        
        public float getBmr() {
            return this.bmr;
        }
        
        public void setBmr(final float bmr) {
            this.bmr = bmr;
        }
        
        public float getOrgan_fat() {
            return this.organ_fat;
        }
        
        public void setOrgan_fat(final float organ_fat) {
            this.organ_fat = organ_fat;
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
