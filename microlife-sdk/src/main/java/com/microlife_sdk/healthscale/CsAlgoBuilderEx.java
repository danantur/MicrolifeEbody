// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.healthscale;

import com.microlife_sdk.health.CSAlgorithmUtils;

public class CsAlgoBuilderEx
{
    private float H;
    private float Wt;
    private byte Sex;
    private int Age;
    private float Z;
    private boolean isAuthorized;
    private long mAuthCode;
    private final String ERRMSG1 = "No Authorized Access!";
    private final String ERRMSG2 = "Illegal device!";
    private final long CS_BLE_KEY = 8613800138008L;
    
    public CsAlgoBuilderEx() {
        this.isAuthorized = true;
    }
    
    public void setUserInfo(final float height, final float weight, final byte sex, final int age, final float resistance) {
        this.H = height;
        this.Wt = weight;
        this.Sex = sex;
        this.Age = age;
        this.Z = resistance;
    }
    
    public byte[] getAuthData() {
        final byte[] bData = new byte[13];
        bData[0] = 5;
        bData[1] = -6;
        bData[2] = 9;
        bData[3] = 0;
        final long randomCode = System.currentTimeMillis();
        final byte[] bCode = this.putLong(randomCode);
        bData[4] = bCode[0];
        bData[5] = bCode[1];
        bData[6] = bCode[2];
        bData[7] = bCode[3];
        bData[8] = bCode[4];
        bData[9] = bCode[5];
        bData[10] = bCode[6];
        bData[11] = bCode[7];
        bData[12] = this.getDatasXor(bData, 2, 11);
        this.mAuthCode = randomCode;
        return bData;
    }
    
    public boolean Authorize(final byte[] msg) throws IllegalDeviceException {
        if (msg.length < 13) {
            throw new IllegalDeviceException("Illegal device!");
        }
        if (msg[0] != 5 && msg[0] != -6) {
            throw new IllegalDeviceException("Illegal device!");
        }
        final byte[] bTmp = this.subBytes(msg, 4, 8);
        if (this.getValidateCode(bTmp) == this.mAuthCode) {
            this.isAuthorized = true;
        }
        else {
            this.isAuthorized = false;
        }
        return this.isAuthorized;
    }
    
    private long getValidateCode(final byte[] data) {
        final byte[] bRet = { (byte)(data[0] ^ 0x18), (byte)(data[1] ^ 0x55), (byte)(data[2] ^ 0x7F), (byte)(data[3] ^ 0xFFFFFF8E), (byte)(data[4] ^ 0xFFFFFFD5), (byte)(data[5] ^ 0x7), (byte)(data[6] ^ 0x0), (byte)(data[7] ^ 0x0) };
        final byte[] bleData = { bRet[2], bRet[3], bRet[0], bRet[1], bRet[5], bRet[4], bRet[7], bRet[6] };
        return this.getLong(bleData, 0);
    }
    
    private long getLong(final byte[] bb, final int index) {
        return ((long)bb[index + 0] & 0xFFL) << 56 | ((long)bb[index + 1] & 0xFFL) << 48 | ((long)bb[index + 2] & 0xFFL) << 40 | ((long)bb[index + 3] & 0xFFL) << 32 | ((long)bb[index + 4] & 0xFFL) << 24 | ((long)bb[index + 5] & 0xFFL) << 16 | ((long)bb[index + 6] & 0xFFL) << 8 | ((long)bb[index + 7] & 0xFFL) << 0;
    }
    
    public boolean hasAuthorized() {
        return this.isAuthorized;
    }
    
    public float getEXF() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getEXF(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getInF() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getInF(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getTF() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getTF(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getTFR() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getTFR(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getLBM() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getLBM(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getSLM() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getSLM(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getPM() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getPM(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getFM() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getFM(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getBFR() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getBFR(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getEE() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getEE(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getOD() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getOD(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getMC() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getMC(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getWC() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getWC(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getBMR() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getBMR(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getMSW() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getMSW(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getVFR() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getVFR(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public float getBodyAge() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return (float)new CSAlgorithmUtils().getBodyAge(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    public int getScore() throws NoAuthException {
        if (!this.isAuthorized) {
            throw new NoAuthException("No Authorized Access!");
        }
        return new CSAlgorithmUtils().getScore(this.H, this.Sex, this.Wt, this.Age, (int)this.Z);
    }
    
    private void putInt(final byte[] bb, final int x, final int index) {
        bb[index + 0] = (byte)(x >> 24);
        bb[index + 1] = (byte)(x >> 16);
        bb[index + 2] = (byte)(x >> 8);
        bb[index + 3] = (byte)(x >> 0);
    }
    
    private byte[] putLong(final long x) {
        final byte[] bb = { (byte)(x >> 56 & 0xFFL), (byte)(x >> 48 & 0xFFL), (byte)(x >> 40 & 0xFFL), (byte)(x >> 32 & 0xFFL), (byte)(x >> 24 & 0xFFL), (byte)(x >> 16 & 0xFFL), (byte)(x >> 8 & 0xFFL), (byte)(x & 0xFFL) };
        return bb;
    }
    
    private byte getDatasXor(final byte[] src, final int istart, final int iend) {
        byte dataCheckByte = src[istart];
        for (byte i = (byte)(istart + 1); i <= iend; ++i) {
            dataCheckByte ^= src[i];
        }
        return dataCheckByte;
    }
    
    private int bytesToInt(final byte[] src) {
        final String tmp = this.bytesToHexString(src);
        int sum = 0;
        try {
            sum = Integer.parseInt(tmp, 16);
        }
        catch (NumberFormatException ex) {}
        return sum;
    }
    
    public String bytesToHexStringReverse(final byte[] src) {
        final StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = src.length - 1; i >= 0; --i) {
            final int v = src[i] & 0xFF;
            final String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    
    private String bytesToHexString(final byte[] src) {
        final StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; ++i) {
            final int v = src[i] & 0xFF;
            final String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    
    private byte[] subBytes(final byte[] src, final int begin, final int count) {
        if (count > 0 && src.length >= begin + count) {
            final byte[] bs = new byte[count];
            for (int i = begin; i < begin + count; ++i) {
                bs[i - begin] = src[i];
            }
            return bs;
        }
        return null;
    }
    
    long encrypt(final long plainText) {
        return plainText ^ 0x7D58E7F5518L;
    }
    
    long decrypt(final long cipherText) {
        return cipherText ^ 0x7D58E7F5518L;
    }
}
