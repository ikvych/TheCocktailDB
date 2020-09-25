package com.ikvych.cocktail.preference.di

import android.content.Context
import android.content.SharedPreferences
import com.ikvych.cocktail.kodein.createProvider
import com.ikvych.cocktail.locale.source.AppSettingLocalSource
import com.ikvych.cocktail.locale.source.TokenLocalSource
import com.ikvych.cocktail.preference.SharedPrefsHelper
import com.ikvych.cocktail.preference.source.AppSettingLocalSourceImpl
import com.ikvych.cocktail.preference.source.TokenLocalSourceImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val localSourceModule = Kodein.Module("localSourceModule") {
    bind<SharedPreferences>() with singleton {
        instance<Context>()
            .applicationContext
            .getSharedPreferences("sp", Context.MODE_PRIVATE)
    }
    bind<SharedPrefsHelper>() with createProvider()
    bind<AppSettingLocalSource>() with createProvider<AppSettingLocalSourceImpl>()
    bind<TokenLocalSource>() with createProvider<TokenLocalSourceImpl>()
}
