package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.R
import com.ikvych.cocktail.util.delegate.stateHandleLiveData
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.util.regex.Pattern

class AuthViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

}