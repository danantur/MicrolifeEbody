package com.dantesting.microlifeebody.connection

import android.util.Log
import com.dantesting.microlifeebody.connection.Parse.Companion.DELETE_ALL_USER_INFO_BYTES
import com.dantesting.microlifeebody.connection.Parse.Companion.HEART_CHECK_BYTES
import com.dantesting.microlifeebody.connection.Parse.Companion.HISTORY_DATA_ACK_BYTES
import com.dantesting.microlifeebody.connection.Parse.Companion.MEASURE_DATA_ACK_BYTES
import com.dantesting.microlifeebody.connection.Parse.Companion.SLEEP_DISCONNECT_TIME_BYTES
import com.dantesting.microlifeebody.connection.Parse.Companion.SYSTEM_CLOCK_BYTES
import com.dantesting.microlifeebody.connection.Parse.Companion.VERSION_BYTES
import com.dantesting.microlifeebody.connection.Parse.Companion.buildScaleUserData
import com.dantesting.microlifeebody.connection.Parse.Companion.encrypt
import com.dantesting.microlifeebody.connection.Parse.Companion.getHistoryData
import java.util.*

class Commands {

    companion object {

        val writeUuid: UUID = UUID.fromString("0000faa1-0000-1000-8000-00805f9b34fb")
        val notifyUuid: UUID = UUID.fromString("0000faa2-0000-1000-8000-00805f9b34fb")

        private var lastData: ByteArray = ByteArray(0)
        private var measurement: ByteArray = ByteArray(23)

        fun parseWeightResponse(data: ByteArray): DeviceResponce? {
            if (data[0] != (-115).toByte()) {
                Log.e("parseResponseError", "Неправильный заголовок ${data.toList()}")
            } else if (data.size != (data[1].toInt() and 255) + 3) {
                Log.e("parseResponseError", "Неправильный заголовок ${data.toList()}")
            } else {
                if (data.contentEquals(lastData)) {
                    Log.e("parseResponseError", "Данные копируют прошлые ${data.toList()}")
                } else {
                    lastData = data
                    val cmd = DeviceCommand.values().find {
                        it.code == data[2].toInt() and 255
                    }
                    if (cmd != null) {
                        if (cmd != DeviceCommand.RECEIVE_MEASURE_DATA_FIRST &&
                                cmd != DeviceCommand.RECEIVE_MEASURE_DATA) {
                            return Parse.parseResponse(cmd, data)
                        }
                        else if (cmd == DeviceCommand.RECEIVE_MEASURE_DATA_FIRST) {
                            System.arraycopy(
                                data,
                                3,
                                measurement,
                                0,
                                15
                            )
                        }
                        else {
                            System.arraycopy(
                                data,
                                3,
                                measurement,
                                15,
                                8
                            )
                            return Parse.parseResponse(cmd, measurement)
                        }
                    } else
                        Log.e("parseResponseError", "Такой команды не предусмотрено ${data.toList()}")
                }
            }
            return null
        }

        fun packWeightCommand(cmd: AppCommand, data: Any?): ByteArray =
            encrypt(
                when (cmd) {
                    AppCommand.HEART_SMG -> HEART_CHECK_BYTES
                    AppCommand.GET_HISTORY_DATA -> getHistoryData(data?.toString())
                    AppCommand.HISTORY_DATA_ACK -> HISTORY_DATA_ACK_BYTES
                    AppCommand.SLEEP_DISCONNECT_TIME -> SLEEP_DISCONNECT_TIME_BYTES
                    AppCommand.MEASURE_DATA_ACK -> MEASURE_DATA_ACK_BYTES
                    AppCommand.DELETE_ALL_USER_INFO -> DELETE_ALL_USER_INFO_BYTES
                    AppCommand.SYNC_SYSTEM_CLOCK -> SYSTEM_CLOCK_BYTES
                    AppCommand.SEND_USER_INFO -> buildScaleUserData(data as ScaleUserInfo)
                    AppCommand.GET_VERSION -> VERSION_BYTES
                }
            )

    }

    data class DeviceResponce(val cmd: DeviceCommand, val data: Any)

    data class ScaleInfo(val bleVer: Int, val scaleVer: Int, val coefficientVer: Int, val arithmeticVer: Int)
    data class UpgradeResult(val result: Int, val type: Int)
    data class Measurement(val weight: Float, val fat: Float, val heartRate: Int, val dateTime: Date,
                           val unit: String, val resistance: Int)

    data class ScaleUserInfo(var userId: String, var sex: Int, var height: Int,
                             var roleType: Int, var age: Int, var weight: Float,
                             var impedance: Int = 500)

    /**
     * Describes app commands to device.
     */
    enum class AppCommand
    {
        /**
         * Команда пинга, требуется отправлять спустя 2 секунды после
         * отправления последней команды, чтобы сохранить подключение
         */
        HEART_SMG,
        /**
         * В SDK отправляется каждый раз, когда засыпают весы, но безрезультатно.
         * Возможно работает на весах, сохраняющих историю.
         *
         * @param user  [ScaleUserInfo]
         */
        GET_HISTORY_DATA,
        /**
         * На тестах не отправляется, скорее всего используется
         * для подтверждения получения данных с комманды [GET_HISTORY_DATA]
         */
        HISTORY_DATA_ACK,
        /**
         * В SDK прописана, но не используется
         */
        SLEEP_DISCONNECT_TIME,
        /**
         * Команда для подтверждения получения данных с весов
         */
        MEASURE_DATA_ACK,
        /**
         * Удаляет все данные пользователей с весов (скорее всего
         * работает только на весах с сохраняемой историей)
         */
        DELETE_ALL_USER_INFO,
        /**
         * Синхронизирует время весов с временем телефона
         */
        SYNC_SYSTEM_CLOCK,
        /**
         * Отправляется после каждого [DeviceCommand.WAKE] весов,
         * устанавливает пользователя, на которого будут записываться данные
         *
         * @param user  [ScaleUserInfo]
         */
        SEND_USER_INFO,
        /**
         * Получение информации о устройстве ([ScaleInfo])
         */
        GET_VERSION
    }

    /**
     * Describes device info receiving while connected.
     */
    enum class DeviceCommand(val code: Int)
    {
        WAKE(144),
        SLEEP(145),
        SCALE_UNIT_CHANGE(146),
        RECEIVE_TIME(152),
        RECEIVE_SCALE_VERSION(156),
        RECEIVE_MEASURE_DATA_FIRST(157),
        RECEIVE_MEASURE_DATA(158),
        RECEIVE_HISTORY_MEASUREMENT_RESULT(160),
        UPGRADE_RESULT(162),
        LOW_POWER(164),
        OTA_UPGRADE_READY(167),
        HISTORY_DOWNLOAD_DONE(169),
        USER_INFO_SETTING_SUCCEEDED(176)
    }
}