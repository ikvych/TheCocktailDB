package com.ikvych.cocktail.data.repository.impl.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.local.source.TokenLocalSource
import com.ikvych.cocktail.data.repository.impl.source.base.BaseRepositoryImpl
import com.ikvych.cocktail.data.repository.source.TokenRepository

class TokenRepositoryImpl(
    private val tokenLocalSource: TokenLocalSource
) : BaseRepositoryImpl(), TokenRepository {

    override val tokenLiveData: LiveData<String?> = tokenLocalSource.tokenLiveData
    override var token: String?
        get() = tokenLocalSource.token
        set(value) {
            tokenLocalSource.token = value
        }

}