// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.model.cloud.api;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSession;
import com.microlife_sdk.model.cloud.model.PushResponse;
import com.microlife_sdk.model.cloud.model.PDFResponse;
import com.microlife_sdk.model.data.Token;
import java.io.InputStream;
import com.microlife_sdk.model.cloud.model.DeviceList;
import com.microlife_sdk.model.cloud.model.AddNote;
import com.microlife_sdk.model.cloud.model.BTHistory;
import com.microlife_sdk.model.cloud.model.WeightHistory;
import com.microlife_sdk.model.cloud.model.MailList;
import com.microlife_sdk.model.cloud.model.ProfileMessage;
import java.io.File;
import okhttp3.MultipartBody;
import com.microlife_sdk.model.cloud.model.BPMHistory;
import com.microlife_sdk.model.cloud.model.BTEventList;
import com.microlife_sdk.model.cloud.model.AddBtEvent;
import com.microlife_sdk.model.cloud.model.BaseMessage;
import com.microlife_sdk.model.cloud.model.ForgotPassword;
import com.microlife_sdk.model.cloud.model.Login;
import com.microlife_sdk.model.cloud.model.Register;
import com.google.gson.JsonSyntaxException;
import okhttp3.Response;
import com.microlife_sdk.model.cloud.model.SystemInfo;
import java.io.IOException;
import okhttp3.Callback;
import android.os.Build;
import okhttp3.FormBody;
import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import javax.net.ssl.TrustManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import androidx.annotation.NonNull;
import com.microlife_sdk.model.XlogUtils;
import javax.net.ssl.HostnameVerifier;
import java.util.concurrent.TimeUnit;
import com.microlife_sdk.model.cloud.listener.OnGetPushHistoryListListener;
import com.microlife_sdk.model.cloud.listener.OnDownloadWeightPdfListener;
import com.microlife_sdk.model.cloud.listener.OnDownloadBTPdfListener;
import com.microlife_sdk.model.cloud.listener.OnDownloadBPMPdfListener;
import com.microlife_sdk.model.cloud.listener.OnGetMemberDataListener;
import com.microlife_sdk.model.cloud.listener.OnGetTokenListener;
import com.microlife_sdk.model.cloud.listener.OnGetDeleteDeviceListener;
import com.microlife_sdk.model.cloud.listener.OnGetDeviceListListener;
import com.microlife_sdk.model.cloud.listener.OnGetAddDeviceListener;
import com.microlife_sdk.model.cloud.listener.OnGetDeleteRecordDataListener;
import com.microlife_sdk.model.cloud.listener.OnGetEditBtDataListener;
import com.microlife_sdk.model.cloud.listener.OnGetEditWeightDataListener;
import com.microlife_sdk.model.cloud.listener.OnGetEditBPMDataListener;
import com.microlife_sdk.model.cloud.listener.OnGetAddNoteDataListener;
import com.microlife_sdk.model.cloud.listener.OnGetBtHistoryListener;
import com.microlife_sdk.model.cloud.listener.OnGetWeightHistoryListener;
import com.microlife_sdk.model.cloud.listener.OnGetMailListListener;
import com.microlife_sdk.model.cloud.listener.OnGetEditMailListener;
import com.microlife_sdk.model.cloud.listener.OnGetAddMailListener;
import com.microlife_sdk.model.cloud.listener.OnGetDeleteAccountListener;
import com.microlife_sdk.model.cloud.listener.OnGetChangePasswordListener;
import com.microlife_sdk.model.cloud.listener.OnGetModifyMemberListener;
import com.microlife_sdk.model.cloud.listener.OnGetBPMHistoryListener;
import com.microlife_sdk.model.cloud.listener.OnGetAddBTDataListener;
import com.microlife_sdk.model.cloud.listener.OnGetBTEventListListener;
import com.microlife_sdk.model.cloud.listener.OnGetEditBtEventListener;
import com.microlife_sdk.model.cloud.listener.OnGETAddBTEventListener;
import com.microlife_sdk.model.cloud.listener.OnGetAddWeightListener;
import com.microlife_sdk.model.cloud.listener.OnGetAddBPMListener;
import com.microlife_sdk.model.cloud.listener.OnGetForgotPasswordListener;
import com.microlife_sdk.model.cloud.listener.OnGetLoginListener;
import com.microlife_sdk.model.cloud.listener.OnGetRegisterListener;
import com.microlife_sdk.model.cloud.listener.OnGetSystemListener;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import android.app.Activity;
import android.util.Log;

