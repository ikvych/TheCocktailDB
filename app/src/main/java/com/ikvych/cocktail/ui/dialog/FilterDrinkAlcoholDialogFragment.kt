package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.ui.dialog.base.ListBaseDialogFragment
import com.ikvych.cocktail.ui.dialog.base.type.AlcoholDrinkDialogType
import com.ikvych.cocktail.ui.dialog.base.type.ItemListDialogButton
import com.ikvych.cocktail.ui.dialog.base.type.ListDialogButton


class FilterDrinkAlcoholDialogFragment :
    ListBaseDialogFragment<AlcoholDrinkFilter?, ListDialogButton, AlcoholDrinkDialogType>() {

    override val dialogType: AlcoholDrinkDialogType = AlcoholDrinkDialogType
    override var data: AlcoholDrinkFilter? = AlcoholDrinkFilter.NONE
    private var selectedAlcoholDrinkFilter: AlcoholDrinkFilter? = AlcoholDrinkFilter.NONE
    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()

    override val dialogListDataAdapter: DialogListDataAdapter<AlcoholDrinkFilter?> =
        object : DialogListDataAdapter<AlcoholDrinkFilter?> {
            override fun getName(data: AlcoholDrinkFilter?): CharSequence {
                return data?.key ?: ""
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
        val alcoholOrdinal = requireArguments().getInt(EXTRA_KEY_SELECTED_ALCOHOL)
        selectedAlcoholDrinkFilter = AlcoholDrinkFilter.values()[alcoholOrdinal]
        listAdapter = DialogListAdapter(arrayListOf(selectedAlcoholDrinkFilter))
    }

    override var listData: List<AlcoholDrinkFilter?> = mutableListOf<AlcoholDrinkFilter?>().apply {
        addAll(AlcoholDrinkFilter.values())
    }.toList()

    override fun getButtonType(view: View): ListDialogButton {
        return when (view.id) {
            R.id.tv_filter_type_element -> ItemListDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }


    override fun obtainDataForView(view: View): AlcoholDrinkFilter? {
        return when (getButtonType(view)) {
            is ItemListDialogButton -> view.tag as? AlcoholDrinkFilter?
            else -> super.obtainDataForView(view)
        }
    }

    companion object {
        fun newInstance(selectedAlcohol: AlcoholDrinkFilter? = null): FilterDrinkAlcoholDialogFragment {
            return FilterDrinkAlcoholDialogFragment().apply {
                arguments = bundleOf(
                    EXTRA_KEY_BUILDER to SimpleDialogBuilder().apply {
                        titleTextResId = R.string.dialog_sort_title
                        isCancelable = true
                        isCloseButtonVisible = true
                        rightButtonText = "Accept"
                        leftButtonText = "Cancel"
                    },
                    EXTRA_KEY_SELECTED_ALCOHOL to selectedAlcohol?.ordinal
                )
            }
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_SELECTED_ALCOHOL = "EXTRA_KEY_SELECTED_ALCOHOL"
    }
}