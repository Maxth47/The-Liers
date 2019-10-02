package com.example.theliers

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bluetooth_search.*
import java.io.IOException
import java.util.*

const val REQUEST_ENABLE_BT = 1001
const val NAME = "Liars Game"

val MY_UUID: UUID = UUID.fromString("085ec7de-e20e-432f-a929-4667b495eeef")
val MY_CLIENT_UUID: UUID = UUID.fromString("95463e87-d97c-4193-acac-abbcaf57696c")

class MainActivity : AppCompatActivity() {
    private val handler: Handler = object : Handler(Looper.getMainLooper()){

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_search)
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),1)
        requestDiscovery()
        //init handler


        //set up buttons
        hostButton.setOnClickListener {
            //BluetoothHandler.cancelBluetoothDiscovery()
            //BluetoothHandler.checkBluetoothBond()
            val  acceptThread = AcceptThread( this)
            acceptThread.start()
        }

        startButton.setOnClickListener {
            //start discovery
            println("xxxxx")
            BluetoothHandler.startBluetoothDiscovery()
        }

        //------------------------------------------
        // init bluetooth
        initBluetooth()

        // register this activity as receiver when discovery
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)

        registerReceiver(receiver, filter)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BluetoothItemViewHolder.ViewAdapter(this)
        recyclerView.adapter!!.notifyDataSetChanged()

    }
    private fun requestDiscovery() {
        println("yyyyyyyyyyyyyyyyyyyy")
        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        }
        startActivity(discoverableIntent)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // to check when user confirmed or cancelled bluetooth enabling
        when (requestCode) {
            REQUEST_ENABLE_BT -> {
                println("xxxxxxx")
                println("return from bluetooth request")
                when(resultCode) {
                    Activity.RESULT_OK -> {
                        println("ok")
                        //assuming the user ok'ed bluetooth
                        BluetoothHandler.checkBluetoothBond()
                    }
                    Activity.RESULT_CANCELED -> println("cancelled")
                }
            }
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            println("on receive------------------------------------")
            println(intent.action)
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    println("-------------------------------------------")
                    println("found device")
                    println("$deviceName $deviceHardwareAddress")
                    if (!BluetoothDevices.list.contains(device) && device.type != BluetoothDevice.DEVICE_TYPE_LE) {
                        BluetoothDevices.list.add(device)
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    // init bluetooth on from inside the app
    private fun initBluetooth() {
        // checking for bluetooth avail and turn on bluetooth if not on
        if (BluetoothHandler.checkBluetoothAvail()) {
            println("===============================")
            println("bluetooth is available")
            // activate bluetooth if bluetooth is not turned on
            if(!BluetoothHandler.checkBluetoothEnabled()) {
                println("bluetooth is not turned on")
                turnBluetoothOn()
            }
        }
    }
    // go to intent to turn  bluetooth on
    private fun turnBluetoothOn() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }
    override fun onDestroy() {
        super.onDestroy()
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver)
    }

    private class AcceptThread(val mainActivity: MainActivity) : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            BluetoothHandler.bluetoothAdaptor?.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID)
        }

        override fun run() {
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    Log.e("socket failed", "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    BluetoothHandler.manageMyConnectedSocket(it, true, mainActivity)
                    // true is for host
                    println("closes server socket")
                    mmServerSocket?.close()
                    shouldLoop = false
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                Log.e("socket: ", "Could not close the connect socket", e)
            }
        }
    }

    fun goToPlay() {
        val intent = Intent(this, FuncPlayActivity::class.java)
        startActivity(intent)
    }
}
