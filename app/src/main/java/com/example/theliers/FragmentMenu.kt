package com.example.theliers

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

import kotlinx.android.synthetic.main.fragment_menu.*
import java.io.File

class FragmentMenu: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the user name from SharePreference
        val sharedPreference = SharedPreference(this.requireActivity())
        Toast.makeText(context, "Hello "+sharedPreference.getUsername()+", Welcome to The Liar game :)", Toast.LENGTH_SHORT).show()

        // NewGame clicked, go to ChooseActivity
        btn_newGame.setOnClickListener {
            val intent = Intent(context, ChooseActivity::class.java)
            startActivity(intent)
        }

        // History clicked, go to History Fragment
        btn_history.setOnClickListener {
            val fr = fragmentManager?.beginTransaction()
            fr?.replace(R.id.fragment, FragmentHistory())
            fr?.commit()
        }

        // History clicked, go to Rule Fragment
        btn_rules.setOnClickListener {
            val fr = fragmentManager?.beginTransaction()
            fr?.replace(R.id.fragment, FragmentRules())
            fr?.commit()
        }

        // Reset clicked, clear history and user name, go to Start Activity
        btn_reset.setOnClickListener {
            val sharedPreferenceX = SharedPreference(this.requireActivity())
            sharedPreferenceX.setUsername("")

            val filePath = context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(filePath, "History.txt")
            file.delete()

            val intent = Intent(context, StartActivity::class.java)
            startActivity(intent)
        }

    }

}