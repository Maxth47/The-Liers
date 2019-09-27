package com.example.theliers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

object BluetoothHandler {
    val bluetoothAdaptor: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun checkBluetoothAvail(): Boolean {
        return bluetoothAdaptor != null
    }

    fun checkBluetoothEnabled(): Boolean {
        return bluetoothAdaptor!!.isEnabled
    }

    fun startBluetoothDiscovery() {
        //bluetoothAdaptor!!.cancelDiscovery()
        println(bluetoothAdaptor!!.startDiscovery())
        var index = 0
        while (bluetoothAdaptor.isDiscovering) {
            println("-------")
            println(index)
            index++
        }
    }

    fun cancelBluetoothDiscovery() {
        bluetoothAdaptor!!.cancelDiscovery()
    }

    fun checkBluetoothBond() {
        val pairedDevice: Set<BluetoothDevice>? = bluetoothAdaptor?.bondedDevices
        pairedDevice?.forEach {device ->
            val deviceName = device.name
            val deviceHardwarAddress = device.address //MAC Address
            println("---------------------------------------")
            println("$deviceName: $deviceHardwarAddress")
        }
    }


}