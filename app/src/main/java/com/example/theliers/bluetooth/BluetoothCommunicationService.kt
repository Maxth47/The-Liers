package com.example.theliers.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log

import java.io.*
import java.nio.charset.StandardCharsets


const val MESSAGE_READ = 1
private const val TAG = "MY_APP_DEBUG_TAG"

class MyBluetoothService(private val handler: Handler) {
    private lateinit var mThread: ConnectedThread

    fun initCommunication(socket: BluetoothSocket) {
        println("service start:::::::::")
        println(socket.isConnected)
        mThread = ConnectedThread(socket)
        mThread.start()
    }

    fun sendInfo(info: String) {
        mThread.write(info.toByteArray( StandardCharsets.UTF_8))
    }

    fun stopConnect() {
        mThread.cancel()
    }

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        // mmBuffer store for the stream
        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream

        override fun run() {

            var numBytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                val mmBuffer = ByteArray(DEFAULT_BUFFER_SIZE)
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    break
                }
                val strWithoutZeros = String(mmBuffer, StandardCharsets.UTF_8).replace(0.toChar().toString(), "")

                // Send the obtained bytes to the UI activity.
                val readMsg = handler.obtainMessage(
                    MESSAGE_READ, numBytes, -1,
                    strWithoutZeros)
                println("  -  ")
                readMsg.what = 1

                println(readMsg)
                readMsg.sendToTarget()

            }

        }

        // Call this from the main activity to send data to the remote device.
        fun write(bytes: ByteArray) {

            try {
                println("------------------------------------------------------------------")
                println("------------------------------------------------------------------")
                println(bytes.toString(StandardCharsets.UTF_8))
                println(mmSocket.isConnected)
                println(mmSocket)

                mmOutStream.write(bytes)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when sending data", e)

                // Send a failure message back to the activity.
                return
            }

        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }
}
