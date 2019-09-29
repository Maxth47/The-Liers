package com.example.theliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main2.btn_history

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        btn_newGame.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btn_history.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_History()).commit()
        }

        btn_rules.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_Rules()).commit()
        }
    }
}
