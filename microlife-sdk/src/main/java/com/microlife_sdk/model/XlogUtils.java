// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model;

import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import android.os.Parcelable;
import android.content.Intent;
import android.content.Context;
import androidx.core.content.FileProvider;
import android.net.Uri;
import android.os.Build;
import com.microlife_sdk.model.protocol.Global;
import java.util.Calendar;
import java.util.ArrayList;
import java.io.IOException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;
import androidx.core.app.ActivityCompat;
import android.os.Environment;
import androidx.annotation.NonNull;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.zip.ZipOutputStream;
import android.app.Activity;
import android.util.Log;

public class XlogUtils
{
    public static String[] PERMISSIONS;
    public static Boolean isShowLog;
    public static String rootPath;
    public static String logPath;
    public static String xlogPath;
    public static Activity baseActivity;
    public static byte[] buf;
    public static ZipOutputStream zipOut;
    public static String zipName;
    public static int bufSize;
    public static int readedBytes;
    public static int SDCARD_LOG_FILE_SAVE_DAYS;
    public static SimpleDateFormat myLogSdf;
    public static SimpleDateFormat logfile;
    public static File zipFile;
    public static boolean checkPermission;
    
    public static void initXlog(@NonNull final Activity baseActivity, final Boolean isShowLog) {
        XlogUtils.isShowLog = isShowLog;
        XlogUtils.baseActivity = baseActivity;
        XlogUtils.checkPermission = checkPermission();
        XlogUtils.logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + XlogUtils.rootPath;
        XlogUtils.xlogPath = XlogUtils.logPath + "/xLog";
        if (XlogUtils.checkPermission) {
            delFile();
        }
    }
    
    public static boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(XlogUtils.baseActivity.getBaseContext(), "android.permission.READ_EXTERNAL_STORAGE") == 0 && ActivityCompat.checkSelfPermission(XlogUtils.baseActivity.getBaseContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && !ActivityCompat.shouldShowRequestPermissionRationale(XlogUtils.baseActivity, "android.permission.READ_EXTERNAL_STORAGE") && !ActivityCompat.shouldShowRequestPermissionRationale(XlogUtils.baseActivity, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            XlogUtils.checkPermission = true;
        }
        else {
            ActivityCompat.requestPermissions(XlogUtils.baseActivity, XlogUtils.PERMISSIONS, 100);
        }
        return XlogUtils.checkPermission;
    }
    
    public static void xLog(final String s, final String s2) {
        Log.e(s, s2);
        if (XlogUtils.checkPermission && XlogUtils.baseActivity != null) {
            writeLogtoFile(s, s2);
        }
    }
    
    public static String getLogPath() {
        String string = "";
        if (Environment.getExternalStorageState().equals("mounted")) {
            string = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + XlogUtils.rootPath + "/xLog";
        }
        final File file;
        if (!(file = new File(string)).exists()) {
            file.mkdirs();
        }
        return string;
    }
    
