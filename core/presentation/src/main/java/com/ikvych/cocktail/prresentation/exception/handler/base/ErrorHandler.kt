package com.ikvych.cocktail.prresentation.exception.handler.base

import androidx.fragment.app.FragmentManager
import java.lang.Exception

interface ErrorHandler/*<ViewRequest : LaunchRequest>*/ {
    val fragmentManager: FragmentManager
    val onUnauthorized: () -> Unit

    fun handleError(info: /*RequestError<ViewRequest>*/Exception)
}