package com.example.theliers

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import android.util.Log

import java.io.*


const val MESSAGE_READ = 1
const val MESSAGE_TOAST = 2
const val MESSAGE_WRITE = 3
private const val TAG = "MY_APP_DEBUG_TAG"

class MyBluetoothService(private val handler: Handler, val type: Boolean) {
    private lateinit var mThread: ConnectedThread

    fun initCommunication(socket: BluetoothSocket) {
        println("service start:::::::::")
        println(socket.isConnected)
        mThread = ConnectedThread(socket)
        mThread.start()
    }

    fun sendInfo(info: String) {
        mThread.write(info.toByteArray( Charsets.UTF_8))
    }

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream
        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream

        override fun run() {

            var numBytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    break
                }
                println(mmBuffer.toString( Charsets.UTF_8))
                // Send the obtained bytes to the UI activity.
                val readMsg = handler.obtainMessage(
                    MESSAGE_READ, numBytes, -1,
                    mmBuffer)
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
                println(bytes)
                println(mmSocket.isConnected)
                println(mmSocket)
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when sending data", e)

                // Send a failure message back to the activity.
                val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }
                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return
            }

            // Share the sent message with the UI activity.
            val writtenMsg = handler.obtainMessage(
                MESSAGE_WRITE, -1, -1, mmBuffer)
            writtenMsg.sendToTarget()
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
