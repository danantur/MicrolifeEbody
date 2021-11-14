// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PDFResponse
{
    @SerializedName("pdf_path")
    @Expose
    public String pdfPath;
    @SerializedName("code")
    @Expose
    public int code;
    @SerializedName("info")
    @Expose
    public String info;
    
    public void setPdfPath(final String pdfPath) {
        this.pdfPath = pdfPath;
    }
    
    public String getPdfPath() {
        return this.pdfPath;
    }
    
    public void setCode(final int code) {
        this.code = code;
    }
    
    public int getCode() {
        return this.code;
    }
    
    public void setInfo(final String info) {
        this.info = info;
    }
    
    public String getInfo() {
        return this.info;
    }
    
    @Override
    public String toString() {
        return "PDFResponse{pdf_path = '" + this.pdfPath + '\'' + ",code = '" + this.code + '\'' + ",info = '" + this.info + '\'' + "}";
    }
}
