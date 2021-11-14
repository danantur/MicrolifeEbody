// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.protocol;

import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;
import com.microlife_sdk.model.data.ThermoMeasureData;
import com.microlife_sdk.model.data.EBodyMeasureData;
import com.microlife_sdk.model.data.DeviceInfo;
import com.microlife_sdk.model.data.CurrentAndMData;
import com.microlife_sdk.model.data.VersionData;
import com.microlife_sdk.model.data.User;
import com.microlife_sdk.model.data.DRecord;
import java.util.Iterator;
import android.net.Uri;
import java.util.ArrayList;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.Context;
import com.microlife_sdk.model.XlogUtils;
import android.content.BroadcastReceiver;
import java.util.List;
import android.app.Activity;
import com.microlife_sdk.model.bluetooth.MyBluetoothLE;
import com.microlife_sdk.model.bluetooth.BluetoothLEClass;
import com.microlife_sdk.model.abcdef.*;

public class BaseProtocol implements BPMProtocol.OnDataResponseListener, ThermoProtocol.OnDataResponseListener, EBodyProtocol.OnConnectStateListener, EBodyProtocol.OnDataResponseListener, BluetoothLEClass.OnIMBluetoothLEListener, MyBluetoothLE.OnWriteStateListener
{
    public static final String TAG;
    public static BaseProtocol protocol;
    public MyBluetoothLE myBluetooth;
    public Activity myAty;
    public String bondMacAddress;
    public List<String> targetBPMNames;
    public List<String> targetThermoNames;
    public ConnectState mConnectState;
    public DeviceType deviceType;
    public MyBluetoothLE.OnWriteStateListener mOnWriteStateListener;
    public OnNotifyStateListener mOnNotifyStateListener;
    public OnConnectStateListener onConnectStateListener;
    public OnDataResponseListener onDataResponseListener;
    public BroadcastReceiver bondedBTReceiver;
    
    public static BaseProtocol getInstance(final Activity activity, final boolean b, final boolean b2, final String s) {
        XlogUtils.initXlog(activity, b2);
        if (BaseProtocol.protocol == null) {
            BaseProtocol.protocol = new BaseProtocol(activity, b, b2, s);
        }
        return BaseProtocol.protocol;
    }
    
    public BaseProtocol(final Activity activity, final boolean b, final boolean b2, final String s) {
        this.bondMacAddress = "";
        this.bondedBTReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                switch (((BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getBondState()) {
                    case 12: {
                        XlogUtils.xLog(BaseProtocol.access$000(), "bondedBTReceiver bonded...");
                        final BaseProtocol a = BaseProtocol.this;
                        a.connect(BaseProtocol.access$100(a), BaseProtocol.this.deviceType);
                        BaseProtocol.access$200(BaseProtocol.this).unregisterReceiver(BaseProtocol.this.bondedBTReceiver);
                        break;
                    }
                    case 11: {
                        XlogUtils.xLog(BaseProtocol.access$000(), "bondedBTReceiver bonding...");
                        break;
                    }
                    case 10: {
                        XlogUtils.xLog(BaseProtocol.access$000(), "bondedBTReceiver can't bond...");
                        break;
                    }
                }
            }
        };
        this.myAty = activity;
        (this.targetBPMNames = new ArrayList<String>()).add("A6 BT");
        this.targetBPMNames.add("A6 BASIS PLUS BT");
        this.targetBPMNames.add("A7 Touch BT");
        this.targetBPMNames.add("B3 BT");
        this.targetBPMNames.add("B6 Connect");
        this.targetBPMNames.add("A6");
        this.targetBPMNames.add("B6");
        this.targetBPMNames.add("Progress");
        (this.targetThermoNames = new ArrayList<String>()).add("3MW1");
        this.targetThermoNames.add("NC150 BT");
        if (this.myBluetooth == null) {
            this.myBluetooth = MyBluetoothLE.getInstance((Context)activity, b2, 600);
            this.myAty = activity;
        }
        final MyBluetoothLE myBluetooth;
        if ((myBluetooth = this.myBluetooth) != null) {
            myBluetooth.setOnIMBluetoothLEListener(this);
            this.myBluetooth.setOnWriteStateListener(this);
        }
        Global.bpmProtocol = BPMProtocol.getInstance(this.myBluetooth, b, b2, s);
        (Global.eBodyProtocol = EBodyProtocol.getInstanceForBase(activity, b, b2, s)).setOnConnectStateListener(this);
        Global.thermoProtocol = new ThermoProtocol(this.myBluetooth, b, b2);
    }
    
