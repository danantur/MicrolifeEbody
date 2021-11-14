// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.data;

import com.microlife_sdk.model.abcdef.*;

public class CurrentAndMData
{
    public int systole;
    public int dia;
    public int hr;
    public int day;
    public int hour;
    public int month;
    public int minute;
    public int MAM;
    public boolean arr;
    public int year;
    public int cuffokr;
    public boolean IHB;
    public boolean AFIb;
    public int resultCode;
    public boolean usual;
    public boolean diagnostic;
    public boolean AM;
    public boolean PM;
    public int condition;
    public int MAP;
    public int MCBP;
    public int PVR;
    public int CSBP;
    public int CPP;
    public int errorCode;
    public int ABPM;
    public int LB;
    public int AA;
    public int SM;
    public boolean isFor3G;
    public int deviceMode;
    
    private void importHexStringFor3G(final String s) {
        final e e = new e(s);
        this.isFor3G = true;
        final e e2 = new e(s);
        this.systole = e2.c(2);
        final e e3 = new e(s);
        this.dia = e3.c(2);
        final e e4 = new e(s);
        this.hr = e4.c(2);
        final e e5 = new e(s);
        final int c = e5.c(2);
        final int n = (c & 0xC0) >> 6;
        this.day = (c & 0x3F);
        final e e6 = new e(s);
        final int c2;
        final int n2 = ((c2 = e6.c(2)) & 0xC0) >> 6;
        this.hour = (c2 & 0x3F);
        this.month = (n2 << 2 | n);
        final e e7 = new e(s);
        this.minute = e7.c(2);
        final int c3;
        int mam;
        if (((c3 = e.c(2)) & 0x80) >> 7 == 1) {
            mam = 3;
        }
        else {
            mam = 0;
        }
        final int n3 = c3;
        this.MAM = mam;
        final boolean arr = (n3 & 0x40) >> 6 == 1;
        final int n4 = c3;
        this.arr = arr;
        this.year = (n4 & 0x3F) + 2000;
        this.IHB = false;
        this.AFIb = false;
        this.cuffokr = 2;
    }
    
    private void importHexStringFor4G(final String s) {
        final e e = new e(s);
        this.isFor3G = false;
        final e e2 = e;
        this.systole = e2.c(2);
        final e e3 = e;
        this.dia = e3.c(2);
        final e e4 = e;
        this.hr = e4.c(2);
        final e e5 = e;
        this.year = e5.c(2) + 2000;
        final e e6 = e;
        this.month = e6.c(2);
        final e e7 = e;
        this.day = e7.c(2);
        final e e8 = e;
        this.hour = e8.c(2);
        final e e9 = e;
        this.minute = e9.c(2);
        final int c;
        final int n = c = e.c(2);
        this.cuffokr = (c & 0x80) >> 7;
        final boolean ihb = (n & 0x40) >> 6 == 1;
        final int n2 = c;
        this.IHB = ihb;
        this.AFIb = ((n2 & 0x20) >> 5 == 1);
        final boolean arr = this.IHB || this.AFIb;
        final int n3 = c;
        this.arr = arr;
        this.MAM = (n3 & 0x18) >> 3;
    }
    
    private void importHexStringForWBP(final String s) {
        final e e = new e(s);
        final e e4;
        final e e3;
        final e e2 = e3 = (e4 = e);
        this.isFor3G = false;
        this.systole = e3.c(2);
        this.dia = e4.c(2);
        this.hr = e.c(2);
        if (this.systole == 69 && this.dia == 114) {
            this.resultCode = this.hr;
        }
        final e e5 = e2;
        final e e6 = e2;
        final int c = e2.c(2);
        final int n = (c & 0xF0) >> 4;
        this.month = (c & 0xF);
        final int c2 = e6.c(2);
        this.day = (c2 & 0xF8) >> 3;
        final int n2 = c2 & 0x7;
        final int c3 = e6.c(2);
        this.hour = (n2 << 2 | (c3 & 0xC0) >> 6);
        this.minute = (c3 & 0x3F);
        final int c4;
        final int n3 = c4 = e5.c(2);
        this.year = ((c4 & 0x80) >> 7) * 16 + 2000 + n;
        final boolean arr = (n3 & 0x40) >> 6 == 1;
        final int n4 = c4;
        this.arr = arr;
        final boolean usual = (n4 & 0x20) >> 5 == 1;
        final int n5 = c4;
        this.usual = usual;
        final boolean diagnostic = (n5 & 0x10) >> 4 == 1;
        final int n6 = c4;
        this.diagnostic = diagnostic;
        final boolean am = (n6 & 0x4) >> 2 == 1;
        final int n7 = c4;
        this.AM = am;
        final boolean pm = (n7 & 0x2) >> 1 == 1;
        final int n8 = c4;
        this.PM = pm;
        this.AFIb = ((n8 & 0x1) == 0x1);
    }
    
