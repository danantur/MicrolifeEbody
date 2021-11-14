// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.interfaces.scale;

import com.microlife_sdk.sdks.bean.scale.OfflineMeasureResult;
import com.microlife_sdk.sdks.bean.scale.ScaleMeasureResult;

public interface IScaleMeasureCallback
{
    void onScaleWake();
    
    void onScaleSleep();
    
    void onReceiveMeasureResult(final ScaleMeasureResult p0);
    
    void onWeightOverLoad();
    
    void onReceiveHistoryRecord(final OfflineMeasureResult p0);
    
    void onHistoryDownloadDone();
    
    void onLowPower();
    
    void setUserInfoSuccess();
    
    void receiveTime(final long p0);
}
