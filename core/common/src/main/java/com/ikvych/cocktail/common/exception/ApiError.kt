package com.ikvych.cocktail.common.exception

import java.lang.RuntimeException

data class ApiError (
    /**
     * Called api method.
     */
    val method: String,
    /**
     * Unique Api error code.
     */
    val code: Int,
    /**
     * Api error message.
     */
    val details: String,
    /**
     * HTTP status code.
     */
    val httpCode: Int,
    /**
     * Cause of Exception.
     */
    override val cause: Throwable? = null
) : RuntimeException(
    "Api error: method=[$method], code=[$code], details=[$details]",
    cause
) {

}