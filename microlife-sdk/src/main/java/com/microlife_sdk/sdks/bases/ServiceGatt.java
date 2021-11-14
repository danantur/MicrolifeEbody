// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.bases;

import android.os.SystemClock;
import com.microlife_sdk.sdks.utils.TimeUtils;
import android.os.Looper;
import java.lang.ref.WeakReference;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.bluetooth.BluetoothAdapter;
import java.util.Collection;
import java.util.LinkedList;
import com.microlife_sdk.sdks.utils.BytesUtils;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattCharacteristic;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import android.text.TextUtils;
import com.microlife_sdk.sdks.utils.LogUtils;
import android.bluetooth.BluetoothGattCallback;
import com.microlife_sdk.sdks.interfaces.IDataReadWriteCallback;
import com.microlife_sdk.sdks.interfaces.IConnectStationCallback;
import com.microlife_sdk.sdks.enums.ConnectStatus;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothDevice;
import java.util.Queue;
import com.microlife_sdk.sdks.bean.SendMessage;
import com.microlife_sdk.sdks.enums.ProductStyle;
import android.content.Context;

public class ServiceGatt
{
    private String TAG;
    private long ServiceGatt_SendThread_interval;
    private static final int WHAT_SEND_HEART = 99;
    private static final int WHAT_GATT_RECONNECT = 1;
    private static final int WHAT_CONNECTED = 2;
    private static final int WHAT_DISCONNECTED = 3;
    private static final int WHAT_CONNECTING = 4;
    private Context context;
    private ProductStyle productStyle;
    private String serviceUUID;
    private String[] deviceReadNotifyUUIDs;
    private String[] deviceWriteUUIDs;
    private SendMessage heartSendMessage;
    private Queue<SendMessage> sendMessagesBufferQueue;
    private SendThread mSendThread;
    private MyHandle myHandle;
    private BluetoothDevice connectBluetoothDevice;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattService mBluetoothGattService;
    private ConnectStatus connectStatus;
    private IConnectStationCallback mIConnectStationCallback;
    private IDataReadWriteCallback mIDataReadWriteCallback;
    private long lastDisconnectTime;
    private static final int DISCONNECT_TIME_INTERVAL = 800;
    private long lastSendMsgTime;
    private int mBluetoothGattConnectCount;
    private BluetoothGattCallback mGattCallback;
    private static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    private boolean sendThreadRunFlag;
    
    void setConnectCallback(final IConnectStationCallback mIConnectStationCallback) {
        this.mIConnectStationCallback = mIConnectStationCallback;
    }
    
    public void setIDataReadWriteCallback(final IDataReadWriteCallback mIDataReadWriteCallback) {
        this.mIDataReadWriteCallback = mIDataReadWriteCallback;
    }
    
