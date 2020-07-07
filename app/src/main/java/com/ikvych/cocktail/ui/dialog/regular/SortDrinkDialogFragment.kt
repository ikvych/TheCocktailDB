package com.ikvych.cocktail.ui.dialog.regular

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.ui.dialog.base.ItemListDialogButton
import com.ikvych.cocktail.ui.dialog.base.ListBaseDialogFragment
import com.ikvych.cocktail.ui.dialog.base.ListDialogButton
import com.ikvych.cocktail.ui.dialog.base.SortDrinkDrinkDialogType


class SortDrinkDialogFragment :
    ListBaseDialogFragment<SortDrinkType?, ListDialogButton, SortDrinkDrinkDialogType>() {

    override val dialogType: SortDrinkDrinkDialogType =
        SortDrinkDrinkDialogType
    override var data: SortDrinkType? = SortDrinkType.RECENT
    private var selectedSortType: SortDrinkType? = SortDrinkType.RECENT
    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()

    override val dialogListDataAdapter: DialogListDataAdapter<SortDrinkType?> =
        object : DialogListDataAdapter<SortDrinkType?> {
            override fun getName(data: SortDrinkType?): CharSequence {
                return data?.key ?: ""
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
        val sortTypeOrdinal = requireArguments().getInt(EXTRA_KEY_SORT_TYPE)
        selectedSortType = SortDrinkType.values()[sortTypeOrdinal]
        listAdapter = DialogListAdapter(selectedSortType)
    }

    override var listData: List<SortDrinkType?> = mutableListOf<SortDrinkType?>().apply {
        addAll(SortDrinkType.values())
    }.toList()

    override fun getButtonType(view: View): ListDialogButton {
        return when (view.id) {
            R.id.tv_filter_type_element -> ItemListDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }


    override fun obtainDataForView(view: View): SortDrinkType? {
        return when (getButtonType(view)) {
            is ItemListDialogButton -> view.tag as? SortDrinkType?
            else -> super.obtainDataForView(view)
        }
    }

    companion object {
        fun newInstance(
            sortType: SortDrinkType? = null
        ): SortDrinkDialogFragment {
            return SortDrinkDialogFragment()
                .apply {
                arguments = bundleOf(
                    EXTRA_KEY_BUILDER to SimpleDialogBuilder().apply {
                        titleTextResId = R.string.dialog_sort_title
                        isCancelable = true
                    },
                    EXTRA_KEY_SORT_TYPE to sortType?.ordinal
                )
            }
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_SORT_TYPE = "EXTRA_KEY_SORT_TYPE"
    }
}