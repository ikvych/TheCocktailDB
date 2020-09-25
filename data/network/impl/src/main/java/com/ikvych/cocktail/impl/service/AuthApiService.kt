package com.ikvych.cocktail.impl.service

import com.google.gson.JsonObject
import com.ikvych.cocktail.data.network.impl.service.base.BaseNetService
import com.ikvych.cocktail.network.model.response.TokenNetModel
import retrofit2.http.*

@JvmSuppressWildcards
interface AuthApiService : BaseNetService{

    @POST("login")
    suspend fun signIn(@Body jsonObject: JsonObject): TokenNetModel

    @POST("register")
    suspend fun logIn(@Body jsonObject: JsonObject): TokenNetModel

}