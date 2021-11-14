// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.protocol;

import java.util.Locale;
import java.util.Iterator;
import java.util.Calendar;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;

import com.microlife_sdk.model.abcdef.e;
import com.microlife_sdk.model.data.DRecord;
import com.microlife_sdk.model.data.DiagnosticDRecord;
import com.microlife_sdk.ideabuslibrary.util.BaseUtils;
import com.microlife_sdk.model.data.VersionData;
import com.microlife_sdk.model.data.User;
import com.microlife_sdk.model.data.NocturnalModeDRecord;
import com.microlife_sdk.model.data.DeviceInfo;
import java.util.TimerTask;
import android.net.Uri;
import java.util.ArrayList;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Message;
import com.microlife_sdk.model.XlogUtils;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Handler;
import java.util.List;
import android.content.Context;
import java.util.Timer;
import com.microlife_sdk.model.bluetooth.MyBluetoothLE;
import com.microlife_sdk.model.bluetooth.BluetoothLEClass;

public class WBPProtocol implements BluetoothLEClass.OnIMBluetoothLEListener, MyBluetoothLE.OnWriteStateListener
{
    public static final String TAG = "WBPProtocol";
    public static WBPProtocol protocol;
    public static final int RECEIVED_ERROR_COUNT = 10000;
    public static final String CMD_REPLY_RESULT_SUCCESS = "81";
    public static final String CMD_REPLY_RESULT_NACK = "91";
    public static final String SPACE_ASSCII = "20";
    public MyBluetoothLE myBluetooth;
    public int receiveErrorCount;
    public StringBuilder allReceivedCommand;
    public boolean isSimulationMode;
    public int position;
    public Timer simulationTimer;
    public int origin;
    public String bondMacAddress;
    public Context myAty;
    public String oldCom;
    public List<String> targetDeviceNames;
    public ConnectState mConnectState;
    public Handler mHandler;
    public MyBluetoothLE.OnWriteStateListener mOnWriteStateListener;
    public OnNotifyStateListener mOnNotifyStateListener;
    public OnConnectStateListener onConnectStateListener;
    public OnDataResponseListener onDataResponseListener;
    public BroadcastReceiver bondedBTReceiver;
    
    public static WBPProtocol getInstance(final Activity activity, final boolean b, final boolean b2) {
        XlogUtils.initXlog(activity, b2);
        if (WBPProtocol.protocol == null) {
            WBPProtocol.protocol = new WBPProtocol((Context)activity, b, b2);
        }
        return WBPProtocol.protocol;
    }
    
    public static WBPProtocol getInstance(final Context context, final boolean b, final boolean b2) {
        if (WBPProtocol.protocol == null) {
            WBPProtocol.protocol = new WBPProtocol(context, b, b2);
        }
        return WBPProtocol.protocol;
    }
    