    ServiceGatt(final Context context, final ProductStyle productStyle, final String serviceUUID, final String[] writeUUIDs, final String[] readUUIDs, final boolean hasSendThread, final long serviceGatt_SendThread_interval, final SendMessage heartSendMessage) {
        this.TAG = "ServiceGatt";
        this.ServiceGatt_SendThread_interval = 300L;
        this.sendMessagesBufferQueue = null;
        this.connectStatus = ConnectStatus.DISCONNECTED;
        this.lastDisconnectTime = 0L;
        this.mGattCallback = new BluetoothGattCallback() {
            public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                LogUtils.i(ServiceGatt.this.TAG, "---onConnectionStateChange, status:" + status + ",new status:" + newState);
                if (status == 0) {
                    if (newState == 2) {
                        if (gatt != null) {
                            final boolean success = gatt.discoverServices();
                            ServiceGatt.this.connectStatus = ConnectStatus.CONNECTING;
                            ServiceGatt.this.sendMessage(4, ServiceGatt.this.connectBluetoothDevice);
                            LogUtils.i(ServiceGatt.this.TAG, "---onConnectionStateChange, ----> gatt.discoverServices() success = " + success);
                        }
                    }
                    else if (newState == 0) {
                        LogUtils.i(ServiceGatt.this.TAG, "---onConnectionStateChange,newState == BluetoothProfile.STATE_DISCONNECTED");
                        if (System.currentTimeMillis() - ServiceGatt.this.lastDisconnectTime > 800L) {
                            ServiceGatt.this.lastDisconnectTime = System.currentTimeMillis();
                            ServiceGatt.this.connectStatus = ConnectStatus.DISCONNECTED;
                            ServiceGatt.this.sendEmptyMessage(3, 0L);
                        }
                    }
                }
                else {
                    LogUtils.i(ServiceGatt.this.TAG, "---onConnectionStateChange,status!=BluetoothGatt.GATT_SUCCESS");
                    if (System.currentTimeMillis() - ServiceGatt.this.lastDisconnectTime > 800L) {
                        ServiceGatt.this.lastDisconnectTime = System.currentTimeMillis();
                        ServiceGatt.this.connectStatus = ConnectStatus.DISCONNECTED;
                        ServiceGatt.this.sendEmptyMessage(3, 0L);
                    }
                }
            }
            
            public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
                super.onServicesDiscovered(gatt, status);
                LogUtils.i(ServiceGatt.this.TAG, "---onServicesDiscovered status = " + status);
                if (status == 0) {
                    final List<BluetoothGattService> gattServices = (List<BluetoothGattService>)gatt.getServices();
                    String serviceUUID2 = "";
                    for (final BluetoothGattService service : gattServices) {
                        final String sid = service.getUuid().toString();
                        LogUtils.i(ServiceGatt.this.TAG, "-----onServicesDiscovered-------service uuid:" + sid);
                        if (sid.contains(ServiceGatt.this.serviceUUID)) {
                            serviceUUID2 = sid;
                            LogUtils.i(ServiceGatt.this.TAG, "-----onServicesDiscovered-------service uuid: \u5339\u914d\u6210\u529f serviceUUID = " + serviceUUID2);
                        }
                    }
                    if (TextUtils.isEmpty((CharSequence)serviceUUID2)) {
                        LogUtils.i(ServiceGatt.this.TAG, "-----onServicesDiscovered-------service\u6ca1\u627e\u5230: \u5339\u914d\u5931\u8d25");
                        return;
                    }
                    ServiceGatt.this.mBluetoothGattService = null;
                    ServiceGatt.this.mBluetoothGattService = gatt.getService(UUID.fromString(serviceUUID2));
                    if (ServiceGatt.this.mBluetoothGattService == null) {
                        LogUtils.i(ServiceGatt.this.TAG, "---------service-----\u5339\u914d\u5931\u8d25");
                        ServiceGatt.this.sendEmptyMessage(3, 0L);
                        ServiceGatt.this.connectStatus = ConnectStatus.DISCONNECTED;
                        ServiceGatt.this.sendEmptyMessage(1, 0L);
                        return;
                    }
                    LogUtils.i(ServiceGatt.this.TAG, "---------service-----\u5339\u914d\u6210\u529f");
                    final BluetoothGattCharacteristic characteristic = ServiceGatt.this.mBluetoothGattService.getCharacteristic(UUID.fromString(ServiceGatt.this.deviceReadNotifyUUIDs[0]));
                    if (characteristic != null) {
                        ServiceGatt.this.connectStatus = ConnectStatus.CONNECTED;
                        ServiceGatt.this.setCharacteristicNotification(characteristic, true);
                    }
                    else {
                        LogUtils.i(ServiceGatt.this.TAG, "---------service-----characteristic = null \u51fa\u73b0\u5f02\u5e38");
                    }
                }
                else {
                    LogUtils.i(ServiceGatt.this.TAG, "---onServicesDiscovered status != BluetoothGatt.GATT_SUCCESS");
                    ServiceGatt.this.connectStatus = ConnectStatus.DISCONNECTED;
                    ServiceGatt.this.sendEmptyMessage(3, 0L);
                }
            }
            
