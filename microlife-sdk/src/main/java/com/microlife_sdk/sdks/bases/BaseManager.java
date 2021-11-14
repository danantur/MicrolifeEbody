// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.bases;

import java.util.List;
import android.bluetooth.BluetoothDevice;
import com.microlife_sdk.sdks.utils.LogUtils;
import com.microlife_sdk.sdks.interfaces.IConnectStationCallback;
import android.app.Activity;
import com.microlife_sdk.sdks.bean.SendMessage;
import com.microlife_sdk.sdks.enums.ProductStyle;
import android.content.Context;
import android.bluetooth.BluetoothAdapter;
import com.microlife_sdk.sdks.utils.BaseHandle;

public class BaseManager extends BaseHandle
{
    private static final String TAG = "BaseManager";
    protected BluetoothAdapter mBluetoothAdapter;
    protected ServiceGatt mServiceGatt;
    
    public BaseManager(final Context context, final ProductStyle productStyle, final String serviceUUID, final String[] writeUUIDs, final String[] readUUIDs, final boolean hasSendThread, final long ServiceGatt_SendThread_interval, final SendMessage heartSendMessage) {
        super(new Object[] { context });
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mServiceGatt = new ServiceGatt(context, productStyle, serviceUUID, writeUUIDs, readUUIDs, hasSendThread, ServiceGatt_SendThread_interval, heartSendMessage);
    }
    
    public boolean isBluetoothOpen() {
        return BlueManager.getInstance().isBluetoothOpen();
    }
    
    public void openBluetooth2(final Activity activity, final int requestCode) {
        BlueManager.getInstance().openBluetooth2(activity, requestCode);
    }
    
    public boolean isSupportBluetooth() {
        return BlueManager.getInstance().isSupportBluetooth();
    }
    
    public void setBlueConnectStationCallback(final IConnectStationCallback mIConnectStationCallback) {
        if (this.mServiceGatt != null) {
            this.mServiceGatt.setConnectCallback(mIConnectStationCallback);
        }
    }
    
    public String getConnectBluetoothAddress() {
        if (this.mServiceGatt != null) {
            return this.mServiceGatt.getConnectBluetoothAddress();
        }
        return null;
    }
    
    public boolean isConnect() {
        return this.mServiceGatt != null && this.mServiceGatt.isConnect();
    }
    
    @Override
    public void exit() {
        if (this.mServiceGatt != null) {
            this.mServiceGatt.exit();
            this.mServiceGatt = null;
        }
    }
    
    public void disConnect() {
        this.disConnect(true);
    }
    
    public void disConnect(final boolean sendfront) {
        if (this.mServiceGatt != null) {
            this.mServiceGatt.disConnect(sendfront);
        }
    }
    
    public String getConnectAddress() {
        if (this.mServiceGatt != null) {
            return this.mServiceGatt.getConnectBluetoothAddress();
        }
        return null;
    }
    
    public void connectDevice(final String address) {
        LogUtils.i("BaseManager", "--BaseManager--connectDevice address = " + address);
        if (this.mBluetoothAdapter != null && this.mServiceGatt != null) {
            final BluetoothDevice bluetoothDevice = this.mBluetoothAdapter.getRemoteDevice(address);
            LogUtils.i("BaseManager", "--BaseManager--connectDevice name = " + bluetoothDevice.getName());
            this.mServiceGatt.connectBluetoothDevice(bluetoothDevice);
        }
    }
    
    public void connectDevice(final BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice != null && this.mServiceGatt != null) {
            LogUtils.i("BaseManager", "--BaseManager--connectDevice address = " + bluetoothDevice.getAddress() + "name = " + bluetoothDevice.getName());
            this.mServiceGatt.connectBluetoothDevice(bluetoothDevice);
        }
    }
    
    public boolean addSendMsg(final SendMessage msg) {
        return this.mServiceGatt != null && this.mServiceGatt.addSendMsg(msg);
    }
    
    public boolean addSendMsgs(final List<SendMessage> msgs) {
        return this.mServiceGatt != null && this.mServiceGatt.addSendMsgs(msgs);
    }
}
