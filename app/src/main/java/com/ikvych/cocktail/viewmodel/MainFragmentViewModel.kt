package com.ikvych.cocktail.viewmodel

import android.app.Application
import android.content.res.Resources
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.ikvych.cocktail.R
import com.ikvych.cocktail.comparator.AlcoholCocktailComparator
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.ui.mapper.CocktailModelMapper
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel
import com.ikvych.cocktail.util.Page
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.util.*

class MainFragmentViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    companion object {
        const val EXTRA_KEY_FILTER_TYPE = "EXTRA_KEY_FILTER_TYPE"
        const val EXTRA_KEY_FILTER = "EXTRA_KEY_FILTER"
        const val EXTRA_KEY_PAGE_NUMBER = "EXTRA_KEY_PAGE_NUMBER"
        const val EXTRA_KEY_SORT_ORDER = "EXTRA_KEY_SORT_ORDER"
    }

    val cocktailLiveData: LiveData<List<CocktailModel>> = cocktailRepository.findAllCocktailsLiveData().map{mapper.mapTo(it!!)}
    private val alcoholComparator: AlcoholCocktailComparator = AlcoholCocktailComparator()
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
    val isBatteryChargingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isBatteryLowLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val batteryPercentLiveData: MutableLiveData<Int> = MutableLiveData()

    val sortLiveData: MutableLiveData<SortDrinkType> = object : MutableLiveData<SortDrinkType>() {

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

    init {
        isBatteryLowLiveData.value = false
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    val filtersLiveData: MutableLiveData<HashMap<DrinkFilterType, DrinkFilter>> =
        object : MutableLiveData<HashMap<DrinkFilterType, DrinkFilter>>() {

            init {
                value = value
            }

            override fun setValue(value: HashMap<DrinkFilterType, DrinkFilter>?) {
                val keys = value?.keys
                val values = value?.values

                val keysArray: IntArray? = keys?.map { it.ordinal }?.toIntArray()
                val valuesArray: ArrayList<String>? = values?.map { it.key } as? ArrayList<String>
                if (keysArray != null && valuesArray != null) {
                    savedStateHandle.set(EXTRA_KEY_FILTER_TYPE, keysArray)
                    savedStateHandle.set(EXTRA_KEY_FILTER, valuesArray)
                }
                super.setValue(value)
            }

            override fun getValue(): HashMap<DrinkFilterType, DrinkFilter>? {
                val keysArray: IntArray? = savedStateHandle.get(EXTRA_KEY_FILTER_TYPE)
                val valuesArray: ArrayList<String>? = savedStateHandle.get(EXTRA_KEY_FILTER)
                var map = HashMap<DrinkFilterType, DrinkFilter>()

                valuesArray?.forEachIndexed { index, value ->
                    val drinkFilterType = DrinkFilterType.values()[keysArray!![index]]
                    val drinkFilter = when (drinkFilterType) {
                        DrinkFilterType.ALCOHOL -> {
                            AlcoholDrinkFilter.values().first { it.key == value }
                        }
                        DrinkFilterType.CATEGORY -> {
                            CategoryDrinkFilter.values().first { it.key == value }
                        }
                        DrinkFilterType.INGREDIENT -> {
                            IngredientDrinkFilter.values().first { it.key == value }
                        }
                        DrinkFilterType.GLASS -> {
                        }
                    } as DrinkFilter
                    map[drinkFilterType] = drinkFilter
                }
                if (map.isNullOrEmpty()) {
                    map = hashMapOf(
                        Pair(DrinkFilterType.ALCOHOL, AlcoholDrinkFilter.NONE),
                        Pair(DrinkFilterType.CATEGORY, CategoryDrinkFilter.NONE),
                        Pair(DrinkFilterType.INGREDIENT, IngredientDrinkFilter.NONE)
                    )
                }
                return map
            }
        }


    val lastAppliedFiltersLiveData: MutableLiveData<HashMap<DrinkFilterType, DrinkFilter>> =
        MutableLiveData()

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


    val filteredCocktailsLiveData: MutableLiveData<List<CocktailModel>> =
        object : MediatorLiveData<List<CocktailModel>>() {
            init {
                value = arrayListOf()
                addSource(cocktailLiveData) {
                    filterAndSortData(cocktailLiveData.value)
                }
                addSource(filtersLiveData) {
                    filterAndSortData(cocktailLiveData.value)
                }
                addSource(sortLiveData) {
                    filterAndSortData(cocktailLiveData.value)
                }
            }

            private fun filterAndSortData(cocktails: List<CocktailModel>?) {
                val cocktailsCopy = cocktails ?: arrayListOf()

                val filteredData =
                    filterData(cocktailsCopy, filtersLiveData.value!!.values.toList() as ArrayList)
                val sortedData = sortData(filteredData, sortLiveData.value!!)
                value = sortedData
            }
        }

    val filteredFavoriteCocktailsLiveData: MutableLiveData<ArrayList<CocktailModel>> =
        object : MediatorLiveData<ArrayList<CocktailModel>>() {
            init {
                if (value == null) {
                    value = arrayListOf()
                }
                addSource(filteredCocktailsLiveData) {
                    val resultList = favoriteFilter(it)
                    value = resultList
                }
            }

            fun favoriteFilter(cocktails: List<CocktailModel>): ArrayList<CocktailModel> {
                var cocktailsCopy = cocktails
                cocktailsCopy = cocktailsCopy.filter {
                    it.isFavorite
                }
                return cocktailsCopy as ArrayList<CocktailModel>
            }
        }

    val allFilteredLiveData: LiveData<SpannableString> =
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

                addSource(filteredCocktailsLiveData) {
                    if (isFiltersPresent() && it.isEmpty() && filteredFavoriteCocktailsLiveData.value!!.isEmpty()) {
                        value = SpannableString(emptySearch)
                    } else {
                        var currentResult = src.replace("h", it.size.toString())
                        currentResult = currentResult.replace(
                            "f",
                            filteredFavoriteCocktailsLiveData.value!!.size.toString()
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

    fun resetFilter(filter: DrinkFilter) {
        when (filter.type) {
            DrinkFilterType.CATEGORY -> {
                filtersLiveData.value =
                    filtersLiveData.value!!.apply { this[filter.type] = CategoryDrinkFilter.NONE }
            }
            DrinkFilterType.ALCOHOL -> {
                filtersLiveData.value =
                    filtersLiveData.value!!.apply { this[filter.type] = AlcoholDrinkFilter.NONE }
            }
            DrinkFilterType.GLASS -> {

            }
            DrinkFilterType.INGREDIENT -> {
                filtersLiveData.value =
                    filtersLiveData.value!!.apply { this[filter.type] = IngredientDrinkFilter.NONE }
            }
        }
    }

    fun isFiltersPresent(): Boolean {
        filtersLiveData.value?.forEach {
            //загальне енам значення для фільтрів яке означає відстуність як такого
            //в стрінгові ресурси не виношу бо наразі немає в цьому сенсу()
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
        lastAppliedFiltersLiveData.value?.forEach {
            if (it.value != filtersLiveData.value!![it.key]) {
                return true
            }
        }
        return false
    }

    fun filterData(cocktails: List<CocktailModel>, cocktailFilters: ArrayList<DrinkFilter>): List<CocktailModel> {
        var cocktailsCopy = cocktails
        cocktailFilters.forEach {
            when (it.type) {
                DrinkFilterType.ALCOHOL -> {
                    if (it != AlcoholDrinkFilter.NONE) {
                        cocktailsCopy = cocktailsCopy.filter { cocktail ->
                            cocktail.alcoholType == it
                        }
                    }
                }
                DrinkFilterType.CATEGORY -> {
                    if (it != CategoryDrinkFilter.NONE) {
                        cocktailsCopy = cocktailsCopy.filter { cocktail ->
                            cocktail.category == it
                        }
                    }
                }
                DrinkFilterType.INGREDIENT -> {
                    if (it != IngredientDrinkFilter.NONE) {
                        cocktailsCopy = cocktailsCopy.filter { cocktail ->
                            var isValid = false
                            for (i in cocktail.ingredients) {
                                if (i == it) {
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
        return cocktailsCopy
    }

    fun sortData(cocktails: List<CocktailModel>, sortDrinkType: SortDrinkType): List<CocktailModel> {
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