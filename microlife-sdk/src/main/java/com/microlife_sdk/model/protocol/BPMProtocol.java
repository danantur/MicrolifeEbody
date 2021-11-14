//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.microlife_sdk.model.protocol;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import com.microlife_sdk.ideabuslibrary.util.BaseUtils;
import com.microlife_sdk.model.XlogUtils;
import com.microlife_sdk.model.bluetooth.MyBluetoothLE;
import com.microlife_sdk.model.bluetooth.BluetoothLEClass.OnIMBluetoothLEListener;
import com.microlife_sdk.model.bluetooth.MyBluetoothLE.OnWriteStateListener;
import com.microlife_sdk.model.data.CurrentAndMData;
import com.microlife_sdk.model.data.DRecord;
import com.microlife_sdk.model.data.DeviceInfo;
import com.microlife_sdk.model.data.User;
import com.microlife_sdk.model.data.VersionData;
import com.microlife_sdk.model.abcdef.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BPMProtocol implements OnIMBluetoothLEListener, OnWriteStateListener {
    public static final String TAG = "BPMProtocol";
    public static BPMProtocol protocol;
    public static final int RECEIVED_ERROR_COUNT = 10000;
    public static final int READ_HISTORY = 0;
    public static final int CLEAR_HISTORY = 3;
    public static final int DISCONNECT_BLE = 4;
    public static final int READ_USER_ID_AND_VERSION_DATA = 5;
    public static final int WRITE_USER_ID = 6;
    public static final int Read_Last_Data = 7;
    public static final int Clear_Last_Data = 8;
    public static final int READ_DEVICE_INFO = 11;
    public static final int READ_DEVICE_TIME = 12;
    public static final int WRITE_DEVICE_INFO = 13;
    public static final int USER_ID_LENGTH = 11;
    public static final int DEVICE_ID_LENGTH = 6;
    public static final String CMD_REPLY_RESULT_SUCCESS = "81";
    public static final String CMD_REPLY_RESULT_NACK = "91";
    public static final String CMD_REPLY_RESULT_NoData = "92";
    public static final String SPACE_ASSCII = "20";
    public MyBluetoothLE myBluetooth;
    public int receiveErrorCount = 0;
    public StringBuilder allReceivedCommand;
    public String bondMacAddress = "";
    public Activity myAty;
    public boolean isSimulationMode = false;
    public int position = 0;
    public Timer simulationTimer;
    public int origin = 49;
    public int userNO = 0;
    public boolean isSuccess;
    public String oldCom = "";
    public List<String> targetDeviceNames;
    public BPMProtocol.ConnectState mConnectState;
    public Handler mHandler;
    public OnWriteStateListener mOnWriteStateListener;
    public BPMProtocol.OnNotifyStateListener mOnNotifyStateListener;
    public BPMProtocol.OnConnectStateListener onConnectStateListener;
    public BPMProtocol.OnDataResponseListener onDataResponseListener;
    public BroadcastReceiver bondedBTReceiver;

    public static BPMProtocol getInstance(Activity var0, boolean var1, boolean var2, String var3) {
        XlogUtils.initXlog(var0, var2);
        if (protocol == null) {
            protocol = new BPMProtocol(var0, var1, var2);
        }

        return protocol;
    }

    public static BPMProtocol getInstance(MyBluetoothLE var0, boolean var1, boolean var2, String var3) {
        if (protocol == null) {
            protocol = new BPMProtocol(var0, var1, var2);
        }

        return protocol;
    }

    public BPMProtocol(MyBluetoothLE var1, boolean var2, boolean var3) {
        this.mHandler = new NamelessClass_2();
        this.bondedBTReceiver = new NamelessClass_1();
        this.isSimulationMode = var2;
        if (!this.isSimulationMode) {
            this.allReceivedCommand = new StringBuilder();
            this.myBluetooth = var1;
        }
    }

    public BPMProtocol(Activity var1, boolean var2, boolean var3) {

        this.mHandler = new NamelessClass_2();

        this.bondedBTReceiver = new NamelessClass_1();
        BaseUtils.setIsPrintLog(var3);
        this.isSimulationMode = var2;
        if (!this.isSimulationMode) {
            this.targetDeviceNames = new ArrayList();
            this.targetDeviceNames.add("A6 BT");
            this.targetDeviceNames.add("A6 BASIS PLUS BT");
            this.targetDeviceNames.add("A7 Touch BT");
            this.targetDeviceNames.add("B3 BT");
            this.targetDeviceNames.add("B6 Connect");
            this.targetDeviceNames.add("A6");
            this.targetDeviceNames.add("B6");
            this.targetDeviceNames.add("Progress");
            if (this.myBluetooth == null) {
                this.myBluetooth = MyBluetoothLE.getInstance(var1, var3, 600);
                this.myAty = var1;
            }

            MyBluetoothLE var4;
            if ((var4 = this.myBluetooth) != null) {
                var4.setOnIMBluetoothLEListener(this);
                this.myBluetooth.setOnWriteStateListener(this);
            }

            this.allReceivedCommand = new StringBuilder();
        }
    }

    private String appendSpaceCharHexStr(String var1, int var2) {
        StringBuilder var3;
        var3 = new StringBuilder(var1);

        for(int var4 = 0; var4 < var2; ++var4) {
            var3.append("20");
        }

        return var3.toString();
    }

    @NonNull
    private String getUserNo() {
        String var1 = "FD";
        if (this.userNO == 1) {
            var1 = "01";
        }

        if (this.userNO == 2) {
            var1 = "02";
        }

        return var1;
    }

    private String convertAsciiStringToHexString(@NonNull String var1) {
        StringBuilder var4;
        var4 = new StringBuilder();
        char[] var5;
        int var2 = (var5 = var1.toCharArray()).length;

        for(int var3 = 0; var3 < var2; ++var3) {
            var4.append(Integer.toHexString(var5[var3]));
        }

        return var4.toString();
    }

    public static String convertBytesToString(byte[] var0) {
        StringBuilder var1;
        var1 = new StringBuilder("");
        if (var0 != null && var0.length > 0) {
            for(int var2 = 0; var2 < var0.length; ++var2) {
                var1.append(BaseUtils.getFillString(var0[var2], 2, "0"));
            }

            return var1.toString();
        } else {
            return null;
        }
    }

    public static Uri getLogZip(String var0) {
        return XlogUtils.getLogZip(var0);
    }

    public static void sendSupportMail(String var0, String var1, String var2) {
        XlogUtils.sendSupportMail(var0, var1, var2);
    }

    private void startSimulationTimer() {
        this.cancelSimulationTimer();
        this.position = 0;
        this.simulationTimer = new Timer();
        this.simulationTimer.schedule(new TimerTask() {
            public void run() {
                BPMProtocol.this.mHandler.sendEmptyMessage(100);
            }
        }, 100L, 700L);
    }

    private void cancelSimulationTimer() {
        Timer var1;
        if ((var1 = this.simulationTimer) != null) {
            var1.cancel();
            this.simulationTimer = null;
        }

    }

    private void handleReceived(String var1) {
        XlogUtils.xLog(TAG, "handleReceived message : " + var1);
        Boolean var2;
        if (var2 = var1.equals(this.oldCom) ^ true) {
            this.oldCom = var1;
        }

        int var3 = Integer.parseInt(var1.substring(8, 10), 16);
        this.origin = Integer.parseInt(var1.substring(2, 4), 16);
        String var4;
        if (this.origin == 49) {
            var4 = "3G";
        } else {
            var4 = "4G";
        }

        String var10000 = var1 = var1.substring(10, var1.length() - 2);
        XlogUtils.xLog(TAG, "CMD : " + var3 + " origin : " + var4 + " data : " + var1);
        boolean var16;
        if (!var10000.equals("81") && !var1.equals("92")) {
            var16 = false;
        } else {
            var16 = true;
        }

        DeviceInfo var8;
        String var20;
        e var21;
        switch(var3) {
            case 0:
                if (var16) {
                    this.readHistorysOrCurrDataAndSyncTiming();
                    return;
                }

                var20 = var1;
                DRecord var15;
                DRecord var26 = var15 = new DRecord();
                d.a(var20, var26, this.origin);
                if (var2) {
                    this.onDataResponseListener.onResponseReadHistory(var15);
                }
                break;
            case 1:
            case 2:
            case 9:
            case 10:
            default:
                this.receiveError(var1);
                return;
            case 3:
                this.onDataResponseListener.onResponseClearHistory(var16);
            case 4:
                break;
            case 5:
                if (var16) {
                    this.readUserAndVersionData();
                    return;
                }

                e var13;
                var13 = new e(var1);
                byte var11;
                if (this.origin == 49) {
                    var11 = 26;
                } else {
                    var11 = 86;
                }

                var1 = var13.a(var11);
                e var18;
                var18 = new e(var1);
                User var14;
                User var23 = var14 = new User();
                var23.setNO(var18.c(2));
                this.userNO = var23.getNO();
                if (this.origin == 49) {
                    var14.setID(var18.b(22));
                    var14.setAge(var18.c(2));
                } else {
                    var21 = var18;
                    e var24 = var18;
                    e var10003 = var18;
                    var4 = var18.b(40);
                    int var17 = var10003.c(2);
                    String var6 = var24.b(40);
                    int var7 = var21.c(2);
                    if (this.userNO == 1) {
                        var6 = var4;
                    }

                    var14.setID(var6);
                    if (this.userNO == 1) {
                        var7 = var17;
                    }

                    var14.setAge(var7);
                }

                Boolean var19 = var2;
                String var25 = var13.a(var13.a());
                VersionData var12;
                VersionData var27 = var12 = new VersionData();
                d.a(var25, var27);
                this.syncTiming();
                if (var19) {
                    this.onDataResponseListener.onResponseReadUserAndVersionData(var14, var12);
                }
                break;
            case 6:
                this.onDataResponseListener.onResponseWriteUser(var16);
                break;
            case 7:
                if (var2) {
                    if (var16) {
                        this.onDataResponseListener.onResponseReadLastData((CurrentAndMData)null, 0, 0, 0, true);
                    } else {
                        var21 = new e(var1);
                        int var9 = var21.c(2);
                        int var10 = var21.c(2);
                        var3 = var21.c(2);
                        var21.a(8);
                        var4 = var21.a(var21.a());
                        CurrentAndMData var5;
                        CurrentAndMData var22 = var5 = new CurrentAndMData();
                        var22.importHexString(var4, this.origin);
                        this.onDataResponseListener.onResponseReadLastData(var5, var9, var10, var3, false);
                    }
                }
                break;
            case 8:
                this.onDataResponseListener.onResponseClearLastData(var16);
                break;
            case 11:
                if (var16) {
                    this.readDeviceInfo();
                    return;
                }

                var20 = var1;
                DeviceInfo var10002 = var8 = new DeviceInfo();
                d.a(var20, var10002, this.origin);
                if (var2) {
                    this.onDataResponseListener.onResponseReadDeviceInfo(var8);
                }
                break;
            case 12:
                if (var16) {
                    this.readDeviceTime();
                    return;
                }

                var10000 = var1;
                DeviceInfo var10001 = var8 = new DeviceInfo();
                d.a(var10000, var10001);
                if (!var8.getisTimeReady()) {
                    this.syncTiming();
                }

                if (var2) {
                    this.onDataResponseListener.onResponseReadDeviceTime(var8);
                }
                break;
            case 13:
                this.onDataResponseListener.onResponseWriteDeviceTime(var16);
        }

        this.receiveErrorCount = 0;
    }

    private boolean isCorrectHeader(String var1) {
        return var1.startsWith("4D");
    }

    private boolean isCorrectEnd(String var1) {
        var1.endsWith("-1");
        return true;
    }

    private int getCorrectLength(String var1) {
        int var2 = 0;
        if (var1.length() > 8) {
            var2 = Integer.parseInt(var1.substring(4, 8), 16);
        }

        return (var2 + 4) * 2;
    }

    private void receiveError(String var1) {
        XlogUtils.xLog(TAG, "Receive error Count = " + this.receiveErrorCount + " message : " + var1);
        int var10003 = this.receiveErrorCount++;
        int var2 = this.allReceivedCommand.length();
        if (this.receiveErrorCount > 10000) {
            this.receiveErrorCount = 0;
            this.allReceivedCommand.delete(0, var2);
        } else {
            int var5;
            int var10000 = var5 = var1.indexOf("-14D");
            StringBuilder var3 = this.allReceivedCommand;
            byte var4 = 0;
            if (var10000 != -1) {
                var2 = var5 + 2;
            }

            var3.delete(var4, var2);
        }

        XlogUtils.xLog(TAG, "RECEIVED ERROR Delete message : " + this.allReceivedCommand);
    }

    public void setOnWriteStateListener(OnWriteStateListener var1) {
        this.mOnWriteStateListener = var1;
    }

    public void setOnNotifyStateListener(BPMProtocol.OnNotifyStateListener var1) {
        this.mOnNotifyStateListener = var1;
    }

    public void setOnConnectStateListener(BPMProtocol.OnConnectStateListener var1) {
        this.onConnectStateListener = var1;
    }

    public void setOnDataResponseListener(BPMProtocol.OnDataResponseListener var1) {
        this.onDataResponseListener = var1;
    }

    public void readRSSI() {
        if (this.isSimulationMode) {
            this.cancelSimulationTimer();
        } else {
            this.myBluetooth.readRSSI();
        }
    }

    public boolean isSupportBluetooth(Activity var1) {
        return this.isSimulationMode || this.myBluetooth.isSupportBluetooth(var1);
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

    public void startScan(int var1) {
        if (this.isSimulationMode) {
            this.startSimulationTimer();
        } else {
            this.myBluetooth.startLEScan(var1, false);
        }
    }

    public void stopScan() {
        if (this.isSimulationMode) {
            this.cancelSimulationTimer();
        } else {
            this.myBluetooth.stopLEScan();
        }
    }

    public void bond(String var1) {
        this.myAty.registerReceiver(this.bondedBTReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
        BluetoothDevice var2;
        BluetoothDevice var10000 = var2 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(var1);
        XlogUtils.xLog(TAG, "bondedBTReceiver BondState：" + var2.getBondState());


        Exception var7 = null;
        int var8 = 0;
        try {
            var8 = var10000.getBondState();
        } catch (Exception var5) {
            var7 = var5;
        }

        if (var8 == 10) {
            try {
                XlogUtils.xLog(TAG, "bondedBTReceiver BOND_NONE..");
                this.bondMacAddress = var1;
                var2.createBond();
                return;
            } catch (Exception var3) {
                var7 = var3;
            }
        }
        if (var7 != null) {
            var7.printStackTrace();
            XlogUtils.xLog(TAG, "Exception Bond：" + var7.getMessage());
        }
    }

    public void connect(String var1) {
        if (this.isSimulationMode) {
            this.mHandler.removeMessages(0);
            this.cancelSimulationTimer();
            this.simulationTimer = new Timer();
            this.simulationTimer.schedule(new TimerTask() {
                public void run() {
                    BPMProtocol.this.mHandler.sendEmptyMessage(101);
                }
            }, 1500L);
        } else {
            BPMProtocol var10000 = this;
            this.bondMacAddress = var1;
            this.myBluetooth.sendCount = 0;
            MyBluetoothLE.MY_UUID_WRITE = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
            MyBluetoothLE.MY_UUID_NOTIFY = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
            ArrayList var2;
            ArrayList var10001 = var2 = new ArrayList();
            var10001.add(var1);
            var10000.myBluetooth.connect(var2);
        }
    }

    public void disconnect() {
        if (this.isSimulationMode) {
            this.onConnectStateListener.onConnectionState(BPMProtocol.ConnectState.Disconnect);
        } else {
            this.myBluetooth.disconnect(18);
        }
    }

    public void readHistorysOrCurrDataAndSyncTiming() {
        if (!this.isSimulationMode) {
            Calendar var1 = Calendar.getInstance();
            String var2 = "00";
            Object[] var10000;
            Object[] var3;
            String var5;
            if (this.origin == 49) {
                var10000 = var3 = new Object[6];
                var3[0] = var1.get(1) % 100;
                var3[1] = var1.get(2) + 1;
                var3[2] = var1.get(5);
                var3[3] = var1.get(11);
                var3[4] = var1.get(12);
                var10000[5] = var1.get(13);
                var5 = String.format("%02X%02X%02X%02X%02X%02X", var3);
            } else {
                var10000 = var3 = new Object[6];
                var3[0] = var1.get(1) % 100;
                var3[1] = var1.get(2) + 1;
                var3[2] = var1.get(5);
                var3[3] = var1.get(11);
                var3[4] = var1.get(12);
                var10000[5] = var1.get(13);
                var5 = String.format("%02X%02X%02X%02X%02X%02X", var3);
                var5 = var5 + this.getUserNo();
            }

            BPMProtocol var6 = this;
            BPMProtocol var10001 = this;
            String var4 = this.myBluetooth.buildCmdString(var2, var5);
            XlogUtils.xLog(TAG, "readHistorysOrCurrDataAndSyncTiming : " + var4);
            StringBuilder var7 = var10001.allReceivedCommand;
            var7.delete(0, var7.length());
            var6.myBluetooth.writeBLWMessage(var4, false, false, true);
        }
    }

    public void clearAllHistorys() {
        if (!this.isSimulationMode) {
            String var1 = "03";
            String var2;
            if (this.origin == 49) {
                var2 = "";
            } else {
                var2 = this.getUserNo();
            }

            BPMProtocol var10000 = this;
            BPMProtocol var10001 = this;
            String var3 = this.myBluetooth.buildCmdString(var1, var2);
            XlogUtils.xLog(TAG, "clearAllHistorys : " + var3);
            StringBuilder var4 = var10001.allReceivedCommand;
            var4.delete(0, var4.length());
            var10000.myBluetooth.writeBLWMessage(var3, false, false, false);
        }
    }

    public void disconnectBPM() {
        if (!this.isSimulationMode) {
            BPMProtocol var10000 = this;
            BPMProtocol var10001 = this;
            String var1 = this.myBluetooth.buildCmdString("04", "");
            XlogUtils.xLog(TAG, "disconnectBPM : " + var1);
            StringBuilder var2 = var10001.allReceivedCommand;
            var2.delete(0, var2.length());
            var10000.myBluetooth.writeBLWMessage(var1, true, true, false);
        }
    }

    public void readUserAndVersionData() {
        if (!this.isSimulationMode) {
            BPMProtocol var10000 = this;
            BPMProtocol var10001 = this;
            String var1 = this.myBluetooth.buildCmdString("05", "");
            XlogUtils.xLog(TAG, "readUserAndVersionData : " + var1);
            StringBuilder var2 = var10001.allReceivedCommand;
            var2.delete(0, var2.length());
            var10000.myBluetooth.writeBLWMessage(var1, false, false, false);
        }
    }

    public void writeUserData(String var1, int var2) {
        if (!this.isSimulationMode) {
            String var3 = "06";
            byte var4;
            if (this.origin == 49) {
                var4 = 11;
            } else {
                var4 = 20;
            }

            String var5 = (new e(var1)).a(var4, false);
            StringBuilder var10000;
            Object[] var6;
            if (this.origin == 49) {
                var10000 = (new StringBuilder()).append(var5);
                (var6 = new Object[1])[0] = var2;
                var5 = var10000.append(String.format("%02X", var6)).toString();
            } else {
                var10000 = (new StringBuilder()).append(this.getUserNo()).append(var5);
                (var6 = new Object[1])[0] = var2;
                var5 = var10000.append(String.format("%02X", var6)).toString();
            }

            var3 = this.myBluetooth.buildCmdString(var3, var5);
            XlogUtils.xLog(TAG, "writeUserData : origin:" + this.origin + " ID:" + var1 + " age:" + var2);
            XlogUtils.xLog(TAG, "writeUserData : " + var3);
            StringBuilder var10001 = this.allReceivedCommand;
            var10001.delete(0, var10001.length());
            this.myBluetooth.writeBLWMessage(var3, false, false, false);
        }
    }

    public void readLastData() {
        if (!this.isSimulationMode && this.origin == 49) {
            BPMProtocol var10000 = this;
            BPMProtocol var10001 = this;
            String var1 = this.myBluetooth.buildCmdString("07", "");
            XlogUtils.xLog(TAG, "readLastData : " + var1);
            StringBuilder var2 = var10001.allReceivedCommand;
            var2.delete(0, var2.length());
            var10000.myBluetooth.writeBLWMessage(var1, false, true, false);
        }
    }

    public void clearLastData() {
        if (!this.isSimulationMode && this.origin == 49) {
            BPMProtocol var10000 = this;
            BPMProtocol var10001 = this;
            String var1 = this.myBluetooth.buildCmdString("08", "");
            XlogUtils.xLog(TAG, "clearLastData : " + var1);
            StringBuilder var2 = var10001.allReceivedCommand;
            var2.delete(0, var2.length());
            var10000.myBluetooth.writeBLWMessage(var1, false, true, false);
        }
    }

    public void readDeviceInfo() {
        if (!this.isSimulationMode) {
            BPMProtocol var10000 = this;
            BPMProtocol var10001 = this;
            String var1 = this.myBluetooth.buildCmdString("0B", "");
            XlogUtils.xLog(TAG, "readDeviceInfo : " + var1);
            StringBuilder var2 = var10001.allReceivedCommand;
            var2.delete(0, var2.length());
            var10000.myBluetooth.writeBLWMessage(var1, false, true, false);
        }
    }

    public void readDeviceTime() {
        if (!this.isSimulationMode && this.origin != 49) {
            BPMProtocol var10000 = this;
            BPMProtocol var10001 = this;
            String var1 = this.myBluetooth.buildCmdString("0C", "");
            XlogUtils.xLog(TAG, "readDeviceTime : " + var1);
            StringBuilder var2 = var10001.allReceivedCommand;
            var2.delete(0, var2.length());
            var10000.myBluetooth.writeBLWMessage(var1, false, false, false);
        }
    }

    public void syncTiming() {
        if (!this.isSimulationMode && this.origin != 49) {
            BPMProtocol var10000 = this;
            BPMProtocol var10001 = this;
            BPMProtocol var10002 = this;
            Calendar var2 = Calendar.getInstance();
            Object[] var1;
            Object[] var10003 = var1 = new Object[6];
            var1[0] = var2.get(1) % 100;
            var1[1] = var2.get(2) + 1;
            var1[2] = var2.get(5);
            var1[3] = var2.get(11);
            var1[4] = var2.get(12);
            var10003[5] = var2.get(13);
            String var3 = String.format("%02X%02X%02X%02X%02X%02X", var1);
            var3 = var10002.myBluetooth.buildCmdString("0D", var3);
            XlogUtils.xLog(TAG, "syncTiming：" + var3);
            StringBuilder var4 = var10001.allReceivedCommand;
            var4.delete(0, var4.length());
            var10000.myBluetooth.writeBLWMessage(var3, false, false, false);
        }
    }

    public void onWriteMessage(boolean var1, String var2) {
        BPMProtocol var10000 = this;
        Message var3;
        Message var10001 = var3 = new Message();
        var3.what = 1000;
        var3.arg1 = (var1 ? 1 : 0);
        var10001.obj = var2;
        var10000.mHandler.sendMessage(var3);
    }

    public void onBtStateChanged(boolean var1) {
        this.onConnectStateListener.onBtStateChanged(var1);
    }

    public void scanResult(String var1, String var2, int var3, byte[] var4) {
        Iterator var5 = this.targetDeviceNames.iterator();

        while(var5.hasNext()) {
            if (var2.startsWith((String)var5.next())) {
                this.onConnectStateListener.onScanResult(var1, var2, var3);
            }
        }

    }

    public void connectionStatus(int var1) {
        XlogUtils.xLog(TAG, "connectionStatus : " + var1);
        if (var1 != 4) {
            if (var1 != 20) {
                if (var1 != 17) {
                    if (var1 == 18) {
                        this.mConnectState = BPMProtocol.ConnectState.Disconnect;
                    }
                } else {
                    this.mConnectState = BPMProtocol.ConnectState.Connected;
                    this.readUserAndVersionData();
                }
            } else {
                this.mConnectState = BPMProtocol.ConnectState.ConnectTimeout;
            }
        } else {
            this.mConnectState = BPMProtocol.ConnectState.ScanFinish;
        }

        this.onConnectStateListener.onConnectionState(this.mConnectState);
    }

    public String getSDKVersion() {
        return Global.sdkVersion;
    }

    public void dataResult(String var1) {
        if (var1.startsWith("Software_Revision_String")) {
            String var44 = var1.split("==")[1];
            XlogUtils.xLog(TAG, "Software Revision String  -> " + var44);
        } else {
            if ((var1 = var1.toUpperCase(Locale.US)).contains("==")) {
                var1 = var1.split("==")[1];
            }

            BPMProtocol var10000 = this;
            BPMProtocol var10001 = this;
            BPMProtocol var10002 = this;
            Message var2;
            Message var10004 = var2 = new Message();
            var2.what = 1001;
            var10004.obj = var1;
            this.mHandler.sendMessage(var2);
            XlogUtils.xLog(TAG, "dataResult : " + var1);

            Exception var51;
            label294: {
                boolean var52;
                try {
                    var10002.allReceivedCommand.append(var1);
                } catch (Exception var43) {
                    var51 = var43;
                    var52 = false;
                    break label294;
                }

                String var54;
                try {
                    var54 = var10001.allReceivedCommand.toString();
                } catch (Exception var42) {
                    var51 = var42;
                    var52 = false;
                    break label294;
                }

                var1 = var54;

                boolean var53;
                try {
                    XlogUtils.xLog(TAG, "allReceivedCommand : " + var1);
                    var53 = var10000.isCorrectHeader(var54);
                } catch (Exception var41) {
                    var51 = var41;
                    var52 = false;
                    break label294;
                }

                boolean var45 = var53;

                boolean var10003;
                String var55;
                try {
                    var10001 = this;
                    var55 = var1;
                    var10003 = this.isCorrectEnd(var1);
                } catch (Exception var40) {
                    var51 = var40;
                    var52 = false;
                    break label294;
                }

                boolean var3 = var10003;

                int var59;
                try {
                    var59 = var10001.getCorrectLength(var55);
                } catch (Exception var39) {
                    var51 = var39;
                    var52 = false;
                    break label294;
                }

                int var4 = var59;

                try {
                    XlogUtils.xLog(TAG, "headerCorrect : " + var45 + " endCorrect : " + var3 + " lengthCorrect : " + var4 + " message.length : " + var1.length());
                } catch (Exception var38) {
                    var51 = var38;
                    var52 = false;
                    break label294;
                }

                int var56;
                int var57;
                if (var53 && var3) {
                    try {
                        var57 = var1.length();
                    } catch (Exception var37) {
                        var51 = var37;
                        var52 = false;
                        break label294;
                    }

                    if (var57 >= var4) {
                        try {
                            this.receiveErrorCount = 0;
                            XlogUtils.xLog(TAG, "All received message -> " + var1);
                        } catch (Exception var33) {
                            var51 = var33;
                            var52 = false;
                            break label294;
                        }

                        while(true) {
                            try {
                                var57 = this.allReceivedCommand.length();
                            } catch (Exception var32) {
                                var51 = var32;
                                var52 = false;
                                break label294;
                            }

                            if (var57 == 0) {
                                return;
                            }

                            try {
                                var10000 = this;
                                var10001 = this;
                                var55 = this.allReceivedCommand.toString();
                            } catch (Exception var31) {
                                var51 = var31;
                                var52 = false;
                                break label294;
                            }

                            var1 = var55;

                            try {
                                var59 = var10001.getCorrectLength(var55);
                            } catch (Exception var30) {
                                var51 = var30;
                                var52 = false;
                                break label294;
                            }

                            int var46 = var59;

                            String var62;
                            try {
                                var62 = var10000.allReceivedCommand.substring(0, var46);
                            } catch (Exception var29) {
                                var51 = var29;
                                var52 = false;
                                break label294;
                            }

                            var54 = var1 = var62;

                            try {
                                var56 = var46;
                                XlogUtils.xLog(TAG, "Start parsing message -> " + var1);
                                XlogUtils.xLog(TAG, "Start parsing New lengthCorrect -> " + var46);
                            } catch (Exception var28) {
                                var51 = var28;
                                var52 = false;
                                break label294;
                            }

                            int var47;
                            var56 = var47 = var56 - 2;

                            try {
                                var54 = var54.substring(var56, var46);
                            } catch (Exception var27) {
                                var51 = var27;
                                var52 = false;
                                break label294;
                            }

                            String var49 = var54;

                            try {
                                var62 = var62.substring(8, 10);
                            } catch (Exception var26) {
                                var51 = var26;
                                var52 = false;
                                break label294;
                            }

                            String var5 = var62;

                            try {
                                XlogUtils.xLog(TAG, "cmd -> " + var1);
                                var53 = var62.equals("91");
                            } catch (Exception var25) {
                                var51 = var25;
                                var52 = false;
                                break label294;
                            }

                            label240: {
                                if (!var53) {
                                    try {
                                        var53 = var5.equals("81");
                                    } catch (Exception var24) {
                                        var51 = var24;
                                        var52 = false;
                                        break label294;
                                    }

                                    if (!var53) {
                                        try {
                                            var62 = this.myBluetooth.calcChecksum(var1.substring(0, 2), var1.substring(2, 4), var1.substring(4, 8), var5, var1.substring(10, var47));
                                            break label240;
                                        } catch (Exception var23) {
                                            var51 = var23;
                                            var52 = false;
                                            break label294;
                                        }
                                    }
                                }

                                try {
                                    var62 = this.myBluetooth.calcChecksum(var1.substring(0, 2), var1.substring(2, 4), var1.substring(4, 8), "00", var5);
                                } catch (Exception var22) {
                                    var51 = var22;
                                    var52 = false;
                                    break label294;
                                }
                            }

                            String var48 = var62;

                            try {
                                var62 = var49;
                                var54 = var48;
                                XlogUtils.xLog(TAG, "receiveChecksum = " + var49 + " calcChecksum = " + var48);
                                var56 = Integer.parseInt(var1.substring(8, 10), 16);
                            } catch (Exception var21) {
                                var51 = var21;
                                var52 = false;
                                break label294;
                            }

                            var47 = var56;
                            int var50 = 0;

                            try {
                                XlogUtils.xLog(TAG, "writeCmd = " + var47 + " receiveCmd = " + var50);
                                var53 = var62.equals(var54);
                            } catch (Exception var20) {
                                var51 = var20;
                                var52 = false;
                                break label294;
                            }

                            if (var53) {
                                try {
                                    var53 = this.isSimulationMode;
                                } catch (Exception var18) {
                                    var51 = var18;
                                    var52 = false;
                                    break label294;
                                }

                                if (!var53) {
                                    try {
                                        var57 = this.myBluetooth.getCommArraySize();
                                    } catch (Exception var17) {
                                        var51 = var17;
                                        var52 = false;
                                        break label294;
                                    }

                                    if (var57 > 0) {
                                        try {
                                            var62 = var1;
                                            var59 = Integer.parseInt(this.myBluetooth.getComm(0).substring(8, 10), 16);
                                        } catch (Exception var16) {
                                            var51 = var16;
                                            var52 = false;
                                            break label294;
                                        }

                                        var47 = var59;

                                        try {
                                            var57 = Integer.parseInt(var62.substring(8, 10), 16);
                                        } catch (Exception var15) {
                                            var51 = var15;
                                            var52 = false;
                                            break label294;
                                        }

                                        var50 = var57;
                                    }
                                }

                                if (var50 == 145) {
                                    try {
                                        this.allReceivedCommand.delete(0, var46);
                                    } catch (Exception var6) {
                                        var51 = var6;
                                        var52 = false;
                                        break label294;
                                    }
                                } else if (var50 != 129 && var50 != 146) {
                                    if (var47 == var50) {
                                        try {
                                            this.myBluetooth.sendCount = 0;
                                            this.myBluetooth.removeComm(0);
                                        } catch (Exception var9) {
                                            var51 = var9;
                                            var52 = false;
                                            break label294;
                                        }
                                    }

                                    try {
                                        var10000 = this;
                                        var54 = var1;
                                        this.allReceivedCommand.delete(0, var46);
                                    } catch (Exception var8) {
                                        var51 = var8;
                                        var52 = false;
                                        break label294;
                                    }

                                    try {
                                        var10000.handleReceived(var54);
                                    } catch (Exception var7) {
                                        var51 = var7;
                                        var52 = false;
                                        break label294;
                                    }
                                } else {
                                    try {
                                        var10000 = this;
                                        this.myBluetooth.sendCount = 0;
                                        this.myBluetooth.removeComm(0);
                                        this.allReceivedCommand.delete(0, var46);
                                    } catch (Exception var14) {
                                        var51 = var14;
                                        var52 = false;
                                        break label294;
                                    }

                                    StringBuilder var61;
                                    StringBuilder var63;
                                    try {
                                        var63 = new StringBuilder();
                                        var61 = var63;
                                    } catch (Exception var13) {
                                        var51 = var13;
                                        var52 = false;
                                        break label294;
                                    }

                                    byte var58 = 8;
                                    String var60 = "%02X";

                                    Object[] var10005;
                                    try {
                                        var10005 = new Object[1];
                                    } catch (Exception var12) {
                                        var51 = var12;
                                        var52 = false;
                                        break label294;
                                    }

                                    Object[] var10006 = var10005;
                                    byte var10007 = 0;

                                    try {
                                        var10006[var10007] = var47;
                                        var61.insert(var58, String.format(var60, var10005));
                                    } catch (Exception var11) {
                                        var51 = var11;
                                        var52 = false;
                                        break label294;
                                    }

                                    try {
                                        var10000.handleReceived(var63.toString());
                                    } catch (Exception var10) {
                                        var51 = var10;
                                        var52 = false;
                                        break label294;
                                    }
                                }
                            } else {
                                try {
                                    XlogUtils.xLog(TAG, "Checksum ERROR : " + var49);
                                    this.receiveError(var1);
                                } catch (Exception var19) {
                                    var51 = var19;
                                    var52 = false;
                                    break label294;
                                }
                            }
                        }
                    }
                }

                try {
                    var10000 = this;
                    var10001 = this;
                    XlogUtils.xLog(TAG, "not finished yet message -> " + var1);
                    var56 = this.receiveErrorCount;
                } catch (Exception var36) {
                    var51 = var36;
                    var52 = false;
                    break label294;
                }

                ++var56;

                try {
                    var10001.receiveErrorCount = var56;
                    var57 = var10000.receiveErrorCount;
                } catch (Exception var35) {
                    var51 = var35;
                    var52 = false;
                    break label294;
                }

                if (var57 <= 10000) {
                    return;
                }

                try {
                    this.receiveError(var1);
                    return;
                } catch (Exception var34) {
                    var51 = var34;
                    var52 = false;
                }
            }

            var51.printStackTrace();
            this.receiveError(var1);
        }
    }

    public interface OnDataResponseListener {
        void onResponseReadHistory(DRecord var1);

        void onResponseClearHistory(boolean var1);

        void onResponseReadUserAndVersionData(User var1, VersionData var2);

        void onResponseWriteUser(boolean var1);

        void onResponseReadLastData(CurrentAndMData var1, int var2, int var3, int var4, boolean var5);

        void onResponseClearLastData(boolean var1);

        void onResponseReadDeviceInfo(DeviceInfo var1);

        void onResponseReadDeviceTime(DeviceInfo var1);

        void onResponseWriteDeviceTime(boolean var1);
    }

    public interface OnConnectStateListener {
        void onBtStateChanged(boolean var1);

        void onScanResult(String var1, String var2, int var3);

        void onConnectionState(BPMProtocol.ConnectState var1);
    }

    public interface OnNotifyStateListener {
        void onNotifyMessage(String var1);
    }

    public static enum ConnectState {
        ScanFinish,
        Connected,
        Disconnect,
        ConnectTimeout;

        ConnectState() {
        }
    }

    class NamelessClass_2 extends Handler {
        public void handleMessage(Message var1) {
            int var2;
            if ((var2 = var1.what) != 0) {
                if (var2 != 100) {
                    if (var2 != 101) {
                        if (var2 != 1000) {
                            BPMProtocol.OnNotifyStateListener var3;
                            if (var2 == 1001 && (var3 = BPMProtocol.this.mOnNotifyStateListener) != null) {
                                var3.onNotifyMessage((String)var1.obj);
                            }
                        } else {
                            OnWriteStateListener var4;
                            if ((var4 = BPMProtocol.this.mOnWriteStateListener) != null) {
                                boolean var7;
                                if (var1.arg1 == 1) {
                                    var7 = true;
                                } else {
                                    var7 = false;
                                }

                                var4.onWriteMessage(var7, (String)var1.obj);
                            }
                        }
                    } else {
                        BPMProtocol.this.connectionStatus(17);
                    }
                } else {
                    BPMProtocol var10000 = BPMProtocol.this;
                    ++var10000.position;
                    BPMProtocol.OnConnectStateListener var5;
                    if ((var5 = var10000.onConnectStateListener) != null) {
                        var5.onScanResult("abcde12345678" + BPMProtocol.this.position, "Fuel " + BPMProtocol.this.position, BPMProtocol.this.position + -40);
                    }

                    BPMProtocol var6;
                    if ((var6 = BPMProtocol.this).position >= 10) {
                        var6.simulationTimer.cancel();
                        BPMProtocol.this.simulationTimer = null;
                    }
                }
            } else {
                BPMProtocol.this.dataResult((String)var1.obj);
            }

        }
    }

    class NamelessClass_1 extends BroadcastReceiver {
        public NamelessClass_1() {
        }

        public void onReceive(Context var1, Intent var2) {
            switch(((BluetoothDevice)var2.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getBondState()) {
                case 10:
                    XlogUtils.xLog(BPMProtocol.TAG, "bondedBTReceiver can't bond...");
                    break;
                case 11:
                    XlogUtils.xLog(BPMProtocol.TAG, "bondedBTReceiver bonding...");
                    break;
                case 12:
                    XlogUtils.xLog(BPMProtocol.TAG, "bondedBTReceiver bonded...");
                    BPMProtocol.this.myAty.unregisterReceiver(BPMProtocol.this.bondedBTReceiver);
                    BPMProtocol var10000 = BPMProtocol.this;
                    var10000.connect(var10000.bondMacAddress);
            }

        }
    }
}
