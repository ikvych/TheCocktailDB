package com.ikvych.cocktail.viewmodel

import android.app.Application
import android.content.res.Resources
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.ikvych.cocktail.R
import com.ikvych.cocktail.comparator.AlcoholDrinkComparator
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.constant.EMPTY_STRING
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.repository.DrinkRepositoryImpl
import com.ikvych.cocktail.data.repository.base.DrinkRepository
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.*
import com.ikvych.cocktail.util.Page
import java.util.*

class MainFragmentViewModel(application: Application) : DrinkViewModel(application) {

    private val drinkRepository: DrinkRepository = DrinkRepositoryImpl(application)
    private val alcoholComparator: AlcoholDrinkComparator = AlcoholDrinkComparator()
    val viewPager2LiveData: MutableLiveData<Page> = MutableLiveData(Page.HISTORY)
    var fragmentJustCreated: Boolean = true

    val isBatteryChargingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isBatteryLowLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val batteryPercentLiveData: MutableLiveData<Int> = MutableLiveData()

    val drinksLiveData: LiveData<List<Drink>> = drinkRepository.getAllDrinksFromDbLiveData()
    val filtersLiveData: MutableLiveData<HashMap<DrinkFilterType, List<DrinkFilter>>> =
        MutableLiveData()
    val lastAppliedFiltersLiveData: MutableLiveData<HashMap<DrinkFilterType, List<DrinkFilter>>> =
        MutableLiveData()

    init {
        //ініціалізую фільтри NONE значеннями
        resetFilters()
    }

    val sortTypeLiveData: MutableLiveData<SortDrinkType> =
        MutableLiveData(SortDrinkType.RECENT)

    val filteredAndSortedDrinksLiveData: MutableLiveData<List<Drink>> =
        object : MediatorLiveData<List<Drink>>() {
            init {
                value = arrayListOf()
                addSource(drinksLiveData) {
                    filterAndSortData(drinksLiveData.value)
                }
                addSource(filtersLiveData) {
                    filterAndSortData(drinksLiveData.value)
                }
                addSource(sortTypeLiveData) {
                    filterAndSortData(drinksLiveData.value)
                }
            }

            private fun filterAndSortData(drinks: List<Drink>?) {
                val drinksCopy = drinks ?: arrayListOf()

                val filteredData =
                    filterData(drinksCopy, filtersLiveData.value!!)
                val filteredAndSortedData = sortData(filteredData, sortTypeLiveData.value!!)
                value = filteredAndSortedData
            }
        }

    val filteredAndSortedFavoriteDrinksLiveData: LiveData<ArrayList<Drink>> =
        filteredAndSortedDrinksLiveData.map {
            filterFavorite(it)
        }

    private fun filterFavorite(drinks: List<Drink>): ArrayList<Drink> {
        return drinks.filter { it.isFavorite() } as ArrayList<Drink>
    }

    val filteredAndSortedResultDrinksLiveData: LiveData<SpannableString> =
        object : MediatorLiveData<SpannableString>() {
            init {
                //Петтерн містить "Found: h @, f %" - де 'h' і ʼfʼ символи які заміюються на кількість знайдених коктейлів
                //у історії і улюблених, відповідно, а ʼ@ʼ і ʼ%ʼ символи які замінюються на іконки історії та улюблені відповідно
                val srcPattern =
                    application.resources.getString(R.string.filter_fragment_search_result_text_pattern)

                val historyDrawable = ContextCompat.getDrawable(
                    application,
                    R.drawable.ic_drink_history
                )
                val favoriteDrawable = ContextCompat.getDrawable(
                    application,
                    R.drawable.ic_drink_favorite
                )

                //convert sp into px
                val drawableSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    16F,
                    Resources.getSystem().displayMetrics
                ).toInt()
                //визначаю розміри і положення іконок
                historyDrawable!!.setBounds(0, 0, drawableSize, drawableSize)
                favoriteDrawable!!.setBounds(0, 0, drawableSize, drawableSize)
                //відображається напис у випадку порожнього результату
                val emptySearch = application.resources.getString(R.string.all_empty_search)

