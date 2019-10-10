package com.example.theliers

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) {

    private var preferenceUserName = "SharedPreference_Username"      // key fro user name
    private var preferenceEnemyName = "SharedPreference_Enemy_Name"        // key for opponent name


    private val preference: SharedPreferences = context.getSharedPreferences(preferenceUserName, Context.MODE_PRIVATE)

    // function to get the user name
    fun getUsername(): String {
        return preference.getString(preferenceUserName, "").toString()
    }

    // function to set the user name
    fun setUsername(name:String){
        val editor = preference.edit()
        editor.putString(preferenceUserName, name)
        editor.apply()
    }

    // function to get the opponent name
    fun getEnemyName(): String {
        return preference.getString(preferenceEnemyName, "").toString()
    }

    // function to set the opponent name
    fun setEnemyname(name:String){
        val editor = preference.edit()
        editor.putString(preferenceEnemyName, name)
        editor.apply()
    }
}