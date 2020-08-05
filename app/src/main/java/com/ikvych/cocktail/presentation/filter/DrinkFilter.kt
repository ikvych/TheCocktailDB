package com.ikvych.cocktail.presentation.filter

import com.ikvych.cocktail.presentation.filter.type.DrinkFilterType

interface DrinkFilter {
    val type: DrinkFilterType
    val key: String
}