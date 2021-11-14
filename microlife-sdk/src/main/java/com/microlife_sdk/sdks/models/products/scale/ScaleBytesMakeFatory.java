// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.models.products.scale;

import com.microlife_sdk.sdks.bean.scale.ScaleUserInfo;
import java.util.Calendar;
import android.text.TextUtils;
import com.microlife_sdk.sdks.utils.LogUtils;
import com.microlife_sdk.sdks.utils.BytesUtils;
import com.microlife_sdk.sdks.bean.SendMessage;

public class ScaleBytesMakeFatory
{
    private static final String TAG = "ScaleBytesMakeFatory";
    private static final byte[] HEARTCHECKBYTES;
    private static final String HEARTCHECK = "\u5fc3\u8df3\u5305";
    private static final String GET_HISTORY_DATA = "\u83b7\u53d6\u5386\u53f2\u6570\u636e";
    private static final byte[] getHistoryDataACK;
    private static final String GET_HISTORY_DATA_ACK = "\u83b7\u53d6\u5386\u53f2\u6570\u636e\u7684\u56de\u5e94";
    private static final String GetMesureDataACK = "\u83b7\u53d6\u6d4b\u91cf\u6570\u636e\u7684\u56de\u5e94";
    private static final byte[] getMesureDataACK;
    private static final byte[] DeleteAllUserInfoByte;
    private static final String DeleteAllUserInfo = "\u5220\u9664\u6240\u6709\u7528\u6237\u4fe1\u606f";
    private static final String SystemClock = "\u4e0b\u53d1\u7cfb\u7edf\u65f6\u95f4";
    private static final String USERINFO = "\u4e0b\u53d1\u7528\u6237\u4fe1\u606f";
    public static final byte[] getVersionByte;
    public static final String getVersionStr = "\u83b7\u53d6\u7248\u672c\u4fe1\u606f";
    private static final byte[] deviceAddressBytes;
    private String connectBluetoothName;
    private String connectBluetoothAddrss;
    
    public static SendMessage getHEARTSmg() {
        final SendMessage heartMsg = new SendMessage();
        heartMsg.srcDatas = ScaleBytesMakeFatory.HEARTCHECKBYTES;
        heartMsg.sendDatas = encrypt(BytesUtils.copyBytes(heartMsg.srcDatas));
        heartMsg.desc = "\u5fc3\u8df3\u5305";
        return heartMsg;
    }
    
    public SendMessage getHistoryDataSmg(final String uid) {
        final SendMessage msg = new SendMessage();
        msg.srcDatas = this.getHistoryData(uid);
        msg.sendDatas = encrypt(BytesUtils.copyBytes(msg.srcDatas));
        msg.desc = "\u83b7\u53d6\u5386\u53f2\u6570\u636e";
        LogUtils.i("ScaleBytesMakeFatory", "----\u83b7\u53d6\u5386\u53f2\u6570\u636e=" + BytesUtils.bytes2HexStr(msg.srcDatas));
        return msg;
    }
    
    private byte[] getHistoryData(final String uid2) {
        final String uid3 = toBtId(uid2);
        String[] uidArr = uid3.split(":");
        if (uidArr.length != 7) {
            uidArr = new String[] { "0", "0", "0", "0", "0", "0", "0" };
        }
        if (TextUtils.isEmpty((CharSequence)this.connectBluetoothName)) {
            LogUtils.e("ScaleBytesMakeFatory", "---------------\u83b7\u53d6\u5386\u53f2\u6570\u636e \u51fa\u73b0\u4e86\u903b\u8f91\u6027\u7684\u9519\u8bef---------");
        }
        if (TextUtils.equals((CharSequence)this.connectBluetoothName, (CharSequence)"Body Fat-B2") || TextUtils.equals((CharSequence)this.connectBluetoothName, (CharSequence)"GOQii Contour") || TextUtils.equals((CharSequence)this.connectBluetoothName, (CharSequence)"GOQii balance") || TextUtils.equals((CharSequence)this.connectBluetoothName, (CharSequence)"GOQii Essential")) {
            final byte[] bytes = { -85, 8, -101, (byte)Integer.parseInt(uidArr[0], 16), (byte)Integer.parseInt(uidArr[1], 16), (byte)Integer.parseInt(uidArr[2], 16), (byte)Integer.parseInt(uidArr[3], 16), (byte)Integer.parseInt(uidArr[4], 16), (byte)Integer.parseInt(uidArr[5], 16), (byte)Integer.parseInt(uidArr[6], 16), 0 };
            LogUtils.i("ScaleBytesMakeFatory", "\u83b7\u53d6\u5386\u53f2\u6570\u636e 08 bytes = " + BytesUtils.bytes2HexStr(bytes));
            return bytes;
        }
        final byte[] bytes = { -85, 7, -101, (byte)Integer.parseInt(uidArr[0], 16), (byte)Integer.parseInt(uidArr[1], 16), (byte)Integer.parseInt(uidArr[2], 16), (byte)Integer.parseInt(uidArr[3], 16), (byte)Integer.parseInt(uidArr[4], 16), (byte)Integer.parseInt(uidArr[5], 16), (byte)Integer.parseInt(uidArr[6], 16), 0 };
        LogUtils.i("ScaleBytesMakeFatory", "\u83b7\u53d6\u5386\u53f2\u6570\u636e 07 bytes = " + BytesUtils.bytes2HexStr(bytes));
        return bytes;
    }
    
