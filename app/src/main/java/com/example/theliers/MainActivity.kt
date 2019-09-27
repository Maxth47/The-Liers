package com.example.theliers

import android.content.Context
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.squareup.seismic.ShakeDetector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ShakeDetector.Listener {

    var shakeTurn = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var shakeDetector = ShakeDetector(this)
        shakeDetector.start(sensorManager)
    }

    override fun hearShake() {
        shakeTurn ++
        var ran = listOf<Int>()
        for (x in 1..5) {
            ran += (1..6).random()
        }
        txtView.setText(shakeTurn.toString()+ran)
        setDiceImage(dice_no1,ran[0])
        setDiceImage(dice_no2,ran[1])
        setDiceImage(dice_no3,ran[2])
        setDiceImage(dice_no4,ran[3])
        setDiceImage(dice_no5,ran[4])
    }

    fun setDiceImage(imageView: ImageView, number: Int){
        when (number) {
            1 -> imageView.setImageResource(R.drawable.ic_dice_1)
            2 -> imageView.setImageResource(R.drawable.ic_dice_2)
            3 -> imageView.setImageResource(R.drawable.ic_dice_3)
            4 -> imageView.setImageResource(R.drawable.ic_dice_4)
            5 -> imageView.setImageResource(R.drawable.ic_dice_5)
            else -> imageView.setImageResource(R.drawable.ic_dice_6)
        }
    }

}
