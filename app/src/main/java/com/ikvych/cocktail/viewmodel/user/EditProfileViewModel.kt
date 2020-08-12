package com.ikvych.cocktail.viewmodel.user

import android.app.Application
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.repository.source.UserRepository
import com.ikvych.cocktail.presentation.extension.mapNotNull
import com.ikvych.cocktail.presentation.mapper.user.UserModelMapper
import com.ikvych.cocktail.presentation.model.user.UserModel
import com.ikvych.cocktail.util.FirebaseHelper
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class EditProfileViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val mapper: UserModelMapper,
    val analytic: FirebaseHelper
) : BaseViewModel(application, savedStateHandle) {
    private val minimumNameSymbolCount: Int = 4

    val triggerObserver: Observer<in Any?> = Observer {  }
    val userLiveData:LiveData<UserModel?> = userRepository.userLiveData.map {
        when {
            it != null -> mapper.mapTo(it)
            else -> null
        }
    }

    val userFullNameLiveData = userLiveData.mapNotNull { "$name $lastName" }

    init {
        userFullNameLiveData.observeForever(triggerObserver)
    }

    override fun onCleared() {
        userFullNameLiveData.removeObserver(triggerObserver)
        super.onCleared()
    }

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
            analytic.logEvent(ANALYTIC_EVENT_PROFILE_DATA_CHANGE, bundleOf(
                    ANALYTIC_KEY_USER_NAME to userFullNameLiveData.value
                )
            )
        }
    }

    companion object {
        const val ANALYTIC_EVENT_PROFILE_DATA_CHANGE = "profile_data_change"
        const val ANALYTIC_KEY_USER_NAME = "user_name"
    }
}