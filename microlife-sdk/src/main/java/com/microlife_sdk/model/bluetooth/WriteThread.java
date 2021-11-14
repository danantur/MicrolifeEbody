// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.bluetooth;

public abstract class WriteThread extends Thread
{
    public static final String TAG;
    public BluetoothLEUtils myBluetooth;
    
    public WriteThread(final BluetoothLEUtils myBluetooth, final BluetoothLEHandler bluetoothLEHandler) {
        this.myBluetooth = myBluetooth;
        this.myBluetooth.isWriteRunning = true;
    }
    
    public WriteThread(final MyBluetoothLE myBluetooth) {
        this.myBluetooth = myBluetooth;
        this.myBluetooth.isWriteRunning = true;
    }
    
    static {
        TAG = WriteThread.class.getSimpleName();
    }
    
    @Override
    public void run() {
        BluetoothLEUtils myBluetooth;
        while ((myBluetooth = this.myBluetooth).isWriteRunning) {
            if (myBluetooth.getCommArraySize() > 0) {
                if (this.myBluetooth.mCurrentStatus != 17) {
                    return;
                }
                this.write();
            }
        }
    }
    
    public abstract void write();
}
