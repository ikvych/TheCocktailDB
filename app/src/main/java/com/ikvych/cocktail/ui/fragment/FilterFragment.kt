package com.ikvych.cocktail.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.entity.Ingredient
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.ui.base.*
import com.ikvych.cocktail.ui.dialog.FilterDrinkAlcoholDialogFragment
import com.ikvych.cocktail.ui.dialog.FilterDrinkCategoryDialogFragment
import com.ikvych.cocktail.ui.dialog.FilterDrinkIngredientDialogFragment
import com.ikvych.cocktail.viewmodel.MainViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import com.ikvych.cocktail.widget.custom.ApplicationToolBar

class FilterFragment : BaseFragment() {

    override var contentLayoutResId: Int = R.layout.fragment_filter
    private lateinit var returnBtn: ImageButton
    private lateinit var acceptBtn: Button
    private lateinit var resetBtn: Button

    private lateinit var alcoholFilter: LinearLayout
    private lateinit var chosenAlcoholFilter: TextView
    private lateinit var categoryFilter: LinearLayout
    private lateinit var chosenCategoryFilter: TextView
    private lateinit var ingredientFilter: LinearLayout
    private lateinit var chosenIngredientFilter: TextView

    private val drinkFilters: HashMap<DrinkFilterType, DrinkFilter> = hashMapOf()

    private lateinit var viewModel: BaseViewModel
    private var fragmentListener: OnFilterResultListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            fragmentListener = context as OnFilterResultListener
        } catch (exception: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement OnFilterResultListener")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            fragmentListener = null
        } catch (exception: ClassCastException) {
            throw ClassCastException("${activity.toString()} must implement OnFilterResultListener")
        }
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)

        initCategoryFilters()
        initAlcoholFilters()
        initIngredientFilters()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        alcoholFilter = view.findViewById(R.id.im_alcohol_filter)
        alcoholFilter.setOnClickListener { v ->
            FilterDrinkAlcoholDialogFragment.newInstance()
                .show(childFragmentManager, FilterDrinkAlcoholDialogFragment::class.java.simpleName)
        }
        chosenAlcoholFilter = view.findViewById(R.id.tv_chosen_alcohol_filter)
        chosenAlcoholFilter.text =
            drinkFilters[DrinkFilterType.ALCOHOL]?.key ?: AlcoholDrinkFilter.NONE.key

        categoryFilter = view.findViewById(R.id.im_category_filter)
        categoryFilter.setOnClickListener { v ->
            FilterDrinkCategoryDialogFragment.newInstance().show(
                childFragmentManager,
                FilterDrinkCategoryDialogFragment::class.java.simpleName
            )
        }
        chosenCategoryFilter = view.findViewById(R.id.tv_chosen_category_filter)
        chosenCategoryFilter.text =
            drinkFilters[DrinkFilterType.CATEGORY]?.key ?: CategoryDrinkFilter.NONE.key

        ingredientFilter = view.findViewById(R.id.im_ingredient_filter)
        ingredientFilter.setOnClickListener { v ->
            FilterDrinkIngredientDialogFragment.newInstance(viewModel.getAllIngredient())
                .show(
                    childFragmentManager,
                    FilterDrinkIngredientDialogFragment::class.java.simpleName
                )
        }
        chosenIngredientFilter = view.findViewById(R.id.tv_chosen_ingredient_filter)
        chosenIngredientFilter.text =
            drinkFilters[DrinkFilterType.INGREDIENT]?.key ?: CategoryDrinkFilter.NONE.key

        acceptBtn = view.findViewById(R.id.btn_accept)
        acceptBtn.setOnClickListener {

            fragmentListener!!.onFilterApply(arrayListOf(*drinkFilters.values.toTypedArray()))
            parentFragmentManager.popBackStack()
        }
        resetBtn = view.findViewById(R.id.btn_reject)
        resetBtn.setOnClickListener {
            drinkFilters.clear()
            fragmentListener!!.onFilterReset()
            parentFragmentManager.popBackStack()
        }

        returnBtn = view.findViewById<ApplicationToolBar>(R.id.atb_fragment_filter).returnBtn
        returnBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun initCategoryFilters() {
        val categoryKey = requireArguments().getString(DrinkFilterType.CATEGORY.key)
            ?: CategoryDrinkFilter.NONE.key

        if (categoryKey != CategoryDrinkFilter.NONE.key) {
            CategoryDrinkFilter.values().forEach {
                if (it.key == categoryKey) {
                    drinkFilters[CategoryDrinkFilter.COCKTAIL.type] = it
                }
            }
        }
    }

    private fun initAlcoholFilters() {
        val alcoholKey = requireArguments().getString(DrinkFilterType.ALCOHOL.key)
            ?: AlcoholDrinkFilter.NONE.key

        if (alcoholKey != AlcoholDrinkFilter.NONE.key) {
            AlcoholDrinkFilter.values().forEach {
                if (it.key == alcoholKey) {
                    drinkFilters[AlcoholDrinkFilter.ALCOHOLIC.type] = it
                }
            }
        }
    }

    private fun initIngredientFilters() {
        val ingredientKey = requireArguments().getString(DrinkFilterType.INGREDIENT.key)
            ?: "None"

        if (ingredientKey == "None") {
            drinkFilters.remove(DrinkFilterType.INGREDIENT)
        } else {
            val ingredient = IngredientDrinkFilter.INGREDIENT
            ingredient.key = ingredientKey
            drinkFilters.put(DrinkFilterType.INGREDIENT, ingredient)
        }
    }

    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        when (type) {
            AlcoholDrinkType -> {
                val alcoholType = data as AlcoholDrinkFilter
                if (alcoholType != AlcoholDrinkFilter.NONE) {
                    drinkFilters[alcoholType.type] = alcoholType
                } else {
                    drinkFilters.remove(alcoholType.type)
                }
                chosenAlcoholFilter.text = alcoholType.key
            }
            CategoryDrinkType -> {
                val categoryType = data as CategoryDrinkFilter
                if (categoryType != CategoryDrinkFilter.NONE) {
                    drinkFilters[categoryType.type] = categoryType
                } else {
                    drinkFilters.remove(categoryType.type)
                }
                chosenCategoryFilter.text = categoryType.key
            }
            IngredientDrinkType -> {
                val ingredientType = data as Ingredient
                val ingredient = IngredientDrinkFilter.INGREDIENT
                ingredient.key = ingredientType.strIngredient1!!
                if (ingredientType.strIngredient1 == "None") {
                    drinkFilters.remove(DrinkFilterType.INGREDIENT)
                } else {
                    drinkFilters[ingredient.type] = ingredient
                }
                chosenIngredientFilter.text = ingredient.key
            }
        }

    }

    interface OnFilterResultListener {
        fun onFilterApply(drinkFilters: ArrayList<DrinkFilter>)
        fun onFilterReset()
    }

    companion object {

        @JvmStatic
        fun newInstance(
            drinkFilters: ArrayList<DrinkFilter> = arrayListOf()
        ) = FilterFragment().apply {
            arguments = Bundle().apply {
                drinkFilters.forEach {
                    putString(it.type.key, it.key)
                }
            }
        }
    }
}