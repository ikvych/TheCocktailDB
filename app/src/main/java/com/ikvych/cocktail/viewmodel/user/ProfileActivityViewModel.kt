package com.ikvych.cocktail.viewmodel.user

import android.app.Application
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import com.google.firebase.analytics.FirebaseAnalytics
import com.ikvych.cocktail.data.repository.source.TokenRepository
import com.ikvych.cocktail.data.repository.source.UserRepository
import com.ikvych.cocktail.presentation.extension.mapNotNull
import com.ikvych.cocktail.presentation.mapper.user.UserModelMapper
import com.ikvych.cocktail.presentation.model.user.UserModel
import com.ikvych.cocktail.util.FirebaseAnalyticHelper
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.io.File

class ProfileActivityViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val mapper: UserModelMapper,
    private val tokenRepository: TokenRepository,
    val analytic: FirebaseAnalyticHelper
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
            val oldAvatarAddress = userLiveData.value?.avatar
            userRepository.updateUserAvatar(file, onUploadProgress)
            val newAvatarAddress = userLiveData.value?.avatar
            if (oldAvatarAddress != newAvatarAddress) {
                analytic.logEvent(ANALYTIC_EVENT_CHANGE_AVATAR, bundleOf(
                    ANALYTIC_KEY_USER_AVATAR to newAvatarAddress,
                    ANALYTIC_KEY_USER_NAME to userFullNameLiveData.value
                ))
            }
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

    companion object {
        const val ANALYTIC_EVENT_CHANGE_AVATAR = "change_profile_photo"
        const val ANALYTIC_KEY_USER_AVATAR = "user_avatar"
        const val ANALYTIC_KEY_USER_NAME = "user_name"
    }
}