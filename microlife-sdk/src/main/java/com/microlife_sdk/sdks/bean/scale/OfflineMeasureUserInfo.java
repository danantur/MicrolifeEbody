// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.bean.scale;

public class OfflineMeasureUserInfo
{
    private static OfflineMeasureUserInfo instance;
    public String userId;
    public int age;
    public int sex;
    public int height;
    public int roleType;
    
    public static OfflineMeasureUserInfo getOfflineMeasureUserInfo() {
        if (OfflineMeasureUserInfo.instance == null) {
            synchronized (ScaleUserInfo.class) {
                OfflineMeasureUserInfo.instance = new OfflineMeasureUserInfo();
            }
        }
        return OfflineMeasureUserInfo.instance;
    }
    
    private OfflineMeasureUserInfo() {
    }
}