    public static Uri getLogZip(final String s) {
        return XlogUtils.getLogZip(s);
    }
    
    public static void sendSupportMail(final String s, final String s2, final String s3) {
        XlogUtils.sendSupportMail(s, s2, s3);
    }
    
    public static /* synthetic */ String access$000() {
        return BaseProtocol.TAG;
    }
    
    public static /* synthetic */ String access$100(final BaseProtocol baseProtocol) {
        return baseProtocol.bondMacAddress;
    }
    
    public static /* synthetic */ Activity access$200(final BaseProtocol baseProtocol) {
        return baseProtocol.myAty;
    }
    
    static {
        TAG = BPMProtocol.class.getSimpleName();
    }
    
    public void setOnWriteStateListener(final MyBluetoothLE.OnWriteStateListener mOnWriteStateListener) {
        this.mOnWriteStateListener = mOnWriteStateListener;
    }
    
    public void setOnNotifyStateListener(final OnNotifyStateListener mOnNotifyStateListener) {
        this.mOnNotifyStateListener = mOnNotifyStateListener;
    }
    
    public void setOnConnectStateListener(final OnConnectStateListener onConnectStateListener) {
        this.onConnectStateListener = onConnectStateListener;
    }
    
    public void setOnDataResponseListener(final OnDataResponseListener onDataResponseListener) {
        this.onDataResponseListener = onDataResponseListener;
    }
    
    @Override
    public void onWriteMessage(final boolean b, final String s) {
    }
    
    @Override
    public void onBtStateChanged(final boolean b) {
        this.onConnectStateListener.onBtStateChanged(b);
    }
    
    @Override
    public void scanResult(final String s, final String s2, final int n, final byte[] array) {
        final Iterator<String> iterator = this.targetBPMNames.iterator();
        while (iterator.hasNext()) {
            if (s2.startsWith(iterator.next())) {
                this.onConnectStateListener.onScanResult(s, s2, n, DeviceType.DEVICE_TYPE_BPM);
            }
        }
        final Iterator<String> iterator2 = this.targetThermoNames.iterator();
        while (iterator2.hasNext()) {
            if (s2.startsWith(iterator2.next())) {
                this.onConnectStateListener.onScanResult(s, s2, n, DeviceType.DEVICE_TYPE_THERMO);
            }
        }
    }
    
    @Override
    public void connectionStatus(final int n) {
        if (n != 4) {
            if (n != 20) {
                if (n != 17) {
                    if (n == 18) {
                        this.mConnectState = ConnectState.Disconnect;
                    }
                }
                else {
                    this.mConnectState = ConnectState.Connected;
                    if (this.deviceType == DeviceType.DEVICE_TYPE_BPM) {
                        this.readUserAndVersionData();
                    }
                }
            }
            else {
                this.mConnectState = ConnectState.ConnectTimeout;
            }
        }
        else {
            this.mConnectState = ConnectState.ScanFinish;
        }
        this.onConnectStateListener.onConnectionState(this.mConnectState, this.deviceType);
    }
    
    @Override
    public void dataResult(final String s) {
        final int n;
        if ((n = this.deviceType.ordinal()) != 1) {
            if (n == 2) {
                Global.thermoProtocol.dataResult(s);
            }
        }
        else {
            Global.bpmProtocol.dataResult(s);
        }
    }
    
    @Override
    public void onResponseReadHistory(final DRecord dRecord) {
        this.onDataResponseListener.onResponseReadHistory(dRecord);
    }
    
    @Override
    public void onResponseClearHistory(final boolean b) {
        this.onDataResponseListener.onResponseClearHistory(b);
    }
    
    @Override
    public void onResponseReadUserAndVersionData(final User user, final VersionData versionData) {
        this.onDataResponseListener.onResponseReadUserAndVersionData(user, versionData);
    }
    
