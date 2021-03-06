package com.ikvych.cocktail.data.network.impl.source

import android.content.Context
import androidx.core.net.toUri
import com.ikvych.cocktail.data.network.impl.requestbody.progress.UploadProgressRequestBody.Companion.asProgressRequestBody
import com.ikvych.cocktail.data.network.impl.service.UserApiService
import com.ikvych.cocktail.data.network.impl.source.base.BaseNetSourceImpl
import com.ikvych.cocktail.data.network.model.UserNetModel
import com.ikvych.cocktail.data.network.source.UserNetSource
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class UserNetSourceImpl(
    private val context: Context,
    apiService: UserApiService
) : BaseNetSourceImpl<UserApiService>(apiService),
    UserNetSource {

    override suspend fun getUser(): UserNetModel {
        return performRequest {
            getUser()
        }
    }

    override suspend fun updateUserLogo(
        avatar: File,
        onProgressChanged: (fraction: Float, progressLength: Long, totalLength: Long) -> Unit
    ) {
/*        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), avatar)*/
        val mediaType = context.contentResolver.getType(avatar.toUri())?.toMediaTypeOrNull()
        val body =
            MultipartBody.Part.createFormData(
                "avatar",
                avatar.name,
                avatar.asProgressRequestBody(mediaType, onProgressChanged)
            )
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