    private void importHexStringForWBO3(final String s) {
        final e e = new e(s);
        this.isFor3G = false;
        final e e2 = new e(s);
        this.condition = e2.c(2);
        final int c = e.c(2);
        final int c2 = e.c(2);
        final int c3 = e.c(2);
        final int c4 = e.c(2);
        final int n = (c4 & 0xF0) >> 4;
        final int n2 = c4 & 0xF;
        final int n3 = (e.c(2) & 0xF8) >> 3;
        final int n4 = e.c(2) & 0x3F;
        final int c5;
        final int n5 = ((c5 = e.c(2)) & 0xC0) >> 6;
        final int n6 = c5;
        final int n7 = (n6 & 0x30) >> 4;
        final int n8 = (n6 & 0x8) >> 3;
        final int n9 = (n6 & 0x4) >> 2;
        final int n10 = (n6 & 0x2) >> 1;
        final int n11 = n6 & 0x1;
        final int n12 = n5 << 4 | n;
        final int c6 = e.c(2);
        final int c7 = e.c(4);
        e.c(4);
        e.c(2);
        e.c(2);
        switch (this.condition) {
            case 3: {
                final int n13 = n11;
                final int sm = n10;
                final int aa = n9;
                final int lb = n8;
                final int abpm = n7;
                final int minute = n4;
                final int day = n3;
                final int month = n2;
                this.year = n12 + 2000;
                this.month = month;
                this.day = day;
                this.minute = minute;
                this.ABPM = abpm;
                this.LB = lb;
                this.AA = aa;
                this.SM = sm;
                final boolean afIb = n13 == 1;
                final int errorCode = c;
                this.AFIb = afIb;
                this.errorCode = errorCode;
                break;
            }
            case 2:
            case 4:
            case 5:
            case 6:
            case 7: {
                final int n14 = n11;
                final int sm2 = n10;
                final int aa2 = n9;
                final int lb2 = n8;
                final int abpm2 = n7;
                final int minute2 = n4;
                final int day2 = n3;
                final int month2 = n2;
                this.year = n12 + 2000;
                this.month = month2;
                this.day = day2;
                this.minute = minute2;
                this.ABPM = abpm2;
                this.LB = lb2;
                this.AA = aa2;
                this.SM = sm2;
                this.AFIb = (n14 == 1);
                break;
            }
            case 1: {
                final int n15 = n11;
                final int sm3 = n10;
                final int aa3 = n9;
                final int lb3 = n8;
                final int abpm3 = n7;
                final int minute3 = n4;
                final int day3 = n3;
                final int month3 = n2;
                final int n16 = n12;
                final int hr = c3;
                final int dia = c2;
                this.systole = c;
                this.dia = dia;
                this.hr = hr;
                this.year = n16 + 2000;
                this.month = month3;
                this.day = day3;
                this.minute = minute3;
                this.ABPM = abpm3;
                this.LB = lb3;
                this.AA = aa3;
                this.SM = sm3;
                final boolean afIb2 = n15 == 1;
                final int mcbp = c7;
                final int map = c6;
                this.AFIb = afIb2;
                this.MAP = map;
                this.MCBP = mcbp;
                break;
            }
            case 0: {
                final int n17 = n11;
                final int sm4 = n10;
                final int aa4 = n9;
                final int lb4 = n8;
                final int abpm4 = n7;
                final int minute4 = n4;
                final int day4 = n3;
                final int month4 = n2;
                final int n18 = n12;
                final int hr2 = c3;
                final int dia2 = c2;
                this.systole = c;
                this.dia = dia2;
                this.hr = hr2;
                this.year = n18 + 2000;
                this.month = month4;
                this.day = day4;
                this.minute = minute4;
                this.ABPM = abpm4;
                this.LB = lb4;
                this.AA = aa4;
                this.SM = sm4;
                final boolean afIb3 = n17 == 1;
                final int map2 = c6;
                this.AFIb = afIb3;
                this.MAP = map2;
                break;
            }
        }
    }
    
