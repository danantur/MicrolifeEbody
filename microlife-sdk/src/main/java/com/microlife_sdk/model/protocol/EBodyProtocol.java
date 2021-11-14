// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.protocol;

import com.microlife_sdk.sdks.bean.scale.ScaleUserInfo;
import android.net.Uri;
import android.content.Context;
import com.microlife_sdk.sdks.EbelterSdkManager;
import com.microlife_sdk.sdks.utils.TimeUtils;
import com.microlife_sdk.sdks.bean.scale.OfflineMeasureResult;
import com.microlife_sdk.model.data.EBodyMeasureData;
import com.microlife_sdk.sdks.bean.scale.ScaleMeasureResult;
import android.bluetooth.BluetoothDevice;
import com.microlife_sdk.sdks.enums.ProductStyle;
import com.microlife_sdk.model.XlogUtils;
import android.app.Activity;
import com.microlife_sdk.sdks.interfaces.scale.IScaleMeasureCallback;
import com.microlife_sdk.sdks.interfaces.IConnectStationCallback;
import com.microlife_sdk.sdks.interfaces.IBlueStationListener;
import com.microlife_sdk.sdks.managers.ScaleManager;
import com.microlife_sdk.model.bluetooth.MyBluetoothLE;

public class EBodyProtocol
{
    public static final String TAG = "EBodyProtocol";
    public static EBodyProtocol protocol;
    public MyBluetoothLE myBluetooth;
    public ScaleManager mScaleManager;
    public ConnectState mConnectState;
    public boolean isSimulationMode;
    public OnConnectStateListener onConnectStateListener;
    public OnDataResponseListener onDataResponseListener;
    public final IBlueStationListener mBlueStationListener;
    public final IConnectStationCallback mConnectStationCallback;
    public final IScaleMeasureCallback mScaleMeasureCallback;
    
    public static EBodyProtocol getInstance(final Activity activity, final boolean b, final boolean b2, final String s) {
        XlogUtils.initXlog(activity, b2);
        if (EBodyProtocol.protocol == null) {
            EBodyProtocol.protocol = new EBodyProtocol(activity, b, b2);
        }
        return EBodyProtocol.protocol;
    }
    
    public static EBodyProtocol getInstanceForBase(final Activity activity, final boolean b, final boolean b2, final String s) {
        if (EBodyProtocol.protocol == null) {
            EBodyProtocol.protocol = new EBodyProtocol(activity, b, b2);
        }
        return EBodyProtocol.protocol;
    }
    
