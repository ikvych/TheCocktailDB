package com.ikvych.cocktail.filter

import com.ikvych.cocktail.filter.type.DrinkFilterType

interface DrinkFilter {
    val type: DrinkFilterType
    val key: String
}