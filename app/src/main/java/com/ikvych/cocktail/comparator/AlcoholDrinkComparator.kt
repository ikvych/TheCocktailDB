package com.ikvych.cocktail.comparator

import com.ikvych.cocktail.data.network.model.Drink
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel

class AlcoholCocktailComparator : Comparator<CocktailModel> {

    override fun compare(o1: CocktailModel?, o2: CocktailModel?): Int {
        return o1!!.alcoholType.ordinal.compareTo(o2?.alcoholType!!.ordinal)
    }


}