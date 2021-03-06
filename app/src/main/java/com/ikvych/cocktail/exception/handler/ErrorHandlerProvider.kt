package com.ikvych.cocktail.exception.handler

import androidx.fragment.app.FragmentManager
import com.ikvych.cocktail.R
import com.ikvych.cocktail.core.common.exception.ApiException
import com.ikvych.cocktail.exception.ApiError
import com.ikvych.cocktail.exception.NetworkConnectionError
import com.ikvych.cocktail.exception.handler.base.ErrorHandler
import com.ikvych.cocktail.presentation.dialog.bottom.RegularBottomSheetDialogFragment
import com.ikvych.cocktail.presentation.dialog.regular.ErrorDialogFragment
import java.lang.Exception


open class ErrorHandlerProvider/*<ViewRequest : LaunchRequest>*/(
    override val fragmentManager: FragmentManager,
    override val onUnauthorized: () -> Unit
) : ErrorHandler/*<ViewRequest>*/ {

    override fun handleError(info: /*RequestError<ViewRequest>*/Exception) {
        when (info/* as? RequestError<ViewRequest> ?: return*/) {
            is ApiError -> {

/*                if ((info.cause as? ApiException)?.httpCode == ResponseStatusCode.FORCE_LOGOUT_412.code) {
                    onUnauthorized()
                    return
                }*/

                ErrorDialogFragment.newInstance {
                    titleTextResId = R.string.app_error_request_error
                    descriptionText = info.message!!
                    leftButtonTextResId = R.string.all_ok_button
                }.show(fragmentManager, ApiException::class.java.name)
            }

            is NetworkConnectionError -> {
                ErrorDialogFragment.newInstance {
                    titleTextResId = R.string.app_error_request_error
                    descriptionTextResId = R.string.app_error_description_check_internet_connection
                    leftButtonTextResId = R.string.all_ok_button
                }.show(fragmentManager, NetworkConnectionError::class.java.name)
            }

            is UnknownError -> {
                ErrorDialogFragment.newInstance() {
                    titleTextResId = R.string.app_error_unknown_error
                    descriptionText = info.message ?: ""
                    rightButtonTextResId = R.string.all_ok_button
                }.show(fragmentManager, UnknownError::class.java.name)
            }

/*            is CancellationError -> {
                if (BuildConfig.DEBUG) "DEBUG Job Cancellation (request ${info.request?.let { it::class.simpleName }}, message = ${info.message})".log
            }*/
        }
    }
}