    public EBodyProtocol(final Activity activity, final boolean b, final boolean b2) {
        this.isSimulationMode = false;
        this.mBlueStationListener = new IBlueStationListener() {
            public void STATE_OFF() {
                XlogUtils.xLog(EBodyProtocol.access$000(), "Bluetooth status of mobile phone:     Closed");
            }
            
            public void STATE_TURNING_OFF() {
                XlogUtils.xLog(EBodyProtocol.access$000(), "Bluetooth status of mobile phone:     Closing...");
            }
            
            public void STATE_ON() {
                XlogUtils.xLog(EBodyProtocol.access$000(), "Bluetooth status of mobile phone:     Open");
            }
            
            public void STATE_TURNING_ON() {
                XlogUtils.xLog(EBodyProtocol.access$000(), "Bluetooth status of mobile phone:     Opening");
            }
        };
        this.mConnectStationCallback = (IConnectStationCallback)new IConnectStationCallback() {
            public void onConnected(final ProductStyle productStyle, final BluetoothDevice bluetoothDevice) {
                XlogUtils.xLog(EBodyProtocol.access$000(), "-----onConnected-----" + bluetoothDevice.getName() + "--ScaleAddress=" + bluetoothDevice.getAddress());
                XlogUtils.xLog(EBodyProtocol.access$000(), "Body fat scale connection status:     Connected\n ScaleName = " + bluetoothDevice.getName() + "--ScaleAddress=" + bluetoothDevice.getAddress());
                EBodyProtocol.this.connect(bluetoothDevice);
                EBodyProtocol.this.onConnectStateListener.onConnectionState(ConnectState.Connected);
            }
            
            public void onConnecting(final ProductStyle productStyle, final BluetoothDevice bluetoothDevice) {
                XlogUtils.xLog(EBodyProtocol.access$000(), "-----onConnecting-----" + bluetoothDevice.getName() + "--ScaleAddress=" + bluetoothDevice.getAddress());
                XlogUtils.xLog(EBodyProtocol.access$000(), "Body fat scale connection status:     Connecting \n ScaleName = " + bluetoothDevice.getName() + "--ScaleAddress=" + bluetoothDevice.getAddress());
            }
            
            public void onDisConnected(final ProductStyle productStyle) {
                XlogUtils.xLog(EBodyProtocol.access$000(), "-----onDisConnected-----");
                XlogUtils.xLog(EBodyProtocol.access$000(), "Body fat scale connection status:     DisConnected");
                EBodyProtocol.this.onConnectStateListener.onConnectionState(ConnectState.Disconnect);
            }
        };
        this.mScaleMeasureCallback = (IScaleMeasureCallback)new IScaleMeasureCallback() {
            public void onScaleWake() {
                XlogUtils.xLog(EBodyProtocol.access$000(), "-----onScaleWake-----Body fat scale wake up");
                EBodyProtocol.this.onConnectStateListener.onConnectionState(ConnectState.ScaleWake);
            }
            
            public void onScaleSleep() {
                XlogUtils.xLog(EBodyProtocol.access$000(), "-----onScaleSleep-----Body fat scales off screen");
                EBodyProtocol.this.onConnectStateListener.onConnectionState(ConnectState.ScaleSleep);
                EBodyProtocol.this.requestOfflineData();
            }
            
            public void onReceiveMeasureResult(final ScaleMeasureResult obj) {
                XlogUtils.xLog(EBodyProtocol.access$000(), "onReceiveMeasureResult-result=" + obj);
                XlogUtils.xLog(EBodyProtocol.access$000(), "-----onReceiveMeasureResult---Received weight data");
                final EBodyMeasureData eBodyMeasureData;
                if ((eBodyMeasureData = EBodyProtocol.this.toEBodyMeasureData(obj)) != null) {
                    EBodyProtocol.this.onDataResponseListener.onResponseMeasureResult2(eBodyMeasureData, obj.getResistance());
                }
            }
            
            public void onWeightOverLoad() {
                XlogUtils.xLog(EBodyProtocol.access$000(), "-----onWeightOverLoad-----Body fat scale overweight warning");
            }
            
            public void onReceiveHistoryRecord(final OfflineMeasureResult obj) {
                XlogUtils.xLog(EBodyProtocol.access$000(), "---\u79bb\u7ebf\u6570\u636e--onReceiveHistoryRecord-result = " + obj);
                XlogUtils.xLog(EBodyProtocol.access$000(), "-----onReceiveHistoryRecord-----Body fat scales receive historical data");
                final EBodyMeasureData eBodyMeasureData;
                if ((eBodyMeasureData = EBodyProtocol.this.toEBodyMeasureData(obj)) != null) {
                    EBodyProtocol.this.onDataResponseListener.onResponseMeasureResult2(eBodyMeasureData, obj.getResistance());
                }
            }
            
            public void onHistoryDownloadDone() {
                XlogUtils.xLog(EBodyProtocol.access$000(), "---\u79bb\u7ebf\u6570\u636e--onHistoryDownloadDone");
            }
            
            public void onLowPower() {
                XlogUtils.xLog(EBodyProtocol.access$000(), "-----onLowPower-----Low battery indicator on body fat scale");
            }
            
            public void setUserInfoSuccess() {
                XlogUtils.xLog(EBodyProtocol.access$000(), "-----setUserInfoSuccess-----User information set successfully");
                EBodyProtocol.this.onDataResponseListener.onUserInfoUpdateSuccess();
            }
            
            public void receiveTime(final long n) {
                XlogUtils.xLog(EBodyProtocol.access$000(), "-----receiveTime-----The time of receipt of the scale.---time = " + TimeUtils.formatTime1(n));
            }
        };
        if (b) {
            return;
        }
        EbelterSdkManager.init(activity.getBaseContext());
        EbelterSdkManager.outLogFalg = true;
        (this.mScaleManager = new ScaleManager(activity.getBaseContext())).addBluetoothStationListener(this.mBlueStationListener);
        this.mScaleManager.setConnectStationCallback(this.mConnectStationCallback);
        this.mScaleManager.setScaleMeasureCallback(this.mScaleMeasureCallback);
        if (this.myBluetooth == null) {
            this.myBluetooth = MyBluetoothLE.getInstance((Context)activity, b2, 600);
        }
    }
    
