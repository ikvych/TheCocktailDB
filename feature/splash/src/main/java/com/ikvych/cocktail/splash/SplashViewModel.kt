package com.ikvych.cocktail.splash

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.prresentation.ui.base.viewmodel.BaseViewModel
import com.ikvych.cocktail.repository.source.UserRepository

class SplashViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : BaseViewModel(application, savedStateHandle) {
    val isUserPresentLiveData: LiveData<Boolean> = MutableLiveData()
    fun refreshUser() {
        launchRequest {
            userRepository.refreshUser()
        }
    }

    fun checkForUser() {
        launchRequest {
            isUserPresentLiveData.postValue(userRepository.getUser() != null)
        }
    }
}