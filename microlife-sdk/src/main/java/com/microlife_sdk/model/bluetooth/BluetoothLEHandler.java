// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.bluetooth;

import android.os.Bundle;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import com.microlife_sdk.model.XlogUtils;
import com.microlife_sdk.model.abcdef.f;

import android.os.Message;
import android.bluetooth.BluetoothAdapter;
import android.os.Handler;

public class BluetoothLEHandler extends Handler
{
    public static final String TAG;
    public BluetoothLEUtils bluetooth;
    public BluetoothAdapter mBluetoothAdapter;
    public boolean isRun;
    
    public BluetoothLEHandler(final BluetoothLEUtils bluetooth, final BluetoothAdapter mBluetoothAdapter) {
        this.isRun = false;
        this.bluetooth = bluetooth;
        this.mBluetoothAdapter = mBluetoothAdapter;
    }
    
    static {
        TAG = BluetoothLEHandler.class.getSimpleName();
    }
    
    public void handleMessage(final Message message) {
        super.handleMessage(message);
        final int what;
        if ((what = message.what) != 6) {
            if (what != 20) {
                if (what != 21) {
                    switch (what) {
                        case 4: {
                            final BluetoothLEUtils bluetooth;
                            (bluetooth = this.bluetooth).isScanning = false;
                            final BluetoothAdapter mBluetoothAdapter;
                            if ((mBluetoothAdapter = this.mBluetoothAdapter) != null) {
                                mBluetoothAdapter.stopLeScan(bluetooth.setLeScanCallback());
                            }
                            final BluetoothLEClass.OnIMBluetoothLEListener mOnIMBluetoothLEListener;
                            if ((mOnIMBluetoothLEListener = BluetoothLEClass.mOnIMBluetoothLEListener) != null) {
                                mOnIMBluetoothLEListener.connectionStatus(message.what);
                                break;
                            }
                            break;
                        }
                        case 3: {
                            if (BluetoothLEClass.mOnIMBluetoothLEListener != null) {
                                final Bundle data = message.getData();
                                BluetoothLEClass.mOnIMBluetoothLEListener.scanResult(data.getString("address"), data.getString("name"), data.getInt("rssi"), data.getByteArray("scanRecord"));
                                break;
                            }
                            break;
                        }
                        case 2: {
                            final BluetoothLEClass.OnIMBluetoothLEListener mOnIMBluetoothLEListener2;
                            if ((mOnIMBluetoothLEListener2 = BluetoothLEClass.mOnIMBluetoothLEListener) != null) {
                                mOnIMBluetoothLEListener2.dataResult((String)message.obj);
                                break;
                            }
                            break;
                        }
                        case 1: {
                            final BluetoothLEClass.OnIMBluetoothLEListener mOnIMBluetoothLEListener3;
                            if ((mOnIMBluetoothLEListener3 = BluetoothLEClass.mOnIMBluetoothLEListener) != null) {
                                mOnIMBluetoothLEListener3.connectionStatus(this.bluetooth.mCurrentStatus);
                            }
                            final BluetoothLEUtils bluetooth2;
                            if ((bluetooth2 = this.bluetooth).mCurrentStatus == 17 && !this.isRun) {
                                final BluetoothLEUtils bluetoothLEUtils = bluetooth2;
                                this.isRun = true;
                                bluetoothLEUtils.onWriteThreadStart(this);
                                break;
                            }
                            this.isRun = false;
                            break;
                        }
                    }
                }
                else {
                    if (f.f) {
                        XlogUtils.xLog(BluetoothLEHandler.TAG, "Creating " + message.arg1 + " Bluetooth connection");
                    }
                    final BluetoothLEUtils bluetooth3;
                    (bluetooth3 = this.bluetooth).mCurrentStatus = 21;
                    if (Build.VERSION.SDK_INT >= 23) {
                        bluetooth3.addBluetoothGatt(((BluetoothDevice)message.obj).connectGatt(bluetooth3.getContext(), false, this.bluetooth.setLeGattCallback(), 2));
                    }
                    else {
                        bluetooth3.addBluetoothGatt(((BluetoothDevice)message.obj).connectGatt(bluetooth3.getContext(), false, this.bluetooth.setLeGattCallback()));
                    }
                }
            }
            else if (this.bluetooth.mCurrentStatus != 17) {
                XlogUtils.xLog(BluetoothLEHandler.TAG, "Connection timed out");
                this.bluetooth.disconnect(20);
            }
        }
        else {
            final BluetoothLEUtils bluetooth4;
            if (!(bluetooth4 = this.bluetooth).isScanning) {
                return;
            }
            bluetooth4.startLEScan(message.arg1, true);
            final BluetoothLEClass.OnIMBluetoothLEListener mOnIMBluetoothLEListener4;
            if ((mOnIMBluetoothLEListener4 = BluetoothLEClass.mOnIMBluetoothLEListener) != null) {
                mOnIMBluetoothLEListener4.connectionStatus(4);
            }
        }
    }
}