    public SendMessage getSleepDisconnectTime() {
        final byte[] bytes = { -85, 3, -94, -6, 0 };
        final SendMessage sendUserMsg = new SendMessage();
        sendUserMsg.srcDatas = bytes;
        sendUserMsg.sendDatas = encrypt(BytesUtils.copyBytes(sendUserMsg.srcDatas));
        sendUserMsg.desc = "\u8bbe\u7f6e\u4f11\u7720\u65ad\u5f00\u65f6\u957f";
        return sendUserMsg;
    }
    
    public SendMessage getetHistoryDataACK() {
        final SendMessage sendUserMsg = new SendMessage();
        sendUserMsg.srcDatas = ScaleBytesMakeFatory.getHistoryDataACK;
        sendUserMsg.sendDatas = encrypt(BytesUtils.copyBytes(ScaleBytesMakeFatory.getHistoryDataACK));
        sendUserMsg.desc = "\u83b7\u53d6\u5386\u53f2\u6570\u636e\u7684\u56de\u5e94";
        return sendUserMsg;
    }
    
    public SendMessage getMesureDataACK() {
        final SendMessage sendUserMsg = new SendMessage();
        sendUserMsg.srcDatas = ScaleBytesMakeFatory.getMesureDataACK;
        sendUserMsg.sendDatas = encrypt(BytesUtils.copyBytes(ScaleBytesMakeFatory.getMesureDataACK));
        sendUserMsg.desc = "\u83b7\u53d6\u6d4b\u91cf\u6570\u636e\u7684\u56de\u5e94";
        return sendUserMsg;
    }
    
    public SendMessage deleteAllUserInfo() {
        final SendMessage sendUserMsg = new SendMessage();
        sendUserMsg.srcDatas = ScaleBytesMakeFatory.DeleteAllUserInfoByte;
        sendUserMsg.sendDatas = encrypt(BytesUtils.copyBytes(ScaleBytesMakeFatory.DeleteAllUserInfoByte));
        sendUserMsg.desc = "\u5220\u9664\u6240\u6709\u7528\u6237\u4fe1\u606f";
        return sendUserMsg;
    }
    
    private byte[] getSystemClock() {
        final Calendar cal = Calendar.getInstance();
        final int year = cal.get(1);
        final int month = cal.get(2) + 1;
        final int date = cal.get(5);
        final int hour = cal.get(11);
        final int minute = cal.get(12);
        final int second = cal.get(13);
        int week = cal.get(7);
        week = ((week == 1) ? 7 : (week - 1));
        final int yearLowHex = year & 0xFF;
        final int yearHeightHex = year >> 8 & 0xFF;
        LogUtils.i("ScaleBytesMakeFatory", "year=" + year + "--yearLowHex=" + yearLowHex + "--yearHeightHex=" + yearHeightHex + "--month=" + month + "--date=" + date + "--hour=" + hour + "--minute=" + minute + "--second=" + second + "--week=" + week);
        final byte[] bytes = { -85, 9, -104, (byte)yearLowHex, (byte)yearHeightHex, (byte)month, (byte)date, (byte)hour, (byte)minute, (byte)second, (byte)week };
        LogUtils.i("ScaleBytesMakeFatory", "----\u4e0b\u53d1\u7684\u65f6\u95f4\u6307\u4ee4 " + BytesUtils.bytes2HexStr(bytes));
        return bytes;
    }
    
    public SendMessage getSyncSystemClock() {
        final SendMessage sendUserMsg = new SendMessage();
        sendUserMsg.srcDatas = this.getSystemClock();
        sendUserMsg.desc = "\u4e0b\u53d1\u7cfb\u7edf\u65f6\u95f4";
        sendUserMsg.sendDatas = encrypt(BytesUtils.copyBytes(sendUserMsg.srcDatas));
        LogUtils.i("ScaleBytesMakeFatory", "\u5f53\u524d\u65f6\u95f4 = " + BytesUtils.bytes2HexStr(sendUserMsg.srcDatas) + "\n \u52a0\u5bc6\u65f6\u95f4 = " + BytesUtils.bytes2HexStr(sendUserMsg.sendDatas));
        return sendUserMsg;
    }
    
    public SendMessage getUserInfoSmg(final ScaleUserInfo userInfo) {
        final SendMessage sendUserMsg = new SendMessage();
        sendUserMsg.srcDatas = buildScaleUserData(userInfo);
        sendUserMsg.desc = "\u4e0b\u53d1\u7528\u6237\u4fe1\u606f";
        sendUserMsg.sendDatas = encrypt(BytesUtils.copyBytes(sendUserMsg.srcDatas));
        return sendUserMsg;
    }
    
