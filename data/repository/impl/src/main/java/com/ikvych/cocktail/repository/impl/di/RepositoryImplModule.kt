package com.ikvych.cocktail.repository.impl.di

import com.ikvych.cocktail.kodein.createSingleton
import com.ikvych.cocktail.repository.impl.source.*
import com.ikvych.cocktail.repository.source.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind


val repositoryModule = Kodein.Module("repositoryImplModule") {
    import(repoMapperModule)

    bind<AppSettingRepository>() with createSingleton<AppSettingRepositoryImpl>()
    bind<AuthRepository>() with createSingleton<AuthRepositoryImpl>()
    bind<CocktailRepository>() with createSingleton<CocktailRepositoryImpl>()
    bind<NotificationRepository>() with createSingleton<NotificationRepositoryImpl>()
    bind<TokenRepository>() with createSingleton<TokenRepositoryImpl>()
    bind<UserRepository>() with createSingleton<UserRepositoryImpl>()
}