package com.microlife_sdk.ideabuslibrary.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BluetoothLEHandler extends Handler {
   private static final String TAG = BluetoothLEHandler.class.getSimpleName();
   private BluetoothLEUtils bluetooth;
   private BluetoothAdapter mBluetoothAdapter;
   private boolean isRun = false;

   public BluetoothLEHandler(BluetoothLEUtils bt, BluetoothAdapter adapter) {
      this.bluetooth = bt;
      this.mBluetoothAdapter = adapter;
   }

   public void handleMessage(Message message) {
      super.handleMessage(message);
      switch(message.what) {
      case 1:
         if (BluetoothLEClass.mOnIMBluetoothLEListener != null) {
            BluetoothLEClass.mOnIMBluetoothLEListener.connectionStatus(this.bluetooth.mCurrentStatus);
         }

         if (this.bluetooth.mCurrentStatus == 17 && !this.isRun) {
            this.isRun = true;
            this.bluetooth.onWriteThreadStart(this);
         } else {
            this.isRun = false;
         }
         break;
      case 2:
         if (BluetoothLEClass.mOnIMBluetoothLEListener != null) {
            BluetoothLEClass.mOnIMBluetoothLEListener.dataResult((String)message.obj);
         }
         break;
      case 3:
         if (BluetoothLEClass.mOnIMBluetoothLEListener != null) {
            Bundle bd = message.getData();
            BluetoothLEClass.mOnIMBluetoothLEListener.scanResult(bd.getString("address"), bd.getString("name"), bd.getInt("rssi"), bd.getByteArray("scanRecord"));
         }
         break;
      case 4:
         this.bluetooth.isScanning = false;
         if (this.mBluetoothAdapter != null) {
            this.mBluetoothAdapter.stopLeScan(this.bluetooth.setLeScanCallback());
         }

         if (BluetoothLEClass.mOnIMBluetoothLEListener != null) {
            BluetoothLEClass.mOnIMBluetoothLEListener.connectionStatus(message.what);
         }
      case 5:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      default:
         break;
      case 6:
         if (!this.bluetooth.isScanning) {
            return;
         }

         this.bluetooth.startLEScan(message.arg1, true);
         if (BluetoothLEClass.mOnIMBluetoothLEListener != null) {
            BluetoothLEClass.mOnIMBluetoothLEListener.connectionStatus(4);
         }
         break;
      case 20:
         if (this.bluetooth.mCurrentStatus != 17) {
            Log.i(TAG, "連線超時");
            this.bluetooth.disconnect(18);
         }
         break;
      case 21:
         Log.i(TAG, "正在創建 第 " + message.arg1 + " 顆藍牙連線");
         this.bluetooth.mCurrentStatus = 21;
         this.bluetooth.addBluetoothGatt(((BluetoothDevice)message.obj).connectGatt(this.bluetooth.getContext(), false, this.bluetooth.setLeGattCallback()));
      }

   }
}
