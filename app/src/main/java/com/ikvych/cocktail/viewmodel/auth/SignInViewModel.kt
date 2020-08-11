package com.ikvych.cocktail.viewmodel.auth

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.repository.source.AuthRepository
import com.ikvych.cocktail.util.delegate.stateHandleLiveData
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.util.regex.Pattern

class SignInViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository
) : BaseViewModel(application, savedStateHandle) {

    companion object {
        const val EXTRA_KEY_LOGIN = "EXTRA_KEY_LOGIN"
        const val EXTRA_KEY_PASSWORD = "EXTRA_KEY_PASSWORD"
    }

    private val triggerObserver: Observer<in Any?> = Observer { }

    //не менше 6 символів і містить хоча б одну цифру і хоча б одну літеру
    private val passwordPattern: Pattern =
        Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z~!@#\$%^&*]{6,}")
    private val loginPattern: Pattern = Pattern.compile(".{7,}") //більше 6 символів

    private val correctLogin = application.resources.getString(R.string.auth_correct_login)
    private val correctPassword = application.resources.getString(R.string.auth_correct_password)

    private val loginErrorMessage: String =
        application.resources.getString(R.string.auth_invalid_login)
    private val passwordErrorMessage: String =
        application.resources.getString(R.string.auth_invalid_password)

    val shouldLogInLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val requestFocusOnLoginLiveData: MutableLiveData<Unit?> = MutableLiveData()
    val requestFocusOnPasswordLiveData: MutableLiveData<Unit?> = MutableLiveData()
    val isKeyboardShownLiveData: MutableLiveData<Boolean?> by stateHandleLiveData()
    val loginInputLiveData: MutableLiveData<String?> by stateHandleLiveData()
    val passwordInputLiveData: MutableLiveData<String?> by stateHandleLiveData()

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
    val isLoginDataValidLiveData: LiveData<Boolean> = isLoginDataMatchPatternLiveData.map {
        /*validateData()*/ true
    }

    private fun validateData(): Boolean {
        val login = loginInputLiveData.value ?: ""
        val password = passwordInputLiveData.value ?: ""
        return login == correctLogin && password == correctPassword
    }


    val errorMessageLiveData: LiveData<String?> = isLoginDataValidLiveData.map {
        generateErrorMessage()
    }

    private fun generateErrorMessage(): String? {
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
            finalErrorMessage = getApplication<Application>().resources.getString(R.string.auth_invalid_data)
        }
        return finalErrorMessage
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
            launchRequest(shouldLogInLiveData) {
                authRepository.signIn(
                    email = loginInputLiveData.value!!,
                    password = passwordInputLiveData.value!!
                )
            }
        } else {
            //переводжу фокус на поле вводу для паролю оскільки в ньому є помилка
            if (!isLoginDataMatchPatternLiveData.value!!.second) {
                requestFocusOnPasswordLiveData.value = Unit
            }
            //переводжу фокус на поле вводу для логіну оскільки в ньому є помилка
            if (!isLoginDataMatchPatternLiveData.value!!.first) {
                requestFocusOnLoginLiveData.value = Unit
            }
            shouldLogInLiveData.value = false
        }
    }

}