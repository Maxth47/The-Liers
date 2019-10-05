package com.example.theliers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportFragmentManager.beginTransaction().replace(R.id.fragment, Fragment_Menu()).commit()

    }
}
