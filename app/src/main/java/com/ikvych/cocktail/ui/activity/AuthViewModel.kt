package com.ikvych.cocktail.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class AuthViewModel {
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