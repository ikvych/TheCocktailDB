package com.ikvych.cocktail.viewmodel.notification

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.repository.model.notification.NotificationRepoModel
import com.ikvych.cocktail.data.repository.source.NotificationRepository
import com.ikvych.cocktail.presentation.mapper.notification.NotificationModelMapper
import com.ikvych.cocktail.presentation.model.notification.NotificationModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class NotificationViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val notificationRepository: NotificationRepository,
    private val mapper: NotificationModelMapper
) : BaseViewModel(
    application,
    savedStateHandle
) {

    fun saveNotification(model: NotificationModel) {
        launchRequest {
            notificationRepository.saveNotification(mapper.mapFrom(model))
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
            notificationRepository.getNotification()?.run(mapper::mapTo)
        }
        return notificationLiveData
    }
}