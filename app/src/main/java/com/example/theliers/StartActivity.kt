package com.example.theliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        btn_start.setOnClickListener {
            if (edt_name.text.isNotEmpty()){
                val sharedPreference = SharedPreference(this)
                sharedPreference.setUsername(edt_name.text.toString())
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Hello "+edt_name.text+", Welcome to The Liar game :)", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please, Enter your name first.", Toast.LENGTH_SHORT).show()
            }

        }

        btn_aboutUs.setOnClickListener {
            Toast.makeText(this, "Metropolia Developers: Thanh Le, Son Bui", Toast.LENGTH_SHORT).show()
        }


    }
}