            public void onDescriptorWrite(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
                LogUtils.i(ServiceGatt.this.TAG, "--------------onDescriptorWrite---------------status = " + status);
                if (status != 0) {
                    LogUtils.i(ServiceGatt.this.TAG, "onDescriptorWrite, status not 0, do disconnect.");
                    ServiceGatt.this.connectStatus = ConnectStatus.DISCONNECTED;
                    ServiceGatt.this.sendEmptyMessage(3, 0L);
                }
                else {
                    final UUID uuid = descriptor.getCharacteristic().getUuid();
                    LogUtils.i(ServiceGatt.this.TAG, "-----onDescriptorWrite------UUID = " + uuid + ", deviceReadNotifyUUIDs.length = " + ServiceGatt.this.deviceReadNotifyUUIDs.length);
                    if (ServiceGatt.this.deviceReadNotifyUUIDs.length > 1) {
                        ServiceGatt.this.setMutiNotify(uuid, 1);
                    }
                    if (ServiceGatt.this.deviceReadNotifyUUIDs.length > 2) {
                        ServiceGatt.this.setMutiNotify(uuid, 2);
                    }
                    if (ServiceGatt.this.deviceReadNotifyUUIDs.length > 3) {
                        ServiceGatt.this.setMutiNotify(uuid, 3);
                    }
                    ServiceGatt.this.sendMessage(2, ServiceGatt.this.connectBluetoothDevice);
                }
            }
            
