package com.ikvych.cocktail.prresentation.di

import com.ikvych.cocktail.kodein.createProvider
import com.ikvych.cocktail.prresentation.mapper.cocktail.CocktailModelMapper
import com.ikvych.cocktail.prresentation.mapper.cocktail.LocalizedStringModelMapper
import com.ikvych.cocktail.prresentation.mapper.user.UserModelMapper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind

val mapperModule = Kodein.Module("mapperModule") {
    bind<CocktailModelMapper>() with createProvider()
    bind<LocalizedStringModelMapper>() with createProvider()
/*    bind<com.ikvych.cocktail.firebase.firebase.mapper.NotificationModelMapper>() with createProvider()*/
    bind<UserModelMapper>() with createProvider()
}