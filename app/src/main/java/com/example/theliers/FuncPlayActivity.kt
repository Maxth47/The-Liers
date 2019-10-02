package com.example.theliers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import kotlinx.android.synthetic.main.activity_func_play.*

class FuncPlayActivity : AppCompatActivity() {

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(inputMessage: Message) {
            println("does this receive?")
            if (inputMessage.what == 0) {
                val byteArray = inputMessage.obj as ByteArray
                play_text.text = byteArray.toString(Charsets.UTF_8)
                println("it works somewhat")
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_func_play)
        val bluetoothService = BluetoothHandler.startBluetoothComm(mHandler)
        var i = 0
        testButton.setOnClickListener {
            bluetoothService.sendInfo("$i $i $i $i $i $i $i $i")
            i++
        }

    }
}
