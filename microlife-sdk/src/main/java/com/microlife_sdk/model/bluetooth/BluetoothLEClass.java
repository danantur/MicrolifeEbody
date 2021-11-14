// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.bluetooth;

import java.util.ArrayList;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGatt;
import android.util.Log;

import java.util.List;

public class BluetoothLEClass
{
    public static final int CALLBACK_STATE = 1;
    public static final int CALLBACK_RESULT = 2;
    public static final int SCAN_RESULT = 3;
    public static final int SCAN_FINISH = 4;
    public static final int SCAN_STOP = 5;
    public static final int SCAN_LOOP = 6;
    public static final int NONE = 16;
    public static final int CONNECTED = 17;
    public static final int DISCONNECTED = 18;
    @Deprecated
    public static final int UNSPECIFIED_ADDRESS = 19;
    public static final int CONNECT_TIMEOUT = 20;
    public static final int CONNECTING = 21;
    public static final int ERROR133_RESTART_BT = 23;
    public static final int ACTION_REQUEST_ENABLE = 100;
    public static final int CONNECT_PERIOD = 15000;
    public static OnIMBluetoothLEListener mOnIMBluetoothLEListener;
    public List<BluetoothGatt> mBluetoothGatts;
    public List<String> commArray;
    public List<BluetoothGattCharacteristic> charWriteList;
    public int charNotifyCount;
    public int connGattCount;
    public boolean isWriteRunning;
    public int mCurrentStatus;
    
    public BluetoothLEClass() {
        this.charNotifyCount = 0;
        this.connGattCount = 0;
        this.isWriteRunning = false;
        this.mCurrentStatus = 16;
    }
    
    public void initBluetoothGattsArray() {
        this.mBluetoothGatts = new ArrayList<BluetoothGatt>();
    }
    
    public void addBluetoothGatt(final BluetoothGatt bluetoothGatt) {
        final List<BluetoothGatt> mBluetoothGatts;
        if ((mBluetoothGatts = this.mBluetoothGatts) != null) {
            mBluetoothGatts.add(bluetoothGatt);
        }
    }
    
    public BluetoothGatt getBluetoothGatt(final int n) {
        final List<BluetoothGatt> mBluetoothGatts;
        if ((mBluetoothGatts = this.mBluetoothGatts) != null && mBluetoothGatts.size() > n) {
            return this.mBluetoothGatts.get(n);
        }
        return null;
    }
    
    public List<BluetoothGatt> getBluetoothGatts() {
        return this.mBluetoothGatts;
    }
    
    public void removeBluetoothGatt() {
        final List<BluetoothGatt> mBluetoothGatts;
        if ((mBluetoothGatts = this.mBluetoothGatts) != null && mBluetoothGatts.size() > 0) {
            this.mBluetoothGatts.remove(0);
        }
    }
    
    public void initCommArray() {
        this.commArray = new ArrayList<>();
    }
    
    public void addCommArray(final String s) {
        final List<String> commArray;
        if ((commArray = this.commArray) != null) {
            commArray.add(s);
        }
    }
    
    public String getComm(final int n) {
        final List<String> commArray;
        if ((commArray = this.commArray) != null && commArray.size() > n) {
            return this.commArray.get(n);
        }
        return null;
    }
    
    public int getCommArraySize() {
        final List<String> commArray = this.commArray;
        return (commArray == null) ? 0 : commArray.size();
    }
    
    public void removeComm(final int n) {
        final List<String> commArray;
        if ((commArray = this.commArray) != null && commArray.size() > n) {
            this.commArray.remove(n);
        }
    }
    
    public void removeSameComm(final String anObject) {
        if (this.commArray == null) {
            return;
        }
        for (int i = 0; i < this.commArray.size(); ++i) {
            if (this.commArray.get(i).equals(anObject)) {
                this.commArray.remove(i);
            }
        }
    }
    
    public void removeOtherComm() {
        final List<String> commArray;
        if ((commArray = this.commArray) != null && commArray.size() > 1) {
            final String s = this.commArray.get(0);
            this.commArray.clear();
            this.commArray.add(s);
        }
    }
    
    public void removeAllComm() {
        final List<String> commArray;
        if ((commArray = this.commArray) != null) {
            commArray.clear();
        }
    }
    
    public void setOnIMBluetoothLEListener(final OnIMBluetoothLEListener mOnIMBluetoothLEListener) {
        BluetoothLEClass.mOnIMBluetoothLEListener = mOnIMBluetoothLEListener;
    }
    
    public interface OnIMBluetoothLEListener
    {
        void onBtStateChanged(final boolean p0);
        
        void scanResult(final String p0, final String p1, final int p2, final byte[] p3);
        
        void connectionStatus(final int p0);
        
        void dataResult(final String p0);
    }
}
