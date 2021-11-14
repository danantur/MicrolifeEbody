package com.microlife_sdk.ideabuslibrary.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Message;
import android.util.Log;
import java.util.List;

public class ConnectionThread extends Thread {
   private static final String TAG = ConnectionThread.class.getSimpleName();
   private BluetoothLEUtils myBluetooth;
   private BluetoothAdapter mBluetoothAdapter;
   private BluetoothLEHandler mHandler;
   private List<String> address;

   public ConnectionThread(BluetoothLEUtils bt, BluetoothAdapter adapter, BluetoothLEHandler handler, List<String> addr) {
      this.myBluetooth = bt;
      this.mBluetoothAdapter = adapter;
      this.mHandler = handler;
      this.address = addr;
   }

   public void run() {
      this.mHandler.removeMessages(20);
      this.mHandler.sendEmptyMessageDelayed(20, 15000L);
      synchronized(this) {
         for(int i = 0; i < this.myBluetooth.connGattCount; ++i) {
            BluetoothDevice device;
            try {
               device = this.mBluetoothAdapter.getRemoteDevice((String)this.address.get(i));
            } catch (IllegalArgumentException var7) {
               Log.e(TAG, "連接 第 " + i + " 顆藍牙出現錯誤  " + var7.toString());
               this.myBluetooth.disconnect(18);
               return;
            }

            if (device == null) {
               Log.e(TAG, "第 " + i + " 顆藍牙未找到");
               this.myBluetooth.disconnect(18);
               return;
            }

            Message msg = new Message();
            msg.what = 21;
            msg.arg1 = i;
            msg.obj = device;
            this.mHandler.sendMessage(msg);
            if (i < this.myBluetooth.connGattCount - 1) {
               try {
                  this.wait();
                  Thread.sleep(600L);
               } catch (InterruptedException var6) {
                  var6.printStackTrace();
                  this.myBluetooth.disconnect(18);
                  return;
               }
            }
         }

      }
   }
}
