// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.bluetooth;

import java.lang.reflect.Method;
import android.bluetooth.BluetoothGatt;
import android.location.LocationManager;
import android.content.IntentFilter;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import java.util.Iterator;
import android.os.Bundle;
import android.os.Message;
import android.bluetooth.BluetoothDevice;
import java.util.ArrayList;
import android.bluetooth.BluetoothManager;
import com.microlife_sdk.model.XlogUtils;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothAdapter;
import java.util.List;
import android.content.Context;
import java.util.UUID;

public abstract class BluetoothLEUtils extends BluetoothLEClass
{
    public static final String TAG = "BluetoothLEUtils";
    public static boolean isRegisterBtReceiver = false;
    public static boolean isRegisterGPSReceiver = false;
    public static final UUID Software_Revision_String;
    public Context context;
    public List<String> mScanList;
    public boolean isScanning;
    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothAdapter.LeScanCallback mLeOldScanCallback;
    public BluetoothGattCallback mLeGattCallback;
    public ConnectionThread mConnectionThread;
    public BluetoothLEHandler mHandler;
    public boolean isRestartingBT;
    public List<BluetoothGattService> positGattServices;
    public int positItem;
    public BroadcastReceiver mReceiver;
    
    public BluetoothLEUtils(final Context context, final boolean f) {
        this.isScanning = false;
        this.positItem = 0;
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
                    try {
                        final int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
                        XlogUtils.xLog(BluetoothLEUtils.access$000(), "BluetoothLEUtils mReceiver--------------" + intExtra);
                        switch (intExtra) {
                            case 12: {
                                XlogUtils.xLog(BluetoothLEUtils.access$000(), "Bluetooth enable!");
                                BluetoothLEClass.mOnIMBluetoothLEListener.onBtStateChanged(true);
                                BluetoothLEUtils.this.unregisterBtReceiver();
                                break;
                            }
                            case 10: {
                                XlogUtils.xLog(BluetoothLEUtils.access$000(), "Bluetooth disable!");
                                BluetoothLEClass.mOnIMBluetoothLEListener.onBtStateChanged(false);
                                BluetoothLEUtils.this.unregisterBtReceiver();
                            }
                        }
                    }
                    catch (Exception ex) {}
                }
            }
        };
        this.context = context;
        com.microlife_sdk.model.abcdef.f.f = f;
        if (context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            final BluetoothManager bluetoothManager;
            if ((bluetoothManager = (BluetoothManager)this.context.getSystemService(Context.BLUETOOTH_SERVICE)) != null) {
                this.mBluetoothAdapter = bluetoothManager.getAdapter();
            }
            this.initCommArray();
        }
        this.mScanList = new ArrayList<String>();
        this.mHandler = new BluetoothLEHandler(this, this.mBluetoothAdapter);
    }
    
    private void restartBT() {
        if (this.mBluetoothAdapter != null) {
            this.stopLEScan();
            if (this.isBTEnabled()) {
                this.mBluetoothAdapter.disable();
            }
            else {
                this.mBluetoothAdapter.enable();
            }
        }
    }
    
    private synchronized void checkIsExist(final BluetoothDevice bluetoothDevice, final int i, final byte[] obj) {
        final Iterator<String> iterator = this.mScanList.iterator();
        while (iterator.hasNext()) {
            if (bluetoothDevice.getAddress().equals(iterator.next())) {
                return;
            }
        }
        this.mScanList.add(bluetoothDevice.getAddress());
        final Message message;
        (message = new Message()).what = 3;
        if (bluetoothDevice.getName() != null && bluetoothDevice.getName().length() > 0) {
            final Message message2 = message;
            final String name = bluetoothDevice.getName();
            final Bundle data = new Bundle();
            data.putString("name", name);
            data.putString("address", bluetoothDevice.getAddress());
            data.putInt("rssi", i);
            data.putByteArray("scanRecord", obj);
            message2.setData(data);
            XlogUtils.xLog(BluetoothLEUtils.TAG, "setLeScanCallbackDevice == name\uff1a" + name + " address\uff1a" + bluetoothDevice.getAddress() + " rssi\uff1a" + i + " scanRecord\uff1a" + obj);
            this.mHandler.sendMessage(message);
        }
    }
    
    private void connectBLEs(final List<String> list) {
        (this.mConnectionThread = new ConnectionThread(this, this.mBluetoothAdapter, this.mHandler, list)).start();
    }
    
    private List<BluetoothGattService> getSupportedGattServices(final int n) {
        if (this.getBluetoothGatt(n) != null) {
            return (List<BluetoothGattService>)this.getBluetoothGatt(n).getServices();
        }
        return null;
    }
    
    private void displayGattSoftwareRevisionString(final int positItem, final List<BluetoothGattService> positGattServices) {
        this.positItem = positItem;
        this.positGattServices = positGattServices;
        if (positGattServices == null) {
            return;
        }
        final Iterator<BluetoothGattService> iterator = positGattServices.iterator();
        while (iterator.hasNext()) {
            final Iterator iterator2 = iterator.next().getCharacteristics().iterator();
            while (iterator2.hasNext()) {
                final BluetoothGattCharacteristic bluetoothGattCharacteristic;
                if ((bluetoothGattCharacteristic = (BluetoothGattCharacteristic) iterator2.next()).getUuid().equals(BluetoothLEUtils.Software_Revision_String)) {
                    XlogUtils.xLog(BluetoothLEUtils.TAG, "-->Read   = " + this.getBluetoothGatt(positItem).readCharacteristic(bluetoothGattCharacteristic));
                }
            }
        }
    }
    
    private void displayGattServices(int mConnectionThread, final List<BluetoothGattService> list) {
        int n = mConnectionThread;
        this.searchGattServices(n, list);
        final int connGattCount;
        if (super.charWriteList.size() == (connGattCount = super.connGattCount) && super.charNotifyCount == connGattCount) {
            if (n != 0 && n == connGattCount - 1) {
                this.connectSuccess();
            }
            else if (n == 0 && n + 1 == super.connGattCount) {
                this.connectSuccess();
            }
        }
        if (this.mConnectionThread == null) {
            return;
        }
        synchronized ((Object)mConnectionThread) {
            this.mConnectionThread.notify();
        }
    }
    
    private void connectSuccess() {
        XlogUtils.xLog(BluetoothLEUtils.TAG, "connectSuccess CONNECTED------");
        super.mCurrentStatus = 17;
        this.mHandler.sendEmptyMessage(1);
        final ConnectionThread mConnectionThread;
        if ((mConnectionThread = this.mConnectionThread) != null && !mConnectionThread.isInterrupted()) {
            this.mConnectionThread.interrupt();
            this.mConnectionThread = null;
        }
        super.charNotifyCount = 0;
    }
    
    private void initParams() {
        final ConnectionThread mConnectionThread;
        if ((mConnectionThread = this.mConnectionThread) != null && !mConnectionThread.isInterrupted()) {
            this.mConnectionThread.interrupt();
            this.mConnectionThread = null;
        }
        this.mHandler.removeMessages(4);
        this.mHandler.removeMessages(20);
        super.charNotifyCount = 0;
        super.isWriteRunning = false;
        this.removeAllComm();
    }
    
    public static /* synthetic */ String access$000() {
        return BluetoothLEUtils.TAG;
    }
    
    public static /* synthetic */ BluetoothAdapter access$200(final BluetoothLEUtils bluetoothLEUtils) {
        return bluetoothLEUtils.mBluetoothAdapter;
    }
    
    public static /* synthetic */ List access$500(final BluetoothLEUtils bluetoothLEUtils) {
        return bluetoothLEUtils.positGattServices;
    }
    
    static {
        Software_Revision_String = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb");
    }
    
    public abstract void onWriteThreadStart(final BluetoothLEHandler p0);
    
    public abstract String onRead(final BluetoothGattCharacteristic p0);
    
    public abstract String onChanged(final BluetoothGattCharacteristic p0);
    
    public abstract void searchGattServices(final int p0, final List<BluetoothGattService> p1);
    
    public abstract boolean writeToBLE(final String p0, final boolean p1);
    
    public Context getContext() {
        return this.context;
    }
    
    public boolean isBTEnabled() {
        final BluetoothAdapter mBluetoothAdapter;
        return (mBluetoothAdapter = this.mBluetoothAdapter) != null && mBluetoothAdapter.isEnabled();
    }
    
    public boolean isScanning() {
        return this.isScanning;
    }
    
    public boolean isEnabled() {
        final BluetoothAdapter mBluetoothAdapter;
        return (mBluetoothAdapter = this.mBluetoothAdapter) != null && mBluetoothAdapter.isEnabled();
    }
    
    public boolean isConnected() {
        return super.mCurrentStatus == 17;
    }
    
    public boolean isSupportBluetooth(final Activity activity) {
        if (!this.context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            return false;
        }
        if (!this.mBluetoothAdapter.isEnabled()) {
            this.registerBtReceiver();
            activity.startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 100);
        }
        return true;
    }
    
    public void registerBtReceiver() {
        if (BluetoothLEUtils.isRegisterBtReceiver) {
            this.unregisterBtReceiver();
        }
        try {
            XlogUtils.xLog(BluetoothLEUtils.TAG, "registerReceiver~~");
            this.context.registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
            try {
                BluetoothLEUtils.isRegisterBtReceiver = true;
            }
            catch (Exception ex) {}
        }
        catch (Exception ex2) {}
    }
    
    public void unregisterBtReceiver() {
        try {
            XlogUtils.xLog(BluetoothLEUtils.TAG, "unregisterReceiver~~");
            this.context.unregisterReceiver(this.mReceiver);
            BluetoothLEUtils.isRegisterBtReceiver = false;
        }
        catch (Exception ex) {}
    }
    
    public void checkGPS(final Activity activity) {
        if (!((LocationManager)this.context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps")) {
            activity.startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 477);
        }
    }
    
    public void startLEScan(final int arg1, final boolean b) {
        XlogUtils.xLog(BluetoothLEUtils.TAG, "startScan");
        final BluetoothAdapter mBluetoothAdapter;
        if ((mBluetoothAdapter = this.mBluetoothAdapter) != null && mBluetoothAdapter.isEnabled()) {
            if (this.isScanning) {
                XlogUtils.xLog(BluetoothLEUtils.TAG, "Already scanning, stop scanning ...");
                this.stopLEScan();
            }
            try {
                this.mScanList.clear();
                if (b) {
                    this.mHandler.removeMessages(6);
                    final Message message = new Message();
                    final Message message3;
                    final Message message2 = message3 = message;
                    new Message();
                    message3.what = 6;
                    message.arg1 = arg1;
                    this.mHandler.sendMessageDelayed(message2, (long)(arg1 * 1000));
                }
                else {
                    this.mHandler.removeMessages(4);
                    this.mHandler.sendEmptyMessageDelayed(4, (long)(arg1 * 1000));
                }
                if (this.mBluetoothAdapter != null) {
                    this.isScanning = true;
                    this.mBluetoothAdapter.startLeScan(this.setLeScanCallback());
                }
            }
            catch (NoClassDefFoundError noClassDefFoundError) {
                noClassDefFoundError.printStackTrace();
            }
            return;
        }
        XlogUtils.xLog(BluetoothLEUtils.TAG, "mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()");
    }
    
    public void stopLEScan() {
        XlogUtils.xLog(BluetoothLEUtils.TAG, "stopScan");
        this.mHandler.removeMessages(4);
        this.isScanning = false;
        final BluetoothAdapter mBluetoothAdapter;
        if ((mBluetoothAdapter = this.mBluetoothAdapter) != null) {
            mBluetoothAdapter.stopLeScan(this.setLeScanCallback());
        }
    }
    
    public BluetoothAdapter.LeScanCallback setLeScanCallback() {
        if (this.mLeOldScanCallback == null) {
            this.mLeOldScanCallback = (BluetoothAdapter.LeScanCallback)new BluetoothAdapter.LeScanCallback() {
                public void onLeScan(final BluetoothDevice bluetoothDevice, final int n, final byte[] array) {
                    final BluetoothLEUtils a;
                    if (!(a = BluetoothLEUtils.this).isScanning) {
                        a.stopLEScan();
                    }
                    else if (bluetoothDevice != null && bluetoothDevice.getAddress() != null && bluetoothDevice.getAddress().length() > 0) {
                        BluetoothLEUtils.this.checkIsExist(bluetoothDevice, n, array);
                    }
                }
            };
        }
        return this.mLeOldScanCallback;
    }
    
    public void connect(final List<String> obj) {
        if (com.microlife_sdk.model.abcdef.f.f) {
            XlogUtils.xLog(BluetoothLEUtils.TAG, "connect MAC : " + obj);
        }
        this.mHandler.removeMessages(4);
        if (this.isScanning()) {
            this.stopLEScan();
        }
        if (this.mBluetoothAdapter != null && obj != null && obj.size() != 0) {
            super.mCurrentStatus = 16;
            this.initBluetoothGattsArray();
            this.initParams();
            super.charWriteList = new ArrayList<>();
            super.connGattCount = obj.size();
            this.connectBLEs(obj);
            return;
        }
        XlogUtils.xLog(BluetoothLEUtils.TAG, "mBluetoothAdapter == null || address == null || address.size() == 0");
        super.mCurrentStatus = 19;
        this.mHandler.sendEmptyMessage(1);
    }
    
    public boolean writeMessage(final String s, final boolean b) {
        return this.writeToBLE(s, b);
    }
    
    public void readRSSI() {
        if (this.getBluetoothGatt(0) != null) {
            XlogUtils.xLog(BluetoothLEUtils.TAG, "read RSSI == " + this.getBluetoothGatt(0).readRemoteRssi());
        }
    }
    
    public void sendTest(final String s) {
        if (this.getBluetoothGatt(0) != null) {
            super.charWriteList.get(0).setValue(com.microlife_sdk.model.abcdef.f.d(s));
            XlogUtils.xLog(BluetoothLEUtils.TAG, "send CommStr == " + this.getBluetoothGatt(0).writeCharacteristic((BluetoothGattCharacteristic)super.charWriteList.get(0)));
        }
    }
    
    public BluetoothGattCallback setLeGattCallback() {
        if (this.mLeGattCallback == null) {
            this.mLeGattCallback = new BluetoothGattCallback() {
                public void onConnectionStateChange(final BluetoothGatt obj, int i, final int j) {
                    final boolean b = i != 0;
                    XlogUtils.xLog(BluetoothLEUtils.access$000(), "onConnectionStateChange == BluetoothGatt\uff1a" + obj + " status\uff1a" + i + " newState\uff1a" + j);
                    if (!b) {
                        if (j == 2) {
                            try {
                                Thread.sleep(600L);
                            }
                            catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            BluetoothLEUtils a;
                            for (i = 0; i < (a = BluetoothLEUtils.this).connGattCount; ++i) {
                                if (obj.equals(a.getBluetoothGatt(i))) {
                                    BluetoothLEUtils.this.getBluetoothGatt(i).discoverServices();
                                    break;
                                }
                            }
                        }
                        else if (j == 0) {
                            this.a(obj, i);
                        }
                    }
                    else {
                        this.a(obj, i);
                    }
                }
                
                public void a(final BluetoothGatt bluetoothGatt, final int n) {
                    bluetoothGatt.disconnect();
                    bluetoothGatt.close();
                    BluetoothLEUtils.this.disconnect(18);
                    if (n == 133) {
                        BluetoothLEUtils.access$200(BluetoothLEUtils.this).isEnabled();
                    }
                }
                
                public void onServicesDiscovered(final BluetoothGatt obj, int i) {
                    final boolean b = i != 0;
                    XlogUtils.xLog(BluetoothLEUtils.access$000(), "onServicesDiscovered ==  BluetoothGatt : " + obj + " status\uff1a" + i);
                    if (!b) {
                        BluetoothLEUtils a;
                        BluetoothLEUtils a2;
                        for (i = 0; i < (a = BluetoothLEUtils.this).connGattCount; ++i) {
                            if (obj.equals(a.getBluetoothGatt(i))) {
                                a2 = BluetoothLEUtils.this;
                                a2.displayGattSoftwareRevisionString(i, a2.getSupportedGattServices(i));
                                break;
                            }
                        }
                    }
                    else {
                        BluetoothLEUtils.this.disconnect(18);
                    }
                }
                
                public void onCharacteristicWrite(final BluetoothGatt obj, final BluetoothGattCharacteristic obj2, final int i) {
                    XlogUtils.xLog(BluetoothLEUtils.access$000(), "onCharacteristicWrite == BluetoothGatt\uff1a" + obj + " BluetoothGattCharacteristic\uff1a" + obj2 + " status\uff1a" + i);
                }
                
                public void onCharacteristicRead(final BluetoothGatt obj, final BluetoothGattCharacteristic obj2, final int i) {
                    XlogUtils.xLog(BluetoothLEUtils.access$000(), "onCharacteristicRead == BluetoothGatt\uff1a" + obj + " BluetoothGattCharacteristic\uff1a" + obj2 + " status\uff1a" + i);
                    final BluetoothLEUtils a = BluetoothLEUtils.this;
                    a.displayGattServices(a.positItem, BluetoothLEUtils.access$500(a));
                    final Message message2;
                    final Message message = message2 = new Message();
                    message.what = 2;
                    message.obj = "Software_Revision_String==" + BluetoothLEUtils.this.onRead(obj2);
                    BluetoothLEUtils.this.mHandler.sendMessage(message2);
                }
                
                public void onCharacteristicChanged(final BluetoothGatt obj, final BluetoothGattCharacteristic obj2) {
                    XlogUtils.xLog(BluetoothLEUtils.access$000(), "onCharacteristicChanged == BluetoothGatt\uff1a" + obj + " BluetoothGattCharacteristic\uff1a" + obj2);
                    final String trim = obj.getDevice().getName().trim();
                    final Message message2;
                    final Message message = message2 = new Message();
                    message.what = 2;
                    message.obj = trim + "==" + BluetoothLEUtils.this.onChanged(obj2);
                    BluetoothLEUtils.this.mHandler.sendMessage(message2);
                }
            };
        }
        return this.mLeGattCallback;
    }
    
    public synchronized void disconnect(int i) {
        final int mCurrentStatus = i;
        XlogUtils.xLog(BluetoothLEUtils.TAG, "disconnect code : " + i);
        this.stopLEScan();
        this.initParams();
        super.mCurrentStatus = mCurrentStatus;
        this.unregisterBtReceiver();
        if (this.getBluetoothGatts() != null && this.getBluetoothGatts().size() > 0) {
            i = 0;
            while (i < this.getBluetoothGatts().size()) {
                try {
                    if (this.getBluetoothGatt(i) != null) {
                        final int n = i;
                        this.getBluetoothGatt(i).disconnect();
                        this.getBluetoothGatt(n).close();
                    }
                    this.mHandler.sendEmptyMessage(1);
                    ++i;
                    continue;
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }
        this.initBluetoothGattsArray();
    }
    
    public boolean refreshDeviceCache(final BluetoothGatt obj) {
        try {
            final Class<? extends BluetoothGatt> class1 = obj.getClass();
            final String name = "refresh";
            try {
                final Method method;
                if ((method = class1.getMethod(name, (Class<?>[])new Class[0])) != null) {
                    return (boolean)method.invoke(obj, new Object[0]);
                }
                return false;
            }
            catch (Exception ex) {
                XlogUtils.xLog(BluetoothLEUtils.TAG, "An exception occured while refreshing device");
            }
        }
        catch (Exception ex2) {}
        return false;
    }
}
