package com.example.theliers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_play.*

class PlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_Playing()).commit()

        ibtn_playing.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_Playing()).commit()
        }

        ibtn_history.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_History()).commit()
        }

        ibtn_rules.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_Rules()).commit()
        }
    }

}
