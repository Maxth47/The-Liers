package com.example.theliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_lose.*

class LoseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lose)

        button.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
    }
}
