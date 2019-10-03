package com.example.theliers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import kotlinx.android.synthetic.main.activity_func_play.*

class FuncPlayActivity : AppCompatActivity() {
 private val gibberish = "%^^&*(*&^^%^^&&*&(!@#&^^@#(&^^*"

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(inputMessage: Message) {
            println("does this receive?")
            if (inputMessage.what == 1) {
                val receivedString = inputMessage.obj as String
                val order = receivedString.split(gibberish)
                println("decode ms")
                if(order.size == 3) {
                    when (order[0]) {
                        "initGame" -> {
                            gameMaster.opponentValue = order[1].toInt()
                            gameMaster.decideTurn()
                         }
                    }
                }
                println("it works somewhat")
            }
        }
    }

    private val bluetoothService = BluetoothHandler.startBluetoothComm(mHandler)
    private val gameMaster = GameMaster(bluetoothService, this)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_func_play)

        startGameButton.setOnClickListener {
            gameMaster.initGame()
        }

    }

    private inner class GameMaster(val bluetoothService: MyBluetoothService, funcPlayActivity: FuncPlayActivity) {
        // Game Master class is to help control the flow of the game

        //first roll value
        var yourValue = -2
        var opponentValue = -2

        //init game by choosing a random value to decide who goes first
        fun initGame() {
            yourValue = (1..6).random()
            bluetoothService.sendInfo("initGame$gibberish$yourValue")
            decideTurn()
        }

        // decide between two players who will go first
        fun decideTurn() {
            if(yourValue > 0 && opponentValue > 0) {
                when {
                    yourValue > opponentValue -> {
                        println("you go first")
                    }
                    yourValue < opponentValue -> {
                        println("you go second")
                    }
                    else -> reroll()
                }
            }
        }

        // reroll dice if two players roll the same value
        fun reroll() {
            opponentValue = -2
            yourValue = (1..6).random()
            bluetoothService.sendInfo("initGame$gibberish$yourValue")
            decideTurn()
        }
    }
}
