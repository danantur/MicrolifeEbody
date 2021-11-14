// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks;

import com.microlife_sdk.sdks.bases.BlueManager;
import android.content.Context;

public class EbelterSdkManager
{
    private static Context app;
    public static boolean outLogFalg;
    
    public static void init(final Context context) {
        EbelterSdkManager.app = context;
        BlueManager.getInstance().init(context);
    }
}
