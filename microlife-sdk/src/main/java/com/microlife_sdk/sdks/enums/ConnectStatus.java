// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.enums;

public enum ConnectStatus
{
    CONNECTED("\u5df2\u8fde\u63a5"), 
    CONNECTING("\u6b63\u5728\u8fde\u63a5..."), 
    DISCONNECTED("\u5df2\u65ad\u5f00");
    
    public String desc;
    
    private ConnectStatus(final String desc) {
        this.desc = desc;
    }
    
    public String getDesc() {
        return this.desc;
    }
}
