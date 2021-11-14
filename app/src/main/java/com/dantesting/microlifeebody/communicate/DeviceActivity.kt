package com.dantesting.microlifeebody.communicate

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dantesting.microlifeebody.R
import com.dantesting.microlifeebody.connection.Client
import com.dantesting.microlifeebody.connection.Commands
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.exceptions.BleException
import java.util.*
import kotlin.collections.ArrayList

class DeviceActivity : AppCompatActivity(), Client.ConnectionCallback,
            Client.StateCallback{

    companion object {
        val MAC_ADDRESS = "MAC_ADDRESS"
        val NAME = "NAME"
    }

    private val TAG = this.javaClass.name

    private lateinit var recycler: RecyclerView
    private val logArray: ArrayList<LogAdapter.Log> = ArrayList()
    private val logAdapter: LogAdapter = LogAdapter(logArray)

    private lateinit var historyBtn: Button
    private lateinit var versionBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var sendBtn: Button

    private lateinit var mac: String

    private lateinit var weightClient: Client
    private lateinit var bleClient: Client

    private var userInfo: Commands.ScaleUserInfo =
        Commands.ScaleUserInfo(
            "00000000000",
            0,
            100,
            0,
            18,
            20f
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_communication)

        weightClient = Client(this, this, connectionCallback = this)
        bleClient = Client(this)
        if (intent.getStringExtra(NAME) != null && intent.getStringExtra(MAC_ADDRESS) != null) {
            title = intent.getStringExtra(NAME)
            mac = intent.getStringExtra(MAC_ADDRESS)!!
        }
        else {
            throw Exception("intent without mac or name")
        }

        setUserInfo()
        initLayout()

        weightClient.connect(mac, userInfo)
    }

    private fun initLayout() {

        historyBtn = findViewById(R.id.get_history)
        versionBtn = findViewById(R.id.get_version)
        deleteBtn = findViewById(R.id.delete_users)
        sendBtn = findViewById(R.id.send_user_info)

        historyBtn.setOnClickListener {
            weightClient.sendCommand(Commands.AppCommand.GET_HISTORY_DATA)
        }

        versionBtn.setOnClickListener {
            weightClient.sendCommand(Commands.AppCommand.GET_VERSION)
        }

        deleteBtn.setOnClickListener {
            weightClient.sendCommand(Commands.AppCommand.DELETE_ALL_USER_INFO)
        }

        sendBtn.setOnClickListener {
            weightClient.sendCommand(Commands.AppCommand.SEND_USER_INFO, userInfo)
        }

        recycler = findViewById(R.id.logRecycler)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = logAdapter
        recycler.itemAnimator = DefaultItemAnimator()

        val dividerItemDecoration = DividerItemDecoration(recycler.context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(baseContext,
                R.drawable.list_divider)!!)
        recycler.addItemDecoration(dividerItemDecoration)
    }

    override fun onDestroy() {
        weightClient.disconnect()
        super.onDestroy()
    }

    override fun onConnectionError(error: BleException) {
        error.printStackTrace()
    }

    override fun onConnectionState(state: RxBleConnection.RxBleConnectionState) {
        when (state) {
            RxBleConnection.RxBleConnectionState.CONNECTING -> {
                addLog("CONNECTING", "")
            }
            RxBleConnection.RxBleConnectionState.CONNECTED -> {
                addLog("CONNECTED", "")
            }
            RxBleConnection.RxBleConnectionState.DISCONNECTED -> {
                addLog("DISCONNECTED", "")
                finish()
            }
            RxBleConnection.RxBleConnectionState.DISCONNECTING -> {
                addLog("DISCONNECTING", "")
            }
        }
    }

    override fun onSendSuccess(cmd: Commands.AppCommand, data: Any?) {
        // возникает при успешном отправлении команды
        if (data != null && (data is Date || data is Commands.Measurement || data is String
                    || data is Commands.UpgradeResult || data is Commands.ScaleInfo)) {
            addLog(cmd.name, "WRITE data = $data")
        }
        else
            addLog(cmd.name, "WRITE")
    }

    override fun onData(cmd: Commands.DeviceCommand, data: Any) {
        // событие получения данных с устройства
        if (data is Date || data is Commands.Measurement || data is String
            || data is Commands.UpgradeResult || data is Commands.ScaleInfo) {
            addLog(cmd.name, "READ data = $data")
        }
        else
            addLog(cmd.name, "READ")
    }

    override fun onError(msg: String) {
        addLog("Error", msg)
    }

    override fun onLocationPermRequired() {
        Log.e(TAG, "onLocationPermRequired()")
    }

    override fun onLocationRequired() {
        Log.e(TAG, "onLocationRequired()")
    }

    override fun onBluetoothRequired() {
        Log.e(TAG, "onBluetoothRequired()")
    }

    override fun onReady() {
        weightClient.connect(mac, userInfo)
    }

    private fun addLog(log: String, data: String) {
        runOnUiThread {
            logArray.add(LogAdapter.Log(log, data))
            logAdapter.notifyItemInserted(logArray.lastIndex)
            if ((recycler.layoutManager as LinearLayoutManager)
                    .findLastCompletelyVisibleItemPosition()
                == logArray.lastIndex - 1)
                recycler.scrollToPosition(logArray.lastIndex)
        }
    }

    private fun makeToast(msg: String) {
        runOnUiThread {
            Toast.makeText(
                applicationContext,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun setUserInfo() {
        val ran = Random()
        val name = IntArray(ran.nextInt(7) + 3)
        val nameBuilder = StringBuilder()
        for (i in name.indices) {
            name[i] = (Math.random() * 26 + 65).toInt()
            nameBuilder.append(name[i].toChar())
        }
        val Id = IntArray(11)
        val IdBuilder = StringBuilder()
        for (i in name.indices) {
            Id[i] = (Math.random() * 10 + 48).toInt()
            IdBuilder.append(Id[i].toChar())
        }
        userInfo.userId = IdBuilder.toString()
        userInfo.sex = ran.nextInt(2)
        userInfo.age = 18 + ran.nextInt(62)
        userInfo.weight = (20 + ran.nextInt(130)).toFloat()
        userInfo.height = 100 + ran.nextInt(120)
        userInfo.roleType = ran.nextInt(2)
    }
}