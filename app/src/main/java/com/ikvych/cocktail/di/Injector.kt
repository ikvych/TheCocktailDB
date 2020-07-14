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
import com.ikvych.cocktail.data.db.impl.dao.CocktailDao
import com.ikvych.cocktail.data.db.impl.dao.base.BaseDao
import com.ikvych.cocktail.data.db.impl.source.DrinkDbSourceImpl
import com.ikvych.cocktail.data.db.source.BaseDbSource
import com.ikvych.cocktail.data.db.source.DrinkDbSource
import com.ikvych.cocktail.data.network.impl.RetrofitInstance
import com.ikvych.cocktail.data.network.impl.service.CocktailNetService
import com.ikvych.cocktail.data.network.impl.service.base.BaseNetService
import com.ikvych.cocktail.data.network.impl.source.CocktailNetSourceImpl
import com.ikvych.cocktail.data.network.source.BaseNetSource
import com.ikvych.cocktail.data.network.source.CocktailNetSource
import com.ikvych.cocktail.data.repository.impl.mapper.CocktailRepoModelMapper
import com.ikvych.cocktail.data.repository.impl.mapper.LocalizedStringRepoModelMapper
import com.ikvych.cocktail.data.repository.impl.mapper.base.BaseRepoModelMapper
import com.ikvych.cocktail.data.repository.impl.source.CocktailRepositoryImpl
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.data.repository.source.base.BaseRepository
import com.ikvych.cocktail.viewmodel.*
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

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
        defaultArguments: Bundle? = (owner as? Activity)?.intent?.extras ?: (owner as? Fragment)?.arguments
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
                MainActivityViewModel::class.java -> MainActivityViewModel(provideRepository(appContext),application, handle) as T
                SearchActivityViewModel::class.java -> SearchActivityViewModel(provideRepository(appContext),application, handle) as T
                ProfileFragmentViewModel::class.java -> ProfileFragmentViewModel(application, handle) as T
                MainFragmentViewModel::class.java -> MainFragmentViewModel(provideRepository(appContext),application, handle) as T
                BaseViewModel::class.java -> BaseViewModel(application, handle, provideRepository(appContext)) as T
                else -> throw NotImplementedError("Must provide repository for class ${modelClass.simpleName}")
            }
        }
    }

    inline fun <reified T : BaseRepository> provideRepository(context: Context): T {
        return when (T::class.java) {
            CocktailRepository::class.java -> CocktailRepositoryImpl(
                provideDbDataSource(context),
                provideNetDataSource(context),
                provideRepoModelMapper(context)
            ) as T
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        }
    }

    inline fun <reified T: BaseRepoModelMapper<*, *, *>> provideRepoModelMapper(context: Context): T {
        return when (T::class.java) {
            CocktailRepoModelMapper::class.java -> CocktailRepoModelMapper(provideNestedRepoModelMapper(context))
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        } as T
    }

/*    inline fun <reified T: BaseModelMapper<*, *>> provideModelMapper(context: Context): T {
        return when (T::class.java) {
            CocktailModelMapper::class.java -> CocktailModelMapper(provideNestedModelMapper(context))
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        } as T
    }*/

    inline fun <reified T: BaseRepoModelMapper<*, *, *>> provideNestedRepoModelMapper(context: Context): T {
        return when (T::class.java) {
            LocalizedStringRepoModelMapper::class.java -> LocalizedStringRepoModelMapper()
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        } as T

    }

/*    inline fun <reified T: BaseModelMapper<*, *>> provideNestedModelMapper(context: Context): T {
        return when (T::class.java) {
            LocalizedStringModelMapper::class.java -> LocalizedStringModelMapper()
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        } as T

    }*/

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
            CocktailDao::class.java -> DrinkDataBase.getInstance(context)?.drinkDao()
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        } as T
    }

    inline fun <reified T: BaseNetSource> provideNetDataSource(context: Context): T {
        return when (T::class.java) {
//            CocktailDbSource::class.java -> CocktailDbSourceImpl(
//                CocktailAppRoomDatabase.instance(context).cocktailDao()
//            ) as T
            CocktailNetSource::class.java -> CocktailNetSourceImpl(provideNetService(context)) as T
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        }
    }

    inline fun <reified T: BaseNetService> provideNetService(context: Context): T {
        return when (T::class.java) {
            CocktailNetService::class.java -> RetrofitInstance.service
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        } as T
    }
}