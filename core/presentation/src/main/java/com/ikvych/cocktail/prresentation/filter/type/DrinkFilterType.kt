package com.ikvych.cocktail.prresentation.filter.type

import com.ikvych.cocktail.prresentation.R

enum class DrinkFilterType(val key: String) {
    CATEGORY("c"),
    ALCOHOL("a"),
    INGREDIENT("i"),
    GLASS("g");

    fun getDrawableId(): Int {
        return when (this) {
            ALCOHOL -> R.drawable.ic_item_drink_filter_alcohol
            CATEGORY -> R.drawable.ic_item_drink_filter_favorite
            INGREDIENT -> R.drawable.ic_item_drink_filter_ingredient
            GLASS -> R.drawable.ic_item_drink_filter_glass
        }
    }
}