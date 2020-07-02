package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.core.util.toAndroidPair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.R
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.util.regex.Pattern

class AuthViewModel(application: Application) : BaseViewModel(application) {
    private val passwordPattern: Pattern =
        Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z~!@#\$%^&*]{6,}") //не менше 6 символів і містить хоча б одну цифру і хоча б одну літеру
    private val loginPattern: Pattern = Pattern.compile(".{7,}") //більше 6 символів

    private val loginErrorMessage: String = "Логін повинний містити більше 6 символів"
    private val passwordErrorMessage: String =
        "Пароль повинний містити більше 6 символів, одну літеру і одну цифру"

    val isKeyboardShown: MutableLiveData<Boolean> = MutableLiveData()
    val loginInputLiveData: MutableLiveData<String?> = MutableLiveData()
    val passwordInputLiveData: MutableLiveData<String?> = MutableLiveData()
    val isLoginDataValidLiveData: LiveData<Pair<Boolean, Boolean>> =
        object : MediatorLiveData<Pair<Boolean, Boolean>>() {
            init {
                value = Pair(false, false)
                addSource(loginInputLiveData) {
                    invalidateLiveData()
                }
                addSource(passwordInputLiveData) {
                    invalidateLiveData()
                }
            }

            private fun invalidateLiveData() {
                val login = loginInputLiveData.value ?: ""
                val password = passwordInputLiveData.value ?: ""

                value = if (loginPattern.matcher(login).matches()) {
                    Pair(true, value!!.second)
                } else {
                    Pair(false, value!!.second)
                }

                value = if (passwordPattern.matcher(password).matches()) {
                    Pair(value!!.first, true)
                } else {
                    Pair(value!!.first, false)
                }
            }
        }
    val errorMessageViewModel: LiveData<String?> = object : MediatorLiveData<String?>() {
        init {
            value = "${loginErrorMessage}\n${passwordErrorMessage}"
            addSource(isLoginDataValidLiveData) {
                generateErrorMessage()
            }
        }

        private fun generateErrorMessage() {
            var finalErrorMessage: String? = null
            if (!isLoginDataValidLiveData.value!!.first) {
                finalErrorMessage = loginErrorMessage
            }
            if (!isLoginDataValidLiveData.value!!.second) {
                if (finalErrorMessage != null) {
                    finalErrorMessage += "\n${passwordErrorMessage}"
                } else {
                    finalErrorMessage = passwordErrorMessage
                }
            }
            value = finalErrorMessage
        }
    }
}