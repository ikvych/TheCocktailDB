package com.ikvych.cocktail.impl.interceptor

import android.os.Build
import com.ikvych.cocktail.network.Constant
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

internal class PlatformVersionInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        return with(original.newBuilder()) {
            header(Constant.Header.PLATFORM_VERSION, Build.VERSION.RELEASE ?: "API ${Build.VERSION.SDK_INT}")
            method(original.method, original.body)
            chain.proceed(build())
        }
    }
}