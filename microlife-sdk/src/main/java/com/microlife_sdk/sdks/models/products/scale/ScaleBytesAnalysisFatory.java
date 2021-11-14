// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.models.products.scale;

import java.util.Calendar;

import com.microlife_sdk.healthscale.NoAuthException;
import com.microlife_sdk.sdks.bean.scale.OfflineMeasureResult;
import com.microlife_sdk.sdks.bean.scale.ScaleMeasureResult;
import com.microlife_sdk.sdks.utils.TimeUtils;
import com.microlife_sdk.sdks.bean.scale.ScaleAnalyzer;
import android.text.TextUtils;
import java.util.Arrays;
import com.microlife_sdk.sdks.utils.BytesUtils;
import com.microlife_sdk.sdks.utils.LogUtils;
import android.os.Looper;
import com.microlife_sdk.sdks.interfaces.scale.IScaleMeasureCallback;
import android.os.Handler;

public class ScaleBytesAnalysisFatory
{
    private static final String TAG = "ScaleBytesAnalysis";
    public boolean needUpdateUserInfo2Front;
    private Handler myMainHandler;
    private String connectBluetoothName;
    private String connectBluetoothAddrss;
    private IScaleMeasureCallback mIScaleMeasureCallback;
    private byte[] lastData;
    private static final byte[] fatMesureResult;
    private long lastMeasureTime;
    
    public ScaleBytesAnalysisFatory() {
        this.needUpdateUserInfo2Front = true;
        this.myMainHandler = new Handler(Looper.getMainLooper());
    }
    
    public void setConnectBluetoothName(final String connectBluetoothName) {
        this.connectBluetoothName = connectBluetoothName;
    }
    
    public String getConnectBluetoothName() {
        return this.connectBluetoothName;
    }
    
    public String getConnectBluetoothAddrss() {
        return this.connectBluetoothAddrss;
    }
    
    public void setConnectBluetoothAddrss(final String connectBluetoothAddrss) {
        this.connectBluetoothAddrss = connectBluetoothAddrss;
    }
    
    public void setmIMeasureResultCallback(final IScaleMeasureCallback IScaleMeasureCallback) {
        this.mIScaleMeasureCallback = IScaleMeasureCallback;
    }
    
