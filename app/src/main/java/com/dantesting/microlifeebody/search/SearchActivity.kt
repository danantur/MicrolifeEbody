package com.dantesting.microlifeebody.search

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dantesting.microlifeebody.communicate.DeviceActivity
import com.dantesting.microlifeebody.R
import com.dantesting.microlifeebody.connection.Client
import com.polidea.rxandroidble2.RxBleDevice
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity(), Client.StateCallback,
    Client.SearchCallback {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: SearchAdapter
    private val devices: ArrayList<RxBleDevice> = ArrayList()

    private lateinit var client: Client

    private var locationDenied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLayout()

        client = Client(this, this, this)
    }

    override fun onResume() {
        locationDenied = false
        client.checkState()
        super.onResume()
    }

    override fun onPause() {
        client.endSearching()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 100)
            if (permissions[0] == Manifest.permission.ACCESS_COARSE_LOCATION)
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    makeToast("Для работы приложения требуется разрешение на получение геоданных!")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initLayout() {

        setContentView(R.layout.activity_search)

        recycler = findViewById(R.id.recyclerView)

        adapter = SearchAdapter(devices, object : SearchAdapter.OnSearchItemClick {
            override fun onClick(device: RxBleDevice) {
                val intent = Intent(applicationContext, DeviceActivity::class.java)
                intent.putExtra(DeviceActivity.MAC_ADDRESS, device.macAddress)
                intent.putExtra(DeviceActivity.NAME, device.name)
                startActivity(intent)
            }
        })

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        recycler.itemAnimator = DefaultItemAnimator()

        val dividerItemDecoration = DividerItemDecoration(recycler.context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(baseContext,
                R.drawable.list_divider)!!)
        recycler.addItemDecoration(dividerItemDecoration)
    }

    private val locationCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        client.checkState()
    }

    private val bluetoothCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK)
            makeToast("Для работы приложения требуется включить Bluetooth!")
    }

    override fun onReady() {
        client.startSearching()
    }

    override fun onDeviceFound(rxBleDevice: RxBleDevice) {
        if (!devices.contains(rxBleDevice)) {
            devices.add(rxBleDevice)
            adapter.notifyItemInserted(devices.lastIndex)
        }
    }

    override fun onError(msg: String) {
        makeToast("Ошибка, $msg")
    }

    override fun onLocationPermRequired() {
        if (Build.VERSION.SDK_INT >= 31) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_PRIVILEGED),
                100
            )
        }
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_PRIVILEGED),
                100
            )
        }
    }

    override fun onLocationRequired() {
        if (locationDenied) {
            makeToast("Для работы приложения требуется включить передачу геоданных!")
            return
        }
        locationDenied = true
        locationCallback.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    override fun onBluetoothRequired() {
        bluetoothCallback.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
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

}