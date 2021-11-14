// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

import java.util.Arrays;

public class OscillationData
{
    public int[] pressureData;
    public int[] amplitudeData;
    
    public void setPressureData(final int[] pressureData) {
        this.pressureData = pressureData;
    }
    
    public int[] getPressureData() {
        return this.pressureData;
    }
    
    public void setAmplitudeData(final int[] amplitudeData) {
        this.amplitudeData = amplitudeData;
    }
    
    public int[] getAmplitudeData() {
        return this.amplitudeData;
    }
    
    @Override
    public String toString() {
        return "OscillationData{pressureData=" + Arrays.toString(this.pressureData) + ", amplitudeData=" + Arrays.toString(this.amplitudeData) + "}";
    }
}
