package com.ikvych.cocktail.presentation.filter.type

enum class SortDrinkType(val key: String) {
    RECENT("Recent"),
    NAME_ASC("Name ascending"),
    NAME_DESC("Name descending"),
    ALCOHOL_ASC("Alcohol ascending"),
    ALCOHOL_DESC("Alcohol descending"),
    INGREDIENT_COUNT_ASC("Ingredient count ascending"),
    INGREDIENT_COUNT_DESC("Ingredient count descending")
}