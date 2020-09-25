package com.ikvych.cocktail.impl.di

import com.ikvych.cocktail.database.source.DrinkDbSource
import com.ikvych.cocktail.database.source.NotificationDbSource
import com.ikvych.cocktail.database.source.UserDbSource
import com.ikvych.cocktail.impl.source.DrinkDbSourceImpl
import com.ikvych.cocktail.impl.source.NotificationDbSourceImpl
import com.ikvych.cocktail.impl.source.UserDbSourceImpl
import com.ikvych.cocktail.kodein.createProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind

val dbSourceModule = Kodein.Module("dbSourceModule") {
    import(roomModule)

    bind<DrinkDbSource>() with createProvider<DrinkDbSourceImpl>()
    bind<NotificationDbSource>() with createProvider<NotificationDbSourceImpl>()
    bind<UserDbSource>() with createProvider<UserDbSourceImpl>()
}