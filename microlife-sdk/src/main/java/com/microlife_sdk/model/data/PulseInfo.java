// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

import java.util.Arrays;

public class PulseInfo
{
    public boolean indexAll;
    public boolean indexPAD;
    public boolean indexAfib;
    public boolean indexIHB;
    public int referOfPulseRate;
    public int pulseRecordCount;
    public int[] headerArr;
    public PulseRecord[] pulseRecordArr;
    
    public PulseInfo() {
        this.headerArr = new int[] { 128, 4, 2, 1 };
        this.pulseRecordArr = new PulseRecord[4];
    }
    
    public void setIndexAll(final boolean indexAll) {
        this.indexAll = indexAll;
    }
    
    public boolean isIndexAll() {
        return this.indexAll;
    }
    
    public void setIndexPAD(final boolean indexPAD) {
        this.indexPAD = indexPAD;
    }
    
    public boolean isIndexPAD() {
        return this.indexPAD;
    }
    
    public void setIndexAfib(final boolean indexAfib) {
        this.indexAfib = indexAfib;
    }
    
    public boolean isIndexAfib() {
        return this.indexAfib;
    }
    
    public void setIndexIHB(final boolean indexIHB) {
        this.indexIHB = indexIHB;
    }
    
    public boolean isIndexIHB() {
        return this.indexIHB;
    }
    
    public void setReferOfPulseRate(final int referOfPulseRate) {
        this.referOfPulseRate = referOfPulseRate;
    }
    
    public int getReferOfPulseRate() {
        return this.referOfPulseRate;
    }
    
    public void setPulseRecordCount(final int pulseRecordCount) {
        this.pulseRecordCount = pulseRecordCount;
    }
    
    public int getPulseRecordCount() {
        return this.pulseRecordCount;
    }
    
    public void addPulseRecordByHeader(final PulseRecord pulseRecord) {
        this.pulseRecordArr[Arrays.asList(new int[][] { this.headerArr }).indexOf(pulseRecord.getHeader())] = pulseRecord;
    }
    
    @Override
    public String toString() {
        return "PulseInfo{indexAll=" + this.indexAll + ", indexPAD=" + this.indexPAD + ", indexAfib=" + this.indexAfib + ", indexIHB=" + this.indexIHB + ", referOfPulseRate=" + this.referOfPulseRate + ", pulseRecordCount=" + this.pulseRecordCount + ", headerArr=" + Arrays.toString(this.headerArr) + ", pulseRecordArr=" + Arrays.toString(this.pulseRecordArr) + "}";
    }
    
    public static class PulseRecord
    {
        public int header;
        public int[] rawDataOfPulseRate;
        
        public void setHeader(final int header) {
            this.header = header;
        }
        
        public int getHeader() {
            return this.header;
        }
        
        public void setRawDataOfPulseRate(final int[] rawDataOfPulseRate) {
            this.rawDataOfPulseRate = rawDataOfPulseRate;
        }
        
        public int[] getRawDataOfPulseRate() {
            return this.rawDataOfPulseRate;
        }
    }
}
