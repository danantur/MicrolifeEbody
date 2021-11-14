// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.managers;

import com.microlife_sdk.sdks.bases.BlueManager;
import android.bluetooth.BluetoothAdapter;
import android.os.Message;
import com.microlife_sdk.sdks.bean.scale.OfflineMeasureResult;
import com.microlife_sdk.sdks.bean.scale.ScaleUserInfo;
import com.microlife_sdk.sdks.bean.scale.ScaleMeasureResult;
import android.text.TextUtils;
import com.microlife_sdk.sdks.enums.ProductStyle;
import com.microlife_sdk.sdks.utils.LogUtils;
import android.content.Context;
import com.microlife_sdk.sdks.interfaces.IBlueStationListener;
import com.microlife_sdk.sdks.interfaces.scale.IScaleMeasureCallback;
import com.microlife_sdk.sdks.interfaces.IConnectStationCallback;
import com.microlife_sdk.sdks.interfaces.IScanCallback;
import com.microlife_sdk.sdks.models.products.scale.ScaleProduct;
import com.microlife_sdk.sdks.bases.BlueScan;
import android.bluetooth.BluetoothDevice;
import com.microlife_sdk.sdks.utils.BaseHandle;

public class ScaleManager extends BaseHandle
{
    private static final String TAG = "ScaleManager";
    private BluetoothDevice device;
    private BlueScan mBlueScan;
    private ScaleProduct mScaleProduct;
    private boolean isWorkFlag;
    private static final int WHAT_RESTART_SCAN = 1;
    private static final int WHAT_AUTO_CHECK = 2;
    private static final int WHAT_START_SCAN = 3;
    private IScanCallback mScanCallback;
    private IConnectStationCallback mIConnectStationCallback;
    private IScaleMeasureCallback mScaleMeasureCallback;
    String connectName;
    private IConnectStationCallback mConnectStationCallback;
    private IScaleMeasureCallback mIScaleMeasureCallback;
    private String mustConnectBlueAddress;
    private IBlueStationListener mIBlueStationListener;
    
