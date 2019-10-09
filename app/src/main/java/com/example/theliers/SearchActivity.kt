package com.example.theliers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theliers.bluetooth.BluetoothDevices
import com.example.theliers.bluetooth.BluetoothHandler
import com.example.theliers.bluetooth.BluetoothItemViewHolder
import kotlinx.android.synthetic.main.activity_bluetooth_search.*
import java.util.*

const val REQUEST_ENABLE_BT = 1001
const val NAME = "Liars Game"

val MY_UUID: UUID = UUID.fromString("085ec7de-e20e-432f-a929-4667b495eeef")

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_search)


        //set up buttons
        cancelSearchButton.setOnClickListener {
            finish()
        }
        //------------------------------------------
        //start discovery

        // register this activity as receiver when discovery
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)

        registerReceiver(receiver, filter)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BluetoothItemViewHolder.ViewAdapter(this)
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

    override fun onDestroy() {
        super.onDestroy()
        // Don't forget to unregister the ACTION_FOUND receiver.
        BluetoothHandler.cancelBluetoothDiscovery()
        unregisterReceiver(receiver)
    }

    fun goToPlay() {
        val intent = Intent(this, PlayActivity::class.java)
        startActivity(intent)
    }
}
