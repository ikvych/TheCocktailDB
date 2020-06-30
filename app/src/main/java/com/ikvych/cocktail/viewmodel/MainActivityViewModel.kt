package com.ikvych.cocktail.viewmodel

import android.app.Application
import android.text.SpannableString
import android.text.style.ImageSpan
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.comparator.AlcoholDrinkComparator
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class MainActivityViewModel(
    application: Application
) : BaseViewModel(application) {

    val timer: MutableLiveData<Long> = MutableLiveData()
    private val alcoholComparator: AlcoholDrinkComparator = AlcoholDrinkComparator()
    private val drinksLiveData: LiveData<List<Drink>> = drinkRepository.getDrinks()
    val navBarTitleVisibilityLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val filtersLiveData: MutableLiveData<HashMap<DrinkFilterType, DrinkFilter>> =
        MutableLiveData<HashMap<DrinkFilterType, DrinkFilter>>()

    val lastAppliedFiltersLiveData: MutableLiveData<HashMap<DrinkFilterType, DrinkFilter>> =
        MutableLiveData<HashMap<DrinkFilterType, DrinkFilter>>()

    init {
        resetFilters()
    }

    val sortLiveData: MutableLiveData<SortDrinkType> =
        MutableLiveData<SortDrinkType>()

    init {
        sortLiveData.value = SortDrinkType.RECENT
    }

    val alcoholFilterLiveData: MutableLiveData<AlcoholDrinkFilter> =
        object : MediatorLiveData<AlcoholDrinkFilter>() {
            init {
                value = AlcoholDrinkFilter.NONE
                addSource(filtersLiveData) {
                    value = it[DrinkFilterType.ALCOHOL] as AlcoholDrinkFilter
                }
            }
        }
    val categoryFilterLiveData: MutableLiveData<CategoryDrinkFilter> =
        object : MediatorLiveData<CategoryDrinkFilter>() {
            init {
                value = CategoryDrinkFilter.NONE
                addSource(filtersLiveData) {
                    value = it[DrinkFilterType.CATEGORY] as CategoryDrinkFilter
                }
            }
        }
    val ingredientFilterLiveData: MutableLiveData<IngredientDrinkFilter> =
        object : MediatorLiveData<IngredientDrinkFilter>() {
            init {
                value = IngredientDrinkFilter.NONE
                addSource(filtersLiveData) {
                    value = it[DrinkFilterType.INGREDIENT] as IngredientDrinkFilter
                }
            }
        }


    fun isFiltersPresent(): Boolean {
        filtersLiveData.value!!.forEach {
            if (it.value.key != "None") {
                return true
            }
        }
        return false
    }

    fun resetFilters() {
        filtersLiveData.value = hashMapOf(
            Pair(DrinkFilterType.ALCOHOL, AlcoholDrinkFilter.NONE),
            Pair(DrinkFilterType.CATEGORY, CategoryDrinkFilter.NONE),
            Pair(DrinkFilterType.INGREDIENT, IngredientDrinkFilter.NONE)
        )
        lastAppliedFiltersLiveData.value = hashMapOf(
            Pair(DrinkFilterType.ALCOHOL, AlcoholDrinkFilter.NONE),
            Pair(DrinkFilterType.CATEGORY, CategoryDrinkFilter.NONE),
            Pair(DrinkFilterType.INGREDIENT, IngredientDrinkFilter.NONE)
        )
    }

    fun isUndoEnabled(): Boolean {
        lastAppliedFiltersLiveData.value!!.forEach {
            if (it.value != filtersLiveData.value!![it.key]) {
                return true
            }
        }
        return false
    }

    val filteredDrinksLiveData: MutableLiveData<List<Drink>> =
        object : MediatorLiveData<List<Drink>>() {
            init {
                value = arrayListOf()
                addSource(drinksLiveData) {
                    filterAndSortData(drinksLiveData.value)
                }
                addSource(filtersLiveData) {
                    filterAndSortData(drinksLiveData.value)
                }
                addSource(sortLiveData) {
                    filterAndSortData(drinksLiveData.value)
                }
            }

            private fun filterAndSortData(drinks: List<Drink>?) {
                val drinksCopy = drinks ?: arrayListOf()

                val filteredData =
                    filterData(drinksCopy, filtersLiveData.value!!.values.toList() as ArrayList)
                val sortedData = sortData(filteredData, sortLiveData.value!!)
                value = sortedData
            }
        }

    val filteredFavoriteDrinksLiveData: MutableLiveData<ArrayList<Drink>> =
        object : MediatorLiveData<ArrayList<Drink>>() {
            init {
                if (value == null) {
                    value = arrayListOf()
                }
                addSource(filteredDrinksLiveData) {
                    val resultList = favoriteFilter(it)
                    value = resultList
                }
            }

            fun favoriteFilter(drinks: List<Drink>): ArrayList<Drink> {
                var drinksCopy = drinks
                drinksCopy = drinksCopy.filter {
                    it.isFavorite()
                }
                return drinksCopy as ArrayList<Drink>
            }
        }


    val generalFilteredLiveData: LiveData<SpannableString> =
        object : MediatorLiveData<SpannableString>() {
            init {
                val src = "Знайдено: h @, f %"
                val historyIndexDrw = src.indexOf("@")
                val favoriteIndexDrw = src.indexOf("%")
                val historyDrawable = ContextCompat.getDrawable(
                    application,
                    com.ikvych.cocktail.R.drawable.ic_drink_history
                )
                val favoriteDrawable = ContextCompat.getDrawable(
                    application,
                    com.ikvych.cocktail.R.drawable.ic_drink_favorite
                )
                historyDrawable!!.setBounds(0, 0, 30, 30)
                favoriteDrawable!!.setBounds(0, 0, 30, 30)

                val emptySearch = "Нічого не знайдено"

                addSource(filteredDrinksLiveData) {
                    if (isFiltersPresent() && it.isEmpty() && filteredFavoriteDrinksLiveData.value!!.isEmpty()) {
                        value = SpannableString(emptySearch)
                    } else {
                        var currentResult = src.replace("h", it.size.toString())
                        currentResult =
                            currentResult.replace(
                                "f",
                                filteredFavoriteDrinksLiveData.value!!.size.toString()
                            )
                        val currentSpannable = SpannableString(currentResult)
                        currentSpannable.setSpan(
                            ImageSpan(historyDrawable),
                            historyIndexDrw,
                            historyIndexDrw + 1,
                            ImageSpan.ALIGN_BASELINE
                        )
                        currentSpannable.setSpan(
                            ImageSpan(favoriteDrawable),
                            favoriteIndexDrw,
                            favoriteIndexDrw + 1,
                            ImageSpan.ALIGN_BASELINE
                        )
                        value = currentSpannable
                    }
                }
            }
        }


    override fun getCurrentData(): List<Drink> {
        val drinkApiService = drinksLiveData.value
        return drinkApiService ?: emptyList()
    }

    fun getFavoriteCurrentData(): List<Drink> {
        return filteredFavoriteDrinksLiveData.value ?: emptyList()
    }

    fun findDrinkById(drinkId: Long): Drink {
        return drinkRepository.findDrinkById(drinkId)
    }

    fun findDrinkByName(drinkName: String): Drink {
        return drinkRepository.findDrinkByName(drinkName)
    }

    override fun saveDrink(drink: Drink) {
        drinkRepository.saveDrink(drink)
    }

    override fun getLiveData(): LiveData<List<Drink>> {
        return drinksLiveData
    }

    override fun getAllIngredient(): List<Ingredient> {
        val ingredientList: List<Ingredient> = drinkRepository.getAllIngredient()
        if (ingredientList.isEmpty()) {
            drinkRepository.initAllIngredient()
        }
        return drinkRepository.getAllIngredient()
    }


    fun filterData(drinks: List<Drink>, drinkFilters: ArrayList<DrinkFilter>): List<Drink> {
        var drinksCopy = drinks
        drinkFilters.forEach {
            when (it.type) {
                DrinkFilterType.ALCOHOL -> {
                    if (it != AlcoholDrinkFilter.NONE) {
                        drinksCopy = drinksCopy.filter { drink ->
                            drink.getStrAlcoholic() == it.key
                        }
                    }
                }
                DrinkFilterType.CATEGORY -> {
                    if (it != CategoryDrinkFilter.NONE) {
                        drinksCopy = drinksCopy.filter { drink ->
                            drink.getStrCategory() == it.key
                        }
                    }
                }
                DrinkFilterType.INGREDIENT -> {
                    if (it != IngredientDrinkFilter.NONE) {
                        drinksCopy = drinksCopy.filter { drink ->
                            var isValid = false
                            for ((key, _) in drink.getIngredients()) {
                                if (key.equals(it.key)) {
                                    isValid = true
                                    break
                                }
                            }
                            isValid
                        }
                    }
                }
                DrinkFilterType.GLASS -> {
                }
            }
        }
        return drinksCopy
    }

    fun sortData(drinks: List<Drink>, sortDrinkType: SortDrinkType): List<Drink> {
        var drinksCopy = drinks
        drinksCopy = when (sortDrinkType) {
            SortDrinkType.RECENT -> drinksCopy.sortedByDescending { drink -> drink.getCreated() }
            SortDrinkType.NAME_ASC -> drinksCopy.sortedBy { drink -> drink.getStrDrink() }
            SortDrinkType.NAME_DESC -> drinksCopy.sortedByDescending { drink -> drink.getStrDrink() }
            SortDrinkType.ALCOHOL_ASC -> drinksCopy.sortedWith(alcoholComparator)
            SortDrinkType.ALCOHOL_DESC -> drinksCopy.sortedWith(alcoholComparator).asReversed()
            SortDrinkType.INGREDIENT_COUNT_ASC -> drinksCopy.sortedBy { drink -> drink.getIngredients().size }
            SortDrinkType.INGREDIENT_COUNT_DESC -> drinksCopy.sortedByDescending { drink -> drink.getIngredients().size }
        }
        return drinksCopy
    }
}