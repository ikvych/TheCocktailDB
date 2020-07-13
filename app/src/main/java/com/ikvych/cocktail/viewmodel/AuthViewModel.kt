package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ikvych.cocktail.R
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.util.regex.Pattern

class AuthViewModel(application: Application) : BaseViewModel(application) {

    private val triggerObserver: Observer<in Any?> = Observer { }
    private val passwordPattern: Pattern =
        Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z~!@#\$%^&*]{6,}") //не менше 6 символів і містить хоча б одну цифру і хоча б одну літеру
    private val loginPattern: Pattern = Pattern.compile(".{7,}") //більше 6 символів

    private val correctLogin = application.resources.getString(R.string.auth_correct_login)
    private val correctPassword = application.resources.getString(R.string.auth_correct_password)

    private val loginErrorMessage: String = application.resources.getString(R.string.auth_invalid_login)
    private val passwordErrorMessage: String = application.resources.getString(R.string.auth_invalid_password)

    val shouldLogInLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val requestFocusOnLoginLiveData: MutableLiveData<Unit> = MutableLiveData()
    val requestFocusOnPasswordLiveData: MutableLiveData<Unit> = MutableLiveData()
    val isKeyboardShownLiveData: MutableLiveData<Boolean> = MutableLiveData()
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


    val errorMessageLiveData: LiveData<String?> = object : MediatorLiveData<String?>() {
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

    init {
        isLoginDataMatchPatternLiveData.observeForever(triggerObserver)
        errorMessageLiveData.observeForever(triggerObserver)
        isLoginDataValidLiveData.observeForever(triggerObserver)
    }

    override fun onCleared() {
        isLoginDataMatchPatternLiveData.removeObserver(triggerObserver)
        errorMessageLiveData.removeObserver(triggerObserver)
        isLoginDataValidLiveData.removeObserver(triggerObserver)
        super.onCleared()
    }

    fun onSubmit() {
        if (isLoginDataMatchPatternLiveData.value!!.first &&
            isLoginDataMatchPatternLiveData.value!!.second &&
            isLoginDataValidLiveData.value!!
        ) {
            shouldLogInLiveData.value = true
        } else {
            if (!isLoginDataMatchPatternLiveData.value!!.second) {
                requestFocusOnPasswordLiveData.value = requestFocusOnPasswordLiveData.value
            }
            if (!isLoginDataMatchPatternLiveData.value!!.first) {
                requestFocusOnLoginLiveData.value = requestFocusOnLoginLiveData.value
            }
            shouldLogInLiveData.value = false
        }
    }
}