package com.ikvych.cocktail.prresentation.util.delegate

import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.prresentation.ui.base.viewmodel.BaseViewModel
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T> BaseViewModel.stateHandle(
    saveKey: String? = null
): ReadWriteProperty<Any, T?> =
    object : ReadWriteProperty<Any, T?> {

        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            val stateKey = saveKey ?: property.name
            return savedStateHandle.get(stateKey)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
            val stateKey = saveKey ?: property.name
            savedStateHandle.set(stateKey, value)
        }
    }

inline fun <reified T> BaseViewModel.stateHandleLiveData (
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
