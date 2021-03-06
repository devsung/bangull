package com.devsung.bangull.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devsung.bangull.data.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val login = MutableLiveData<Boolean>()

    fun login() =
        CoroutineScope(Dispatchers.Main).launch {
            if (repository.login(email.value.toString(), password.value.toString()))
                login.value = true
        }
}