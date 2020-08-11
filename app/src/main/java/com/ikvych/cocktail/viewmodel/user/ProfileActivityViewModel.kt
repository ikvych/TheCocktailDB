package com.ikvych.cocktail.viewmodel.user

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import com.ikvych.cocktail.data.repository.source.TokenRepository
import com.ikvych.cocktail.data.repository.source.UserRepository
import com.ikvych.cocktail.presentation.extension.mapNotNull
import com.ikvych.cocktail.presentation.mapper.user.UserModelMapper
import com.ikvych.cocktail.presentation.model.user.UserModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.io.File

class ProfileActivityViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val mapper: UserModelMapper,
    private val tokenRepository: TokenRepository
) : BaseViewModel(application, savedStateHandle) {

    val userLiveData:LiveData<UserModel?> = userRepository.userLiveData.map {
        when {
            it != null -> mapper.mapTo(it)
            else -> null
        }
    }

    val userFullNameLiveData = userLiveData.mapNotNull { "$name $lastName" }
    val userEmailLiveData = userLiveData.mapNotNull { email }
    val isUserPresentLiveData: LiveData<Boolean> = MutableLiveData()

    fun refreshUser() {
        launchRequest {
            userRepository.refreshUser()
        }
    }

    fun uploadAvatar(file: File, onUploadProgress: (Float) -> Unit = {_ -> }) {
        launchRequest {
            userRepository.updateUserAvatar(file, onUploadProgress)
        }
    }

    fun removeUser() {
        launchRequest {
            userRepository.deleteUser()
        }
        tokenRepository.token = null
    }

    fun checkForUser() {
        launchRequest {
            val user = userRepository.getUser()
            isUserPresentLiveData.postValue(user != null)
        }
    }
}