public class BaseCloudApi
{
    public static final String TAG = "BaseCloudApi";
    public static Boolean isShowLog;
    public static Activity baseActivity;
    public static final MediaType MEDIA_TYPE_PNG;
    public static final MediaType MEDIA_TYPE_MP3;
    public static final String URL_DevelopSever = "https://dev.microlifecloud.com/";
    public static final String URL_TestSever = "https://developer.microlifecloud.com/";
    public static final String URL_ProductSever = "https://service.microlifecloud.com/";
    public static final String API_SYS = "api/sys";
    public static final String API_Token = "oauth/token";
    public static final String API_Register = "api/register";
    public static final String API_LOGIN = "api/login";
    public static final String API_FORGOT_PASSWORD = "api/forgot_password";
    public static final String API_ADD_BPM = "api/add_bpm";
    public static final String API_ADD_WEIGHT = "api/add_weight_data";
    public static final String API_ADD_BT_EVENT = "api/add_bt_event";
    public static final String API_EDIT_BT_EVENT = "api/edit_bt_event";
    public static final String API_GET_EVENT_LIST = "api/get_bt_event_list";
    public static final String API_ADD_BT_DATA = "api/add_bt_data";
    public static final String API_GET_BPM_HISTORY = "api/get_bpm_history_data";
    public static final String API_GET_WEIGHT_HISTORY = "api/get_weight_history_data";
    public static final String API_GET_BT_HISTORY = "api/get_bt_history_data";
    public static final String API_MODIFY_MEMBER = "api/modify_member";
    public static final String API_CHANGE_PASSWORD = "api/change_password";
    public static final String API_DELETE_ACCOUNT = "api/delete_data";
    public static final String API_ADD_MAIL = "api/add_mail_notification";
    public static final String API_EDIT_MAIL = "api/edit_mail_notification";
    public static final String API_LIST_MAIL = "api/get_mail_notification";
    public static final String API_ADD_NOTE = "api/add_note_data";
    public static final String API_EDIT_BPM = "api/edit_bpm";
    public static final String API_EDIT_WEIGHT = "api/edit_weight_data";
    public static final String API_EDIT_BT = "api/edit_bt_data";
    public static final String API_DELETE_DATA = "api/delete_record_data";
    public static final String API_ADD_DEVICE = "api/add_device";
    public static final String API_GET_DEVICE_LIST = "api/get_device_list";
    public static final String API_DELETE_DEVICE = "api/delete_device_data";
    public static final String API_GET_MEMBER_DATA = "api/get_member_data";
    public static final String API_BPM_PDF = "api/download_bpm_pdf?lang=";
    public static final String API_BT_PDF = "api/download_bt_pdf?lang=";
    public static final String API_WEIGHT_PDF = "api/download_weight_pdf?lang=";
    public static final String API_PUSH_HISTORY = "api/get_push_history_list";
    public static final String Login_URL = "oauth/code?";
    public static final String ChangePassword_URL = "member/member_pwd_upt?";
    public OkHttpClient okHttpClient;
    public RequestBody requestBody;
    public Request request;
    public Call call;
    public Gson gson;
    public String POST_URL;
    public OnGetSystemListener onGetSystemListener;
    public OnGetRegisterListener onGetRegisterListener;
    public OnGetLoginListener onGetLoginListener;
    public OnGetForgotPasswordListener onGetForgotPasswordListener;
    public OnGetAddBPMListener onGetAddBPMListener;
    public OnGetAddWeightListener onGetAddWeightListener;
    public OnGETAddBTEventListener onGetAddBTEventListener;
    public OnGetEditBtEventListener onGetEditBtEventListener;
    public OnGetBTEventListListener onGetBTEventListListener;
    public OnGetAddBTDataListener onGetAddBTDataListener;
    public OnGetBPMHistoryListener onGetBPMHistoryListener;
    public OnGetModifyMemberListener onGetModifyMemberListener;
    public OnGetChangePasswordListener onGetChangePasswordListener;
    public OnGetDeleteAccountListener onGetDeleteAccountListener;
    public OnGetAddMailListener onGetAddMailListener;
    public OnGetEditMailListener onGetEditMailListener;
    public OnGetMailListListener onGetMailListListener;
    public OnGetWeightHistoryListener onGetWeightHistoryListener;
    public OnGetBtHistoryListener onGetBtHistoryListener;
    public OnGetAddNoteDataListener onGetAddNoteDataListener;
    public OnGetEditBPMDataListener onGetEditBPMDataListener;
    public OnGetEditWeightDataListener onGetEditWeightDataListener;
    public OnGetEditBtDataListener onGetEditBtDataListener;
    public OnGetDeleteRecordDataListener onGetDeleteRecordDataListener;
    public OnGetAddDeviceListener onGetAddDeviceListener;
    public OnGetDeviceListListener onGetDeviceListListener;
    public OnGetDeleteDeviceListener onGetDeleteDeviceListener;
    public OnGetTokenListener onGetTokenListener;
    public OnGetMemberDataListener onGetMemberDataListener;
    public OnDownloadBPMPdfListener onDownloadBPMPdfListener;
    public OnDownloadBTPdfListener onDownloadBTPdfListener;
    public OnDownloadWeightPdfListener onDownloadWeightPdfListener;
    public OnGetPushHistoryListListener onGetPushHistoryListListener;
    
    public BaseCloudApi() {
        this.okHttpClient = new OkHttpClient.Builder().connectTimeout(30L, TimeUnit.SECONDS).readTimeout(30L, TimeUnit.SECONDS).sslSocketFactory(createSSLSocketFactory()).hostnameVerifier((HostnameVerifier)new k0()).build();
        this.gson = new Gson();
        this.POST_URL = "https://service.microlifecloud.com/";
    }
    
    public static BaseCloudApi getInstance() {
        final Activity baseActivity;
        if ((baseActivity = BaseCloudApi.baseActivity) != null) {
            XlogUtils.initXlog(baseActivity, BaseCloudApi.isShowLog);
        }
        return i0.a();
    }
    
