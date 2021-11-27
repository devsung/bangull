package com.devsung.bangull.data

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.widget.Toast
import kotlin.concurrent.thread

class SettingRepository(private val context: Context) : Repository(context) {

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

    fun requestPermission(): Cancellable {
        val permission = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG
        )
        var granted = 0
        permission.forEach { p ->
            if (context.checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED)
                granted++
        }
        return if (granted != permission.size)
            PermissionRequester.requestPermissions(context, *permission) {
                var isGranted = true
                it.forEach { permission ->
                    if (permission.state != State.GRANTED)
                        isGranted = false
                }
                if (!isGranted)
                    Toast.makeText(context, "앱 권한 설정에 모두 동의해주세요.", Toast.LENGTH_SHORT).show()
            }
        else { { } }
    }
}