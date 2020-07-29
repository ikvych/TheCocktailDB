package com.ikvych.cocktail.viewmodel.base

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.repository.DrinkRepositoryImpl
import com.ikvych.cocktail.data.repository.base.DrinkRepository

open class BaseViewModel(application: Application): AndroidViewModel(application)