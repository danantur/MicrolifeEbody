// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.cloud.model;

import java.util.List;

public class DeviceList
{
    public int code;
    public String info;
    public int total_num;
    public List<BpmBean> bpm;
    public List<WeightBean> weight;
    public List<BtBean> bt;
    
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
    
    public List<BpmBean> getBpm() {
        return this.bpm;
    }
    
    public void setBpm(final List<BpmBean> bpm) {
        this.bpm = bpm;
    }
    
    public List<WeightBean> getWeight() {
        return this.weight;
    }
    
    public void setWeight(final List<WeightBean> weight) {
        this.weight = weight;
    }
    
    public List<BtBean> getBt() {
        return this.bt;
    }
    
    public void setBt(final List<BtBean> bt) {
        this.bt = bt;
    }
    
    public static class BtBean
    {
        public String device_model;
        public String mac_address;
        public ErrorCodeBeanXX error_code;
        
        public String getDevice_model() {
            return this.device_model;
        }
        
        public void setDevice_model(final String device_model) {
            this.device_model = device_model;
        }
        
        public String getMac_address() {
            return this.mac_address;
        }
        
        public void setMac_address(final String mac_address) {
            this.mac_address = mac_address;
        }
        
        public ErrorCodeBeanXX getError_code() {
            return this.error_code;
        }
        
        public void setError_code(final ErrorCodeBeanXX error_code) {
            this.error_code = error_code;
        }
        
        public static class ErrorCodeBeanXX
        {
            public int ErrorAmbH;
            public int ErrorAmbL;
            public int ErrorBodyH;
            public int ErrorBodyL;
            
            public int getErrorAmbH() {
                return this.ErrorAmbH;
            }
            
            public void setErrorAmbH(final int errorAmbH) {
                this.ErrorAmbH = errorAmbH;
            }
            
            public int getErrorAmbL() {
                return this.ErrorAmbL;
            }
            
            public void setErrorAmbL(final int errorAmbL) {
                this.ErrorAmbL = errorAmbL;
            }
            
            public int getErrorBodyH() {
                return this.ErrorBodyH;
            }
            
            public void setErrorBodyH(final int errorBodyH) {
                this.ErrorBodyH = errorBodyH;
            }
            
            public int getErrorBodyL() {
                return this.ErrorBodyL;
            }
            
            public void setErrorBodyL(final int errorBodyL) {
                this.ErrorBodyL = errorBodyL;
            }
        }
    }
    
    public static class WeightBean
    {
        public String device_model;
        public String mac_address;
        public ErrorCodeBeanX error_code;
        
        public String getDevice_model() {
            return this.device_model;
        }
        
        public void setDevice_model(final String device_model) {
            this.device_model = device_model;
        }
        
        public String getMac_address() {
            return this.mac_address;
        }
        
        public void setMac_address(final String mac_address) {
            this.mac_address = mac_address;
        }
        
        public ErrorCodeBeanX getError_code() {
            return this.error_code;
        }
        
        public void setError_code(final ErrorCodeBeanX error_code) {
            this.error_code = error_code;
        }
        
        public static class ErrorCodeBeanX
        {
            public int ErrorWeightOverload;
            public int ErrorImpedance;
            
            public int getErrorWeightOverload() {
                return this.ErrorWeightOverload;
            }
            
            public void setErrorWeightOverload(final int errorWeightOverload) {
                this.ErrorWeightOverload = errorWeightOverload;
            }
            
            public int getErrorImpedance() {
                return this.ErrorImpedance;
            }
            
            public void setErrorImpedance(final int errorImpedance) {
                this.ErrorImpedance = errorImpedance;
            }
        }
    }
    
    public static class BpmBean
    {
        public String device_model;
        public String mac_address;
        public ErrorCodeBean error_code;
        
        public String getDevice_model() {
            return this.device_model;
        }
        
        public void setDevice_model(final String device_model) {
            this.device_model = device_model;
        }
        
        public String getMac_address() {
            return this.mac_address;
        }
        
        public void setMac_address(final String mac_address) {
            this.mac_address = mac_address;
        }
        
        public ErrorCodeBean getError_code() {
            return this.error_code;
        }
        
        public void setError_code(final ErrorCodeBean error_code) {
            this.error_code = error_code;
        }
        
        public static class ErrorCodeBean
        {
            public int Error1;
            public int Error2;
            public int Error3;
            public int Error5;
            
            public int getError1() {
                return this.Error1;
            }
            
            public void setError1(final int error1) {
                this.Error1 = error1;
            }
            
            public int getError2() {
                return this.Error2;
            }
            
            public void setError2(final int error2) {
                this.Error2 = error2;
            }
            
            public int getError3() {
                return this.Error3;
            }
            
            public void setError3(final int error3) {
                this.Error3 = error3;
            }
            
            public int getError5() {
                return this.Error5;
            }
            
            public void setError5(final int error5) {
                this.Error5 = error5;
            }
        }
    }
}
