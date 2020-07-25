package com.ikvych.cocktail.filter.type

import com.ikvych.cocktail.constant.DRINK_FILTER_ABSENT
import com.ikvych.cocktail.filter.DrinkFilter

enum class CategoryDrinkFilter(override val type: DrinkFilterType, override val key: String) :
    DrinkFilter {
    NONE(DrinkFilterType.CATEGORY, DRINK_FILTER_ABSENT),
    ORDINARY_DRINK(DrinkFilterType.CATEGORY, "Ordinary Drink"),
    COCKTAIL(DrinkFilterType.CATEGORY, "Cocktail"),
    MILK_FLOAT_SHAKE(DrinkFilterType.CATEGORY, "Milk / Float / Shake"),
    OTHER_UNKNOWN(DrinkFilterType.CATEGORY, "Other/Unknown"),
    COCOA(DrinkFilterType.CATEGORY, "Cocoa"),
    SHOT(DrinkFilterType.CATEGORY, "Shot"),
    COFFEE_TEA(DrinkFilterType.CATEGORY, "Coffee / Tea"),
    HOMEMADE_LIQUEUR(DrinkFilterType.CATEGORY, "Homemade Liqueur"),
    PUNCH_PARTY_DRINK(DrinkFilterType.CATEGORY, "Punch / Party Drink"),
    BEER(DrinkFilterType.CATEGORY, "Beer"),
    SOFT_DRINK_SODA(DrinkFilterType.CATEGORY, "Soft Drink / Soda")
}