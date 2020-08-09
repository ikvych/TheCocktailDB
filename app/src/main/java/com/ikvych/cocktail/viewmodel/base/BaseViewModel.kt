package com.ikvych.cocktail.viewmodel.base

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.repository.source.AppSettingRepository
import com.ikvych.cocktail.data.repository.impl.source.AppSettingRepositoryImpl
import com.ikvych.cocktail.util.ConnectivityInfoLiveData
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


open class BaseViewModel(
    application: Application,
    val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    val networkObserver: Observer<Boolean> = Observer {}
    val errorLiveData: MutableLiveData<java.lang.Exception> = MutableLiveData()
    val connectivityInfoLiveData: ConnectivityInfoLiveData = ConnectivityInfoLiveData(application)

    init {
        connectivityInfoLiveData.observeForever(networkObserver)
    }

    override fun onCleared() {
        connectivityInfoLiveData.removeObserver(networkObserver)
        super.onCleared()
    }

    protected val appSettingRepository: AppSettingRepository =
        AppSettingRepositoryImpl.instance(application)

    protected fun <T> launchRequest(
        liveData: LiveData<T>? = null,
        context: CoroutineContext = Dispatchers.IO,
        request: suspend CoroutineScope.() -> T
    ): Job {
        return viewModelScope.launch {
            try {
                withContext(context) { request() }.apply { liveData?.setValue(this) }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    errorLiveData.value = e
                    errorLiveData.value = null
                }
            }
        }
    }

    protected fun <T> LiveData<T>.setValue(value: T) {
        (this as? MutableLiveData)?.value = value
    }

    protected fun <T> LiveData<T>.postValue(value: T) {
        (this as? MutableLiveData)?.postValue(value)
    }

    val selectedLanguageLiveData: MutableLiveData<Int> =
        appSettingRepository.selectedLanguageLiveData
}