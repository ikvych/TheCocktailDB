package com.ikvych.cocktail.util.delegate

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.util.ArrayList
import java.util.HashMap
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T> BaseViewModel.stateHandle(
    saveKey: String
): ReadWriteProperty<Any, T?> =
    object : ReadWriteProperty<Any, T?> {

        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            return savedStateHandle.get(saveKey)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
            savedStateHandle.set(saveKey, value)
        }
    }

inline fun <reified T> BaseViewModel.stateHandleLiveData(
    saveKey: String? = null,
    initialValue: T? = null
): ReadOnlyProperty<Any, MutableLiveData<T?>> =
    object : ReadOnlyProperty<Any, MutableLiveData<T?>> {
        override fun getValue(thisRef: Any, property: KProperty<*>): MutableLiveData<T?> {
            val stateKey = saveKey ?: property.name
            return if (initialValue == null) {
                savedStateHandle.getLiveData(stateKey)
            } else {
                savedStateHandle.getLiveData(stateKey, initialValue)
            }
        }
    }
