package com.devsung.bangull

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.devsung.bangull.data.UserRepository
import com.devsung.bangull.databinding.FragmentMainBinding
import com.devsung.bangull.viewmodels.MainViewModel
import com.devsung.bangull.viewmodels.MainViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.vm = ViewModelProvider(this, MainViewModelFactory(UserRepository(context!!.applicationContext)))
            .get(MainViewModel::class.java)
        return binding.root
    }
}