package com.ikvych.cocktail.viewmodel.notification

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.repository.model.notification.NotificationRepoModel
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.data.repository.source.NotificationRepository
import com.ikvych.cocktail.presentation.mapper.cocktail.CocktailModelMapper
import com.ikvych.cocktail.presentation.mapper.notification.NotificationModelMapper
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.presentation.model.notification.NotificationModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class NotificationViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val notificationRepository: NotificationRepository,
    private val cocktailRepository: CocktailRepository,
    private val notificationMapper: NotificationModelMapper,
    private val cocktailMapper: CocktailModelMapper
) : BaseViewModel(
    application,
    savedStateHandle
) {

    fun saveCocktail(model: CocktailModel) {
        launchRequest {
            cocktailRepository.addOrReplaceCocktail(cocktailMapper.mapFrom(model))
        }
    }

    fun getCocktailByIdLiveDataAndSave(cocktailId: Long): MutableLiveData<CocktailModel?> {
        val cocktailLiveData = MutableLiveData<CocktailModel?>()
        launchRequest(cocktailLiveData) {
            val cocktailDb = cocktailRepository.findCocktailById(cocktailId)
            if (cocktailDb == null) {
                val cocktailNet = cocktailRepository.getCocktailById(cocktailId)
                if (cocktailNet != null) {
                    cocktailRepository.addOrReplaceCocktail(cocktailNet)
                    cocktailMapper.mapTo(cocktailNet)
                } else {
                    null
                }
            } else {
                cocktailMapper.mapTo(cocktailDb)
            }
        }
        return cocktailLiveData
    }

    fun saveNotification(model: NotificationModel) {
        launchRequest {
            notificationRepository.saveNotification(notificationMapper.mapFrom(model))
        }
    }

    fun deleteNotification() {
        launchRequest {
            notificationRepository.deleteNotification()
        }
    }

    fun getNotificationLiveData(): MutableLiveData<NotificationModel?> {
        val notificationLiveData = MutableLiveData<NotificationModel?>()
        launchRequest(notificationLiveData) {
            notificationRepository.getNotification()?.run(notificationMapper::mapTo)
        }
        return notificationLiveData
    }
}