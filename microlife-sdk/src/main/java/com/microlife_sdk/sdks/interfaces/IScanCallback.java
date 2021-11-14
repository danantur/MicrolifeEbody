// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.interfaces;

import android.bluetooth.BluetoothDevice;

public interface IScanCallback
{
    void beforeScan();
    
    void scanOneDevice(final BluetoothDevice p0, final int p1, final byte[] p2);
    
    void overScan();
}
