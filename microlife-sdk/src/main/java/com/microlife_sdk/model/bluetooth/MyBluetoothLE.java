// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.bluetooth;

import com.microlife_sdk.ideabuslibrary.util.BaseUtils;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import java.util.List;
import com.microlife_sdk.model.XlogUtils;
import android.content.Context;
import java.util.UUID;

public class MyBluetoothLE extends BluetoothLEUtils implements MyWriteThread.OnWriteStateListener
{
    public static final String TAG = "MyBluetoothLE";
    public static MyBluetoothLE myBluetooth;
    public static MyWriteThread writeThread;
    public static final UUID MY_UUID_SERVICE;
    public static UUID MY_UUID_WRITE;
    public static UUID MY_UUID_NOTIFY;
    public static final UUID MY_UUID_UPDATE_NOTIFICATION_DESCRIPTOR_UUID;
    public static final String HEADER = "4D";
    public static final String END = "-1";
    public static final String DEVICE_CODE_BPM_SEND = "FF";
    public static final String DEVICE_CODE_BPM_REPLY = "31";
    public static final String DEVICE_CODE_THERMO_APP_REPLY = "FE";
    public static final String DEVICE_CODE_THERMO_SEND = "41";
    public static final int CMD_LENGTH_INDEX_START = 4;
    public static final int CMD_LENGTH_INDEX_END = 8;
    public static final int CMD_CMD_INDEX_START = 8;
    public static final int CMD_CMD_INDEX_END = 10;
    public static final int COMMAND_COUNT = 10;
    public int sendCount;
    public String sendCom;
    public int TIME;
    public OnWriteStateListener mOnWriteStateListener;
    
    public static MyBluetoothLE getInstance(final Context context, final boolean b, final int time) {
        if (MyBluetoothLE.myBluetooth == null) {
            MyBluetoothLE.myBluetooth = new MyBluetoothLE(context, b);
        }
        final MyBluetoothLE myBluetooth = MyBluetoothLE.myBluetooth;
        myBluetooth.TIME = time;
        return myBluetooth;
    }
    
    public MyBluetoothLE(final Context context, final boolean b) {
        super(context, b);
        this.sendCount = 0;
        this.sendCom = "";
        this.TIME = 600;
    }
    
    static {
        MY_UUID_SERVICE = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
        MyBluetoothLE.MY_UUID_WRITE = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
        MyBluetoothLE.MY_UUID_NOTIFY = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
        MY_UUID_UPDATE_NOTIFICATION_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    }
    
    public void setOnWriteStateListener(final OnWriteStateListener mOnWriteStateListener) {
        this.mOnWriteStateListener = mOnWriteStateListener;
    }
    
    @Override
    public void onWriteThreadStart(final BluetoothLEHandler bluetoothLEHandler) {
        (MyBluetoothLE.writeThread = new MyWriteThread(MyBluetoothLE.myBluetooth)).setOnWriteStateListener(this);
        MyBluetoothLE.writeThread.start();
    }
    
    public boolean writeBLWMessage(final String sendCom, final boolean b, final boolean b2, final boolean b3) {
        if (b2) {
            MyBluetoothLE.writeThread.addNotRespondArray(sendCom);
        }
        if (b3) {
            MyBluetoothLE.writeThread.addBigRespondArray(sendCom);
        }
        this.sendCom = sendCom;
        return this.writeToBLE(sendCom, b);
    }
    
    @Override
    public synchronized boolean writeToBLE(final String str, final boolean b) {
        XlogUtils.xLog(MyBluetoothLE.TAG, "writeToBLE = " + str);
        if (str == null) {
            return false;
        }
        if (b || this.getCommArraySize() > 10) {
            this.removeOtherComm();
        }
        this.addCommArray(str);
        XlogUtils.xLog(MyBluetoothLE.TAG, "writeMessage  getCommArraySize   = " + this.getCommArraySize());
        return true;
    }
    
    @Override
    public void searchGattServices(final int i, final List<BluetoothGattService> list) {
        XlogUtils.xLog(MyBluetoothLE.TAG, "searchGattServices  " + i + " ble Service");
        if (list == null) {
            return;
        }
        final Iterator<BluetoothGattService> iterator = list.iterator();
        while (iterator.hasNext()) {
            final Iterator iterator2 = iterator.next().getCharacteristics().iterator();
            while (iterator2.hasNext()) {
                final BluetoothGattCharacteristic bluetoothGattCharacteristic;
                if ((bluetoothGattCharacteristic = (BluetoothGattCharacteristic) iterator2.next()).getUuid().equals(MyBluetoothLE.MY_UUID_WRITE)) {
                    bluetoothGattCharacteristic.setWriteType(1);
                    super.charWriteList.add(bluetoothGattCharacteristic);
                    XlogUtils.xLog(MyBluetoothLE.TAG, "-->Write  NAME = " + this.getBluetoothGatt(i).getDevice().getName());
                }
                else {
                    if (!bluetoothGattCharacteristic.getUuid().equals(MyBluetoothLE.MY_UUID_NOTIFY)) {
                        continue;
                    }
                    final BluetoothGattCharacteristic bluetoothGattCharacteristic2 = bluetoothGattCharacteristic;
                    XlogUtils.xLog(MyBluetoothLE.TAG, "-->NOTIFY   = " + this.getBluetoothGatt(i).setCharacteristicNotification(bluetoothGattCharacteristic, true));
                    final BluetoothGattDescriptor descriptor;
                    if ((descriptor = bluetoothGattCharacteristic2.getDescriptor(MyBluetoothLE.MY_UUID_UPDATE_NOTIFICATION_DESCRIPTOR_UUID)) != null) {
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        this.getBluetoothGatt(i).writeDescriptor(descriptor);
                    }
                    ++super.charNotifyCount;
                    XlogUtils.xLog(MyBluetoothLE.TAG, "-->Notify  charNotifyCount = " + super.charNotifyCount);
                }
            }
        }
    }
    