    @Override
    public void onResponseWriteUser(final boolean b) {
        this.onDataResponseListener.onResponseWriteUser(b);
    }
    
    @Override
    public void onResponseReadLastData(final CurrentAndMData currentAndMData, final int n, final int n2, final int n3, final boolean b) {
        this.onDataResponseListener.onResponseReadLastData(currentAndMData, n, n2, n3, b);
    }
    
    @Override
    public void onResponseClearLastData(final boolean b) {
        this.onDataResponseListener.onResponseClearLastData(b);
    }
    
    @Override
    public void onResponseReadDeviceInfo(final DeviceInfo deviceInfo) {
        this.onDataResponseListener.onResponseReadDeviceInfo(deviceInfo);
    }
    
    @Override
    public void onResponseReadDeviceTime(final DeviceInfo deviceInfo) {
        this.onDataResponseListener.onResponseReadDeviceTime(deviceInfo);
    }
    
    @Override
    public void onResponseWriteDeviceTime(final boolean b) {
        this.onDataResponseListener.onResponseWriteDeviceTime(b);
    }
    
    @Override
    public void onScanResult(final BluetoothDevice bluetoothDevice) {
        this.onConnectStateListener.onScanResult(bluetoothDevice, DeviceType.DEVICE_TYPE_EBODY);
    }
    
    @Override
    public void onConnectionState(final EBodyProtocol.ConnectState connectState) {
        switch (connectState.ordinal()) {
            case 5: {
                this.mConnectState = ConnectState.ScanFinish;
                break;
            }
            case 4: {
                this.mConnectState = ConnectState.ScaleSleep;
                break;
            }
            case 3: {
                this.mConnectState = ConnectState.ScaleWake;
                break;
            }
            case 2: {
                this.mConnectState = ConnectState.Disconnect;
                break;
            }
            case 1: {
                this.mConnectState = ConnectState.Connected;
                break;
            }
        }
        this.onConnectStateListener.onConnectionState(this.mConnectState, DeviceType.DEVICE_TYPE_EBODY);
    }
    
    @Override
    public void onUserInfoUpdateSuccess() {
        this.onDataResponseListener.onUserInfoUpdateSuccess();
    }
    
    @Override
    public void onDeleteAllUsersSuccess() {
        this.onDataResponseListener.onDeleteAllUsersSuccess();
    }
    
    @Override
    public void onResponseMeasureResult2(final EBodyMeasureData eBodyMeasureData, final float n) {
        this.onDataResponseListener.onResponseMeasureResult2(eBodyMeasureData, n);
    }
    
    @Override
    public void onResponseDeviceInfo(final String s, final int n, final float n2) {
        this.onDataResponseListener.onResponseDeviceInfo(s, n, n2);
    }
    
    @Override
    public void onResponseUploadMeasureData(final ThermoMeasureData thermoMeasureData) {
        this.onDataResponseListener.onResponseUploadMeasureData(thermoMeasureData);
    }
    
    public boolean isSupportBluetooth(final DeviceType deviceType) {
        final boolean b = false;
        boolean b2 = false;
        switch (deviceType.ordinal()) {
            default: {
                b2 = b;
                break;
            }
            case 4: {
                b2 = Global.eBodyProtocol.isSupportBluetooth(this.myAty);
                break;
            }
            case 3: {
                b2 = (Global.bpmProtocol.isSupportBluetooth(this.myAty) || Global.eBodyProtocol.isSupportBluetooth(this.myAty) || Global.thermoProtocol.isSupportBluetooth(this.myAty));
                break;
            }
            case 2: {
                b2 = Global.thermoProtocol.isSupportBluetooth(this.myAty);
                break;
            }
            case 1: {
                b2 = Global.bpmProtocol.isSupportBluetooth(this.myAty);
                break;
            }
        }
        return b2;
    }
    
    public boolean isEnableBt(final DeviceType deviceType) {
        boolean b = false;
        switch (deviceType.ordinal()) {
            case 4: {
                b = Global.eBodyProtocol.isEnableBt();
                break;
            }
            case 3: {
                b = (Global.bpmProtocol.isEnableBt() || Global.eBodyProtocol.isEnableBt() || Global.thermoProtocol.isEnableBt());
                break;
            }
            case 2: {
                b = Global.thermoProtocol.isEnableBt();
                break;
            }
            case 1: {
                b = Global.bpmProtocol.isEnableBt();
                break;
            }
        }
        return b;
    }
    
