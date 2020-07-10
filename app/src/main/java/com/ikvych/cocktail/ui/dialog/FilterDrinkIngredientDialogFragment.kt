package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.ui.dialog.base.ListBaseDialogFragment
import com.ikvych.cocktail.ui.dialog.base.type.IngredientDrinkDialogType
import com.ikvych.cocktail.ui.dialog.base.type.ItemListDialogButton
import com.ikvych.cocktail.ui.dialog.base.type.ListDialogButton


class FilterDrinkIngredientDialogFragment :
    ListBaseDialogFragment<IngredientDrinkFilter?, ListDialogButton, IngredientDrinkDialogType>() {

    override val dialogType: IngredientDrinkDialogType = IngredientDrinkDialogType
    override var data: IngredientDrinkFilter? = IngredientDrinkFilter.NONE
    private var selectedIngredientDrinkFilter: IngredientDrinkFilter? = IngredientDrinkFilter.NONE
    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()

    override val dialogListDataAdapter: DialogListDataAdapter<IngredientDrinkFilter?> =
        object : DialogListDataAdapter<IngredientDrinkFilter?> {
            override fun getName(data: IngredientDrinkFilter?): CharSequence {
                return data?.key ?: ""
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
        val ingredientOrdinal = requireArguments().getInt(EXTRA_KEY_SELECTED_INGREDIENT)
        selectedIngredientDrinkFilter = IngredientDrinkFilter.values()[ingredientOrdinal]
        listAdapter = DialogListAdapter(selectedIngredientDrinkFilter)
    }

    override var listData: List<IngredientDrinkFilter?> = mutableListOf<IngredientDrinkFilter?>().apply {
        addAll(IngredientDrinkFilter.values())
    }.toList()

    override fun getButtonType(view: View): ListDialogButton {
        return when (view.id) {
            R.id.tv_filter_type_element -> ItemListDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }

    override fun obtainDataForView(view: View): IngredientDrinkFilter? {
        return when (getButtonType(view)) {
            is ItemListDialogButton -> view.tag as? IngredientDrinkFilter?
            else -> super.obtainDataForView(view)
        }
    }

    companion object {
        fun newInstance(
            selectedIngredient: IngredientDrinkFilter? = null
        ): FilterDrinkIngredientDialogFragment {
            return FilterDrinkIngredientDialogFragment()
                .apply {
                    arguments = bundleOf(
                        EXTRA_KEY_BUILDER to SimpleDialogBuilder().apply {
                            titleTextResId = R.string.dialog_sort_title
                            isCancelable = true
                        },
                        EXTRA_KEY_SELECTED_INGREDIENT to selectedIngredient?.ordinal
                    )
                }
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_SELECTED_INGREDIENT = "EXTRA_KEY_SELECTED_INGREDIENT"
    }
}