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

    val salt = MutableLiveData<String>()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            val user = repository.getUser()
            if (!user.isEmpty() && repository.loginLogic(user.email, user.password))
                salt.value = repository.salt
        }
    }

    fun login() =
        CoroutineScope(Dispatchers.Main).launch {
            if (repository.login(email.value.toString(), password.value.toString()))
                salt.value = repository.salt
        }
}