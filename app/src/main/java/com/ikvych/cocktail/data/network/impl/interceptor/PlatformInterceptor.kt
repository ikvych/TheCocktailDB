package com.ikvych.cocktail.data.network.impl.interceptor

import com.ikvych.cocktail.data.network.Constant
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

internal class PlatformInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        return with(original.newBuilder()) {
            header(Constant.Header.PLATFORM, "android")
            method(original.method, original.body)
            chain.proceed(build())
        }
    }
}