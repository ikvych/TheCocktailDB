package com.ikvych.cocktail.search.di

import com.ikvych.cocktail.api.SearchStarter
import com.ikvych.cocktail.kodein.createProvider
import com.ikvych.cocktail.search.navigation.SearchStarterImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind

val searchStarterModule = Kodein.Module("searchStarterModule") {
    bind<SearchStarter>() with createProvider<SearchStarterImpl>()
}