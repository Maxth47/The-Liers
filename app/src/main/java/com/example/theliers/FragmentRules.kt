package com.example.theliers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_rules.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

@Suppress("DEPRECATION")
class FragmentRules: Fragment() {

    lateinit var jsonArr:JSONArray
    var jsonArrIndex:Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rules, container, false)
    }

    @SuppressLint("StringFormatMatches", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            if(isNetworkAvailable()) {
                val myRunnable = Conn(mHandler)
                val myThread = Thread(myRunnable)
                myThread.start()
            }

        // go back to Menu Fragment
        btn_goBackFromRules.setOnClickListener {
            val fr = fragmentManager?.beginTransaction()
            fr?.replace(R.id.fragment, FragmentMenu())
            fr?.commit()
        }

        // load content for last step
        btn_leftArrow.setOnClickListener {
            jsonArrIndex--
            if (jsonArrIndex <0) jsonArrIndex = 0
            txt_stepTitle.text = getString(R.string.step_title, jsonArrIndex, jsonArr.getJSONObject(jsonArrIndex).getString("title"))
            txt_stepContent.text = getString(R.string.step_content, jsonArr.getJSONObject(jsonArrIndex).getString("content"), jsonArr.getJSONObject(jsonArrIndex).getString("example"))
            downloadImageUsingOkHTTP(jsonArr.getJSONObject(jsonArrIndex).getString("imageURL"))
        }

        // load content for new step
        btn_rightArrow.setOnClickListener {
            jsonArrIndex++
            if (jsonArrIndex == jsonArr.length()) jsonArrIndex = jsonArr.length()-1
            txt_stepTitle.text = getString(R.string.step_title, jsonArrIndex, jsonArr.getJSONObject(jsonArrIndex).getString("title"))
            txt_stepContent.text = getString(R.string.step_content, jsonArr.getJSONObject(jsonArrIndex).getString("content"), jsonArr.getJSONObject(jsonArrIndex).getString("example"))
            downloadImageUsingOkHTTP(jsonArr.getJSONObject(jsonArrIndex).getString("imageURL"))
        }

    }


    // create a handle to add message
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        @SuppressLint("StringFormatMatches")
        override fun handleMessage(inputMessage: Message) {
            if (inputMessage.what == 0) {
                jsonArr = JSONArray(inputMessage.obj.toString())
                txt_stepTitle.text = getString(R.string.step_title, 0, jsonArr.getJSONObject(jsonArrIndex).getString("title"))
                txt_stepContent.text = getString(R.string.step_content, jsonArr.getJSONObject(0).getString("content"), jsonArr.getJSONObject(jsonArrIndex).getString("example"))
                downloadImageUsingOkHTTP(jsonArr.getJSONObject(0).getString("imageURL"))
            }
        }
    }

    // first, check if network is available.
    private fun isNetworkAvailable(): Boolean { val cm = requireActivity().getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
        return cm.activeNetworkInfo?.isConnected == true
    }

    //create a worker with a Handler as parameter
    class Conn(mHand: Handler): Runnable {
        private val myHandler = mHand
        override fun run() {
            val content = StringBuilder()
            try {
                // declare URL to text file, create a connection to it and put into stream.
                val myUrl = URL("http://users.metropolia.fi/~thanhvl/Liar-Dice-Rules.json")
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

    // load image from imageURL
    private fun downloadImageUsingOkHTTP(imageURL: String) {
        val okRequest = Request.Builder()
            .url(imageURL)
            .build()

        OkHttpClient().newCall(okRequest).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
            }

            override fun onResponse(call: Call?, response: Response?) {
                val inputStream = response?.body()?.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                requireActivity().runOnUiThread {
                    img_stepImage.setImageBitmap(bitmap)
                }
            }

        })
    }

}

