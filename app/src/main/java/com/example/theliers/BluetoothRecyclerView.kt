package com.example.theliers

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.theliers.BluetoothHandler.bluetoothAdaptor
import kotlinx.android.synthetic.main.bluetooth_item.view.*
import java.io.IOException


class BluetoothItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class ViewAdapter(val handler: Handler) : RecyclerView.Adapter<BluetoothItemViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothItemViewHolder {
            return BluetoothItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.bluetooth_item,
                    parent,
                    false
                ) as ConstraintLayout
            )
        }

        override fun getItemCount() = BluetoothDevices.list.size

        override fun onBindViewHolder(holder: BluetoothItemViewHolder, position: Int) {
            val item = BluetoothDevices.list[position]
            println("xxxxxxxxxxx")
            if(item.name != null ){
                holder.itemView.deviceName.text = item.name
            }
            holder.itemView.deviceAddress.text = item.address

            holder.itemView.setOnClickListener {
                println("----------------cnnecting")
                val connectAsClient = ConnectThread(item, handler)
                connectAsClient.start()
            }
        }

        private inner class ConnectThread(device: BluetoothDevice, handler: Handler) : Thread() {

            private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
                device.createRfcommSocketToServiceRecord(MY_UUID)
            }

            override fun run() {
                // Cancel discovery because it otherwise slows down the connection.
                bluetoothAdaptor?.cancelDiscovery()

                mmSocket?.use { socket ->
                    // Connect to the remote device through the socket. This call blocks
                    // until it succeeds or throws an exception.
                    socket.connect()

                    // The connection attempt succeeded. Perform work associated with
                    // the connection in a separate thread.
                    // false is for client
                    BluetoothHandler.manageMyConnectedSocket(socket, false, handler)
                    //val intent = Intent(this,MainActivity::class.java)
                }
            }

            /*// Closes the client socket and causes the thread to finish.
            fun cancel() {
                try {
                    mmSocket?.close()
                } catch (e: IOException) {
                    Log.e("client socket", "Could not close the client socket", e)
                }
            }*/
        }
    }
}