    public static void writeLogtoFile(String string, String logPath) {
        final String s = string;
        final Date date = new Date();
        final StringBuilder obj;
        (obj = new StringBuilder()).append(XlogUtils.logfile.format(date));
        if (s.equals("BaseCloudApi")) {
            obj.append("-BaseCloudApi");
        }
        string = XlogUtils.myLogSdf.format(date) + "    " + string + "    " + logPath;
        if ((logPath = getLogPath()) != null && !"".equals(logPath)) {
            final File file;
            if (!(file = new File(logPath)).exists()) {
                file.mkdirs();
            }
            File file3 = null;
            final File file2 = file3;
            try {
                file3 = new File(logPath + File.separator + (Object)obj + ".log");
                final FileWriter out = new FileWriter(file2, true);
                final String str = string;
                final BufferedWriter bufferedWriter = new BufferedWriter(out);
                bufferedWriter.write(str);
                bufferedWriter.newLine();
                bufferedWriter.close();
                out.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void delFile() {
        final String logPath;
        if ((logPath = getLogPath()) != null && !"".equals(logPath)) {
            final String format = XlogUtils.logfile.format(getDateBefore());
            final File[] listFiles;
            if ((listFiles = new File(logPath).listFiles()) != null) {
                final File[] array = listFiles;
                final ArrayList<File> list = new ArrayList<File>();
                for (int length = array.length, i = 0; i < length; ++i) {
                    final File file;
                    if (compareDate((file = listFiles[i]).getName().substring(0, 10), format)) {
                        list.add(file);
                    }
                }
                if (list.size() > 0) {
                    for (int j = 0; j < list.size(); ++j) {
                        final File file2;
                        if ((file2 = list.get(j)).exists()) {
                            file2.delete();
                        }
                    }
                }
            }
            final File file3;
            if ((file3 = new File(XlogUtils.logPath, XlogUtils.zipName)).exists()) {
                file3.delete();
            }
        }
    }
    
    public static Boolean compareDate(final String source, final String source2) {
        try {
            final Date parse = XlogUtils.logfile.parse(source);
            try {
                return XlogUtils.logfile.parse(source2).getTime() >= parse.getTime();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        catch (Exception ex2) {}
        return false;
    }
    
    public static Date getDateBefore() {
        final Date time = new Date();
        final Calendar instance = Calendar.getInstance();
        instance.setTime(time);
        instance.set(5, instance.get(5) - XlogUtils.SDCARD_LOG_FILE_SAVE_DAYS);
        return instance.getTime();
    }
    
    public static String logInfoStr() {
        final StringBuffer sb2;
        final StringBuffer sb = sb2 = new StringBuffer();
        sb.append("\nSDK Version\uff1a" + Global.sdkVersion);
        sb.append("\nDevice OS Version \uff1a" + Build.VERSION.RELEASE);
        sb.append("\nDevice MODEL\uff1a" + Build.MODEL);
        String str;
        String str2;
        if (Build.VERSION.SDK_INT >= 24) {
            str = XlogUtils.baseActivity.getResources().getConfiguration().getLocales().get(0).getCountry();
            str2 = XlogUtils.baseActivity.getResources().getConfiguration().getLocales().get(0).getLanguage();
        }
        else {
            str = XlogUtils.baseActivity.getResources().getConfiguration().locale.getCountry();
            str2 = XlogUtils.baseActivity.getResources().getConfiguration().locale.getLanguage();
        }
        final StringBuffer sb3 = sb2;
        sb3.append("\nLanguage\uff1a" + str2);
        sb3.append("\nLocate\uff1a" + str);
        return sb3.toString();
    }
    
    public static Uri getLogZip(final String str) {
        if (XlogUtils.checkPermission) {
            xLog("LogInfo", logInfoStr());
            delFile();
            doZip(XlogUtils.xlogPath);
            return FileProvider.getUriForFile((Context)XlogUtils.baseActivity, str + ".fileprovider", XlogUtils.zipFile);
        }
        return null;
    }
    
    public static void sendSupportMail(final String s, final String str, final String s2) {
        final Intent intent = new Intent();
        final Intent intent3;
        final Intent intent2 = intent3 = intent;
        new Intent("android.intent.action.SEND");
        intent3.setType("application/zip");
        intent3.putExtra("android.intent.extra.EMAIL", new String[] { "ServiceAndroid@Microlife.com.tw" });
        intent3.putExtra("android.intent.extra.SUBJECT", "[" + s + " - Android] Support");
        final StringBuffer sb = new StringBuffer();
        sb.append("App Name \uff1a" + s);
        sb.append("\nApp Version\uff1a" + str);
        sb.append(logInfoStr());
        intent3.putExtra("android.intent.extra.TEXT", sb.toString());
        intent.putExtra("android.intent.extra.STREAM", (Parcelable)getLogZip(s2));
        XlogUtils.baseActivity.startActivity(Intent.createChooser(intent2, (CharSequence)"Send Supprt Mail"));
    }
    
    public static void doZip(final String pathname) {
        XlogUtils.buf = new byte[XlogUtils.bufSize];
        final File file = new File(pathname);
        XlogUtils.zipFile = new File(XlogUtils.logPath + XlogUtils.zipName);
        try {
            handleDir(file, XlogUtils.zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(XlogUtils.zipFile))));
            XlogUtils.zipOut.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void handleDir(final File file, final ZipOutputStream zipOutputStream) throws IOException {
        final File[] listFiles;
        if ((listFiles = file.listFiles()).length == 0) {
            zipOutputStream.putNextEntry(new ZipEntry(file.toString() + "/"));
            zipOutputStream.closeEntry();
        }
        else {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file2;
                if ((file2 = listFiles[i]).isDirectory()) {
                    handleDir(file2, zipOutputStream);
                }
                else {
                    final FileInputStream fileInputStream = new FileInputStream(file2);
                    zipOutputStream.putNextEntry(new ZipEntry(file2.toString()));
                    while ((XlogUtils.readedBytes = fileInputStream.read(XlogUtils.buf)) > 0) {
                        zipOutputStream.write(XlogUtils.buf, 0, XlogUtils.readedBytes);
                    }
                    zipOutputStream.closeEntry();
                }
            }
        }
    }
    
    static {
        XlogUtils.PERMISSIONS = new String[] { "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA" };
        XlogUtils.isShowLog = true;
        XlogUtils.rootPath = "/MicroLifeDeviceLog";
        XlogUtils.zipName = "/MicroLifeDeviceLog.zip";
        XlogUtils.bufSize = 512;
        XlogUtils.SDCARD_LOG_FILE_SAVE_DAYS = 7;
        XlogUtils.myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        XlogUtils.logfile = new SimpleDateFormat("yyyy-MM-dd");
        XlogUtils.checkPermission = false;
    }
}
