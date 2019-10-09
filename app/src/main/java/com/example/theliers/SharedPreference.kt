package com.example.theliers

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) {

    private var preferenceUserName = "SharedPreference_Username"
    private var preferenceEnemyName = "SharedPreference_Enemy_Name"


    private val preference: SharedPreferences = context.getSharedPreferences(preferenceUserName, Context.MODE_PRIVATE)

    fun getUsername(): String {
        return preference.getString(preferenceUserName, "").toString()
    }

    fun setUsername(name:String){
        val editor = preference.edit()
        editor.putString(preferenceUserName, name)
        editor.apply()
    }

    fun getEnemyName(): String {
        return preference.getString(preferenceEnemyName, "").toString()
    }

    fun setEnemyname(name:String){
        val editor = preference.edit()
        editor.putString(preferenceEnemyName, name)
        editor.apply()
    }
}