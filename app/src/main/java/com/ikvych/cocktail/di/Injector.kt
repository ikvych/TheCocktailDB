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
import com.google.gson.GsonBuilder
import com.ikvych.cocktail.data.db.impl.DrinkDataBase
import com.ikvych.cocktail.data.db.impl.dao.CocktailDao
import com.ikvych.cocktail.data.db.impl.dao.UserDao
import com.ikvych.cocktail.data.db.impl.dao.base.BaseDao
import com.ikvych.cocktail.data.db.impl.source.DrinkDbSourceImpl
import com.ikvych.cocktail.data.db.impl.source.UserDbSourceImpl
import com.ikvych.cocktail.data.db.source.base.BaseDbSource
import com.ikvych.cocktail.data.db.source.DrinkDbSource
import com.ikvych.cocktail.data.db.source.UserDbSource
import com.ikvych.cocktail.data.local.impl.SharedPrefsHelper
import com.ikvych.cocktail.data.local.impl.source.TokenLocalSourceImpl
import com.ikvych.cocktail.data.local.source.TokenLocalSource
import com.ikvych.cocktail.data.network.impl.deserializer.BooleanDeserializer
import com.ikvych.cocktail.data.network.impl.deserializer.Iso8601DateDeserializer
import com.ikvych.cocktail.data.network.impl.deserializer.model.CocktailNetModelDeserializer
import com.ikvych.cocktail.data.network.impl.interceptor.AppVersionInterceptor
import com.ikvych.cocktail.data.network.impl.interceptor.PlatformInterceptor
import com.ikvych.cocktail.data.network.impl.interceptor.PlatformVersionInterceptor
import com.ikvych.cocktail.data.network.impl.interceptor.PostmanMockInterceptor
import com.ikvych.cocktail.data.network.impl.interceptor.TokenInterceptor
import com.ikvych.cocktail.data.network.impl.service.AuthApiService
import com.ikvych.cocktail.data.network.impl.service.CocktailApiService
import com.ikvych.cocktail.data.network.impl.service.UserApiService
import com.ikvych.cocktail.data.network.impl.service.base.BaseNetService
import com.ikvych.cocktail.data.network.impl.source.AuthNetSourceImpl
import com.ikvych.cocktail.data.network.impl.source.CocktailNetSourceImpl
import com.ikvych.cocktail.data.network.impl.source.UserNetSourceImpl
import com.ikvych.cocktail.data.network.model.cocktail.CocktailNetResponse
import com.ikvych.cocktail.data.network.source.AuthNetSource
import com.ikvych.cocktail.data.network.source.base.BaseNetSource
import com.ikvych.cocktail.data.network.source.CocktailNetSource
import com.ikvych.cocktail.data.network.source.UserNetSource
import com.ikvych.cocktail.data.repository.impl.mapper.cocktail.CocktailRepoModelMapper
import com.ikvych.cocktail.data.repository.impl.mapper.cocktail.LocalizedStringRepoModelMapper
import com.ikvych.cocktail.data.repository.impl.mapper.base.BaseRepoModelMapper
import com.ikvych.cocktail.data.repository.impl.mapper.user.UserRepoModelMapper
import com.ikvych.cocktail.data.repository.impl.source.AuthRepositoryImpl
import com.ikvych.cocktail.data.repository.impl.source.CocktailRepositoryImpl
import com.ikvych.cocktail.data.repository.impl.source.TokenRepositoryImpl
import com.ikvych.cocktail.data.repository.impl.source.UserRepositoryImpl
import com.ikvych.cocktail.data.repository.source.AuthRepository
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.data.repository.source.TokenRepository
import com.ikvych.cocktail.data.repository.source.UserRepository
import com.ikvych.cocktail.data.repository.source.base.BaseRepository
import com.ikvych.cocktail.presentation.mapper.cocktail.CocktailModelMapper
import com.ikvych.cocktail.presentation.mapper.cocktail.LocalizedStringModelMapper
import com.ikvych.cocktail.presentation.mapper.base.BaseModelMapper
import com.ikvych.cocktail.presentation.mapper.user.UserModelMapper
import com.ikvych.cocktail.viewmodel.*
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import io.devlight.data.network.impl.extension.deserializeType
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

object Injector {
    private lateinit var appContext: Context

    private val baseGsonBuilder: GsonBuilder
        get() = GsonBuilder()
            .registerTypeAdapter(deserializeType<Boolean>(), BooleanDeserializer(false))
            .registerTypeAdapter(deserializeType<Date>(), Iso8601DateDeserializer())
            .registerTypeAdapter(
                deserializeType<CocktailNetResponse>(),
                CocktailNetModelDeserializer()
            )
            .setPrettyPrinting()
            .serializeNulls()

    val cocktailRetrofit by lazy {
        provideRetrofit(
            appContext,
            "https://www.thecocktaildb.com/",
            setOf(),
            setOf(
                GsonConverterFactory.create(baseGsonBuilder.create())
            ),
            provideOkHttpClientBuilder(),
            *arrayOf()
        )
    }

