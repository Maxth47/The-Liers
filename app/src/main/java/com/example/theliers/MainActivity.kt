package com.example.theliers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_Playing()).commit()

        btn_playing.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_Playing()).commit()
        }

        btn_history.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_History()).commit()
        }

        btn_rules.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_Rules()).commit()
        }
    }

}
