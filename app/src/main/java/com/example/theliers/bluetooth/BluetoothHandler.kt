package com.example.theliers.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import com.example.theliers.HostActivity
import com.example.theliers.SearchActivity

object BluetoothHandler {

    lateinit var socket: BluetoothSocket
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
        if(bluetoothAdaptor!!.isDiscovering){
            bluetoothAdaptor.cancelDiscovery()
        }
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

    fun manageMyConnectedSocket(mSocket: BluetoothSocket, type: Boolean, searchActivity: SearchActivity?, hostActivity: HostActivity?) {
        socket = mSocket
        println("")
        println("------init-------------------------------------")
        println(socket.connectionType)
        println(socket.isConnected)
        // check connection type
        if (type) {
            println("connect as host")
        } else {
            println("connect as client")
        }
        // start direct communication
        searchActivity?.goToPlay()
        hostActivity?.goToPlay()
        println(socket.isConnected)
    }

    fun startBluetoothComm (handler: Handler): MyBluetoothService {
        val connectionService = MyBluetoothService(
            handler
        )
        println("-------------------------------------------------------")
        println("--------------------starting commute-------------------")
        println(socket.isConnected)
        connectionService.initCommunication(socket)
        return connectionService
    }
}