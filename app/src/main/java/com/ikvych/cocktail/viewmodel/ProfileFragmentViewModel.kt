package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class ProfileFragmentViewModel(application: Application) : BaseViewModel(application) {
    val logOutLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun onLogOut() {
        logOutLiveData.value = true
    }
}