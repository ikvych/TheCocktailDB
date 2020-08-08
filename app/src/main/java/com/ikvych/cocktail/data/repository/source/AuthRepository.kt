package com.ikvych.cocktail.data.repository.source

import com.ikvych.cocktail.data.repository.source.base.BaseRepository

interface AuthRepository : BaseRepository {

    /**
     * @return true - if user has already its profile data filled (go to Main)
     * Otherwise user must fill profile data
     */
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(firstName: String, lastName: String, email: String, password: String): Boolean
}