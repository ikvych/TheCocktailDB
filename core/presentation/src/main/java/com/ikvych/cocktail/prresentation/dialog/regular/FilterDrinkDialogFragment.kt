package com.ikvych.cocktail.prresentation.dialog.regular

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.ikvych.cocktail.prresentation.R
import com.ikvych.cocktail.prresentation.filter.DrinkFilter
import com.ikvych.cocktail.prresentation.dialog.type.ItemListDialogButton
import com.ikvych.cocktail.prresentation.dialog.type.ListDialogButton
import com.ikvych.cocktail.prresentation.dialog.base.ListBaseDialogFragment
import com.ikvych.cocktail.prresentation.dialog.type.FilterDrinkDialogType
import com.ikvych.cocktail.prresentation.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.prresentation.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.prresentation.filter.type.DrinkFilterType
import com.ikvych.cocktail.prresentation.filter.type.GlassDrinkFilter
import java.lang.IllegalArgumentException


class FilterDrinkDialogFragment :
    ListBaseDialogFragment<DrinkFilter, ListDialogButton, FilterDrinkDialogType>() {

    private var selectedDrinkFilter: DrinkFilter? = null

    override val dialogType: FilterDrinkDialogType = FilterDrinkDialogType
    override var data: DrinkFilter? = null
    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()

    override val dialogListDataAdapter: DialogListDataAdapter<DrinkFilter> =
        object : DialogListDataAdapter<DrinkFilter> {
            override fun getName(data: DrinkFilter): CharSequence {
                return data.key
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
        val drinkFilterKey = requireArguments().getString(EXTRA_KEY_SELECTED_FILTER_KEY)
        val drinkFilterTypeOrdinal = requireArguments().getInt(EXTRA_KEY_SELECTED_FILTER_TYPE, -1)

        selectedDrinkFilter = when (drinkFilterTypeOrdinal) {
            DrinkFilterType.ALCOHOL.ordinal -> {
                AlcoholDrinkFilter.values().first { it.key == drinkFilterKey}}
            DrinkFilterType.CATEGORY.ordinal -> {
                CategoryDrinkFilter.values().first { it.key == drinkFilterKey}}
            DrinkFilterType.GLASS.ordinal -> {
                GlassDrinkFilter.values().first { it.key == drinkFilterKey}}
            else -> throw IllegalArgumentException("Unknown drink filter")
        }

        listAdapter = DialogListAdapter(selectedDrinkFilter)
        initListData()
    }

    private fun initListData() {
        listData = mutableListOf<DrinkFilter>().apply {
            if (selectedDrinkFilter is AlcoholDrinkFilter) {
                addAll(AlcoholDrinkFilter.values())
            }
            if (selectedDrinkFilter is CategoryDrinkFilter) {
                addAll(CategoryDrinkFilter.values())
            }
            if (selectedDrinkFilter is GlassDrinkFilter) {
                addAll(GlassDrinkFilter.values())
            }
        }.toList()
    }

    override fun getButtonType(view: View): ListDialogButton {
        return when (view.id) {
            R.id.tv_button_item -> ItemListDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }


    override fun obtainDataForView(view: View): DrinkFilter? {
        return when (getButtonType(view)) {
            is ItemListDialogButton -> view.tag as? DrinkFilter?
            else -> super.obtainDataForView(view)
        }
    }

    companion object {
        fun newInstance(drinkFilter: DrinkFilter): FilterDrinkDialogFragment {
            return FilterDrinkDialogFragment().apply {
                arguments = bundleOf(
                    EXTRA_KEY_BUILDER to SimpleDialogBuilder().apply {
                        titleTextResId = R.string.dialog_filter_title
                        isCancelable = true
                        isCloseButtonVisible = true
                    },
                    EXTRA_KEY_SELECTED_FILTER_KEY to drinkFilter.key,
                    EXTRA_KEY_SELECTED_FILTER_TYPE to drinkFilter.type.ordinal
                )
            }
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_SELECTED_FILTER_KEY = "EXTRA_KEY_SELECTED_FILTER_KEY"
        private const val EXTRA_KEY_SELECTED_FILTER_TYPE = "EXTRA_KEY_SELECTED_FILTER_TYPE"
    }
}