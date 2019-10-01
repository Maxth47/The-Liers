package com.example.theliers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import kotlinx.android.synthetic.main.activity_play.*

class PlayActivity : AppCompatActivity() {

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(inputMessage: Message) {
            //text_network.text = "does it work"
            if (inputMessage.what == 0) {
                play_text.text = inputMessage.obj.toString()
                println("it works somewhat")
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        val bluetoothService = BluetoothHandler.startBluetoothComm(mHandler)

        testButton.setOnClickListener {
            bluetoothService.sendInfo("sending info over")
        }

    }
}
