package com.microlife_sdk.ideabuslibrary.bluetooth;

public abstract class WriteThread extends Thread {
   private static final String TAG = WriteThread.class.getSimpleName();
   private BluetoothLEUtils myBluetooth;

   public WriteThread(BluetoothLEUtils bt) {
      this.myBluetooth = bt;
      this.myBluetooth.isWriteRunning = true;
   }

   public void run() {
      while(true) {
         if (this.myBluetooth.isWriteRunning) {
            if (this.myBluetooth.getCommArraySize() <= 0) {
               continue;
            }

            if (this.myBluetooth.mCurrentStatus == 17) {
               this.write();
               continue;
            }

            return;
         }

         return;
      }
   }

   public abstract void write();
}
