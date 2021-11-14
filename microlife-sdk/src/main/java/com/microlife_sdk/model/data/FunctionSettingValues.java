// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

import com.microlife_sdk.model.abcdef.e;

public class FunctionSettingValues
{
    public boolean Fun_SEL_AFib;
    public boolean Fun_SEL_CBP_algo;
    public boolean Fun_SEL_AMPM;
    public boolean Fun_SEL_CBP_CL;
    public boolean Fun_SEL_AutoTest;
    public boolean Fun_SEL_Bluetooth;
    public boolean Fun_SEL_UnitKpa;
    public boolean Fun_SEL_RS232;
    
    public void importHexString(final String s, int fun_SEL_UnitKpa) {
        final int c;
        if (((c = new e(s).c(2)) & 0x1) == 0x1) {
            fun_SEL_UnitKpa = 1;
        }
        else {
            fun_SEL_UnitKpa = 0;
        }
        final int n = c;
        this.Fun_SEL_AFib = (fun_SEL_UnitKpa != 0);
        if ((n & 0x2) >> 1 == 1) {
            fun_SEL_UnitKpa = 1;
        }
        else {
            fun_SEL_UnitKpa = 0;
        }
        final int n2 = c;
        this.Fun_SEL_CBP_algo = (fun_SEL_UnitKpa != 0);
        if ((n2 & 0x4) >> 2 == 1) {
            fun_SEL_UnitKpa = 1;
        }
        else {
            fun_SEL_UnitKpa = 0;
        }
        final int n3 = c;
        this.Fun_SEL_AMPM = (fun_SEL_UnitKpa != 0);
        if ((n3 & 0x8) >> 3 == 1) {
            fun_SEL_UnitKpa = 1;
        }
        else {
            fun_SEL_UnitKpa = 0;
        }
        final int n4 = c;
        this.Fun_SEL_CBP_CL = (fun_SEL_UnitKpa != 0);
        if ((n4 & 0x10) >> 4 == 1) {
            fun_SEL_UnitKpa = 1;
        }
        else {
            fun_SEL_UnitKpa = 0;
        }
        final int n5 = c;
        this.Fun_SEL_AutoTest = (fun_SEL_UnitKpa != 0);
        if ((n5 & 0x20) >> 5 == 1) {
            fun_SEL_UnitKpa = 1;
        }
        else {
            fun_SEL_UnitKpa = 0;
        }
        final int n6 = c;
        this.Fun_SEL_Bluetooth = (fun_SEL_UnitKpa != 0);
        if ((n6 & 0x40) >> 6 == 1) {
            fun_SEL_UnitKpa = 1;
        }
        else {
            fun_SEL_UnitKpa = 0;
        }
        final int n7 = c;
        this.Fun_SEL_UnitKpa = (fun_SEL_UnitKpa != 0);
        this.Fun_SEL_RS232 = ((n7 & 0x80) >> 7 == 1);
    }
    
    public boolean isFun_SEL_AFib() {
        return this.Fun_SEL_AFib;
    }
    
    public void setFun_SEL_AFib(final boolean fun_SEL_AFib) {
        this.Fun_SEL_AFib = fun_SEL_AFib;
    }
    
    public boolean isFun_SEL_CBP_algo() {
        return this.Fun_SEL_CBP_algo;
    }
    
    public void setFun_SEL_CBP_algo(final boolean fun_SEL_CBP_algo) {
        this.Fun_SEL_CBP_algo = fun_SEL_CBP_algo;
    }
    
    public boolean isFun_SEL_AMPM() {
        return this.Fun_SEL_AMPM;
    }
    
    public void setFun_SEL_AMPM(final boolean fun_SEL_AMPM) {
        this.Fun_SEL_AMPM = fun_SEL_AMPM;
    }
    
    public boolean isFun_SEL_CBP_CL() {
        return this.Fun_SEL_CBP_CL;
    }
    
    public void setFun_SEL_CBP_CL(final boolean fun_SEL_CBP_CL) {
        this.Fun_SEL_CBP_CL = fun_SEL_CBP_CL;
    }
    
    public boolean isFun_SEL_AutoTest() {
        return this.Fun_SEL_AutoTest;
    }
    
    public void setFun_SEL_AutoTest(final boolean fun_SEL_AutoTest) {
        this.Fun_SEL_AutoTest = fun_SEL_AutoTest;
    }
    
    public boolean isFun_SEL_Bluetooth() {
        return this.Fun_SEL_Bluetooth;
    }
    
    public void setFun_SEL_Bluetooth(final boolean fun_SEL_Bluetooth) {
        this.Fun_SEL_Bluetooth = fun_SEL_Bluetooth;
    }
    
    public boolean isFun_SEL_UnitKpa() {
        return this.Fun_SEL_UnitKpa;
    }
    
    public void setFun_SEL_UnitKpa(final boolean fun_SEL_UnitKpa) {
        this.Fun_SEL_UnitKpa = fun_SEL_UnitKpa;
    }
    
    public boolean isFun_SEL_RS232() {
        return this.Fun_SEL_RS232;
    }
    
    public void setFun_SEL_RS232(final boolean fun_SEL_RS232) {
        this.Fun_SEL_RS232 = fun_SEL_RS232;
    }
    
    @Override
    public String toString() {
        return "FunctionSettingValues{  Fun_SEL_AFib=" + this.Fun_SEL_AFib + ", Fun_SEL_CBP_algo=" + this.Fun_SEL_CBP_algo + ", Fun_SEL_AMPM=" + this.Fun_SEL_AMPM + ", Fun_SEL_CBP_CL=" + this.Fun_SEL_CBP_CL + ", Fun_SEL_AutoTest=" + this.Fun_SEL_AutoTest + ", Fun_SEL_Bluetooth=" + this.Fun_SEL_Bluetooth + ", Fun_SEL_UnitKpa=" + this.Fun_SEL_UnitKpa + ", Fun_SEL_RS232=" + this.Fun_SEL_RS232 + "}";
    }
}
