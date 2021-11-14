package com.microlife_sdk.ideabuslibrary.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import com.microlife_sdk.ideabuslibrary.util.BaseUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BluetoothLEUtils extends BluetoothLEClass {
   private static final String TAG = BluetoothLEUtils.class.getSimpleName();
   private Context context;
   private List<String> mScanList;
   public boolean isScanning = false;
   private BluetoothAdapter mBluetoothAdapter;
   private LeScanCallback mLeOldScanCallback;
   private BluetoothGattCallback mLeGattCallback;
   private ConnectionThread mConnectionThread;
   public BluetoothLEHandler mHandler;
   private static boolean isRegisterBtReceiver = false;
   private boolean isRestartingBT;
   private BroadcastReceiver mReceiver = new BroadcastReceiver() {
      public void onReceive(Context context, Intent intent) {
         String action = intent.getAction();
         if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
            try {
               int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
               switch(state) {
               case 10:
                  BaseUtils.printLog("d", BluetoothLEUtils.TAG, "Bluetooth disable!");
                  BluetoothLEClass.mOnIMBluetoothLEListener.onBtStateChanged(false);
                  BluetoothLEUtils.this.unregisterBtReceiver();
               case 11:
               case 13:
               default:
                  break;
               case 12:
                  BaseUtils.printLog("d", BluetoothLEUtils.TAG, "Bluetooth enable!");
                  BluetoothLEClass.mOnIMBluetoothLEListener.onBtStateChanged(true);
                  BluetoothLEUtils.this.unregisterBtReceiver();
               }
            } catch (Exception var5) {
            }
         }

      }
   };

   public abstract void onWriteThreadStart(BluetoothLEHandler var1);

   public abstract String onRead(BluetoothGattCharacteristic var1);

   public abstract String onChanged(BluetoothGattCharacteristic var1);

   public abstract void searchGattServices(int var1, List<BluetoothGattService> var2);

   public abstract boolean writeToBLE(String var1, boolean var2);

   public BluetoothLEUtils(Context c, boolean isPrintLog) {
      this.context = c;
      BaseUtils.setIsPrintLog(isPrintLog);
      if (c.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
         BluetoothManager bluetoothManager = (BluetoothManager)this.context.getSystemService(Context.BLUETOOTH_SERVICE);
         if (bluetoothManager != null) {
            this.mBluetoothAdapter = bluetoothManager.getAdapter();
         }

         this.initCommArray();
      }

      this.mScanList = new ArrayList();
      this.mHandler = new BluetoothLEHandler(this, this.mBluetoothAdapter);
   }

   public Context getContext() {
      return this.context;
   }

   public boolean isBTEnabled() {
      return this.mBluetoothAdapter != null && this.mBluetoothAdapter.isEnabled();
   }

   private void restartBT() {
      if (this.mBluetoothAdapter != null) {
         this.stopLEScan();
         if (this.isBTEnabled()) {
            this.mBluetoothAdapter.disable();
         } else {
            this.mBluetoothAdapter.enable();
         }
      }

   }

   public boolean isScanning() {
      return this.isScanning;
   }

   public boolean isConnected() {
      return this.mCurrentStatus == 17;
   }

   public boolean isSupportBluetooth(Activity activity) {
      if (!this.context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
         return false;
      } else {
         if (!this.mBluetoothAdapter.isEnabled()) {
            this.registerBtReceiver();
            Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
            activity.startActivityForResult(enableBtIntent, 100);
         }

         return true;
      }
   }

   public void registerBtReceiver() {
      if (isRegisterBtReceiver) {
         this.unregisterBtReceiver();
      }

      try {
         this.context.registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
         isRegisterBtReceiver = true;
      } catch (Exception var2) {
      }

   }

   public void unregisterBtReceiver() {
      try {
         this.context.unregisterReceiver(this.mReceiver);
         isRegisterBtReceiver = false;
      } catch (Exception var2) {
      }

   }

   public void startLEScan(int timeout, boolean isLoop) {
      BaseUtils.printLog("d", TAG, "startLEScan");
      if (this.mBluetoothAdapter != null && this.mBluetoothAdapter.isEnabled()) {
         if (this.isScanning) {
            BaseUtils.printLog("d", TAG, "已在掃描中,停止掃描");
            this.stopLEScan();
         }

         try {
            this.mScanList.clear();
            if (isLoop) {
               this.mHandler.removeMessages(6);
               Message msg = new Message();
               msg.what = 6;
               msg.arg1 = timeout;
               this.mHandler.sendMessageDelayed(msg, (long)(timeout * 1000));
            } else {
               this.mHandler.removeMessages(4);
               this.mHandler.sendEmptyMessageDelayed(4, (long)(timeout * 1000));
            }

            if (this.mBluetoothAdapter != null) {
               this.isScanning = true;
               this.mBluetoothAdapter.startLeScan(this.setLeScanCallback());
            }
         } catch (NoClassDefFoundError var4) {
            var4.printStackTrace();
         }

      } else {
         BaseUtils.printLog("d", TAG, "未開啟藍牙或不支援BLE");
      }
   }

   public void stopLEScan() {
      BaseUtils.printLog("d", TAG, "stopLEScan");
      this.mHandler.removeMessages(4);
      this.isScanning = false;
      if (this.mBluetoothAdapter != null) {
         this.mBluetoothAdapter.stopLeScan(this.setLeScanCallback());
      }

   }

   public LeScanCallback setLeScanCallback() {
      if (this.mLeOldScanCallback == null) {
         this.mLeOldScanCallback = new LeScanCallback() {
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
               if (!BluetoothLEUtils.this.isScanning) {
                  BluetoothLEUtils.this.stopLEScan();
               } else if (device != null && device.getAddress() != null && device.getAddress().length() > 0) {
                  BluetoothLEUtils.this.checkIsExist(device, rssi, scanRecord);
               }

            }
         };
      }

      return this.mLeOldScanCallback;
   }

   private synchronized void checkIsExist(BluetoothDevice device, int rssi, byte[] scanRecord) {
      Iterator var4 = this.mScanList.iterator();

      String mac;
      while(var4.hasNext()) {
         mac = (String)var4.next();
         if (device.getAddress().equals(mac)) {
            return;
         }
      }

      this.mScanList.add(device.getAddress());
      Message msg = new Message();
      msg.what = 3;
      mac = device.getName() != null && device.getName().length() > 0 ? device.getName() : "n/a";
      Bundle bd = new Bundle();
      bd.putString("name", mac);
      bd.putString("address", device.getAddress());
      bd.putInt("rssi", rssi);
      bd.putByteArray("scanRecord", scanRecord);
      msg.setData(bd);
      this.mHandler.sendMessage(msg);
   }

   public void connect(List<String> address) {
      BaseUtils.printLog("d", TAG, "connect->" + address);
      this.mHandler.removeMessages(4);
      if (this.isScanning()) {
         this.stopLEScan();
      }

      if (this.mBluetoothAdapter != null && address != null && address.size() != 0) {
         this.mCurrentStatus = 16;
         this.initBluetoothGattsArray();
         this.initParams();
         this.charWriteList = new ArrayList();
         this.connGattCount = address.size();
         this.connectBLEs(address);
      } else {
         BaseUtils.printLog("d", TAG, "mBluetoothAdapter == null 或 無效地址");
         this.mCurrentStatus = 19;
         this.mHandler.sendEmptyMessage(1);
      }
   }

   private void connectBLEs(List<String> address) {
      this.mConnectionThread = new ConnectionThread(this, this.mBluetoothAdapter, this.mHandler, address);
      this.mConnectionThread.start();
   }

   public boolean writeMessage(String str, boolean clearAllComm) {
      return this.writeToBLE(str, clearAllComm);
   }

   public void readRSSI() {
      if (this.getBluetoothGatt(0) != null) {
         boolean isWriteOk = this.getBluetoothGatt(0).readRemoteRssi();
         BaseUtils.printLog("d", TAG, "成功讀取RSSI = " + isWriteOk);
      }

   }

   protected void sendTest(String commStr) {
      if (this.getBluetoothGatt(0) != null) {
         boolean isWriteOk = this.getBluetoothGatt(0).writeCharacteristic((BluetoothGattCharacteristic)this.charWriteList.get(0));
         ((BluetoothGattCharacteristic)this.charWriteList.get(0)).setValue(BaseUtils.convertHexToByteArray(commStr));
         BaseUtils.printLog("d", TAG, "成功寫出 = " + isWriteOk);
      }

   }

   public BluetoothGattCallback setLeGattCallback() {
      if (this.mLeGattCallback == null) {
         this.mLeGattCallback = new BluetoothGattCallback() {
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
               if (newState == 2) {
                  BaseUtils.printLog("d", BluetoothLEUtils.TAG, "成功連線到 BLE GATT 設備.");

                  try {
                     Thread.sleep(600L);
                  } catch (InterruptedException var5) {
                     var5.printStackTrace();
                  }

                  for(int i = 0; i < BluetoothLEUtils.this.connGattCount; ++i) {
                     if (gatt.equals(BluetoothLEUtils.this.getBluetoothGatt(i))) {
                        BaseUtils.printLog("d", BluetoothLEUtils.TAG, "正在搜尋 第" + i + " 顆藍牙 Service   NAME = " + BluetoothLEUtils.this.getBluetoothGatt(i).getDevice().getName());
                        BluetoothLEUtils.this.getBluetoothGatt(i).discoverServices();
                        break;
                     }
                  }
               } else if (newState == 0) {
                  BaseUtils.printLog("e", BluetoothLEUtils.TAG, "Device 斷線 from GATT server. status = " + status);
                  BluetoothLEUtils.this.disconnect(18);
               }

            }

            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
               if (status == 0) {
                  BaseUtils.printLog("d", BluetoothLEUtils.TAG, "onServicesDiscovered  搜尋藍牙 Service成功 status = " + status);

                  for(int i = 0; i < BluetoothLEUtils.this.connGattCount; ++i) {
                     if (gatt.equals(BluetoothLEUtils.this.getBluetoothGatt(i))) {
                        BaseUtils.printLog("d", BluetoothLEUtils.TAG, "搜尋到 第" + i + " 顆藍牙 Service   NAME = " + BluetoothLEUtils.this.getBluetoothGatt(i).getDevice().getName());
                        BluetoothLEUtils.this.displayGattServices(i, BluetoothLEUtils.this.getSupportedGattServices(i));
                        break;
                     }
                  }
               } else {
                  BaseUtils.printLog("e", BluetoothLEUtils.TAG, "斷線  onServicesDiscovered received: " + status);
                  BluetoothLEUtils.this.disconnect(18);
               }

            }

            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
               BaseUtils.printLog("d", BluetoothLEUtils.TAG, "成功發送至設備    getDevice.getName = " + gatt.getDevice().getName() + " , status = " + status);
            }

            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
               BaseUtils.printLog("d", BluetoothLEUtils.TAG, "成功讀取設備訊息   getDevice.getName = " + gatt.getDevice().getName() + " , status = " + status);
               Message msg = new Message();
               msg.what = 2;
               msg.obj = BluetoothLEUtils.this.onRead(characteristic);
               BluetoothLEUtils.this.mHandler.sendMessage(msg);
            }

            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
               String deviceName = gatt.getDevice().getName().trim();
               BaseUtils.printLog("d", BluetoothLEUtils.TAG, "接收資料 deviceName = " + deviceName);
               Message msg = new Message();
               msg.what = 2;
               msg.obj = deviceName + "==" + BluetoothLEUtils.this.onChanged(characteristic);
               BluetoothLEUtils.this.mHandler.sendMessage(msg);
            }
         };
      }

      return this.mLeGattCallback;
   }

   private List<BluetoothGattService> getSupportedGattServices(int item) {
      return this.getBluetoothGatt(item) != null ? this.getBluetoothGatt(item).getServices() : null;
   }

   private void displayGattServices(int item, List<BluetoothGattService> gattServices) {
      this.searchGattServices(item, gattServices);
      if (this.charWriteList.size() == this.connGattCount && this.charNotifyCount == this.connGattCount) {
         if (item != 0 && item == this.connGattCount - 1) {
            this.connectSuccess();
         } else if (item == 0 && item + 1 == this.connGattCount) {
            this.connectSuccess();
         }
      }

      if (this.mConnectionThread != null) {
         synchronized(this.mConnectionThread) {
            this.mConnectionThread.notify();
         }
      }
   }

   private void connectSuccess() {
      BaseUtils.printLog("d", TAG, "connectSuccess CONNECTED------");
      this.mCurrentStatus = 17;
      this.mHandler.sendEmptyMessage(1);
      if (this.mConnectionThread != null && !this.mConnectionThread.isInterrupted()) {
         this.mConnectionThread.interrupt();
         this.mConnectionThread = null;
      }

      this.charNotifyCount = 0;
   }

   private void initParams() {
      if (this.mConnectionThread != null && !this.mConnectionThread.isInterrupted()) {
         this.mConnectionThread.interrupt();
         this.mConnectionThread = null;
      }

      this.mHandler.removeMessages(4);
      this.mHandler.removeMessages(20);
      this.charNotifyCount = 0;
      this.isWriteRunning = false;
      this.removeAllComm();
   }

   public synchronized void disconnect(int status) {
      BaseUtils.printLog("d", TAG, "disconnect code : " + status);
      this.stopLEScan();
      this.initParams();
      this.mCurrentStatus = status;
      this.unregisterBtReceiver();
      if (this.getBluetoothGatts() != null && this.getBluetoothGatts().size() > 0) {
         for(int i = 0; i < this.getBluetoothGatts().size(); ++i) {
            try {
               if (this.getBluetoothGatt(i) != null) {
                  this.getBluetoothGatt(i).disconnect();
                  this.getBluetoothGatt(i).close();
                  BaseUtils.printLog("e", TAG, "disconnect : " + i);
               }
            } catch (Exception var4) {
               var4.printStackTrace();
               return;
            }

            this.mHandler.sendEmptyMessage(1);
         }
      }

      this.initBluetoothGattsArray();
   }

   public boolean refreshDeviceCache(BluetoothGatt gatt) {
      try {
         Method localMethod = gatt.getClass().getMethod("refresh");
         if (localMethod != null) {
            boolean bool = (Boolean)localMethod.invoke(gatt);
            return bool;
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return false;
   }
}
