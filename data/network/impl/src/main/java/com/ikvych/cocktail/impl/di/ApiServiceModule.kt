package com.ikvych.cocktail.impl.di

import com.ikvych.cocktail.impl.service.AuthApiService
import com.ikvych.cocktail.impl.service.CocktailApiService
import com.ikvych.cocktail.impl.service.UserApiService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

internal val apiServiceModule = Kodein.Module("apiServiceModule") {
    import(apiModule)

    singletonApiService<AuthApiService>("userRetrofit")
    singletonApiService<UserApiService>("userRetrofit")
    singletonApiService<CocktailApiService>("cocktailRetrofit")
}

private inline fun <reified T : Any> Kodein.Builder.singletonApiService(retrofitTag: String? = null): Unit =
    bind<T>() with singleton { instance<Retrofit>(retrofitTag).create(T::class.java) }