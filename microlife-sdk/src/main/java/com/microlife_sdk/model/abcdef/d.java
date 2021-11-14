package com.microlife_sdk.model.abcdef;

import com.microlife_sdk.model.data.CurrentAndMData;
import java.util.List;
import java.util.ArrayList;
import com.microlife_sdk.model.data.NocturnalModeDRecord;
import com.microlife_sdk.model.data.DiagnosticDRecord;
import com.microlife_sdk.model.data.DRecord;
import com.microlife_sdk.model.data.DeviceInfo;
import java.util.BitSet;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import com.microlife_sdk.model.data.VersionData;

// 
// Decompiled by Procyon v0.5.36
// 

public class d
{
    public static final String a = "d";
    
    public static void a(final String s, final VersionData versionData) {
        final e e = new e(s);
        final e e8;
        final e e7;
        final e e6;
        final e e5;
        final e e4;
        final e e3;
        final e e2 = e3 = (e4 = (e5 = (e6 = (e7 = (e8 = e)))));
        new e(s);
        versionData.setFWName(a(e3.a(6)));
        versionData.setYear(e4.c(2) + 2000);
        versionData.setMonth(e5.c(2));
        versionData.setDay(e6.c(2));
        versionData.setMaxUser(e7.c(2));
        versionData.setMaxMemory(e8.c(2));
        final BitSet value = BitSet.valueOf(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(e.c(2)).array());
        final BitSet set4;
        final BitSet set3;
        final BitSet set2;
        final BitSet set;
        versionData.setOptionIHB((set = (set2 = (set3 = (set4 = value)))).get(0));
        versionData.setOptionAfib(set.get(1));
        versionData.setOPtionMAM(set2.get(2));
        versionData.setOptionAmbientT(set3.get(3));
        versionData.setOptionTubeless(set4.get(6));
        versionData.setOptionDeviceID(value.get(7));
        double deviceBatteryVoltage;
        if (e2.a() > 0) {
            deviceBatteryVoltage = e2.c(2) / 10.0;
        }
        else {
            deviceBatteryVoltage = 0.0;
        }
        versionData.setDeviceBatteryVoltage(deviceBatteryVoltage);
    }
    
    public static void c(final String s, final VersionData versionData) {
        final e e = new e(s);
        final e e10;
        final e e9;
        final e e8;
        final e e7;
        final e e6;
        final e e5;
        final e e4;
        final e e3;
        final e e2 = e3 = (e4 = (e5 = (e6 = (e7 = (e8 = (e9 = (e10 = e)))))));
        versionData.setFWName(a(e3.a(6)));
        versionData.setYear(e2.c(2) + 2000);
        versionData.setMonth(e4.c(2));
        versionData.setDay(e5.c(2));
        versionData.setMaxUser(e6.c(2));
        versionData.setMaxMemory(e7.c(2));
        final BitSet value = BitSet.valueOf(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(e8.c(2)).array());
        final BitSet set2;
        final BitSet set;
        versionData.setOptionDiagnosticModeAFib((set = (set2 = value)).get(3));
        versionData.setOpenNoUsualModeAFib(set.get(2));
        versionData.setOpenNocturnalMode(set2.get(1));
        versionData.setOpenBPtype(value.get(0));
        versionData.setDeviceBatteryVoltage(e9.c(2) / 10.0);
        versionData.setProtocolVersion(e10.c(4));
        e.a(2);
        versionData.setCurrentMode(e.c(2));
    }
    
    public static void b(final String s, final VersionData versionData) {
        final e e = new e(s);
        final e e10;
        final e e9;
        final e e8;
        final e e7;
        final e e6;
        final e e5;
        final e e4;
        final e e3;
        final e e2 = e3 = (e4 = (e5 = (e6 = (e7 = (e8 = (e9 = (e10 = e)))))));
        versionData.setFWName(a(e3.a(6)));
        versionData.setYear(e2.c(2) + 2000);
        versionData.setMonth(e4.c(2));
        versionData.setDay(e5.c(2));
        versionData.setMaxUser(e6.c(2));
        versionData.setMaxMemory(e7.c(2));
        final BitSet value = BitSet.valueOf(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(e8.c(2)).array());
        versionData.setOptionCBP(value.get(2));
        versionData.setOptionAfib(value.get(1));
        versionData.setDeviceBatteryVoltage(e9.c(2) / 10.0);
        versionData.setProtocolVersion(e10.c(4));
        final int c;
        final boolean optionIHB = (c = e.c(2)) == 1;
        final int n = c;
        versionData.setOptionIHB(optionIHB);
        versionData.setOptionPAD(n == 2);
    }
    
