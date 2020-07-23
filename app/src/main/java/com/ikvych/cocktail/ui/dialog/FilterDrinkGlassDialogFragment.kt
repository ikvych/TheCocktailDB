package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.GlassDrinkFilter
import com.ikvych.cocktail.ui.dialog.base.ListBaseDialogFragment
import com.ikvych.cocktail.ui.dialog.base.type.AlcoholDrinkDialogType
import com.ikvych.cocktail.ui.dialog.base.type.GlassDrinkDialogType
import com.ikvych.cocktail.ui.dialog.base.type.ItemListDialogButton
import com.ikvych.cocktail.ui.dialog.base.type.ListDialogButton


class FilterDrinkGlassDialogFragment :
    ListBaseDialogFragment<GlassDrinkFilter?, ListDialogButton, GlassDrinkDialogType>() {

    override val dialogType: GlassDrinkDialogType = GlassDrinkDialogType
    override var data: GlassDrinkFilter? = GlassDrinkFilter.NONE
    private var selectedGlassDrinkFilter: GlassDrinkFilter? = GlassDrinkFilter.NONE
    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()

    override val dialogListDataAdapter: DialogListDataAdapter<GlassDrinkFilter?> =
        object : DialogListDataAdapter<GlassDrinkFilter?> {
            override fun getName(data: GlassDrinkFilter?): CharSequence {
                return data?.key ?: ""
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
        val glassOrdinal = requireArguments().getInt(EXTRA_KEY_SELECTED_GLASS)
        selectedGlassDrinkFilter = GlassDrinkFilter.values()[glassOrdinal]
        listAdapter = DialogListAdapter(selectedGlassDrinkFilter)
    }

    override var listData: List<GlassDrinkFilter?> = mutableListOf<GlassDrinkFilter?>().apply {
        addAll(GlassDrinkFilter.values())
    }.toList()

    override fun getButtonType(view: View): ListDialogButton {
        return when (view.id) {
            R.id.tv_filter_type_element -> ItemListDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }


    override fun obtainDataForView(view: View): GlassDrinkFilter? {
        return when (getButtonType(view)) {
            is ItemListDialogButton -> view.tag as? GlassDrinkFilter?
            else -> super.obtainDataForView(view)
        }
    }

    companion object {
        fun newInstance(selectedGlass: GlassDrinkFilter? = null): FilterDrinkGlassDialogFragment {
            return FilterDrinkGlassDialogFragment().apply {
                arguments = bundleOf(
                    EXTRA_KEY_BUILDER to SimpleDialogBuilder().apply {
                        titleTextResId = R.string.dialog_sort_title
                        isCancelable = true
                        isCloseButtonVisible = true
                    },
                    EXTRA_KEY_SELECTED_GLASS to selectedGlass?.ordinal
                )
            }
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_SELECTED_GLASS = "EXTRA_KEY_SELECTED_ALCOHOL"
    }
}