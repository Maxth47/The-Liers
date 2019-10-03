package com.example.theliers

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1).also {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),1)
        }

        // init bluetooth
        initBluetooth()

        btn_start.setOnClickListener {
            if (edt_name.text.isNotEmpty()){
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please, Enter your name first.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // to check when user confirmed or cancelled bluetooth enabling
        when (requestCode) {
            REQUEST_ENABLE_BT -> {
                println("xxxxxxx")
                println("return from bluetooth request")
                when(resultCode) {
                    Activity.RESULT_OK -> {
                        println("ok")
                        //assuming the user ok'ed bluetooth
                        BluetoothHandler.checkBluetoothBond()
                    }
                    Activity.RESULT_CANCELED -> println("cancelled")
                }
            }
        }
    }


    // init bluetooth on from inside the app
    private fun initBluetooth() {
        // checking for bluetooth avail and turn on bluetooth if not on
        if (BluetoothHandler.checkBluetoothAvail()) {
            println("===============================")
            println("bluetooth is available")
            // activate bluetooth if bluetooth is not turned on
            if(!BluetoothHandler.checkBluetoothEnabled()) {
                println("bluetooth is not turned on")
                turnBluetoothOn()
            }
        }
    }
    // go to intent to turn  bluetooth on
    private fun turnBluetoothOn() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }
}
