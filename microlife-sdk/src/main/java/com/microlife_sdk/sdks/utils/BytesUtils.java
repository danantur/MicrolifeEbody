// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.utils;

public class BytesUtils
{
    private static final String TAG = "BytesUtils";
    
    public static String bytes2HexStr(final byte[] data) {
        if (data == null || data.length == 0) {
            return "[ ]";
        }
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (final byte b : data) {
            builder.append(String.format("%02X", b)).append(" ");
        }
        builder.deleteCharAt(builder.lastIndexOf(" "));
        builder.append("]");
        return builder.toString();
    }
    
    public static int getIntFrom2Byte(final byte h, final byte l) {
        return (l & 0xFF) | (h & 0xFF) << 8;
    }
    
    public static byte[] getHLByte(final int value) {
        final byte[] byteArray = { (byte)(value >> 8), (byte)value };
        return byteArray;
    }
    
    public static int getByteHLByIndex(final byte data, final int index) {
        int result;
        if (index == 0) {
            result = (data & 0x80) >> 7;
        }
        else if (index == 1) {
            result = (data & 0x40) >> 6;
        }
        else if (index == 2) {
            result = (data & 0x20) >> 5;
        }
        else if (index == 3) {
            result = (data & 0x10) >> 4;
        }
        else if (index == 4) {
            result = (data & 0x8) >> 3;
        }
        else if (index == 5) {
            result = (data & 0x4) >> 2;
        }
        else if (index == 6) {
            result = (data & 0x2) >> 1;
        }
        else {
            if (index != 7) {
                throw new RuntimeException("\u8fd0\u884c\u65f6\u5f02\u5e38\uff1adata = " + data + "--index = " + index);
            }
            result = (data & 0x1);
        }
        return result;
    }
    
    public static byte[] copyBytes(final byte[] bytes) {
        final byte[] bs = new byte[bytes.length];
        System.arraycopy(bytes, 0, bs, 0, bytes.length);
        return bs;
    }
}
