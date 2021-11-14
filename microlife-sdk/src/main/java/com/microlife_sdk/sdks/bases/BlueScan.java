// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.bases;

import android.os.Message;
import android.os.Looper;
import java.lang.ref.WeakReference;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.bluetooth.le.BluetoothLeScanner;
import android.os.Build;
import android.bluetooth.le.ScanResult;
import com.microlife_sdk.sdks.utils.LogUtils;
import android.text.TextUtils;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.BluetoothAdapter;
import com.microlife_sdk.sdks.interfaces.IScanCallback;

public class BlueScan
{
    private static final String TAG = "BlueScan";
    private static final int WHAT_SCANBLUETOOTH_TIME_OVER = 20;
    private long mScanTime;
    private IScanCallback mIScanCallback;
    private BluetoothAdapter mBluetoothAdapter;
    public boolean isScaningFlag;
    private MyMainHandle myMainHandle;
    private String filterName;
    private final BluetoothAdapter.LeScanCallback mLeScanCallback;
    private final ScanCallback mScanCallback;
    
    public void setFilterName(final String filterName) {
        this.filterName = filterName;
    }
    
    public BlueScan(final Object obj) {
        this.mScanTime = 12000L;
        this.mLeScanCallback = (BluetoothAdapter.LeScanCallback)new BluetoothAdapter.LeScanCallback() {
            public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                if (device != null && !TextUtils.isEmpty((CharSequence)device.getAddress()) && !TextUtils.isEmpty((CharSequence)device.getName())) {
                    LogUtils.i("BlueScan", "---mLeScanCallback--onLeScan:device.name = " + device.getName() + "--address = " + device.getAddress());
                    if (TextUtils.equals((CharSequence)"GOQii Contour", (CharSequence)device.getName()) || TextUtils.equals((CharSequence)"GOQii Essential", (CharSequence)device.getName()) || TextUtils.equals((CharSequence)"GOQii balance", (CharSequence)device.getName()) || TextUtils.equals((CharSequence)"Body Fat-B16", (CharSequence)device.getName()) || TextUtils.equals((CharSequence)"Body Fat-B1", (CharSequence)device.getName()) || TextUtils.equals((CharSequence)"Body Fat-B2", (CharSequence)device.getName()) || device.getName().toLowerCase().contains("lnv_11")) {
                        if (!TextUtils.isEmpty((CharSequence)BlueScan.this.filterName)) {
                            if (TextUtils.equals((CharSequence)BlueScan.this.filterName, (CharSequence)device.getName())) {
                                BlueScan.this.scanOneDevice(device, rssi, scanRecord);
                            }
                        }
                        else {
                            BlueScan.this.scanOneDevice(device, rssi, scanRecord);
                        }
                    }
                }
            }
        };
        this.mScanCallback = new ScanCallback() {
            public void onScanResult(final int callbackType, final ScanResult result) {
                super.onScanResult(callbackType, result);
                BluetoothDevice device = null;
                if (Build.VERSION.SDK_INT >= 21) {
                    device = result.getDevice();
                }
                if (device != null && !TextUtils.isEmpty((CharSequence)device.getAddress()) && !TextUtils.isEmpty((CharSequence)device.getName())) {
                    LogUtils.i("BlueScan", "---mScanCallback--onScanResult:device.name = " + device.getName() + "--address = " + device.getAddress());
                    if (TextUtils.equals((CharSequence)"GOQii Contour", (CharSequence)device.getName()) || TextUtils.equals((CharSequence)"GOQii Essential", (CharSequence)device.getName()) || TextUtils.equals((CharSequence)"GOQii balance", (CharSequence)device.getName()) || TextUtils.equals((CharSequence)"Body Fat-B16", (CharSequence)device.getName()) || TextUtils.equals((CharSequence)"Body Fat-B1", (CharSequence)device.getName()) || TextUtils.equals((CharSequence)"Body Fat-B2", (CharSequence)device.getName()) || device.getName().toLowerCase().contains("lnv_11")) {
                        if (!TextUtils.isEmpty((CharSequence)BlueScan.this.filterName)) {
                            if (TextUtils.equals((CharSequence)BlueScan.this.filterName, (CharSequence)device.getName()) && Build.VERSION.SDK_INT >= 21) {
                                BlueScan.this.scanOneDevice(device, result.getRssi(), result.getScanRecord().getBytes());
                            }
                        }
                        else if (Build.VERSION.SDK_INT >= 21) {
                            BlueScan.this.scanOneDevice(device, result.getRssi(), result.getScanRecord().getBytes());
                        }
                    }
                }
            }
        };
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.myMainHandle = new MyMainHandle(obj);
    }
    
    public void setScanTime(final int scanTime) {
        this.mScanTime = scanTime;
    }
    
    public void setScanCallback(final IScanCallback mIScanCallback) {
        this.mIScanCallback = mIScanCallback;
    }
    
    public void startScanBluetoothDevices(final IScanCallback mIScanCallback) {
        this.setScanCallback(mIScanCallback);
        this.beforeScan();
        this.scanLeDevice(true);
        if (this.myMainHandle != null) {
            this.myMainHandle.sendEmptyMessageDelayed(20, this.mScanTime);
        }
    }
    
    public void startScanBluetoothDevices() {
        if (this.isScaningFlag) {
            LogUtils.i("BlueScan", "\u5df2\u7ecf\u5728\u626b\u63cf\u4e86\uff0c\u65e0\u9700\u91cd\u65b0\u626b\u63cf");
            return;
        }
        this.beforeScan();
        this.scanLeDevice(true);
        if (this.myMainHandle != null) {
            this.myMainHandle.sendEmptyMessageDelayed(20, this.mScanTime);
        }
    }
    
    public void stopScanBluetoothDevices() {
        this.scanLeDevice(false);
    }
    
    private void scanLeDevice(final boolean enable) {
        if (this.mBluetoothAdapter == null) {
            LogUtils.i("BlueScan", "\u4f60\u7684\u624b\u673a\u4e0d\u652f\u6301\u84dd\u7259\u8bbe\u5907");
            return;
        }
        if (enable) {
            LogUtils.i("BlueScan", "\u5f00\u59cb\u626b\u63cf\u84dd\u7259\u8bbe\u5907\u3002\u3002\u3002");
            this.isScaningFlag = true;
            if (Build.VERSION.SDK_INT < 21) {
                this.mBluetoothAdapter.startLeScan(this.mLeScanCallback);
            }
            else {
                final BluetoothLeScanner bluetoothLeScanner = this.mBluetoothAdapter.getBluetoothLeScanner();
                if (bluetoothLeScanner != null) {
                    bluetoothLeScanner.startScan(this.mScanCallback);
                }
            }
        }
        else {
            LogUtils.i("BlueScan", "\u505c\u6b62\u626b\u63cf\u84dd\u7259\u8bbe\u5907");
            this.isScaningFlag = false;
            if (Build.VERSION.SDK_INT < 21) {
                this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
            }
            else {
                final BluetoothLeScanner bluetoothLeScanner = this.mBluetoothAdapter.getBluetoothLeScanner();
                if (bluetoothLeScanner != null) {
                    bluetoothLeScanner.stopScan(this.mScanCallback);
                }
            }
        }
    }
    
    public void exitScan() {
        LogUtils.i("BlueScan", "-------exitScan------");
        if (this.isScaningFlag) {
            if (this.myMainHandle != null) {
                this.myMainHandle.removeMessages(20);
                this.myMainHandle.exitHandle();
            }
            this.scanLeDevice(false);
            if (this.mIScanCallback != null) {
                LogUtils.i("BlueScan", "------------overScan()");
                this.mIScanCallback.overScan();
            }
        }
    }
    
    private void beforeScan() {
        if (this.mIScanCallback != null) {
            LogUtils.i("BlueScan", "------------beforeScan()");
            this.mIScanCallback.beforeScan();
        }
    }
    
    private void scanOneDevice(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        if (this.mIScanCallback != null) {
            this.mIScanCallback.scanOneDevice(device, rssi, scanRecord);
        }
    }
    
    private void overScan() {
        this.scanLeDevice(false);
        if (this.mIScanCallback != null) {
            LogUtils.i("BlueScan", "------------overScan()");
            this.mIScanCallback.overScan();
        }
    }
    
    public void realse() {
        this.stopScanBluetoothDevices();
        if (this.myMainHandle != null) {
            this.myMainHandle.exitHandle();
            this.myMainHandle = null;
        }
    }
    
    @SuppressLint({ "HandlerLeak" })
    private class MyMainHandle extends Handler
    {
        private WeakReference<Object> mWeakReference;
        
        MyMainHandle(final Object obj) {
            super(Looper.getMainLooper());
            this.mWeakReference = new WeakReference<Object>(obj);
        }
        
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (this.mWeakReference == null || this.mWeakReference.get() == null) {
                return;
            }
            LogUtils.i("BlueScan", Thread.currentThread().getName() + "--MyMainHandle()--msg.what" + msg.what);
            switch (msg.what) {
                case 20: {
                    BlueScan.this.overScan();
                    break;
                }
            }
        }
        
        private void exitHandle() {
            this.removeCallbacksAndMessages((Object)null);
            if (this.mWeakReference != null) {
                this.mWeakReference.clear();
                this.mWeakReference = null;
            }
        }
    }
}
