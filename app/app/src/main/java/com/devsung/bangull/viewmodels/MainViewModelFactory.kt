package com.devsung.bangull.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devsung.bangull.data.SettingRepository
import com.devsung.bangull.data.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainViewModelFactory(
    private val userRepository: UserRepository,
    private val settingRepository: SettingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java))
            MainViewModel(userRepository, settingRepository) as T
        else
            throw IllegalArgumentException()
    }
}