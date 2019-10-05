package com.example.theliers

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

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

        btn_newGame.setOnClickListener {
            val intent = Intent(context, PlayActivity::class.java)
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
            val intent = Intent(context, StartActivity::class.java)
            startActivity(intent)
        }

    }

}