// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.utils;

import android.os.Looper;
import java.lang.ref.WeakReference;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

public class BaseHandle
{
    private MainHandle mMainHandle;
    private IHandleMsgCallBack msgCallBack;
    
    public void post(final Runnable r) {
        if (this.mMainHandle != null) {
            this.mMainHandle.post(r);
        }
    }
    
    public void postDelayed(final Runnable r, final long delayMillis) {
        if (this.mMainHandle != null) {
            this.mMainHandle.postDelayed(r, delayMillis);
        }
    }
    
    public BaseHandle(final Object... objects) {
        Object object;
        if (objects == null || objects.length == 0) {
            object = this;
        }
        else {
            object = objects[0];
        }
        this.mMainHandle = new MainHandle(object);
    }
    
    public boolean sendEmptyMessage(final int what) {
        return this.sendEmptyMessageDelayed(what, 0L);
    }
    
    public boolean sendEmptyMessageDelayed(final int what, final long delayMillis) {
        return this.mMainHandle != null && this.mMainHandle.sendEmptyMessageDelayed(what, delayMillis);
    }
    
    public boolean sendEmptyMessageAtTime(final int what, final long uptimeMillis) {
        return this.mMainHandle != null && this.mMainHandle.sendEmptyMessageAtTime(what, uptimeMillis);
    }
    
    public boolean sendMessageDelayed(final Message msg, final long delayMillis) {
        return this.mMainHandle != null && this.mMainHandle.sendMessageDelayed(msg, delayMillis);
    }
    
    public boolean sendMessage(final Message msg) {
        return this.sendMessageDelayed(msg, 0L);
    }
    
    public boolean sendMessageValue(final int what, final int... values) {
        if (values == null || values.length == 0) {
            return false;
        }
        final Message msg = Message.obtain();
        msg.what = what;
        if (values.length == 1) {
            msg.arg1 = values[0];
        }
        else if (values.length == 2) {
            msg.arg2 = values[1];
        }
        return this.sendMessage(msg);
    }
    
    public boolean sendMessageObj(final int what, final Object obj) {
        if (obj == null) {
            return false;
        }
        final Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        this.sendMessage(msg);
        return true;
    }
    
    public void removeMessages(final int what) {
        if (this.mMainHandle != null) {
            this.mMainHandle.removeMessages(what);
        }
    }
    
    public void handleMsg(final Message msg) {
    }
    
    public void exit() {
        this.removeCallbacksAndMessages();
        this.msgCallBack = null;
        this.mMainHandle = null;
    }
    
    public void removeCallbacksAndMessages() {
        if (this.mMainHandle != null) {
            this.mMainHandle.removeCallbacksAndMessages((Object)null);
        }
    }
    
    public void setMsgCallBack(final IHandleMsgCallBack msgCallBack) {
        this.msgCallBack = msgCallBack;
    }
    
    @SuppressLint({ "HandlerLeak" })
    class MainHandle extends Handler
    {
        private WeakReference<Object> weakReference;
        
        MainHandle(final Object obj) {
            super(Looper.getMainLooper());
            this.weakReference = new WeakReference<Object>(obj);
        }
        
        public void handleMessage(final Message msg) {
            if (this.weakReference.get() != null) {
                if (BaseHandle.this.msgCallBack != null) {
                    BaseHandle.this.msgCallBack.handleMsg(msg);
                }
                BaseHandle.this.handleMsg(msg);
            }
        }
    }
    
    public interface IHandleMsgCallBack
    {
        void handleMsg(final Message p0);
    }
}
