package com.ikvych.cocktail.prresentation.util.comparator

import com.ikvych.cocktail.prresentation.model.cocktail.CocktailModel

class AlcoholCocktailComparator : Comparator<CocktailModel> {

    override fun compare(o1: CocktailModel?, o2: CocktailModel?): Int {
        return o1!!.alcoholType.ordinal.compareTo(o2?.alcoholType!!.ordinal)
    }
}