    public SendMessage getVersion() {
        final SendMessage sendUserMsg = new SendMessage();
        sendUserMsg.srcDatas = ScaleBytesMakeFatory.getVersionByte;
        sendUserMsg.desc = "\u83b7\u53d6\u7248\u672c\u4fe1\u606f";
        sendUserMsg.sendDatas = encrypt(BytesUtils.copyBytes(sendUserMsg.srcDatas));
        return sendUserMsg;
    }
    
    public static String toBtId(final String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        final StringBuilder sb = new StringBuilder(s);
        final int remainder = s.length() % 7;
        String newStr = "";
        if (remainder > 0) {
            for (int i = 0; i < 7 - remainder; ++i) {
                sb.append("a");
            }
            newStr = sb.toString();
        }
        else {
            newStr = s;
        }
        sb.setLength(0);
        for (int multiple = newStr.length() / 7, j = 0; j * multiple < newStr.length(); ++j) {
            final String singleByte = newStr.substring(j * multiple, (j + 1) * multiple);
            try {
                final int aaa = Integer.parseInt(singleByte, 16);
                if (aaa > 255) {
                    return null;
                }
            }
            catch (Exception e) {
                return null;
            }
            sb.append(singleByte).append(":");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
    
    public static byte[] buildScaleUserData(final ScaleUserInfo userInfo) {
        String[] btIdArr = null;
        final String btId = toBtId(userInfo.getUserId());
        if (btId != null) {
            btIdArr = btId.split(":");
        }
        if (btIdArr == null || btIdArr.length != 7) {
            btIdArr = new String[] { "0", "0", "0", "0", "0", "0", "0" };
        }
        int sexAndAge = userInfo.getAge();
        if (userInfo.getSex() == 1) {
            sexAndAge |= 0x80;
        }
        final int weightInt = (int)(userInfo.getWeight() * 10.0f);
        final int weight1 = weightInt & 0xFF;
        final int weight2 = weightInt >> 8 & 0xFF;
        int resistance1 = 255;
        int resistance2 = 255;
        if (userInfo.getImpedance() >= 200 && userInfo.getImpedance() <= 1500) {
            resistance1 = (userInfo.getImpedance() & 0xFF);
            resistance2 = (userInfo.getImpedance() >> 8 & 0xFF);
        }
        int height = userInfo.getHeight();
        if (userInfo.getHeight() < 100) {
            height = 100;
        }
        else if (userInfo.getHeight() > 220) {
            height = 220;
        }
        final int height2 = height & 0xFF;
        final int height3 = height >> 8 & 0xFF;
        final byte heightByte = (byte)height;
        byte roleTypeByte = 1;
        if (userInfo.getRoleType() == 1) {
            roleTypeByte = 2;
        }
        final byte[] bytes = { -85, 14, -103, (byte)Integer.parseInt(btIdArr[0], 16), (byte)Integer.parseInt(btIdArr[1], 16), (byte)Integer.parseInt(btIdArr[2], 16), (byte)Integer.parseInt(btIdArr[3], 16), (byte)Integer.parseInt(btIdArr[4], 16), (byte)Integer.parseInt(btIdArr[5], 16), (byte)Integer.parseInt(btIdArr[6], 16), (byte)sexAndAge, heightByte, roleTypeByte, (byte)weight1, (byte)weight2, (byte)resistance1, (byte)resistance2 };
        return bytes;
    }
    
    private static byte[] encrypt(final byte[] data) {
        LogUtils.i("ScaleBytesMakeFatory", "----encript--- before --data = " + BytesUtils.bytes2HexStr(data));
        if ((data[0] & 0xFF) == 0xAB) {
            for (int i = 3, j = 0; i < data.length; ++i, ++j) {
                final int n = i;
                data[n] ^= ScaleBytesMakeFatory.deviceAddressBytes[j % 6];
            }
        }
        LogUtils.i("ScaleBytesMakeFatory", "----encript-----data = " + BytesUtils.bytes2HexStr(data));
        return data;
    }
    
    public void setConnectBluetoothName(final String connectBluetoothName) {
        this.connectBluetoothName = connectBluetoothName;
    }
    
    public String getConnectBluetoothName() {
        return this.connectBluetoothName;
    }
    
    public String getConnectBluetoothAddrss() {
        return this.connectBluetoothAddrss;
    }
    
    public void setConnectBluetoothAddrss(final String connectBluetoothAddrss) {
        this.connectBluetoothAddrss = connectBluetoothAddrss;
    }
    
    static {
        HEARTCHECKBYTES = new byte[] { -85, 1, -80 };
        getHistoryDataACK = new byte[] { -85, 2, -101, 1 };
        getMesureDataACK = new byte[] { -85, 2, -93, 0 };
        DeleteAllUserInfoByte = new byte[] { -85, 2, -95, 0 };
        getVersionByte = new byte[] { -85, 1, -100 };
        deviceAddressBytes = new byte[] { 22, 21, 20, 19, 18, 17 };
    }
}
