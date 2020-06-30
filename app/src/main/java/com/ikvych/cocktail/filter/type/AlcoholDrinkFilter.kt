package com.ikvych.cocktail.filter.type

import com.ikvych.cocktail.filter.DrinkFilter

enum class AlcoholDrinkFilter(override val type: DrinkFilterType, override val key: String) : DrinkFilter {
    NONE(DrinkFilterType.ALCOHOL, "None"),
    ALCOHOLIC(DrinkFilterType.ALCOHOL, "Alcoholic"),
    NON_ALCOHOLIC(DrinkFilterType.ALCOHOL, "Non alcoholic"),
    OPTIONAL_ALCOHOL(DrinkFilterType.ALCOHOL, "Optional alcohol")
}