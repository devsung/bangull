package com.devsung.bangull

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.devsung.bangull.databinding.ActivityCallBinding
import com.devsung.bangull.viewmodels.CallViewModel

class CallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call)
        binding.vm = CallViewModel(intent.getParcelableExtra("customer")!!)
    }
}