package com.ikvych.cocktail.viewmodel.auth

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.repository.source.AuthRepository
import com.ikvych.cocktail.util.EMPTY_STRING
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.lang.StringBuilder
import java.util.regex.Pattern

class SignUpViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository
) : BaseViewModel(application, savedStateHandle) {

    private val minimumNameSymbolCount: Int = 4
    private val minimumPasswordSymbolCount: Int = 6
    private val atLeastOneNumber = Regex("[0-9]+")
    private val atLeastOneLetter = Regex("[a-zA-Z]+")
    private val emailPattern: Pattern =
        Pattern.compile("^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})\$")
    private val passwordPattern: Pattern =
        Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z~!@#\$%^&*]{6,}")

    val passwordVisibilityLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val shouldLogInLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val firstNameInputLiveData: MutableLiveData<String> = MutableLiveData("Ivan")
    val lastNameInputLiveData: MutableLiveData<String> = MutableLiveData("Kvych")
    val emailInputLiveData: MutableLiveData<String> = MutableLiveData("ikvych@gmail.com")
    val passwordInputLiveData: MutableLiveData<String> = MutableLiveData("123qweasd")
    val passwordConfirmInputLiveData: MutableLiveData<String> = MutableLiveData("123qweasd")

    /*First Name*/
    val validateFirstNameLiveData: LiveData<String> = firstNameInputLiveData.map {
        val symbolQuantity = minimumNameSymbolCount - it.length
        application.resources.getString(R.string.auth_minimal_symbol_count)
            .replace("$", symbolQuantity.toString())
    }

    val isValidFirstNameLiveData: LiveData<Boolean> = validateFirstNameLiveData.map {
        minimumNameSymbolCount - firstNameInputLiveData.value!!.length <= 0
    }

    /*Last Name*/
    val validateLastNameLiveData: LiveData<String> = lastNameInputLiveData.map {
        val symbolQuantity = minimumNameSymbolCount - it.length
        application.resources.getString(R.string.auth_minimal_symbol_count)
            .replace("$", symbolQuantity.toString())
    }

    val isValidLastNameLiveData: LiveData<Boolean> = validateLastNameLiveData.map {
        minimumNameSymbolCount - lastNameInputLiveData.value!!.length <= 0
    }

    /*Email*/
    val validateEmailLiveData: LiveData<String> = emailInputLiveData.map {
        application.resources.getString(R.string.auth_email_format)
    }

    val isValidEmailLiveData: LiveData<Boolean> = validateEmailLiveData.map {
        emailPattern.matcher(emailInputLiveData.value!!).matches()
    }


    /*Password*/
    val isValidPasswordLiveData: LiveData<Boolean> = passwordInputLiveData.map {
        val result = passwordPattern.matcher(it).matches()
        result
    }

    val validatePasswordLiveData: LiveData<String> = isValidPasswordLiveData.map {
        if (!it) {
            val builder = StringBuilder()
            val password = passwordInputLiveData.value!!
            if (password.length < minimumPasswordSymbolCount) {
                val symbolQuantity = minimumPasswordSymbolCount - password.length
                builder.append(
                    application.resources.getString(R.string.auth_minimal_symbol_count)
                        .replace("$", symbolQuantity.toString()) + "\n"
                )
            }
            if (!password.contains(atLeastOneNumber) || !password.contains(atLeastOneLetter)) {
                builder.append(application.resources.getString(R.string.auth_password_minimun_require))
                if (!password.contains(atLeastOneNumber)) {
                    builder.append(application.resources.getString(R.string.auth_password_one_number))
                }
                if (!password.contains(atLeastOneLetter)) {
                    builder.append(application.resources.getString(R.string.auth_password_one_letter))
                }
            }
            builder.toString()
        } else {
            EMPTY_STRING
        }
    }

    /*Confirm password*/
    val isValidPasswordConfirmLiveData: LiveData<Boolean> = object : MediatorLiveData<Boolean>() {
        init {
            addSource(passwordConfirmInputLiveData) {
                value = it == passwordInputLiveData.value
            }
            addSource(passwordInputLiveData) {
                value = it == passwordConfirmInputLiveData.value
            }
        }
    }

    val validateConfirmPasswordLiveData: LiveData<String> = isValidPasswordConfirmLiveData.map {
        if (!it) {
            application.resources.getString(R.string.auth_passwords_do_not_mutch)
        } else {
            EMPTY_STRING
        }
    }

    val isValidEnteredDataLIveData: MutableLiveData<Boolean> =
        object : MediatorLiveData<Boolean>() {
            init {
                addSource(isValidFirstNameLiveData) {
                    value = validate()
                }
                addSource(isValidLastNameLiveData) {
                    value = validate()
                }
                addSource(isValidEmailLiveData) {
                    value = validate()
                }
                addSource(isValidPasswordLiveData) {
                    value = validate()
                }
                addSource(isValidPasswordConfirmLiveData) {
                    value = validate()
                }
            }

            fun validate(): Boolean {
                return isValidFirstNameLiveData.value ?: false &&
                        isValidLastNameLiveData.value ?: false &&
                        isValidEmailLiveData.value ?: false &&
                        isValidPasswordLiveData.value ?: false &&
                        isValidPasswordConfirmLiveData.value ?: false
            }
        }

    fun onSubmit() {
        launchRequest(shouldLogInLiveData) {
            authRepository.signUp(
                firstName = firstNameInputLiveData.value!!,
                lastName = lastNameInputLiveData.value!!,
                email = emailInputLiveData.value!!,
                password = passwordInputLiveData.value!!
            )
        }
    }

}