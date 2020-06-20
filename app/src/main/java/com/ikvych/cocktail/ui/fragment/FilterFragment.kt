package com.ikvych.cocktail.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.ui.activity.MainActivity
import com.ikvych.cocktail.ui.base.BaseFragment
import com.ikvych.cocktail.ui.base.ALCOHOL_FILTER_BUNDLE_KEY
import com.ikvych.cocktail.ui.base.ALCOHOL_FILTER_KEY
import com.ikvych.cocktail.ui.base.FRAGMENT_ID
import java.util.*

class FilterFragment : BaseFragment() {

    lateinit var radioBtn: RadioButton
    lateinit var alcoholRadioGroup: RadioGroup

    lateinit var fragmentListener: OnFilterFragmentListener

    lateinit var acceptBtn: Button
    lateinit var resetBtn: Button

    private lateinit var alcoholDrinkFilter: AlcoholDrinkFilter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            fragmentListener = activity as OnFilterFragmentListener
            val activity = requireActivity() as MainActivity
            activity.filterBtn.visibility = View.GONE
        } catch (exception: ClassCastException) {
            throw ClassCastException("${activity.toString()} must implement FragmentListener")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            val activity = requireActivity() as MainActivity
            activity.filterBtn.visibility = View.VISIBLE
        } catch (exception: ClassCastException) {
            throw ClassCastException("${activity.toString()} must implement FragmentListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alcoholRadioGroup = requireView().findViewById(R.id.alcohol_radio_group)
        initAlcoholFilters()

        acceptBtn = requireView().findViewById(R.id.btn_accept)
        acceptBtn.setOnClickListener {
/*            setFragmentResult(
                ALCOHOL_FILTER_KEY,
                bundleOf(ALCOHOL_FILTER_BUNDLE_KEY to alcoholDrinkFilter.name)
            )*/
            fragmentListener.onFilterApply(alcoholDrinkFilter)
        }
        resetBtn = requireView().findViewById(R.id.btn_reject)
        resetBtn.setOnClickListener {
            radioBtn = requireView().findViewById(R.id.rb_none)
            radioBtn.isChecked = true
        }
    }

    private fun initAlcoholFilters() {
        var alcoholType = requireArguments().getString(DrinkFilterType.ALCOHOL.key)
            ?: AlcoholDrinkFilter.ALCOHOLIC.key
        alcoholType = alcoholType.toUpperCase(Locale.getDefault()).replace(" ", "_")
        alcoholDrinkFilter = AlcoholDrinkFilter.valueOf(alcoholType)

        radioBtn = when (alcoholDrinkFilter) {
            AlcoholDrinkFilter.ALCOHOLIC -> requireView().findViewById(R.id.rb_alcoholic)
            AlcoholDrinkFilter.NON_ALCOHOLIC -> requireView().findViewById(R.id.rb_non_alcoholic)
            AlcoholDrinkFilter.OPTIONAL_ALCOHOL -> requireView().findViewById(R.id.rb_optional_alcoholic)
            AlcoholDrinkFilter.NONE -> requireView().findViewById(R.id.rb_none)
        }
        radioBtn.isChecked = true

        alcoholRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            radioBtn = requireView().findViewById(checkedId)
            alcoholDrinkFilter = when (radioBtn.id) {
                R.id.rb_alcoholic -> AlcoholDrinkFilter.ALCOHOLIC
                R.id.rb_non_alcoholic -> AlcoholDrinkFilter.NON_ALCOHOLIC
                R.id.rb_optional_alcoholic -> AlcoholDrinkFilter.OPTIONAL_ALCOHOL
                else -> AlcoholDrinkFilter.NONE
            }
        }
    }

    interface OnFilterFragmentListener {
        fun onFilterApply(alcoholDrinkFilter: AlcoholDrinkFilter)
        fun onFilterRest()
    }

    companion object {

        @JvmStatic
        fun newInstance(
            viewId: Int,
            vararg drinkFilters: DrinkFilter = arrayOf(AlcoholDrinkFilter.NON_ALCOHOLIC)
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