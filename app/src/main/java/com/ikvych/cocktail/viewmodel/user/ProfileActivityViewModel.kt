package com.ikvych.cocktail.viewmodel.user

import android.app.Application
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import com.ikvych.cocktail.data.repository.source.TokenRepository
import com.ikvych.cocktail.data.repository.source.UserRepository
import com.ikvych.cocktail.presentation.extension.distinctNotNullValues
import com.ikvych.cocktail.presentation.extension.mapNotNull
import com.ikvych.cocktail.presentation.mapper.user.UserModelMapper
import com.ikvych.cocktail.presentation.model.user.UserModel
import com.ikvych.cocktail.util.FirebaseHelper
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.io.File

class ProfileActivityViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val mapper: UserModelMapper,
    private val tokenRepository: TokenRepository,
    val analytic: FirebaseHelper
) : BaseViewModel(application, savedStateHandle) {

    val isUserPresentLiveData: LiveData<Boolean> = MutableLiveData()
    private val userLiveData: LiveData<UserModel> =
        userRepository.userLiveData.mapNotNull { mapper.mapTo(this) }

    val userFullNameLiveData = userLiveData.map { "${it.name} ${it.lastName}" }
    val userDataChangedLiveData = userFullNameLiveData.distinctNotNullValues()

    val userEmailLiveData = userLiveData.map { it.email }

    val userAvatarLiveData = userLiveData.map { it.avatar }
    val userAvatarPhotoChangedLiveData = userAvatarLiveData.distinctNotNullValues()

    fun refreshUser() {
        launchRequest {
            userRepository.refreshUser()
        }
    }

    fun uploadAvatar(file: File, onUploadProgress: (Float) -> Unit = { _ -> }) {
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
            isUserPresentLiveData.postValue(userRepository.getUser() != null)
        }
    }
}