    public ScaleManager(final Context context) {
        super(new Object[] { context });
        this.mScanCallback = new IScanCallback() {
            @Override
            public void beforeScan() {
                LogUtils.i("ScaleManager", "---beforeScan---");
                ScaleManager.this.device = null;
            }
            
            @Override
            public void scanOneDevice(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                ScaleManager.this.post(new Runnable() {
                    @Override
                    public void run() {
                        ScaleManager.this.recordScanDevice(device);
                    }
                });
            }
            
            @Override
            public void overScan() {
                LogUtils.i("ScaleManager", "---\u626b\u63cf\u7ed3\u675f---");
                if (ScaleManager.this.device == null) {
                    LogUtils.i("ScaleManager", "---\u626b\u63cf\u7ed3\u675f device = null \u9700\u8981\u91cd\u65b0\u626b\u63cf");
                    ScaleManager.this.removeMessages(1);
                    ScaleManager.this.sendEmptyMessageDelayed(1, 3000L);
                }
            }
        };
        this.mConnectStationCallback = new IConnectStationCallback() {
            @Override
            public void onConnected(final ProductStyle productStyle, final BluetoothDevice device) {
                if (device == null) {
                    return;
                }
                final String name = device.getName();
                final String address = device.getAddress();
                LogUtils.i("ScaleManager", "--\u8fde\u63a5\u6210\u529f--onConnected--device.name = " + name + "---device.address = " + address);
                if (ScaleManager.this.mBlueScan != null) {
                    ScaleManager.this.mBlueScan.exitScan();
                }
                ScaleManager.this.connectName = name;
                ScaleManager.this.mScaleProduct.setConnectName(name);
                ScaleManager.this.mScaleProduct.setConnectAddress(address);
                if (TextUtils.equals((CharSequence)"Body Fat-B16", (CharSequence)name) && ScaleManager.this.mScaleProduct != null) {
                    ScaleManager.this.mScaleProduct.syncSystemClock();
                }
                if (ScaleManager.this.mIConnectStationCallback != null) {
                    ScaleManager.this.mIConnectStationCallback.onConnected(ProductStyle.SCALE, device);
                }
                ScaleManager.this.stopScan();
            }
            
            @Override
            public void onConnecting(final ProductStyle productStyle, final BluetoothDevice device) {
                if (ScaleManager.this.mIConnectStationCallback != null) {
                    ScaleManager.this.mIConnectStationCallback.onConnecting(ProductStyle.SCALE, device);
                }
            }
            
            @Override
            public void onDisConnected(final ProductStyle productStyle) {
                LogUtils.i("ScaleManager", "-------------\u8fde\u63a5\u65ad\u5f00");
                if (ScaleManager.this.mIConnectStationCallback != null) {
                    ScaleManager.this.mIConnectStationCallback.onDisConnected(ProductStyle.SCALE);
                }
            }
        };
        this.mIScaleMeasureCallback = new IScaleMeasureCallback() {
            @Override
            public void onReceiveMeasureResult(final ScaleMeasureResult result) {
                if (ScaleManager.this.mScaleProduct != null) {
                    ScaleManager.this.mScaleProduct.sendMesureDataACK();
                    ScaleUserInfo.getScaleUserInfo().setWeight(result.weight);
                    ScaleUserInfo.getScaleUserInfo().setImpedance((int)result.getResistance());
                    ScaleManager.this.mScaleProduct.updateScaleUserInfo(ScaleUserInfo.getScaleUserInfo());
                }
                if (ScaleManager.this.mScaleMeasureCallback != null) {
                    ScaleManager.this.mScaleMeasureCallback.onReceiveMeasureResult(result);
                }
            }
            
            @Override
            public void onWeightOverLoad() {
                if (ScaleManager.this.mScaleMeasureCallback != null) {
                    ScaleManager.this.mScaleMeasureCallback.onWeightOverLoad();
                }
            }
            
            @Override
            public void onReceiveHistoryRecord(final OfflineMeasureResult result) {
                if (ScaleManager.this.mScaleProduct != null) {
                    ScaleManager.this.mScaleProduct.sendGetHistoryDataACK();
                }
                if (ScaleManager.this.mScaleMeasureCallback != null) {
                    ScaleManager.this.mScaleMeasureCallback.onReceiveHistoryRecord(result);
                }
            }
            
            @Override
            public void onHistoryDownloadDone() {
                if (ScaleManager.this.mScaleMeasureCallback != null) {
                    ScaleManager.this.mScaleMeasureCallback.onHistoryDownloadDone();
                }
            }
            
            @Override
            public void onLowPower() {
                if (ScaleManager.this.mScaleMeasureCallback != null) {
                    ScaleManager.this.mScaleMeasureCallback.onLowPower();
                }
            }
            
            @Override
            public void setUserInfoSuccess() {
                if (ScaleManager.this.mScaleMeasureCallback != null) {
                    ScaleManager.this.mScaleMeasureCallback.setUserInfoSuccess();
                }
            }
            
            @Override
            public void receiveTime(final long time) {
                if (ScaleManager.this.mScaleProduct != null) {
                    ScaleManager.this.mScaleProduct.sendUserInfo(ScaleUserInfo.getScaleUserInfo());
                }
                if (ScaleManager.this.mScaleMeasureCallback != null) {
                    ScaleManager.this.mScaleMeasureCallback.receiveTime(time);
                }
            }
            
            @Override
            public void onScaleWake() {
                if ((TextUtils.equals((CharSequence)"Body Fat-B1", (CharSequence)ScaleManager.this.connectName) || TextUtils.equals((CharSequence)"Body Fat-B2", (CharSequence)ScaleManager.this.connectName) || (!TextUtils.isEmpty((CharSequence)ScaleManager.this.connectName) && ScaleManager.this.connectName.contains("lnv_11")) || TextUtils.equals((CharSequence)"GOQii Contour", (CharSequence)ScaleManager.this.connectName) || TextUtils.equals((CharSequence)"GOQii Essential", (CharSequence)ScaleManager.this.connectName) || TextUtils.equals((CharSequence)"GOQii balance", (CharSequence)ScaleManager.this.connectName)) && ScaleManager.this.mScaleProduct != null) {
                    ScaleManager.this.mScaleProduct.syncSystemClock();
                }
                if (ScaleManager.this.mScaleMeasureCallback != null) {
                    ScaleManager.this.mScaleMeasureCallback.onScaleWake();
                }
            }
            
            @Override
            public void onScaleSleep() {
                if (ScaleManager.this.mScaleMeasureCallback != null) {
                    ScaleManager.this.mScaleMeasureCallback.onScaleSleep();
                }
            }
        };
        this.mIBlueStationListener = new IBlueStationListener() {
            @Override
            public void STATE_OFF() {
            }
            
            @Override
            public void STATE_TURNING_OFF() {
            }
            
            @Override
            public void STATE_ON() {
                ScaleManager.this.removeMessages(2);
                ScaleManager.this.sendEmptyMessageDelayed(2, 0L);
            }
            
            @Override
            public void STATE_TURNING_ON() {
            }
        };
        this.init(context);
        this.addBluetoothStationListener(this.mIBlueStationListener);
    }
    
