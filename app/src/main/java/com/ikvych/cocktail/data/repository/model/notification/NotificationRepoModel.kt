package com.ikvych.cocktail.data.repository.model.notification

class NotificationRepoModel(
    val id: Long = 1L,
    val type: String = "",
    val image: String? = "",
    val cocktailId: Long? = -1L
)