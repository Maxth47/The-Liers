package com.example.theliers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.theliers.bluetooth.BluetoothHandler
import kotlinx.android.synthetic.main.activity_func_play.*
import kotlinx.android.synthetic.main.activity_play.*

class PlayActivity : AppCompatActivity() {
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(inputMessage: Message) {
            println("does this receive?")
            if (inputMessage.what == 1) {
                val byteArray = inputMessage.obj as ByteArray
                play_text.text = byteArray.toString(Charsets.UTF_8)
                println("it works somewhat")
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        val bluetoothService = BluetoothHandler.startBluetoothComm(mHandler)

        /*testButton.setOnClickListener {
            bluetoothService.sendInfo("$i $i $i $i $i $i $i $i")
            i++
        }*/


        supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_Playing()).commit()

        ibtn_playing.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_Playing()).commit()
        }

        ibtn_history.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_History()).commit()
        }

        ibtn_rules.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_Rules()).commit()
        }
    }

}
