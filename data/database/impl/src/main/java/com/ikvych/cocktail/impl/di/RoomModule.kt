package com.ikvych.cocktail.impl.di

import android.content.Context
import com.ikvych.cocktail.impl.DrinkDataBase
import com.ikvych.cocktail.impl.dao.CocktailDao
import com.ikvych.cocktail.impl.dao.NotificationDao
import com.ikvych.cocktail.impl.dao.UserDao
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val roomModule = Kodein.Module("roomModule") {
    bind<DrinkDataBase>() with singleton {
        DrinkDataBase.getInstance(instance<Context>())
    }
    bind<UserDao>() with singleton {
        instance<DrinkDataBase>().userDao()
    }
    bind<NotificationDao>() with singleton {
        instance<DrinkDataBase>().notificationDao()
    }
    bind<CocktailDao>() with singleton {
        instance<DrinkDataBase>().drinkDao()
    }
}