    public static Uri getLogZip(final String s) {
        return XlogUtils.getLogZip(s);
    }
    
    public static void sendSupportMail(final String s, final String s2, final String s3) {
        XlogUtils.sendSupportMail(s, s2, s3);
    }
    
    public static /* synthetic */ String access$000() {
        return EBodyProtocol.TAG;
    }
    
    public void setOnConnectStateListener(final OnConnectStateListener onConnectStateListener) {
        this.onConnectStateListener = onConnectStateListener;
    }
    
    public void setOnDataResponseListener(final OnDataResponseListener onDataResponseListener) {
        this.onDataResponseListener = onDataResponseListener;
    }
    
    public String getSDKVersion() {
        return Global.sdkVersion;
    }
    
    public final EBodyMeasureData toEBodyMeasureData(final ScaleMeasureResult scaleMeasureResult) {
        if (!scaleMeasureResult.isOnlyWeightData()) {
            final EBodyMeasureData eBodyMeasureData = new com.microlife_sdk.model.data.EBodyMeasureData();
            final EBodyMeasureData eBodyMeasureData13;
            final EBodyMeasureData eBodyMeasureData12;
            final EBodyMeasureData eBodyMeasureData11;
            final EBodyMeasureData eBodyMeasureData10;
            final EBodyMeasureData eBodyMeasureData9;
            final EBodyMeasureData eBodyMeasureData8;
            final EBodyMeasureData eBodyMeasureData7;
            final EBodyMeasureData eBodyMeasureData6;
            final EBodyMeasureData eBodyMeasureData5;
            final EBodyMeasureData eBodyMeasureData4;
            final EBodyMeasureData eBodyMeasureData3;
            final EBodyMeasureData eBodyMeasureData2 = eBodyMeasureData3 = (eBodyMeasureData4 = (eBodyMeasureData5 = (eBodyMeasureData6 = (eBodyMeasureData7 = (eBodyMeasureData8 = (eBodyMeasureData9 = (eBodyMeasureData10 = (eBodyMeasureData11 = (eBodyMeasureData12 = (eBodyMeasureData13 = eBodyMeasureData))))))))));
            new EBodyMeasureData();
            final String[] split = scaleMeasureResult.getMeasureTime().split(" ");
            final String[] split2 = split[0].split("-");
            final String[] split3;
            final String[] array = split3 = split[1].split(":");
            final EBodyMeasureData eBodyMeasureData14 = eBodyMeasureData2;
            final String[] array2 = split3;
            final EBodyMeasureData eBodyMeasureData15 = eBodyMeasureData2;
            final String[] array3 = split3;
            final EBodyMeasureData eBodyMeasureData16 = eBodyMeasureData2;
            final String[] array4 = split2;
            final EBodyMeasureData eBodyMeasureData17 = eBodyMeasureData2;
            final String[] array5 = split2;
            eBodyMeasureData2.setYear(Integer.parseInt(split2[0]));
            eBodyMeasureData17.setMonth(Integer.parseInt(array5[1]));
            eBodyMeasureData16.setDay(Integer.parseInt(array4[2]));
            eBodyMeasureData15.setHour(Integer.parseInt(array3[0]));
            eBodyMeasureData14.setMinute(Integer.parseInt(array2[1]));
            eBodyMeasureData3.setSecond(Integer.parseInt(array[2]));
            eBodyMeasureData4.setAge(scaleMeasureResult.getAge());
            eBodyMeasureData5.setGender(scaleMeasureResult.getSex());
            eBodyMeasureData6.setUnit((scaleMeasureResult.getWeightUnit().equals("kg") ^ true) ? 1 : 0);
            eBodyMeasureData7.setFat(scaleMeasureResult.getFat());
            eBodyMeasureData8.setWeight(scaleMeasureResult.getWeight());
            eBodyMeasureData9.setWater(scaleMeasureResult.getWaterRate());
            eBodyMeasureData10.setVisceraFat(scaleMeasureResult.getVisceralFat());
            eBodyMeasureData11.setMuscle(scaleMeasureResult.getMuscleVolume());
            eBodyMeasureData12.setBone(scaleMeasureResult.getBoneVolume());
            eBodyMeasureData13.setKcal(scaleMeasureResult.getBmr());
            eBodyMeasureData.setBmi(scaleMeasureResult.getBmi());
            return eBodyMeasureData;
        }
        return null;
    }
    
