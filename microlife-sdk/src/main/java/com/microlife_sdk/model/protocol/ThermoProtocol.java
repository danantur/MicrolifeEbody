//
// Decompiled by Procyon v0.5.36
//

package com.microlife_sdk.model.protocol;

import java.util.Locale;
import java.util.Iterator;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;
import com.microlife_sdk.model.data.ThermoMeasureData;
import java.util.Date;
import java.util.TimerTask;
import android.net.Uri;
import com.microlife_sdk.ideabuslibrary.util.BaseUtils;
import java.util.ArrayList;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.Context;
import android.os.Looper;
import android.os.Message;
import com.microlife_sdk.model.XlogUtils;
import android.content.BroadcastReceiver;
import android.os.Handler;
import java.util.List;
import android.app.Activity;

import java.util.Timer;
import com.microlife_sdk.model.bluetooth.MyBluetoothLE;
import com.microlife_sdk.model.bluetooth.BluetoothLEClass;

public class ThermoProtocol implements BluetoothLEClass.OnIMBluetoothLEListener, MyBluetoothLE.OnWriteStateListener
{
    public static final String TAG = "ThermoProtocol";
    public static ThermoProtocol protocol;
    public static final int RECEIVED_ERROR_COUNT = 10;
    public static final int SEND_REQUEST = 161;
    public static final int UPLOAD_MEASURE_DATA = 160;
    public static final int UPLOAD_CALIBRATE_PARAM = 162;
    public static final String CMD_REPLY_RESULT_SUCCESS = "81";
    public MyBluetoothLE myBluetooth;
    public int receiveErrorCount;
    public StringBuilder allReceivedCommand;
    public boolean isSimulationMode;
    public int position;
    public Timer simulationTimer;
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

    public static ThermoProtocol getInstance(final Activity activity, final boolean b, final boolean b2, final String s) {
        XlogUtils.initXlog(activity, b2);
        if (ThermoProtocol.protocol == null) {
            ThermoProtocol.protocol = new ThermoProtocol(activity, b, b2);
        }
        return ThermoProtocol.protocol;
    }

    public static ThermoProtocol getInstance(final MyBluetoothLE myBluetoothLE, final boolean b, final boolean b2, final String s) {
        if (ThermoProtocol.protocol == null) {
            ThermoProtocol.protocol = new ThermoProtocol(myBluetoothLE, b, b2);
        }
        return ThermoProtocol.protocol;
    }