    public boolean isScanning(final DeviceType deviceType) {
        boolean b = false;
        switch (deviceType.ordinal()) {
            case 4: {
                b = Global.eBodyProtocol.isScanning();
                break;
            }
            case 3: {
                b = (Global.bpmProtocol.isScanning() || Global.eBodyProtocol.isScanning() || Global.thermoProtocol.isScanning());
                break;
            }
            case 2: {
                b = Global.thermoProtocol.isScanning();
                break;
            }
            case 1: {
                b = Global.bpmProtocol.isScanning();
                break;
            }
        }
        return b;
    }
    
    public boolean isConnected(final DeviceType deviceType) {
        boolean b = false;
        switch (deviceType.ordinal()) {
            case 4: {
                b = Global.eBodyProtocol.isConnected();
                break;
            }
            case 3: {
                b = (Global.bpmProtocol.isConnected() || Global.eBodyProtocol.isConnected() || Global.thermoProtocol.isConnected());
                break;
            }
            case 2: {
                b = Global.thermoProtocol.isConnected();
                break;
            }
            case 1: {
                b = Global.bpmProtocol.isConnected();
                break;
            }
        }
        return b;
    }
    
    public void startScan(final int n, final DeviceType deviceType) {
        this.deviceType = deviceType;
        switch (deviceType.ordinal()) {
            case 4: {
                Global.eBodyProtocol.startScan();
                break;
            }
            case 3: {
                Global.eBodyProtocol.startScan();
                this.myBluetooth.startLEScan(n, false);
                break;
            }
            case 1:
            case 2: {
                this.myBluetooth.startLEScan(n, false);
                break;
            }
        }
    }
    
    public void stopScan(final DeviceType deviceType) {
        this.deviceType = deviceType;
        switch (deviceType.ordinal()) {
            case 4: {
                Global.eBodyProtocol.stopScan();
                break;
            }
            case 3: {
                Global.eBodyProtocol.stopScan();
                this.myBluetooth.stopLEScan();
                break;
            }
            case 1:
            case 2: {
                this.myBluetooth.stopLEScan();
                break;
            }
        }
    }
    
