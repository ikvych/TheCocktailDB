package com.ikvych.cocktail.network.source

import com.ikvych.cocktail.network.source.base.BaseNetSource

interface AuthNetSource: BaseNetSource {

    /**
     * @return login token
     */
    suspend fun signIn(email: String, password: String): String
    suspend fun signUp(firstName: String, lastName: String, email: String, password: String): String
}