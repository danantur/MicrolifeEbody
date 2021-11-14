// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.bean.scale;

import android.text.TextUtils;

import com.microlife_sdk.healthscale.NoAuthException;
import com.microlife_sdk.sdks.utils.LogUtils;
import com.microlife_sdk.sdks.utils.BytesUtils;
import com.microlife_sdk.healthscale.CsAlgoBuilderEx;

public class ScaleAnalyzer
{
    private static final String TAG = "ScaleAnalyzer";
    private static ScaleAnalyzer instance;
    public static final String kg = "kg";
    public static final String lb = "lb";
    private CsAlgoBuilderEx mBuilderEx;
    
    private ScaleAnalyzer() {
        this.mBuilderEx = new CsAlgoBuilderEx();
    }
    
    public static ScaleAnalyzer getInstance() {
        return ScaleAnalyzer.instance;
    }
    
    public ScaleMeasureResult getMeasureResult(final byte[] data, final String connectBluetoothName) throws NoAuthException {
        if (data == null || data.length < 18) {
            return null;
        }
        LogUtils.i("ScaleAnalyzer", "----getMeasureResult\u89e3\u6790\u7684\u6570\u636e=" + BytesUtils.bytes2HexStr(data));
        final float weight = this.get2ByteValue(data[5], data[4]) / 10.0f;
        final float fat = this.get2ByteValue(data[7], data[6]) / 10.0f;
        int resistance2;
        final int resistance = resistance2 = this.get2ByteValue(data[17], data[16]);
        final int year = this.get2ByteValue(data[9], data[8]);
        final int month = data[10] & 0xFF;
        final int day = data[11] & 0xFF;
        final int hour = data[12] & 0xFF;
        final int minute = data[13] & 0xFF;
        final int second = data[14] & 0xFF;
        final int weekOfYear = data[15] & 0xFF;
        final String measureTime = new StringBuffer(year + "").append("-").append(this.numberFormat(month + "")).append("-").append(this.numberFormat(day + "")).append(" ").append(this.numberFormat(hour + "")).append(":").append(this.numberFormat(minute + "")).append(":").append(this.numberFormat(second + "")).toString();
        final int unitByte = data[18] & 0xFF;
        final boolean isUnitKg = 1 != unitByte;
        if (TextUtils.equals((CharSequence)connectBluetoothName, (CharSequence)"Body Fat-B16")) {
            LogUtils.i("ScaleAnalyzer", "\u6536\u5230\u7684\u963b\u6297=" + resistance2);
            resistance2 = (resistance * 100 + 10000) / 103 - ScaleUserInfo.getScaleUserInfo().getHeight();
            LogUtils.i("ScaleAnalyzer", "\u5b9e\u6d4b\u963b\u6297=" + resistance2);
            resistance2 = (int)((weight * 40.0f + resistance2 * 60 + 10000.0f) * 1.0 / 100.0);
            LogUtils.i("ScaleAnalyzer", "\u4f20\u5165sdk\u7684 \u52a0\u5bc6\u963b\u6297=" + resistance2);
        }
        final ScaleUserInfo userInfo = ScaleUserInfo.getScaleUserInfo();
        final ScaleMeasureResult result = new ScaleMeasureResult();
        result.userId = userInfo.getUserId();
        result.sex = userInfo.getSex();
        result.height = userInfo.getHeight();
        result.roleType = userInfo.getRoleType();
        result.age = userInfo.getAge();
        result.isOnlyWeightData = false;
        result.measureTime = measureTime;
        result.resistance = (float)resistance;
        int age = userInfo.getAge();
        if (age < 18) {
            age = 18;
        }
        else if (age > 80) {
            age = 80;
        }
        final int orSex = 1 - userInfo.getSex();
        this.mBuilderEx.setUserInfo((float)result.height, weight, (byte)orSex, age, (float)resistance2);
        float bmi;
        float wr;
        float bmr;
        float vf;
        float mv;
        float sm;
        float bv;
        int ba;
        float protein;
        int score;
        if (fat > 0.0f) {
            try {
                wr = this.mBuilderEx.getTFR();
                bmr = this.mBuilderEx.getBMR();
                vf = this.mBuilderEx.getVFR();
                sm = this.mBuilderEx.getSLM();
                bv = this.mBuilderEx.getMSW();
                ba = (int)this.mBuilderEx.getBodyAge();
                if (Math.abs(ba - age) > 10) {
                    if (ba > age) {
                        ba = age + 10;
                    }
                    else {
                        ba = age - 10;
                    }
                }
                if (ba < 0) {
                    ba = 0;
                }
                protein = this.mBuilderEx.getPM();
                score = this.mBuilderEx.getScore();
                protein = this.dbzKgToPer(protein, weight);
            } catch (NoAuthException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            bmr = (wr = (vf = (mv = (sm = (bv = (protein = 0.0f))))));
            score = (ba = 0);
        }
        bmi = this.getBmi(weight, (float)result.height);
        result.fat = this.errorDataTo0(fat);
        result.weight = this.errorDataTo0(weight);
        result.waterRate = this.errorDataTo0(wr);
        result.bmr = this.errorDataTo0(bmr);
        result.visceralFat = this.errorDataTo0(vf);
        result.muscleVolume = this.errorDataTo0(sm);
        result.boneVolume = this.errorDataTo0(bv);
        result.bodyAge = this.errorDataTo0((float)ba);
        result.bodyScore = this.errorDataTo0((float)score);
        result.protein = this.errorDataTo0(protein);
        result.bmi = this.errorDataTo0(bmi);
        result.fatUnit = "%";
        result.weightUnit = (isUnitKg ? "kg" : "lb");
        if (result.fat == 0.0f && resistance == 0) {
            result.isOnlyWeightData = true;
        }
        else {
            result.isOnlyWeightData = false;
        }
        LogUtils.i("ScaleAnalyzer", "year = " + year);
        LogUtils.i("ScaleAnalyzer", "\u6d4b\u91cf\u51fa\u6765\u7684\u6570\u636e " + result);
        return result;
    }
    
    public ScaleMeasureResult getMeasureResultB16(final byte[] data) {
        final int year = this.get2ByteValue(data[8], data[7]);
        final int month = data[9] & 0xFF;
        final int day = data[10] & 0xFF;
        final int hour = data[11] & 0xFF;
        final int minute = data[12] & 0xFF;
        final int second = data[13] & 0xFF;
        final int weekOfYear = data[14] & 0xFF;
        final String measureTime = new StringBuffer(year + "").append("-").append(this.numberFormat(month + "")).append("-").append(this.numberFormat(day + "")).append(" ").append(this.numberFormat(hour + "")).append(":").append(this.numberFormat(minute + "")).append(":").append(this.numberFormat(second + "")).toString();
        final float weight = this.get2ByteValue(data[16], data[15]) / 10.0f;
        final float fat = this.get2ByteValue(data[18], data[17]) / 10.0f;
        int resistance2;
        final int resistance1 = resistance2 = this.get2ByteValue(data[20], data[19]);
        final int bpm = data[21] & 0xFF;
        final int unit = data[22];
        final boolean isUnitKg = 1 != unit;
        final ScaleUserInfo userInfo = ScaleUserInfo.getScaleUserInfo();
        resistance2 = (resistance1 * 100 + 10000) / 103 - userInfo.getHeight();
        resistance2 = (int)((weight * 40.0f + resistance2 * 60 + 10000.0f) * 1.0 / 100.0);
        final int sex = userInfo.getSex();
        int age = userInfo.getAge();
        final float height = (float)userInfo.getHeight();
        if (age < 18) {
            age = 18;
        }
        else if (age > 80) {
            age = 80;
        }
        final int orSex = 1 - sex;
        float wr = 0.0f;
        float bmr = 0.0f;
        float vf = 0.0f;
        float mv = 0.0f;
        float sm = 0.0f;
        float bv = 0.0f;
        float protein = 0.0f;
        float bmi = 0.0f;
        int ba = 0;
        int score = 0;
        try {
            this.mBuilderEx.setUserInfo(height, weight, (byte)orSex, age, (float)resistance2);
            if (fat > 0.0f) {
                wr = this.mBuilderEx.getTFR();
                bmr = this.mBuilderEx.getBMR();
                vf = this.mBuilderEx.getVFR();
                sm = this.mBuilderEx.getSLM();
                bv = this.mBuilderEx.getMSW();
                ba = (int)this.mBuilderEx.getBodyAge();
                if (Math.abs(ba - age) > 10) {
                    if (ba > age) {
                        ba = age + 10;
                    }
                    else {
                        ba = age - 10;
                    }
                }
                if (ba < 0) {
                    ba = 0;
                }
                protein = this.mBuilderEx.getPM();
                score = this.mBuilderEx.getScore();
                protein = this.dbzKgToPer(protein, weight);
            }
            else {
                bmr = (wr = (vf = (mv = (sm = (bv = (protein = -1.0f))))));
                score = (ba = 0);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        final ScaleMeasureResult result = new ScaleMeasureResult();
        result.userId = userInfo.getUserId();
        result.sex = userInfo.getSex();
        result.height = userInfo.getHeight();
        result.roleType = userInfo.getRoleType();
        result.age = userInfo.getAge();
        result.isOnlyWeightData = false;
        result.measureTime = measureTime;
        result.resistance = (float)resistance1;
        bmi = this.getBmi(weight, (float)result.height);
        LogUtils.i("ScaleAnalyzer", "---------BMI\u7684\u503c weight =" + weight + "---height=" + result.height + "----BMI=" + bmi);
        result.setWeight(this.errorDataTo0(weight));
        result.setFat(this.errorDataTo0(fat));
        result.setWaterRate(this.errorDataTo0(wr));
        result.setBmr(this.errorDataTo0(bmr));
        result.setVisceralFat(this.errorDataTo0(vf));
        result.setMuscleVolume(this.errorDataTo0(sm));
        result.setSkeletalMuscle(this.errorDataTo0(mv));
        result.setBoneVolume(this.errorDataTo0(bv));
        result.setProtein(this.errorDataTo0(protein));
        result.setBmi(this.errorDataTo0(bmi));
        result.setBodyScore(this.errorDataTo0((float)score));
        result.setHeartRate((int)this.errorDataTo0((float)bpm));
        result.setBodyAge(this.errorDataTo0((float)ba));
        result.weightUnit = (isUnitKg ? "kg" : "lb");
        return result;
    }
    
    public OfflineMeasureResult getoffMeasureResult(final byte[] data, final String connectBluetoothName) {
        if (data == null || data.length < 18) {
            return null;
        }
        LogUtils.i("ScaleAnalyzer", "----getMeasureResult\u89e3\u6790\u7684\u6570\u636e=" + BytesUtils.bytes2HexStr(data));
        final float weight = this.get2ByteValue(data[5], data[4]) / 10.0f;
        final float fat = this.get2ByteValue(data[7], data[6]) / 10.0f;
        int resistance2;
        final int resistance = resistance2 = this.get2ByteValue(data[17], data[16]);
        final int year = this.get2ByteValue(data[9], data[8]);
        final int month = data[10] & 0xFF;
        final int day = data[11] & 0xFF;
        final int hour = data[12] & 0xFF;
        final int minute = data[13] & 0xFF;
        final int second = data[14] & 0xFF;
        final int weekOfYear = data[15] & 0xFF;
        final String measureTime = new StringBuffer(year + "").append("-").append(this.numberFormat(month + "")).append("-").append(this.numberFormat(day + "")).append(" ").append(this.numberFormat(hour + "")).append(":").append(this.numberFormat(minute + "")).append(":").append(this.numberFormat(second + "")).toString();
        final boolean isUnitKg = true;
        final int suspectedData = data[18];
        final boolean isSuspectedData = (suspectedData & 0xFF) == 0xAA;
        if (TextUtils.equals((CharSequence)connectBluetoothName, (CharSequence)"Body Fat-B16")) {
            LogUtils.i("ScaleAnalyzer", "\u6536\u5230\u7684\u963b\u6297=" + resistance2);
            resistance2 = (resistance * 100 + 10000) / 103 - ScaleUserInfo.getScaleUserInfo().getHeight();
            LogUtils.i("ScaleAnalyzer", "\u5b9e\u6d4b\u963b\u6297=" + resistance2);
            resistance2 = (int)((weight * 40.0f + resistance2 * 60 + 10000.0f) * 1.0 / 100.0);
            LogUtils.i("ScaleAnalyzer", "\u4f20\u5165sdk\u7684 \u52a0\u5bc6\u963b\u6297=" + resistance2);
        }
        final OfflineMeasureUserInfo userInfo = OfflineMeasureUserInfo.getOfflineMeasureUserInfo();
        final OfflineMeasureResult result = new OfflineMeasureResult();
        result.userId = userInfo.userId;
        result.sex = userInfo.sex;
        result.height = userInfo.height;
        result.roleType = userInfo.roleType;
        result.age = userInfo.age;
        result.measureTime = measureTime;
        result.resistance = (float)resistance;
        int age = result.age;
        if (age < 18) {
            age = 18;
        }
        else if (age > 80) {
            age = 80;
        }
        final int orSex = 1 - userInfo.sex;
        this.mBuilderEx.setUserInfo((float)result.height, weight, (byte)orSex, age, (float)resistance2);
        float bmi = 0.0f;
        float wr;
        float bmr;
        float vf;
        float mv;
        float sm;
        float bv;
        int ba;
        float protein;
        int score;
        if (fat > 0.0f) {
            try {
                wr = this.mBuilderEx.getTFR();
                bmr = this.mBuilderEx.getBMR();
                vf = this.mBuilderEx.getVFR();
                sm = this.mBuilderEx.getSLM();
                bv = this.mBuilderEx.getMSW();
                ba = (int)this.mBuilderEx.getBodyAge();
                if (Math.abs(ba - age) > 10) {
                    if (ba > age) {
                        ba = age + 10;
                    }
                    else {
                        ba = age - 10;
                    }
                }
                if (ba < 0) {
                    ba = 0;
                }
                protein = this.mBuilderEx.getPM();
                score = this.mBuilderEx.getScore();
                protein = this.dbzKgToPer(protein, weight);
            } catch (NoAuthException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            bmr = (wr = (vf = (mv = (sm = (bv = (protein = 0.0f))))));
            score = (ba = 0);
        }
        bmi = this.getBmi(weight, (float)result.height);
        LogUtils.i("ScaleAnalyzer", "---------BMI\u7684\u503c weight =" + weight + "---height=" + result.height + "----BMI=" + bmi);
        result.fat = this.errorDataTo0(fat);
        result.weight = this.errorDataTo0(weight);
        result.waterRate = this.errorDataTo0(wr);
        result.bmr = this.errorDataTo0(bmr);
        result.visceralFat = this.errorDataTo0(vf);
        result.muscleVolume = this.errorDataTo0(sm);
        result.boneVolume = this.errorDataTo0(bv);
        result.bodyAge = this.errorDataTo0((float)ba);
        result.bodyScore = this.errorDataTo0((float)score);
        result.protein = this.errorDataTo0(protein);
        result.bmi = this.errorDataTo0(bmi);
        result.fatUnit = "%";
        result.weightUnit = "kg";
        result.isSuspectedData = isSuspectedData;
        return result;
    }
    
    private int get2ByteValue(final byte high, final byte low) {
        return (high & 0xFF) << 8 | (low & 0xFF);
    }
    
    private float errorDataTo0(final float value) {
        if (value < 0.0f) {
            return 0.0f;
        }
        return value;
    }
    
    private String numberFormat(final String number) {
        return (number.length() < 2) ? ("0" + number) : number;
    }
    
    private float dbzKgToPer(final float dbz, final float weight) {
        float preF = dbz / weight * 100.0f;
        preF = Math.round(preF * 10.0f) / 10.0f;
        preF = Math.min(preF, 100.0f);
        return preF;
    }
    
    private float getBmi(final float weight, final float userHeight) {
        final float bmi = weight / (userHeight * userHeight / 10000.0f);
        return Math.round(bmi * 10.0f) / 10.0f;
    }
    
    static {
        ScaleAnalyzer.instance = new ScaleAnalyzer();
    }
}
