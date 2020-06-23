package com.ikvych.cocktail.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.ui.activity.MainActivity
import com.ikvych.cocktail.ui.base.BaseFragment
import com.ikvych.cocktail.ui.base.FRAGMENT_ID
import com.ikvych.cocktail.widget.custom.ApplicationToolBar

class FilterFragment : BaseFragment() {

    private lateinit var alcoholRadioGroup: RadioGroup
    private lateinit var categoryRadioGroup: RadioGroup
    private lateinit var returnBtn: ImageButton
    private lateinit var acceptBtn: Button
    private lateinit var resetBtn: Button

    private val drinkFilters: HashMap<DrinkFilterType, DrinkFilter> = hashMapOf()

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

        alcoholRadioGroup = requireView().findViewById(R.id.alcohol_radio_group)
        categoryRadioGroup = requireView().findViewById(R.id.category_radio_group)
        initCategoryFilters()
        initAlcoholFilters()

        acceptBtn = view.findViewById(R.id.btn_accept)
        acceptBtn.setOnClickListener {
            fragmentListener!!.onFilterApply(*drinkFilters.values.toTypedArray())
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

    interface OnFilterResultListener {
        fun onFilterApply(vararg drinkFilters: DrinkFilter)
        fun onFilterReset()
    }

    companion object {

        @JvmStatic
        fun newInstance(
            viewId: Int,
            vararg drinkFilters: DrinkFilter
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