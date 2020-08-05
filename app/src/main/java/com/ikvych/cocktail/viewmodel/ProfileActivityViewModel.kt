package com.ikvych.cocktail.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import com.ikvych.cocktail.data.repository.source.AuthRepository
import com.ikvych.cocktail.data.repository.source.UserRepository
import com.ikvych.cocktail.presentation.extension.mapNotNull
import com.ikvych.cocktail.presentation.mapper.user.UserModelMapper
import com.ikvych.cocktail.presentation.model.user.UserModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.io.File

class ProfileActivityViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val mapper: UserModelMapper
) : BaseViewModel(application, savedStateHandle) {

    val userLiveData:LiveData<UserModel?> = userRepository.userLiveData.map {
        when {
            it != null -> mapper.mapTo(it)
            else -> null
        }
    }

    val userFullNameLiveData = userLiveData.mapNotNull { "$name $lastName" }
    val userEmailLiveData = userLiveData.mapNotNull { email }

    val userNameLiveData: MutableLiveData<String> = MutableLiveData()

    fun uploadAvatar(file: File) {
        launchRequest {
            userRepository.updateUserLogo(file)
            userRepository.refreshUser()
        }
    }

    init {
        userNameLiveData.setValue("HELLO FROM VIEW MODEL")
/*        launchRequest {
            authRepository.logIn(
                firstName = "Ivan",
                lastName = "Kvych",
                email = "ikvuch@gmail.com",
                password = "123456"
            )
        }*/

        launchRequest {
            authRepository.signIn(
                email = "ikvuch@gmail.com",
                password = "123456"
            )
        }
    }
}