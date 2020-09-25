package com.ikvych.cocktail.impl.di

import com.ikvych.cocktail.impl.source.AuthNetSourceImpl
import com.ikvych.cocktail.impl.source.CocktailNetSourceImpl
import com.ikvych.cocktail.impl.source.UserNetSourceImpl
import com.ikvych.cocktail.kodein.createProvider
import com.ikvych.cocktail.network.source.AuthNetSource
import com.ikvych.cocktail.network.source.CocktailNetSource
import com.ikvych.cocktail.network.source.UserNetSource
import org.kodein.di.Kodein
import org.kodein.di.generic.bind

val netSourceModule = Kodein.Module("netSourceModule") {
    import(apiServiceModule)

    bind<AuthNetSource>() with createProvider<AuthNetSourceImpl>()
    bind<CocktailNetSource>() with createProvider<CocktailNetSourceImpl>()
    bind<UserNetSource>() with createProvider<UserNetSourceImpl>()
}