// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class DiagnosticDRecord
{
    public static final String ZERO_CURRENT_DATA = "00000000000000";
    public static final int CURRENT_DATA_LENGTH = 14;
    public List DayData;
    public int SYSavgAll;
    public int DIAavgAll;
    public int PULavgAll;
    public int SYSavgM;
    public int DIAavgM;
    public int PULavgM;
    public int SYSavgE;
    public int DIAavgE;
    public int PULavgE;
    public int DiagMemoryEndIndex01b;
    public int DiagMemoryEndIndex02b;
    public int DiagMemorySet01b;
    public int DiagMemorySet02b;
    public int DiagDAYCount;
    public int DiagTimes;
    public int DiagOver;
    
    public DiagnosticDRecord() {
        this.DayData = new ArrayList();
    }
    
    public List getDayData() {
        return this.DayData;
    }
    
    public void setDayData(final List dayData) {
        this.DayData = dayData;
    }
    
    public int getDIAavgAll() {
        return this.DIAavgAll;
    }
    
    public int getDIAavgM() {
        return this.DIAavgM;
    }
    
    public int getDIAavgE() {
        return this.DIAavgE;
    }
    
    public int getDiagMemoryEndIndex01b() {
        return this.DiagMemoryEndIndex01b;
    }
    
    public int getPULavgAll() {
        return this.PULavgAll;
    }
    
    public int getDiagMemoryEndIndex02b() {
        return this.DiagMemoryEndIndex02b;
    }
    
    public int getPULavgM() {
        return this.PULavgM;
    }
    
    public int getPULavgE() {
        return this.PULavgE;
    }
    
    public int getDiagMemorySet01b() {
        return this.DiagMemorySet01b;
    }
    
    public int getSYSavgAll() {
        return this.SYSavgAll;
    }
    
    public int getSYSavgE() {
        return this.SYSavgE;
    }
    
    public int getDiagDAYCount() {
        return this.DiagDAYCount;
    }
    
    public int getDiagMemorySet02b() {
        return this.DiagMemorySet02b;
    }
    
    public int getSYSavgM() {
        return this.SYSavgM;
    }
    
    public int getDiagTimes() {
        return this.DiagTimes;
    }
    
    public void setDIAavgAll(final int diAavgAll) {
        this.DIAavgAll = diAavgAll;
    }
    
    public void setDIAavgE(final int diAavgE) {
        this.DIAavgE = diAavgE;
    }
    
    public void setDIAavgM(final int diAavgM) {
        this.DIAavgM = diAavgM;
    }
    
    public void setDiagDAYCount(final int diagDAYCount) {
        this.DiagDAYCount = diagDAYCount;
    }
    
    public void setDiagMemoryEndIndex01b(final int diagMemoryEndIndex01b) {
        this.DiagMemoryEndIndex01b = diagMemoryEndIndex01b;
    }
    
    public void setPULavgAll(final int puLavgAll) {
        this.PULavgAll = puLavgAll;
    }
    
    public int getDiagOver() {
        return this.DiagOver;
    }
    
    public void setDiagMemoryEndIndex02b(final int diagMemoryEndIndex02b) {
        this.DiagMemoryEndIndex02b = diagMemoryEndIndex02b;
    }
    
    public void setDiagMemorySet01b(final int diagMemorySet01b) {
        this.DiagMemorySet01b = diagMemorySet01b;
    }
    
    public void setDiagMemorySet02b(final int diagMemorySet02b) {
        this.DiagMemorySet02b = diagMemorySet02b;
    }
    
    public void setDiagTimes(final int diagTimes) {
        this.DiagTimes = diagTimes;
    }
    
    public void setDiagOver(final int diagOver) {
        this.DiagOver = diagOver;
    }
    
    public void setPULavgE(final int puLavgE) {
        this.PULavgE = puLavgE;
    }
    
    public void setPULavgM(final int puLavgM) {
        this.PULavgM = puLavgM;
    }
    
    public void setSYSavgAll(final int sySavgAll) {
        this.SYSavgAll = sySavgAll;
    }
    
    public void setSYSavgE(final int sySavgE) {
        this.SYSavgE = sySavgE;
    }
    
    public void setSYSavgM(final int sySavgM) {
        this.SYSavgM = sySavgM;
    }
    
    @Override
    public String toString() {
        return "DRecord{  DayData=" + Arrays.deepToString(this.DayData.toArray()) + ", SYSavgAll=" + this.SYSavgAll + ", DIAavgAll=" + this.DIAavgAll + ", PULavgAll=" + this.PULavgAll + ", SYSavgM=" + this.SYSavgM + ", DIAavgM=" + this.DIAavgM + ", PULavgM=" + this.PULavgM + ", SYSavgE=" + this.SYSavgE + ", DIAavgE=" + this.DIAavgE + ", PULavgE=" + this.PULavgE + ", DiagMemoryEndIndex01b=" + this.DiagMemoryEndIndex01b + ", DiagMemoryEndIndex02b=" + this.DiagMemoryEndIndex02b + ", DiagMemorySet01b=" + this.DiagMemorySet01b + ", DiagMemorySet02b=" + this.DiagMemorySet02b + ", DiagDAYCount=" + this.DiagDAYCount + ", DiagTimes=" + this.DiagTimes + ", DiagOver=" + this.DiagOver + "}";
    }
}
