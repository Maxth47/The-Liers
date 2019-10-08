package com.example.theliers

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.theliers.bluetooth.BluetoothHandler
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val sharedPreference = SharedPreference(this)
        if (sharedPreference.getUsername() !== ""){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        btn_start.setOnClickListener {
            if (edt_name.text.isNotEmpty()){
                val sharedPreferenceX = SharedPreference(this)
                sharedPreferenceX.setUsername(edt_name.text.toString())
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please, Enter your name first.", Toast.LENGTH_SHORT).show()
            }

        }

        btn_aboutUs.setOnClickListener {
            Toast.makeText(this, "Metropolia Developers: Thanh Le, Son Bui", Toast.LENGTH_SHORT).show()
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

}
