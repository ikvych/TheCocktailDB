package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.ui.dialog.base.ListBaseDialogFragment
import com.ikvych.cocktail.ui.dialog.base.type.CategoryDrinkDialogType
import com.ikvych.cocktail.ui.dialog.base.type.ItemListDialogButton
import com.ikvych.cocktail.ui.dialog.base.type.ListDialogButton


class FilterDrinkCategoryDialogFragment :
    ListBaseDialogFragment<CategoryDrinkFilter?, ListDialogButton, CategoryDrinkDialogType>() {

    override val dialogType: CategoryDrinkDialogType = CategoryDrinkDialogType
    override var data: CategoryDrinkFilter? = CategoryDrinkFilter.NONE
    private var selectedCategoryDrinkFilter: CategoryDrinkFilter? = CategoryDrinkFilter.NONE
    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()

    override val dialogListDataAdapter: DialogListDataAdapter<CategoryDrinkFilter?> =
        object : DialogListDataAdapter<CategoryDrinkFilter?> {
            override fun getName(data: CategoryDrinkFilter?): CharSequence {
                return data?.key ?: ""
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
        val categoryOrdinal = requireArguments().getInt(EXTRA_KEY_SELECTED_CATEGORY)
        selectedCategoryDrinkFilter = CategoryDrinkFilter.values()[categoryOrdinal]
        listAdapter = DialogListAdapter(arrayListOf(selectedCategoryDrinkFilter))
    }

    override var listData: List<CategoryDrinkFilter?> =
        mutableListOf<CategoryDrinkFilter?>().apply {
            addAll(CategoryDrinkFilter.values())
        }.toList()

    override fun getButtonType(view: View): ListDialogButton {
        return when (view.id) {
            R.id.tv_filter_type_element -> ItemListDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }


    override fun obtainDataForView(view: View): CategoryDrinkFilter? {
        return when (getButtonType(view)) {
            is ItemListDialogButton -> view.tag as? CategoryDrinkFilter?
            else -> super.obtainDataForView(view)
        }
    }

    companion object {
        fun newInstance(selectedCategory: CategoryDrinkFilter? = null): FilterDrinkCategoryDialogFragment {
            return FilterDrinkCategoryDialogFragment().apply {
                arguments = bundleOf(
                    EXTRA_KEY_BUILDER to SimpleDialogBuilder().apply {
                        titleTextResId = R.string.dialog_sort_title
                        isCancelable = true
                        isCloseButtonVisible = true
                        rightButtonText = "Accept"
                        leftButtonText = "Cancel"
                    },
                    EXTRA_KEY_SELECTED_CATEGORY to selectedCategory?.ordinal
                )
            }
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_SELECTED_CATEGORY = "EXTRA_KEY_SELECTED_CATEGORY"
    }
}