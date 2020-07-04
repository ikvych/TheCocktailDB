package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.util.regex.Pattern

class AuthViewModel(application: Application) : BaseViewModel(application) {
    private val passwordPattern: Pattern =
        Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z~!@#\$%^&*]{6,}") //не менше 6 символів і містить хоча б одну цифру і хоча б одну літеру
    private val loginPattern: Pattern = Pattern.compile(".{7,}") //більше 6 символів

    private val correctLogin = "123qweasd"
    private val correctPassword = "123qweasd"

    private val loginErrorMessage: String = "Логін повинний містити більше 6 символів"
    private val passwordErrorMessage: String =
        "Пароль повинний містити більше 6 символів, одну літеру і одну цифру"

    val isKeyboardShown: MutableLiveData<Boolean> = MutableLiveData()
    val loginInputLiveData: MutableLiveData<String?> = MutableLiveData()
    val passwordInputLiveData: MutableLiveData<String?> = MutableLiveData()
    init {
        loginInputLiveData.value = correctLogin
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
                //блок виконується коли є помилка у співпадінні захардкодженого логіну або паролю з введеними
            } else if (!isLoginDataValidLiveData.value!!) {
                finalErrorMessage = "Невірні логін або пароль!"
            }
            value = finalErrorMessage
        }
    }
}