    public ThermoProtocol(final MyBluetoothLE myBluetooth, final boolean isSimulationMode, final boolean b) {
        this.receiveErrorCount = 0;
        this.isSimulationMode = false;
        this.position = 0;
        this.bondMacAddress = "";
        this.oldCom = "";
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(final Message message) {
                final int what;
                if ((what = message.what) != 0) {
                    if (what != 100) {
                        if (what != 101) {
                            if (what != 1000) {
                                if (what == 1001) {
                                    final OnNotifyStateListener mOnNotifyStateListener;
                                    if ((mOnNotifyStateListener = ThermoProtocol.this.mOnNotifyStateListener) != null) {
                                        mOnNotifyStateListener.onNotifyMessage((String)message.obj);
                                    }
                                }
                            }
                            else {
                                final MyBluetoothLE.OnWriteStateListener mOnWriteStateListener;
                                if ((mOnWriteStateListener = ThermoProtocol.this.mOnWriteStateListener) != null) {
                                    mOnWriteStateListener.onWriteMessage(message.arg1 == 1, (String)message.obj);
                                }
                            }
                        }
                        else {
                            ThermoProtocol.this.connectionStatus(17);
                        }
                    }
                    else {
                        final ThermoProtocol a = ThermoProtocol.this;
                        ++a.position;
                        final OnConnectStateListener onConnectStateListener;
                        if ((onConnectStateListener = a.onConnectStateListener) != null) {
                            onConnectStateListener.onScanResult("abcde12345678" + ThermoProtocol.this.position, "Fuel " + ThermoProtocol.this.position, ThermoProtocol.this.position - 40);
                        }
                        final ThermoProtocol a2;
                        if ((a2 = ThermoProtocol.this).position >= 10) {
                            ThermoProtocol.access$000(a2).cancel();
                            ThermoProtocol.access$002(ThermoProtocol.this, null);
                        }
                    }
                }
                else {
                    ThermoProtocol.this.dataResult((String)message.obj);
                }
            }
        };
        this.bondedBTReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                switch (((BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getBondState()) {
                    case 12: {
                        XlogUtils.xLog(ThermoProtocol.access$100(), "bondedBTReceiver bonded...");
                        ThermoProtocol.access$200(ThermoProtocol.this).unregisterReceiver(ThermoProtocol.this.bondedBTReceiver);
                        final ThermoProtocol a = ThermoProtocol.this;
                        a.connect(ThermoProtocol.access$300(a));
                        break;
                    }
                    case 11: {
                        XlogUtils.xLog(ThermoProtocol.access$100(), "bondedBTReceiver bonding...");
                        break;
                    }
                    case 10: {
                        XlogUtils.xLog(ThermoProtocol.access$100(), "bondedBTReceiver can't bond...");
                        break;
                    }
                }
            }
        };
        this.isSimulationMode = isSimulationMode;
        if (this.isSimulationMode) {
            return;
        }
        this.allReceivedCommand = new StringBuilder();
        this.myBluetooth = myBluetooth;
    }

    public ThermoProtocol(final Activity myAty, final boolean isSimulationMode, final boolean b) {
        this.receiveErrorCount = 0;
        this.isSimulationMode = false;
        this.position = 0;
        this.bondMacAddress = "";
        this.oldCom = "";
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(final Message message) {
                final int what;
                if ((what = message.what) != 0) {
                    if (what != 100) {
                        if (what != 101) {
                            if (what != 1000) {
                                if (what == 1001) {
                                    final OnNotifyStateListener mOnNotifyStateListener;
                                    if ((mOnNotifyStateListener = ThermoProtocol.this.mOnNotifyStateListener) != null) {
                                        mOnNotifyStateListener.onNotifyMessage((String)message.obj);
                                    }
                                }
                            }
                            else {
                                final MyBluetoothLE.OnWriteStateListener mOnWriteStateListener;
                                if ((mOnWriteStateListener = ThermoProtocol.this.mOnWriteStateListener) != null) {
                                    mOnWriteStateListener.onWriteMessage(message.arg1 == 1, (String)message.obj);
                                }
                            }
                        }
                        else {
                            ThermoProtocol.this.connectionStatus(17);
                        }
                    }
                    else {
                        final ThermoProtocol a = ThermoProtocol.this;
                        ++a.position;
                        final OnConnectStateListener onConnectStateListener;
                        if ((onConnectStateListener = a.onConnectStateListener) != null) {
                            onConnectStateListener.onScanResult("abcde12345678" + ThermoProtocol.this.position, "Fuel " + ThermoProtocol.this.position, ThermoProtocol.this.position - 40);
                        }
                        final ThermoProtocol a2;
                        if ((a2 = ThermoProtocol.this).position >= 10) {
                            ThermoProtocol.access$000(a2).cancel();
                            ThermoProtocol.access$002(ThermoProtocol.this, null);
                        }
                    }
                }
                else {
                    ThermoProtocol.this.dataResult((String)message.obj);
                }
            }
        };
        this.bondedBTReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                switch (((BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getBondState()) {
                    case 12: {
                        XlogUtils.xLog(ThermoProtocol.access$100(), "bondedBTReceiver bonded...");
                        ThermoProtocol.access$200(ThermoProtocol.this).unregisterReceiver(ThermoProtocol.this.bondedBTReceiver);
                        final ThermoProtocol a = ThermoProtocol.this;
                        a.connect(ThermoProtocol.access$300(a));
                        break;
                    }
                    case 11: {
                        XlogUtils.xLog(ThermoProtocol.access$100(), "bondedBTReceiver bonding...");
                        break;
                    }
                    case 10: {
                        XlogUtils.xLog(ThermoProtocol.access$100(), "bondedBTReceiver can't bond...");
                        break;
                    }
                }
            }
        };
        this.isSimulationMode = isSimulationMode;
        if (this.isSimulationMode) {
            return;
        }
        (this.targetDeviceNames = new ArrayList<String>()).add("3MW1");
        this.targetDeviceNames.add("NC150 BT");
        if (this.myBluetooth == null) {
            this.myBluetooth = MyBluetoothLE.getInstance((Context)myAty, b, 600);
            this.myAty = myAty;
        }
        final MyBluetoothLE myBluetooth;
        if ((myBluetooth = this.myBluetooth) != null) {
            myBluetooth.setOnIMBluetoothLEListener(this);
            this.myBluetooth.setOnWriteStateListener(this);
        }
        this.allReceivedCommand = new StringBuilder();
    }

    private String fillPreZero(String string) {
        if (string.length() == 1) {
            string = "0" + string;
        }
        return string;
    }

    public static String convertBytesToString(final byte[] array) {
        final StringBuilder sb = new StringBuilder("");
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; ++i) {
                sb.append(BaseUtils.getFillString((int)array[i], 2, "0"));
            }
            return sb.toString();
        }
        return null;
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
                ThermoProtocol.access$400(ThermoProtocol.this).sendEmptyMessage(100);
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

    private void handleReceived(String s) {
        final String s2 = s;
        XlogUtils.xLog("handleReceived", s);
        XlogUtils.xLog(ThermoProtocol.TAG, "handleReceived message : " + s);
        final Boolean value;
        if (value = (s2.equals(this.oldCom) ^ true)) {
            this.oldCom = s;
        }
        final int int1;
        final int n = int1 = Integer.parseInt(s.substring(8, 10), 16);
        final String s3 = s;
        s = s3.substring(10, s3.length() - 2);
        XlogUtils.xLog(ThermoProtocol.TAG, "CMD : " + int1 + " data : " + s);
        if (n != 160) {
            if (int1 != 161) {
                this.receiveError(s);
                return;
            }
            final Boolean b = value;
            final String s4 = s;
            XlogUtils.xLog(ThermoProtocol.TAG, "SEND_REQUEST data\uff1a" + s);
            final String substring = s4.substring(0, 12);
            final String substring2 = s4.substring(12, 14);
            final String s5 = s;
            s = s5.substring(s5.length() - 2);
            final int int2 = Integer.parseInt(substring2, 16);
            final String s6 = substring;
            final float n2 = (Integer.parseInt(s, 16) + 100) / 100.0f;
            this.replyMacAddressOrTime(int2, s6, new Date(System.currentTimeMillis()));
            if (b) {
                this.onDataResponseListener.onResponseDeviceInfo(substring, int2, n2);
            }
        }
        else {
            final Boolean b2 = value;
            XlogUtils.xLog(ThermoProtocol.TAG, "UPLOAD_MEASURE_DATA data\uff1a" + s);
            final ThermoMeasureData thermoMeasureData = new com.microlife_sdk.model.data.ThermoMeasureData();
            final ThermoMeasureData thermoMeasureData2 = thermoMeasureData;
            final String s7 = s;
            new ThermoMeasureData();
            thermoMeasureData.importHexString(s7);
            this.replyUploadMeasureData(129);
            if (b2) {
                this.onDataResponseListener.onResponseUploadMeasureData(thermoMeasureData2);
            }
        }
        this.receiveErrorCount = 0;
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

    private void receiveError(final String str) {
        XlogUtils.xLog(ThermoProtocol.TAG, "Receive error Count = " + this.receiveErrorCount + " message : " + str);
        ++this.receiveErrorCount;
        int length = this.allReceivedCommand.length();
        if (this.receiveErrorCount > 10) {
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
        XlogUtils.xLog(ThermoProtocol.TAG, "RECEIVED ERROR Delete message : " + (Object)this.allReceivedCommand);
    }

    public static /* synthetic */ Timer access$000(final ThermoProtocol thermoProtocol) {
        return thermoProtocol.simulationTimer;
    }

    public static /* synthetic */ Timer access$002(final ThermoProtocol thermoProtocol, final Timer simulationTimer) {
        return thermoProtocol.simulationTimer = simulationTimer;
    }

    public static /* synthetic */ String access$100() {
        return ThermoProtocol.TAG;
    }

    public static /* synthetic */ Activity access$200(final ThermoProtocol thermoProtocol) {
        return thermoProtocol.myAty;
    }

    public static /* synthetic */ String access$300(final ThermoProtocol thermoProtocol) {
        return thermoProtocol.bondMacAddress;
    }

    public static /* synthetic */ Handler access$400(final ThermoProtocol thermoProtocol) {
        return thermoProtocol.mHandler;
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
                XlogUtils.xLog(ThermoProtocol.TAG, "bondedBTReceiver BOND_NONE..");
                this.bondMacAddress = bondMacAddress;
                bluetoothDevice.createBond();
            }
            else {
                XlogUtils.xLog(ThermoProtocol.TAG, "bondedBTReceiver BOND..");
                this.myAty.unregisterReceiver(this.bondedBTReceiver);
                this.connect(bondMacAddress);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            XlogUtils.xLog(ThermoProtocol.TAG, "Exception Bond\uff1a" + ex.getMessage());
        }
    }

    public void connect(final String bondMacAddress) {
        if (this.isSimulationMode) {
            this.mHandler.removeMessages(0);
            this.cancelSimulationTimer();
            (this.simulationTimer = new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    ThermoProtocol.access$400(ThermoProtocol.this).sendEmptyMessage(101);
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

    public void replyMacAddressOrTime(final int n, String s, final Date date) {
        if (this.isSimulationMode) {
            return;
        }
        final String s2 = "01";
        if (n != 3) {
            final String tag = ThermoProtocol.TAG;
            s = "" + date.getYear() % 100;
            BaseUtils.printLog("d", tag, s);
            final String tag2 = ThermoProtocol.TAG;
            s = "" + (date.getMonth() + 1);
            BaseUtils.printLog("d", tag2, s);
            final String tag3 = ThermoProtocol.TAG;
            s = "" + date.getDate();
            BaseUtils.printLog("d", tag3, s);
            BaseUtils.printLog("d", ThermoProtocol.TAG, "");
            s = String.format("%02X", date.getYear() % 100) + String.format("%02X", date.getMonth() + 1) + String.format("%02X", date.getDate()) + String.format("%02X", date.getHours()) + String.format("%02X", date.getMinutes()) + String.format("%02X", date.getSeconds());
        }
        final String buildCmdStringForThermo = this.myBluetooth.buildCmdStringForThermo(s2, s);
        XlogUtils.xLog(ThermoProtocol.TAG, "replyMacAddressOrTime\uff1a" + buildCmdStringForThermo);
        final StringBuilder allReceivedCommand = this.allReceivedCommand;
        allReceivedCommand.delete(0, allReceivedCommand.length());
        this.myBluetooth.writeBLWMessage(buildCmdStringForThermo, true, true, false);
    }

    public void replyUploadMeasureData(final int n) {
        if (this.isSimulationMode) {
            return;
        }
        final String buildCmdStringForThermo = this.myBluetooth.buildCmdStringForThermo("81", "");
        XlogUtils.xLog(ThermoProtocol.TAG, "replyUploadMeasureData\uff1a" + buildCmdStringForThermo);
        this.myBluetooth.writeBLWMessage(buildCmdStringForThermo, true, true, false);
    }

    public String getSDKVersion() {
        return Global.sdkVersion;
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
        XlogUtils.xLog(ThermoProtocol.TAG, "connectionStatus : " + i);
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

    @Override
    public void dataResult(String s) {
        if (s.startsWith("Software_Revision_String")) {
            String var35 = s.split("==")[1];
            XlogUtils.xLog(TAG, "Software Revision String  -> " + var35);
        } else {
            if ((s = s.toUpperCase(Locale.US)).contains("==")) {
                s = s.split("==")[1];
            }

            ThermoProtocol s0000 = this;
            ThermoProtocol s0001 = this;
            ThermoProtocol s0002 = this;
            XlogUtils.xLog(TAG, "dataResult : " + s);
            Message var2;
            Message s0004 = var2 = new Message();
            var2.what = 1001;
            s0004.obj = s;
            this.mHandler.sendMessage(var2);

            Exception var40;
            label226: {
                boolean var41;
                try {
                    s0002.allReceivedCommand.append(s);
                } catch (Exception var34) {
                    var40 = var34;
                    var41 = false;
                    break label226;
                }

                String var43;
                try {
                    var43 = s0001.allReceivedCommand.toString();
                } catch (Exception var33) {
                    var40 = var33;
                    var41 = false;
                    break label226;
                }

                s = var43;

                boolean var42;
                try {
                    XlogUtils.xLog(TAG, "allReceivedCommand : " + s);
                    var42 = s0000.isCorrectHeader(var43);
                } catch (Exception var32) {
                    var40 = var32;
                    var41 = false;
                    break label226;
                }

                boolean var36 = var42;

                boolean s0003;
                String var44;
                try {
                    s0001 = this;
                    var44 = s;
                    s0003 = this.isCorrectEnd(s);
                } catch (Exception var31) {
                    var40 = var31;
                    var41 = false;
                    break label226;
                }

                boolean var3 = s0003;

                int var47;
                try {
                    var47 = s0001.getCorrectLength(var44);
                } catch (Exception var30) {
                    var40 = var30;
                    var41 = false;
                    break label226;
                }

                int var4 = var47;

                try {
                    XlogUtils.xLog(TAG, "headerCorrect : " + var36 + " endCorrect : " + var3 + " lengthCorrect : " + var4 + " message.length : " + s.length());
                } catch (Exception var29) {
                    var40 = var29;
                    var41 = false;
                    break label226;
                }

                int var46;
                if (var42 && var3) {
                    try {
                        var46 = s.length();
                    } catch (Exception var28) {
                        var40 = var28;
                        var41 = false;
                        break label226;
                    }

                    if (var46 >= var4) {
                        try {
                            this.receiveErrorCount = 0;
                            XlogUtils.xLog(TAG, "All received message -> " + s);
                        } catch (Exception var24) {
                            var40 = var24;
                            var41 = false;
                            break label226;
                        }

                        while(true) {
                            try {
                                var46 = this.allReceivedCommand.length();
                            } catch (Exception var23) {
                                var40 = var23;
                                var41 = false;
                                break label226;
                            }

                            if (var46 == 0) {
                                return;
                            }

                            try {
                                s0000 = this;
                                s0001 = this;
                                var44 = this.allReceivedCommand.toString();
                            } catch (Exception var22) {
                                var40 = var22;
                                var41 = false;
                                break label226;
                            }

                            s = var44;

                            try {
                                var47 = s0001.getCorrectLength(var44);
                            } catch (Exception var21) {
                                var40 = var21;
                                var41 = false;
                                break label226;
                            }

                            int var37 = var47;

                            String var48;
                            try {
                                var48 = s0000.allReceivedCommand.substring(0, var37);
                            } catch (Exception var20) {
                                var40 = var20;
                                var41 = false;
                                break label226;
                            }

                            s = var48;

                            try {
                                var47 = var37;
                                XlogUtils.xLog(TAG, "Start parsing message -> " + s);
                                XlogUtils.xLog(TAG, "Start parsing New lengthCorrect -> " + var37);
                            } catch (Exception s9) {
                                var40 = s9;
                                var41 = false;
                                break label226;
                            }

                            int var38;
                            var47 = var38 = var47 - 2;

                            try {
                                var48 = var48.substring(var47, var37);
                            } catch (Exception s8) {
                                var40 = s8;
                                var41 = false;
                                break label226;
                            }

                            String var39 = var48;

                            try {
                                s0001 = this;
                                var44 = s.substring(8, 10);
                            } catch (Exception s7) {
                                var40 = s7;
                                var41 = false;
                                break label226;
                            }

                            String var5 = var44;

                            try {
                                var42 = var48.equals(s0001.myBluetooth.calcChecksum(s.substring(0, 2), s.substring(2, 4), s.substring(4, 8), var5, s.substring(10, var38)));
                            } catch (Exception s6) {
                                var40 = s6;
                                var41 = false;
                                break label226;
                            }

                            if (var42) {
                                try {
                                    s0000 = this;
                                    var47 = Integer.parseInt(s.substring(8, 10), 16);
                                } catch (Exception s4) {
                                    var40 = s4;
                                    var41 = false;
                                    break label226;
                                }

                                var38 = var47;
                                var4 = 0;

                                try {
                                    var42 = s0000.isSimulationMode;
                                } catch (Exception s3) {
                                    var40 = s3;
                                    var41 = false;
                                    break label226;
                                }

                                if (!var42) {
                                    try {
                                        var46 = this.myBluetooth.getCommArraySize();
                                    } catch (Exception s2) {
                                        var40 = s2;
                                        var41 = false;
                                        break label226;
                                    }

                                    if (var46 > 0) {
                                        try {
                                            var48 = s;
                                            var47 = Integer.parseInt(this.myBluetooth.getComm(0).substring(8, 10), 16);
                                        } catch (Exception s1) {
                                            var40 = s1;
                                            var41 = false;
                                            break label226;
                                        }

                                        var38 = var47;

                                        try {
                                            var46 = Integer.parseInt(var48.substring(8, 10), 16);
                                        } catch (Exception s0) {
                                            var40 = s0;
                                            var41 = false;
                                            break label226;
                                        }

                                        var4 = var46;

                                        try {
                                            XlogUtils.xLog(TAG, "writeCmd = " + var38 + " receiveCmd = " + var4);
                                        } catch (Exception var9) {
                                            var40 = var9;
                                            var41 = false;
                                            break label226;
                                        }
                                    }
                                }

                                if (var38 == var4) {
                                    try {
                                        this.myBluetooth.sendCount = 0;
                                        this.myBluetooth.removeComm(0);
                                    } catch (Exception var8) {
                                        var40 = var8;
                                        var41 = false;
                                        break label226;
                                    }
                                }

                                try {
                                    s0000 = this;
                                    var43 = s;
                                    this.allReceivedCommand.delete(0, var37);
                                } catch (Exception var7) {
                                    var40 = var7;
                                    var41 = false;
                                    break label226;
                                }

                                try {
                                    s0000.handleReceived(var43);
                                } catch (Exception var6) {
                                    var40 = var6;
                                    var41 = false;
                                    break label226;
                                }
                            } else {
                                try {
                                    XlogUtils.xLog(TAG, "Checksum ERROR : " + var39);
                                    this.receiveError(s);
                                } catch (Exception s5) {
                                    var40 = s5;
                                    var41 = false;
                                    break label226;
                                }
                            }
                        }
                    }
                }

                int var45;
                try {
                    s0000 = this;
                    s0001 = this;
                    XlogUtils.xLog(TAG, "not finished yet message -> " + s);
                    var45 = this.receiveErrorCount;
                } catch (Exception var27) {
                    var40 = var27;
                    var41 = false;
                    break label226;
                }

                ++var45;

                try {
                    s0001.receiveErrorCount = var45;
                    var46 = s0000.receiveErrorCount;
                } catch (Exception var26) {
                    var40 = var26;
                    var41 = false;
                    break label226;
                }

                if (var46 <= 10) {
                    return;
                }

                try {
                    this.receiveError(s);
                    return;
                } catch (Exception var25) {
                    var40 = var25;
                    var41 = false;
                }
            }

            var40.printStackTrace();
            this.receiveError(s);
        }
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
        void onResponseDeviceInfo(final String p0, final int p1, final float p2);

        void onResponseUploadMeasureData(final ThermoMeasureData p0);
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
