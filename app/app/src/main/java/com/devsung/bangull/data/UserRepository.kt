package com.devsung.bangull.data

import android.content.Context
import com.devsung.androidhelper.support.network.callback.Transport
import com.devsung.androidhelper.support.network.connection.Connection
import com.devsung.androidhelper.support.parser.XML
import com.devsung.androidhelper.support.security.hash.Hash
import kotlinx.coroutines.*
import java.net.URL

@ExperimentalCoroutinesApi
class UserRepository(context: Context) : Repository(context) {

    var salt: String? = null

    private fun setUser(email: String, password: String) {
        val sharedPreferences = getSharedPreferences("user")
        sharedPreferences
            .edit()
            .putString("email", email)
            .putString("password", password)
            .apply()
    }

    fun getUser(): User {
        val sharedPreferences = getSharedPreferences("user")
        val email = sharedPreferences.getString("email", "").toString()
        val password = sharedPreferences.getString("password", "").toString()
        return User(email, password)
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
                    salt = (item[1] as Pair<*, *>).second.toString()
                    setUser(email, password)
                    return@async true
                }
            }
            return@async false
        }.await()
    }

    private fun getHash(string: String): String =
        Hash(string, "SHA-512").output.split("\n").joinToString("")
}