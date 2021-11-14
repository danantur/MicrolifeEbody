// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.interfaces;

import android.bluetooth.BluetoothDevice;
import com.microlife_sdk.sdks.enums.ProductStyle;

public interface IConnectStationCallback
{
    void onConnected(final ProductStyle p0, final BluetoothDevice p1);
    
    void onConnecting(final ProductStyle p0, final BluetoothDevice p1);
    
    void onDisConnected(final ProductStyle p0);
}
