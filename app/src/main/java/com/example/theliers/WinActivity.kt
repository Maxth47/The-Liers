package com.example.theliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_lose.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class WinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)

        button.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        saveWinResultToExternalStorage()
    }

    fun saveWinResultToExternalStorage() {
        if( Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            var sharedPreference = SharedPreference(this)
            val userName = sharedPreference.getUsername()
            val opponentName = sharedPreference.getEnemyName()
            val timeStamp: String = SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date())
            val inputText = "[MatchResult:] ${timeStamp} : ${userName} WIN AGAINST ${opponentName} \n"
            val filePath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(filePath, "History.txt")
            file.appendText(inputText)
        } else {
            Toast.makeText(this, "No dice saved", Toast.LENGTH_LONG).show()
        }
    }

}
