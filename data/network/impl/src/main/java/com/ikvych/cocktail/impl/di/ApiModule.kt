package com.ikvych.cocktail.impl.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ikvych.cocktail.impl.deserializer.BooleanDeserializer
import com.ikvych.cocktail.impl.deserializer.Iso8601DateDeserializer
import com.ikvych.cocktail.impl.deserializer.model.CocktailNetModelDeserializer
import com.ikvych.cocktail.kodein.createSingleton
import com.ikvych.cocktail.network.model.cocktail.CocktailNetModel
import com.ikvych.cocktail.impl.extension.deserializeType
import com.ikvych.cocktail.impl.interceptor.AppVersionInterceptor
import com.ikvych.cocktail.impl.interceptor.PlatformInterceptor
import com.ikvych.cocktail.impl.interceptor.PlatformVersionInterceptor
import com.ikvych.cocktail.impl.interceptor.PostmanMockInterceptor
import com.ikvych.cocktail.impl.interceptor.TokenInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
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

internal val apiModule = Kodein.Module("apiModule") {
    import(interceptorModule)

    bind<Gson>() with singleton { provideGson() }
    bind<OkHttpClient.Builder>() with singleton { provideOkHttpClientBuilder() }
    bind<Retrofit>("cocktailRetrofit") with singleton {
        provideRetrofit(
            context = instance(),
            hostName = "https://www.thecocktaildb.com/",
            callAdapterFactories = setOf(),
            converterFactories = setOf(
                GsonConverterFactory.create(instance())
            ),
            okHttpClientBuilder = instance(),
            interceptors = *arrayOf()
        )
    }
    bind<Retrofit>("userRetrofit") with singleton {
        provideRetrofit(
            context = instance(),
            hostName = "https://devlightschool.ew.r.appspot.com/",
            callAdapterFactories = setOf(),
            converterFactories = setOf(
                GsonConverterFactory.create(instance())
            ),
            okHttpClientBuilder = provideOkHttpClientBuilder(),
            interceptors = *arrayOf(
                instance<TokenInterceptor>(),
                instance<AppVersionInterceptor>(),
                instance<PlatformInterceptor>(),
                instance<PlatformVersionInterceptor>()
            )
        )
    }
}

private val interceptorModule = Kodein.Module("interceptorModule") {
    bind<AppVersionInterceptor>() with createSingleton()
    bind<PlatformInterceptor>() with createSingleton()
    bind<PlatformVersionInterceptor>() with createSingleton()
    bind<PostmanMockInterceptor>() with createSingleton()
    bind<TokenInterceptor>() with singleton {
        TokenInterceptor {
            /*instance<TokenRepository>().token*/""
        }
    }
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

private fun provideGson(): Gson {
    return GsonBuilder()
        .registerTypeAdapter(deserializeType<Boolean>(), BooleanDeserializer(false))
        .registerTypeAdapter(deserializeType<Date>(), Iso8601DateDeserializer())
        .registerTypeAdapter(
            deserializeType<CocktailNetModel>(),
            CocktailNetModelDeserializer()
        )
        .setPrettyPrinting()
        .serializeNulls()
        .create()
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