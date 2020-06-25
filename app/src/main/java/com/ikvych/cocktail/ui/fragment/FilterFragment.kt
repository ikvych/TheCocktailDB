package com.ikvych.cocktail.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.entity.Ingredient
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.ui.activity.MainActivity
import com.ikvych.cocktail.ui.base.*
import com.ikvych.cocktail.ui.dialog.FilterDrinkAlcoholDialogFragment
import com.ikvych.cocktail.ui.dialog.FilterDrinkCategoryDialogFragment
import com.ikvych.cocktail.ui.dialog.FilterDrinkIngredientDialogFragment
import com.ikvych.cocktail.viewmodel.MainViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import com.ikvych.cocktail.widget.custom.ApplicationToolBar

class FilterFragment : BaseFragment() {

    private lateinit var alcoholRadioGroup: RadioGroup
    private lateinit var categoryRadioGroup: RadioGroup
    private lateinit var returnBtn: ImageButton
    private lateinit var acceptBtn: Button
    private lateinit var resetBtn: Button

    private lateinit var alcoholFilter: ImageButton
    private lateinit var categoryFilter: ImageButton
    private lateinit var ingredientFilter: ImageButton

    private val drinkFilters: HashMap<DrinkFilterType, DrinkFilter> = hashMapOf()

    lateinit var viewModel: BaseViewModel
    private var fragmentListener: OnFilterResultListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            fragmentListener = activity as OnFilterResultListener
        } catch (exception: ClassCastException) {
            throw ClassCastException("${activity.toString()} must implement OnFilterResultListener")
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

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        alcoholFilter = view.findViewById(R.id.im_alcohol_filter)
        alcoholFilter.setOnClickListener { v ->
            FilterDrinkAlcoholDialogFragment.newInstance().show(childFragmentManager)
        }

        categoryFilter = view.findViewById(R.id.im_category_filter)
        categoryFilter.setOnClickListener { v ->
            FilterDrinkCategoryDialogFragment.newInstance().show(childFragmentManager)
        }

        ingredientFilter = view.findViewById(R.id.im_ingredient_filter)
        ingredientFilter.setOnClickListener { v ->
            FilterDrinkIngredientDialogFragment.newInstance(viewModel.getAllIngredient())
                .show(childFragmentManager)
        }

        alcoholRadioGroup = requireView().findViewById(R.id.alcohol_radio_group)
        categoryRadioGroup = requireView().findViewById(R.id.category_radio_group)
/*        initCategoryFilters()*/
/*        initAlcoholFilters()*/

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
        val categoryType = requireArguments().getString(DrinkFilterType.CATEGORY.key)
            ?: CategoryDrinkFilter.NONE.key

        CategoryDrinkFilter.values().forEach {
            categoryRadioGroup.addView(
                RadioButton(requireContext()).apply {
                    id = View.generateViewId()
                    text = it.key
                    if (it.key == categoryType) {
                        if (it.key != CategoryDrinkFilter.NONE.key) {
                            drinkFilters[it.type] = it
                        }
                        isChecked = true
                    }
                }
            )
        }

        categoryRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            CategoryDrinkFilter.values().forEach {
                if (it.key == requireView().findViewById<RadioButton>(checkedId).text) {
                    if (it.key != CategoryDrinkFilter.NONE.key) {
                        drinkFilters[it.type] = it
                    } else {
                        drinkFilters.remove(it.type)
                    }
                }
            }
        }
    }

    private fun initAlcoholFilters() {
        val alcoholType = requireArguments().getString(DrinkFilterType.ALCOHOL.key)
            ?: AlcoholDrinkFilter.NONE.key

        AlcoholDrinkFilter.values().forEach {
            if (it.key == alcoholType) {
                val alcoholRadioBtn: RadioButton = when (it) {
                    AlcoholDrinkFilter.ALCOHOLIC -> requireView().findViewById(R.id.rb_alcoholic)
                    AlcoholDrinkFilter.NON_ALCOHOLIC -> requireView().findViewById(R.id.rb_non_alcoholic)
                    AlcoholDrinkFilter.OPTIONAL_ALCOHOL -> requireView().findViewById(R.id.rb_optional_alcoholic)
                    AlcoholDrinkFilter.NONE -> requireView().findViewById(R.id.rb_none)
                }
                if (it.key != AlcoholDrinkFilter.NONE.key) {
                    drinkFilters.put(it.type, it)
                }
                alcoholRadioBtn.isChecked = true
            }
        }

        alcoholRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            AlcoholDrinkFilter.values().forEach {
                if (it.key == requireView().findViewById<RadioButton>(checkedId).text) {
                    if (it.key != AlcoholDrinkFilter.NONE.key) {
                        drinkFilters[it.type] = it
                    } else {
                        drinkFilters.remove(it.type)
                    }
                }
            }
        }
    }

    override fun onBottomSheetDialogFragmentClick(
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
                println(alcoholType.key + " alcoholType from alcohol filter")
            }
            CategoryDrinkType -> {
                val categoryType = data as CategoryDrinkFilter
                if (categoryType != CategoryDrinkFilter.NONE) {
                    drinkFilters[categoryType.type] = categoryType
                } else {
                    drinkFilters.remove(categoryType.type)
                }
                println(categoryType.key + " categoryType from alcohol filter")
            }
            IngredientDrinkType -> {
                val categoryType = data as Ingredient
                val ingredient = IngredientDrinkFilter.INGREDIENT
                ingredient.key = categoryType.strIngredient1!!
                drinkFilters[ingredient.type] = ingredient
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
            viewId: Int,
            drinkFilters: ArrayList<DrinkFilter> = arrayListOf()
        ) = FilterFragment().apply {
            arguments = Bundle().apply {
                putInt(FRAGMENT_ID, viewId)
                drinkFilters.forEach {
                    putString(it.type.key, it.key)
                }
            }
        }
    }
}