// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

import com.microlife_sdk.model.abcdef.*;

public class SettingValues
{
    public int ABPMStart;
    public int ABPMEnd;
    public int ABPMInt_first;
    public int ABPMInt_second;
    public int HI_infPressure;
    public boolean CBP_zone2_meas_off;
    public boolean CBP_zone1_meas_off;
    public boolean SW_SEL_silent;
    public boolean SW_checkhide;
    public int CBPInt_first;
    public int CBPInt_second;
    public int AUS_HI_infPressure;
    public boolean SW_AUTO_hide;
    public boolean SW_AUS_Hide;
    public boolean SW_AVG_no_include_first;
    public boolean SW_CBP;
    public boolean SW_AFib;
    public boolean SW_AMPM;
    public boolean SW_Kpa;
    public int RestTime;
    public int IntervalTime;
    public int AutoMeasureNumber;
    public int origin;
    
    private void importHexStringFormWBO(final String s) {
        final e e = new e(s);
        final e e4;
        final e e3;
        final e e2 = e3 = (e4 = e);
        new e(s);
        this.AUS_HI_infPressure = e3.c(2) * 256 + e2.c(2);
        e4.a(4);
        this.HI_infPressure = e4.c(2) * 256 + e2.c(2);
        final int c;
        final boolean sw_Kpa = ((c = e.c(2)) & 0x80) >> 7 == 1;
        final int n = c;
        this.SW_Kpa = sw_Kpa;
        final boolean sw_AMPM = (n & 0x40) >> 6 == 1;
        final int n2 = c;
        this.SW_AMPM = sw_AMPM;
        final boolean sw_AFib = (n2 & 0x20) >> 5 == 1;
        final int n3 = c;
        this.SW_AFib = sw_AFib;
        final boolean sw_CBP = (n3 & 0x10) >> 4 == 1;
        final int n4 = c;
        this.SW_CBP = sw_CBP;
        final boolean sw_AVG_no_include_first = (n4 & 0x8) >> 3 == 1;
        final int n5 = c;
        this.SW_AVG_no_include_first = sw_AVG_no_include_first;
        final boolean sw_AUS_Hide = (n5 & 0x4) >> 2 == 1;
        final int n6 = c;
        this.SW_AUS_Hide = sw_AUS_Hide;
        final boolean sw_SEL_silent = (n6 & 0x2) >> 1 == 1;
        final int n7 = c;
        this.SW_SEL_silent = sw_SEL_silent;
        final boolean sw_AUTO_hide = (n7 & 0x1) == 0x1;
        final e e5 = e2;
        final e e6 = e2;
        final e e7 = e2;
        this.SW_AUTO_hide = sw_AUTO_hide;
        e7.a(4);
        this.RestTime = e7.c(2) * 256 + e2.c(2);
        this.IntervalTime = e6.c(2) * 256 + e2.c(2);
        this.AutoMeasureNumber = e5.c(2);
    }
    
    private void importHexStringFormWBO3(final String s) {
        final e e = new e(s);
        final e e7;
        final e e6;
        final e e5;
        final e e4;
        final e e3;
        final e e2 = e3 = (e4 = (e5 = (e6 = (e7 = e))));
        new e(s);
        this.ABPMStart = e3.c(2);
        this.ABPMEnd = e4.c(2);
        this.ABPMInt_first = e5.c(2);
        this.ABPMInt_second = e6.c(2);
        this.HI_infPressure = e7.c(4);
        final int c;
        final boolean cbp_zone2_meas_off = ((c = e.c(2)) & 0x8) >> 3 == 1;
        final int n = c;
        this.CBP_zone2_meas_off = cbp_zone2_meas_off;
        final boolean cbp_zone1_meas_off = (n & 0x4) >> 2 == 1;
        final int n2 = c;
        this.CBP_zone1_meas_off = cbp_zone1_meas_off;
        final boolean sw_SEL_silent = (n2 & 0x2) >> 1 == 1;
        final int n3 = c;
        this.SW_SEL_silent = sw_SEL_silent;
        final boolean sw_checkhide = (n3 & 0x1) == 0x1;
        final e e8 = e2;
        final e e9 = e2;
        this.SW_checkhide = sw_checkhide;
        this.CBPInt_first = e9.c(2);
        this.CBPInt_second = e8.c(2);
    }
    
    public void importHexString(final String s, final int origin) {
        this.origin = origin;
        if (origin != 65) {
            if (origin == 66) {
                this.importHexStringFormWBO(s);
            }
        }
        else {
            this.importHexStringFormWBO3(s);
        }
    }
    
    public boolean checkSettingValues() {
        return (this.AutoMeasureNumber >= 3 || !this.SW_AVG_no_include_first) && (this.AutoMeasureNumber != 1 || !this.SW_AFib) && (!this.SW_CBP || this.AutoMeasureNumber * this.IntervalTime <= 1800);
    }
    
    public int getABPMStart() {
        return this.ABPMStart;
    }
    
    public void setABPMStart(final int abpmStart) {
        this.ABPMStart = abpmStart;
    }
    
    public int getABPMEnd() {
        return this.ABPMEnd;
    }
    
    public void setABPMEnd(final int abpmEnd) {
        this.ABPMEnd = abpmEnd;
    }
    
    public int getABPMInt_first() {
        return this.ABPMInt_first;
    }
    
    public void setABPMInt_first(final int abpmInt_first) {
        this.ABPMInt_first = abpmInt_first;
    }
    
    public int getABPMInt_second() {
        return this.ABPMInt_second;
    }
    
    public void setABPMInt_second(final int abpmInt_second) {
        this.ABPMInt_second = abpmInt_second;
    }
    
    public int getHI_infPressure() {
        return this.HI_infPressure;
    }
    
