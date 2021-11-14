// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.enums;

public enum ProductStyle
{
    SCALE("\u4f53\u8102\u79e4", "B1 B2 B16 GOQii");
    
    private String productName;
    private String bluetoothName;
    
    private ProductStyle(final String productName, final String bluetoothName) {
        this.productName = productName;
        this.bluetoothName = bluetoothName;
    }
    
    public String getProductName() {
        return this.productName;
    }
    
    public String getBlueName() {
        return this.bluetoothName;
    }
}
