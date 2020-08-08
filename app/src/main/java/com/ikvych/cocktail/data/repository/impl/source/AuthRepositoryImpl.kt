package com.ikvych.cocktail.data.repository.impl.source

import com.ikvych.cocktail.data.db.source.UserDbSource
import com.ikvych.cocktail.data.local.source.TokenLocalSource
import com.ikvych.cocktail.data.network.source.AuthNetSource
import com.ikvych.cocktail.data.network.source.UserNetSource
import com.ikvych.cocktail.data.repository.impl.mapper.user.UserRepoModelMapper
import com.ikvych.cocktail.data.repository.source.AuthRepository

class AuthRepositoryImpl(
    private val authNetSource: AuthNetSource,
    private val userNetSource: UserNetSource,
    private val userDbSource: UserDbSource,
    private val userModelMapper: UserRepoModelMapper,
    private val tokenLocalSource: TokenLocalSource
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Boolean {
        return authNetSource.signIn(email, password)
            .let {
                tokenLocalSource.token = it

                //refresh user
                val user = userNetSource.getUser()
                val userModel = userModelMapper.mapRepoToDb(userModelMapper.mapNetToRepo(user))
                userDbSource.saveUser(userModel)

                tokenLocalSource.token != null
            }
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Boolean {
        return authNetSource.signUp(firstName, lastName, email, password)
            .let {
                tokenLocalSource.token = it

                //refresh user
                userNetSource.getUser()
                    .run { userModelMapper.mapRepoToDb(userModelMapper.mapNetToRepo(this)) }
                    .run { userDbSource.saveUser(this) }

                tokenLocalSource.token != null
            }
    }
}
