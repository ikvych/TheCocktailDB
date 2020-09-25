package com.ikvych.cocktail.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import com.ikvych.cocktail.prresentation.extension.distinctNotNullValues
import com.ikvych.cocktail.prresentation.extension.mapNotNull
import com.ikvych.cocktail.prresentation.mapper.user.UserModelMapper
import com.ikvych.cocktail.prresentation.model.user.UserModel
import com.ikvych.cocktail.prresentation.ui.base.viewmodel.BaseViewModel
import com.ikvych.cocktail.prresentation.util.FirebaseHelper
import com.ikvych.cocktail.repository.source.TokenRepository
import com.ikvych.cocktail.repository.source.UserRepository
import java.io.File

class ProfileActivityViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val mapper: UserModelMapper,
    private val tokenRepository: TokenRepository,
    val analytic: FirebaseHelper
) : BaseViewModel(application, savedStateHandle) {

    private val userLiveData: LiveData<UserModel> =
        userRepository.userLiveData.mapNotNull { mapper.mapTo(this) }

    val userFullNameLiveData = userLiveData.map { "${it.name} ${it.lastName}" }
    val userDataChangedLiveData = userFullNameLiveData.distinctNotNullValues()

    val userEmailLiveData = userLiveData.map { it.email }

    val userAvatarLiveData = userLiveData.map { it.avatar }
    val userAvatarPhotoChangedLiveData = userAvatarLiveData.distinctNotNullValues()

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


}