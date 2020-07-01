package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class AuthViewModel(application: Application) : BaseViewModel(application) {
    val login: String = "ivan"
    val password: String = "ivan"


    val loginInputLiveData: MutableLiveData<String?> = MutableLiveData()
    val passwordInputLiveData: MutableLiveData<String?> = MutableLiveData()
    val isLoginDataValidLiveData: LiveData<Boolean> = object : MediatorLiveData<Boolean>() {
        init {
            addSource(loginInputLiveData) {
                invalidateLiveData()
            }
            addSource(passwordInputLiveData) {
                invalidateLiveData()
            }
        }
        private fun invalidateLiveData() {
            value = loginInputLiveData.value == login && passwordInputLiveData.value == password
        }
    }
}