package com.example.theliers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_func_play.*

class FuncPlayActivity : AppCompatActivity()  {

    private val gibberish = "--!@#$%^)+_-&*--"

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(inputMessage: Message) {
            println("does this receive?")
            if (inputMessage.what == 1) {
                val receivedString = inputMessage.obj as String
                val order = receivedString.split(gibberish)
                println("decode ms")
                if(order.size == 3) {
                    println(order)
                    when (order[0]) {
                        "initGame" -> {
                            gameMaster.opponentValue = order[1].toInt()
                            gameMaster.decideTurn()
                        }

                        "guess" -> {
                            getOpponentRoll(order[1].toInt(), order[2].toInt())
                            takeTurn()
                        }

                        "call" -> {

                        }

                        "return call" -> {

                        }
                    }
                }
                println("it works somewhat")
            }
        }
    }

    private val bluetoothService = BluetoothHandler.startBluetoothComm(mHandler)
    private val gameMaster = GameMaster(bluetoothService, this)

    //array list of int for spinner
    var totalList = listOf(1,2,3,4,5,6,7,8,9,10)
    var dicelist = listOf(1,2,3,4,5,6)

    //int to remember last opponent bid
    var totalBid = 0
    var typeBid = 0

    //string to remember players choice
    var opponentBid = ""
    var yourBid = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_func_play)

        //set up buttons for game
        guessButton.isEnabled = false
        callButton.isEnabled = false
        totalSpinner.isEnabled = false
        typeSpinner.isEnabled = false

        //set up spinner
        setUpSpinner()

        //start button to start game
        startGameButton.setOnClickListener {
            gameMaster.initGame()
            startGameButton.isEnabled = false
        }

        //guess button to send bid
        guessButton.setOnClickListener {
            guess()
        }

        callButton.setOnClickListener {
            gameMaster.call()
        }

    }

    //init gameplay
    fun gameInit(playOder: String) {
        when(playOder) {
            "first" -> {
                turnView.text = "You go first"
                gameMaster.rollDice(4)
                //roll from 0 to 4 aka 5 dice
                takeFirstTurn()
                startGameButton.isEnabled = false
            }

            "second" -> {
                turnView.text = "You go second"
                gameMaster.rollDice(4)
                //roll from 0 to 4 aka 5 dice
                passTurn()
                startGameButton.isEnabled = false
            }
        }

    }

    //display player dice
    fun displayDice(diceArray: String) {
        yourRoll.text = diceArray
    }

    //take your turn
    fun takeFirstTurn() {
        turnView.text = "Your go first"
        guessButton.isEnabled = true
        totalSpinner.isEnabled = true
        typeSpinner.isEnabled = true
    }

    //take your turn
    fun takeTurn() {
        turnView.text = "Your turn"
        guessButton.isEnabled = true
        callButton.isEnabled = true
        totalSpinner.isEnabled = true
        typeSpinner.isEnabled = true
    }

    //pass your turn
    fun passTurn(){
        turnView.text = "Opponent turn"
        guessButton.isEnabled = false
        callButton.isEnabled = false
        totalSpinner.isEnabled = false
        typeSpinner.isEnabled = false
    }

    //get opponent roll
    fun getOpponentRoll(total: Int, dice: Int) {
        opponentBid += "$total x $dice dice\n"
        totalBid = total
        typeBid = dice
        opponentGuess.text = opponentBid
        //setSpinnerChoice(total,dice)
        takeTurn()
    }
    // check illegal
    fun checkIllegal(total: Int, dice: Int): Boolean {
        if(total < totalBid || dice < typeBid) return true
        if(total == totalBid && dice == typeBid) return true
        return false
    }
    //guess
    fun guess() {

        val total = totalSpinner.selectedItem.toString()
        val type = typeSpinner.selectedItem.toString()

        println("guess value =======================")
        println(totalBid)
        println(typeBid)
        println(total)
        println(type)
        if(checkIllegal(total.toInt(), type.toInt())) {
            Toast.makeText(this, "Illegal move", Toast.LENGTH_SHORT).show()
        } else {
            println("-------------------------------------------")
            println("-------------------------------------------")
            bluetoothService.sendInfo("guess$gibberish$total$gibberish$type")
            yourBid += total + " x " + type + "dice"+"\n"
            yourGuess.text = yourBid
            //setSpinnerChoice(total.toInt(), type.toInt())
            passTurn()
        }

    }
    //set choice
    fun setSpinnerChoice(total: Int, dice: Int) {
        totalList = if (total < 9) {
            (total+1..10).toList()
        } else {
            listOf(10)
        }
        println("total list value")
        println(totalList)

        dicelist = if (dice < 5) {
            (dice+1..6).toList()
        } else {
            listOf(6)
        }
        println("total list value")
        println(totalList)
        totalSpinner.adapter
        setUpSpinner()
    }

    //set up spinner
    fun setUpSpinner() {
        //set up total spinner
        totalSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,totalList)

        //set type spinner
        typeSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,dicelist)
    }

    private inner class GameMaster(val bluetoothService: MyBluetoothService, val funcPlayActivity: FuncPlayActivity) {
        // Game Master class is to help control the flow of the game
        val ran = mutableListOf<Int>()
        //first roll value
        var yourValue = -2
        var opponentValue = -2

        //init game by choosing a random value to decide who goes first
        fun initGame() {
            yourValue = (1..6).random()
            bluetoothService.sendInfo("initGame$gibberish$yourValue$gibberish"+0)
            decideTurn()
        }

        // decide between two players who will go first
        fun decideTurn() {
            if(yourValue > 0 && opponentValue > 0) {
                when {
                    yourValue > opponentValue -> {
                        println("you go first")
                        funcPlayActivity.gameInit("first")
                    }
                    yourValue < opponentValue -> {
                        println("you go second")
                        funcPlayActivity.gameInit("second")
                    }
                    else -> reroll()
                }
            }
        }

        // reroll dice if two players roll the same value
        fun reroll() {
            opponentValue = -2
            initGame()
            decideTurn()
        }

        // roll dice for game start
        fun rollDice(number: Int){
            ran.clear()
            for (x in 0..number) {
                ran += (1..6).random()
            }
            funcPlayActivity.displayDice(ran.toString())
        }

        // challenge opponent bid
        fun call() {
            bluetoothService.sendInfo("call$gibberish"+ "0$gibberish"+"0")
        }

        // return opponent call
        fun returnCall(){
            bluetoothService
        }
    }
}