    public void bond(final String bondMacAddress, final DeviceType deviceType) {
        this.myAty.registerReceiver(this.bondedBTReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
        final BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(bondMacAddress);
        try {
            if (remoteDevice.getBondState() == 10) {
                final BluetoothDevice bluetoothDevice = remoteDevice;
                XlogUtils.xLog(BaseProtocol.TAG, "bondedBTReceiver BOND_NONE");
                this.bondMacAddress = bondMacAddress;
                bluetoothDevice.createBond();
            }
            else {
                XlogUtils.xLog(BaseProtocol.TAG, "bondedBTReceiver BOND..");
                this.myAty.unregisterReceiver(this.bondedBTReceiver);
                this.connect(bondMacAddress, deviceType);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            XlogUtils.xLog(BaseProtocol.TAG, "Exception Bond\uff1a" + ex.getMessage());
        }
    }
    
    public void connect(final String s, final DeviceType deviceType) {
        this.deviceType = deviceType;
        final int n;
        if ((n = deviceType.ordinal()) != 1) {
            if (n == 2) {
                Global.thermoProtocol.setOnDataResponseListener(this);
                Global.thermoProtocol.connect(s);
            }
        }
        else {
            Global.bpmProtocol.setOnDataResponseListener(this);
            Global.bpmProtocol.connect(s);
        }
    }
    
    public void connect(final BluetoothDevice bluetoothDevice, final DeviceType deviceType) {
        this.deviceType = deviceType;
        if (deviceType.ordinal() == 4) {
            Global.eBodyProtocol.setOnDataResponseListener(this);
            Global.eBodyProtocol.connect(bluetoothDevice);
        }
    }
    
    public void unbindDevice(final DeviceType deviceType) {
        if (deviceType.ordinal() == 4) {
            Global.eBodyProtocol.unbindDevice();
        }
    }
    
    public void disconnect(final DeviceType deviceType) {
        switch (deviceType.ordinal()) {
            case 4: {
                if (Global.eBodyProtocol.isConnected()) {
                    Global.eBodyProtocol.disconnect();
                    break;
                }
                break;
            }
            case 3: {
                if (Global.eBodyProtocol.isConnected()) {
                    Global.eBodyProtocol.disconnect();
                }
                if (Global.bpmProtocol.isConnected()) {
                    Global.bpmProtocol.disconnect();
                }
                if (Global.thermoProtocol.isConnected()) {
                    Global.thermoProtocol.disconnect();
                    break;
                }
                break;
            }
            case 2: {
                if (Global.thermoProtocol.isConnected()) {
                    Global.thermoProtocol.disconnect();
                    break;
                }
                break;
            }
            case 1: {
                if (Global.bpmProtocol.isConnected()) {
                    Global.bpmProtocol.disconnect();
                    break;
                }
                break;
            }
        }
    }
    
    public String getSDKVersion() {
        return Global.sdkVersion;
    }
    
    public void readHistorysOrCurrDataAndSyncTiming() {
        Global.bpmProtocol.readHistorysOrCurrDataAndSyncTiming();
    }
    
    public void clearAllHistorys() {
        Global.bpmProtocol.clearAllHistorys();
    }
    
    public void disconnectBPM() {
        Global.bpmProtocol.disconnectBPM();
    }
    
    public void readUserAndVersionData() {
        Global.bpmProtocol.readUserAndVersionData();
    }
    
    public void writeUserData(final String s, final int n) {
        Global.bpmProtocol.writeUserData(s, n);
    }
    
    public void readLastData() {
        Global.bpmProtocol.readLastData();
    }
    
    public void clearLastData() {
        Global.bpmProtocol.clearLastData();
    }
    
    public void readDeviceInfo() {
        Global.bpmProtocol.readDeviceInfo();
    }
    
    public void readDeviceTime() {
        Global.bpmProtocol.readDeviceTime();
    }
    
    public void syncTiming() {
        Global.bpmProtocol.syncTiming();
    }
    
    public String sendUserInfoToScale(final String s, final String s2, final Integer n, final Integer n2, final float n3, final Integer n4, final float n5, final int n6) {
        return Global.eBodyProtocol.sendUserInfoToScale(s, s2, n, n2, n3, n4, n5, n6);
    }
    
    public void sendDelAllUser() {
        Global.eBodyProtocol.sendDelAllUser();
    }
    
    public void requestOfflineData() {
        Global.eBodyProtocol.requestOfflineData();
    }
    
    public enum DeviceType
    {
        DEVICE_TYPE_ALL, 
        DEVICE_TYPE_BPM, 
        DEVICE_TYPE_EBODY, 
        DEVICE_TYPE_THERMO;
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
        void onResponseReadHistory(final DRecord p0);
        
        void onResponseClearHistory(final boolean p0);
        
        void onResponseReadUserAndVersionData(final User p0, final VersionData p1);
        
        void onResponseWriteUser(final boolean p0);
        
        void onResponseReadLastData(final CurrentAndMData p0, final int p1, final int p2, final int p3, final boolean p4);
        
        void onResponseClearLastData(final boolean p0);
        
        void onResponseReadDeviceInfo(final DeviceInfo p0);
        
        void onResponseReadDeviceTime(final DeviceInfo p0);
        
        void onResponseWriteDeviceTime(final boolean p0);
        
        void onUserInfoUpdateSuccess();
        
        void onDeleteAllUsersSuccess();
        
        void onResponseMeasureResult2(final EBodyMeasureData p0, final float p1);
        
        void onResponseDeviceInfo(final String p0, final int p1, final float p2);
        
        void onResponseUploadMeasureData(final ThermoMeasureData p0);
    }
    
    public interface OnConnectStateListener
    {
        void onBtStateChanged(final boolean p0);
        
        void onScanResult(final String p0, final String p1, final int p2, final DeviceType p3);
        
        void onScanResult(final BluetoothDevice p0, final DeviceType p1);
        
        void onConnectionState(final ConnectState p0, final DeviceType p1);
    }
    
    public interface OnNotifyStateListener
    {
        void onNotifyMessage(final String p0);
    }
}
