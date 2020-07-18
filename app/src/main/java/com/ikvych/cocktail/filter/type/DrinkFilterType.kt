package com.ikvych.cocktail.filter.type

import android.graphics.drawable.Drawable
import com.ikvych.cocktail.R

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
            GLASS -> -R.drawable.ic_item_drink_filter_alcohol
        }
    }

    fun getColorAsString(): String {
        return when (this) {
            ALCOHOL -> "#DCEDC8"
            CATEGORY -> "#BBDEFB"
            INGREDIENT -> "#E0E0E0"
            GLASS -> "#E0E0E0"
        }
    }
}