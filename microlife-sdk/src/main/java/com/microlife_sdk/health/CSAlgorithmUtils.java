// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.health;

public class CSAlgorithmUtils
{
    public native float getEXF(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getInF(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getTF(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getTFR(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getLBM(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getSLM(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getPM(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getFM(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getBFR(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getEE(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getOD(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getMC(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getWC(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getBMR(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getMSW(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native float getVFR(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native int getBodyAge(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    public native int getScore(final float p0, final byte p1, final float p2, final int p3, final int p4);
    
    static {
        System.loadLibrary("csalgorithm");
    }
}
