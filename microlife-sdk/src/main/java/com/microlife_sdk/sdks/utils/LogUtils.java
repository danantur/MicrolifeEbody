// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.utils;

import android.util.Log;
import com.microlife_sdk.sdks.EbelterSdkManager;

public class LogUtils
{
    public static void i(final String tag, final String msg) {
        if (EbelterSdkManager.outLogFalg) {
            Log.i(tag, "EbelterSDK:" + Thread.currentThread().getName() + msg);
        }
    }
    
    public static void e(final String tag, final String msg) {
        if (EbelterSdkManager.outLogFalg) {
            Log.e(tag, "EbelterSDK:" + Thread.currentThread().getName() + msg);
        }
    }
}