    public void setHI_infPressure(final int hi_infPressure) {
        this.HI_infPressure = hi_infPressure;
    }
    
    public int getCBPInt_first() {
        return this.CBPInt_first;
    }
    
    public void setCBPInt_first(final int cbpInt_first) {
        this.CBPInt_first = cbpInt_first;
    }
    
    public int getCBPInt_second() {
        return this.CBPInt_second;
    }
    
    public void setCBPInt_second(final int cbpInt_second) {
        this.CBPInt_second = cbpInt_second;
    }
    
    public boolean isCBP_zone2_meas_off() {
        return this.CBP_zone2_meas_off;
    }
    
    public void setCBP_zone2_meas_off(final boolean cbp_zone2_meas_off) {
        this.CBP_zone2_meas_off = cbp_zone2_meas_off;
    }
    
    public boolean isCBP_zone1_meas_off() {
        return this.CBP_zone1_meas_off;
    }
    
    public void setCBP_zone1_meas_off(final boolean cbp_zone1_meas_off) {
        this.CBP_zone1_meas_off = cbp_zone1_meas_off;
    }
    
    public boolean isSW_SEL_silent() {
        return this.SW_SEL_silent;
    }
    
    public void setSW_SEL_silent(final boolean sw_SEL_silent) {
        this.SW_SEL_silent = sw_SEL_silent;
    }
    
    public boolean isSW_checkhide() {
        return this.SW_checkhide;
    }
    
    public void setSW_checkhide(final boolean sw_checkhide) {
        this.SW_checkhide = sw_checkhide;
    }
    
    @Override
    public String toString() {
        String s = "";
        final int origin;
        if ((origin = this.origin) != 65) {
            if (origin == 66) {
                s = "SettingValues{AUS_HI_infPressure=" + this.AUS_HI_infPressure + ", HI_infPressure=" + this.HI_infPressure + ", SW_AUTO_hide=" + this.SW_AUTO_hide + ", SW_SEL_silent=" + this.SW_SEL_silent + ", SW_AUS_Hide=" + this.SW_AUS_Hide + ", SW_AVG_no_include_first=" + this.SW_AVG_no_include_first + ", SW_CBP=" + this.SW_CBP + ", SW_AFib=" + this.SW_AFib + ", SW_AMPM=" + this.SW_AMPM + ", SW_Kpa=" + this.SW_Kpa + ", RestTime=" + this.RestTime + ", IntervalTime=" + this.IntervalTime + ", AutoMeasureNumber=" + this.AutoMeasureNumber + "}";
            }
        }
        else {
            s = "SettingValues{ABPMStart=" + this.ABPMStart + ", ABPMEnd=" + this.ABPMEnd + ", ABPMInt_first=" + this.ABPMInt_first + ", ABPMInt_second=" + this.ABPMInt_second + ", HI_infPressure=" + this.HI_infPressure + ", CBP_zone2_meas_off=" + this.CBP_zone2_meas_off + ", CBP_zone1_meas_off=" + this.CBP_zone1_meas_off + ", SW_SEL_silent=" + this.SW_SEL_silent + ", SW_checkhide=" + this.SW_checkhide + ", CBPInt_first=" + this.CBPInt_first + ", CBPInt_second=" + this.CBPInt_second + "}";
        }
        return s;
    }
    
    public int getAUS_HI_infPressure() {
        return this.AUS_HI_infPressure;
    }
    
    public void setAUS_HI_infPressure(final int aus_HI_infPressure) {
        this.AUS_HI_infPressure = aus_HI_infPressure;
    }
    
    public boolean isSW_AUTO_hide() {
        return this.SW_AUTO_hide;
    }
    
    public void setSW_AUTO_hide(final boolean sw_AUTO_hide) {
        this.SW_AUTO_hide = sw_AUTO_hide;
    }
    
    public boolean isSW_AUS_Hide() {
        return this.SW_AUS_Hide;
    }
    
    public void setSW_AUS_Hide(final boolean sw_AUS_Hide) {
        this.SW_AUS_Hide = sw_AUS_Hide;
    }
    
    public boolean isSW_AVG_no_include_first() {
        return this.SW_AVG_no_include_first;
    }
    
    public void setSW_AVG_no_include_first(final boolean sw_AVG_no_include_first) {
        this.SW_AVG_no_include_first = sw_AVG_no_include_first;
    }
    
    public boolean isSW_CBP() {
        return this.SW_CBP;
    }
    
    public void setSW_CBP(final boolean sw_CBP) {
        this.SW_CBP = sw_CBP;
    }
    
    public boolean isSW_AFib() {
        return this.SW_AFib;
    }
    
    public void setSW_AFib(final boolean sw_AFib) {
        this.SW_AFib = sw_AFib;
    }
    
    public boolean isSW_AMPM() {
        return this.SW_AMPM;
    }
    
    public void setSW_AMPM(final boolean sw_AMPM) {
        this.SW_AMPM = sw_AMPM;
    }
    
    public boolean isSW_Kpa() {
        return this.SW_Kpa;
    }
    
    public void setSW_Kpa(final boolean sw_Kpa) {
        this.SW_Kpa = sw_Kpa;
    }
    
    public int getRestTime() {
        return this.RestTime;
    }
    
    public void setRestTime(final int restTime) {
        this.RestTime = restTime;
    }
    
    public int getIntervalTime() {
        return this.IntervalTime;
    }
    
    public void setIntervalTime(final int intervalTime) {
        this.IntervalTime = intervalTime;
    }
    
    public int getAutoMeasureNumber() {
        return this.AutoMeasureNumber;
    }
    
    public void setAutoMeasureNumber(final int autoMeasureNumber) {
        this.AutoMeasureNumber = autoMeasureNumber;
    }
}
