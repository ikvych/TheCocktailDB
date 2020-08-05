package com.ikvych.cocktail.data.network.source

import com.google.gson.JsonObject
import com.ikvych.cocktail.data.network.source.base.BaseNetSource

interface AuthNetSource: BaseNetSource {

    /**
     * @return login token
     */
    suspend fun signIn(email: String, password: String): String
    suspend fun logIn(firstName: String, lastName: String, email: String, password: String): String
}