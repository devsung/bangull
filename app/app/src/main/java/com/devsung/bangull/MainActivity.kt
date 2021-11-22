package com.devsung.bangull

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.devsung.bangull.data.UserRepository
import com.devsung.bangull.databinding.ActivityMainBinding
import com.devsung.bangull.viewmodels.MainViewModel
import com.devsung.bangull.viewmodels.MainViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = ViewModelProvider(this, MainViewModelFactory(UserRepository(this)))
            .get(MainViewModel::class.java)
            .also {
                it.login.observe(this) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
    }
}