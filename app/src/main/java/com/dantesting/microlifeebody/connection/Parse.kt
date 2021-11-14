package com.dantesting.microlifeebody.connection

import com.dantesting.microlifeebody.connection.Commands.DeviceCommand.*
import com.dantesting.microlifeebody.connection.Commands.DeviceCommand
import com.dantesting.microlifeebody.connection.Commands.DeviceResponce
import java.util.*
import kotlin.math.roundToInt

class Parse {

    companion object {

        val HEART_CHECK_BYTES = byteArrayOf(-85, 1, -80)
        val HISTORY_DATA_ACK_BYTES = byteArrayOf(-85, 2, -101, 1)
        val MEASURE_DATA_ACK_BYTES = byteArrayOf(-85, 2, -93, 0)
        val DELETE_ALL_USER_INFO_BYTES = byteArrayOf(-85, 2, -95, 0)
        val VERSION_BYTES = byteArrayOf(-85, 1, -100)
        private val DEVICE_ADDRESS_BYTES = byteArrayOf(22, 21, 20, 19, 18, 17)
        val SLEEP_DISCONNECT_TIME_BYTES = byteArrayOf(-85, 3, -94, -6, 0)
        val SYSTEM_CLOCK_BYTES: ByteArray
            get() {
                val cal = Calendar.getInstance()
                val year = cal[1]
                val month = cal[2] + 1
                val date = cal[5]
                val hour = cal[11]
                val minute = cal[12]
                val second = cal[13]
                var week = cal[7]
                week = if (week == 1) 7 else week - 1
                val yearLowHex = year and 255
                val yearHeightHex = year shr 8 and 255
                val bytes = byteArrayOf(
                    -85,
                    9,
                    -104,
                    yearLowHex.toByte(),
                    yearHeightHex.toByte(),
                    month.toByte(),
                    date.toByte(),
                    hour.toByte(),
                    minute.toByte(),
                    second.toByte(),
                    week.toByte()
                )
                return bytes
            }

        fun parseResponse(cmd: DeviceCommand, data: ByteArray): DeviceResponce =
            DeviceResponce(
                cmd,
                when (cmd) {
                    WAKE,
                    SLEEP,
                    LOW_POWER,
                    OTA_UPGRADE_READY,
                    HISTORY_DOWNLOAD_DONE,
                    USER_INFO_SETTING_SUCCEEDED,
                    RECEIVE_MEASURE_DATA_FIRST -> Any()
                    SCALE_UNIT_CHANGE ->
                        if (data[3].toInt() == 1) "kg" else "lb"
                    RECEIVE_TIME ->
                        Date(
                            buildIntFromBytes(data[4], data[3]),
                            data[5].toInt() and 255,
                            data[6].toInt() and 255,
                            data[7].toInt() and 255,
                            data[8].toInt() and 255,
                            data[9].toInt() and 255
                        )
                    RECEIVE_SCALE_VERSION ->
                        Commands.ScaleInfo(
                            (data[4].toInt() and 255) shl 8 or (data[3].toInt() and 255),
                            (data[6].toInt() and 255) shl 8 or (data[5].toInt() and 255),
                            (data[8].toInt() and 255) shl 8 or (data[7].toInt() and 255),
                            (data[10].toInt() and 255) shl 8 or (data[9].toInt() and 255)
                        )
                    UPGRADE_RESULT ->
                        Commands.UpgradeResult(
                            data[3].toInt() and 255,
                            data[4].toInt() and 255
                        )
                    RECEIVE_MEASURE_DATA ->
                        parseMeasureResult(data)
                    RECEIVE_HISTORY_MEASUREMENT_RESULT ->
                        parseOffMeasureResult(data)
                }
            )

        fun buildScaleUserData(userInfo: Commands.ScaleUserInfo): ByteArray {
            var btIdArr: Array<String>? = null
            val btId: String? = toBtId(userInfo.userId)
            if (btId != null) {
                btIdArr = btId.split(":").toTypedArray()
            }
            if (btIdArr == null || btIdArr.size != 7) {
                btIdArr = arrayOf("0", "0", "0", "0", "0", "0", "0")
            }
            var sexAndAge: Int = userInfo.age
            if (userInfo.sex == 1) {
                sexAndAge = sexAndAge or 128
            }
            val weightInt = (userInfo.weight * 10.0f).toInt()
            val weight1 = weightInt and 255
            val weight2 = weightInt shr 8 and 255
            var resistance1 = 255
            var resistance2 = 255
            if (userInfo.impedance in 200..1500) {
                resistance1 = userInfo.impedance and 255
                resistance2 = userInfo.impedance shr 8 and 255
            }
            var height: Int = userInfo.height
            if (userInfo.height < 100) {
                height = 100
            } else if (userInfo.height > 220) {
                height = 220
            }
            val heightByte = height.toByte()
            var roleTypeByte: Byte = 1
            if (userInfo.roleType == 1) {
                roleTypeByte = 2
            }
            return byteArrayOf(
                -85, 14, -103,
                btIdArr[0].toInt(16).toByte(),
                btIdArr[1].toInt(16).toByte(),
                btIdArr[2].toInt(16).toByte(),
                btIdArr[3].toInt(16).toByte(),
                btIdArr[4].toInt(16).toByte(),
                btIdArr[5].toInt(16).toByte(),
                btIdArr[6].toInt(16).toByte(),
                sexAndAge.toByte(), heightByte, roleTypeByte,
                weight1.toByte(), weight2.toByte(), resistance1.toByte(), resistance2.toByte()
            )
        }

        fun getHistoryData(uid2: String?): ByteArray {
            val uid: String? = toBtId(uid2)
            var uidArr = uid?.split(":")?.toTypedArray()
            if (uidArr?.size != 7) {
                uidArr = arrayOf("0", "0", "0", "0", "0", "0", "0")
            }
            return byteArrayOf(
                -85, 7, -101,
                uidArr[0].toInt(16).toByte(),
                uidArr[1].toInt(16).toByte(),
                uidArr[2].toInt(16).toByte(),
                uidArr[3].toInt(16).toByte(),
                uidArr[4].toInt(16).toByte(),
                uidArr[5].toInt(16).toByte(),
                uidArr[6].toInt(16).toByte(),
                0
            )
        }

        fun encrypt(data: ByteArray): ByteArray {
            if (data[0].toInt() and 255 == 171) {
                var i = 3
                var j = 0
                while (i < data.size) {
                    data[i] = (data[i].toInt() xor DEVICE_ADDRESS_BYTES[j%6].toInt()).toByte()
                    ++i
                    ++j
                }
            }
            return data
        }

        private fun toBtId(s: String?): String? {
            return if (s != null && s.isNotEmpty()) {
                val sb = StringBuilder(s)
                val remainder = s.length % 7
                var newStr = ""
                var multiple: Int
                if (remainder > 0) {
                    multiple = 0
                    while (multiple < 7 - remainder) {
                        sb.append("a")
                        ++multiple
                    }
                    newStr = sb.toString()
                } else {
                    newStr = s
                }
                sb.setLength(0)
                multiple = newStr.length / 7
                var i = 0
                while (i * multiple < newStr.length) {
                    val singleByte = newStr.substring(i * multiple, (i + 1) * multiple)
                    try {
                        val aaa = singleByte.toInt(16)
                        if (aaa > 255) {
                            return null
                        }
                    } catch (var8: Exception) {
                        return null
                    }
                    sb.append(singleByte).append(":")
                    ++i
                }
                if (sb.length > 1) {
                    sb.deleteCharAt(sb.length - 1)
                }
                sb.toString()
            } else {
                null
            }
        }

        private fun parseMeasureResult(data: ByteArray): Commands.Measurement {
            val year: Int = buildIntFromBytes(data[8], data[7])
            val month: Int = data[9].toInt() and 255
            val day: Int = data[10].toInt() and 255
            val hour: Int = data[11].toInt() and 255
            val minute: Int = data[12].toInt() and 255
            val second: Int = data[13].toInt() and 255
            val weight = buildIntFromBytes(data[16], data[15]).toFloat() / 10.0f
            val fat = buildIntFromBytes(data[18], data[17]).toFloat() / 10.0f
            val resistance: Int = buildIntFromBytes(data[20], data[19])
            val bpm: Int = data[21].toInt() and 255
            val unit = data[22].toInt()
            return Commands.Measurement(
                weight, fat, bpm, Date(year, month, day, hour, minute, second),
                if (unit == 1) "kg" else "lb", resistance
            )
        }

        private fun parseOffMeasureResult(data: ByteArray): Commands.Measurement {
            if (data.size >= 18) {
                val weight = buildIntFromBytes(data[5], data[4]).toFloat() / 10.0f
                val fat = buildIntFromBytes(data[7], data[6]).toFloat() / 10.0f
                val resistance: Int = buildIntFromBytes(data[17], data[16])
                val year: Int = buildIntFromBytes(data[9], data[8])
                val month: Int = data[10].toInt() and 255
                val day: Int = data[11].toInt() and 255
                val hour: Int = data[12].toInt() and 255
                val minute: Int = data[13].toInt() and 255
                val second: Int = data[14].toInt() and 255
                return Commands.Measurement(
                    weight, fat, 0, Date(year, month, day, hour, minute, second),
                    "kg", resistance
                )
            } else {
                return Commands.Measurement(0f,0f,0, Date(),"",0)
            }
        }

        private fun getBmi(weight: Float, userHeight: Float): Float {
            val bmi = weight / (userHeight * userHeight / 10000.0f)
            return (bmi * 10.0f).roundToInt().toFloat() / 10.0f
        }

        private fun buildIntFromBytes(high: Byte, low: Byte): Int =
            high.toInt() and 255 shl 8 or low.toInt() and 255
    }
}