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

    //зберігає значення чи показана клавіатура чи прихована
    val isKeyboardShown: MutableLiveData<Boolean> = MutableLiveData()
    //дані введені з поля логін
    val loginInputLiveData: MutableLiveData<String?> = MutableLiveData()
    //дані введені з поля пароль
    val passwordInputLiveData: MutableLiveData<String?> = MutableLiveData()
    //відслідковує чи введені логін і пароль відповідають паттернам логіну і пароля
    //liveData містить в собі пару Boolean значень, перше відповідає чи валідний логін,
    // друге відповідає чи валідний пароль
    val isAuthDataMatchPatternLiveData: LiveData<Pair<Boolean, Boolean>> =
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
    val isAuthDataValidLiveData: LiveData<Boolean> =
        object : MediatorLiveData<Boolean>() {
            init {
                value = false
                addSource(isAuthDataMatchPatternLiveData) {
                    validateData()
                }
            }

            private fun validateData() {
                val login = loginInputLiveData.value ?: ""
                val password = passwordInputLiveData.value ?: ""
                value = login == correctLogin && password == correctPassword
            }
        }



    //формує повідомлення з помилкою валідації логіну і паролю
    val errorMessageLiveData: LiveData<String?> = object : MediatorLiveData<String?>() {
        init {
            value = "${loginErrorMessage}\n${passwordErrorMessage}"

            addSource(isAuthDataMatchPatternLiveData) {
                generateErrorMessage()
            }

        }

        private fun generateErrorMessage() {
            var finalErrorMessage: String? = null

            //блок виконується коли є помилка у паттерні логіна або паролю
            if (!isAuthDataMatchPatternLiveData.value!!.first || !isAuthDataMatchPatternLiveData.value!!.second) {
                if (!isAuthDataMatchPatternLiveData.value!!.first) {
                    finalErrorMessage = loginErrorMessage
                }
                if (!isAuthDataMatchPatternLiveData.value!!.second) {
                    if (finalErrorMessage != null) {
                        finalErrorMessage += "\n${passwordErrorMessage}"
                    } else {
                        finalErrorMessage = passwordErrorMessage
                    }
                }
                //блок виконується коли захардкоджений логіну або пароль не відповідають введеним
            } else if (!isAuthDataValidLiveData.value!!) {
                finalErrorMessage = application.resources.getString(R.string.auth_invalid_data)
            }
            value = finalErrorMessage
        }
    }

    init {
        isAuthDataMatchPatternLiveData.observeForever(triggerObserver)
        errorMessageLiveData.observeForever(triggerObserver)
        isAuthDataValidLiveData.observeForever(triggerObserver)
    }

    override fun onCleared() {
        isAuthDataMatchPatternLiveData.removeObserver(triggerObserver)
        errorMessageLiveData.removeObserver(triggerObserver)
        isAuthDataValidLiveData.removeObserver(triggerObserver)
        super.onCleared()
    }
}