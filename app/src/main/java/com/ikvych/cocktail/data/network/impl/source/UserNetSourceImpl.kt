package com.ikvych.cocktail.data.network.impl.source

import com.ikvych.cocktail.data.network.impl.service.UserApiService
import com.ikvych.cocktail.data.network.impl.source.base.BaseNetSourceImpl
import com.ikvych.cocktail.data.network.model.UserNetModel
import com.ikvych.cocktail.data.network.source.UserNetSource
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class UserNetSourceImpl(
    apiService: UserApiService
) : BaseNetSourceImpl<UserApiService>(apiService),
    UserNetSource {

    override suspend fun getUser(): UserNetModel {
        return performRequest {
            getUser()
        }
    }

    override suspend fun updateUserLogo(avatar: File) {
        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), avatar)
        val body =
            MultipartBody.Part.createFormData("avatar", avatar.name, requestFile)
        performRequest {
            updateUserAvatar(body)
        }
    }

    override suspend fun updateUser(user: UserNetModel) {
        return performRequest {
            updateUser(user)
        }
    }
}
