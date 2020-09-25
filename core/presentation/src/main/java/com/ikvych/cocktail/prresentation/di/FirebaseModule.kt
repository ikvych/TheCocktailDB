package com.ikvych.cocktail.prresentation.di

import com.ikvych.cocktail.prresentation.util.FirebaseHelper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val firebaseModule = Kodein.Module("firebaseModule") {
    bind<FirebaseHelper>() with singleton { FirebaseHelper.getInstance(instance()) }
}