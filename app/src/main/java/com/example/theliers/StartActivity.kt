package com.example.theliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.fragment_history.*

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
                val sharedPreference = SharedPreference(this)
                sharedPreference.setUsername(edt_name.text.toString())
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
}
