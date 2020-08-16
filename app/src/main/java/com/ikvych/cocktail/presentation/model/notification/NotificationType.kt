package com.ikvych.cocktail.presentation.model.notification

enum class NotificationType(val key: String) {
    NOTIFICATION_TYPE_MAIN("main"),
    NOTIFICATION_TYPE_PROFILE("profile"),
    NOTIFICATION_TYPE_PROFILE_EDIT("profile_edit"),
    NOTIFICATION_TYPE_COCKTAIL_DETAIL("cocktail_detail"),
    NOTIFICATION_TYPE_RATE_APP("rate_app"),
    NOTIFICATION_TYPE_UNDEFINED("undefined"),
}