    private void importHexStringForWBO(final String s) {
        final e e = new e(s);
        this.isFor3G = false;
        final e e2 = new e(s);
        this.condition = e2.c(2);
        final int c = e.c(2);
        final int c2 = e.c(2);
        final int c3 = e.c(2);
        final int c4 = e.c(2);
        final int n = (c4 & 0xF0) >> 4;
        final int n2 = c4 & 0xF;
        final int n3 = (e.c(2) & 0xF8) >> 3;
        final int n4 = e.c(2) & 0x3F;
        final int c5;
        final int n5 = ((c5 = e.c(2)) & 0xC0) >> 6;
        final int n6 = c5;
        final int n7 = (n6 & 0x30) >> 4;
        final int n8 = (n6 & 0x8) >> 3;
        final int n9 = (n6 & 0x4) >> 2;
        final int n10 = (n6 & 0x2) >> 1;
        final int n11 = n6 & 0x1;
        final int n12 = n5 << 4 | n;
        final int c6 = e.c(2);
        final int c7 = e.c(4);
        final int c8 = e.c(4);
        final int c9 = e.c(2);
        final int c10 = e.c(2);
        switch (this.condition) {
            case 3: {
                final int n13 = n11;
                final int sm = n10;
                final int aa = n9;
                final int lb = n8;
                final int abpm = n7;
                final int minute = n4;
                final int day = n3;
                final int month = n2;
                final int n14 = n12;
                this.errorCode = c;
                this.year = n14 + 2000;
                this.month = month;
                this.day = day;
                this.minute = minute;
                this.ABPM = abpm;
                this.LB = lb;
                this.AA = aa;
                this.SM = sm;
                this.AFIb = (n13 == 1);
                break;
            }
            case 2: {
                final int n15 = n11;
                final int sm2 = n10;
                final int aa2 = n9;
                final int lb2 = n8;
                final int abpm2 = n7;
                final int minute2 = n4;
                final int day2 = n3;
                final int month2 = n2;
                this.year = n12 + 2000;
                this.month = month2;
                this.day = day2;
                this.minute = minute2;
                this.ABPM = abpm2;
                this.LB = lb2;
                this.AA = aa2;
                this.SM = sm2;
                this.AFIb = (n15 == 1);
                break;
            }
            case 1: {
                final int n16 = n11;
                final int sm3 = n10;
                final int aa3 = n9;
                final int lb3 = n8;
                final int abpm3 = n7;
                final int minute3 = n4;
                final int day3 = n3;
                final int month3 = n2;
                final int n17 = n12;
                final int hr = c3;
                final int dia = c2;
                this.systole = c;
                this.dia = dia;
                this.hr = hr;
                this.year = n17 + 2000;
                this.month = month3;
                this.day = day3;
                this.minute = minute3;
                this.ABPM = abpm3;
                this.LB = lb3;
                this.AA = aa3;
                this.SM = sm3;
                final boolean afIb = n16 == 1;
                final int cpp = c10;
                final int csbp = c9;
                final int pvr = c8;
                final int mcbp = c7;
                final int map = c6;
                this.AFIb = afIb;
                this.MAP = map;
                this.MCBP = mcbp;
                this.PVR = pvr;
                this.CSBP = csbp;
                this.CPP = cpp;
                break;
            }
            case 0: {
                final int n18 = n11;
                final int sm4 = n10;
                final int aa4 = n9;
                final int lb4 = n8;
                final int abpm4 = n7;
                final int minute4 = n4;
                final int day4 = n3;
                final int month4 = n2;
                final int n19 = n12;
                final int hr2 = c3;
                final int dia2 = c2;
                this.systole = c;
                this.dia = dia2;
                this.hr = hr2;
                this.year = n19 + 2000;
                this.month = month4;
                this.day = day4;
                this.minute = minute4;
                this.ABPM = abpm4;
                this.LB = lb4;
                this.AA = aa4;
                this.SM = sm4;
                final boolean afIb2 = n18 == 1;
                final int map2 = c6;
                this.AFIb = afIb2;
                this.MAP = map2;
                break;
            }
        }
    }
    
    public void importHexString(final String s, final int deviceMode) {
        this.deviceMode = deviceMode;
        if (deviceMode != 49) {
            if (deviceMode != 58) {
                if (deviceMode != 81) {
                    if (deviceMode != 65) {
                        if (deviceMode == 66) {
                            this.importHexStringForWBO(s);
                        }
                    }
                    else {
                        this.importHexStringForWBO3(s);
                    }
                }
                else {
                    this.importHexStringForWBP(s);
                }
            }
            else {
                this.importHexStringFor4G(s);
            }
        }
        else {
            this.importHexStringFor3G(s);
        }
    }
    
    public int getSystole() {
        return this.systole;
    }
    
    public void setSystole(final int systole) {
        this.systole = systole;
    }
    
    public int getDia() {
        return this.dia;
    }
    
