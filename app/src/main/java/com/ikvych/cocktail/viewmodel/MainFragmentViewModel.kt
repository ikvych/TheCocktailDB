package com.ikvych.cocktail.viewmodel

import android.app.Application
import android.content.res.Resources
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.ikvych.cocktail.R
import com.ikvych.cocktail.util.AlcoholCocktailComparator
import com.ikvych.cocktail.presentation.filter.type.SortDrinkType
import com.ikvych.cocktail.util.DRINK_FILTER_ABSENT
import com.ikvych.cocktail.util.EMPTY_STRING
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.presentation.filter.DrinkFilter
import com.ikvych.cocktail.presentation.filter.type.*
import com.ikvych.cocktail.presentation.mapper.CocktailModelMapper
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.presentation.model.cocktail.IngredientModel
import com.ikvych.cocktail.util.BatteryStateLiveData
import com.ikvych.cocktail.util.Page
import java.util.*
import kotlin.collections.ArrayList

class MainFragmentViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    application: Application,
    savedStateHandle: SavedStateHandle
) : DrinkViewModel(application, savedStateHandle, cocktailRepository, mapper) {

    companion object {
        const val EXTRA_KEY_FILTER_TYPE = "EXTRA_KEY_FILTER_TYPE"
        const val EXTRA_KEY_FILTER = "EXTRA_KEY_FILTER"
        const val EXTRA_KEY_PAGE_NUMBER = "EXTRA_KEY_PAGE_NUMBER"
        const val EXTRA_KEY_SORT_ORDER = "EXTRA_KEY_SORT_ORDER"
    }

    val ingredientsListLiveData: LiveData<List<IngredientModel>> =
        cocktailRepository.ingredientsListLiveData
            .map { list ->
                list.map { IngredientModel(DrinkFilterType.INGREDIENT, it.ingredient) }
            }

    private val triggerObserver: Observer<in Any?> = Observer { }

    init {
        ingredientsListLiveData.observeForever(triggerObserver)
    }

    override fun onCleared() {
        ingredientsListLiveData.removeObserver(triggerObserver)
        super.onCleared()
    }

    private val alcoholComparator: AlcoholCocktailComparator =
        AlcoholCocktailComparator()
    val viewPager2LiveData: MutableLiveData<Page> = object : MutableLiveData<Page>() {
        init {
            value = value
        }

        override fun setValue(value: Page?) {
            savedStateHandle.set(EXTRA_KEY_PAGE_NUMBER, value?.ordinal)
            super.setValue(value)
        }

        override fun getValue(): Page? {
            val pageNumber = savedStateHandle.get<Int>(EXTRA_KEY_PAGE_NUMBER) ?: 0
            return Page.values()[pageNumber]
        }

    }
    var fragmentJustCreated: Boolean = true

    val cachedBatteryStateLiveData: BatteryStateLiveData = BatteryStateLiveData(application)
    val cocktailsLiveData: LiveData<List<CocktailModel>> =
        cocktailRepository.findAllCocktailsLiveData().map { mapper.mapTo(it!!) }

    @Suppress("IMPLICIT_CAST_TO_ANY")
/*    val filtersLiveData: MutableLiveData<HashMap<DrinkFilterType, List<DrinkFilter>>> =
        MutableLiveData()*/
    val filtersLiveData: MutableLiveData<HashMap<DrinkFilterType, List<DrinkFilter>>> =
        object : MutableLiveData<HashMap<DrinkFilterType, List<DrinkFilter>>>() {

            init {
                value = value
            }

            override fun setValue(value: HashMap<DrinkFilterType, List<DrinkFilter>>?) {
                value?.forEach { entry ->
                    val filterTypeOrdinal = entry.key.ordinal
                    val drinkFilterKeyArray: Array<String> =
                        entry.value.map { it.key }.toTypedArray()

                    savedStateHandle.set(
                        "${EXTRA_KEY_FILTER_TYPE}${entry.key.key}",
                        filterTypeOrdinal
                    )
                    savedStateHandle.set("${EXTRA_KEY_FILTER}${entry.key.key}", drinkFilterKeyArray)
                }
                super.setValue(value)
            }

            override fun getValue(): HashMap<DrinkFilterType, List<DrinkFilter>>? {
                var map = HashMap<DrinkFilterType, List<DrinkFilter>>()

                DrinkFilterType.values().forEach { filterType ->
                    val filterTypeOrdinal: Int =
                        savedStateHandle.get("${EXTRA_KEY_FILTER_TYPE}${filterType.key}")
                            ?: return@forEach
                    val currentFilterType = DrinkFilterType.values()[filterTypeOrdinal]

                    val drinkFilterKeyArray: Array<String>? =
                        savedStateHandle.get("${EXTRA_KEY_FILTER}${filterType.key}")
                    val drinkFilters: ArrayList<DrinkFilter> = arrayListOf()
                    when (currentFilterType) {
                        DrinkFilterType.ALCOHOL -> {
                            drinkFilterKeyArray?.forEach { key ->
                                drinkFilters.add(
                                    AlcoholDrinkFilter.values().first { it.key == key })
                            }
                        }
                        DrinkFilterType.CATEGORY -> {
                            drinkFilterKeyArray?.forEach { key ->
                                drinkFilters.add(
                                    CategoryDrinkFilter.values().first { it.key == key })
                            }
                        }
                        DrinkFilterType.GLASS -> {
                            drinkFilterKeyArray?.forEach { key ->
                                drinkFilters.add(GlassDrinkFilter.values().first { it.key == key })
                            }
                        }
                        DrinkFilterType.INGREDIENT -> {
                            val list = ingredientsListLiveData.value
                            drinkFilterKeyArray?.forEach { key ->
                                drinkFilters.add(
                                    list?.first { it.key == key } ?: IngredientModel(
                                        DrinkFilterType.INGREDIENT,
                                        key
                                    )
                                )
                            }
                        }
                    }
                    map[currentFilterType] = drinkFilters
                }

                if (map.isNullOrEmpty()) {
                    map = hashMapOf(
                        Pair(DrinkFilterType.ALCOHOL, arrayListOf(AlcoholDrinkFilter.NONE)),
                        Pair(DrinkFilterType.CATEGORY, arrayListOf(CategoryDrinkFilter.NONE)),
                        Pair(
                            DrinkFilterType.INGREDIENT,
                            arrayListOf(
                                IngredientModel(
                                    DrinkFilterType.INGREDIENT,
                                    DRINK_FILTER_ABSENT
                                )
                            )
                        ),
                        Pair(DrinkFilterType.GLASS, arrayListOf(GlassDrinkFilter.NONE))
                    )
                }
                return map
            }
        }


    val lastAppliedFiltersLiveData: MutableLiveData<HashMap<DrinkFilterType, List<DrinkFilter>>> =
        MutableLiveData()

    val sortTypeLiveData: MutableLiveData<SortDrinkType> =
        object : MutableLiveData<SortDrinkType>() {
            init {
                value = value
            }

            override fun setValue(value: SortDrinkType?) {
                savedStateHandle.set(EXTRA_KEY_SORT_ORDER, value?.ordinal)
                super.setValue(value)
            }

            override fun getValue(): SortDrinkType? {
                val sortOrderOrdinal = savedStateHandle.get<Int>(EXTRA_KEY_SORT_ORDER) ?: 0
                return SortDrinkType.values()[sortOrderOrdinal]
            }
        }

    val filteredAndSortedDrinksLiveData: MutableLiveData<List<CocktailModel>> =
        object : MediatorLiveData<List<CocktailModel>>() {
            init {
                value = arrayListOf()
                addSource(cocktailsLiveData) {
                    filterAndSortData(cocktailsLiveData.value)
                }
                addSource(filtersLiveData) {
                    filterAndSortData(cocktailsLiveData.value)
                }
                addSource(sortTypeLiveData) {
                    filterAndSortData(cocktailsLiveData.value)
                }
            }

            private fun filterAndSortData(cocktails: List<CocktailModel>?) {
                val cocktailsCopy = cocktails ?: arrayListOf()

                val filteredData =
                    filterData(cocktailsCopy, filtersLiveData.value!!)
                val sortedData = sortData(filteredData, sortTypeLiveData.value!!)
                value = sortedData
            }
        }

    val filteredAndSortedFavoriteDrinksLiveData: LiveData<ArrayList<CocktailModel>> =
        filteredAndSortedDrinksLiveData.map {
            favoriteFilter(it)
        }

    private fun favoriteFilter(cocktails: List<CocktailModel>): ArrayList<CocktailModel> {
        var cocktailsCopy = cocktails
        cocktailsCopy = cocktailsCopy.filter {
            it.isFavorite
        }
        return cocktailsCopy as ArrayList<CocktailModel>
    }

    val filteredAndSortedResultDrinksLiveData: LiveData<SpannableString> =
        object : MediatorLiveData<SpannableString>() {
            init {
                //Петтерн містить "Found: h @, f %" - де 'h' і ʼfʼ символи які заміюються на кількість знайдених коктейлів
                //у історії і улюблених, відповідно, а ʼ@ʼ і ʼ%ʼ символи які замінюються на іконки історії та улюблені відповідно
                val src =
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
                historyDrawable!!.setBounds(0, 0, drawableSize, drawableSize)
                favoriteDrawable!!.setBounds(0, 0, drawableSize, drawableSize)

                val emptySearch = application.resources.getString(R.string.all_empty_search)

                addSource(filteredAndSortedDrinksLiveData) {
                    if (isFiltersPresent() && it.isEmpty() && filteredAndSortedFavoriteDrinksLiveData.value!!.isEmpty()) {
                        value = SpannableString(emptySearch)
                    } else {
                        var currentResult = src.replace("h", it.size.toString())
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
        if (it[DrinkFilterType.INGREDIENT]!!.contains(
                it[DrinkFilterType.INGREDIENT]!!.find { cocktail ->
                    cocktail.key == DRINK_FILTER_ABSENT
                }
            )
        ) {
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
                    filtersLiveData.value!![DrinkFilterType.INGREDIENT]!!.find { cocktail ->
                        cocktail.key == DRINK_FILTER_ABSENT
                    }
                ) ||
                !filtersLiveData.value!![DrinkFilterType.GLASS]!!.contains(GlassDrinkFilter.NONE)
    }

    //обнуляє фільтри
    fun resetFilters() {
        filtersLiveData.value = hashMapOf(
            Pair(DrinkFilterType.ALCOHOL, arrayListOf(AlcoholDrinkFilter.NONE)),
            Pair(DrinkFilterType.CATEGORY, arrayListOf(CategoryDrinkFilter.NONE)),
            Pair(
                DrinkFilterType.INGREDIENT,
                arrayListOf(
                    IngredientModel(
                        DrinkFilterType.INGREDIENT,
                        DRINK_FILTER_ABSENT
                    )
                )
            ),
            Pair(DrinkFilterType.GLASS, arrayListOf(GlassDrinkFilter.NONE))
        )
        lastAppliedFiltersLiveData.value = hashMapOf(
            Pair(DrinkFilterType.ALCOHOL, arrayListOf(AlcoholDrinkFilter.NONE)),
            Pair(DrinkFilterType.CATEGORY, arrayListOf(CategoryDrinkFilter.NONE)),
            Pair(
                DrinkFilterType.INGREDIENT,
                arrayListOf(
                    IngredientModel(
                        DrinkFilterType.INGREDIENT,
                        DRINK_FILTER_ABSENT
                    )
                )
            ),
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
                launchRequest {
                    val currentFilter = IngredientModel(
                        DrinkFilterType.INGREDIENT,
                        cocktailRepository.findIngredient(filter.key).ingredient
                    )
                    filtersLiveData.postValue(filtersLiveData.value!!.apply {
                        val filters: List<DrinkFilter> = this[filter.type]!!
                        if (filters.size == 1) {
                            this[filter.type] = arrayListOf(
                                IngredientModel(
                                    DrinkFilterType.INGREDIENT,
                                    DRINK_FILTER_ABSENT
                                )
                            )
                        } else {
                            this[filter.type] = this[filter.type]!!.filter { it != currentFilter }
                        }
                    })
                }
            }
        }
    }

    fun filterData(
        cocktails: List<CocktailModel>,
        drinkFilters: HashMap<DrinkFilterType, List<DrinkFilter>>
    ): List<CocktailModel> {
        var cocktailsCopy = cocktails
        drinkFilters.forEach {
            when (it.key) {
                DrinkFilterType.ALCOHOL -> {
                    if (!it.value.contains(AlcoholDrinkFilter.NONE)) {
                        cocktailsCopy = cocktailsCopy.filter { cocktail ->
                            var isValidDrink = false
                            for (i in it.value.indices) {
                                if (cocktail.alcoholType == it.value[i]) {
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
                        cocktailsCopy = cocktailsCopy.filter { drink ->
                            var isValidDrink = false
                            for (i in it.value.indices) {
                                if (drink.category == it.value[i]) {
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
                    if (!it.value.contains(it.value.find { cocktail ->
                            cocktail.key == DRINK_FILTER_ABSENT
                        })) {
                        cocktailsCopy = cocktailsCopy.filter { drink ->
                            var isValidDrink = true
                            for (i in it.value.indices) {
                                var isPresentCurrentIngredient = false
                                for (y in drink.ingredients.indices) {
                                    if (drink.ingredients[y] == it.value[i]) {
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
                        cocktailsCopy = cocktailsCopy.filter { drink ->
                            var isValidDrink = false
                            for (i in it.value.indices) {
                                if (drink.glass == it.value[i]) {
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
        return cocktailsCopy
    }

    fun sortData(
        cocktails: List<CocktailModel>,
        sortDrinkType: SortDrinkType
    ): List<CocktailModel> {
        var cocktailCopy = cocktails
        cocktailCopy = when (sortDrinkType) {
            SortDrinkType.RECENT -> cocktailCopy.sortedByDescending { cocktail -> cocktail.id }
            SortDrinkType.NAME_ASC -> cocktailCopy.sortedBy { cocktail -> cocktail.names.defaults }
            SortDrinkType.NAME_DESC -> cocktailCopy.sortedByDescending { cocktail -> cocktail.names.defaults }
            SortDrinkType.ALCOHOL_ASC -> cocktailCopy.sortedWith(alcoholComparator)
            SortDrinkType.ALCOHOL_DESC -> cocktailCopy.sortedWith(alcoholComparator).asReversed()
            SortDrinkType.INGREDIENT_COUNT_ASC -> cocktailCopy.sortedBy { cocktail -> cocktail.ingredients.size }
            SortDrinkType.INGREDIENT_COUNT_DESC -> cocktailCopy.sortedByDescending { cocktail -> cocktail.ingredients.size }
        }
        return cocktailCopy
    }
}