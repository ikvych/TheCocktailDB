package com.ikvych.cocktail.data.repository.impl.source.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.db.model.Drink
import com.ikvych.cocktail.data.db.source.DrinkDbSource
import com.ikvych.cocktail.data.repository.source.DrinkRepository
import com.ikvych.cocktail.data.repository.source.base.BaseRepository

open class BaseRepositoryImpl : BaseRepository