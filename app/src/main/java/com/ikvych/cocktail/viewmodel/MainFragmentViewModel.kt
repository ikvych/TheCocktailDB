package com.ikvych.cocktail.viewmodel

import android.app.Application
import android.content.res.Resources
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.Log
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.comparator.AlcoholDrinkComparator
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.util.Page
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.util.*

class MainFragmentViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    val drinksLiveData: LiveData<List<Drink>> = drinkRepository.getAllDrinksFromDbLiveData()
    private val alcoholComparator: AlcoholDrinkComparator = AlcoholDrinkComparator()
    val viewPager2LiveData: MutableLiveData<Page> = MutableLiveData()
    val isBatteryChargingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isBatteryLowLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val batteryPercentLiveData: MutableLiveData<Int> = MutableLiveData()

    val sortLiveData: MutableLiveData<SortDrinkType> =
        MutableLiveData<SortDrinkType>()

    init {
        sortLiveData.value = SortDrinkType.RECENT
    }

    init {
        isBatteryLowLiveData.value = false
    }

    val filtersLiveData: MutableLiveData<HashMap<DrinkFilterType, DrinkFilter>> =
        object : MutableLiveData<HashMap<DrinkFilterType, DrinkFilter>>() {

            init {
                value = value
            }

            override fun setValue(value: HashMap<DrinkFilterType, DrinkFilter>?) {
                Log.d("MyTags", "SAVE")
                val keys = value?.keys
                val values1 = value?.values

                val keyArray1: IntArray? = keys?.map { it.ordinal }?.toIntArray()
                val valuesArray1: ArrayList<String>? = values1?.map { it.key } as? ArrayList<String>
                if (keyArray1 != null && valuesArray1 != null) {
                    savedStateHandle.set("EXTRA_KEY_KEYS", keyArray1)
                    savedStateHandle.set("EXTRA_KEY_VALUES", valuesArray1)
                }
                super.setValue(value)
            }

            override fun getValue(): HashMap<DrinkFilterType, DrinkFilter>? {
                Log.d("MyTags", "GET")
                val keyArray: IntArray? = savedStateHandle.get("EXTRA_KEY_KEYS")
                val valuesArray: ArrayList<String>? = savedStateHandle.get("EXTRA_KEY_VALUES")
                Log.d("MyTags", "${keyArray}")
                Log.d("MyTags", "${valuesArray}")
                var map = HashMap<DrinkFilterType, DrinkFilter>()

                valuesArray?.forEachIndexed { index, value ->
                    val drinkFilterType = DrinkFilterType.values()[keyArray!![index]]
                    val drinkFilter = when (drinkFilterType) {
                        DrinkFilterType.ALCOHOL -> {
                            AlcoholDrinkFilter.values().filter { it.key == value }.first()
                        }
                        DrinkFilterType.CATEGORY -> {
                            CategoryDrinkFilter.values().filter { it.key == value }.first()
                        }
                        DrinkFilterType.INGREDIENT -> {
                            IngredientDrinkFilter.values().filter { it.key == value }.first()
                        }
                        DrinkFilterType.GLASS -> {}
                    } as DrinkFilter
                    map.put(drinkFilterType, drinkFilter)
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
        MutableLiveData<HashMap<DrinkFilterType, DrinkFilter>>()

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

    val allFilteredLiveData: LiveData<SpannableString> =
        object : MediatorLiveData<SpannableString>() {
            init {
                val src = "Знайдено: h @, f %"

                val historyDrawable = ContextCompat.getDrawable(
                    application,
                    com.ikvych.cocktail.R.drawable.ic_drink_history
                )
                val favoriteDrawable = ContextCompat.getDrawable(
                    application,
                    com.ikvych.cocktail.R.drawable.ic_drink_favorite
                )

                //convert sp into px
                val drawableSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    16F,
                    Resources.getSystem().displayMetrics
                ).toInt()
                historyDrawable!!.setBounds(0, 0, drawableSize, drawableSize)
                favoriteDrawable!!.setBounds(0, 0, drawableSize, drawableSize)

                val emptySearch = "Нічого не знайдено"

                addSource(filteredDrinksLiveData) {
                    if (isFiltersPresent() && it.isEmpty() && filteredFavoriteDrinksLiveData.value!!.isEmpty()) {
                        value = SpannableString(emptySearch)
                    } else {
                        var currentResult = src.replace("h", it.size.toString())
                        currentResult = currentResult.replace(
                            "f",
                            filteredFavoriteDrinksLiveData.value!!.size.toString()
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


    fun getAllDrinksFromDb(): List<Drink> {
        return drinksLiveData.value ?: emptyList()
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