package com.example.theliers

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_start.*

import kotlinx.android.synthetic.main.fragment_menu.*
import java.io.File

class Fragment_Menu: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreference = SharedPreference(this.requireActivity())
        Toast.makeText(context, "Hello "+sharedPreference.getUsername()+", Welcome to The Liar game :)", Toast.LENGTH_SHORT).show()

        btn_newGame.setOnClickListener {
            val intent = Intent(context, ChooseActivity::class.java)
            startActivity(intent)
        }

        btn_history.setOnClickListener {
            var fr = getFragmentManager()?.beginTransaction()
            fr?.replace(R.id.fragment, Fragment_History())
            fr?.commit()
        }

        btn_rules.setOnClickListener {
            var fr = getFragmentManager()?.beginTransaction()
            fr?.replace(R.id.fragment, Fragment_Rules())
            fr?.commit()
        }

        btn_reset.setOnClickListener {

            val sharedPreference = SharedPreference(this.requireActivity())
            sharedPreference.setUsername("")

            val filePath = context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(filePath, "History.txt")
            file.delete()

            val intent = Intent(context, StartActivity::class.java)
            startActivity(intent)
        }

    }

}