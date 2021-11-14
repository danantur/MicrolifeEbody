// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.cloud.model;

public class Login
{
    public int code;
    public String info;
    public int account_id;
    public DataBean data;
    
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
    
    public int getAccount_id() {
        return this.account_id;
    }
    
    public void setAccount_id(final int account_id) {
        this.account_id = account_id;
    }
    
    public DataBean getData() {
        return this.data;
    }
    
    public void setData(final DataBean data) {
        this.data = data;
    }
    
    public static class DataBean
    {
        public String name;
        public String email;
        public String photo;
        public String birthday;
        public String gender;
        public int height;
        public int unit_type;
        public float weight;
        public float sys;
        public int sys_unit;
        public float dia;
        public float goal_weight;
        public float body_fat;
        public int sys_activity;
        public int dia_activity;
        public int weight_activity;
        public int body_fat_activity;
        public int register_type;
        public int bmi_activity;
        public float bmi;
        public String conditions;
        public int threshold;
        public int bp_measurement_arm;
        public int cuff_size;
        public int date_format;
        
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
        
        public String getPhoto() {
            return this.photo;
        }
        
        public void setPhoto(final String photo) {
            this.photo = photo;
        }
        
        public String getBirthday() {
            return this.birthday;
        }
        
        public void setBirthday(final String birthday) {
            this.birthday = birthday;
        }
        
        public String getGender() {
            return this.gender;
        }
        
        public void setGender(final String gender) {
            this.gender = gender;
        }
        
        public int getHeight() {
            return this.height;
        }
        
        public void setHeight(final int height) {
            this.height = height;
        }
        
        public int getUnit_type() {
            return this.unit_type;
        }
        
        public void setUnit_type(final int unit_type) {
            this.unit_type = unit_type;
        }
        
        public float getWeight() {
            return this.weight;
        }
        
        public void setWeight(final float weight) {
            this.weight = weight;
        }
        
        public float getSys() {
            return this.sys;
        }
        
        public void setSys(final float sys) {
            this.sys = sys;
        }
        
        public int getSys_unit() {
            return this.sys_unit;
        }
        
        public void setSys_unit(final int sys_unit) {
            this.sys_unit = sys_unit;
        }
        
        public float getDia() {
            return this.dia;
        }
        
        public void setDia(final float dia) {
            this.dia = dia;
        }
        
        public float getGoal_weight() {
            return this.goal_weight;
        }
        
        public void setGoal_weight(final float goal_weight) {
            this.goal_weight = goal_weight;
        }
        
        public float getBody_fat() {
            return this.body_fat;
        }
        
        public void setBody_fat(final float body_fat) {
            this.body_fat = body_fat;
        }
        
        public int getSys_activity() {
            return this.sys_activity;
        }
        
        public void setSys_activity(final int sys_activity) {
            this.sys_activity = sys_activity;
        }
        
        public int getDia_activity() {
            return this.dia_activity;
        }
        
        public void setDia_activity(final int dia_activity) {
            this.dia_activity = dia_activity;
        }
        
        public int getWeight_activity() {
            return this.weight_activity;
        }
        
        public void setWeight_activity(final int weight_activity) {
            this.weight_activity = weight_activity;
        }
        
        public int getBody_fat_activity() {
            return this.body_fat_activity;
        }
        
        public void setBody_fat_activity(final int body_fat_activity) {
            this.body_fat_activity = body_fat_activity;
        }
        
        public int getRegister_type() {
            return this.register_type;
        }
        
        public void setRegister_type(final int register_type) {
            this.register_type = register_type;
        }
        
        public int getBmi_activity() {
            return this.bmi_activity;
        }
        
        public void setBmi_activity(final int bmi_activity) {
            this.bmi_activity = bmi_activity;
        }
        
        public float getBmi() {
            return this.bmi;
        }
        
        public void setBmi(final float bmi) {
            this.bmi = bmi;
        }
        
        public String getConditions() {
            return this.conditions;
        }
        
        public void setConditions(final String conditions) {
            this.conditions = conditions;
        }
        
        public int getThreshold() {
            return this.threshold;
        }
        
        public void setThreshold(final int threshold) {
            this.threshold = threshold;
        }
        
        public int getBp_measurement_arm() {
            return this.bp_measurement_arm;
        }
        
        public void setBp_measurement_arm(final int bp_measurement_arm) {
            this.bp_measurement_arm = bp_measurement_arm;
        }
        
        public int getCuff_size() {
            return this.cuff_size;
        }
        
        public void setCuff_size(final int cuff_size) {
            this.cuff_size = cuff_size;
        }
        
        public int getDate_format() {
            return this.date_format;
        }
        
        public void setDate_format(final int date_format) {
            this.date_format = date_format;
        }
    }
}