    public void startWork() {
        if (this.isWorkFlag) {
            LogUtils.i("ScaleManager", "ScaleManager\u5df2\u7ecf\u5728\u5de5\u4f5c\u4e86");
            return;
        }
        this.isWorkFlag = true;
        this.scanBlueDevices();
        this.removeMessages(2);
        this.sendEmptyMessageDelayed(2, 7000L);
    }
    
    public void stopWork() {
        this.stopAutoCheck();
        this.disConnectDevice(true);
        this.isWorkFlag = false;
    }
    
    @Override
    public void handleMsg(final Message msg) {
        super.handleMsg(msg);
        if (msg.what == 1) {
            LogUtils.i("ScaleManager", "---\u6536\u5230\u91cd\u65b0\u626b\u63cf\u7684\u6307\u4ee4");
            this.scanBlueDevices();
        }
        else if (msg.what == 2) {
            this.removeMessages(2);
            this.sendEmptyMessageDelayed(2, 7000L);
            this.autoCheck();
        }
        else if (msg.what == 3 && this.mBlueScan != null) {
            LogUtils.i("ScaleManager", "---\u6536\u5230\u5f00\u59cb\u626b\u63cf\u7684\u6307\u4ee4");
            this.mBlueScan.startScanBluetoothDevices(this.mScanCallback);
        }
    }
    
    private void scanBlueDevices() {
        if (this.mBlueScan != null) {
            this.mBlueScan.stopScanBluetoothDevices();
        }
        this.removeMessages(3);
        this.sendEmptyMessageDelayed(3, 1000L);
    }
    
    private void recordScanDevice(final BluetoothDevice device) {
        final String name = device.getName();
        final String address = device.getAddress();
        LogUtils.i("ScaleManager", "\u626b\u63cf\u5230\u8bbe\u5907--Name = " + name + "---Addrss = " + address);
        if (!TextUtils.isEmpty((CharSequence)this.mustConnectBlueAddress) && !TextUtils.equals((CharSequence)this.mustConnectBlueAddress, (CharSequence)address)) {
            return;
        }
        LogUtils.i("ScaleManager", "--\u627e\u5230\u4e86\u9700\u8981\u8fde\u63a5\u7684\u84dd\u7259\u8bbe\u5907------Name = " + name + "---Addrss = " + address + "---\u51c6\u5907\u8fde\u63a5---");
        this.device = device;
        if (this.mBlueScan != null) {
            this.mBlueScan.exitScan();
        }
        this.connectBluetooth(device);
    }
    
    private void connectBluetooth(final BluetoothDevice bluetoothDevice) {
        if (this.mScaleProduct != null) {
            this.mScaleProduct.connectDevice(bluetoothDevice);
        }
    }
    
    private void connectBluetooth(final String address) {
        if (this.mScaleProduct != null) {
            this.mScaleProduct.connectDevice(address);
        }
    }
    
    private void autoCheck() {
        LogUtils.i("ScaleManager", "---\u81ea\u52a8\u68c0\u6d4b\u673a\u5236\u6b63\u5728\u8fd0\u884c--autoCheck\uff08\uff09");
        if (this.mScaleProduct == null) {
            LogUtils.i("ScaleManager", "\u81ea\u52a8\u68c0\u6d4b mScaleProduct = null \u6b64\u6b21 return");
            return;
        }
        if (this.mScaleProduct.isConnect()) {
            LogUtils.i("ScaleManager", "\u81ea\u52a8\u68c0\u6d4b \u8bbe\u5907--\u5df2\u7ecf\u8fde\u63a5 \u6b64\u6b21return");
            return;
        }
        LogUtils.i("ScaleManager", "\u81ea\u52a8\u68c0\u6d4b \u8bbe\u5907\u6ca1\u6709\u8fde\u63a5\u4e2d \u51c6\u5907\u91cd\u65b0\u626b\u63cf");
        this.scanBlueDevices();
    }
    
    private void init(final Context context) {
        this.mBlueScan = new BlueScan(context);
        this.mScaleProduct = new ScaleProduct(context, 300L);
        this.setListeners();
    }
    
    private void setListeners() {
        this.mScaleProduct.setBlueConnectStationCallback(this.mConnectStationCallback);
        this.mScaleProduct.setmIMeasureResultCallback(this.mIScaleMeasureCallback);
    }
    
