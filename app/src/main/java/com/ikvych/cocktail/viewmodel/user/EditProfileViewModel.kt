package com.ikvych.cocktail.viewmodel.user

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.repository.source.UserRepository
import com.ikvych.cocktail.presentation.mapper.user.UserModelMapper
import com.ikvych.cocktail.presentation.model.user.UserModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class EditProfileViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val mapper: UserModelMapper
) : BaseViewModel(application, savedStateHandle) {
    private val minimumNameSymbolCount: Int = 4

    val userLiveData = userRepository.userLiveData

    val shouldReturnLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val firstNameInputLiveData: MutableLiveData<String> = object : MediatorLiveData<String>() {
        init {
            addSource(userLiveData) {
                value = it?.name ?: ""
            }
        }
    }
    val lastNameInputLiveData: MutableLiveData<String> = object : MediatorLiveData<String>() {
        init {
            addSource(userLiveData) {
                value = it?.lastName ?: ""
            }
        }
    }

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

    val isValidEnteredDataLIveData: MutableLiveData<Boolean> =
        object : MediatorLiveData<Boolean>() {
            init {
                addSource(isValidFirstNameLiveData) {
                    value = validate()
                }
                addSource(isValidLastNameLiveData) {
                    value = validate()
                }
            }

            fun validate(): Boolean {
                val result = isValidFirstNameLiveData.value ?: false &&
                        isValidLastNameLiveData.value ?: false
                return result
            }
        }

    fun onSubmit() {
        launchRequest {
            userRepository.updateUser(
                mapper.mapFrom(
                    UserModel(
                        id = userLiveData.value!!.id,
                        name = firstNameInputLiveData.value!!,
                        lastName = lastNameInputLiveData.value!!,
                        email = userLiveData.value!!.email,
                        avatar = userLiveData.value!!.avatar
                    )
                )
            )
            shouldReturnLiveData.postValue(true)
        }
    }
}