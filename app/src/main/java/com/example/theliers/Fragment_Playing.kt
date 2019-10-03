package com.example.theliers

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.seismic.ShakeDetector
import kotlinx.android.synthetic.main.fragment_playing.*
import java.io.File

class Fragment_Playing: Fragment(), ShakeDetector.Listener, AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    var shakeTurn = 0
    var playableState = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playing, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner_1.adapter = activity?.let { ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, resources.getStringArray(R.array.numberOfDice)) }
        spinner_2.adapter = activity?.let { ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, resources.getStringArray(R.array.whatDice)) }

        dice_no6.setImageResource(R.drawable.ic_dice_)
        dice_no7.setImageResource(R.drawable.ic_dice_)
        dice_no8.setImageResource(R.drawable.ic_dice_)
        dice_no9.setImageResource(R.drawable.ic_dice_)
        dice_no10.setImageResource(R.drawable.ic_dice_)

        if (playableState){
            val sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val shakeDetector = ShakeDetector(this)
            shakeDetector.start(sensorManager)
        }
    }

    override fun hearShake() {
        shakeTurn ++
        val ran = mutableListOf<Int>()
        for (x in 1..5) {
            ran += (1..6).random()
        }
        //txtView.setText(shakeTurn.toString()+ran)
        setDiceImage(dice_no1,ran[0])
        setDiceImage(dice_no2,ran[1])
        setDiceImage(dice_no3,ran[2])
        setDiceImage(dice_no4,ran[3])
        setDiceImage(dice_no5,ran[4])
        playableState = false


        // Save into external storage.
        if(ran.isNotEmpty() && Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val inputText = ran.toString() + "\n"
            val filePath = context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(filePath, "History.txt")
            file.appendText(inputText)
        } else {
            Toast.makeText(context, "No dice saved", Toast.LENGTH_LONG).show()
        }

        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val filePath = context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(filePath, "History.txt")
            if(file.exists()) txtView.text = file.readText()
            else txtView.text = "No text available"
        }
    }

    private fun setDiceImage(imageView: ImageView, number: Int){
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