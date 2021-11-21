package com.devsung.bangull

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.devsung.bangull.data.UserRepository
import com.devsung.bangull.databinding.ActivityLoginBinding
import com.devsung.bangull.viewmodels.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.vm = LoginViewModel(UserRepository(this)).also {
            it.salt.observe(this) {
                startActivity(Intent(this, MainActivity::class.java).also { i -> i.putExtra("salt", it) })
                finish()
            }
        }
    }
}