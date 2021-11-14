// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.protocol;

import java.util.Locale;
import java.util.Calendar;
import java.util.Iterator;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;

import com.microlife_sdk.model.abcdef.e;
import com.microlife_sdk.model.data.CBPdataAndCalCBP;
import com.microlife_sdk.model.data.DRecord;
import com.microlife_sdk.ideabuslibrary.util.BaseUtils;
import com.microlife_sdk.model.data.VersionData;
import com.microlife_sdk.model.data.User;
import com.microlife_sdk.model.data.SettingValues;
import com.microlife_sdk.model.data.DeviceInfo;
import com.microlife_sdk.model.data.FunctionSettingValues;
import android.net.Uri;
import java.util.ArrayList;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.Context;
import android.os.Message;
import com.microlife_sdk.model.XlogUtils;
import java.util.TimerTask;
import android.content.BroadcastReceiver;
import android.os.Handler;
import java.util.List;
import android.app.Activity;
import java.util.Timer;
import com.microlife_sdk.model.bluetooth.MyBluetoothLE;
import com.microlife_sdk.model.bluetooth.BluetoothLEClass;

public class WBO3Protocol implements BluetoothLEClass.OnIMBluetoothLEListener, MyBluetoothLE.OnWriteStateListener
{
    public static final String TAG = "WBO3Protocol";
    public static WBO3Protocol protocol;
    public static final int RECEIVED_ERROR_COUNT = 15000;
    public static final String CMD_REPLY_RESULT_NACK = "91";
    public static final String SPACE_ASSCII = "00";
    public MyBluetoothLE myBluetooth;
    public int receiveErrorCount;
    public StringBuilder allReceivedCommand;
    public boolean isSimulationMode;
    public int position;
    public Timer simulationTimer;
    public int origin;
    public String bondMacAddress;
    public Activity myAty;
    public String oldCom;
    public List<String> targetDeviceNames;
    public ConnectState mConnectState;
    public Handler mHandler;
    public MyBluetoothLE.OnWriteStateListener mOnWriteStateListener;
    public OnNotifyStateListener mOnNotifyStateListener;
    public OnConnectStateListener onConnectStateListener;
    public OnDataResponseListener onDataResponseListener;
    public BroadcastReceiver bondedBTReceiver;
    
