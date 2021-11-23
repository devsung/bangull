package com.devsung.bangull.data

import android.content.Context
import android.content.SharedPreferences
import kotlin.concurrent.thread

class SettingRepository(context: Context) : Repository(context) {

    fun setSetting(array: ArrayList<Boolean>) {
        thread {
            val sharedPreferences = getSharedPreferences("setting")
            sharedPreferences
                .edit()
                .putBoolean("voice", array[0])
                .putBoolean("popup", array[1])
                .putBoolean("door", array[2])
                .apply()
        }
    }

    fun getSetting(): ArrayList<Boolean> {
        val sharedPreferences = getSharedPreferences("setting")
        return ArrayList<Boolean>().apply {
            add(getBoolean(sharedPreferences, "voice", true))
            add(getBoolean(sharedPreferences, "popup", true))
            add(getBoolean(sharedPreferences, "door", false))
        }
    }

    private fun getBoolean(sharedPreferences: SharedPreferences, key: String, def: Boolean) =
        sharedPreferences.getBoolean(key, def)
}