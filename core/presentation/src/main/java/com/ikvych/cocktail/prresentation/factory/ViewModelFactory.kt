package com.ikvych.cocktail.prresentation.factory

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.ikvych.cocktail.kodein.createInstance
import org.kodein.di.DKodein

/**
 * General implementation of [ViewModelProvider.Factory] for all ViewModels
 * which creates a new instance of ViewModel using [createInstance] method.
 *
 * Allows to have custom constructor parameters in ViewModel
 * without creating bunch of [ViewModelProvider.Factory] for such a ViewModel.
 */
internal class ViewModelFactory(private val dKodein: DKodein) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        dKodein.createInstance(modelClass.kotlin)
}

internal class ViewModelSaveStateFactory(
    private val dKodein: DKodein,
    owner: SavedStateRegistryOwner,
    defaultArguments: Bundle? = (owner as? Activity)?.intent?.extras
        ?: (owner as? Fragment)?.arguments
) : AbstractSavedStateViewModelFactory(
    owner,
    defaultArguments
) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return dKodein.createInstance(modelClass.kotlin)
    }

}