    public final EBodyMeasureData toEBodyMeasureData(final OfflineMeasureResult offlineMeasureResult) {
        if (!offlineMeasureResult.isSuspectedData()) {
            final EBodyMeasureData eBodyMeasureData = new com.microlife_sdk.model.data.EBodyMeasureData();
            final EBodyMeasureData eBodyMeasureData13;
            final EBodyMeasureData eBodyMeasureData12;
            final EBodyMeasureData eBodyMeasureData11;
            final EBodyMeasureData eBodyMeasureData10;
            final EBodyMeasureData eBodyMeasureData9;
            final EBodyMeasureData eBodyMeasureData8;
            final EBodyMeasureData eBodyMeasureData7;
            final EBodyMeasureData eBodyMeasureData6;
            final EBodyMeasureData eBodyMeasureData5;
            final EBodyMeasureData eBodyMeasureData4;
            final EBodyMeasureData eBodyMeasureData3;
            final EBodyMeasureData eBodyMeasureData2 = eBodyMeasureData3 = (eBodyMeasureData4 = (eBodyMeasureData5 = (eBodyMeasureData6 = (eBodyMeasureData7 = (eBodyMeasureData8 = (eBodyMeasureData9 = (eBodyMeasureData10 = (eBodyMeasureData11 = (eBodyMeasureData12 = (eBodyMeasureData13 = eBodyMeasureData))))))))));
            new EBodyMeasureData();
            final String[] split = offlineMeasureResult.getMeasureTime().split(" ");
            final String[] split2 = split[0].split("-");
            final String[] split3;
            final String[] array = split3 = split[1].split(":");
            final EBodyMeasureData eBodyMeasureData14 = eBodyMeasureData2;
            final String[] array2 = split3;
            final EBodyMeasureData eBodyMeasureData15 = eBodyMeasureData2;
            final String[] array3 = split3;
            final EBodyMeasureData eBodyMeasureData16 = eBodyMeasureData2;
            final String[] array4 = split2;
            final EBodyMeasureData eBodyMeasureData17 = eBodyMeasureData2;
            final String[] array5 = split2;
            eBodyMeasureData2.setYear(Integer.parseInt(split2[0]));
            eBodyMeasureData17.setMonth(Integer.parseInt(array5[1]));
            eBodyMeasureData16.setDay(Integer.parseInt(array4[2]));
            eBodyMeasureData15.setHour(Integer.parseInt(array3[0]));
            eBodyMeasureData14.setMinute(Integer.parseInt(array2[1]));
            eBodyMeasureData3.setSecond(Integer.parseInt(array[2]));
            eBodyMeasureData4.setAge(offlineMeasureResult.getAge());
            eBodyMeasureData5.setGender(offlineMeasureResult.getSex());
            eBodyMeasureData6.setUnit((offlineMeasureResult.getWeightUnit().equals("kg") ^ true) ? 1 : 0);
            eBodyMeasureData7.setFat(offlineMeasureResult.getFat());
            eBodyMeasureData8.setWeight(offlineMeasureResult.getWeight());
            eBodyMeasureData9.setWater(offlineMeasureResult.getWaterRate());
            eBodyMeasureData10.setVisceraFat(offlineMeasureResult.getVisceralFat());
            eBodyMeasureData11.setMuscle(offlineMeasureResult.getMuscleVolume());
            eBodyMeasureData12.setBone(offlineMeasureResult.getBoneVolume());
            eBodyMeasureData13.setKcal(offlineMeasureResult.getBmr());
            eBodyMeasureData.setBmi(offlineMeasureResult.getBmi());
            return eBodyMeasureData;
        }
        return null;
    }
    