                addSource(filteredAndSortedDrinksLiveData) {
                    //якщо фільтрів немає і відфільтовані списки коктейлів (історія і улюблене) пусті,
                    // тоді показую emptySearch
                    if (isFiltersPresent() && it.isEmpty() && filteredAndSortedFavoriteDrinksLiveData.value!!.isEmpty()) {
                        value = SpannableString(emptySearch)
                    } else {
                        var currentResult = srcPattern.replace("h", it.size.toString())
                        currentResult = currentResult.replace(
                            "f",
                            filteredAndSortedFavoriteDrinksLiveData.value!!.size.toString()
                        )
                        val historyIndexDrw = currentResult.indexOf("@")
                        val favoriteIndexDrw = currentResult.indexOf("%")
                        val currentSpannable = SpannableString(currentResult)
                        currentSpannable.setSpan(
                            ImageSpan(historyDrawable),
                            historyIndexDrw,
                            historyIndexDrw + 1,
                            ImageSpan.ALIGN_BOTTOM
                        )
                        currentSpannable.setSpan(
                            ImageSpan(favoriteDrawable),
                            favoriteIndexDrw,
                            favoriteIndexDrw + 1,
                            ImageSpan.ALIGN_BOTTOM
                        )
                        value = currentSpannable
                    }
                }
            }
        }

    val alcoholFilterLiveData: LiveData<String> = filtersLiveData.map {
        if (it[DrinkFilterType.ALCOHOL]!!.first() == AlcoholDrinkFilter.NONE) {
            EMPTY_STRING
        } else {
            it[DrinkFilterType.ALCOHOL]!!.first().key
        }
    }

    val categoryFilterLiveData: LiveData<String> = filtersLiveData.map {
        if (it[DrinkFilterType.CATEGORY]!!.first() == CategoryDrinkFilter.NONE) {
            EMPTY_STRING
        } else {
            it[DrinkFilterType.CATEGORY]!!.first().key
        }
    }

    val glassFilterLiveData: LiveData<String> = filtersLiveData.map {
        if (it[DrinkFilterType.GLASS]!!.first() == GlassDrinkFilter.NONE) {
            EMPTY_STRING
        } else {
            it[DrinkFilterType.GLASS]!!.first().key
        }
    }

    val ingredientFilterLiveData: LiveData<String> = filtersLiveData.map {
        val ingredientFilterText = StringBuilder()
        if (it[DrinkFilterType.INGREDIENT]!!.contains(IngredientDrinkFilter.NONE)) {
            ingredientFilterText.append(EMPTY_STRING)
        } else {
            it[DrinkFilterType.INGREDIENT]?.forEach { filter ->
                ingredientFilterText.append(
                    "${filter.key}  "
                )
            }
        }
        ingredientFilterText.toString()
    }

    @Suppress("UNCHECKED_CAST")
    fun updateFilter(filter: DrinkFilter) {
        lastAppliedFiltersLiveData.value =
            filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, List<DrinkFilter>>
        filtersLiveData.value = filtersLiveData.value!!.apply {
            this[filter.type] = arrayListOf(filter)
        }
    }

    //наразі цей метод заточений під інгредієнти, в подальшому при необхідності можливо переробити
    //на DrinkFilterType
    @Suppress("UNCHECKED_CAST")
    fun updateFilterList(filters: List<DrinkFilter>) {
        lastAppliedFiltersLiveData.value =
            filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, List<DrinkFilter>>
        filtersLiveData.value = filtersLiveData.value!!.apply {
            this[DrinkFilterType.INGREDIENT] = filters
        }
    }

    fun isFiltersPresent(): Boolean {
        return !filtersLiveData.value!![DrinkFilterType.ALCOHOL]!!.contains(AlcoholDrinkFilter.NONE) ||
                !filtersLiveData.value!![DrinkFilterType.CATEGORY]!!.contains(CategoryDrinkFilter.NONE) ||
                !filtersLiveData.value!![DrinkFilterType.INGREDIENT]!!.contains(
                    IngredientDrinkFilter.NONE
                ) ||
                !filtersLiveData.value!![DrinkFilterType.GLASS]!!.contains(GlassDrinkFilter.NONE)
    }

    //обнуляє фільтри
    fun resetFilters() {
        filtersLiveData.value = hashMapOf(
            Pair(DrinkFilterType.ALCOHOL, arrayListOf(AlcoholDrinkFilter.NONE)),
            Pair(DrinkFilterType.CATEGORY, arrayListOf(CategoryDrinkFilter.NONE)),
            Pair(DrinkFilterType.INGREDIENT, arrayListOf(IngredientDrinkFilter.NONE)),
            Pair(DrinkFilterType.GLASS, arrayListOf(GlassDrinkFilter.NONE))
        )
        lastAppliedFiltersLiveData.value = hashMapOf(
            Pair(DrinkFilterType.ALCOHOL, arrayListOf(AlcoholDrinkFilter.NONE)),
            Pair(DrinkFilterType.CATEGORY, arrayListOf(CategoryDrinkFilter.NONE)),
            Pair(DrinkFilterType.INGREDIENT, arrayListOf(IngredientDrinkFilter.NONE)),
            Pair(DrinkFilterType.GLASS, arrayListOf(GlassDrinkFilter.NONE))
        )
    }

    //Метод визначає чи відрізняються поточні фільтри від попередньо обраних.
    //Якщо відрізняються то повертає true і в такому випадку можна показувати Undo button
    fun isUndoEnabled(): Boolean {
        lastAppliedFiltersLiveData.value!!.forEach {
            if (it.value != filtersLiveData.value!![it.key]) {
                return true
            }
        }
        return false
    }

    //обнуляє певний тип фільтру
    fun resetFilter(filter: DrinkFilter) {
        when (filter.type) {
            DrinkFilterType.CATEGORY -> {
                filtersLiveData.value = filtersLiveData.value!!.apply {
                    this[filter.type] = arrayListOf(CategoryDrinkFilter.NONE)
                }
            }
            DrinkFilterType.ALCOHOL -> {
                filtersLiveData.value = filtersLiveData.value!!.apply {
                    this[filter.type] = arrayListOf(AlcoholDrinkFilter.NONE)
                }
            }
            DrinkFilterType.GLASS -> {
                filtersLiveData.value = filtersLiveData.value!!.apply {
                    this[filter.type] = arrayListOf(GlassDrinkFilter.NONE)
                }
            }
            DrinkFilterType.INGREDIENT -> {
                val currentFilter = IngredientDrinkFilter.values().first { it.key == filter.key }
                filtersLiveData.value = filtersLiveData.value!!.apply {
                    val filters: List<DrinkFilter> = this[filter.type]!!
                    if (filters.size == 1) {
                        this[filter.type] = arrayListOf(IngredientDrinkFilter.NONE)
                    } else {
                        this[filter.type] = this[filter.type]!!.filter { it != currentFilter }
                    }
                }
            }
        }
    }

    fun getAllDrinksFromDb(): List<Drink> {
        return drinksLiveData.value ?: emptyList()
    }

    fun filterData(
        drinks: List<Drink>,
        drinkFilters: HashMap<DrinkFilterType, List<DrinkFilter>>
    ): List<Drink> {
        var drinksCopy = drinks
        drinkFilters.forEach {
            when (it.key) {
                DrinkFilterType.ALCOHOL -> {
                    if (!it.value.contains(AlcoholDrinkFilter.NONE)) {
                        drinksCopy = drinksCopy.filter { drink ->
                            var isValidDrink = false
                            for (i in it.value.indices) {
                                if (drink.getStrAlcoholic() == it.value[i].key) {
                                    isValidDrink = true
                                    break
                                }
                            }
                            isValidDrink
                        }
                    }
                }
                DrinkFilterType.CATEGORY -> {
                    if (!it.value.contains(CategoryDrinkFilter.NONE)) {
                        drinksCopy = drinksCopy.filter { drink ->
                            var isValidDrink = false
                            for (i in it.value.indices) {
                                if (drink.getStrCategory() == it.value[i].key) {
                                    isValidDrink = true
                                    break
                                }
                            }
                            isValidDrink
                        }
                    }
                }
                //фільтрування ||
/*                DrinkFilterType.INGREDIENT -> {
                    if (!it.value.contains(IngredientDrinkFilter.NONE)) {
                        drinksCopy = drinksCopy.filter { drink ->
                            var isValidDrink = false
                            for (i in it.value.indices) {
                                for ((key, _) in drink.getIngredients()) {
                                    if (key == it.value[i].key) {
                                        isValidDrink = true
                                        break
                                    }
                                }
                                if (isValidDrink) break
                            }
                            isValidDrink
                        }
                    }
                }*/
                //фільтрування &&
                DrinkFilterType.INGREDIENT -> {
                    if (!it.value.contains(IngredientDrinkFilter.NONE)) {
                        drinksCopy = drinksCopy.filter { drink ->
                            var isValidDrink = true
                            for (i in it.value.indices) {
                                var isPresentCurrentIngredient = false
                                for ((key, _) in drink.getIngredients()) {
                                    if (key == it.value[i].key) {
                                        isPresentCurrentIngredient = true
                                        break
                                    }
                                }
                                if (!isPresentCurrentIngredient) {
                                    isValidDrink = false
                                    break
                                }
                            }
                            isValidDrink
                        }
                    }
                }
                DrinkFilterType.GLASS -> {
                    if (!it.value.contains(GlassDrinkFilter.NONE)) {
                        drinksCopy = drinksCopy.filter { drink ->
                            var isValidDrink = false
                            for (i in it.value.indices) {
                                if (drink.getStrGlass() == it.value[i].key) {
                                    isValidDrink = true
                                    break
                                }
                            }
                            isValidDrink
                        }
                    }
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