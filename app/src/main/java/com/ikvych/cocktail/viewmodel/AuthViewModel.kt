package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.util.delegate.stateHandleLiveData
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.util.regex.Pattern
import com.ikvych.cocktail.R

class AuthViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    companion object {
        const val EXTRA_KEY_LOGIN = "EXTRA_KEY_LOGIN"
        const val EXTRA_KEY_PASSWORD = "EXTRA_KEY_PASSWORD"
    }

    private val passwordPattern: Pattern =
        Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z~!@#\$%^&*]{6,}") //не менше 6 символів і містить хоча б одну цифру і хоча б одну літеру
    private val loginPattern: Pattern = Pattern.compile(".{7,}") //більше 6 символів

    private val correctLogin = application.resources.getString(R.string.auth_correct_login)
    private val correctPassword = application.resources.getString(R.string.auth_correct_password)

    private val loginErrorMessage: String = application.resources.getString(R.string.auth_invalid_login)
    private val passwordErrorMessage: String = application.resources.getString(R.string.auth_invalid_password)

    val isKeyboardShown: MutableLiveData<Boolean> = MutableLiveData()
    val loginInputLiveData: MutableLiveData<String?> by stateHandleLiveData(EXTRA_KEY_LOGIN)
    val passwordInputLiveData: MutableLiveData<String?> by stateHandleLiveData(EXTRA_KEY_PASSWORD)

    init {
        if (loginInputLiveData.value.isNullOrEmpty())
        loginInputLiveData.value = correctLogin
        if (passwordInputLiveData.value.isNullOrEmpty())
        passwordInputLiveData.value = correctPassword
    }
    //відслідковує чи введені логін і пароль відповідають паттернам логіну і пароля
    //liveData містить в собі пару Boolean значень, перше відповідає чи валідний логін,
    // друге відповідає чи валідний пароль
    val isLoginDataMatchPatternLiveData: LiveData<Pair<Boolean, Boolean>> =
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

    //відслідковує чи введені пароль і логін відповідають захардкодженим
    val isLoginDataValidLiveData: LiveData<Boolean> =
        object : MediatorLiveData<Boolean>() {
            init {
                value = false
                addSource(isLoginDataMatchPatternLiveData) {
                    validateData()
                }
            }

            private fun validateData() {
                val login = loginInputLiveData.value ?: ""
                val password = passwordInputLiveData.value ?: ""
                value = login == correctLogin && password == correctPassword
            }
        }


    val errorMessageViewModel: LiveData<String?> = object : MediatorLiveData<String?>() {
        init {
            value = "${loginErrorMessage}\n${passwordErrorMessage}"
            addSource(isLoginDataMatchPatternLiveData) {
                generateErrorMessage()
            }
        }

        private fun generateErrorMessage() {
            var finalErrorMessage: String? = null

            //блок виконується коли є помилка у паттерні логіна або паролю
            if (!isLoginDataMatchPatternLiveData.value!!.first || !isLoginDataMatchPatternLiveData.value!!.second) {
                if (!isLoginDataMatchPatternLiveData.value!!.first) {
                    finalErrorMessage = loginErrorMessage
                }
                if (!isLoginDataMatchPatternLiveData.value!!.second) {
                    if (finalErrorMessage != null) {
                        finalErrorMessage += "\n${passwordErrorMessage}"
                    } else {
                        finalErrorMessage = passwordErrorMessage
                    }
                }
                //блок виконується коли немає помилок у попередньому блоці але є помилка
                // у співпадінні захардкодженого логіну або паролю з введеними
            } else if (!isLoginDataValidLiveData.value!!) {
                finalErrorMessage = application.resources.getString(R.string.auth_invalid_data)
            }
            value = finalErrorMessage
        }
    }
}