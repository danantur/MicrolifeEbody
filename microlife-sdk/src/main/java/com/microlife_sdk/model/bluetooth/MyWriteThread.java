// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.bluetooth;

import java.util.Iterator;
import com.microlife_sdk.model.XlogUtils;
import com.microlife_sdk.ideabuslibrary.util.BaseUtils;
import android.bluetooth.BluetoothGattCharacteristic;
import java.util.ArrayList;
import java.util.List;

public class MyWriteThread extends WriteThread
{
    public static final String TAG;
    public static final int TIME_LONG = 3000;
    public int TIME;
    public MyBluetoothLE myBluetooth;
    public List<String> notRespondArray;
    public List<String> bigRespondArray;
    public OnWriteStateListener mOnWriteStateListener;
    
    public MyWriteThread(final MyBluetoothLE myBluetooth) {
        super(myBluetooth);
        this.TIME = 600;
        this.myBluetooth = myBluetooth;
        this.notRespondArray = new ArrayList<String>();
        this.bigRespondArray = new ArrayList<String>();
        final int time;
        if ((time = this.myBluetooth.TIME) > this.TIME) {
            this.TIME = time;
        }
    }
    
    private void writeData(final String str) {
        MyBluetoothLE myBluetooth;
        for (int i = 0; i < (myBluetooth = this.myBluetooth).connGattCount; ++i) {
            if (myBluetooth.getBluetoothGatt(i) != null) {
                this.myBluetooth.charWriteList.get(i).setValue(BaseUtils.convertHexToByteArray(str));
                final boolean writeCharacteristic = this.myBluetooth.getBluetoothGatt(i).writeCharacteristic((BluetoothGattCharacteristic)this.myBluetooth.charWriteList.get(i));
                final OnWriteStateListener mOnWriteStateListener;
                if ((mOnWriteStateListener = this.mOnWriteStateListener) != null) {
                    mOnWriteStateListener.onWriteMessage(writeCharacteristic, str);
                }
                XlogUtils.xLog(MyWriteThread.TAG, "writeCharacteristic " + i + "  = " + writeCharacteristic + " , message = " + str);
            }
        }
    }
    
    static {
        TAG = MyWriteThread.class.getSimpleName();
    }
    
    public void setOnWriteStateListener(final OnWriteStateListener mOnWriteStateListener) {
        this.mOnWriteStateListener = mOnWriteStateListener;
    }
    
    public void addNotRespondArray(String upperCase) {
        if (this.notRespondArray != null && upperCase != null) {
            upperCase = upperCase.toUpperCase();
            final List<String> notRespondArray = this.notRespondArray;
            boolean b = true;
            final Iterator<String> iterator = notRespondArray.iterator();
            while (iterator.hasNext()) {
                if (upperCase.startsWith(iterator.next())) {
                    b = false;
                }
            }
            if (b) {
                this.notRespondArray.add(upperCase);
            }
            XlogUtils.xLog(MyWriteThread.TAG, "not Respond Com Array = " + this.notRespondArray);
        }
    }
    
    public void addBigRespondArray(final String s) {
        final List<String> bigRespondArray;
        if ((bigRespondArray = this.bigRespondArray) != null && s != null) {
            bigRespondArray.clear();
            this.bigRespondArray.add(s.toUpperCase());
            XlogUtils.xLog(MyWriteThread.TAG, "big Respond Com Array = " + this.bigRespondArray);
        }
    }
    
    @Override
    public void run() {
        super.run();
    }
    
    @Override
    public void write() {
        String str;
        final String s = str = this.myBluetooth.getComm(0).toUpperCase();
        XlogUtils.xLog(MyWriteThread.TAG, "CommArraySize  = " + this.myBluetooth.getCommArraySize() + " write  = " + str);
        if (s == null) {
            this.myBluetooth.removeComm(0);
            return;
        }
        if (str.length() <= 40) {
            this.writeData(str);
        }
        else {
            while (str.length() != 0) {
                int length;
                if (str.length() > 40) {
                    length = 40;
                }
                else {
                    length = str.length();
                }
                final String s2 = str;
                final int beginIndex = length;
                final String s3 = str;
                XlogUtils.xLog(MyWriteThread.TAG, "Segmented transmission - write  = " + str.substring(0, length));
                this.writeData(s3.substring(0, length));
                str = s2.substring(beginIndex);
                try {
                    Thread.sleep(300L);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        final MyBluetoothLE myBluetooth = this.myBluetooth;
        ++myBluetooth.sendCount;
        XlogUtils.xLog(MyWriteThread.TAG, "The number of transmissions = " + this.myBluetooth.sendCount);
        if (this.myBluetooth.sendCount >= 10) {
            XlogUtils.xLog(MyWriteThread.TAG, "Reply timeout automatically disconnect!");
            final MyBluetoothLE myBluetooth2 = this.myBluetooth;
            myBluetooth2.sendCount = 0;
            myBluetooth2.disconnect(20);
            return;
        }
        final Iterator<String> iterator = this.notRespondArray.iterator();
        while (iterator.hasNext()) {
            final String str2;
            if (str.startsWith(str2 = iterator.next())) {
                XlogUtils.xLog(MyWriteThread.TAG, "Del Com = " + str2);
                final MyBluetoothLE myBluetooth3 = this.myBluetooth;
                myBluetooth3.removeComm(myBluetooth3.sendCount = 0);
            }
        }
        int time = this.TIME;
        final Iterator<String> iterator2 = this.bigRespondArray.iterator();
        while (iterator2.hasNext()) {
            if (str.startsWith(iterator2.next())) {
                time = 3000;
            }
        }
        final int n = time;
        XlogUtils.xLog(MyWriteThread.TAG, "sleep_time = " + time);
        final long n2 = n;
        try {
            Thread.sleep(n2);
        }
        catch (InterruptedException ex2) {
            ex2.printStackTrace();
        }
    }
    
    public interface OnWriteStateListener
    {
        void onWriteMessage(final boolean p0, final String p1);
    }
}
