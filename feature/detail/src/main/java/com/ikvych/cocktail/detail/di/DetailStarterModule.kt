package com.ikvych.cocktail.detail.di

import com.ikvych.cocktail.api.DetailStarter
import com.ikvych.cocktail.detail.navigation.DetailStarterImpl
import com.ikvych.cocktail.kodein.createProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind

val detailStarterModule = Kodein.Module("detailStarterModule") {
    bind<DetailStarter>() with createProvider<DetailStarterImpl>()
}