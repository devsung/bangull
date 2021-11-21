package com.devsung.bangull.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devsung.bangull.data.User
import com.devsung.bangull.data.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private lateinit var user: User
    private lateinit var salt: String

    val login = MutableLiveData<Boolean>()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            user = repository.getUser()
            if (user.isEmpty() || !repository.loginLogic(user.email, user.password))
                login.value = false
            else
                salt = repository.salt.toString()
        }
    }
}