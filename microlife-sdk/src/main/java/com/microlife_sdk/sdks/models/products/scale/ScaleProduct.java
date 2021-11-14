// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.models.products.scale;

import java.util.List;
import com.microlife_sdk.sdks.bean.scale.OfflineMeasureUserInfo;
import com.microlife_sdk.sdks.bean.SendMessage;
import com.microlife_sdk.sdks.utils.BytesUtils;
import com.microlife_sdk.sdks.interfaces.scale.IScaleMeasureCallback;
import com.microlife_sdk.sdks.utils.LogUtils;
import java.util.Arrays;
import com.microlife_sdk.sdks.interfaces.IDataReadWriteCallback;
import com.microlife_sdk.sdks.enums.ProductStyle;
import android.content.Context;
import com.microlife_sdk.sdks.bean.scale.ScaleUserInfo;
import com.microlife_sdk.sdks.bases.BaseManager;

public class ScaleProduct extends BaseManager
{
    private static final String TAG = "ScaleProduct";
    private ScaleBytesMakeFatory mScaleBytesMakeFatory;
    private ScaleBytesAnalysisFatory mScaleBytesAnalysisFatory;
    
    public void updateScaleUserInfo(final ScaleUserInfo userInfo) {
        if (this.isConnect()) {
            this.sendUserInfo(userInfo);
        }
    }
    
    public ScaleProduct(final Context mContext, final long ServiceGatt_SendThread_interval) {
        super(mContext, ProductStyle.SCALE, "0000faa0-0000-1000-8000-00805f9b34fb", new String[] { "0000faa1-0000-1000-8000-00805f9b34fb" }, new String[] { "0000faa2-0000-1000-8000-00805f9b34fb" }, true, ServiceGatt_SendThread_interval, ScaleBytesMakeFatory.getHEARTSmg());
        this.mScaleBytesMakeFatory = new ScaleBytesMakeFatory();
        this.mScaleBytesAnalysisFatory = new ScaleBytesAnalysisFatory();
        this.mServiceGatt.setIDataReadWriteCallback(new IDataReadWriteCallback() {
            @Override
            public void onCharacteristicChanged(final byte[] data) {
                LogUtils.i("ScaleProduct", "ScaleBlue--IDataReadWriteCallback-onCharacteristicChanged--\u8bfb\u5230-" + Arrays.toString(data));
                ScaleProduct.this.mScaleBytesAnalysisFatory.resultAnalysis(data);
            }
            
            @Override
            public void writeData(final byte[] data) {
            }
        });
    }
    
    public void setmIMeasureResultCallback(final IScaleMeasureCallback mIMeasureResultCallback) {
        this.mScaleBytesAnalysisFatory.setmIMeasureResultCallback(mIMeasureResultCallback);
    }
    
    public void sendUserInfo(final ScaleUserInfo userInfo) {
        if (this.mScaleBytesMakeFatory != null) {
            final SendMessage sendMessage = this.mScaleBytesMakeFatory.getUserInfoSmg(userInfo);
            LogUtils.i("ScaleProduct", "---\u4e0b\u53d1\u7528\u6237\u4fe1\u606f userInfo = " + userInfo);
            LogUtils.i("ScaleProduct", "\u4e0b\u53d1\u7528\u6237\u4fe1\u606f bytes[] = " + BytesUtils.bytes2HexStr(sendMessage.srcDatas));
            this.addSendMsg(sendMessage);
        }
    }
    
    public void sendHeartCheck() {
        final ScaleBytesMakeFatory mScaleBytesMakeFatory = this.mScaleBytesMakeFatory;
        this.addSendMsg(ScaleBytesMakeFatory.getHEARTSmg());
    }
    
    public void getVersion() {
        this.addSendMsg(this.mScaleBytesMakeFatory.getVersion());
    }
    
    public void sendGetHistoryDataRequest(final String userId, final int height, final int sex, final int age, final int roleType) {
        final OfflineMeasureUserInfo userInfo = OfflineMeasureUserInfo.getOfflineMeasureUserInfo();
        userInfo.userId = userId;
        userInfo.sex = sex;
        userInfo.height = height;
        userInfo.age = age;
        userInfo.roleType = roleType;
        this.addSendMsg(this.mScaleBytesMakeFatory.getHistoryDataSmg(userId));
    }
    
    public void sendGetHistoryDataACK() {
        this.addSendMsg(this.mScaleBytesMakeFatory.getetHistoryDataACK());
    }
    
    public void sendMesureDataACK() {
        this.addSendMsg(this.mScaleBytesMakeFatory.getMesureDataACK());
    }
    
    public void sendMsg(final SendMessage msg, final String desc) {
        LogUtils.i("ScaleProduct", "----sendMsg--" + desc);
        this.addSendMsg(msg);
    }
    
    public void sendMsgs(final List<SendMessage> msgs, final String desc) {
        LogUtils.i("ScaleProduct", "----sendMsgs--" + desc);
        this.addSendMsgs(msgs);
    }
    
    public void syncSystemClock() {
        LogUtils.i("ScaleProduct", "----syncSystemClock-----send sync system time cmd.");
        this.addSendMsg(this.mScaleBytesMakeFatory.getSyncSystemClock());
    }
    
    public void deleteAllUserInfo() {
        this.addSendMsg(this.mScaleBytesMakeFatory.deleteAllUserInfo());
    }
    
    public void setSleepDisconnectTime() {
        LogUtils.i("ScaleProduct", "\u4f53\u8102\u79e4\u53d1\u9001\u4e86\u8bbe\u7f6e\u4f11\u7720\u65f6\u95f4\u65ad\u5f00\u7684\u6307\u4ee4");
        this.addSendMsg(this.mScaleBytesMakeFatory.getSleepDisconnectTime());
    }
    
    public static float kg2lb(final float kg) {
        return (float)(kg * 144480.0 / 65536.0);
    }
    
    public static float lb2kg(final float lb) {
        return (float)(lb * 65536.0 / 144480.0);
    }
    
    public void setConnectName(final String name) {
        if (this.mScaleBytesAnalysisFatory != null) {
            this.mScaleBytesAnalysisFatory.setConnectBluetoothName(name);
        }
        if (this.mScaleBytesMakeFatory != null) {
            this.mScaleBytesMakeFatory.setConnectBluetoothName(name);
        }
    }
    
    public void needUpdateUserInfo2Front(final boolean need) {
        if (this.mScaleBytesAnalysisFatory != null) {
            this.mScaleBytesAnalysisFatory.needUpdateUserInfo2Front(need);
        }
    }
    
    public void setConnectAddress(final String address) {
        if (this.mScaleBytesAnalysisFatory != null) {
            this.mScaleBytesAnalysisFatory.setConnectBluetoothAddrss(address);
        }
        if (this.mScaleBytesMakeFatory != null) {
            this.mScaleBytesMakeFatory.setConnectBluetoothAddrss(address);
        }
    }
}
