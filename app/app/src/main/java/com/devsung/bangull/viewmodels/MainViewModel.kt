package com.devsung.bangull.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devsung.bangull.data.*
import com.devsung.bangull.service.BangullService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel(
    private val userRepository: UserRepository,
    private val settingRepository: SettingRepository
) : ViewModel() {

    private lateinit var user: User
    private var cancelRequest: Cancellable? = null

    val text = MutableLiveData<String>()
    val login = MutableLiveData<Boolean>()
    val animation = ObservableField<Boolean>()
    val service = MutableLiveData<Boolean>()
    val setting = MutableLiveData<ArrayList<Boolean>>()

    init {
        ViewBinding.ready = 0
        CoroutineScope(Dispatchers.Main).launch {
            user = userRepository.getUser()
            if (user.isEmpty() || !userRepository.loginLogic(user.email, user.password))
                login.value = false
            else {
                userRepository.databaseUpdate()
                cancelRequest = settingRepository.requestPermission()
            }
        }
        setting.value = settingRepository.getSetting()
        serviceText()
    }

    fun button() {
        if (animation.get() == null)
            animation.set(true)
        else
            animation.set(!animation.get()!!)
    }

    fun service() {
        BangullService.isAlive = !BangullService.isAlive
        service.value = BangullService.isAlive
        serviceText()
    }

    private fun serviceText() {
        text.value = if (BangullService.isAlive) "중지" else "시작"
    }

    fun setting() = settingRepository.setSetting(setting.value!!)

    fun logout() = userRepository.logout()

    override fun onCleared() {
        super.onCleared()
        cancelRequest?.invoke()
    }
}