package com.ikvych.cocktail.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.ikvych.cocktail.data.db.impl.DrinkDataBase
import com.ikvych.cocktail.data.db.impl.dao.DrinkDao
import com.ikvych.cocktail.data.db.impl.dao.base.BaseDao
import com.ikvych.cocktail.data.db.impl.source.DrinkDbSourceImpl
import com.ikvych.cocktail.data.db.source.BaseDbSource
import com.ikvych.cocktail.data.db.source.DrinkDbSource
import com.ikvych.cocktail.data.repository.impl.source.DrinkRepositoryImpl
import com.ikvych.cocktail.data.repository.source.DrinkRepository
import com.ikvych.cocktail.data.repository.source.base.BaseRepository
import com.ikvych.cocktail.viewmodel.AuthViewModel
import com.ikvych.cocktail.viewmodel.DrinkDetailViewModel
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import com.ikvych.cocktail.viewmodel.SearchActivityViewModel

object Injector {
    private lateinit var appContext: Context

    fun init(applicationContext: Context) {
        require(applicationContext is Application) { "Context must be application" }
        appContext = applicationContext
        // init DrinkDataBase
        DrinkDataBase.getInstance(applicationContext)
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(
        val application: Application,
        owner: SavedStateRegistryOwner,
        defaultArguments: Bundle? = (owner as? Activity)?.intent?.extras ?: (owner as? Fragment)?.requireArguments()
    ) : AbstractSavedStateViewModelFactory(
        owner,
        defaultArguments
    ) {

        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            return when(modelClass) {
                AuthViewModel::class.java -> AuthViewModel(application, handle) as T
                DrinkDetailViewModel::class.java -> DrinkDetailViewModel(provideRepository(appContext), application, handle) as T
                MainActivityViewModel::class.java -> MainActivityViewModel(application, handle) as T
                SearchActivityViewModel::class.java -> SearchActivityViewModel(application, handle) as T
                else -> throw NotImplementedError("Must provide repository for class ${modelClass.simpleName}")
            }
        }
    }

    inline fun <reified T : BaseRepository> provideRepository(context: Context): T {
        return when (T::class.java) {
            DrinkRepository::class.java -> DrinkRepositoryImpl(
                provideDbDataSource(context)
            ) as T
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        }
    }

    inline fun <reified T: BaseDbSource> provideDbDataSource(context: Context): T {
        return when (T::class.java) {
//            CocktailDbSource::class.java -> CocktailDbSourceImpl(
//                CocktailAppRoomDatabase.instance(context).cocktailDao()
//            ) as T
            DrinkDbSource::class.java -> DrinkDbSourceImpl(provideDao(context)) as T
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        }
    }

    inline fun <reified T: BaseDao> provideDao(context: Context): T {
        return when (T::class.java) {
            DrinkDao::class.java -> DrinkDataBase.getInstance(context)?.drinkDao()
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        } as T
    }
}