    private void startSimulationTimer() {
        this.cancelSimulationTimer();
        this.position = 0;
        (this.simulationTimer = new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                WBO3Protocol.access$100(WBO3Protocol.this).sendEmptyMessage(100);
            }
        }, 100L, 700L);
    }
    
    private void cancelSimulationTimer() {
        final Timer simulationTimer;
        if ((simulationTimer = this.simulationTimer) != null) {
            simulationTimer.cancel();
            this.simulationTimer = null;
        }
    }
    
    public static WBO3Protocol getInstance(final Activity activity, final boolean b, final boolean b2, final String s) {
        XlogUtils.initXlog(activity, b2);
        if (WBO3Protocol.protocol == null) {
            WBO3Protocol.protocol = new WBO3Protocol(activity, b, b2);
        }
        return WBO3Protocol.protocol;
    }
    
    public WBO3Protocol(final Activity myAty, final boolean isSimulationMode, final boolean b) {
        this.receiveErrorCount = 0;
        this.isSimulationMode = false;
        this.position = 0;
        this.origin = 65;
        this.bondMacAddress = "";
        this.oldCom = "";
        this.mHandler = new Handler() {
            public void handleMessage(final Message message) {
                final int what;
                if ((what = message.what) != 0) {
                    if (what != 100) {
                        if (what != 101) {
                            if (what != 1000) {
                                if (what == 1001) {
                                    final OnNotifyStateListener mOnNotifyStateListener;
                                    if ((mOnNotifyStateListener = WBO3Protocol.this.mOnNotifyStateListener) != null) {
                                        mOnNotifyStateListener.onNotifyMessage((String)message.obj);
                                    }
                                }
                            }
                            else {
                                final MyBluetoothLE.OnWriteStateListener mOnWriteStateListener;
                                if ((mOnWriteStateListener = WBO3Protocol.this.mOnWriteStateListener) != null) {
                                    mOnWriteStateListener.onWriteMessage(message.arg1 == 1, (String)message.obj);
                                }
                            }
                        }
                        else {
                            WBO3Protocol.this.connectionStatus(17);
                        }
                    }
                    else {
                        final WBO3Protocol a = WBO3Protocol.this;
                        ++a.position;
                        final OnConnectStateListener onConnectStateListener;
                        if ((onConnectStateListener = a.onConnectStateListener) != null) {
                            onConnectStateListener.onScanResult("abcde12345678" + WBO3Protocol.this.position, "Fuel " + WBO3Protocol.this.position, WBO3Protocol.this.position - 40);
                        }
                        final WBO3Protocol a2;
                        if ((a2 = WBO3Protocol.this).position >= 10) {
                            WBO3Protocol.access$000(a2).cancel();
                            WBO3Protocol.access$002(WBO3Protocol.this, null);
                        }
                    }
                }
                else {
                    WBO3Protocol.this.dataResult((String)message.obj);
                }
            }
        };
        this.bondedBTReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                switch (((BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getBondState()) {
                    case 12: {
                        XlogUtils.xLog(WBO3Protocol.access$200(), "bondedBTReceiver bonded...");
                        WBO3Protocol.access$300(WBO3Protocol.this).unregisterReceiver(WBO3Protocol.this.bondedBTReceiver);
                        final WBO3Protocol a = WBO3Protocol.this;
                        a.connect(WBO3Protocol.access$400(a));
                        break;
                    }
                    case 11: {
                        XlogUtils.xLog(WBO3Protocol.access$200(), "bondedBTReceiver bonding...");
                        break;
                    }
                    case 10: {
                        XlogUtils.xLog(WBO3Protocol.access$200(), "bondedBTReceiver can't bond...");
                        break;
                    }
                }
            }
        };
        this.isSimulationMode = isSimulationMode;
        if (this.isSimulationMode) {
            return;
        }
        (this.targetDeviceNames = new ArrayList<String>()).add("WatchBP O3");
        if (this.myBluetooth == null) {
            this.myBluetooth = MyBluetoothLE.getInstance((Context)myAty, b, 1800);
            this.myAty = myAty;
        }
        final MyBluetoothLE myBluetooth;
        if ((myBluetooth = this.myBluetooth) != null) {
            myBluetooth.setOnIMBluetoothLEListener(this);
            this.myBluetooth.setOnWriteStateListener(this);
        }
        this.allReceivedCommand = new StringBuilder();
    }
    
    public static Uri getLogZip(final String s) {
        return XlogUtils.getLogZip(s);
    }
    
    public static void sendSupportMail(final String s, final String s2, final String s3) {
        XlogUtils.sendSupportMail(s, s2, s3);
    }
    
    private void handleReceived(String str) {
        final String s = str;
        XlogUtils.xLog(WBO3Protocol.TAG, "handleReceived message : " + str);
        final Boolean value;
        if (value = (s.equals(this.oldCom) ^ true)) {
            this.oldCom = str;
        }
        final int cmd = this.getCmd(str);
        final String s2 = str;
        this.origin = Integer.parseInt(str.substring(2, 4), 16);
        final boolean equals = (str = s2.substring(10, s2.length() - 4)).equals("91");
        XlogUtils.xLog(WBO3Protocol.TAG, "CMD : " + cmd + " data : " + str);
        while (true) {
            switch (cmd) {
                default: {
                    this.receiveError(str);
                }
                case 16: {
                    if (equals) {
                        break;
                    }
                    final FunctionSettingValues functionSettingValues = new com.microlife_sdk.model.data.FunctionSettingValues();
                    final FunctionSettingValues functionSettingValues2 = functionSettingValues;
                    final String s3 = str;
                    new FunctionSettingValues();
                    functionSettingValues.importHexString(s3, this.origin);
                    this.onDataResponseListener.onResponseReadFunctionSettingValues(functionSettingValues2);
                    break;
                }
                case 12: {
                    final Boolean b = value;
                    final DeviceInfo deviceInfo;
                    com.microlife_sdk.model.abcdef.d.a(str, deviceInfo = new DeviceInfo());
                    if (b) {
                        this.onDataResponseListener.onResponseReadDeviceTime(deviceInfo);
                    }
                    break;
                }
                case 11: {
                    final Boolean b2 = value;
                    final String s4 = str;
                    final DeviceInfo deviceInfo2 = new com.microlife_sdk.model.data.DeviceInfo();
                    final DeviceInfo deviceInfo3 = deviceInfo2;
                    new DeviceInfo();
                    com.microlife_sdk.model.abcdef.d.a(s4, deviceInfo2, this.origin);
                    if (b2) {
                        this.onDataResponseListener.onResponseReadDeviceInfo(deviceInfo3);
                    }
                    break;
                }
                case 7: {
                    if (equals) {
                        break;
                    }
                    final SettingValues settingValues = new com.microlife_sdk.model.data.SettingValues();
                    final SettingValues settingValues2 = settingValues;
                    final String s5 = str;
                    new SettingValues();
                    settingValues.importHexString(s5, this.origin);
                    this.onDataResponseListener.onResponseReadSettingValues(settingValues2);
                    break;
                }
                case 5: {
                    final Boolean b3 = value;
                    final e e2;
                    final e e = e2 = new e(str);
                    final User user = new com.microlife_sdk.model.data.User();
                    final User user2 = user;
                    final e e3 = e2;
                    new User();
                    user.setID(e3.b(60));
                    final String a = e.a(e.a());
                    final VersionData versionData = new com.microlife_sdk.model.data.VersionData();
                    final VersionData versionData2 = versionData;
                    final String s6 = a;
                    final VersionData versionData3 = versionData2;
                    new VersionData();
                    com.microlife_sdk.model.abcdef.d.b(s6, versionData3);
                    BaseUtils.printLog("d", WBO3Protocol.TAG, versionData.toString());
                    if (b3) {
                        this.writeDeviceTime();
                        this.onDataResponseListener.onResponseReadUserAndVersionData(user2, versionData2);
                    }
                    break;
                }
                case 0: {
                    if (equals) {
                        break;
                    }
                    final Boolean b4 = value;
                    final String s7 = str;
                    final DRecord dRecord = new com.microlife_sdk.model.data.DRecord();
                    final DRecord dRecord2 = dRecord;
                    new DRecord();
                    com.microlife_sdk.model.abcdef.d.a(s7, dRecord, this.origin);
                    if (b4) {
                        this.onDataResponseListener.onResponseReadAllHistorys(dRecord2);
                    }
                    break;
                }
                case 4: {
                    this.receiveErrorCount = 0;
                }
                case 19: {
                    str = new e(str).b(str.length());
                    this.onDataResponseListener.onResponseReadBTModuleName(str);
                    continue;
                }
                case 13: {
                    this.onDataResponseListener.onResponseWriteDeviceTime(equals ^ true);
                    continue;
                }
                case 8: {
                    this.onDataResponseListener.onResponseWriteSettingValues(equals ^ true);
                    continue;
                }
                case 6: {
                    this.onDataResponseListener.onResponseWriteUserID(equals ^ true);
                    continue;
                }
                case 3: {
                    this.onDataResponseListener.onResponseClearHistorys(equals ^ true);
                    continue;
                }
                case 1: {
                    final boolean b5 = equals;
                    final CBPdataAndCalCBP cbPdataAndCalCBP = new CBPdataAndCalCBP();
                    if (!b5) {
                        cbPdataAndCalCBP.importHexString(str, this.origin);
                    }
                    this.onDataResponseListener.onResponseReadCBPData(cbPdataAndCalCBP, equals);
                    continue;
                }
            }
            break;
        }
    }
    
    private boolean isCorrectHeader(final String s) {
        return s.startsWith("4D");
    }
    
    private boolean isCorrectEnd(final String s) {
        s.endsWith("-1");
        return true;
    }
    
    private int getCorrectLength(final String s) {
        int int1 = 0;
        if (s.length() > 8) {
            int1 = Integer.parseInt(s.substring(4, 8), 16);
        }
        return (int1 + 4) * 2;
    }
    
    private int getCmd(final String s) {
        return Integer.parseInt(s.substring(8, 10), 16);
    }
    
    private void receiveError(final String str) {
        XlogUtils.xLog(WBO3Protocol.TAG, "Receive error Count = " + this.receiveErrorCount + " message : " + str);
        ++this.receiveErrorCount;
        int length = this.allReceivedCommand.length();
        if (this.receiveErrorCount > 15000) {
            this.receiveErrorCount = 0;
            this.allReceivedCommand.delete(0, length);
        }
        else {
            final int index = str.indexOf("-14D");
            final StringBuilder allReceivedCommand = this.allReceivedCommand;
            final int start = 0;
            if (index != -1) {
                length = index + 2;
            }
            allReceivedCommand.delete(start, length);
        }
        XlogUtils.xLog(WBO3Protocol.TAG, "RECEIVED ERROR Delete message : " + (Object)this.allReceivedCommand);
    }
    
    public static /* synthetic */ Timer access$000(final WBO3Protocol wbo3Protocol) {
        return wbo3Protocol.simulationTimer;
    }
    
    public static /* synthetic */ Timer access$002(final WBO3Protocol wbo3Protocol, final Timer simulationTimer) {
        return wbo3Protocol.simulationTimer = simulationTimer;
    }
    
    public static /* synthetic */ Handler access$100(final WBO3Protocol wbo3Protocol) {
        return wbo3Protocol.mHandler;
    }
    
    public static /* synthetic */ String access$200() {
        return WBO3Protocol.TAG;
    }
    
    public static /* synthetic */ Activity access$300(final WBO3Protocol wbo3Protocol) {
        return wbo3Protocol.myAty;
    }
    
    public static /* synthetic */ String access$400(final WBO3Protocol wbo3Protocol) {
        return wbo3Protocol.bondMacAddress;
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
    
    public void readRSSI() {
        if (this.isSimulationMode) {
            this.cancelSimulationTimer();
            return;
        }
        this.myBluetooth.readRSSI();
    }
    
    public String getSDKVersion() {
        return Global.sdkVersion;
    }
    
    public boolean isSupportBluetooth(final Activity activity) {
        return this.isSimulationMode || this.myBluetooth.isSupportBluetooth(activity);
    }
    
    public boolean isEnableBt() {
        return this.isSimulationMode || this.myBluetooth.isBTEnabled();
    }
    
    public boolean isScanning() {
        return this.isSimulationMode || this.myBluetooth.isScanning();
    }
    
    public boolean isConnected() {
        return this.isSimulationMode || this.myBluetooth.isConnected();
    }
    
    public void startScan(final int n) {
        if (this.isSimulationMode) {
            this.startSimulationTimer();
            return;
        }
        this.myBluetooth.startLEScan(n, false);
    }
    
    public void stopScan() {
        if (this.isSimulationMode) {
            this.cancelSimulationTimer();
            return;
        }
        this.myBluetooth.stopLEScan();
    }
    
    public void bond(final String bondMacAddress) {
        this.myAty.registerReceiver(this.bondedBTReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
        final BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(bondMacAddress);
        try {
            if (remoteDevice.getBondState() == 10) {
                final BluetoothDevice bluetoothDevice = remoteDevice;
                XlogUtils.xLog(WBO3Protocol.TAG, "bondedBTReceiver BOND_NONE..");
                this.bondMacAddress = bondMacAddress;
                bluetoothDevice.createBond();
            }
            else {
                XlogUtils.xLog(WBO3Protocol.TAG, "bondedBTReceiver BOND..");
                this.myAty.unregisterReceiver(this.bondedBTReceiver);
                this.connect(bondMacAddress);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            XlogUtils.xLog(WBO3Protocol.TAG, "Exception Bond\uff1a" + ex.getMessage());
        }
    }
    
    public void connect(final String bondMacAddress) {
        if (this.isSimulationMode) {
            this.mHandler.removeMessages(0);
            this.cancelSimulationTimer();
            (this.simulationTimer = new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    WBO3Protocol.access$100(WBO3Protocol.this).sendEmptyMessage(101);
                }
            }, 1500L);
            return;
        }
        this.bondMacAddress = bondMacAddress;
        this.myBluetooth.sendCount = 0;
        MyBluetoothLE.MY_UUID_WRITE = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
        MyBluetoothLE.MY_UUID_NOTIFY = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
        final ArrayList list = new ArrayList();
        final ArrayList list2 = list;
        new ArrayList();
        list.add(bondMacAddress);
        this.myBluetooth.connect(list2);
    }
    
    public void disconnect() {
        if (this.isSimulationMode) {
            this.onConnectStateListener.onConnectionState(ConnectState.Disconnect);
            return;
        }
        this.myBluetooth.disconnect(18);
    }
    
    @Override
    public void onWriteMessage(final boolean arg1, final String obj) {
        final Message message = new Message();
        final Message message4;
        final Message message3;
        final Message message2 = message3 = (message4 = message);
        new Message();
        message3.what = 1000;
        message4.arg1 = (arg1 ? 1 : 0);
        message.obj = obj;
        this.mHandler.sendMessage(message2);
    }
    
    @Override
    public void onBtStateChanged(final boolean b) {
        this.onConnectStateListener.onBtStateChanged(b);
    }
    
    @Override
    public void scanResult(final String s, final String s2, final int n, final byte[] array) {
        final Iterator<String> iterator = this.targetDeviceNames.iterator();
        while (iterator.hasNext()) {
            if (s2.startsWith(iterator.next())) {
                this.onConnectStateListener.onScanResult(s, s2, n);
            }
        }
    }
    
    @Override
    public void connectionStatus(final int i) {
        XlogUtils.xLog(WBO3Protocol.TAG, "connectionStatus : " + i);
        if (i != 4) {
            if (i != 20) {
                if (i != 17) {
                    if (i == 18) {
                        this.mConnectState = ConnectState.Disconnect;
                    }
                }
                else {
                    this.mConnectState = ConnectState.Connected;
                    this.readUserAndVersionData();
                }
            }
            else {
                this.mConnectState = ConnectState.ConnectTimeout;
            }
        }
        else {
            this.mConnectState = ConnectState.ScanFinish;
        }
        this.onConnectStateListener.onConnectionState(this.mConnectState);
    }
    
    public void readAllHistorys() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBO3 = this.myBluetooth.buildCmdStringForWBO3("00", "");
        XlogUtils.xLog(WBO3Protocol.TAG, "readAllHistorys\uff1a" + buildCmdStringForWBO3);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBO3, false, false, true);
    }
    
    public void readCBPData(final int i, final CBPdataAndCalCBP.Dformat dformat) {
        if (this.isSimulationMode) {
            return;
        }
        final String s = "01";
        int n = 0;
        final int n2;
        if ((n2 = dformat.ordinal()) != 1) {
            if (n2 != 2) {
                if (n2 == 3) {
                    n = 3;
                }
            }
            else {
                n = 1;
            }
        }
        else {
            n = 0;
        }
        final Object[] args;
        final Object[] array = args = new Object[2];
        final int j = n;
        args[0] = i;
        array[1] = j;
        final String buildCmdStringForWBO3 = this.myBluetooth.buildCmdStringForWBO3(s, String.format("%04X%02X", args));
        XlogUtils.xLog(WBO3Protocol.TAG, "readCBPData\uff1a" + buildCmdStringForWBO3);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBO3, false, false, true);
    }
    
    public void clearAllHistorys() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBO3 = this.myBluetooth.buildCmdStringForWBO3("03", "");
        XlogUtils.xLog(WBO3Protocol.TAG, "clearAllHistorys\uff1a" + buildCmdStringForWBO3);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBO3, false, false, true);
    }
    
    public void disconnectWBO3() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBO3 = this.myBluetooth.buildCmdStringForWBO3("04", "");
        XlogUtils.xLog(WBO3Protocol.TAG, "disconnectWBO3\uff1a" + buildCmdStringForWBO3);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBO3, true, true, false);
    }
    
    public void readUserAndVersionData() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBO3 = this.myBluetooth.buildCmdStringForWBO3("05", "");
        XlogUtils.xLog(WBO3Protocol.TAG, "readUserAndVersionData\uff1a" + buildCmdStringForWBO3);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBO3, false, false, false);
    }
    
    public void writeUserID(final String s) {
        if (this.isSimulationMode) {
            return;
        }
        BaseUtils.printLog("d", WBO3Protocol.TAG, "WBO3Protocol.writeUserData origin:" + this.origin + " ID:" + s);
        final String buildCmdStringForWBO3 = this.myBluetooth.buildCmdStringForWBO3("06", new e(s).a(30, true));
        XlogUtils.xLog(WBO3Protocol.TAG, "writeUserData origin\uff1a" + this.origin + " ID\uff1a" + s);
        XlogUtils.xLog(WBO3Protocol.TAG, "writeUserData : " + buildCmdStringForWBO3);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBO3, false, false, false);
    }
    
    public void readSettingValues() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBO3 = this.myBluetooth.buildCmdStringForWBO3("07", "");
        XlogUtils.xLog(WBO3Protocol.TAG, "readSettingValues\uff1a" + buildCmdStringForWBO3);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBO3, false, false, false);
    }
    
    public void writeSettingValues(final SettingValues settingValues) {
        if (this.isSimulationMode) {
            return;
        }
        final String s = "08";
        final int n = (settingValues.isCBP_zone2_meas_off() ? 1 : 0) << 3 | (settingValues.isCBP_zone1_meas_off() ? 1 : 0) << 2 | (settingValues.isSW_SEL_silent() ? 1 : 0) << 1 | (settingValues.isSW_checkhide() ? 1 : 0);
        final Object[] array = new Object[8];
        final Object[] array3;
        final Object[] array2;
        final Object[] args = array2 = (array3 = array);
        final int i = n;
        final Object[] array4 = args;
        final Object[] array5 = args;
        final Object[] array6 = args;
        final Object[] array7 = args;
        args[0] = settingValues.getABPMStart();
        array7[1] = settingValues.getABPMEnd();
        array6[2] = settingValues.getABPMInt_first();
        array5[3] = settingValues.getABPMInt_second();
        array4[4] = settingValues.getHI_infPressure();
        array2[5] = i;
        array3[6] = settingValues.getCBPInt_first();
        array[7] = settingValues.getCBPInt_second();
        final String buildCmdStringForWBO3 = this.myBluetooth.buildCmdStringForWBO3(s, String.format("%02X%02X%02X%02X%04X%02X%02X%02X000000000000", args));
        XlogUtils.xLog(WBO3Protocol.TAG, "writeSettingValues\uff1a" + buildCmdStringForWBO3);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBO3, false, false, false);
    }
    
    public void readDeviceIDAndInfo() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBO3 = this.myBluetooth.buildCmdStringForWBO3("0B", "");
        XlogUtils.xLog(WBO3Protocol.TAG, "readDeviceIDAndInfo\uff1a" + buildCmdStringForWBO3);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBO3, false, false, false);
    }
    
    public void readDeviceTime() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBO3 = this.myBluetooth.buildCmdStringForWBO3("0C", "");
        XlogUtils.xLog(WBO3Protocol.TAG, "readDeviceTime\uff1a" + buildCmdStringForWBO3);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBO3, false, false, false);
    }
    
    public void writeDeviceTime() {
        if (this.isSimulationMode) {
            return;
        }
        final Calendar instance = Calendar.getInstance();
        final Object[] args;
        final Object[] array = args = new Object[6];
        final Calendar calendar = instance;
        final Object[] array2 = args;
        final Calendar calendar2 = instance;
        final Object[] array3 = args;
        final Calendar calendar3 = instance;
        final Object[] array4 = args;
        final Calendar calendar4 = instance;
        final Object[] array5 = args;
        final Calendar calendar5 = instance;
        args[0] = instance.get(1) % 100;
        array5[1] = calendar5.get(2) + 1;
        array4[2] = calendar4.get(5);
        array3[3] = calendar3.get(11);
        array2[4] = calendar2.get(12);
        array[5] = calendar.get(13);
        final String buildCmdStringForWBO3 = this.myBluetooth.buildCmdStringForWBO3("0D", String.format("%02X%02X%02X%02X%02X%02X", args));
        XlogUtils.xLog(WBO3Protocol.TAG, "writeDeviceTime\uff1a" + buildCmdStringForWBO3);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBO3, false, false, false);
    }
    
    public void readFunctionSettingValue() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBO3 = this.myBluetooth.buildCmdStringForWBO3("10", "");
        XlogUtils.xLog(WBO3Protocol.TAG, "readSettingValues\uff1a" + buildCmdStringForWBO3);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBO3, false, false, false);
    }
    
    public void readBTModuleName() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBO3 = this.myBluetooth.buildCmdStringForWBO3("13", "");
        XlogUtils.xLog(WBO3Protocol.TAG, "readBTModuleName\uff1a" + buildCmdStringForWBO3);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBO3, false, false, false);
    }
    
    @Override
    public void dataResult(String s) {
        if (s.startsWith("Software_Revision_String")) {
            XlogUtils.xLog(WBO3Protocol.TAG, "Software Revision String  -> " + s.split("==")[1]);
            return;
        }
        if ((s = s.toUpperCase(Locale.US)).contains("==")) {
            s = s.split("==")[1];
        }
        final Message message = new Message();
        final Message message2 = message;
        final String obj = s;
        final Message message3 = message2;
        new Message();
        message3.what = 1001;
        message.obj = obj;
        this.mHandler.sendMessage(message2);
        XlogUtils.xLog(WBO3Protocol.TAG, "dataResult  -> " + s);
        try {
            this.allReceivedCommand.append(s);
            try {
                final String s2 = s = this.allReceivedCommand.toString();
                XlogUtils.xLog(WBO3Protocol.TAG, "allReceivedCommand = " + s);
                final boolean correctHeader = this.isCorrectHeader(s2);
                final String s3 = s;
                final boolean correctEnd = this.isCorrectEnd(s);
                try {
                    final int correctLength = this.getCorrectLength(s3);
                    try {
                        XlogUtils.xLog(WBO3Protocol.TAG, "headerCorrect : " + correctHeader + " endCorrect : " + correctEnd + " lengthCorrect : " + correctLength + " message.length : " + s.length());
                        Label_0679: {
                            if (!correctHeader || !correctEnd || s.length() < correctLength) {
                                break Label_0679;
                            }
                            this.receiveErrorCount = 0;
                            XlogUtils.xLog(WBO3Protocol.TAG, " All received message -> " + s);
                            while (true) {
                                if (this.allReceivedCommand.length() == 0) {
                                    return;
                                }
                                final int correctLength2 = this.getCorrectLength(s = this.allReceivedCommand.toString());
                                try {
                                    final String s4 = s = this.allReceivedCommand.substring(0, correctLength2);
                                    final int n = correctLength2;
                                    XlogUtils.xLog(WBO3Protocol.TAG, "Start parsing message -> " + s);
                                    XlogUtils.xLog(WBO3Protocol.TAG, "Start parsing New lengthCorrect -> " + correctLength2);
                                    final int endIndex;
                                    final String substring;
                                    final String s5 = substring = s4.substring(endIndex = n - 4, correctLength2);
                                    final String substring2 = s.substring(8, 10);
                                    try {
                                        XlogUtils.xLog(WBO3Protocol.TAG, "cmd = " + substring2);
                                        final String calcChecksum4 = this.myBluetooth.calcChecksum4(s.substring(0, 2), s.substring(2, 4), s.substring(4, 8), s.substring(8, 10), s.substring(10, endIndex));
                                        XlogUtils.xLog(WBO3Protocol.TAG, "receiveChecksum = " + substring + " calcChecksum = " + calcChecksum4);
                                        Label_0644: {
                                            if (!s5.equals(calcChecksum4)) {
                                                break Label_0644;
                                            }
                                            int i = this.getCmd(s);
                                            int cmd = 0;
                                            Label_0604: {
                                                if (this.isSimulationMode || this.myBluetooth.getCommArraySize() <= 0) {
                                                    break Label_0604;
                                                }
                                                final String s6 = s;
                                                i = this.getCmd(this.myBluetooth.getComm(0));
                                                try {
                                                    cmd = this.getCmd(s6);
                                                    try {
                                                        XlogUtils.xLog(WBO3Protocol.TAG, "writeCmd = " + i + " receiveCmd = " + cmd);
                                                        if (i == cmd) {
                                                            this.myBluetooth.sendCount = 0;
                                                            this.myBluetooth.removeComm(0);
                                                        }
                                                        final String s7 = s;
                                                        this.allReceivedCommand.delete(0, correctLength2);
                                                        try {
                                                            this.handleReceived(s7);
                                                            continue;
                                                            // iftrue(Label_0741:, this.receiveErrorCount <= 15000)
                                                        }
                                                        catch (Exception ex) {
                                                            ex.printStackTrace();
                                                            this.receiveError(s);
                                                        }
                                                    }
                                                    catch (Exception ex2) {}
                                                }
                                                catch (Exception ex3) {}
                                            }
                                        }
                                    }
                                    catch (Exception ex4) {}
                                }
                                catch (Exception ex5) {}
                                break;
                            }
                        }
                    }
                    catch (Exception ex6) {}
                }
                catch (Exception ex7) {}
            }
            catch (Exception ex8) {}
        }
        catch (Exception ex9) {}
        Label_0741:;
    }
    
    public enum ConnectState
    {
        ScanFinish, 
        Connected, 
        Disconnect, 
        ConnectTimeout;
    }
    
    public interface OnDataResponseListener
    {
        void onResponseReadAllHistorys(final DRecord p0);
        
        void onResponseReadCBPData(final CBPdataAndCalCBP p0, final boolean p1);
        
        void onResponseClearHistorys(final boolean p0);
        
        void onResponseReadUserAndVersionData(final User p0, final VersionData p1);
        
        void onResponseWriteUserID(final boolean p0);
        
        void onResponseReadSettingValues(final SettingValues p0);
        
        void onResponseWriteSettingValues(final boolean p0);
        
        void onResponseReadDeviceInfo(final DeviceInfo p0);
        
        void onResponseReadDeviceTime(final DeviceInfo p0);
        
        void onResponseWriteDeviceTime(final boolean p0);
        
        void onResponseReadFunctionSettingValues(final FunctionSettingValues p0);
        
        void onResponseReadBTModuleName(final String p0);
    }
    
    public interface OnConnectStateListener
    {
        void onBtStateChanged(final boolean p0);
        
        void onScanResult(final String p0, final String p1, final int p2);
        
        void onConnectionState(final ConnectState p0);
    }
    
    public interface OnNotifyStateListener
    {
        void onNotifyMessage(final String p0);
    }
}