    @Override
    public String onRead(final BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        String str = "";
        final byte[] value;
        if ((value = bluetoothGattCharacteristic.getValue()).length > 0) {
            str = new String(value, StandardCharsets.UTF_8);
        }
        final String s = str;
        XlogUtils.xLog(MyBluetoothLE.TAG, "onRead()  message = " + str);
        return s;
    }
    
    @Override
    public String onChanged(final BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        final String convertBytesToHexString = BaseUtils.convertBytesToHexString(bluetoothGattCharacteristic.getValue());
        XlogUtils.xLog(MyBluetoothLE.TAG, "onChanged()  message = " + convertBytesToHexString);
        return convertBytesToHexString;
    }
    
    public String calcChecksum(String string, final String str, final String s, final String s2, final String s3) {
        final String s4 = string;
        XlogUtils.xLog(MyBluetoothLE.TAG, "calcChecksum cmd = " + s2);
        XlogUtils.xLog(MyBluetoothLE.TAG, "calcChecksum lengthstr = " + s);
        XlogUtils.xLog(MyBluetoothLE.TAG, "calcChecksum  data = " + s3);
        try {
            int int1 = Integer.parseInt(s4, 16);
            try {
                try {
                    final String s5 = string = str + s + s2 + s3;
                    XlogUtils.xLog(MyBluetoothLE.TAG, "calcChecksum AllData = " + string);
                    final int length = s5.length();
                    int beginIndex = 0;
                    for (int i = 2; i <= length; i += 2) {
                        int1 += Integer.parseInt(string.substring(beginIndex, i), 16);
                        beginIndex += 2;
                    }
                    final String format = String.format("%02x", int1 & 0xFF);
                    XlogUtils.xLog(MyBluetoothLE.TAG, "calcChecksum = " + format);
                    return format.toUpperCase();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    return "00";
                }
            }
            catch (Exception ex2) {}
        }
        catch (Exception ex3) {}
        return "";
    }
    
    public String calcChecksum4(String string, final String str, final String s, final String s2, final String s3) {
        final String s4 = string;
        XlogUtils.xLog(MyBluetoothLE.TAG, "calcChecksum4 cmd = " + s2);
        XlogUtils.xLog(MyBluetoothLE.TAG, "calcChecksum4 lengthstr = " + s);
        XlogUtils.xLog(MyBluetoothLE.TAG, "calcChecksum4  data = " + s3);
        try {
            int int1 = Integer.parseInt(s4, 16);
            try {
                try {
                    final String s5 = string = str + s + s2 + s3;
                    XlogUtils.xLog(MyBluetoothLE.TAG, "calcChecksum4 AllData = " + string);
                    final int length = s5.length();
                    int beginIndex = 0;
                    for (int i = 2; i <= length; i += 2) {
                        int1 += Integer.parseInt(string.substring(beginIndex, i), 16);
                        beginIndex += 2;
                    }
                    final String format = String.format("%04x", int1 & 0xFFFF);
                    XlogUtils.xLog(MyBluetoothLE.TAG, "calcChecksum4 = " + format);
                    return format.toUpperCase();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    return "0000";
                }
            }
            catch (Exception ex2) {}
        }
        catch (Exception ex3) {}
        return "";
    }
    
    public String buildCmdString(final String str, final String str2) {
        final String format = String.format("%04x", str2.length() / 2 + 1 + 1);
        final String string = "4DFF" + format + str + str2 + this.calcChecksum("4D", "FF", format, str, str2);
        XlogUtils.xLog(MyBluetoothLE.TAG, "buildCmdString = " + string);
        return string;
    }
    
    public String buildCmdStringForWBO3(final String str, final String str2) {
        final String format = String.format("%04x", str2.length() / 2 + 1 + 2);
        final String string = "4DFF" + format + str + str2 + this.calcChecksum4("4D", "FF", format, str, str2);
        XlogUtils.xLog(MyBluetoothLE.TAG, "buildCmdStringForWBO3 = " + string);
        return string;
    }
    
    public String buildCmdStringForThermo(final String str, final String str2) {
        final String format = String.format("%04x", str2.length() / 2 + 1 + 1);
        final String string = "4DFE" + format + str + str2 + this.calcChecksum("4D", "FE", format, str, str2);
        XlogUtils.xLog(MyBluetoothLE.TAG, "buildCmdStringForThermo = " + string);
        return string;
    }
    
    public String buildCmdStringForWBP(final String str, final String str2) {
        final String format = String.format("%02x", str2.length() / 2 + 1 + 1);
        final String string = "4DFF" + format + str + str2 + this.calcChecksum("4D", "FF", format, str, str2);
        XlogUtils.xLog(MyBluetoothLE.TAG, "buildCmdStringForWBP = " + string);
        return string;
    }
    
    @Override
    public void onWriteMessage(final boolean b, final String s) {
        final OnWriteStateListener mOnWriteStateListener;
        if ((mOnWriteStateListener = this.mOnWriteStateListener) != null) {
            mOnWriteStateListener.onWriteMessage(b, s);
        }
    }
    
    public interface OnWriteStateListener
    {
        void onWriteMessage(final boolean p0, final String p1);
    }
}