    val devSchoolRetrofit by lazy {

        val provideRepository = provideRepository(appContext, TokenRepository::class.java)

        provideRetrofit(
            appContext,
            "https://devlightschool.ew.r.appspot.com/",
            setOf(),
            setOf(
                GsonConverterFactory.create(baseGsonBuilder.create())
            ),
            provideOkHttpClientBuilder(),
            *arrayOf(
                TokenInterceptor { provideRepository.token },
                AppVersionInterceptor(),
                PlatformInterceptor(),
                PlatformVersionInterceptor()
            )
        )
    }

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
        defaultArguments: Bundle? = (owner as? Activity)?.intent?.extras
            ?: (owner as? Fragment)?.arguments
    ) : AbstractSavedStateViewModelFactory(
        owner,
        defaultArguments
    ) {

        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            return when (modelClass) {
                MainActivityViewModel::class.java -> MainActivityViewModel(
                    provideRepository(appContext, CocktailRepository::class.java), provideModelMapper(
                        appContext
                    ), application, handle
                ) as T
                SearchActivityViewModel::class.java -> SearchActivityViewModel(
                    provideRepository(appContext, CocktailRepository::class.java), provideModelMapper(
                        appContext
                    ), application, handle
                ) as T
                ProfileActivityViewModel::class.java -> ProfileActivityViewModel(
                    application,
                    handle,
                    provideRepository(appContext, AuthRepository::class.java),
                    provideRepository(appContext, UserRepository::class.java),
                    provideModelMapper(appContext),
                    provideRepository(appContext, TokenRepository::class.java)
                ) as T
                MainFragmentViewModel::class.java -> MainFragmentViewModel(
                    provideRepository(appContext, CocktailRepository::class.java), provideModelMapper(
                        appContext
                    ), application, handle
                ) as T
                DrinkViewModel::class.java -> DrinkViewModel(
                    application, handle, provideRepository(appContext, CocktailRepository::class.java), provideModelMapper(
                        appContext
                    )
                ) as T
                BaseViewModel::class.java -> BaseViewModel(
                    application, handle
                ) as T
                SignInViewModel::class.java -> SignInViewModel(
                    application,
                    handle,
                    provideRepository(appContext, AuthRepository::class.java),
                    provideRepository(appContext, UserRepository::class.java),
                    provideRepository(
                        appContext, TokenRepository::class.java
                    )
                ) as T
                SignUpViewModel::class.java -> SignUpViewModel(
                    application,
                    handle,
                    provideRepository(appContext, AuthRepository::class.java),
                    provideRepository(appContext, UserRepository::class.java),
                    provideRepository(
                        appContext, TokenRepository::class.java
                    )
                ) as T
                EditProfileViewModel::class.java -> EditProfileViewModel(
                    application,
                    handle,
                    provideRepository(appContext, AuthRepository::class.java),
                    provideRepository(appContext, UserRepository::class.java),
                    provideRepository(
                        appContext, TokenRepository::class.java
                    ),
                    provideModelMapper(appContext)
                ) as T
                else -> throw NotImplementedError("Must provide repository for class ${modelClass.simpleName}")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : BaseRepository> provideRepository(context: Context, clazz: Class<T>): T {
        return when (clazz) {
            CocktailRepository::class.java -> CocktailRepositoryImpl(
                provideDbDataSource(context),
                provideNetDataSource(context),
                provideRepoModelMapper(context)
            ) as T
            UserRepository::class.java -> UserRepositoryImpl(
                provideDbDataSource(context),
                provideNetDataSource(context),
                provideRepoModelMapper(context)
            ) as T
            AuthRepository::class.java -> AuthRepositoryImpl(
                provideNetDataSource(context),
                provideNetDataSource(context),
                provideDbDataSource(context),
                provideRepoModelMapper(context),
                provideLocalDataSource(context)
            ) as T
            TokenRepository::class.java -> TokenRepositoryImpl(
                provideLocalDataSource(context)
            ) as T
            else -> throw IllegalStateException("Must provide repository for class ${clazz.simpleName}")
        }
    }

    inline fun <reified T> provideLocalDataSource(context: Context): T {
        "LOG provideLocalDataSource class = ${T::class.java.simpleName}"
        return when (T::class.java) {
            TokenLocalSource::class.java -> TokenLocalSourceImpl(
                SharedPrefsHelper(
                    context.getSharedPreferences(
                        "sp",
                        Context.MODE_PRIVATE
                    )
                )
            ) as T
            else -> throw IllegalStateException("Must provide LocalDataSource for class ${T::class.java.simpleName}")
        }
    }

    inline fun <reified T : BaseRepoModelMapper<*, *, *>> provideRepoModelMapper(context: Context): T {
        return when (T::class.java) {
            CocktailRepoModelMapper::class.java -> CocktailRepoModelMapper(
                provideNestedRepoModelMapper(context)
            )
            UserRepoModelMapper::class.java -> UserRepoModelMapper()
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        } as T
    }

    inline fun <reified T : BaseModelMapper<*, *>> provideModelMapper(context: Context): T {
        return when (T::class.java) {
            CocktailModelMapper::class.java -> CocktailModelMapper(
                provideNestedModelMapper(context)
            )
            UserModelMapper::class.java -> UserModelMapper()
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        } as T
    }

    inline fun <reified T : BaseRepoModelMapper<*, *, *>> provideNestedRepoModelMapper(context: Context): T {
        return when (T::class.java) {
            LocalizedStringRepoModelMapper::class.java -> LocalizedStringRepoModelMapper()
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        } as T

    }

    inline fun <reified T : BaseModelMapper<*, *>> provideNestedModelMapper(context: Context): T {
        return when (T::class.java) {
            LocalizedStringModelMapper::class.java -> LocalizedStringModelMapper()
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        } as T

    }

    inline fun <reified T : BaseDbSource> provideDbDataSource(context: Context): T {
        return when (T::class.java) {
//            CocktailDbSource::class.java -> CocktailDbSourceImpl(
//                CocktailAppRoomDatabase.instance(context).cocktailDao()
//            ) as T
            DrinkDbSource::class.java -> DrinkDbSourceImpl(provideDao(context)) as T
            UserDbSource::class.java -> UserDbSourceImpl(provideDao(context)) as T
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        }
    }

    inline fun <reified T : BaseDao> provideDao(context: Context): T {
        return when (T::class.java) {
            CocktailDao::class.java -> DrinkDataBase.getInstance(context)?.drinkDao()
            UserDao::class.java -> DrinkDataBase.getInstance(context)?.userDao()
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        } as T
    }

    inline fun <reified T : BaseNetSource> provideNetDataSource(context: Context): T {
        return when (T::class.java) {
//            CocktailDbSource::class.java -> CocktailDbSourceImpl(
//                CocktailAppRoomDatabase.instance(context).cocktailDao()
//            ) as T
            CocktailNetSource::class.java -> CocktailNetSourceImpl(provideNetService()) as T
            UserNetSource::class.java -> UserNetSourceImpl(context, provideNetService()) as T
            AuthNetSource::class.java -> AuthNetSourceImpl(provideNetService()) as T
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        }
    }

    inline fun <reified T : BaseNetService> provideNetService(): T {
        return when (T::class.java) {
            CocktailApiService::class.java -> cocktailRetrofit.create(T::class.java) as T
            UserApiService::class.java -> devSchoolRetrofit.create(T::class.java) as T
            AuthApiService::class.java -> devSchoolRetrofit.create(T::class.java) as T
            else -> throw IllegalStateException("Must provide repository for class ${T::class.java.simpleName}")
        } as T
    }

    private fun provideRetrofit(
        context: Context,
        hostName: String,
        callAdapterFactories: Set<CallAdapter.Factory> = setOf(),
        converterFactories: Set<Converter.Factory> = setOf(),
        okHttpClientBuilder: OkHttpClient.Builder,
        vararg interceptors: Interceptor
    ): Retrofit {
        interceptors.forEach { okHttpClientBuilder.addInterceptor(it) }
        configureOkHttpInterceptors(context, okHttpClientBuilder)

        val builder = Retrofit.Builder()

        callAdapterFactories.forEach {
            builder.addCallAdapterFactory(it)
        }

        converterFactories.forEach { builder.addConverterFactory(it) }

        builder
            .client(okHttpClientBuilder.build())
            .baseUrl(hostName)

        return builder.build()
    }

    internal fun configureOkHttpInterceptors(
        context: Context,
        okHttpClientBuilder: OkHttpClient.Builder
    ) {

//        // OkHttp Logger
//        val logger = HttpLoggingInterceptor()
//        logger.level = HttpLoggingInterceptor.Level.BODY
//        okHttpClientBuilder.addInterceptor(logger)

        // Postman Mock
        okHttpClientBuilder.addInterceptor(PostmanMockInterceptor())

//        // Gander
//        okHttpClientBuilder.addInterceptor(
//            GanderInterceptor(context).apply {
//                showNotification(true)
//            }
//        )
    }

    private fun provideOkHttpClientBuilder(
        readTimeoutSeconds: Long = 120,
        writeTimeoutSeconds: Long = 120
    ): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        try {
            val trustAllCerts = arrayOf(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) = Unit

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) = Unit

                    override fun getAcceptedIssuers(): Array<X509Certificate?> = emptyArray()
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("TLS"/*TlsVersion.TLS_1_3.javaName*//*"SSL"*/)
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            builder.sslSocketFactory(sslSocketFactory, trustAllCerts.first() as X509TrustManager)

        } catch (e: Exception) { // should never happen
            e.printStackTrace()
        }

        return builder
            .hostnameVerifier(HostnameVerifier { _, _ -> true })
            .readTimeout(readTimeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(writeTimeoutSeconds, TimeUnit.SECONDS)
    }

}