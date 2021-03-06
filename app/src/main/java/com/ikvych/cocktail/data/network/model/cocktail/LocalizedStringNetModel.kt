package com.ikvych.cocktail.data.network.model.cocktail

import com.google.gson.annotations.SerializedName

class LocalizedStringNetModel(
    val defaultName: String? = null,
    val defaultAlternate: String? = null,
    val es: String? = null,
    val de: String? = null,
    val fr: String? = null,
    val zhHans: String? = null,
    val zhHant: String? = null
)