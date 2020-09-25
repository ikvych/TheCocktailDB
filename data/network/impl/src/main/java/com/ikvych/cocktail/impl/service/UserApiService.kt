package com.ikvych.cocktail.impl.service

import com.ikvych.cocktail.data.network.impl.service.base.BaseNetService
import com.ikvych.cocktail.network.Constant.Header.TOKEN_HEADER
import com.ikvych.cocktail.network.model.UserNetModel
import okhttp3.MultipartBody
import retrofit2.http.*

@JvmSuppressWildcards
interface UserApiService : BaseNetService{

    @Headers(TOKEN_HEADER)
    @GET("users/profile")
    suspend fun getUser(): UserNetModel

    @Headers(TOKEN_HEADER)
    @POST("users/profile")
    suspend fun updateUser(@Body user: UserNetModel)

    @Multipart
    @Headers(TOKEN_HEADER)
    @POST("users/avatar")
    suspend fun updateUserAvatar(@Part avatar: MultipartBody.Part)
}