    public static BaseCloudApi getInstance(@NonNull final Activity baseActivity, final Boolean isShowLog) {
        BaseCloudApi.baseActivity = baseActivity;
        BaseCloudApi.isShowLog = isShowLog;
        XlogUtils.initXlog(BaseCloudApi.baseActivity, BaseCloudApi.isShowLog);
        return i0.a();
    }
    
    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory socketFactory = null;
        try {
            final SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, new TrustManager[] { new j0() }, new SecureRandom());
            socketFactory = instance.getSocketFactory();
        }
        catch (Exception ex) {}
        return socketFactory;
    }
    
    private void getSystemInfo(final String s, final String s2, final String s3, final String s4) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("enc", "Mjg4NzY0OA==").add("client_unique_id", Build.SERIAL).add("os", "android").add("machine_type", s3).add("model", Build.MODEL).add("company", Build.MANUFACTURER).add("push_unique_id", s4).add("gps", s2).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/sys").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$600(BaseCloudApi.this).getSystemMessage(ex.getMessage(), null);
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getSystemInfo\n" + inputStream2String);
                try {
                    final SystemInfo systemInfo = (SystemInfo)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)SystemInfo.class);
                    try {
                        BaseCloudApi.access$600(BaseCloudApi.this).getSystemMessage(null, systemInfo);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$600(BaseCloudApi.this).getSystemMessage(ex.getMessage(), null);
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getRegisterInfo(final String s, final String s2, final String s3, final String s4, final String s5) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("account", s).add("password", s2).add("birthday", s3).add("country", s4).add("register_type", s5).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/register").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$700(BaseCloudApi.this).getRegister(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getRegisterInfo\n" + inputStream2String);
                try {
                    final Register register = (Register)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)Register.class);
                    try {
                        BaseCloudApi.access$700(BaseCloudApi.this).getRegister(register, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$700(BaseCloudApi.this).getRegister(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getLoginInfo(final String s, final String s2) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("account", s).add("password", s2).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/login").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$800(BaseCloudApi.this).getLoginMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getLoginInfo\n" + inputStream2String);
                try {
                    final Login login = (Login)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)Login.class);
                    try {
                        BaseCloudApi.access$800(BaseCloudApi.this).getLoginMessage(login, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$800(BaseCloudApi.this).getLoginMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getForgotPasswordInfo(final String s) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("account", s).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/forgot_password").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$900(BaseCloudApi.this).getForgotPasswordMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getForgotPasswordInfo\n" + inputStream2String);
                try {
                    final ForgotPassword forgotPassword = (ForgotPassword)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)ForgotPassword.class);
                    try {
                        BaseCloudApi.access$900(BaseCloudApi.this).getForgotPasswordMessage(forgotPassword, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$900(BaseCloudApi.this).getForgotPasswordMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getAddBPMInfo(final String s, final String s2, final long l, final int i, final int j, final int k, final int m, final int i2, final int i3, final int i4, final String s3, final String s4) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("sys", String.valueOf(i)).add("dia", String.valueOf(j)).add("pul", String.valueOf(k)).add("user_id", s2).add("bpm_id", String.valueOf(l)).add("afib", String.valueOf(m)).add("pad", String.valueOf(i2)).add("mode", String.valueOf(i3)).add("date", s3).add("mac_address", s4).add("cuffokr", String.valueOf(i4)).build();
        this.request = new Request.Builder().post(this.requestBody).url(this.POST_URL + "api/add_bpm").build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$1000(BaseCloudApi.this).getAddBPMMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getAddBPMInfo\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$1000(BaseCloudApi.this).getAddBPMMessage(baseMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$1000(BaseCloudApi.this).getAddBPMMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getAddWeightInfo(final String s, final int i, final float f, final float f2, final float f3, final float f4, final float f5, final float f6, final float f7, final float f8, final String s2, final String s3) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("weight_id", String.valueOf(i)).add("weight", String.valueOf(f)).add("bmi", String.valueOf(f2)).add("body_fat", String.valueOf(f3)).add("water", String.valueOf(f4)).add("skeleton", String.valueOf(f5)).add("muscle", String.valueOf(f6)).add("bmr", String.valueOf(f7)).add("organ_fat", String.valueOf(f8)).add("date", s2).add("mac_address", s3).build();
        this.request = new Request.Builder().post(this.requestBody).url(this.POST_URL + "api/add_weight_data").build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$1100(BaseCloudApi.this).getAddWeightMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getAddWeightInfo\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$1100(BaseCloudApi.this).getAddWeightMessage(baseMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$1100(BaseCloudApi.this).getAddWeightMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getAddBTEventInfo(final String s, final String s2, final String s3, final String s4, final String s5) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("event_code", s2).add("event", s3).add("type", s4).add("event_time", s5).build();
        this.request = new Request.Builder().post(this.requestBody).url(this.POST_URL + "api/add_bt_event").build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$1200(BaseCloudApi.this).getAddBtEvent(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getAddBTEventInfo\n" + inputStream2String);
                try {
                    final AddBtEvent addBtEvent = (AddBtEvent)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)AddBtEvent.class);
                    try {
                        BaseCloudApi.access$1200(BaseCloudApi.this).getAddBtEvent(addBtEvent, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$1200(BaseCloudApi.this).getAddBtEvent(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getEditBTEventInfo(final String s, final Long n, final String s2, final String s3, final String s4, final String s5) {
        if (s5.equals("1")) {
            this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("event_code", String.valueOf(n)).add("delete", s5).build();
        }
        else {
            this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("event_code", String.valueOf(n)).add("event", s2).add("type", s3).add("event_time", s4).build();
        }
        this.request = new Request.Builder().post(this.requestBody).url(this.POST_URL + "api/edit_bt_event").build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$1300(BaseCloudApi.this).getEditBtEventMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getEditBTEventInfo\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$1300(BaseCloudApi.this).getEditBtEventMessage(baseMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$1300(BaseCloudApi.this).getEditBtEventMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getBTEventListInfo(final String s) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/get_bt_event_list").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$1400(BaseCloudApi.this).getBTEventListmessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getBTEventListInfo\n" + inputStream2String);
                try {
                    final BTEventList list = (BTEventList)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BTEventList.class);
                    try {
                        BaseCloudApi.access$1400(BaseCloudApi.this).getBTEventListmessage(list, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$1400(BaseCloudApi.this).getBTEventListmessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getAddBTDataInfo(final String s, final Long obj, final int i, final float f, final float f2, final String s2, final String s3) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("event_code", String.valueOf(obj)).add("bt_id", String.valueOf(i)).add("body_temp", String.valueOf(f)).add("room_temp", String.valueOf(f2)).add("date", s2).add("mac_address", s3).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/add_bt_data").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$1500(BaseCloudApi.this).getAddBTDataMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getAddBTDataInfo\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$1500(BaseCloudApi.this).getAddBTDataMessage(baseMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$1500(BaseCloudApi.this).getAddBTDataMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getBPMHistoryInfo(final String s) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/get_bpm_history_data").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$1600(BaseCloudApi.this).getBPMHistoryMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getBPMHistoryInfo\n" + inputStream2String);
                try {
                    final BPMHistory bpmHistory = (BPMHistory)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BPMHistory.class);
                    try {
                        BaseCloudApi.access$1600(BaseCloudApi.this).getBPMHistoryMessage(bpmHistory, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$1600(BaseCloudApi.this).getBPMHistoryMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getModifyMemberInfo(final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final String s7, final float n, final float n2, final int n3, final float n4, final int n5, final float n6, final float n7, final float n8, final String s8, final int n9, final int n10, final int n11, final int n12, final int n13, final float n14, final int n15, final int n16, final int n17, final int n18) {
        if ("".equals(s5)) {
            this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("client_id", s2).add("client_secret", s3).add("name", s4).add("birthday", s6).add("gender", s7).add("height", String.valueOf(n)).add("weight", String.valueOf(n2)).add("unit_type", String.valueOf(n3)).add("sys", String.valueOf(n4)).add("sys_unit", String.valueOf(n5)).add("dia", String.valueOf(n6)).add("goal_weight", String.valueOf(n7)).add("body_fat", String.valueOf(n8)).add("conditions", s8).add("sys_activity", String.valueOf(n9)).add("dia_activity", String.valueOf(n10)).add("weight_activity", String.valueOf(n11)).add("body_fat_activity", String.valueOf(n12)).add("threshold", String.valueOf(n13)).add("bmi", String.valueOf(n14)).add("bmi_activity", String.valueOf(n15)).add("cuff_size", String.valueOf(n16)).add("bp_measurement_arm", String.valueOf(n17)).add("date_format", String.valueOf(n18)).build();
        }
        else {
            this.requestBody = (RequestBody)new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("access_token", s).addFormDataPart("client_id", s2).addFormDataPart("client_secret", s3).addFormDataPart("name", s4).addFormDataPart("birthday", s6).addFormDataPart("gender", s7).addFormDataPart("height", String.valueOf(n)).addFormDataPart("weight", String.valueOf(n2)).addFormDataPart("unit_type", String.valueOf(n3)).addFormDataPart("sys", String.valueOf(n4)).addFormDataPart("sys_unit", String.valueOf(n5)).addFormDataPart("dia", String.valueOf(n6)).addFormDataPart("goal_weight", String.valueOf(n7)).addFormDataPart("body_fat", String.valueOf(n8)).addFormDataPart("conditions", s8).addFormDataPart("sys_activity", String.valueOf(n9)).addFormDataPart("dia_activity", String.valueOf(n10)).addFormDataPart("weight_activity", String.valueOf(n11)).addFormDataPart("body_fat_activity", String.valueOf(n12)).addFormDataPart("threshold", String.valueOf(n13)).addFormDataPart("bmi", String.valueOf(n14)).addFormDataPart("bmi_activity", String.valueOf(n15)).addFormDataPart("cuff_size", String.valueOf(n16)).addFormDataPart("bp_measurement_arm", String.valueOf(n17)).addFormDataPart("date_format", String.valueOf(n18)).addFormDataPart("photo", s5, RequestBody.create(BaseCloudApi.MEDIA_TYPE_PNG, new File(s5))).build();
        }
        this.request = new Request.Builder().post(this.requestBody).url(this.POST_URL + "api/modify_member").build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$1700(BaseCloudApi.this).getModifyMember(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getModifyMemberInfo\n" + inputStream2String);
                try {
                    final ProfileMessage profileMessage = (ProfileMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)ProfileMessage.class);
                    try {
                        BaseCloudApi.access$1700(BaseCloudApi.this).getModifyMember(profileMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$1700(BaseCloudApi.this).getModifyMember(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getChangePasswordInfo(final String s, final String s2, final String s3) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("account_id", s).add("old_password", s2).add("new_password", s3).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/change_password").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$1800(BaseCloudApi.this).getChangePasswordMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getChangePasswordInfo\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$1800(BaseCloudApi.this).getChangePasswordMessage(baseMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$1800(BaseCloudApi.this).getChangePasswordMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getDeleteAccountInfo(final String s) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/delete_data").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$1900(BaseCloudApi.this).getDeleteAccountMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getDeleteAccountInfo\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$1900(BaseCloudApi.this).getDeleteAccountMessage(baseMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$1900(BaseCloudApi.this).getDeleteAccountMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getAddMail(final String s, final String s2, final String s3) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("name", s2).add("email", s3).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/add_mail_notification").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$2000(BaseCloudApi.this).getAddMailMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getAddMail\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$2000(BaseCloudApi.this).getAddMailMessage(baseMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$2000(BaseCloudApi.this).getAddMailMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getEditMailInfo(final String s, final String s2, final String s3, final String s4, final String s5) {
        if (s5.equals("")) {
            this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("mail_id", s2).add("name", s3).add("email", s4).build();
        }
        else {
            this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("mail_id", s2).add("delete", s5).build();
        }
        this.request = new Request.Builder().url(this.POST_URL + "api/edit_mail_notification").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$2100(BaseCloudApi.this).getEditMailMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getEditMailInfo\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$2100(BaseCloudApi.this).getEditMailMessage(baseMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$2100(BaseCloudApi.this).getEditMailMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getMailListInfo(final String s) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/get_mail_notification").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$2200(BaseCloudApi.this).getMailListMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getMailListInfo\n" + inputStream2String);
                try {
                    final MailList list = (MailList)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)MailList.class);
                    try {
                        BaseCloudApi.access$2200(BaseCloudApi.this).getMailListMessage(list, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$2200(BaseCloudApi.this).getMailListMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getWeightHistoryInfo(final String s) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/get_weight_history_data").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$2300(BaseCloudApi.this).getWeightHistoryMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getWeightHistoryInfo\n" + inputStream2String);
                try {
                    final WeightHistory weightHistory = (WeightHistory)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)WeightHistory.class);
                    try {
                        BaseCloudApi.access$2300(BaseCloudApi.this).getWeightHistoryMessage(weightHistory, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$2300(BaseCloudApi.this).getWeightHistoryMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getBTHistoryInfo(final String s, final String s2) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("event_code", s2).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/get_bt_history_data").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$2400(BaseCloudApi.this).getBTHistoryMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getBTHistoryInfo\n" + inputStream2String);
                try {
                    final BTHistory btHistory = (BTHistory)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BTHistory.class);
                    try {
                        BaseCloudApi.access$2400(BaseCloudApi.this).getBTHistoryMessage(btHistory, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$2400(BaseCloudApi.this).getBTHistoryMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getAddNoteDataInfo(final String s, final String s2, final String s3, final String s4, final String pathname, final String pathname2, final String s5) {
        if (!pathname.equals("") && !pathname2.equals("")) {
            if (pathname.startsWith("https") && !pathname2.startsWith("https")) {
                this.requestBody = (RequestBody)new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("access_token", s).addFormDataPart("type_id", s2).addFormDataPart("note_type", s3).addFormDataPart("note", s4).addFormDataPart("recording_time", s5).addFormDataPart("photo", pathname).addFormDataPart("recording", pathname2, RequestBody.create(BaseCloudApi.MEDIA_TYPE_MP3, new File(pathname2))).build();
            }
            else if (!pathname.startsWith("https") && pathname2.startsWith("https")) {
                this.requestBody = (RequestBody)new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("access_token", s).addFormDataPart("type_id", s2).addFormDataPart("note_type", s3).addFormDataPart("note", s4).addFormDataPart("recording_time", s5).addFormDataPart("photo", pathname, RequestBody.create(BaseCloudApi.MEDIA_TYPE_PNG, new File(pathname))).addFormDataPart("recording", pathname2).build();
            }
            else if (!pathname.startsWith("https") && !pathname2.startsWith("https")) {
                this.requestBody = (RequestBody)new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("access_token", s).addFormDataPart("type_id", s2).addFormDataPart("note_type", s3).addFormDataPart("note", s4).addFormDataPart("recording_time", s5).addFormDataPart("photo", pathname, RequestBody.create(BaseCloudApi.MEDIA_TYPE_PNG, new File(pathname))).addFormDataPart("recording", pathname2, RequestBody.create(BaseCloudApi.MEDIA_TYPE_MP3, new File(pathname2))).build();
            }
            else {
                this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("type_id", s2).add("note_type", s3).add("note", s4).add("photo", pathname).add("recording_time", s5).add("recording", pathname2).build();
            }
        }
        else if (pathname.equals("") && !pathname2.equals("")) {
            if (pathname2.startsWith("https")) {
                this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("type_id", s2).add("note_type", s3).add("note", s4).add("recording_time", s5).add("photo", pathname).add("recording", pathname2).build();
            }
            else {
                this.requestBody = (RequestBody)new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("access_token", s).addFormDataPart("type_id", s2).addFormDataPart("note_type", s3).addFormDataPart("note", s4).addFormDataPart("recording_time", s5).addFormDataPart("photo", pathname).addFormDataPart("recording", pathname2, RequestBody.create(BaseCloudApi.MEDIA_TYPE_MP3, new File(pathname2))).build();
            }
        }
        else if (!pathname.equals("")) {
            Log.d("debug", pathname2);
            if (pathname.startsWith("https")) {
                this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("type_id", s2).add("note_type", s3).add("note", s4).add("recording_time", s5).add("photo", pathname).add("recording", pathname2).build();
            }
            else {
                this.requestBody = (RequestBody)new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("access_token", s).addFormDataPart("type_id", s2).addFormDataPart("note_type", s3).addFormDataPart("note", s4).addFormDataPart("recording_time", s5).addFormDataPart("photo", pathname, RequestBody.create(BaseCloudApi.MEDIA_TYPE_PNG, new File(pathname))).addFormDataPart("recording", pathname2).build();
            }
        }
        else {
            this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("type_id", s2).add("photo", pathname).add("recording_time", s5).add("recording", pathname2).add("note_type", s3).add("note", s4).build();
        }
        this.request = new Request.Builder().post(this.requestBody).url(this.POST_URL + "api/add_note_data").build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                Log.d("debug", ex.getMessage());
                BaseCloudApi.access$2500(BaseCloudApi.this).getAddNoteDataMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getAddNoteDataInfo\n" + inputStream2String);
                try {
                    final AddNote addNote = (AddNote)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)AddNote.class);
                    try {
                        BaseCloudApi.access$2500(BaseCloudApi.this).getAddNoteDataMessage(addNote, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$2500(BaseCloudApi.this).getAddNoteDataMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getEditBPMDataInfo(final String s, final long l, final int i, final int j, final int k, final String s2) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("bpm_id", String.valueOf(l)).add("sys", String.valueOf(i)).add("dia", String.valueOf(j)).add("pul", String.valueOf(k)).add("date", s2).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/edit_bpm").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$2600(BaseCloudApi.this).getEditBPMDataInfo(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getEditBPMDataInfo\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$2600(BaseCloudApi.this).getEditBPMDataInfo(baseMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$2600(BaseCloudApi.this).getEditBPMDataInfo(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getEditWeightDataInfo(final String s, final int i, final float f, final float f2, final float f3, final String s2) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("weight_id", String.valueOf(i)).add("weight", String.valueOf(f)).add("bmi", String.valueOf(f2)).add("body_fat", String.valueOf(f3)).add("date", s2).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/edit_weight_data").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$2700(BaseCloudApi.this).onGetEditWeightDataMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getEditWeightDataInfo\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$2700(BaseCloudApi.this).onGetEditWeightDataMessage(baseMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$2700(BaseCloudApi.this).onGetEditWeightDataMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getEditBtDataInfo(final String s, final int i, final int j, final float f, final String s2) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("event_code", String.valueOf(i)).add("bt_id", String.valueOf(j)).add("body_temp", String.valueOf(f)).add("date", s2).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/edit_bt_data").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$2800(BaseCloudApi.this).getEditBtDataMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getEditBtDataInfo\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$2800(BaseCloudApi.this).getEditBtDataMessage(baseMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$2800(BaseCloudApi.this).getEditBtDataMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getDeleteRecordDataInfo(final String s, final long l, final int i) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("type_id", String.valueOf(l)).add("note_type", String.valueOf(i)).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/delete_record_data").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$2900(BaseCloudApi.this).getDeleteRecordDataMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getDeleteRecordDataInfo\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$2900(BaseCloudApi.this).getDeleteRecordDataMessage(baseMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        ex.printStackTrace();
                        BaseCloudApi.access$2900(BaseCloudApi.this).getDeleteRecordDataMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getAddDeviceInfo(final String s, final String s2, final String s3, final String s4, final String s5) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("device_type", s2).add("device_model", s3).add("mac_address", s4).add("error_code", s5).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/add_device").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$3000(BaseCloudApi.this).addDeviceMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getAddDeviceInfo\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$3000(BaseCloudApi.this).addDeviceMessage(baseMessage, null);
                    }
                    catch (JsonSyntaxException ex) {
                        ex.printStackTrace();
                        BaseCloudApi.access$3000(BaseCloudApi.this).addDeviceMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    private void getDeviceListInfo(final String s) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/get_device_list").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$3100(BaseCloudApi.this).getDeviceListMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getDeviceListInfo\n" + inputStream2String);
                try {
                    final DeviceList list = (DeviceList)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)DeviceList.class);
                    try {
                        BaseCloudApi.access$3100(BaseCloudApi.this).getDeviceListMessage(list, null);
                    }
                    catch (JsonSyntaxException ex) {
                        ex.printStackTrace();
                        BaseCloudApi.access$3100(BaseCloudApi.this).getDeviceListMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    public static /* synthetic */ OnGetTokenListener access$400(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetTokenListener;
    }
    
    public static /* synthetic */ Gson access$500(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.gson;
    }
    
    public static /* synthetic */ OnGetSystemListener access$600(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetSystemListener;
    }
    
    public static /* synthetic */ OnGetRegisterListener access$700(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetRegisterListener;
    }
    
    public static /* synthetic */ OnGetLoginListener access$800(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetLoginListener;
    }
    
    public static /* synthetic */ OnGetForgotPasswordListener access$900(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetForgotPasswordListener;
    }
    
    public static /* synthetic */ OnGetAddBPMListener access$1000(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetAddBPMListener;
    }
    
    public static /* synthetic */ OnGetAddWeightListener access$1100(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetAddWeightListener;
    }
    
    public static /* synthetic */ OnGETAddBTEventListener access$1200(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetAddBTEventListener;
    }
    
    public static /* synthetic */ OnGetEditBtEventListener access$1300(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetEditBtEventListener;
    }
    
    public static /* synthetic */ OnGetBTEventListListener access$1400(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetBTEventListListener;
    }
    
    public static /* synthetic */ OnGetAddBTDataListener access$1500(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetAddBTDataListener;
    }
    
    public static /* synthetic */ OnGetBPMHistoryListener access$1600(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetBPMHistoryListener;
    }
    
    public static /* synthetic */ OnGetModifyMemberListener access$1700(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetModifyMemberListener;
    }
    
    public static /* synthetic */ OnGetChangePasswordListener access$1800(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetChangePasswordListener;
    }
    
    public static /* synthetic */ OnGetDeleteAccountListener access$1900(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetDeleteAccountListener;
    }
    
    public static /* synthetic */ OnGetAddMailListener access$2000(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetAddMailListener;
    }
    
    public static /* synthetic */ OnGetEditMailListener access$2100(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetEditMailListener;
    }
    
    public static /* synthetic */ OnGetMailListListener access$2200(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetMailListListener;
    }
    
    public static /* synthetic */ OnGetWeightHistoryListener access$2300(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetWeightHistoryListener;
    }
    
    public static /* synthetic */ OnGetBtHistoryListener access$2400(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetBtHistoryListener;
    }
    
    public static /* synthetic */ OnGetAddNoteDataListener access$2500(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetAddNoteDataListener;
    }
    
    public static /* synthetic */ OnGetEditBPMDataListener access$2600(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetEditBPMDataListener;
    }
    
    public static /* synthetic */ OnGetEditWeightDataListener access$2700(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetEditWeightDataListener;
    }
    
    public static /* synthetic */ OnGetEditBtDataListener access$2800(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetEditBtDataListener;
    }
    
    public static /* synthetic */ OnGetDeleteRecordDataListener access$2900(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetDeleteRecordDataListener;
    }
    
    public static /* synthetic */ OnGetAddDeviceListener access$3000(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetAddDeviceListener;
    }
    
    public static /* synthetic */ OnGetDeviceListListener access$3100(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetDeviceListListener;
    }
    
    public static /* synthetic */ OnGetDeleteDeviceListener access$3200(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetDeleteDeviceListener;
    }
    
    public static /* synthetic */ OnGetMemberDataListener access$3300(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetMemberDataListener;
    }
    
    public static /* synthetic */ OnDownloadBPMPdfListener access$3400(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onDownloadBPMPdfListener;
    }
    
    public static /* synthetic */ OnDownloadWeightPdfListener access$3500(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onDownloadWeightPdfListener;
    }
    
    public static /* synthetic */ OnDownloadBTPdfListener access$3600(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onDownloadBTPdfListener;
    }
    
    public static /* synthetic */ OnGetPushHistoryListListener access$3700(final BaseCloudApi baseCloudApi) {
        return baseCloudApi.onGetPushHistoryListListener;
    }
    
    static {
        BaseCloudApi.isShowLog = true;
        BaseCloudApi.baseActivity = null;
        MEDIA_TYPE_PNG = MediaType.parse("image/png");
        MEDIA_TYPE_MP3 = MediaType.parse("audio/mpeg");
    }
    
    public void setOnGetSystemListener(final OnGetSystemListener onGetSystemListener) {
        this.onGetSystemListener = onGetSystemListener;
    }
    
    public void setOnGetRegisterListener(final OnGetRegisterListener onGetRegisterListener) {
        this.onGetRegisterListener = onGetRegisterListener;
    }
    
    public void setOnGetLoginListener(final OnGetLoginListener onGetLoginListener) {
        this.onGetLoginListener = onGetLoginListener;
    }
    
    public void setOnGetForgotPasswordListener(final OnGetForgotPasswordListener onGetForgotPasswordListener) {
        this.onGetForgotPasswordListener = onGetForgotPasswordListener;
    }
    
    public void setOnGetAddBPMListener(final OnGetAddBPMListener onGetAddBPMListener) {
        this.onGetAddBPMListener = onGetAddBPMListener;
    }
    
    public void setOnGetAddWeightListener(final OnGetAddWeightListener onGetAddWeightListener) {
        this.onGetAddWeightListener = onGetAddWeightListener;
    }
    
    public void setOnGetAddBTEventListener(final OnGETAddBTEventListener onGetAddBTEventListener) {
        this.onGetAddBTEventListener = onGetAddBTEventListener;
    }
    
    public void setOnGetEditBtEventListener(final OnGetEditBtEventListener onGetEditBtEventListener) {
        this.onGetEditBtEventListener = onGetEditBtEventListener;
    }
    
    public void setOnGetBTEventListListener(final OnGetBTEventListListener onGetBTEventListListener) {
        this.onGetBTEventListListener = onGetBTEventListListener;
    }
    
    public void setOnGetAddBTDataListener(final OnGetAddBTDataListener onGetAddBTDataListener) {
        this.onGetAddBTDataListener = onGetAddBTDataListener;
    }
    
    public void setOnGetBPMHistoryListener(final OnGetBPMHistoryListener onGetBPMHistoryListener) {
        this.onGetBPMHistoryListener = onGetBPMHistoryListener;
    }
    
    public void setOnGetModifyMemberListener(final OnGetModifyMemberListener onGetModifyMemberListener) {
        this.onGetModifyMemberListener = onGetModifyMemberListener;
    }
    
    public void setOnGetChangePasswordListener(final OnGetChangePasswordListener onGetChangePasswordListener) {
        this.onGetChangePasswordListener = onGetChangePasswordListener;
    }
    
    public void setOnGetDeleteAccountListener(final OnGetDeleteAccountListener onGetDeleteAccountListener) {
        this.onGetDeleteAccountListener = onGetDeleteAccountListener;
    }
    
    public void setOnGetAddMailListener(final OnGetAddMailListener onGetAddMailListener) {
        this.onGetAddMailListener = onGetAddMailListener;
    }
    
    public void setOnGetEditMailListener(final OnGetEditMailListener onGetEditMailListener) {
        this.onGetEditMailListener = onGetEditMailListener;
    }
    
    public void setOnGetMailListListener(final OnGetMailListListener onGetMailListListener) {
        this.onGetMailListListener = onGetMailListListener;
    }
    
    public void setOnGetWeightHistoryListener(final OnGetWeightHistoryListener onGetWeightHistoryListener) {
        this.onGetWeightHistoryListener = onGetWeightHistoryListener;
    }
    
    public void setOnGetBtHistoryListener(final OnGetBtHistoryListener onGetBtHistoryListener) {
        this.onGetBtHistoryListener = onGetBtHistoryListener;
    }
    
    public void setOnGetAddNoteDataListener(final OnGetAddNoteDataListener onGetAddNoteDataListener) {
        this.onGetAddNoteDataListener = onGetAddNoteDataListener;
    }
    
    public void setOnGetEditBPMDataListener(final OnGetEditBPMDataListener onGetEditBPMDataListener) {
        this.onGetEditBPMDataListener = onGetEditBPMDataListener;
    }
    
    public void setOnGetEditWeightDataListener(final OnGetEditWeightDataListener onGetEditWeightDataListener) {
        this.onGetEditWeightDataListener = onGetEditWeightDataListener;
    }
    
    public void setOnGetEditBtDataListener(final OnGetEditBtDataListener onGetEditBtDataListener) {
        this.onGetEditBtDataListener = onGetEditBtDataListener;
    }
    
    public void setOnGetDeleteRecordDataListener(final OnGetDeleteRecordDataListener onGetDeleteRecordDataListener) {
        this.onGetDeleteRecordDataListener = onGetDeleteRecordDataListener;
    }
    
    public void setOnGetAddDeviceListener(final OnGetAddDeviceListener onGetAddDeviceListener) {
        this.onGetAddDeviceListener = onGetAddDeviceListener;
    }
    
    public void setOnGetDeviceListListener(final OnGetDeviceListListener onGetDeviceListListener) {
        this.onGetDeviceListListener = onGetDeviceListListener;
    }
    
    public void setOnGetDeleteDeviceListener(final OnGetDeleteDeviceListener onGetDeleteDeviceListener) {
        this.onGetDeleteDeviceListener = onGetDeleteDeviceListener;
    }
    
    public void setOnGetTokenListener(final OnGetTokenListener onGetTokenListener) {
        this.onGetTokenListener = onGetTokenListener;
    }
    
    public void setOnGetMemberDataListener(final OnGetMemberDataListener onGetMemberDataListener) {
        this.onGetMemberDataListener = onGetMemberDataListener;
    }
    
    public void setOnDownloadBPMPdfListener(final OnDownloadBPMPdfListener onDownloadBPMPdfListener) {
        this.onDownloadBPMPdfListener = onDownloadBPMPdfListener;
    }
    
    public void setOnDownloadWeightPdfListener(final OnDownloadWeightPdfListener onDownloadWeightPdfListener) {
        this.onDownloadWeightPdfListener = onDownloadWeightPdfListener;
    }
    
    public void setOnDownloadBTPdfListener(final OnDownloadBTPdfListener onDownloadBTPdfListener) {
        this.onDownloadBTPdfListener = onDownloadBTPdfListener;
    }
    
    public void setOnGetPushHistoryListListener(final OnGetPushHistoryListListener onGetPushHistoryListListener) {
        this.onGetPushHistoryListListener = onGetPushHistoryListListener;
    }
    
    public String inputStream2String(final InputStream inputStream) throws IOException {
        final StringBuffer sb = new StringBuffer();
        final byte[] array = new byte[4096];
        int read;
        while ((read = inputStream.read(array)) != -1) {
            sb.append(new String(array, 0, read));
        }
        return sb.toString();
    }
    
    public void setMainSeverType(final SeverType severType) {
        final int n;
        if ((n = severType.ordinal()) != 1) {
            if (n != 2) {
                if (n == 3) {
                    this.POST_URL = "https://service.microlifecloud.com/";
                }
            }
            else {
                this.POST_URL = "https://developer.microlifecloud.com/";
            }
        }
        else {
            this.POST_URL = "https://dev.microlifecloud.com/";
        }
    }
    
    public String loginURL(final String str, final String str2, final String str3) {
        return this.POST_URL + "oauth/code?" + "client_id=" + str + "&redirect_uri=" + str2 + "&lang=" + str3;
    }
    
    public String changePasswordURL(final String str) {
        return this.POST_URL + "member/member_pwd_upt?" + "lang=" + str;
    }
    
    public void setSysInfo(final String s, final String s2, final String s3, final String s4) {
        this.getSystemInfo(s, s2, s3, s4);
    }
    
    public void setRegisterInfo(final String s, final String s2, final String s3, final String s4, final String s5) {
        this.getRegisterInfo(s, s2, s3, s4, s5);
    }
    
    public void setLoginInfo(final String s, final String s2) {
        this.getLoginInfo(s, s2);
    }
    
    public void setForgotPasswordInfo(final String s) {
        this.getForgotPasswordInfo(s);
    }
    
    public void setAddBPMInfo(final String s, final String s2, final long n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final String s3, final String s4) {
        this.getAddBPMInfo(s, s2, n, n2, n3, n4, n5, n6, n7, n8, s3, s4);
    }
    
    public void setAddWeightInfo(final String s, final int n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final float n8, final float n9, final String s2, final String s3) {
        this.getAddWeightInfo(s, n, n2, n3, n4, n5, n6, n7, n8, n9, s2, s3);
    }
    
    public void setAddBTEventInfo(final String s, final String s2, final String s3, final String s4, final String s5) {
        this.getAddBTEventInfo(s, s2, s3, s4, s5);
    }
    
    public void setEditBTEventInfo(final String s, final Long n, final String s2, final String s3, final String s4, final String s5) {
        this.getEditBTEventInfo(s, n, s2, s3, s4, s5);
    }
    
    public void setGetEventListInfo(final String s) {
        this.getBTEventListInfo(s);
    }
    
    public void setAddBtDataInfo(final String s, final Long n, final int n2, final float n3, final float n4, final String s2, final String s3) {
        this.getAddBTDataInfo(s, n, n2, n3, n4, s2, s3);
    }
    
    public void setGetBPMHistoryInfo(final String s) {
        this.getBPMHistoryInfo(s);
    }
    
    public void setModifyMemberInfo(final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final String s7, final float n, final float n2, final int n3, final float n4, final int n5, final float n6, final float n7, final float n8, final String s8, final int n9, final int n10, final int n11, final int n12, final int n13, final float n14, final int n15, final int n16, final int n17, final int n18) {
        this.getModifyMemberInfo(s, s2, s3, s4, s5, s6, s7, n, n2, n3, n4, n5, n6, n7, n8, s8, n9, n10, n11, n12, n13, n14, n15, n16, n17, n18);
    }
    
    public void setChangePasswordInfo(final String s, final String s2, final String s3) {
        this.getChangePasswordInfo(s, s2, s3);
    }
    
    public void setDeleteAccountInfo(final String s) {
        this.getDeleteAccountInfo(s);
    }
    
    public void setAddMailInfo(final String s, final String s2, final String s3) {
        this.getAddMail(s, s2, s3);
    }
    
    public void setEditMailInfo(final String s, final String s2, final String s3, final String s4, final String s5) {
        this.getEditMailInfo(s, s2, s3, s4, s5);
    }
    
    public void setGetMailListInfo(final String s) {
        this.getMailListInfo(s);
    }
    
    public void setGetWeightHistoryInfo(final String s) {
        this.getWeightHistoryInfo(s);
    }
    
    public void setGetBTHistoryInfo(final String s, final String s2) {
        this.getBTHistoryInfo(s, s2);
    }
    
    public void setGetAddNoteDataInfo(final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final String s7) {
        this.getAddNoteDataInfo(s, s2, s3, s4, s5, s6, s7);
    }
    
    public void setGetEditBPMDataInfo(final String s, final long n, final int n2, final int n3, final int n4, final String s2) {
        this.getEditBPMDataInfo(s, n, n2, n3, n4, s2);
    }
    
    public void setGetEditWeightDataInfo(final String s, final int n, final float n2, final float n3, final float n4, final String s2) {
        this.getEditWeightDataInfo(s, n, n2, n3, n4, s2);
    }
    
    public void setGetEditBtDataInfo(final String s, final int n, final int n2, final float n3, final String s2) {
        this.getEditBtDataInfo(s, n, n2, n3, s2);
    }
    
    public void setGetDeleteRecordDataInfo(final String s, final long n, final int n2) {
        this.getDeleteRecordDataInfo(s, n, n2);
    }
    
    public void setGetAddDeviceInfo(final String s, final int i, final String s2, final String s3, final String s4) {
        this.getAddDeviceInfo(s, String.valueOf(i), s2, s3, s4);
    }
    
    public void setGetDeviceListInfo(final String s) {
        this.getDeviceListInfo(s);
    }
    
    public void getToken(final String s, final String s2, final String s3, final String s4, final String s5, final String s6) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("grant_type", s).add("code", s2).add("refresh_token", s3).add("client_id", s4).add("client_secret", s5).add("redirect_uri", s6).build();
        this.request = new Request.Builder().url(this.POST_URL + "oauth/token").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$400(BaseCloudApi.this).getToken(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getToken\n" + inputStream2String);
                try {
                    final Token token = (Token)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)Token.class);
                    try {
                        BaseCloudApi.access$400(BaseCloudApi.this).getToken(token, "");
                    }
                    catch (JsonSyntaxException ex) {
                        BaseCloudApi.access$400(BaseCloudApi.this).getToken(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    public void setDeleteDeviceInfo(final String s, final String s2) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("mac_address", s2).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/delete_device_data").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                BaseCloudApi.access$3200(BaseCloudApi.this).getDeleteDeviceMessage(null, ex.getMessage());
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "setDeleteDeviceInfo\n" + inputStream2String);
                try {
                    final BaseMessage baseMessage = (BaseMessage)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)BaseMessage.class);
                    try {
                        BaseCloudApi.access$3200(BaseCloudApi.this).getDeleteDeviceMessage(baseMessage, "");
                    }
                    catch (JsonSyntaxException ex) {
                        ex.printStackTrace();
                        BaseCloudApi.access$3200(BaseCloudApi.this).getDeleteDeviceMessage(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    public void getMemberData(final String s, final String s2, final String s3) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).add("client_id", s2).add("client_secret", s3).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/get_member_data").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                if (BaseCloudApi.access$3300(BaseCloudApi.this) != null) {
                    BaseCloudApi.access$3300(BaseCloudApi.this).getMemberDataResponse(null, ex.getMessage());
                }
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getMemberData\n" + inputStream2String);
                try {
                    final Login login = (Login)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)Login.class);
                    try {
                        if (BaseCloudApi.access$3300(BaseCloudApi.this) != null) {
                            BaseCloudApi.access$3300(BaseCloudApi.this).getMemberDataResponse(login, "");
                        }
                    }
                    catch (JsonSyntaxException ex) {
                        if (BaseCloudApi.access$3300(BaseCloudApi.this) == null) {
                            return;
                        }
                        BaseCloudApi.access$3300(BaseCloudApi.this).getMemberDataResponse(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    public void downLoadBPMPDF(final String str, final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final String s7, final String pathname) {
        this.requestBody = (RequestBody)new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("access_token", s).addFormDataPart("start_date", s2).addFormDataPart("end_date", s3).addFormDataPart("sys_threshold", s4).addFormDataPart("dia_threshold", s5).addFormDataPart("unit", s6).addFormDataPart("decimal", s7).addFormDataPart("photo", pathname, RequestBody.create(BaseCloudApi.MEDIA_TYPE_PNG, new File(pathname))).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/download_bpm_pdf?lang=" + str).post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                if (BaseCloudApi.access$3400(BaseCloudApi.this) != null) {
                    BaseCloudApi.access$3400(BaseCloudApi.this).onDownloadBpm(null, ex.getMessage());
                }
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "downLoadBPMPDF\n" + inputStream2String);
                try {
                    final PDFResponse pdfResponse = (PDFResponse)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)PDFResponse.class);
                    try {
                        if (BaseCloudApi.access$3400(BaseCloudApi.this) != null) {
                            BaseCloudApi.access$3400(BaseCloudApi.this).onDownloadBpm(pdfResponse, "");
                        }
                    }
                    catch (JsonSyntaxException ex) {
                        if (BaseCloudApi.access$3400(BaseCloudApi.this) == null) {
                            return;
                        }
                        BaseCloudApi.access$3400(BaseCloudApi.this).onDownloadBpm(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    public void downLoadWeightPDF(final String str, final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final String s7, final String s8, final String pathname) {
        this.requestBody = (RequestBody)new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("access_token", s).addFormDataPart("start_date", s2).addFormDataPart("end_date", s3).addFormDataPart("wei_threshold", s4).addFormDataPart("bmi_threshold", s5).addFormDataPart("fat_threshold", s6).addFormDataPart("unit", s7).addFormDataPart("decimal", s8).addFormDataPart("photo", pathname, RequestBody.create(BaseCloudApi.MEDIA_TYPE_PNG, new File(pathname))).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/download_weight_pdf?lang=" + str).post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                if (BaseCloudApi.access$3500(BaseCloudApi.this) != null) {
                    BaseCloudApi.access$3500(BaseCloudApi.this).onDownLoadWeight(null, ex.getMessage());
                }
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "downLoadWeightPDF\n" + inputStream2String);
                try {
                    final PDFResponse pdfResponse = (PDFResponse)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)PDFResponse.class);
                    try {
                        if (BaseCloudApi.access$3500(BaseCloudApi.this) != null) {
                            BaseCloudApi.access$3500(BaseCloudApi.this).onDownLoadWeight(pdfResponse, "");
                        }
                    }
                    catch (JsonSyntaxException ex) {
                        if (BaseCloudApi.access$3500(BaseCloudApi.this) == null) {
                            return;
                        }
                        BaseCloudApi.access$3500(BaseCloudApi.this).onDownLoadWeight(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    public void downLoadBTPDF(final String str, final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final String pathname) {
        this.requestBody = (RequestBody)new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("access_token", s).addFormDataPart("start_date", s2).addFormDataPart("end_date", s3).addFormDataPart("event_code", s4).addFormDataPart("threshold", "37.5").addFormDataPart("unit", s5).addFormDataPart("decimal", s6).addFormDataPart("photo", pathname, RequestBody.create(BaseCloudApi.MEDIA_TYPE_PNG, new File(pathname))).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/download_bt_pdf?lang=" + str).post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                if (BaseCloudApi.access$3600(BaseCloudApi.this) != null) {
                    BaseCloudApi.access$3600(BaseCloudApi.this).onDownloadBt(null, ex.getMessage());
                }
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "downLoadBTPDF\n" + inputStream2String);
                try {
                    final PDFResponse pdfResponse = (PDFResponse)BaseCloudApi.access$500(BaseCloudApi.this).fromJson(inputStream2String, (Class)PDFResponse.class);
                    try {
                        if (BaseCloudApi.access$3600(BaseCloudApi.this) != null) {
                            BaseCloudApi.access$3600(BaseCloudApi.this).onDownloadBt(pdfResponse, "");
                        }
                    }
                    catch (JsonSyntaxException ex) {
                        if (BaseCloudApi.access$3600(BaseCloudApi.this) == null) {
                            return;
                        }
                        BaseCloudApi.access$3600(BaseCloudApi.this).onDownloadBt(null, ex.getMessage());
                    }
                }
                catch (JsonSyntaxException ex2) {}
            }
        });
    }
    
    public void getPushHistoryList(final String s) {
        this.requestBody = (RequestBody)new FormBody.Builder().add("access_token", s).build();
        this.request = new Request.Builder().url(this.POST_URL + "api/get_push_history_list").post(this.requestBody).build();
        (this.call = this.okHttpClient.newCall(this.request)).enqueue((Callback)new Callback() {
            public void onFailure(final Call call, final IOException ex) {
                if (BaseCloudApi.access$3700(BaseCloudApi.this) != null) {
                    BaseCloudApi.access$3700(BaseCloudApi.this).getPushHistoryMessage(null, ex.getMessage());
                }
            }
            
            public void onResponse(final Call call, final Response response) throws IOException {
                final String inputStream2String = BaseCloudApi.this.inputStream2String(response.body().byteStream());
                XlogUtils.xLog("BaseCloudApi", "getPushHistoryList\n" + inputStream2String);
                try {
                    try {
                        final PushResponse pushResponse = (PushResponse)new Gson().fromJson(inputStream2String, (Class)PushResponse.class);
                        try {
                            if (BaseCloudApi.access$3700(BaseCloudApi.this) != null) {
                                BaseCloudApi.access$3700(BaseCloudApi.this).getPushHistoryMessage(pushResponse, "");
                            }
                        }
                        catch (JsonSyntaxException ex) {
                            if (BaseCloudApi.access$3700(BaseCloudApi.this) == null) {
                                return;
                            }
                            BaseCloudApi.access$3700(BaseCloudApi.this).getPushHistoryMessage(null, ex.getMessage());
                        }
                    }
                    catch (JsonSyntaxException ex2) {}
                }
                catch (JsonSyntaxException ex3) {}
            }
        });
    }
    
    public static class k0 implements HostnameVerifier
    {
        @Override
        public boolean verify(final String s, final SSLSession sslSession) {
            return true;
        }
    }
    
    public static class j0 implements X509TrustManager
    {
        @Override
        public void checkClientTrusted(final X509Certificate[] array, final String s) throws CertificateException {
        }
        
        @Override
        public void checkServerTrusted(final X509Certificate[] array, final String s) throws CertificateException {
        }
        
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
    
    public static class i0
    {
        public static final BaseCloudApi a;
        
        public static /* synthetic */ BaseCloudApi a() {
            return i0.a;
        }
        
        static {
            a = new BaseCloudApi();
        }
    }
    
    public enum SeverType
    {
        MicroLifeDevelopSever, 
        MicroLifeTestSever, 
        MicroLifeProductSever;
    }
}