    public void resultAnalysis(final byte[] data) {
        if (data == null || data.length <= 3) {
            LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u4e0d\u80fd\u5904\u7406\u9519\u8bef\u6570\u636e");
            return;
        }
        if (data[0] != -115) {
            LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u6570\u636e\u5305\u5934\u4e0d\u6b63\u786e");
            return;
        }
        if (data.length != (data[1] & 0xFF) + 3) {
            LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u6570\u636e\u957f\u5ea6\u4e0d\u6b63\u786e");
            return;
        }
        LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u89e3\u6790\u7684\u6570\u636e=" + BytesUtils.bytes2HexStr(data));
        if (Arrays.equals(this.lastData, data)) {
            LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u89e3\u6790\u7684\u6570\u636e\u4e0e\u4e0a\u4e00\u6b21\u662f\u91cd\u590d\u7684 \u5c4f\u853d\u6389");
            return;
        }
        this.lastData = data;
        final int cmdCode = data[2] & 0xFF;
        LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-cmdCode = " + Integer.toHexString(cmdCode));
        switch (cmdCode) {
            case 144: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u79e4\u5df2\u5524\u9192");
                this.myMainHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback != null) {
                            ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback.onScaleWake();
                        }
                    }
                });
                break;
            }
            case 145: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u79e4\u5df2\u4f11\u7720");
                this.myMainHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback != null) {
                            ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback.onScaleSleep();
                        }
                    }
                });
                break;
            }
            case 146: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u79f0\u91cd\u5355\u4f4d\u5df2\u7ecf\u6539\u53d8");
                this.onScaleUnitChanged(data);
                break;
            }
            case 147: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u63a5\u6536\u5230\u7b2cn\u7ec4\u95f9\u949f");
                break;
            }
            case 152: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u63a5\u6536\u5230\u5f53\u524d\u65f6\u949f");
                this.onReceiveTime(data);
                break;
            }
            case 156: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u63a5\u6536\u5230\u7248\u672c\u4fe1\u606f");
                this.onReceiveScaleVersion(data);
                break;
            }
            case 157:
            case 158: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u63a5\u6536\u5230\u4f53\u8102\u80aa\u6d4b\u91cf\u7ed3\u679c");
                if (TextUtils.equals((CharSequence)"Body Fat-B1", (CharSequence)this.connectBluetoothName) || TextUtils.equals((CharSequence)"Body Fat-B2", (CharSequence)this.connectBluetoothName) || (!TextUtils.isEmpty((CharSequence)this.connectBluetoothName) && this.connectBluetoothName.toLowerCase().contains("lnv_11")) || TextUtils.equals((CharSequence)"GOQii Contour", (CharSequence)this.connectBluetoothName) || TextUtils.equals((CharSequence)"GOQii balance", (CharSequence)this.connectBluetoothName) || TextUtils.equals((CharSequence)"GOQii Essential", (CharSequence)this.connectBluetoothName)) {
                    LogUtils.i("ScaleBytesAnalysis", "------\u89e3\u6790\u7684\u662f \u6ca1\u6709 \u6ca1\u6709 \u6ca1\u6709\u5fc3\u7387 \u79e4");
                    this.onReceiveMeasureResult(data);
                    break;
                }
                if (TextUtils.equals((CharSequence)"Body Fat-B16", (CharSequence)this.connectBluetoothName)) {
                    LogUtils.i("ScaleBytesAnalysis", "------\u89e3\u6790\u7684\u662f \u6709 \u6709 \u6709\u5fc3\u7387 \u79e4 B16");
                    this.fatMesure(data, cmdCode);
                    break;
                }
                break;
            }
            case 160: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u63a5\u6536\u5230\u5386\u53f2\u6d4b\u91cf\u6570\u636e");
                this.onReceiveHistoryMeasureResult(data);
                break;
            }
            case 161: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u63a5\u6536\u5230\u5347\u7ea7\u5305\u5e94\u7b54");
                break;
            }
            case 162: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u5347\u7ea7\u7ed3\u679c");
                this.onUpgradeResult(data);
            }
            case 164: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u4f4e\u7535\u63d0\u793a");
                this.onScaleLowPower(data);
                break;
            }
            case 165: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u6d4b\u8102\u51fa\u9519");
                break;
            }
            case 166: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u63a5\u6536\u5230\u4fee\u6539\u95f9\u949fACK");
                break;
            }
            case 167: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u63a5\u6536\u5230OTA\u5347\u7ea7\u5c31\u7eea\u6d88\u606f");
                this.onOtaUpgradeReady(data);
            }
            case 169: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u79e4\u5386\u53f2\u8bb0\u5f55\u4e0a\u4f20\u5b8c\u6bd5\u6d88\u606f");
                this.onHistoryDownloadDone(data);
                break;
            }
            case 170: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-scaleOtaResponse \u5347\u7ea7 " + BytesUtils.bytes2HexStr(data));
                break;
            }
            case 176: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u7528\u6237\u4fe1\u606f\u8bbe\u7f6e\u6210\u529f");
                this.onUserInfoSettingSucceeded();
                break;
            }
            case 177: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u79e4\u54cd\u5e94\u65e0\u8fde\u63a5\u524d\u6d4b\u91cf\u7ed3\u679c\u54cd\u5e94");
                break;
            }
            case 182: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u4e0a\u4f20\u79e4\u662f\u5426\u4e0e\u624b\u673a\u7ed1\u5b9a\u8fc7");
                break;
            }
            case 183: {
                LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u7ed1\u5b9a\u786e\u8ba4\u6307\u4ee4");
                break;
            }
        }
    }
    
    private void fatMesure(final byte[] data, final int cmd) {
        if (cmd == 157) {
            LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u8102\u80aa\u6d4b\u91cf\u7ed3\u679c(\u7b2c1\u5305)" + BytesUtils.bytes2HexStr(data));
            if (data.length != 19) {
                LogUtils.i("ScaleBytesAnalysis", "\u7b2c1\u5305\u957f\u5ea6\u4e0d\u7b49\u4e8e19 \u662f\u9519\u8bef\u7684\u5305");
                return;
            }
            System.arraycopy(data, 3, ScaleBytesAnalysisFatory.fatMesureResult, 0, 15);
        }
        else if (cmd == 158) {
            LogUtils.i("ScaleBytesAnalysis", "ScaleBytesAnalysisFatory-\u8102\u80aa\u6d4b\u91cf\u7ed3\u679c(\u7b2c2\u5305)" + BytesUtils.bytes2HexStr(data));
            if (data.length != 12) {
                LogUtils.i("ScaleBytesAnalysis", "\u7b2c2\u5305\u957f\u5ea6\u4e0d\u7b49\u4e8e12 \u662f\u9519\u8bef\u7684\u5305");
                return;
            }
            System.arraycopy(data, 3, ScaleBytesAnalysisFatory.fatMesureResult, 15, 8);
            this.parseB16Mesure(ScaleBytesAnalysisFatory.fatMesureResult);
        }
    }
    
    private void parseB16Mesure(final byte[] fatMesureResult) {
        LogUtils.i("ScaleBytesAnalysis", "----\u89e3\u6790B16\u7684\u4f53\u8102\u6570\u636e");
        final ScaleMeasureResult result = ScaleAnalyzer.getInstance().getMeasureResultB16(fatMesureResult);
        result.setBluetoothAddress(this.connectBluetoothAddrss);
        result.setBluetoothName(this.connectBluetoothName);
        final long time = TimeUtils.parseFormatter1Time(result.getMeasureTime()).getTime();
        if (time - this.lastMeasureTime <= 6000L) {
            LogUtils.i("ScaleBytesAnalysis", "\u6d4b\u91cf\u65f6\u95f4\u592a\u8fd1 \u88ab\u5c4f\u853d\u6389 lastMeasureTime=" + TimeUtils.formatTime1(this.lastMeasureTime) + "--\u8fd9\u6b21time=" + TimeUtils.formatTime1(time));
            return;
        }
        this.lastMeasureTime = time;
        this.myMainHandler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback != null) {
                    LogUtils.i("ScaleBytesAnalysis", "----\u63a5\u6536\u5230\u4e86B16\u6d4b\u91cf\u6570\u636e\uff1a" + result);
                    ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback.onReceiveMeasureResult(result);
                }
            }
        });
    }
    
    private void onReceiveMeasureResult(final byte[] data) {
        final ScaleMeasureResult result;
        try {
            result = ScaleAnalyzer.getInstance().getMeasureResult(data, this.connectBluetoothName);
        } catch (NoAuthException e) {
            e.printStackTrace();
            return;
        }
        result.setBluetoothAddress(this.connectBluetoothAddrss);
        result.setBluetoothName(this.connectBluetoothName);
        final long time = TimeUtils.parseFormatter1Time(result.getMeasureTime()).getTime();
        if (time - this.lastMeasureTime <= 6000L) {
            LogUtils.i("ScaleBytesAnalysis", "\u6d4b\u91cf\u65f6\u95f4\u592a\u8fd1 \u88ab\u5c4f\u853d\u6389 lastMeasureTime=" + TimeUtils.formatTime1(this.lastMeasureTime) + "--\u8fd9\u6b21time=" + TimeUtils.formatTime1(time));
            return;
        }
        this.lastMeasureTime = time;
        this.myMainHandler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback != null) {
                    LogUtils.i("ScaleBytesAnalysis", "----\u63a5\u6536\u5230\u4e86\u6d4b\u91cf\u6570\u636e\uff1a" + result);
                    ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback.onReceiveMeasureResult(result);
                }
            }
        });
    }
    
    private void onReceiveScaleVersion(final byte[] data) {
        final int ble0 = data[3] & 0xFF;
        final int ble2 = data[4] & 0xFF;
        final int scale0 = data[5] & 0xFF;
        final int scale2 = data[6] & 0xFF;
        final int coefficient0 = data[7] & 0xFF;
        final int coefficient2 = data[8] & 0xFF;
        final int arithmetic0 = data[9] & 0xFF;
        final int arithmetic2 = data[10] & 0xFF;
        final int bleVer = ble2 << 8 | ble0;
        final int scaleVer = scale2 << 8 | scale0;
        final int coefficientVer = coefficient2 << 8 | coefficient0;
        final int arithmeticVer = arithmetic2 << 8 | arithmetic0;
        LogUtils.i("ScaleBytesAnalysis", "receive scale version info.bleVer:" + bleVer + "scaleVer:" + scaleVer + "coefficientVer:" + coefficientVer + "arithmeticVer:" + arithmeticVer);
    }
    
    private int get2ByteValue(final byte high, final byte low) {
        return (high & 0xFF) << 8 | (low & 0xFF);
    }
    
    private void onScaleUnitChanged(final byte[] data) {
        final String scaleUnit = (data[3] == 1) ? "kg" : "lb";
    }
    
    private void onReceiveHistoryMeasureResult(final byte[] data) {
        final OfflineMeasureResult result = ScaleAnalyzer.getInstance().getoffMeasureResult(data, this.connectBluetoothName);
        result.setBluetoothAddress(this.connectBluetoothAddrss);
        result.setBluetoothName(this.connectBluetoothName);
        this.myMainHandler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback != null) {
                    ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback.onReceiveHistoryRecord(result);
                }
            }
        });
    }
    
    private void onUpgradeResult(final byte[] data) {
        final int result = data[3] & 0xFF;
        final int type = data[4] & 0xFF;
        LogUtils.i("ScaleBytesAnalysis", "--\u63a5\u6536\u5230\u5347\u7ea7\u7ed3\u679c result:" + result + "   type = " + type);
    }
    
    private void onScaleLowPower(final byte[] data) {
        this.myMainHandler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback != null) {
                    ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback.onLowPower();
                }
            }
        });
    }
    
    private void onOtaUpgradeReady(final byte[] data) {
    }
    
    private void onHistoryDownloadDone(final byte[] data) {
        this.myMainHandler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback != null) {
                    ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback.onHistoryDownloadDone();
                }
            }
        });
    }
    
    private void onUserInfoSettingSucceeded() {
        if (this.needUpdateUserInfo2Front) {
            this.needUpdateUserInfo2Front = false;
            this.myMainHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback != null) {
                        ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback.setUserInfoSuccess();
                    }
                }
            });
        }
    }
    
    private void onReceiveTime(final byte[] data) {
        LogUtils.i("ScaleBytesAnalysis", "----onReceiveAlarmClock---receive scale clock.");
        final int year = this.get2ByteValue(data[4], data[3]);
        final int month = data[5] & 0xFF;
        final int day = data[6] & 0xFF;
        final int hour = data[7] & 0xFF;
        final int minute = data[8] & 0xFF;
        final int second = data[9] & 0xFF;
        final int weekOfYear = data[10] & 0xFF;
        LogUtils.i("ScaleBytesAnalysis", "\u63a5\u6536\u5230\u65f6\u95f4=year:" + year + "month:" + month + "day:" + day + "hour:" + hour + "minute:" + minute + "second:" + second + "weekOfYear:" + weekOfYear);
        final Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        cal.set(5, day);
        cal.set(11, hour);
        cal.set(12, minute);
        cal.set(13, second);
        final long scaleDateLong = cal.getTime().getTime();
        this.myMainHandler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback != null) {
                    ScaleBytesAnalysisFatory.this.mIScaleMeasureCallback.receiveTime(scaleDateLong);
                }
            }
        });
    }
    
    public void needUpdateUserInfo2Front(final boolean need) {
        this.needUpdateUserInfo2Front = need;
    }
    
    static {
        fatMesureResult = new byte[23];
    }
}
