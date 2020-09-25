package com.ikvych.cocktail.impl.source

import com.google.gson.JsonObject
import com.ikvych.cocktail.impl.service.AuthApiService
import com.ikvych.cocktail.impl.source.base.BaseNetSourceImpl
import com.ikvych.cocktail.network.source.AuthNetSource

class AuthNetSourceImpl(
    apiService: AuthApiService
) : BaseNetSourceImpl<AuthApiService>(apiService),
    AuthNetSource {

    override suspend fun signIn(email: String, password: String): String {
        return performRequest {
            val result = signIn(
                JsonObject().apply {
                    addProperty("email", email)
                    addProperty("password", password)
                }
            ).token
            println()
            result
        }
    }

    override suspend fun signUp(firstName: String, lastName: String, email: String, password: String): String {
        return performRequest {
            val result = logIn(
                JsonObject().apply {
                    addProperty("name", firstName)
                    addProperty("lastName", lastName)
                    addProperty("email", email)
                    addProperty("password", password)
                }
            ).token
            println()
            result
        }
    }
}
