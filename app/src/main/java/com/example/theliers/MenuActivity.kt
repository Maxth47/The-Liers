package com.example.theliers

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.theliers.bluetooth.BluetoothHandler


class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportFragmentManager.beginTransaction().replace(R.id.fragment, FragmentMenu()).commit()

        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),1)
        // init bluetooth
        initBluetooth()
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
