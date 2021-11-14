// 
// Decompiled by Procyon v0.5.36
// 

package com.microlife_sdk.sdks.bases;

import java.util.Iterator;
import com.microlife_sdk.sdks.utils.LogUtils;
import android.text.TextUtils;
import android.content.IntentFilter;
import android.content.Intent;
import android.app.Activity;
import android.content.BroadcastReceiver;
import java.util.ArrayList;
import com.microlife_sdk.sdks.interfaces.IBlueStationListener;
import java.util.List;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;

public class BlueManager
{
    private static final String TAG = "BlueManager";
    private Context appContext;
    private BluetoothAdapter mBluetoothAdapter;
    private List<IBlueStationListener> mIBlueStationListeners;
    private BluetoothStationReceiver mBluetoothStationReceiver;
    private static BlueManager instance;
    
    private BlueManager() {
    }
    
    public static BlueManager getInstance() {
        if (BlueManager.instance == null) {
            synchronized (BlueManager.class) {
                if (BlueManager.instance == null) {
                    BlueManager.instance = new BlueManager();
                }
            }
        }
        return BlueManager.instance;
    }
    
    public void init(final Context context) {
        this.appContext = context.getApplicationContext();
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mIBlueStationListeners = new ArrayList<IBlueStationListener>();
    }
    
    public void addBluetoothStationListener(final IBlueStationListener listener) {
        if (listener == null) {
            return;
        }
        this.mIBlueStationListeners.add(listener);
        if (this.mIBlueStationListeners.size() == 1) {
            this.registerBluetoothStationReceiver();
        }
    }
    
    public void removeBluetoothStationListener(final IBlueStationListener listener) {
        if (listener == null) {
            return;
        }
        final boolean success = this.mIBlueStationListeners.remove(listener);
        if (success && this.mIBlueStationListeners.size() == 0) {
            this.unRegisterBluetoothStationReceiver();
        }
    }
    
    public void clearAllBluetoothStationListener() {
        final int count = this.mIBlueStationListeners.size();
        this.mIBlueStationListeners.clear();
        if (count > 0) {
            this.unRegisterBluetoothStationReceiver();
        }
    }
    
    private void registerBluetoothStationReceiver() {
        this.mBluetoothStationReceiver = new BluetoothStationReceiver();
        this.appContext.registerReceiver((BroadcastReceiver)this.mBluetoothStationReceiver, this.makeBlueFilters());
    }
    
    private void unRegisterBluetoothStationReceiver() {
        this.appContext.unregisterReceiver((BroadcastReceiver)this.mBluetoothStationReceiver);
        this.mBluetoothStationReceiver = null;
    }
    
    public boolean isBluetoothOpen() {
        return this.mBluetoothAdapter != null && this.mBluetoothAdapter.getState() != 10;
    }
    
    public void openBluetooth() {
        if (this.mBluetoothAdapter != null && !this.isBluetoothOpen()) {
            this.mBluetoothAdapter.enable();
        }
    }
    
    public void openBluetooth2(final Activity activity, final int requestCode) {
        if (activity != null && this.mBluetoothAdapter != null && !this.isBluetoothOpen()) {
            final Intent requestBluetoothOn = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
            requestBluetoothOn.setAction("android.bluetooth.adapter.action.REQUEST_DISCOVERABLE");
            requestBluetoothOn.putExtra("android.bluetooth.adapter.extra.DISCOVERABLE_DURATION", 120);
            activity.startActivityForResult(requestBluetoothOn, requestCode);
        }
    }
    
    public void openBluetooth3(final Activity activity) {
        final Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
        activity.startActivityForResult(enableBtIntent, 101);
    }
    
    public void closeBluetooth() {
        if (this.mBluetoothAdapter != null && this.isBluetoothOpen()) {
            this.mBluetoothAdapter.disable();
        }
    }
    
    public boolean isSupportBluetooth() {
        return this.mBluetoothAdapter != null;
    }
    
    private IntentFilter makeBlueFilters() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
        return intentFilter;
    }
    
    private class BluetoothStationReceiver extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (TextUtils.isEmpty((CharSequence)action)) {
                return;
            }
            if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                final int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                switch (state) {
                    case 10: {
                        LogUtils.i("BlueManager", "--------STATE_OFF \u624b\u673a\u84dd\u7259\u5173\u95ed");
                        for (final IBlueStationListener listener : BlueManager.this.mIBlueStationListeners) {
                            listener.STATE_OFF();
                        }
                        break;
                    }
                    case 13: {
                        LogUtils.i("BlueManager", "--------STATE_TURNING_OFF \u624b\u673a\u84dd\u7259\u6b63\u5728\u5173\u95ed");
                        for (final IBlueStationListener listener : BlueManager.this.mIBlueStationListeners) {
                            listener.STATE_TURNING_OFF();
                        }
                        break;
                    }
                    case 12: {
                        LogUtils.i("BlueManager", "--------STATE_ON \u624b\u673a\u84dd\u7259\u5f00\u542f");
                        for (final IBlueStationListener listener : BlueManager.this.mIBlueStationListeners) {
                            listener.STATE_ON();
                        }
                        break;
                    }
                    case 11: {
                        LogUtils.i("BlueManager", "--------STATE_TURNING_ON \u624b\u673a\u84dd\u7259\u6b63\u5728\u5f00\u542f");
                        for (final IBlueStationListener listener : BlueManager.this.mIBlueStationListeners) {
                            listener.STATE_TURNING_ON();
                        }
                        break;
                    }
                }
            }
        }
    }
}
