package com.ikvych.cocktail.repository.model.user

data class UserRepoModel(
    val id: Long = 1L,
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val avatar: String = ""
)