    public static String a(final String s) {
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            final StringBuilder sb2 = sb;
            final int beginIndex = i;
            sb2.append((char)Integer.parseInt(s.substring(beginIndex, i = beginIndex + 2), 16));
        }
        return sb.toString();
    }
    
    public static void a(String connectType, final DeviceInfo deviceInfo, final int n) {
        final e e = new e(connectType);
        final e e3;
        final e e2 = e3 = e;
        e3.c(2);
        deviceInfo.setID(e3.a(12));
        if (e.c(2) == 66) {
            connectType = "Bluetooth";
        }
        else {
            connectType = "USB";
        }
        final e e4 = e2;
        deviceInfo.setConnectType(connectType);
        e4.c(2);
        deviceInfo.setMeasurementTimes(e4.c(6));
        while (e2.a() > 0) {
            int c;
            switch (c = e2.c(2)) {
                case 70: {
                    c = 15;
                    break;
                }
                case 69: {
                    c = 14;
                    break;
                }
                case 68: {
                    c = 13;
                    break;
                }
                case 67: {
                    c = 12;
                    break;
                }
                case 66: {
                    c = 11;
                    break;
                }
                case 65: {
                    c = 10;
                    break;
                }
            }
            deviceInfo.setErrHappendTimes(c, e2.c(4));
        }
    }
    
    public static void a(final String s, final DeviceInfo deviceInfo) {
        final e e;
        final Boolean value = (e = new e(s)).c(2) == 1;
        final e e2 = e;
        final e e3 = e;
        final e e4 = e;
        final e e5 = e;
        final e e6 = e;
        final e e7 = e;
        deviceInfo.setisTimeReady(value);
        deviceInfo.setYear(e7.c(2) + 2000);
        deviceInfo.setMonth(e6.c(2));
        deviceInfo.setDay(e5.c(2));
        deviceInfo.setHour(e4.c(2));
        deviceInfo.setMinute(e3.c(2));
        deviceInfo.setSecond(e2.c(2));
    }
    
    public static void c(final String s, final DeviceInfo deviceInfo) {
        final e e = new e(s);
        e.a(2);
        deviceInfo.setSn(a(e.a(e.a())));
    }
    
    public static void b(final String s, final DeviceInfo deviceInfo) {
        final e e2;
        final e e = e2 = new e(s);
        e.c(2);
        final Boolean value = e.c(2) == 1;
        final e e3 = e2;
        final e e4 = e2;
        final e e5 = e2;
        final e e6 = e2;
        deviceInfo.setOpenNocturnal(value);
        deviceInfo.setYear(e6.c(2) + 2000);
        deviceInfo.setMonth(e5.c(2));
        deviceInfo.setDay(e4.c(2));
        deviceInfo.setHour(e3.c(2));
    }
    
    public static void a(final String s, final DRecord dRecord, final int n) {
        final e e = new e(s);
        if (n != 49 && n != 58) {
            if (n != 81) {
                if (n != 65) {
                    if (n == 66) {
                        c(e, dRecord, n);
                    }
                }
                else {
                    d(e, dRecord, n);
                }
            }
            else {
                e(e, dRecord, n);
            }
        }
        else {
            b(e, dRecord, n);
        }
    }
    
    public static void b(final e e, final DRecord dRecord, final int n) {
        final e e2 = new e(e.a(14));
        final int c = e2.c(2);
        final int c2 = e2.c(2);
        final int c3 = e2.c(2);
        final int noOfCurrentMeasurement = c2;
        dRecord.setMode(c);
        dRecord.setNoOfCurrentMeasurement(noOfCurrentMeasurement);
        dRecord.setHistoryMeasuremeNumber(c3);
        final int c4 = e2.c(2);
        final int c5 = e2.c(2);
        dRecord.setUserNumber(c4);
        dRecord.setMAMState(c5);
        a(new e(e.a(a(n) * 3)), dRecord, n);
        f(e, dRecord, n);
    }
    
    public static void e(final e e, final DRecord dRecord, final int n) {
        final e e2 = new e(e.a(14));
        final int c = e2.c(2);
        final int c2 = e2.c(2);
        final int c3 = e2.c(2);
        final int noOfCurrentMeasurement = c2;
        dRecord.setMode(c);
        dRecord.setNoOfCurrentMeasurement(noOfCurrentMeasurement);
        dRecord.setHistoryMeasuremeNumber(c3);
        a(new e(e.a(a(n) * 3)), dRecord, n);
        f(e, dRecord, n);
    }
    
    public static void d(final e e, final DRecord dRecord, final int n) {
        final e e2 = new e(e.a(14));
        final int c = e2.c(2);
        final int c2 = e2.c(4);
        dRecord.setMode(c);
        dRecord.setHistoryMeasuremeNumber(c2);
        f(e, dRecord, n);
    }
    
    public static void c(final e e, final DRecord dRecord, int c) {
        final int n = c;
        final e e2 = new e(e.a(14));
        final int c2 = e2.c(2);
        c = e2.c(4);
        final int c3 = e2.c(2);
        final int historyMeasuremeNumber = c;
        dRecord.setMode(c2);
        dRecord.setHistoryMeasuremeNumber(historyMeasuremeNumber);
        dRecord.setAverage(c3);
        f(e, dRecord, n);
    }
    
    public static void a(String s, final DiagnosticDRecord diagnosticDRecord, final int n) {
        final e e = new e(s);
        final e e5;
        final e e4;
        final e e3;
        final e e2 = e3 = (e4 = (e5 = e));
        e3.a(14);
        e3.a(28);
        a(e2, diagnosticDRecord, n);
        s = e2.a(14);
        final e e6 = new e(s);
        final e e8;
        final e e7 = e8 = e6;
        diagnosticDRecord.setSYSavgAll(e8.c(2));
        diagnosticDRecord.setDIAavgAll(e7.c(2));
        diagnosticDRecord.setPULavgAll(e6.c(2));
        s = e4.a(14);
        final e e9 = new e(s);
        final e e11;
        final e e10 = e11 = e9;
        diagnosticDRecord.setSYSavgM(e11.c(2));
        diagnosticDRecord.setDIAavgM(e10.c(2));
        diagnosticDRecord.setPULavgM(e9.c(2));
        s = e5.a(14);
        final e e12 = new e(s);
        final e e14;
        final e e13 = e14 = e12;
        diagnosticDRecord.setSYSavgE(e14.c(2));
        diagnosticDRecord.setDIAavgE(e13.c(2));
        diagnosticDRecord.setPULavgE(e12.c(2));
        s = e.a(14);
        final e e15 = new e(s);
        final e e20;
        final e e19;
        final e e18;
        final e e17;
        final e e16 = e17 = (e18 = (e19 = (e20 = e15)));
        diagnosticDRecord.setDiagMemoryEndIndex01b(e17.c(2));
        diagnosticDRecord.setDiagMemoryEndIndex02b(e16.c(2));
        diagnosticDRecord.setDiagMemorySet01b(e18.c(2));
        diagnosticDRecord.setDiagMemorySet02b(e19.c(2));
        diagnosticDRecord.setDiagTimes(e20.c(2));
        diagnosticDRecord.setDiagOver(e15.c(2));
    }
    
    public static void a(String a, final NocturnalModeDRecord nocturnalModeDRecord, final int n) {
        final e e = new e(a);
        final e e2 = e;
        a = e2.a(28);
        final e e3 = new e(a);
        final e e8;
        final e e7;
        final e e6;
        final e e5;
        final e e4 = e5 = (e6 = (e7 = (e8 = e3)));
        nocturnalModeDRecord.setIndex(e5.c(2));
        nocturnalModeDRecord.setYear(e4.c(2));
        nocturnalModeDRecord.setMonth(e6.c(2));
        nocturnalModeDRecord.setDay(e7.c(2));
        nocturnalModeDRecord.setHour(e8.c(2));
        nocturnalModeDRecord.setMinute(e3.c(2));
        nocturnalModeDRecord.setMData(a(e, n));
    }
    
    public static void a(final e e, final DiagnosticDRecord diagnosticDRecord, final int n) {
        final int n2 = 112;
        final e e2 = new e(e.a(784));
        final ArrayList<List<CurrentAndMData>> dayData = new ArrayList<List<CurrentAndMData>>();
        while (e2.a() >= n2) {
            dayData.add(a(new e(e2.a(n2)), n));
        }
        diagnosticDRecord.setDayData(dayData);
    }
    
    public static List<CurrentAndMData> a(final e e, final int n) {
        final ArrayList<CurrentAndMData> list = new ArrayList<CurrentAndMData>();
        final int a = a(n);
        while (e.a() >= a) {
            final String a2;
            if (!a(a2 = e.a(a), n)) {
                final ArrayList<CurrentAndMData> list2 = list;
                final CurrentAndMData currentAndMData = new com.microlife_sdk.model.data.CurrentAndMData();
                final CurrentAndMData currentAndMData2 = currentAndMData;
                final String s = a2;
                new CurrentAndMData();
                currentAndMData.importHexString(s, n);
                list2.add(currentAndMData);
            }
        }
        return list;
    }
    
    public static void f(final e e, final DRecord dRecord, final int n) {
        final ArrayList<CurrentAndMData> mData = new ArrayList<CurrentAndMData>();
        final int a = a(n);
        while (e.a() >= a) {
            final String a2;
            if (!a(a2 = e.a(a), n)) {
                final ArrayList<CurrentAndMData> list = mData;
                final CurrentAndMData currentAndMData = new com.microlife_sdk.model.data.CurrentAndMData();
                final CurrentAndMData currentAndMData2 = currentAndMData;
                final String s = a2;
                new CurrentAndMData();
                currentAndMData.importHexString(s, n);
                list.add(currentAndMData);
            }
        }
        dRecord.setMData(mData);
    }
    
    public static void a(final e e, final DRecord dRecord, final int n) {
        final CurrentAndMData[] currentData = new CurrentAndMData[3];
        boolean b = false;
        int n2 = 0;
        final int a = a(n);
        while (e.a() >= a) {
            final String a2;
            if (!a(a2 = e.a(a), n)) {
                final CurrentAndMData[] array = currentData;
                final int n3 = n2;
                final CurrentAndMData currentAndMData = new com.microlife_sdk.model.data.CurrentAndMData();
                final CurrentAndMData currentAndMData2 = currentAndMData;
                final String s = a2;
                new CurrentAndMData();
                currentAndMData.importHexString(s, n);
                array[n3] = currentAndMData;
                ++n2;
                b = true;
            }
        }
        final boolean measureMode = b;
        dRecord.setCurrentData(currentData);
        dRecord.setMeasureMode(measureMode);
    }
    
    public static boolean a(final String s, final int n) {
        String s2 = "00000000000000";
        if (n != 49) {
            if (n == 58) {
                s2 = "00000000000000000000";
                return s.equals(s2);
            }
            if (n != 81) {
                if (n != 65 && n != 66) {
                    return s.equals(s2);
                }
                s2 = "0000000000000000000000000000";
                return s.equals(s2);
            }
        }
        s2 = "00000000000000";
        return s.equals(s2);
    }
    
    public static int a(final int n) {
        int n2 = 14;
        if (n != 49) {
            if (n == 58) {
                n2 = 20;
                return n2;
            }
            if (n != 81) {
                if (n != 65 && n != 66) {
                    return n2;
                }
                n2 = 30;
                return n2;
            }
        }
        n2 = 14;
        return n2;
    }
}
