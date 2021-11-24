package com.devsung.bangull.data

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import com.devsung.androidhelper.support.network.callback.Transport
import com.devsung.androidhelper.support.network.connection.Connection
import com.devsung.androidhelper.support.parser.XML
import com.devsung.androidhelper.support.security.hash.Hash
import kotlinx.coroutines.*
import java.io.File
import java.net.URL
import kotlin.concurrent.thread

@ExperimentalCoroutinesApi
class UserRepository(private val context: Context) : Repository(context) {

    private fun setUser(email: String, password: String, salt: String) {
        thread {
            val sharedPreferences = getSharedPreferences("user")
            sharedPreferences
                .edit()
                .putString("email", email)
                .putString("password", password)
                .putString("salt", salt)
                .apply()
        }
    }

    fun getUser(): User {
        val sharedPreferences = getSharedPreferences("user")
        return User(
            sharedPreferences.getString("email", "").toString(),
            sharedPreferences.getString("password", "").toString(),
            sharedPreferences.getString("salt", "").toString()
        )
    }

    suspend fun login(_email: String, _password: String): Boolean {
        if (getTransport() == Transport.DISCONNECT)
            showToast("인터넷 연결이 필요합니다.")
        else if (isEmpty(_email) && isEmpty(_password))
            showToast("이메일과 비밀번호를 입력하세요.")
        else if (isEmpty(_email))
            showToast("이메일을 입력하세요.")
        else if (isEmpty(_password))
            showToast("비밀번호를 입력하세요.")
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_email).matches())
            showToast("이메일 형식이 올바르지 않습니다.")
        else
            return loginLogic(getHash(_email), getHash(_password))
        return false
    }

    suspend fun loginLogic(email: String, password: String): Boolean {
        return CoroutineScope(Dispatchers.Main).async {
            val result = Connection().apply {
                url = URL(decode(getLoginUrlFromJNI()))
                contents.add(Pair("id", email))
                contents.add(Pair("pw", password))
            }.open()
            val item = XML(result).contents[0] as Array<*>
            when ((item[0] as Pair<*, *>).second.toString()) {
                "0" -> showToast("해당 사용자를 찾을 수 없습니다.")
                "1" -> {
                    setUser(email, password, (item[1] as Pair<*, *>).second.toString())
                    return@async true
                }
            }
            return@async false
        }.await()
    }

    fun logout() {
        AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
            .setTitle("로그아웃")
            .setMessage("이 기기에서 로그아웃하시겠습니까?")
            .setPositiveButton("확인") { _, _ ->
                clearSharedPreferences("setting")
                clearSharedPreferences("user")
                (context as Activity).finish()
            }
            .setNegativeButton("취소", null)
            .create()
            .show()
    }

    private fun getHash(string: String): String =
        Hash(string, "SHA-512").output.split("\n").joinToString("")

    fun databaseUpdate() {
        val filePath = "${context.filesDir.absolutePath}/${decode(getDatabaseFromJNI())}"
        val file = File(filePath)
        val user = getUser()
    }
}