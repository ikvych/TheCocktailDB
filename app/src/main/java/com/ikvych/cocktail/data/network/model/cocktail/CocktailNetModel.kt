package com.ikvych.cocktail.data.network.model.cocktail

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.HashMap


class CocktailNetModel (
    val id: Long = -1L,
    val names: LocalizedStringNetModel = LocalizedStringNetModel(),
    val category: String = "",
    val alcoholType: String = "",
    val glass: String = "",
    val image: String = "",
    val instructions: LocalizedStringNetModel = LocalizedStringNetModel(),
    val ingredientsWithMeasures: Map<String, String> = emptyMap(),
    val date: Date = Date()
)