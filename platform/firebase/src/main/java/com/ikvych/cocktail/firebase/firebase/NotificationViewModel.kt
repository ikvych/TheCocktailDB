package com.ikvych.cocktail.firebase.firebase

/*import android.app.Application
import androidx.lifecycle.MutableLiveData*/

/*
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
}*/
