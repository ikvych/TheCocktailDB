package com.ikvych.cocktail.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.ui.activity.MainActivity
import com.ikvych.cocktail.ui.base.BaseFragment
import com.ikvych.cocktail.ui.base.FRAGMENT_ID

class FilterFragment : BaseFragment() {

    lateinit var alcoholRadioGroup: RadioGroup
    lateinit var categoryRadioGroup: RadioGroup

    val drinkFilters: HashMap<DrinkFilterType, DrinkFilter> = hashMapOf()

    lateinit var fragmentListener: OnFilterResultListener

    lateinit var acceptBtn: Button
    lateinit var resetBtn: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            fragmentListener = activity as OnFilterResultListener
            val activity = requireActivity() as MainActivity
            activity.filterBtn.visibility = View.GONE
            activity.indicatorView.visibility = View.GONE
        } catch (exception: ClassCastException) {
            throw ClassCastException("${activity.toString()} must implement OnFilterResultListener")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            val activity = requireActivity() as MainActivity
            activity.filterBtn.visibility = View.VISIBLE
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

        acceptBtn = requireView().findViewById(R.id.btn_accept)
        acceptBtn.setOnClickListener {
/*            setFragmentResult(
                ALCOHOL_FILTER_KEY,
                bundleOf(ALCOHOL_FILTER_BUNDLE_KEY to alcoholDrinkFilter.name)
            )*/
            fragmentListener.onFilterApply(*drinkFilters.values.toTypedArray())
        }
        resetBtn = requireView().findViewById(R.id.btn_reject)
        resetBtn.setOnClickListener {
        }
    }

    private fun initCategoryFilters() {
        val categoryType = requireArguments().getString(DrinkFilterType.CATEGORY.key)
            ?: CategoryDrinkFilter.OTHER_UNKNOWN.key

        CategoryDrinkFilter.values().forEach {
            categoryRadioGroup.addView(
                RadioButton(requireContext()).apply {
                    id = View.generateViewId()
                    text = it.key
                    if (it.key == categoryType) {
                        drinkFilters.put(it.type, it)
                        isChecked = true
                    }
                }
            )
        }

        categoryRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            CategoryDrinkFilter.values().forEach { it ->
                if (it.key == requireView().findViewById<RadioButton>(checkedId).text) {
                    if (it.key != "None") {
                        drinkFilters.put(it.type, it)
                    } else {
                        drinkFilters.remove(it.type)
                    }
                }
            }
        }
    }

    private fun initAlcoholFilters() {
        val alcoholType = requireArguments().getString(DrinkFilterType.ALCOHOL.key)
            ?: AlcoholDrinkFilter.ALCOHOLIC.key

        AlcoholDrinkFilter.values().forEach {
            if (it.key == alcoholType) {
                val alcoholRadioBtn: RadioButton = when (it) {
                    AlcoholDrinkFilter.ALCOHOLIC -> requireView().findViewById(R.id.rb_alcoholic)
                    AlcoholDrinkFilter.NON_ALCOHOLIC -> requireView().findViewById(R.id.rb_non_alcoholic)
                    AlcoholDrinkFilter.OPTIONAL_ALCOHOL -> requireView().findViewById(R.id.rb_optional_alcoholic)
                    AlcoholDrinkFilter.NONE -> requireView().findViewById(R.id.rb_none)
                }
                drinkFilters.put(it.type, it)
                alcoholRadioBtn.isChecked = true
            }
        }

        alcoholRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            AlcoholDrinkFilter.values().forEach { it ->
                if (it.key == requireView().findViewById<RadioButton>(checkedId).text) {
                    if (it.key != "None") {
                        drinkFilters.put(it.type, it)
                    } else {
                        drinkFilters.remove(it.type)
                    }
                }
            }
        }
    }

    interface OnFilterResultListener {
        fun onFilterApply(vararg drinkFilters: DrinkFilter)
        fun onFilterRest()
    }

    companion object {

        @JvmStatic
        fun newInstance(
            viewId: Int,
            vararg drinkFilters: DrinkFilter = arrayOf(
                AlcoholDrinkFilter.NON_ALCOHOLIC,
                CategoryDrinkFilter.COCKTAIL
            )
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