            public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                LogUtils.i(ServiceGatt.this.TAG, "received--------------onCharacteristicRead---------------status = " + status + "\n" + BytesUtils.bytes2HexStr(characteristic.getValue()));
            }
            
            public void onCharacteristicChanged(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                LogUtils.i(ServiceGatt.this.TAG, "received----------onCharacteristicChanged () = " + BytesUtils.bytes2HexStr(characteristic.getValue()));
                if (ServiceGatt.this.isConnect() && ServiceGatt.this.mIDataReadWriteCallback != null) {
                    ServiceGatt.this.mIDataReadWriteCallback.onCharacteristicChanged(characteristic.getValue());
                }
            }
        };
        this.sendThreadRunFlag = true;
        this.productStyle = productStyle;
        this.TAG = productStyle.getProductName() + "ServiceGatt";
        this.serviceUUID = serviceUUID.substring(0, 23);
        this.deviceWriteUUIDs = writeUUIDs;
        this.deviceReadNotifyUUIDs = readUUIDs;
        this.context = context;
        this.ServiceGatt_SendThread_interval = serviceGatt_SendThread_interval;
        this.heartSendMessage = heartSendMessage;
        this.myHandle = new MyHandle(context);
        this.sendMessagesBufferQueue = new LinkedList<SendMessage>();
        if (hasSendThread) {
            this.sendThreadRunFlag = true;
            (this.mSendThread = new SendThread()).start();
        }
    }
    
    public boolean addSendMsg(final SendMessage message) {
        return this.sendMessagesBufferQueue != null && this.sendMessagesBufferQueue.add(message);
    }
    
    public boolean addSendMsgs(final List<SendMessage> messages) {
        return this.sendMessagesBufferQueue != null && this.sendMessagesBufferQueue.addAll((Collection<? extends SendMessage>) messages);
    }
    
    void connectBluetoothDevice(final BluetoothDevice connectBluetoothDevice) {
        if (connectBluetoothDevice == null) {
            return;
        }
        if (this.isConnect()) {
            if (TextUtils.equals((CharSequence)connectBluetoothDevice.getAddress(), (CharSequence)this.connectBluetoothDevice.getAddress())) {
                LogUtils.i(this.TAG, connectBluetoothDevice.getAddress() + "--A1--\u5df2\u7ecf\u8fde\u63a5\u65e0\u9700\u91cd\u65b0\u8fde\u63a5 \u6ca1\u6709\u4efb\u4f55\u64cd\u4f5c");
            }
            else {
                LogUtils.i(this.TAG, connectBluetoothDevice.getAddress() + "--A2--\u6b63\u5728\u8fde\u63a5\u7740 \u5730\u5740\u8ddf\u4f20\u8fdb\u6765\u7684\u5730\u5740\u662f \u4e0d\u4e00\u6837\u7684 \u6267\u884c\u6e05\u9664\u7f13\u5b58clearBlueGatt(true)\u548c\u5f00\u59cb\u8fde\u63a5gattConnect()");
                this.clearBlueGatt(true);
                this.connectBluetoothDevice = connectBluetoothDevice;
                this.gattConnect();
            }
        }
        else if (this.mBluetoothGatt != null && TextUtils.equals((CharSequence)this.mBluetoothGatt.getDevice().getAddress(), (CharSequence)connectBluetoothDevice.getAddress())) {
            LogUtils.i(this.TAG, connectBluetoothDevice.getAddress() + "--B1--\u6ca1\u6709\u8fde\u63a5\u7740 \u5e76\u4e14mBluetoothGatt\u4e0d\u4e3a\u7a7a\u4e14\u4e0a\u4e00\u6b21\u8fde\u63a5\u7740\u7684\u5730\u5740\u8ddf\u8fd9\u6b21\u662f\u4e00\u6837\u7684 \u6267\u884cmBluetoothGatt.connect()");
            ++this.mBluetoothGattConnectCount;
            if (this.mBluetoothGattConnectCount < 2) {
                LogUtils.i(this.TAG, "---B11-\u91c7\u7528\u7684\u662fmBluetoothGatt.connect()\u8fde\u63a5\u65b9\u5f0f");
                this.mBluetoothGatt.connect();
            }
            else {
                LogUtils.i(this.TAG, "---B11-\u91c7\u7528\u7684\u662fclearBlueGatt\uff08\uff09---gattConnect()\u8fde\u63a5\u65b9\u5f0f");
                this.clearBlueGatt(false);
                this.connectBluetoothDevice = connectBluetoothDevice;
                this.gattConnect();
            }
        }
        else {
            LogUtils.i(this.TAG, connectBluetoothDevice.getAddress() + "--B2--\u6ca1\u6709\u8fde\u63a5\u7740 \u5730\u5740\u8ddf\u4f20\u8fdb\u6765\u7684\u5730\u5740\u662f \u4e0d\u4e00\u6837\u7684 \u6267\u884c\u6e05\u9664\u7f13\u5b58clearBlueGatt(true)\u548c\u5f00\u59cb\u8fde\u63a5gattConnect()");
            this.clearBlueGatt(false);
            this.connectBluetoothDevice = connectBluetoothDevice;
            this.gattConnect();
        }
    }
    
    private void gattConnect() {
        if (this.connectBluetoothDevice != null) {
            this.mBluetoothGatt = this.connectBluetoothDevice.connectGatt(this.context, false, this.mGattCallback);
            if (this.mBluetoothGatt != null) {
                LogUtils.i(this.TAG, "gattConnect2Disconnect------------\u51c6\u5907\u8fde\u63a5\u7684\u662f:" + this.mBluetoothGatt.getDevice().getName());
            }
        }
    }
    
    void clearBlueGatt(final boolean sendfront) {
        LogUtils.i(this.TAG, "gattConnect2Disconnect------------clearBlueGatt\uff08sendfront\uff09 sendfront = " + sendfront);
        try {
            if (this.mBluetoothGatt != null && BluetoothAdapter.getDefaultAdapter() != null && BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                this.mBluetoothGatt.disconnect();
                this.mBluetoothGatt.close();
                this.mBluetoothGatt = null;
                this.mBluetoothGattService = null;
                this.mBluetoothGattConnectCount = 0;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.connectStatus = ConnectStatus.DISCONNECTED;
        if (sendfront) {
            this.sendEmptyMessage(3, 0L);
        }
    }
    
    void disConnect(final boolean sendfront) {
        try {
            if (this.mBluetoothGatt != null) {
                this.mBluetoothGatt.disconnect();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.connectStatus = ConnectStatus.DISCONNECTED;
        if (sendfront) {
            this.sendEmptyMessage(3, 0L);
        }
    }
    
    private void clearBlueGatt() {
        this.clearBlueGatt(true);
    }
    
    private void sendEmptyMessage(final int what, final long delay) {
        if (this.myHandle != null) {
            this.myHandle.sendEmptyMessageDelayed(what, delay);
        }
    }
    
    private void sendMessage(final int what, final Object object) {
        if (this.myHandle != null) {
            final Message message = this.myHandle.obtainMessage();
            message.what = what;
            message.obj = object;
            this.myHandle.sendMessage(message);
        }
    }
    
    private void setMutiNotify(final UUID uuid, final int channelNumber) {
        if (this.mBluetoothGattService != null && uuid.toString().equals(this.deviceReadNotifyUUIDs[channelNumber - 1])) {
            LogUtils.i(this.TAG, "-----onDescriptorWrite---characteristic " + (channelNumber - 1) + "----set notifty characteristic " + channelNumber);
            final BluetoothGattCharacteristic characteristic = this.mBluetoothGattService.getCharacteristic(UUID.fromString(this.deviceReadNotifyUUIDs[channelNumber]));
            if (characteristic != null) {
                this.setCharacteristicNotification(characteristic, true);
            }
        }
    }
    
    private void setCharacteristicNotification(final BluetoothGattCharacteristic characteristic, final boolean enable) {
        if (this.mBluetoothGatt != null && characteristic != null) {
            this.mBluetoothGatt.setCharacteristicNotification(characteristic, enable);
            final BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            if (descriptor != null) {
                LogUtils.i(this.TAG, "mBluetoothGatt.writeDescriptor.");
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                this.mBluetoothGatt.writeDescriptor(descriptor);
            }
        }
        else {
            LogUtils.i(this.TAG, "setCharacteristicNotification-mBluetoothGatt = null \u72b6\u6001\u5f02\u5e38");
        }
    }
    
    private boolean writeData(final byte[] data) {
        if (data == null) {
            LogUtils.e(this.TAG, "\u5199\u5165\u7684\u6570\u636e\u4e0d\u80fd\u4e3a\u7a7a");
            return false;
        }
        if (this.mBluetoothGatt == null) {
            LogUtils.e(this.TAG, "----mBluetoothGatt = null \u5199\u5165\u5931\u8d25");
            return false;
        }
        if (this.mBluetoothGattService == null) {
            LogUtils.e(this.TAG, "----mBluetoothGattService = null \u5199\u5165\u5931\u8d25");
            return false;
        }
        final BluetoothGattCharacteristic alertLevel = this.mBluetoothGattService.getCharacteristic(UUID.fromString(this.deviceWriteUUIDs[0]));
        if (alertLevel == null) {
            LogUtils.e(this.TAG, "----alertLevel = null \u5199\u5165\u5931\u8d25");
            return false;
        }
        final int storedLevel = alertLevel.getWriteType();
        LogUtils.i(this.TAG, "storedLevel() - storedLevel=" + storedLevel);
        alertLevel.setValue(data);
        alertLevel.setWriteType(1);
        final boolean status = this.mBluetoothGatt.writeCharacteristic(alertLevel);
        LogUtils.i(this.TAG, "writeLlsAlertLevel() - status=" + status);
        if (this.mIDataReadWriteCallback != null) {
            this.mIDataReadWriteCallback.writeData(data);
        }
        return status;
    }
    
    boolean isConnect() {
        return this.connectBluetoothDevice != null && this.connectStatus == ConnectStatus.CONNECTED;
    }
    
    private void addHeart() {
        if (this.heartSendMessage != null && this.isConnect()) {
            LogUtils.i(this.TAG, "\u624b\u52a8\u6dfb\u52a0\u4e86\u4e00\u4e2a\u4f53\u8102\u79e4\u7684\u5fc3\u8df3\u5305");
            this.addSendMsg(this.heartSendMessage);
        }
    }
    
    public void exit() {
        this.clearBlueGatt();
        if (this.myHandle != null) {
            this.myHandle.exitHandle();
            this.myHandle = null;
        }
        this.context = null;
        this.connectBluetoothDevice = null;
        this.sendThreadRunFlag = false;
        if (this.mSendThread != null) {
            this.mSendThread.interrupt();
            this.mSendThread = null;
        }
    }
    
    private void startSendHeart() {
        if (this.myHandle != null) {
            this.myHandle.removeMessages(99);
            this.myHandle.sendEmptyMessage(99);
        }
    }
    
    public String getConnectBluetoothAddress() {
        if (this.connectBluetoothDevice != null && this.isConnect()) {
            return this.connectBluetoothDevice.getAddress();
        }
        return null;
    }
    
    @SuppressLint({ "HandlerLeak" })
    private class MyHandle extends Handler
    {
        private WeakReference<Context> mWeakReference;
        
        MyHandle(final Context context) {
            super(Looper.getMainLooper());
            this.mWeakReference = new WeakReference<Context>(context);
        }
        
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (this.mWeakReference == null || this.mWeakReference.get() == null) {
                return;
            }
            switch (msg.what) {
                case 1: {
                    ServiceGatt.this.gattConnect();
                    break;
                }
                case 2: {
                    if (ServiceGatt.this.mIConnectStationCallback != null) {
                        ServiceGatt.this.mIConnectStationCallback.onConnected(ServiceGatt.this.productStyle, (BluetoothDevice)msg.obj);
                        break;
                    }
                    break;
                }
                case 3: {
                    if (ServiceGatt.this.mIConnectStationCallback != null) {
                        ServiceGatt.this.mIConnectStationCallback.onDisConnected(ServiceGatt.this.productStyle);
                        break;
                    }
                    break;
                }
                case 4: {
                    if (ServiceGatt.this.mIConnectStationCallback != null) {
                        ServiceGatt.this.mIConnectStationCallback.onConnecting(ServiceGatt.this.productStyle, (BluetoothDevice)msg.obj);
                        break;
                    }
                    break;
                }
                case 99: {
                    ServiceGatt.this.addHeart();
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
    
    private class SendThread extends Thread
    {
        @Override
        public void run() {
            super.run();
            while (ServiceGatt.this.sendThreadRunFlag) {
                if (ServiceGatt.this.connectStatus == ConnectStatus.CONNECTED) {
                    if (ServiceGatt.this.sendMessagesBufferQueue != null && ServiceGatt.this.sendMessagesBufferQueue.size() > 0) {
                        final SendMessage sendMessage = ServiceGatt.this.sendMessagesBufferQueue.poll();
                        if (sendMessage != null && sendMessage.sendDatas != null && sendMessage.sendDatas.length > 0) {
                            final SendMessage sendMessage2 = sendMessage;
                            ++sendMessage2.sendCount;
                            final boolean sendResultFlag = ServiceGatt.this.writeData(sendMessage.sendDatas);
                            if (sendResultFlag) {
                                ServiceGatt.this.lastSendMsgTime = System.currentTimeMillis();
                            }
                            if (!sendResultFlag) {
                                final int sendCount = sendMessage.sendCount;
                                final int reSendCount = sendMessage.reSendCount;
                                final String desc = sendMessage.desc;
                                final String addDescStr = "#" + TimeUtils.getCurrentTime2() + "--\u53d1\u9001 sendCount=" + sendCount + "\u6b21\u5931\u8d25";
                                sendMessage.desc = desc + addDescStr;
                                if (sendCount < reSendCount + 1 && ServiceGatt.this.sendMessagesBufferQueue != null) {
                                    ServiceGatt.this.sendMessagesBufferQueue.add(sendMessage);
                                }
                            }
                        }
                    }
                    if (ServiceGatt.this.heartSendMessage != null && System.currentTimeMillis() - ServiceGatt.this.lastSendMsgTime >= 2000L) {
                        ServiceGatt.this.startSendHeart();
                    }
                }
                else {
                    if (ServiceGatt.this.myHandle != null) {
                        ServiceGatt.this.myHandle.removeMessages(99);
                    }
                    if (ServiceGatt.this.sendMessagesBufferQueue != null) {
                        ServiceGatt.this.sendMessagesBufferQueue.clear();
                    }
                }
                long sleeptime;
                if (ServiceGatt.this.sendMessagesBufferQueue != null && ServiceGatt.this.sendMessagesBufferQueue.size() > 0) {
                    sleeptime = 20L;
                }
                else {
                    sleeptime = ServiceGatt.this.ServiceGatt_SendThread_interval;
                }
                SystemClock.sleep(sleeptime);
            }
        }
    }
}
