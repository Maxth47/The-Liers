package com.example.theliers

import android.content.Context

class SharedPreference(context: Context) {

    var PREFERENCE_USERNAME = "SharedPreference_Username"

    val preference = context.getSharedPreferences(PREFERENCE_USERNAME, Context.MODE_PRIVATE)

    fun getUsername(): String {
        return preference.getString(PREFERENCE_USERNAME, "").toString()
    }

    fun setUsername(name:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_USERNAME, name)
        editor.apply()
    }
}