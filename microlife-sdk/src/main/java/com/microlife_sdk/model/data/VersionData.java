// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

public class VersionData
{
    public int year;
    public int month;
    public int day;
    public int maxUser;
    public int maxMemory;
    public boolean optionIHB;
    public boolean optionAfib;
    public boolean OPtionMAM;
    public boolean optionAmbientT;
    public boolean optionTubeless;
    public boolean optionDeviceID;
    public double deviceBatteryVoltage;
    public String FWName;
    public boolean optionPAD;
    public boolean optionCBP;
    public boolean optionDiagnosticModeAFib;
    public boolean openNoUsualModeAFib;
    public boolean openNocturnalMode;
    public boolean openBPtype;
    public int protocolVersion;
    public int currentMode;
    
    public void importHexString(final String s) {
    }
    
    public void setYear(final int year) {
        this.year = year;
    }
    
    public int getYear() {
        return this.year;
    }
    
    public void setMonth(final int month) {
        this.month = month;
    }
    
    public int getMonth() {
        return this.month;
    }
    
    public void setDay(final int day) {
        this.day = day;
    }
    
    public int getDay() {
        return this.day;
    }
    
    public void setMaxUser(final int maxUser) {
        this.maxUser = maxUser;
    }
    
    public int getMaxUser() {
        return this.maxUser;
    }
    
    public void setMaxMemory(final int maxMemory) {
        this.maxMemory = maxMemory;
    }
    
    public int getMaxMemory() {
        return this.maxMemory;
    }
    
    public void setOptionIHB(final boolean optionIHB) {
        this.optionIHB = optionIHB;
    }
    
    public boolean isOptionIHB() {
        return this.optionIHB;
    }
    
    public void setOptionAfib(final boolean optionAfib) {
        this.optionAfib = optionAfib;
    }
    
    public boolean isOptionAfib() {
        return this.optionAfib;
    }
    
    public void setOPtionMAM(final boolean oPtionMAM) {
        this.OPtionMAM = oPtionMAM;
    }
    
    public boolean isOPtionMAM() {
        return this.OPtionMAM;
    }
    
    public void setOptionAmbientT(final boolean optionAmbientT) {
        this.optionAmbientT = optionAmbientT;
    }
    
    public boolean isOptionAmbientT() {
        return this.optionAmbientT;
    }
    
    public void setOptionTubeless(final boolean optionTubeless) {
        this.optionTubeless = optionTubeless;
    }
    
    public boolean isOptionTubeless() {
        return this.optionTubeless;
    }
    
    public void setOptionDeviceID(final boolean optionDeviceID) {
        this.optionDeviceID = optionDeviceID;
    }
    
    public boolean isOptionDeviceID() {
        return this.optionDeviceID;
    }
    
    public void setDeviceBatteryVoltage(final double deviceBatteryVoltage) {
        this.deviceBatteryVoltage = deviceBatteryVoltage;
    }
    
    public double getDeviceBatteryVoltage() {
        return this.deviceBatteryVoltage;
    }
    
    public void setFWName(final String fwName) {
        this.FWName = fwName;
    }
    
    public String getFWName() {
        return this.FWName;
    }
    
    @Override
    public String toString() {
        return "VersionData{year=" + this.year + ", month=" + this.month + ", day=" + this.day + ", maxUser=" + this.maxUser + ", maxMemory=" + this.maxMemory + ", optionIHB=" + this.optionIHB + ", optionAfib=" + this.optionAfib + ", OPtionMAM=" + this.OPtionMAM + ", optionAmbientT=" + this.optionAmbientT + ", optionTubeless=" + this.optionTubeless + ", optionDeviceID=" + this.optionDeviceID + ", deviceBatteryVoltage=" + this.deviceBatteryVoltage + ", FWName='" + this.FWName + '\'' + "}";
    }
    
    public int getCurrentMode() {
        return this.currentMode;
    }
    
    public void setCurrentMode(final int currentMode) {
        this.currentMode = currentMode;
    }
    
    public int getProtocolVersion() {
        return this.protocolVersion;
    }
    
    public void setProtocolVersion(final int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
    
    public boolean isOpenBPtype() {
        return this.openBPtype;
    }
    
    public void setOpenBPtype(final boolean openBPtype) {
        this.openBPtype = openBPtype;
    }
    
    public boolean isOpenNocturnalMode() {
        return this.openNocturnalMode;
    }
    
    public void setOpenNocturnalMode(final boolean openNocturnalMode) {
        this.openNocturnalMode = openNocturnalMode;
    }
    
    public boolean isOpenNoUsualModeAFib() {
        return this.openNoUsualModeAFib;
    }
    
    public void setOpenNoUsualModeAFib(final boolean openNoUsualModeAFib) {
        this.openNoUsualModeAFib = openNoUsualModeAFib;
    }
    
    public boolean isOptionDiagnosticModeAFib() {
        return this.optionDiagnosticModeAFib;
    }
    
    public void setOptionDiagnosticModeAFib(final boolean optionDiagnosticModeAFib) {
        this.optionDiagnosticModeAFib = optionDiagnosticModeAFib;
    }
    
    public boolean isOptionCBP() {
        return this.optionCBP;
    }
    
    public void setOptionCBP(final boolean optionCBP) {
        this.optionCBP = optionCBP;
    }
    
    public boolean isOptionPAD() {
        return this.optionPAD;
    }
    
    public void setOptionPAD(final boolean optionPAD) {
        this.optionPAD = optionPAD;
    }
}
