package com.ikvych.cocktail.comparator.type

enum class SortDrinkType(var sortOrder: SortOrder = SortOrder.Descending) {
    RECENT(),
    NAME(),
    ALCOHOL(),
    INGREDIENT_COUNT()
}