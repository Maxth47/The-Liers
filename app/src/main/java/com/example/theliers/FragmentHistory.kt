package com.example.theliers

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_history.*
import java.io.File
import kotlin.math.roundToInt

class FragmentHistory: Fragment() {

    private var history = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txt_history.isSelected = true

        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val filePath = context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(filePath, "History.txt")
            if(file.exists()) {
                history = file.readText()
                txt_history.text = history
                txt_history.movementMethod = ScrollingMovementMethod()
            }
            else txt_history.text = getString(R.string.history_empty)
        }

        btn_goBackFromHistory.setOnClickListener {
            val fr = fragmentManager?.beginTransaction()
            fr?.replace(R.id.fragment, FragmentMenu())
            fr?.commit()
        }

        val numberOfMatches = history.split("[MatchResult:]").size-1
        val numberOfWinMatches = history.split("WIN").size-1
        var winningRatio = 0
        if (numberOfWinMatches != 0) {winningRatio =
            (100*numberOfWinMatches/numberOfMatches).toFloat().roundToInt()
        }
        val sharedPreference = SharedPreference(this.requireActivity())
        txt_winning_rate.text = getString(R.string.history_analysis, sharedPreference.getUsername(), numberOfMatches, winningRatio)
    }
}