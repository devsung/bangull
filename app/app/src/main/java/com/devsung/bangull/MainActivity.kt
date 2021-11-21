package com.devsung.bangull

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.devsung.bangull.data.UserRepository
import com.devsung.bangull.databinding.ActivityMainBinding
import com.devsung.bangull.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = MainViewModel(UserRepository(this)).also {
            it.login.observe(this) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}