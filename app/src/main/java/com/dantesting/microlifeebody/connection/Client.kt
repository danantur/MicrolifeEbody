package com.dantesting.microlifeebody.connection

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import com.dantesting.microlifeebody.connection.Commands.AppCommand.*
import com.dantesting.microlifeebody.connection.Commands.DeviceCommand.*
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.exceptions.BleException
import com.polidea.rxandroidble2.exceptions.BleScanException
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.disposables.Disposable

@Suppress("NAME_SHADOWING")
class Client(ctx: Context, private val stateCallback: StateCallback? = null,
             private val searchCallback: SearchCallback? = null,
             private val connectionCallback: ConnectionCallback? = null) {

    private fun safeDispose(vararg subs: Disposable?) {
        for (sub in subs)
            if (sub != null)
                if (!sub.isDisposed)
                    sub.dispose()
    }

    private val TAG: String = this.javaClass.name

    private val bleClient: RxBleClient = RxBleClient.create(ctx)

    private var stateDisposable: Disposable = getState()
    private var searchDisposable: Disposable? = null

    private var device: RxBleDevice? = null

    private var connection: RxBleConnection? = null
    private var connectionDisposable: Disposable? = null
    private var connectionStateDisposable: Disposable? = null

    private var heartHandler = Handler(Looper.getMainLooper())
    private var user: Commands.ScaleUserInfo? = null

    @SuppressLint("CheckResult")
    fun sendCommand(cmd: Commands.AppCommand, data: Any? = null) {
        if (connection != null) {
            connection!!.writeCharacteristic(Commands.writeUuid, Commands.packWeightCommand(cmd, data))
                .subscribe({
                    heartHandler.removeCallbacksAndMessages(null)
                    heartHandler.postDelayed({
                        sendCommand(HEART_SMG)
                    }, 2000)
                    connectionCallback!!.onSendSuccess(cmd, data)
                }, {
                    connectionCallback!!.onConnectionError(it as BleException)
                })
        }
        else
            connectionCallback?.onConnectionError(BleException("connection is not set up yet!"))
    }

    fun connect(mac: String, user: Commands.ScaleUserInfo) {
        device = bleClient.getBleDevice(mac)
        this.user = user
        disconnect()
        connectionDisposable = getConnSub()
        connectionStateDisposable = getConnStateSub()
    }

    fun disconnect() {
        safeDispose(connectionDisposable, connectionStateDisposable, stateDisposable)
    }

    fun checkState() {
        handleScanState(bleClient.state)
        stateDisposable = getState()
    }

    private fun handleScanState(state: RxBleClient.State) {
        when (state) {
            RxBleClient.State.READY -> stateCallback?.onReady()
            RxBleClient.State.BLUETOOTH_NOT_AVAILABLE -> stateCallback?.onError("Bluetooth не доступен на вашем устройстве")
            RxBleClient.State.LOCATION_PERMISSION_NOT_GRANTED -> stateCallback?.onLocationPermRequired()
            RxBleClient.State.BLUETOOTH_NOT_ENABLED -> stateCallback?.onBluetoothRequired()
            RxBleClient.State.LOCATION_SERVICES_NOT_ENABLED -> stateCallback?.onLocationRequired()
        }
    }

    fun startSearching() {
        if (searchCallback != null) {
            safeDispose(searchDisposable)
            stateDisposable = getState()
            searchDisposable = getSearch()
        }
        else
            stateCallback?.onError("Клиент инициализирован на поиск!")
    }

    fun endSearching() {
        safeDispose(searchDisposable, stateDisposable)
    }

    fun getBleDevice(mac: String): RxBleDevice {
        return bleClient.getBleDevice(mac)
    }

    private fun getSearch(): Disposable =
        bleClient.scanBleDevices(ScanSettings.Builder().build(), ScanFilter.empty())
            .subscribe(
                { scanResult ->
                    searchCallback?.onDeviceFound(scanResult.bleDevice)
                },
                { throwable ->
                    if (throwable is BleScanException) {
                        stateCallback?.onError(
                                when (throwable.reason) {
                                    BleScanException.BLUETOOTH_CANNOT_START -> "ошибка при запуске службы Bluetooth"
                                    BleScanException.SCAN_FAILED_ALREADY_STARTED -> "сканирование уже запущено"
                                    BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> "ошибка регистрации приложения"
                                    BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED -> "ваше устройство не поддерживает сканирование через Bluetooth"
                                    BleScanException.SCAN_FAILED_INTERNAL_ERROR -> "Внутренняя ошибка сканирования"
                                    BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES -> "Нехватка ресурсов оборудования"
                                    BleScanException.UNDOCUMENTED_SCAN_THROTTLE -> "UNDOCUMENTED_SCAN_THROTTLE"
                                    BleScanException.UNKNOWN_ERROR_CODE -> "Неизвестная ошибка"
                                    else -> return@subscribe
                                })
                    }
                })

    private fun getState(): Disposable =
        bleClient.observeStateChanges()
            .subscribe({ state ->
                handleScanState(state)
            },{ throwable ->
                Log.e(TAG, throwable.stackTraceToString())
            })

    private fun getConnSub(): Disposable =
        device!!.establishConnection(false)
            .subscribe(
                { connection_ ->
                    connection = connection_
                    connection_.discoverServices()
                        .subscribe({
                            for (r in it.bluetoothGattServices) {
                                Log.e("service", r.uuid.toString())
                                for (e in r.characteristics) {
                                    Log.e("char", e.uuid.toString())
                                }
                            }
                        }, { throwable ->
                            connectionCallback?.onConnectionError(throwable as BleException)
                        })
                    connection_.setupNotification(Commands.notifyUuid)
                        .flatMap { it }
                        .subscribe({
                            try {
                                val response = Commands.parseWeightResponse(it)
                                    ?: throw Exception("Не удалось прочитать ответ с устройства")
                                val (cmd, data) = response
                                when (cmd) {
                                    RECEIVE_MEASURE_DATA -> {
                                        sendCommand(MEASURE_DATA_ACK)
                                        user!!.impedance = (data as Commands.Measurement).resistance
                                        user!!.weight = data.weight
                                        sendCommand(SEND_USER_INFO, user)
                                    }
                                    RECEIVE_HISTORY_MEASUREMENT_RESULT ->
                                        sendCommand(HISTORY_DATA_ACK)
                                    RECEIVE_TIME -> 
                                        sendCommand(SEND_USER_INFO, user)
                                    WAKE -> {
                                        if (TextUtils.equals(
                                                "Body Fat-B1",
                                                device!!.name
                                            ) || TextUtils.equals(
                                                "Body Fat-B2",
                                                device!!.name
                                            ) || !TextUtils.isEmpty(device!!.name) && device!!.name!!.contains(
                                                "lnv_11"
                                            ) || TextUtils.equals(
                                                "GOQii Contour",
                                                device!!.name
                                            ) || TextUtils.equals(
                                                "GOQii Essential",
                                                device!!.name
                                            ) || TextUtils.equals(
                                                "GOQii balance",
                                                device!!.name
                                            ))
                                        sendCommand(SYNC_SYSTEM_CLOCK)
                                    }
                                }
                                connectionCallback?.onData(cmd, data)
                            } catch (ex: Exception) {
                                Log.e("parseError", ex.stackTraceToString())
                            }
                        }, {
                            connectionCallback?.onConnectionError(it as BleException)
                        })
                },
                { throwable ->
                    connectionCallback?.onConnectionError(throwable as BleException)
                }
            )

    private fun getConnStateSub(): Disposable =
        device!!.observeConnectionStateChanges()
            .subscribe(
                { state ->
                    when (state) {
                        RxBleConnection.RxBleConnectionState.CONNECTED -> {
                            if (TextUtils.equals("Body Fat-B16" as CharSequence, device!!.name as CharSequence?))
                                sendCommand(SYNC_SYSTEM_CLOCK)
                        }
                    }
                    connectionCallback?.onConnectionState(state)
                },
                { throwable ->
                    connectionCallback?.onConnectionError(throwable as BleException)
                }
            )

    interface StateCallback {
        fun onError(msg: String)
        fun onLocationPermRequired()
        fun onLocationRequired()
        fun onBluetoothRequired()
        fun onReady()
    }

    interface SearchCallback {
        fun onDeviceFound(rxBleDevice: RxBleDevice)
    }

    interface ConnectionCallback {
        fun onConnectionError(error: BleException)
        fun onConnectionState(state: RxBleConnection.RxBleConnectionState)
        fun onSendSuccess(cmd: Commands.AppCommand, data: Any?)
        fun onData(cmd: Commands.DeviceCommand, data: Any)
    }
}