package com.ikvych.cocktail.data.network.impl.interceptor

import com.ikvych.cocktail.data.network.Constant
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

internal class PostmanMockInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        return with(original.newBuilder()) {
            if (original.method() != "GET") header(Constant.Header.X_MOCK_MATCH_REQUEST_BODY, "true")
            method(original.method(), original.body())
            chain.proceed(build())
        }
    }
}

