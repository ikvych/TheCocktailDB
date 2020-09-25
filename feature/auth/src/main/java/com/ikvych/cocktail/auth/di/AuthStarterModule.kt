package com.ikvych.cocktail.auth.di

import com.ikvych.cocktail.api.AuthStarter
import com.ikvych.cocktail.auth.navigation.AuthStarterImpl
import com.ikvych.cocktail.kodein.createProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind

val authStarterModule = Kodein.Module("authStarterModule") {
    bind<AuthStarter>() with createProvider<AuthStarterImpl>()
}