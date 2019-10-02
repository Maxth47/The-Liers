package com.example.theliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_menu.btn_history

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btn_newGame.setOnClickListener {
            val intent = Intent(this, PlayActivity::class.java)
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
