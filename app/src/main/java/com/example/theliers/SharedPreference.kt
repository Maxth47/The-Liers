package com.example.theliers

import android.content.Context

class SharedPreference(context: Context) {

    var PREFERENCE_USERNAME = "SharedPreference_Username"
    var PREFERENCE_ENEMY_NAME = "SharedPreference_Enemy_Name"

    val preference = context.getSharedPreferences(PREFERENCE_USERNAME, Context.MODE_PRIVATE)
    val enemy_prefernce = context.getSharedPreferences(PREFERENCE_ENEMY_NAME, Context.MODE_PRIVATE)

    fun getUsername(): String {
        return preference.getString(PREFERENCE_USERNAME, "").toString()
    }

    fun setUsername(name:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_USERNAME, name)
        editor.apply()
    }

    fun getEnemyName(): String {
        return preference.getString(PREFERENCE_ENEMY_NAME, "").toString()
    }

    fun setEnemyname(name:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_ENEMY_NAME, name)
        editor.apply()
    }
}