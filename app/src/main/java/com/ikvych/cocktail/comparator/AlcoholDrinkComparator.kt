package com.ikvych.cocktail.comparator

import com.ikvych.cocktail.data.entity.Drink

class AlcoholDrinkComparator : Comparator<Drink> {

    private val priorityMap: HashMap<String, Int> =
        hashMapOf(Pair("Alcoholic", 0), Pair("Optional alcohol", 1), Pair("Non alcoholic", 2))

    override fun compare(o1: Drink?, o2: Drink?): Int {
        val valO1 = priorityMap[o1!!.getStrAlcoholic()]
        val valO2 = priorityMap[o2!!.getStrAlcoholic()]
        return valO1!!.compareTo(valO2!!)
    }

}