    public void setDia(final int dia) {
        this.dia = dia;
    }
    
    public int getHr() {
        return this.hr;
    }
    
    public void setHr(final int hr) {
        this.hr = hr;
    }
    
    public int getDay() {
        return this.day;
    }
    
    public void setDay(final int day) {
        this.day = day;
    }
    
    public int getHour() {
        return this.hour;
    }
    
    public void setHour(final int hour) {
        this.hour = hour;
    }
    
    public int getMonth() {
        return this.month;
    }
    
    public void setMonth(final int month) {
        this.month = month;
    }
    
    public int getMinute() {
        return this.minute;
    }
    
    public void setMinute(final int minute) {
        this.minute = minute;
    }
    
    public int getResultCode() {
        return this.resultCode;
    }
    
    public void setResultCode(final int resultCode) {
        this.resultCode = resultCode;
    }
    
    public void setMAM(final int mam) {
        this.MAM = mam;
    }
    
    public boolean isArr() {
        return this.arr;
    }
    
    public void setArr(final boolean arr) {
        this.arr = arr;
    }
    
    public int getYear() {
        return this.year;
    }
    
    public void setYear(final int year) {
        this.year = year;
    }
    
    public int getCuffokr() {
        return this.cuffokr;
    }
    
    public boolean getIHB() {
        return this.IHB;
    }
    
    public boolean getAFIb() {
        return this.AFIb;
    }
    
    public boolean getIsFor3G() {
        return this.isFor3G;
    }
    
    public boolean isAFIb() {
        return this.AFIb;
    }
    
    public boolean isFor3G() {
        return this.isFor3G;
    }
    
    public boolean isIHB() {
        return this.IHB;
    }
    
    public void setAFIb(final boolean afIb) {
        this.AFIb = afIb;
    }
    
    public void setCuffokr(final int cuffokr) {
        this.cuffokr = cuffokr;
    }
    
    public void setFor3G(final boolean isFor3G) {
        this.isFor3G = isFor3G;
    }
    
    public void setIHB(final boolean ihb) {
        this.IHB = ihb;
    }
    
    public boolean isAM() {
        return this.AM;
    }
    
    public boolean isDiagnostic() {
        return this.diagnostic;
    }
    
    public int isDeviceMode() {
        return this.deviceMode;
    }
    
    public boolean isPM() {
        return this.PM;
    }
    
    public boolean isUsual() {
        return this.usual;
    }
    
    public int getMAM() {
        return this.MAM;
    }
    
    public void setDiagnostic(final boolean diagnostic) {
        this.diagnostic = diagnostic;
    }
    
    public void setUsual(final boolean usual) {
        this.usual = usual;
    }
    
    public void setAM(final boolean am) {
        this.AM = am;
    }
    
    public void setDeviceMode(final int deviceMode) {
        this.deviceMode = deviceMode;
    }
    
    public void setPM(final boolean pm) {
        this.PM = pm;
    }
    
    public int getCondition() {
        return this.condition;
    }
    
    public void setCondition(final int condition) {
        this.condition = condition;
    }
    
    public int getMAP() {
        return this.MAP;
    }
    
    public void setMAP(final int map) {
        this.MAP = map;
    }
    
    public int getMCBP() {
        return this.MCBP;
    }
    
    public void setMCBP(final int mcbp) {
        this.MCBP = mcbp;
    }
    
    public int getPVR() {
        return this.PVR;
    }
    
    public void setPVR(final int pvr) {
        this.PVR = pvr;
    }
    
    public int getCSBP() {
        return this.CSBP;
    }
    
    public void setCSBP(final int csbp) {
        this.CSBP = csbp;
    }
    
    public int getCPP() {
        return this.CPP;
    }
    
    public void setCPP(final int cpp) {
        this.CPP = cpp;
    }
    
    public int getErrorCode() {
        return this.errorCode;
    }
    
    public void setErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }
    
    @Override
    public String toString() {
        return "CurrentAndMData{systole=" + this.systole + ", dia=" + this.dia + ", hr=" + this.hr + ", day=" + this.day + ", hour=" + this.hour + ", month=" + this.month + ", minute=" + this.minute + ", MAM=" + this.MAM + ", arr=" + this.arr + ", year=" + this.year + ", cuffokr=" + this.cuffokr + ", IHB=" + this.IHB + ", AFIb=" + this.AFIb + ", isFor3G=" + this.isFor3G + ", resultCode=" + this.resultCode + ", usual=" + this.usual + ", diagnostic=" + this.diagnostic + ", AM=" + this.AM + ", PM=" + this.PM + ", deviceMode=" + this.deviceMode + "}";
    }
}
