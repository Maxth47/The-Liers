package com.example.theliers

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_rules.*
import java.io.File

class Fragment_History: Fragment() {

    var numberOfGames = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //txt_history.setSingleLine()
        txt_history.isSelected = true

        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val filePath = context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(filePath, "History.txt")
            if(file.exists()) {
                txt_history.text = file.readText()
                numberOfGames = file.readText().split("[").size
            }
            else txt_history.text = "History empty ..."
        }

        btn_goBackFromHistory.setOnClickListener {
            var fr = getFragmentManager()?.beginTransaction()
            fr?.replace(R.id.fragment, Fragment_Menu())
            fr?.commit()
        }

        val sharedPreference = SharedPreference(this.requireActivity())
        txt_winning_rate.text = "Player : " + sharedPreference.getUsername() +" : Matches : "+ numberOfGames + " : Winning Ratio : 92%"

    }
}