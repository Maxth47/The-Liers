package com.example.theliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.theliers.bluetooth.BluetoothHandler
import com.example.theliers.bluetooth.MyBluetoothService
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

                        "bid" -> {
                            getOpponentGuessHistory(order[1].toInt(), order[2].toInt())
                            takeTurn()
                        }

                        "call" -> {
                            gameMaster.returnCall()
                            gameMaster.decideWin(order[1])
                        }

                        "returncall" -> {
                            gameMaster.decideWin(order[1])
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

    var startIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_func_play)

        //set up buttons for game
        guessButton.isEnabled = false
        callButton.isEnabled = false
        totalSpinner.isEnabled = false
        typeSpinner.isEnabled = false
        enemyRoll.text = "enemy has 5 dice"
        //set up spinner
        setUpSpinner()

        //start button to start game
        startGameButton.setOnClickListener {
            if (startIndex==0) {
                gameMaster.initGame()
                startIndex++
            } else {
                val display =  gameMaster.enemyNumberOfDice + 1
                enemyRoll.text = display.toString()
                gameMaster.startNextRound()
            }
            startGameButton.isEnabled = false
        }

        //bid button to send bid
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
                gameMaster.rollDice()
                takeFirstTurn()
                startGameButton.isEnabled = false
            }

            "second" -> {
                turnView.text = "You go second"
                gameMaster.rollDice() //roll from 0 to 4 aka 5 dice
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
    fun getOpponentGuessHistory(total: Int, dice: Int) {
        opponentBid += "$total x $dice dice\n"
        totalBid = total
        typeBid = dice
        gameMaster.setCurrentBid(total,dice)
        println("------ current bid ------")
        println(gameMaster.currentBid)
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
    //bid
    fun guess() {
        val total = totalSpinner.selectedItem.toString()
        val type = typeSpinner.selectedItem.toString()

        if(checkIllegal(total.toInt(), type.toInt())) {
            Toast.makeText(this, "Illegal move", Toast.LENGTH_SHORT).show()
        } else {

            bluetoothService.sendInfo("bid$gibberish$total$gibberish$type")
            yourBid += total + " x " + type + "dice"+"\n"
            yourGuess.text = yourBid
            gameMaster.setCurrentBid(total.toInt(), type.toInt())
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

    //display enemy roll
    fun displayEnemyRoll(enemyRollArray: String){
        enemyRoll.text = enemyRollArray
    }

    //clear bid history
    fun clearGuessHistory(opponentDice: String) {
        val enemyDice = opponentDice.toInt()+1
        val display = "enemy has $enemyDice dice"
        totalBid = 0
        typeBid = 0
        opponentGuess.text =""
        opponentBid = ""
        yourBid = ""
        yourGuess.text = ""
        enemyRoll.text = display
    }

    //go to result
    fun goToResult(win: String){
        if(win == "win") {
            startActivity(Intent(this, winActivity::class.java))
        } else {
            startActivity(Intent(this, LoseActivity::class.java))
        }
    }

    //display win lost
    fun displayWin(win: String) {
        turnView.text = win
    }

    private inner class GameMaster(val bluetoothService: MyBluetoothService, val funcPlayActivity: FuncPlayActivity) {
        // Game Master class is to help control the flow of the game
        var numberOfDice = 4
        var enemyNumberOfDice = 4
        val yourRollList = mutableListOf<Int>()
        //first roll value
        var yourValue = -2
        var opponentValue = -2

        var currentBidder: String? = null
        var currentBid = arrayListOf(0,0)
        lateinit var lastRoundResult: String

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
                        currentBidder = "you"
                    }
                    yourValue < opponentValue -> {
                        println("you go second")
                        funcPlayActivity.gameInit("second")
                        currentBidder = "enemy"
                    }
                    else -> reroll()
                }
            }
        }

        //get current bid
        fun setCurrentBid(total: Int, type: Int) {
            currentBid.clear()
            currentBid = arrayListOf(total, type)
        }

        // reroll dice if two players roll the same value
        fun reroll() {
            opponentValue = -2
            initGame()
            decideTurn()
        }

        // roll dice for game start
        fun rollDice(){
            yourRollList.clear()
            for (x in 0..numberOfDice) {
                yourRollList += (1..6).random()
            }
            funcPlayActivity.displayDice(yourRollList.toString())
        }

        // challenge opponent bid
        fun call() {
            var sentInfo = "call$gibberish"
            yourRollList.forEach{
                sentInfo+= "$it-"
            }
            currentBidder = "enemy"
            sentInfo += "-!@#$%^)+_-&*--"
            bluetoothService.sendInfo(sentInfo)
        }

        // return opponent call
        fun returnCall(){
            var sentInfo = "returncall$gibberish"
            yourRollList.forEach{
                sentInfo+= "$it-"
            }
            currentBidder = "you"
            sentInfo += "-!@#$%^)+_-&*--"
            bluetoothService.sendInfo(sentInfo)
        }

        // decide winnder of round
        fun decideWin(opponentRoll: String) {
            funcPlayActivity.displayEnemyRoll(opponentRoll)
            val oppRollList = opponentRoll.split("-")
            var joinedList = oppRollList.toMutableList()
            yourRollList.forEach{
                joinedList.add(it.toString())
            }
            println(joinedList)
            val count = joinedList.count{it == currentBid[1].toString()}
            println("-----------------------")
            println(currentBidder)
            println(count)
            println(currentBid[0])
            println(currentBid[1])
            if(currentBidder == "you") {
                lastRoundResult = if(count == currentBid[0] || count > currentBid[0]) {
                    println(1)
                    println("you win this round")
                    Toast.makeText(this@FuncPlayActivity,"You win this \nStarting next round",Toast.LENGTH_LONG).show()
                    "win"
                } else {
                    println(2)
                    println("you lose this round")
                    Toast.makeText(this@FuncPlayActivity,"You lose this round\nStarting next round",Toast.LENGTH_LONG).show()
                    "lost"
                }
            } else {
                lastRoundResult = if(count == currentBid[0] || count > currentBid[0]) {
                    println(3)
                    println("you lose this round")
                    Toast.makeText(this@FuncPlayActivity,"You lost this \nStarting next round",Toast.LENGTH_LONG).show()
                    "lost"
                } else {
                    println(4)
                    println("you win this round")
                    Toast.makeText(this@FuncPlayActivity,"You won this round\nStarting next round",Toast.LENGTH_LONG).show()
                    "win"
                }
            }
            funcPlayActivity.displayWin(lastRoundResult)
            funcPlayActivity.passTurn()
            funcPlayActivity.startGameButton.isEnabled = true
        }

        //start next round
        fun startNextRound() {
            currentBid.clear()
            currentBid = arrayListOf(0,0)

            if(lastRoundResult == "win") {
                enemyNumberOfDice--
                funcPlayActivity.clearGuessHistory(enemyNumberOfDice.toString())

                if(enemyNumberOfDice < 0) {
                    bluetoothService.stopConnect()
                    funcPlayActivity.goToResult("win")
                } else {
                    rollDice()
                    funcPlayActivity.passTurn()
                }

            } else {
                numberOfDice--
                funcPlayActivity.clearGuessHistory(enemyNumberOfDice.toString())

                if(numberOfDice < 0) {
                    bluetoothService.stopConnect()
                    funcPlayActivity.goToResult("lost")
                } else {
                    rollDice()
                    funcPlayActivity.takeFirstTurn()
                }
            }

        }

    }
}
