package com.ikvych.cocktail.presentation.filter.type

import com.ikvych.cocktail.util.DRINK_FILTER_ABSENT
import com.ikvych.cocktail.presentation.filter.DrinkFilter

enum class AlcoholDrinkFilter(override val type: DrinkFilterType, override val key: String) : DrinkFilter {
    NONE(DrinkFilterType.ALCOHOL, DRINK_FILTER_ABSENT),
    ALCOHOLIC(DrinkFilterType.ALCOHOL, "Alcoholic"),
    OPTIONAL_ALCOHOL(DrinkFilterType.ALCOHOL, "Optional alcohol"),
    NON_ALCOHOLIC(DrinkFilterType.ALCOHOL, "Non alcoholic")
}