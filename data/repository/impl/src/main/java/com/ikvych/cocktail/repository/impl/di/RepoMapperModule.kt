package com.ikvych.cocktail.repository.impl.di

import com.ikvych.cocktail.repository.impl.mapper.cocktail.CocktailRepoModelMapper
import com.ikvych.cocktail.repository.impl.mapper.cocktail.LocalizedStringRepoModelMapper
import com.ikvych.cocktail.repository.impl.mapper.notification.NotificationRepoModelMapper
import com.ikvych.cocktail.repository.impl.mapper.user.UserRepoModelMapper
import com.ikvych.cocktail.kodein.createProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind

internal val repoMapperModule = Kodein.Module("repoMapperModule") {
    bind<CocktailRepoModelMapper>() with createProvider()
    bind<LocalizedStringRepoModelMapper>() with createProvider()
    bind<NotificationRepoModelMapper>() with createProvider()
    bind<UserRepoModelMapper>() with createProvider()
}