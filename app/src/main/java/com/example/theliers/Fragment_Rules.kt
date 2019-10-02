package com.example.theliers

import android.content.Context
import android.net.ConnectivityManager
import android.os.*
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_rules.*
import org.json.JSONObject
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class Fragment_Rules: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rules, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(isNetworkAvailable()) {
            val myRunnable = Conn(mHandler)
            val myThread = Thread(myRunnable)
            myThread.start()
        }
    }


    // create a handle to add message
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(inputMessage: Message) { if (inputMessage.what == 0) {
            txt_rules.text = inputMessage.obj.toString()
            txt_rules.setMovementMethod(ScrollingMovementMethod())
        }
        } }

    // first, check if network is available.
    private fun isNetworkAvailable(): Boolean { val cm = requireActivity().getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
        return cm.activeNetworkInfo?.isConnected == true
    }


    //create a worker with a Handler as parameter
    class Conn(mHand: Handler): Runnable {
        val myHandler = mHand
        override fun run() {
            var content = StringBuilder()
            try {
                // declare URL to text file, create a connection to it and put into stream.
                val myUrl = URL("http://users.metropolia.fi/~thanhvl/HowToPlayLiarDice.txt")
                val urlConnection = myUrl.openConnection() as HttpURLConnection
                val inputStream = urlConnection.inputStream

                // get text from stream, convert to string and send to main thread.
                val allText = inputStream.bufferedReader().use { it.readText() }
                content.append(allText)
                val str = content.toString()
                val msg = myHandler.obtainMessage()
                msg.what = 0
                msg.obj = str
                myHandler.sendMessage(msg)
            } catch (e: Exception) {
                Log.d("Error", e.toString())
            }
        }

    }

}

