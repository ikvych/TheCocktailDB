package com.ikvych.cocktail.prresentation.filter

import com.ikvych.cocktail.prresentation.filter.type.DrinkFilterType

interface DrinkFilter {
    val type: DrinkFilterType
    val key: String
}