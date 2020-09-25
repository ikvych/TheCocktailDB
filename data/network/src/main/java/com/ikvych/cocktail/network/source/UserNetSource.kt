package com.ikvych.cocktail.network.source

import com.ikvych.cocktail.network.model.UserNetModel
import com.ikvych.cocktail.network.source.base.BaseNetSource
import java.io.File

interface UserNetSource : BaseNetSource {
    suspend fun getUser(): UserNetModel
    suspend fun updateUser(user: UserNetModel)
    suspend fun updateUserLogo(
        avatar: File,
        onProgressChanged: (fraction: Float, progressLength: Long, totalLength: Long) -> Unit = { _, _, _ -> }
    )
}