    public boolean isSupportBluetooth(final Activity activity) {
        return this.isSimulationMode || this.myBluetooth.isSupportBluetooth(activity);
    }
    
    public boolean isEnableBt() {
        return this.isSimulationMode || this.myBluetooth.isBTEnabled();
    }
    
    public boolean isScanning() {
        return this.isSimulationMode || this.mScaleManager.isWorkFlag();
    }
    
    public boolean isConnected() {
        return this.isSimulationMode || this.mScaleManager.isConnected();
    }
    
    public void startScan() {
        this.mScaleManager.startWork();
    }
    
    public void stopScan() {
        this.mScaleManager.stopWork();
    }
    
    public void connect(final BluetoothDevice bluetoothDevice) {
        this.mScaleManager.setMustConnectBlueAddress(bluetoothDevice.getAddress());
    }
    
    public void unbindDevice() {
        this.mScaleManager.setMustConnectBlueAddress((String)null);
    }
    
    public void disconnect() {
        final ScaleManager mScaleManager;
        if ((mScaleManager = this.mScaleManager) != null) {
            mScaleManager.removeBluetoothStationListener(this.mBlueStationListener);
            this.mScaleManager.exit();
            this.mScaleManager = null;
        }
    }
    
    public String sendUserInfoToScale(final String s, final String userId, final Integer n, final Integer n2, final float weight, final Integer n3, final float n4, final int roleType) {
        final ScaleUserInfo scaleUserInfo = ScaleUserInfo.getScaleUserInfo();
        final ScaleUserInfo scaleUserInfo6;
        final ScaleUserInfo scaleUserInfo5;
        final ScaleUserInfo scaleUserInfo4;
        final ScaleUserInfo scaleUserInfo3;
        final ScaleUserInfo scaleUserInfo2 = scaleUserInfo3 = (scaleUserInfo4 = (scaleUserInfo5 = (scaleUserInfo6 = scaleUserInfo)));
        scaleUserInfo2.setUserId(userId);
        scaleUserInfo3.setAge((int)n2);
        scaleUserInfo4.setHeight((int)n3);
        scaleUserInfo5.setRoleType(roleType);
        scaleUserInfo6.setAge((int)n);
        scaleUserInfo.setWeight(weight);
        this.mScaleManager.updateUserInfo(scaleUserInfo2);
        return userId;
    }
    
    public void sendDelAllUser() {
        final ScaleManager mScaleManager;
        if ((mScaleManager = this.mScaleManager) != null && mScaleManager.isConnected()) {
            this.mScaleManager.deleteAllUserInfo();
        }
    }
    
    public void requestOfflineData() {
        final ScaleUserInfo scaleUserInfo = ScaleUserInfo.getScaleUserInfo();
        final ScaleManager mScaleManager;
        if ((mScaleManager = this.mScaleManager) != null && mScaleManager.isConnected()) {
            this.mScaleManager.requestOffMesureData(scaleUserInfo.getUserId(), scaleUserInfo.getHeight(), scaleUserInfo.getSex(), scaleUserInfo.getAge(), scaleUserInfo.getRoleType());
        }
    }
    
    public enum ConnectState
    {
        ScanFinish, 
        Connected, 
        Disconnect, 
        ConnectTimeout, 
        ScaleWake, 
        ScaleSleep;
    }
    
    public interface OnDataResponseListener
    {
        void onUserInfoUpdateSuccess();
        
        void onDeleteAllUsersSuccess();
        
        void onResponseMeasureResult2(final EBodyMeasureData p0, final float p1);
    }
    
    public interface OnConnectStateListener
    {
        void onScanResult(final BluetoothDevice p0);
        
        void onConnectionState(final ConnectState p0);
    }
}
