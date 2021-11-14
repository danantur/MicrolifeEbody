// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Message;
import com.microlife_sdk.model.XlogUtils;
import java.util.List;
import android.bluetooth.BluetoothAdapter;

public class ConnectionThread extends Thread
{
    public static final String TAG;
    public BluetoothLEUtils myBluetooth;
    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothLEHandler mHandler;
    public List<String> address;
    
    public ConnectionThread(final BluetoothLEUtils myBluetooth, final BluetoothAdapter mBluetoothAdapter, final BluetoothLEHandler mHandler, final List<String> address) {
        this.myBluetooth = myBluetooth;
        this.mBluetoothAdapter = mBluetoothAdapter;
        this.mHandler = mHandler;
        this.address = address;
    }
    
    static {
        TAG = ConnectionThread.class.getSimpleName();
    }
    
    @Override
    public void run() {
        this.mHandler.removeMessages(20);
        this.mHandler.sendEmptyMessageDelayed(20, 15000L);
        // monitorenter(this)
        int i = 0;
        try {
            while (i < this.myBluetooth.connGattCount) {
                try {
                    final BluetoothDevice remoteDevice;
                    if ((remoteDevice = this.mBluetoothAdapter.getRemoteDevice((String)this.address.get(i))) == null) {
                        XlogUtils.xLog(ConnectionThread.TAG, i + " Bluetooth not found");
                        this.myBluetooth.disconnect(18);
                        // monitorexit(this)
                        return;
                    }
                    final int n = i;
                    final Message message = new Message();
                    final Message message2 = message;
                    final BluetoothDevice obj = remoteDevice;
                    final Message message3 = message2;
                    final int arg1 = i;
                    final Message message4 = message2;
                    new Message();
                    message4.what = 21;
                    message3.arg1 = arg1;
                    message.obj = obj;
                    this.mHandler.sendMessage(message2);
                    if (n < this.myBluetooth.connGattCount - 1) {
                        try {
                            this.wait();
                            Thread.sleep(600L);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                            this.myBluetooth.disconnect(18);
                            // monitorexit(this)
                            return;
                        }
                    }
                    ++i;
                    continue;
                }
                catch (IllegalArgumentException ex2) {
                    XlogUtils.xLog(ConnectionThread.TAG, "connection " + i + " Bluetooth error  " + ex2.toString());
                    this.myBluetooth.disconnect(18);
                    // monitorexit(this)
                    return;
                }
            }
        }
        // monitorexit(this)
        finally {
        }
        // monitorexit(this)
    }
}
