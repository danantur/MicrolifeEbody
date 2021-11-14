// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.cloud.listener;

import com.microlife_sdk.model.cloud.model.PushResponse;

public interface OnGetPushHistoryListListener
{
    void getPushHistoryMessage(final PushResponse p0, final String p1);
}