    public void setConnectStationCallback(final IConnectStationCallback mIConnectStationCallback) {
        this.mIConnectStationCallback = mIConnectStationCallback;
    }
    
    public void setScaleMeasureCallback(final IScaleMeasureCallback mScaleMeasureCallback) {
        this.mScaleMeasureCallback = mScaleMeasureCallback;
    }
    
    @Override
    public void exit() {
        if (this.mBlueScan != null) {
            if (this.mBlueScan.isScaningFlag) {
                this.mBlueScan.stopScanBluetoothDevices();
            }
            this.mBlueScan = null;
        }
        this.removeBluetoothStationListener(this.mIBlueStationListener);
        this.stopAutoCheck();
        if (this.mScaleProduct != null) {
            this.mScaleProduct.exit();
            this.mScaleProduct = null;
        }
    }
    
    private void stopAutoCheck() {
        this.removeMessages(2);
        if (this.mBlueScan != null) {
            this.mBlueScan.stopScanBluetoothDevices();
        }
    }
    
    private void stopScan() {
        if (this.mBlueScan != null) {
            this.mBlueScan.stopScanBluetoothDevices();
        }
    }
    
    public String getMustConnectBlueAddress() {
        return this.mustConnectBlueAddress;
    }
    
    public boolean setMustConnectBlueAddress(final String mustConnectBlueAddress) {
        if (TextUtils.isEmpty((CharSequence)mustConnectBlueAddress)) {
            LogUtils.i("ScaleManager", "Enter mustConnectBlueAddress as an empty error.Setup failed");
            return false;
        }
        final boolean isBlueAddress = BluetoothAdapter.checkBluetoothAddress(mustConnectBlueAddress);
        if (!isBlueAddress) {
            LogUtils.i("ScaleManager", "The address string entered is not a bluetooth address");
            return false;
        }
        if (this.isConnected()) {
            final String nowConnectAddress = this.getConnectAddrss();
            if (!TextUtils.equals((CharSequence)nowConnectAddress, (CharSequence)mustConnectBlueAddress)) {
                this.disConnectDevice();
            }
        }
        this.mustConnectBlueAddress = mustConnectBlueAddress;
        return true;
    }
    
    public static boolean checkBluetoothAddress(final String address) {
        return BluetoothAdapter.checkBluetoothAddress(address);
    }
    
    public boolean isConnected() {
        return this.mScaleProduct != null && this.mScaleProduct.isConnect();
    }
    
    public String getConnectAddrss() {
        if (this.mScaleProduct != null) {
            return this.mScaleProduct.getConnectBluetoothAddress();
        }
        return null;
    }
    
    private void disConnectDevice() {
        if (this.mScaleProduct != null) {
            this.mScaleProduct.disConnect(true);
        }
    }
    
    private void disConnectDevice(final boolean sendFront) {
        if (this.mScaleProduct != null) {
            this.mScaleProduct.disConnect(sendFront);
        }
    }
    
    public boolean isSupportBluetooth() {
        return BlueManager.getInstance().isSupportBluetooth();
    }
    
    public boolean isBluetoothOpen() {
        return BlueManager.getInstance().isBluetoothOpen();
    }
    
    public void addBluetoothStationListener(final IBlueStationListener listener) {
        BlueManager.getInstance().addBluetoothStationListener(listener);
    }
    
    public void removeBluetoothStationListener(final IBlueStationListener listener) {
        BlueManager.getInstance().removeBluetoothStationListener(listener);
    }
    
    public void updateUserInfo(final ScaleUserInfo userInfo) {
        if (this.mScaleProduct != null) {
            this.mScaleProduct.needUpdateUserInfo2Front(true);
        }
        if (this.isConnected() && this.mScaleProduct != null) {
            this.mScaleProduct.sendUserInfo(userInfo);
        }
    }
    
    public void requestOffMesureData(final String userId, final int height, final int sex, final int age, final int roleType) {
        if (this.mScaleProduct != null) {
            this.mScaleProduct.sendGetHistoryDataRequest(userId, height, sex, age, roleType);
        }
    }
    
    public void deleteAllUserInfo() {
        if (this.mScaleProduct != null) {
            this.mScaleProduct.deleteAllUserInfo();
        }
    }
    
    public static float kg2lb(final float kgValue) {
        return ScaleProduct.kg2lb(kgValue);
    }
    
    public static float lb2kg(final float lbValue) {
        return ScaleProduct.lb2kg(lbValue);
    }
    
    public boolean isWorkFlag() {
        return this.isWorkFlag;
    }
}
