// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.interfaces;

public interface IDataReadWriteCallback
{
    void onCharacteristicChanged(final byte[] p0);
    
    void writeData(final byte[] p0);
}
