// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.bean;

import com.microlife_sdk.sdks.utils.BytesUtils;
import com.microlife_sdk.sdks.utils.TimeUtils;

public class SendMessage
{
    public long productTime;
    public int type;
    public String desc;
    public int reSendCount;
    public int sendCount;
    public byte[] srcDatas;
    public byte[] sendDatas;
    
    public SendMessage() {
        this.reSendCount = 0;
        this.sendCount = 0;
        this.productTime = System.currentTimeMillis();
    }
    
    @Override
    public String toString() {
        return "SendMessage{productTime =" + TimeUtils.formatTime1(this.productTime) + ", type=" + this.type + ", sendCount=" + this.sendCount + ", desc='" + this.desc + '\'' + ", reSendCount=" + this.reSendCount + ", srcDatas=" + BytesUtils.bytes2HexStr(this.srcDatas) + ", sendDatas=" + BytesUtils.bytes2HexStr(this.sendDatas) + '}';
    }
}
