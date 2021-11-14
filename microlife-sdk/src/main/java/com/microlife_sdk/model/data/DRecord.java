// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class DRecord
{
    public static final String ZERO_CURRENT_DATA = "00000000000000";
    public static final int CURRENT_DATA_LENGTH = 14;
    public static final String ZERO_CURRENT_DAT_4GA = "00000000000000000000";
    public static final int CURRENT_DATA_LENGTH_4G = 20;
    public static final String ZERO_CURRENT_DAT_WBO = "0000000000000000000000000000";
    public static final int CURRENT_DATA_LENGTH_WBO = 30;
    public int mode;
    public int noOfCurrentMeasurement;
    public int historyMeasuremeNumber;
    public int userNumber;
    public int MAMState;
    public int Average;
    public CurrentAndMData[] currentData;
    public List<CurrentAndMData> MData;
    public boolean measureMode;
    
    public DRecord() {
        this.MData = new ArrayList<CurrentAndMData>();
    }
    
    public void setMode(final int mode) {
        this.mode = mode;
    }
    
    public int getMode() {
        return this.mode;
    }
    
    public void setNoOfCurrentMeasurement(final int noOfCurrentMeasurement) {
        this.noOfCurrentMeasurement = noOfCurrentMeasurement;
    }
    
    public int getNoOfCurrentMeasurement() {
        return this.noOfCurrentMeasurement;
    }
    
    public void setHistoryMeasuremeNumber(final int historyMeasuremeNumber) {
        this.historyMeasuremeNumber = historyMeasuremeNumber;
    }
    
    public int getHistoryMeasuremeNumber() {
        return this.historyMeasuremeNumber;
    }
    
    public void setUserNumber(final int userNumber) {
        this.userNumber = userNumber;
    }
    
    public int getUserNumber() {
        return this.userNumber;
    }
    
    public void setMAMState(final int mamState) {
        this.MAMState = mamState;
    }
    
    public int getMAMState() {
        return this.MAMState;
    }
    
    public void setAverage(final int average) {
        this.Average = average;
    }
    
    public int getAverage() {
        return this.Average;
    }
    
    public void setCurrentData(final CurrentAndMData[] currentData) {
        this.currentData = currentData;
    }
    
    public CurrentAndMData[] getCurrentData() {
        return this.currentData;
    }
    
    public void setMData(final List<CurrentAndMData> mData) {
        this.MData = mData;
    }
    
    public List<CurrentAndMData> getMData() {
        return this.MData;
    }
    
    public void setMeasureMode(final boolean measureMode) {
        this.measureMode = measureMode;
    }
    
    public boolean isMeasureMode() {
        return this.measureMode;
    }
    
    @Override
    public String toString() {
        return "DRecord{mode=" + this.mode + ", noOfCurrentMeasurement=" + this.noOfCurrentMeasurement + ", historyMeasuremeNumber=" + this.historyMeasuremeNumber + ", userNumber=" + this.userNumber + ", MAMState=" + this.MAMState + ", currentData=" + Arrays.toString(this.currentData) + ", MData=" + Arrays.toString(this.MData.toArray()) + ", measureMode=" + this.measureMode + "}";
    }
}
