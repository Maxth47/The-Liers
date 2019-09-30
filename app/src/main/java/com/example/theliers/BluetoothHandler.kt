package com.example.theliers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler

object BluetoothHandler {
    private lateinit var handler: Handler
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

    fun manageMyConnectedSocket(socket: BluetoothSocket, type: Boolean, mhandler: Handler) {
        handler = mhandler
        println("---------------------------------------------------")
        println("---------------------------------------------------")
        println(socket.connectionType)
        println(socket.isConnected)

        // check connection type
        if (type) {
            println("connect as host")
        } else {
            println("connect as client")
        }
        // start direct communication
        val connectionService = MyBluetoothService(handler, type)
        connectionService.initCommunication(socket)
    }

    fun getCurrentHandler(): Handler {
        return handler
    }

}