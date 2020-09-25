package com.ikvych.cocktail.impl.interceptor

import com.ikvych.cocktail.network.Constant
import okhttp3.Interceptor
import okhttp3.Response
import org.kodein.di.android.BuildConfig
import java.io.IOException

internal class AppVersionInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        return with(original.newBuilder()) {
            header(Constant.Header.APPLICATION_VERSION, BuildConfig.VERSION_NAME)
            method(original.method, original.body)
            chain.proceed(build())
        }
    }
}