    public WBPProtocol(final Context myAty, final boolean isSimulationMode, final boolean b) {
        this.receiveErrorCount = 0;
        this.isSimulationMode = false;
        this.position = 0;
        this.origin = 81;
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
                                    if ((mOnNotifyStateListener = WBPProtocol.this.mOnNotifyStateListener) != null) {
                                        mOnNotifyStateListener.onNotifyMessage((String)message.obj);
                                    }
                                }
                            }
                            else {
                                final MyBluetoothLE.OnWriteStateListener mOnWriteStateListener;
                                if ((mOnWriteStateListener = WBPProtocol.this.mOnWriteStateListener) != null) {
                                    mOnWriteStateListener.onWriteMessage(message.arg1 == 1, (String)message.obj);
                                }
                            }
                        }
                        else {
                            WBPProtocol.this.connectionStatus(17);
                        }
                    }
                    else {
                        final WBPProtocol a = WBPProtocol.this;
                        ++a.position;
                        final OnConnectStateListener onConnectStateListener;
                        if ((onConnectStateListener = a.onConnectStateListener) != null) {
                            onConnectStateListener.onScanResult("abcde12345678" + WBPProtocol.this.position, "Fuel " + WBPProtocol.this.position, WBPProtocol.this.position - 40);
                        }
                        final WBPProtocol a2;
                        if ((a2 = WBPProtocol.this).position >= 10) {
                            WBPProtocol.access$000(a2).cancel();
                            WBPProtocol.access$002(WBPProtocol.this, null);
                        }
                    }
                }
                else {
                    WBPProtocol.this.dataResult((String)message.obj);
                }
            }
        };
        this.bondedBTReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                switch (((BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getBondState()) {
                    case 12: {
                        XlogUtils.xLog(WBPProtocol.access$100(), "bondedBTReceiver bonded...");
                        WBPProtocol.access$200(WBPProtocol.this).unregisterReceiver(WBPProtocol.this.bondedBTReceiver);
                        WBPProtocol.this.readUserAndVersionData();
                        break;
                    }
                    case 11: {
                        XlogUtils.xLog(WBPProtocol.access$100(), "bondedBTReceiver bonding...");
                        break;
                    }
                    case 10: {
                        XlogUtils.xLog(WBPProtocol.access$100(), "bondedBTReceiver can't bond...");
                        break;
                    }
                }
            }
        };
        this.isSimulationMode = isSimulationMode;
        if (this.isSimulationMode) {
            return;
        }
        (this.targetDeviceNames = new ArrayList<String>()).add("WatchBP Home");
        if (this.myBluetooth == null) {
            this.myBluetooth = MyBluetoothLE.getInstance(myAty, b, 1800);
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
    
    private void startSimulationTimer() {
        this.cancelSimulationTimer();
        this.position = 0;
        (this.simulationTimer = new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                WBPProtocol.access$300(WBPProtocol.this).sendEmptyMessage(100);
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
    
    private void handleReceived(String str) {
        final String s = str;
        XlogUtils.xLog(WBPProtocol.TAG, "handleReceived message : " + str);
        final Boolean value;
        if (value = (s.equals(this.oldCom) ^ true)) {
            this.oldCom = str;
        }
        final int cmd = this.getCmd(str);
        final String s2 = str;
        this.origin = Integer.parseInt(str.substring(2, 4), 16);
        str = s2.substring(8, s2.length() - 2);
        XlogUtils.xLog(WBPProtocol.TAG, "CMD : " + cmd + " data : " + str);
        while (true) {
            switch (cmd) {
                default: {
                    this.receiveError(str);
                }
                case 15: {
                    final Boolean b = value;
                    final DeviceInfo deviceInfo;
                    com.microlife_sdk.model.abcdef.d.c(str, deviceInfo = new DeviceInfo());
                    if (b) {
                        this.onDataResponseListener.onResponseReadReadSerialNumber(deviceInfo);
                    }
                    break;
                }
                case 14: {
                    final String s3 = str;
                    final NocturnalModeDRecord nocturnalModeDRecord = new NocturnalModeDRecord();
                    boolean b2;
                    if (s3.equals("91")) {
                        b2 = true;
                    }
                    else {
                        final String s4 = str;
                        final NocturnalModeDRecord nocturnalModeDRecord2 = nocturnalModeDRecord;
                        b2 = false;
                        com.microlife_sdk.model.abcdef.d.a(s4, nocturnalModeDRecord2, this.origin);
                    }
                    if (value) {
                        this.onDataResponseListener.onResponseReadNocturnalPatternHistory(nocturnalModeDRecord, b2);
                    }
                    break;
                }
                case 13: {
                    final Boolean b3 = value;
                    final e e2 = new e(str);
                    str = e2.a(86);
                    final e e3 = new e(str);
                    final User user = new com.microlife_sdk.model.data.User();
                    final User user2 = user;
                    final e e4 = e3;
                    final User user3 = user2;
                    final e e5 = e3;
                    new User();
                    user3.setNO(e5.c(2));
                    user.setID(e4.b(22));
                    final String a = e2.a(e2.a());
                    final VersionData versionData = new com.microlife_sdk.model.data.VersionData();
                    final VersionData versionData2 = versionData;
                    final String s5 = a;
                    final VersionData versionData3 = versionData2;
                    new VersionData();
                    com.microlife_sdk.model.abcdef.d.c(s5, versionData3);
                    BaseUtils.printLog("d", WBPProtocol.TAG, versionData.toString());
                    this.writeDeviceTime();
                    if (b3) {
                        this.onDataResponseListener.onResponseReadUserAndVersionData(user2, versionData2);
                    }
                    break;
                }
                case 12: {
                    final Boolean b4 = value;
                    final DeviceInfo deviceInfo2;
                    com.microlife_sdk.model.abcdef.d.a(str, deviceInfo2 = new DeviceInfo());
                    if (b4) {
                        this.onDataResponseListener.onResponseReadDeviceTime(deviceInfo2);
                    }
                    break;
                }
                case 11: {
                    final Boolean b5 = value;
                    final String s6 = str;
                    final DeviceInfo deviceInfo3 = new com.microlife_sdk.model.data.DeviceInfo();
                    final DeviceInfo deviceInfo4 = deviceInfo3;
                    new DeviceInfo();
                    com.microlife_sdk.model.abcdef.d.a(s6, deviceInfo3, this.origin);
                    if (b5) {
                        this.onDataResponseListener.onResponseReadDeviceInfo(deviceInfo4);
                    }
                    break;
                }
                case 7: {
                    if (str.length() == 2) {
                        this.onDataResponseListener.onResponseChangeNocturnalModeSetting(str.equals("81"));
                        break;
                    }
                    final Boolean b6 = value;
                    final DeviceInfo deviceInfo5;
                    com.microlife_sdk.model.abcdef.d.b(str, deviceInfo5 = new DeviceInfo());
                    if (b6) {
                        this.onDataResponseListener.onResponseReadNocturnalModeSetting(deviceInfo5);
                    }
                    break;
                }
                case 1: {
                    final String s7 = str;
                    final DiagnosticDRecord diagnosticDRecord = new DiagnosticDRecord();
                    boolean b7;
                    if (s7.equals("91")) {
                        b7 = true;
                    }
                    else {
                        final String s8 = str;
                        final DiagnosticDRecord diagnosticDRecord2 = diagnosticDRecord;
                        b7 = false;
                        com.microlife_sdk.model.abcdef.d.a(s8, diagnosticDRecord2, this.origin);
                    }
                    if (value) {
                        this.onDataResponseListener.onResponseReadDiagnosticModeHistory(diagnosticDRecord, b7);
                    }
                    break;
                }
                case 0: {
                    final String s9 = str;
                    final DRecord dRecord = new DRecord();
                    boolean b8;
                    if (s9.equals("91")) {
                        b8 = true;
                    }
                    else {
                        final String s10 = str;
                        final DRecord dRecord2 = dRecord;
                        b8 = false;
                        com.microlife_sdk.model.abcdef.d.a(s10, dRecord2, this.origin);
                    }
                    if (value) {
                        this.onDataResponseListener.onResponseReadUsualModeHistory(dRecord, b8);
                    }
                    break;
                }
                case 4: {
                    this.receiveErrorCount = 0;
                }
                case 6: {
                    this.onDataResponseListener.onResponseWriteUserID(str.equals("81"));
                    continue;
                }
                case 5: {
                    this.onDataResponseListener.onResponseWriteDeviceTime(str.equals("81"));
                    continue;
                }
                case 3: {
                    this.onDataResponseListener.onResponseClearCurrentModeHistory(str.equals("81"));
                    continue;
                }
                case 2: {
                    this.onDataResponseListener.onResponseClearSelectedModeHistory(str.equals("81"));
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
            int1 = Integer.parseInt(s.substring(6, 7) + s.substring(4, 6), 16);
        }
        return (int1 + 3) * 2;
    }
    
    private int getCmd(final String s) {
        return Integer.parseInt(s.substring(7, 8), 16);
    }
    
    private void receiveError(final String str) {
        XlogUtils.xLog(WBPProtocol.TAG, "Receive error Count = " + this.receiveErrorCount + " message : " + str);
        ++this.receiveErrorCount;
        int length = this.allReceivedCommand.length();
        if (this.receiveErrorCount > 10000) {
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
        XlogUtils.xLog(WBPProtocol.TAG, "RECEIVED ERROR Delete message : " + (Object)this.allReceivedCommand);
    }
    
    public static /* synthetic */ Timer access$000(final WBPProtocol wbpProtocol) {
        return wbpProtocol.simulationTimer;
    }
    
    public static /* synthetic */ Timer access$002(final WBPProtocol wbpProtocol, final Timer simulationTimer) {
        return wbpProtocol.simulationTimer = simulationTimer;
    }
    
    public static /* synthetic */ String access$100() {
        return WBPProtocol.TAG;
    }
    
    public static /* synthetic */ Context access$200(final WBPProtocol wbpProtocol) {
        return wbpProtocol.myAty;
    }
    
    public static /* synthetic */ Handler access$300(final WBPProtocol wbpProtocol) {
        return wbpProtocol.mHandler;
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
                XlogUtils.xLog(WBPProtocol.TAG, "bondedBTReceiver BOND_NONE..");
                this.bondMacAddress = bondMacAddress;
                bluetoothDevice.createBond();
            }
            else {
                XlogUtils.xLog(WBPProtocol.TAG, "bondedBTReceiver BOND..");
                this.myAty.unregisterReceiver(this.bondedBTReceiver);
                this.connect(this.bondMacAddress);
                this.readUserAndVersionData();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            XlogUtils.xLog(WBPProtocol.TAG, "Exception Bond\uff1a" + ex.getMessage());
        }
    }
    
    public void connect(final String bondMacAddress) {
        if (this.isSimulationMode) {
            this.mHandler.removeMessages(0);
            this.cancelSimulationTimer();
            (this.simulationTimer = new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    WBPProtocol.access$300(WBPProtocol.this).sendEmptyMessage(101);
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
    
    public void readUsualModeHistoryData() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("00", "");
        XlogUtils.xLog(WBPProtocol.TAG, "readUsualModeHistoryData\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, true);
    }
    
    public void readDiagnosticModeHistoryData() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("01", "");
        XlogUtils.xLog(WBPProtocol.TAG, "readDiagnosticModeHistoryData\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, true);
    }
    
    public void clearHistoryData(final boolean b, final boolean b2, final boolean b3) {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("02", String.format("%02X", (b3 ? 1 : 0) << 2 | (b2 ? 1 : 0) << 1 | (b ? 1 : 0)));
        XlogUtils.xLog(WBPProtocol.TAG, "clearHistoryData\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, false);
    }
    
    public void clearCurrentModeHistoryData() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("03", "");
        XlogUtils.xLog(WBPProtocol.TAG, "clearCurrentModeHistoryData\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, false);
    }
    
    public void disconnectWBP() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("04", "");
        XlogUtils.xLog(WBPProtocol.TAG, "disconnectWBP\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, true, true, false);
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
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("05", String.format("%02X%02X%02X%02X%02X%02X", args));
        XlogUtils.xLog(WBPProtocol.TAG, "writeDeviceTime\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, false);
    }
    
    public void writeUserID(final String str) {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("06", "00" + new e(str).a(11, true) + "00000000000000000000");
        XlogUtils.xLog(WBPProtocol.TAG, "writeUserData origin\uff1a" + this.origin + " ID\uff1a" + str);
        XlogUtils.xLog(WBPProtocol.TAG, "writeUserData\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, false);
    }
    
    public void readNocturnalModeSetting() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("07", "01");
        XlogUtils.xLog(WBPProtocol.TAG, "readNocturnalModeSetting\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, false);
    }
    
    public void changeNocturnalModeSetting(final boolean i, final int n, final int j, final int k, final int l) {
        if (this.isSimulationMode) {
            return;
        }
        final String s = "07";
        final Object[] array = new Object[5];
        final Object[] array4;
        final Object[] array3;
        final Object[] array2;
        final Object[] args = array2 = (array3 = (array4 = array));
        args[0] = (i ? 1 : 0);
        array2[1] = n % 100;
        array3[2] = j;
        array4[3] = k;
        array[4] = l;
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP(s, String.format("02%02X%02X%02X%02X%02X", args));
        XlogUtils.xLog(WBPProtocol.TAG, "readNocturnalModeSetting\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, false);
    }
    
    public void readDeviceIDAndInfo() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("0B", "");
        XlogUtils.xLog(WBPProtocol.TAG, "readDeviceIDAndInfo\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, false);
    }
    
    public void readDeviceTime() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("0C", "");
        XlogUtils.xLog(WBPProtocol.TAG, "readDeviceTime\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, false);
    }
    
    public void readUserAndVersionData() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("0D", "");
        XlogUtils.xLog(WBPProtocol.TAG, "readUserAndVersionData\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, false);
    }
    
    public void readNocturnalModeHistoryData() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("0E", "");
        XlogUtils.xLog(WBPProtocol.TAG, "readNocturnalModeHistoryData\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, true);
    }
    
    public void writeSerialNumber(final String str) {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("0F", "0157534E" + new e(str).a(20, true));
        XlogUtils.xLog(WBPProtocol.TAG, "writeSerialNumber origin\uff1a" + this.origin + " SN\uff1a" + str);
        XlogUtils.xLog(WBPProtocol.TAG, "writeSerialNumber\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, true);
    }
    
    public void readSerialNumber() {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForWBP = this.myBluetooth.buildCmdStringForWBP("0F", "00");
        XlogUtils.xLog(WBPProtocol.TAG, "readSerialNumber\uff1a" + buildCmdStringForWBP);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForWBP, false, false, false);
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
        XlogUtils.xLog(WBPProtocol.TAG, "connectionStatus\uff1a" + i);
        if (i != 4) {
            if (i != 20) {
                if (i != 17) {
                    if (i == 18) {
                        this.mConnectState = ConnectState.Disconnect;
                    }
                }
                else {
                    this.mConnectState = ConnectState.Connected;
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
    
    public String getSDKVersion() {
        return Global.sdkVersion;
    }
    
    @Override
    public void dataResult(String str) {
        if (str.startsWith("Software_Revision_String")) {
            final String s = str = str.split("==")[1];
            XlogUtils.xLog(WBPProtocol.TAG, "Software Revision String  -> " + str);
            if (s.startsWith("1.0.22")) {
                this.bond(this.bondMacAddress);
            }
            else {
                this.readUserAndVersionData();
            }
            return;
        }
        if ((str = str.toUpperCase(Locale.US)).contains("4D51029131")) {
            return;
        }
        if (str.contains("==")) {
            str = str.split("==")[1];
        }
        final Message message = new Message();
        final Message message2 = message;
        final String obj = str;
        final Message message3 = message2;
        new Message();
        message3.what = 1001;
        message.obj = obj;
        this.mHandler.sendMessage(message2);
        XlogUtils.xLog(WBPProtocol.TAG, "dataResult  -> " + str);
        try {
            this.allReceivedCommand.append(str);
            try {
                final String s2 = str = this.allReceivedCommand.toString();
                XlogUtils.xLog(WBPProtocol.TAG, "allReceivedCommand = " + str);
                final boolean correctHeader = this.isCorrectHeader(s2);
                final String s3 = str;
                final boolean correctEnd = this.isCorrectEnd(str);
                try {
                    final int correctLength = this.getCorrectLength(s3);
                    try {
                        XlogUtils.xLog(WBPProtocol.TAG, "headerCorrect : " + correctHeader + " endCorrect : " + correctEnd + " lengthCorrect : " + correctLength + " message.length : " + str.length());
                        Label_0715: {
                            if (!correctHeader || !correctEnd || str.length() < correctLength) {
                                break Label_0715;
                            }
                            this.receiveErrorCount = 0;
                            XlogUtils.xLog(WBPProtocol.TAG, " All received message -> " + str);
                        Block_31_Outer:
                            while (true) {
                                if (this.allReceivedCommand.length() == 0) {
                                    return;
                                }
                                final int correctLength2 = this.getCorrectLength(str = this.allReceivedCommand.toString());
                                try {
                                    final String s4 = str = this.allReceivedCommand.substring(0, correctLength2);
                                    final int n = correctLength2;
                                    XlogUtils.xLog(WBPProtocol.TAG, "Start parsing message -> " + str);
                                    XlogUtils.xLog(WBPProtocol.TAG, "Start parsing New lengthCorrect -> " + correctLength2);
                                    final int endIndex;
                                    final String substring;
                                    final String s5 = substring = s4.substring(endIndex = n - 2, correctLength2);
                                    final String substring2 = str.substring(6, 8);
                                    try {
                                        XlogUtils.xLog(WBPProtocol.TAG, "cmd = " + substring2);
                                        final String calcChecksum = this.myBluetooth.calcChecksum(str.substring(0, 2), str.substring(2, 4), str.substring(4, 6), str.substring(6, 8), str.substring(8, endIndex));
                                        XlogUtils.xLog(WBPProtocol.TAG, "receiveChecksum = " + substring + " calcChecksum = " + calcChecksum);
                                        Label_0680: {
                                            if (!s5.equals(calcChecksum)) {
                                                break Label_0680;
                                            }
                                            int i = this.getCmd(str);
                                            int cmd = 0;
                                            Label_0640: {
                                                if (this.isSimulationMode || this.myBluetooth.getCommArraySize() <= 0) {
                                                    break Label_0640;
                                                }
                                                final String s6 = str;
                                                i = this.getCmd(this.myBluetooth.getComm(0));
                                                try {
                                                    cmd = this.getCmd(s6);
                                                    try {
                                                        XlogUtils.xLog(WBPProtocol.TAG, "writeCmd = " + i + " receiveCmd = " + cmd);
                                                        if (i == cmd) {
                                                            this.myBluetooth.sendCount = 0;
                                                            this.myBluetooth.removeComm(0);
                                                        }
                                                        final String s7 = str;
                                                        this.allReceivedCommand.delete(0, correctLength2);
                                                        try {
                                                            this.handleReceived(s7);
                                                            continue;
                                                        }
                                                        catch (Exception ex) {
                                                            ex.printStackTrace();
                                                            this.receiveError(str);
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
        Label_0777:;
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
        void onResponseReadUsualModeHistory(final DRecord p0, final boolean p1);
        
        void onResponseReadDiagnosticModeHistory(final DiagnosticDRecord p0, final boolean p1);
        
        void onResponseClearSelectedModeHistory(final boolean p0);
        
        void onResponseClearCurrentModeHistory(final boolean p0);
        
        void onResponseWriteDeviceTime(final boolean p0);
        
        void onResponseWriteUserID(final boolean p0);
        
        void onResponseReadNocturnalModeSetting(final DeviceInfo p0);
        
        void onResponseChangeNocturnalModeSetting(final boolean p0);
        
        void onResponseReadDeviceInfo(final DeviceInfo p0);
        
        void onResponseReadDeviceTime(final DeviceInfo p0);
        
        void onResponseReadUserAndVersionData(final User p0, final VersionData p1);
        
        void onResponseReadNocturnalPatternHistory(final NocturnalModeDRecord p0, final boolean p1);
        
        void onResponseReadReadSerialNumber(final DeviceInfo p0);
        
        void onResponseWriteSerialNumber(final boolean p0);
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
