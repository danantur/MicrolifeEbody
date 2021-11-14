// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import com.microlife_sdk.model.abcdef.e;

public class CBPdataAndCalCBP
{
    public static final String endchar = "15FE0A0D";
    public static final String ZERODATA = "FFFFFFFF";
    public Dformat dformat;
    public List<Integer> CBPdata;
    public List<Integer> CalCBP;
    
    public CBPdataAndCalCBP() {
        this.CBPdata = new ArrayList<Integer>();
        this.CalCBP = new ArrayList<Integer>();
    }
    
    public Dformat getDformat() {
        return this.dformat;
    }
    
    public void setDformat(final Dformat dformat) {
        this.dformat = dformat;
    }
    
    public void importHexString(String a, final int n) {
        final e e = new e(a);
        final e e2 = e;
        this.dformat = Dformat.NoCBPRaw;
        switch (e.c(2)) {
            case 3: {
                this.dformat = Dformat.FullCBPRaw;
                break;
            }
            case 1:
            case 2: {
                this.dformat = Dformat.LowCBPRaw;
                break;
            }
            case 0: {
                this.dformat = Dformat.NoCBPRaw;
                break;
            }
        }
        Boolean b = true;
        while (b) {
            final String a2;
            if ((a2 = e2.a(8)).equals("15FE0A0D")) {
                b = false;
            }
            else {
                if (a2.equals("FFFFFFFF")) {
                    continue;
                }
                final e e3 = new e(a2);
                this.CBPdata.add(e3.c(4));
                this.CBPdata.add(e3.c(4));
            }
        }
        while (e2.a() > 8) {
            if (!(a = e2.a(8)).equals("FFFFFFFF")) {
                final e e4 = new e(a);
                this.CalCBP.add(e4.c(4));
                this.CalCBP.add(e4.c(4));
            }
        }
    }
    
    @Override
    public String toString() {
        return "CRecord{  Dformat = " + this.dformat + ", CBPdata = " + Arrays.toString(this.CBPdata.toArray()) + ", CalCBP = " + Arrays.toString(this.CalCBP.toArray()) + "}";
    }
    
    public enum Dformat
    {
        NoCBPRaw, 
        LowCBPRaw, 
        FullCBPRaw;
    }
}
