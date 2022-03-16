package com.example.myapplication

import android.content.Context

val PREFERENCE_NAME = "SharePreferenceExample"
val PREFERENCE_LANGUAJE = "Language"

class MyPreference (context: Context){

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    fun getLoginCount() : String? {
        return preference.getString(PREFERENCE_LANGUAJE, "es")
    }


    fun setLoginCount(Language:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_LANGUAJE,Language)
        editor.apply()
    }
}