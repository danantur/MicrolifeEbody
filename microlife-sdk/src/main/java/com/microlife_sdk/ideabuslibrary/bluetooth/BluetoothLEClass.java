package com.microlife_sdk.ideabuslibrary.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import java.util.ArrayList;
import java.util.List;

public class BluetoothLEClass {
   public static final int CALLBACK_STATE = 1;
   public static final int CALLBACK_RESULT = 2;
   public static final int SCAN_RESULT = 3;
   public static final int SCAN_FINISH = 4;
   public static final int SCAN_STOP = 5;
   public static final int SCAN_LOOP = 6;
   public static final int NONE = 16;
   public static final int CONNECTED = 17;
   public static final int DISCONNECTED = 18;
   /** @deprecated */
   @Deprecated
   public static final int UNSPECIFIED_ADDRESS = 19;
   public static final int CONNECT_TIMEOUT = 20;
   public static final int CONNECTING = 21;
   public static final int ERROR133_RESTART_BT = 23;
   public static final int ACTION_REQUEST_ENABLE = 100;
   public static final int CONNECT_PERIOD = 15000;
   private List<BluetoothGatt> mBluetoothGatts;
   private List<String> commArray;
   public List<BluetoothGattCharacteristic> charWriteList;
   public int charNotifyCount = 0;
   public int connGattCount = 0;
   public boolean isWriteRunning = false;
   public int mCurrentStatus = 16;
   public static OnIMBluetoothLEListener mOnIMBluetoothLEListener;

   public void initBluetoothGattsArray() {
      this.mBluetoothGatts = new ArrayList();
   }

   public void addBluetoothGatt(BluetoothGatt gatt) {
      if (this.mBluetoothGatts != null) {
         this.mBluetoothGatts.add(gatt);
      }

   }

   public BluetoothGatt getBluetoothGatt(int i) {
      return this.mBluetoothGatts != null && this.mBluetoothGatts.size() > i ? (BluetoothGatt)this.mBluetoothGatts.get(i) : null;
   }

   public List<BluetoothGatt> getBluetoothGatts() {
      return this.mBluetoothGatts;
   }

   public void removeBluetoothGatt() {
      if (this.mBluetoothGatts != null && this.mBluetoothGatts.size() > 0) {
         this.mBluetoothGatts.remove(0);
      }

   }

   public void initCommArray() {
      this.commArray = new ArrayList();
   }

   public void addCommArray(String comm) {
      if (this.commArray != null) {
         this.commArray.add(comm);
      }

   }

   public String getComm(int i) {
      return this.commArray != null && this.commArray.size() > i ? (String)this.commArray.get(i) : null;
   }

   public int getCommArraySize() {
      return this.commArray == null ? 0 : this.commArray.size();
   }

   public void removeComm(int i) {
      if (this.commArray != null && this.commArray.size() > i) {
         this.commArray.remove(i);
      }

   }

   public void removeSameComm(String cmd) {
      if (this.commArray != null) {
         for(int i = 0; i < this.commArray.size(); ++i) {
            if (((String)this.commArray.get(i)).equals(cmd)) {
               this.commArray.remove(i);
            }
         }

      }
   }

   public void removeOtherComm() {
      if (this.commArray != null && this.commArray.size() > 1) {
         String str = (String)this.commArray.get(0);
         this.commArray.clear();
         this.commArray.add(str);
      }

   }

   public void removeAllComm() {
      if (this.commArray != null) {
         this.commArray.clear();
      }

   }

   public void setOnIMBluetoothLEListener(OnIMBluetoothLEListener l) {
      mOnIMBluetoothLEListener = l;
   }

   public interface OnIMBluetoothLEListener {
      void onBtStateChanged(boolean var1);

      void scanResult(String var1, String var2, int var3, byte[] var4);

      void connectionStatus(int var1);

      void dataResult(String var1);
   }
}
