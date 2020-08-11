package com.ikvych.cocktail.exception.handler.base

import android.content.Context
import androidx.fragment.app.FragmentManager
import java.lang.Exception

interface ErrorHandler/*<ViewRequest : LaunchRequest>*/ {
    val fragmentManager: FragmentManager
    val onUnauthorized: () -> Unit

    fun handleError(info: /*RequestError<ViewRequest>*/Exception)
}