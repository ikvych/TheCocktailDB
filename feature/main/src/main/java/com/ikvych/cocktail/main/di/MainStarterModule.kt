package com.ikvych.cocktail.main.di

import com.ikvych.cocktail.api.MainStarter
import com.ikvych.cocktail.kodein.createProvider
import com.ikvych.cocktail.main.navigation.MainStarterImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind

val mainStarterModule = Kodein.Module("mainStarterModule") {
    bind<MainStarter>() with createProvider<MainStarterImpl>()
}