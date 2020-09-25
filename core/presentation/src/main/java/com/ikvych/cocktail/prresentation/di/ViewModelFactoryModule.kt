package com.ikvych.cocktail.prresentation.di

import androidx.lifecycle.ViewModelProvider
import com.ikvych.cocktail.prresentation.factory.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

/**
 * A [Kodein.Module] which binds [ViewModelProvider.Factory] with its implementation.
 */
internal val viewModelFactoryModule = Kodein.Module(name = "viewModelFactoryModule") {
    bind<ViewModelProvider.Factory>() with provider {
        ViewModelFactory(
            this.dkodein
        )
    }
}