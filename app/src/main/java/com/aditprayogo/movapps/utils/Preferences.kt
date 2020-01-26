package com.aditprayogo.movapps.utils

import android.content.Context
import android.content.SharedPreferences
import java.security.AccessControlContext

class Preferences(val context: Context) {

    companion object {
        const val MEETING_PREF = "USER_PREF"
    }

    val sharePref = context.getSharedPreferences(MEETING_PREF, 0)

    fun setValues(key: String, value: String){

        val editor: SharedPreferences.Editor = sharePref.edit()
        editor.putString(key,value)
        editor.apply()

    }

    fun getValues(key: String): String? {
        return sharePref.getString(key,null)
    }
}