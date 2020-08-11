package com.ikvych.cocktail.data.network

/**
 * Network Specific constants goes here, such as reqeust header keys, etc.
 */
object Constant {

    object Header {
        const val AUTHORIZATION = "Authorization"
        const val PLATFORM = "Platform"
        const val PLATFORM_VERSION = "PlatformVersion"
        const val X_MOCK_MATCH_REQUEST_BODY = "x-mock-match-request-body"
        const val APPLICATION_VERSION = "Application-Version"
        const val TOKEN_HEADER = "$AUTHORIZATION: token for replace"
    }
}