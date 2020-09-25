package com.ikvych.cocktail.impl.interceptor

import com.ikvych.cocktail.network.Constant
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

internal class TokenInterceptor(

    private val getToken: () -> String?
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val token = original.header(Constant.Header.AUTHORIZATION)
        val request = original.newBuilder()

        if (token != null) {
            val tokenPref = getToken()
            if (token != tokenPref) {
                request.header(Constant.Header.AUTHORIZATION, tokenPref ?: "")
            }
        }

        request.method(original.method, original.body)
        return chain.proceed(request.build())
    }
}

