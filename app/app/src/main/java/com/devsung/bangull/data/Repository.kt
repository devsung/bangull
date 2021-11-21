package com.devsung.bangull.data

import android.content.Context
import android.util.Base64
import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.devsung.androidhelper.support.network.callback.NetworkCallback
import com.devsung.androidhelper.support.network.callback.Transport

open class Repository(private val context: Context) {

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    fun getSharedPreferences(fileName: String) = EncryptedSharedPreferences.create(
        fileName,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun showToast(string: String) = Toast.makeText(context, string, Toast.LENGTH_SHORT).show()

    fun isEmpty(string: String) = string == "null" || string == ""

    fun getTransport(): Transport = NetworkCallback(context).getTransport()

    fun decode(url: String) = String(Base64.decode(url, Base64.DEFAULT))

    external fun getLoginUrlFromJNI(): String
}