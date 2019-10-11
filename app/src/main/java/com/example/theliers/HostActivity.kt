package com.example.theliers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.theliers.bluetooth.BluetoothHandler
import kotlinx.android.synthetic.main.activity_host.*
import java.io.IOException

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        val  acceptThread = AcceptThread(this)
        acceptThread.start()
        cancelButton.setOnClickListener {
            acceptThread.cancel()
            finish()
        }

        hostButton.setOnClickListener {
            requestDiscoverable()
        }
    }

    private fun requestDiscoverable() {
        println("yyyyyyyyyyyyyyyyyyyy")
        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        }
        startActivity(discoverableIntent)
    }

    private class AcceptThread(val hostActivity: HostActivity) : Thread() {

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
                    BluetoothHandler.manageMyConnectedSocket(it, true, null, hostActivity)
                    // true is for host
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
        val intent = Intent(this, PlayActivity::class.java)
        startActivity(intent)
    }
}
