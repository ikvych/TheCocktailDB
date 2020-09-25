package com.ikvych.cocktail.profile.di

import com.ikvych.cocktail.api.AuthStarter
import com.ikvych.cocktail.api.ProfileStarter
import com.ikvych.cocktail.kodein.createProvider
import com.ikvych.cocktail.profile.navigation.ProfileStarterImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind

val profileStarterModule = Kodein.Module("profileStarterModule") {
    bind<ProfileStarter>() with createProvider<ProfileStarterImpl>()
}