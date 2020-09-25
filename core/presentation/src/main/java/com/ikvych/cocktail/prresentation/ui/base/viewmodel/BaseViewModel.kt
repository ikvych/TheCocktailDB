package com.ikvych.cocktail.prresentation.ui.base.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.common.exception.ApiError
import com.ikvych.cocktail.common.exception.ApiException
import com.ikvych.cocktail.common.exception.NetworkConnectionError
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(
    application: Application,
    val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val networkObserver: Observer<Boolean> = Observer {}
    val errorLiveData: MutableLiveData<java.lang.Exception> = MutableLiveData()

/*    protected val appSettingRepository: AppSettingRepository =
        AppSettingRepositoryImpl.instance(application)*/

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
                    errorLiveData.value = parseException(e)
                    errorLiveData.value = null
                }
            }
        }
    }

    private fun parseException(e: java.lang.Exception) : java.lang.Exception {
        return when (e) {
            is ApiException -> {
                if (e.code == ApiException.NO_INTERNET_CONNECTION) {
                    NetworkConnectionError()
                } else {
                    ApiError(e.method, e.code, e.details, e.httpCode, e.cause)
                }
            }
            else -> {e}
        }
    }

    protected fun <T> LiveData<T>.setValue(value: T) {
        (this as? MutableLiveData)?.value = value
    }

    protected fun <T> LiveData<T>.postValue(value: T) {
        (this as? MutableLiveData)?.postValue(value)
    }

/*    val selectedLanguageLiveData: MutableLiveData<Int> =
        appSettingRepository.selectedLanguageLiveData*/
}