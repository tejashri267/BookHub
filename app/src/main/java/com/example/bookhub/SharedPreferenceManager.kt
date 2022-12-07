package com.example.bookhub

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(context: Context) {
    private var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences =
            context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun putString(key: String?, value: String?) {
        val editor = sharedPreferences!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String?): String? {
        return sharedPreferences!!.getString(key, null)
    }

    fun clearPreferences() {
        val editor = sharedPreferences!!.edit()
        editor.clear()
        editor.apply()
    }

}