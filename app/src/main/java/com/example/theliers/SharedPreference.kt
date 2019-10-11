package com.example.theliers

import android.content.Context

class SharedPreference(context: Context) {

    private var preferenceUserName: String = "SharedPreference_Username"
    private var preferenceEnemyName = "SharedPreference_Enemy_Name"


    private val preference = context.getSharedPreferences(preferenceUserName, Context.MODE_PRIVATE)

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

    fun setEnemyName(name:String){
        val editor = preference.edit()
        editor.putString(preferenceEnemyName, name)
        editor.apply()
    }
}