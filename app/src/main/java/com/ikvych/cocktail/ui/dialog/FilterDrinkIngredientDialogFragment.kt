package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.ui.dialog.base.MultiSelectionListBaseDialogFragment
import com.ikvych.cocktail.ui.dialog.base.type.*


class FilterDrinkIngredientDialogFragment :
    MultiSelectionListBaseDialogFragment<List<IngredientDrinkFilter>, IngredientDrinkFilter, ListDialogButton, IngredientDrinkDialogType>(),
    MultiSelectionListBaseDialogFragment.OnMultiSelectionListClick {

    override val dialogType: IngredientDrinkDialogType = IngredientDrinkDialogType
    override var data: List<IngredientDrinkFilter>? = arrayListOf(IngredientDrinkFilter.NONE)
    override var selectedElements: ArrayList<IngredientDrinkFilter> = arrayListOf(IngredientDrinkFilter.NONE)
    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()

    override val dialogListDataAdapter: DialogListDataAdapter<IngredientDrinkFilter> =
        object : DialogListDataAdapter<IngredientDrinkFilter> {
            override fun getName(data: IngredientDrinkFilter): CharSequence {
                return data.key
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
        val ingredientOrdinals = requireArguments().getIntArray(EXTRA_KEY_SELECTED_INGREDIENTS)
        selectedElements = ingredientOrdinals?.map { IngredientDrinkFilter.values()[it] } as? ArrayList<IngredientDrinkFilter> ?: arrayListOf(IngredientDrinkFilter.NONE)
        listAdapter = DialogListAdapter()
    }

    override var listElement: List<IngredientDrinkFilter> = mutableListOf<IngredientDrinkFilter>().apply {
        addAll(IngredientDrinkFilter.values())
    }.toList()

    override fun getButtonType(view: View): ListDialogButton {
        return when (view.id) {
            R.id.b_dialog_left_button -> LeftListDialogButton
            R.id.b_dialog_right_button -> {
                data = selectedElements
                RightListDialogButton
            }
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }

/*    override fun obtainDataForView(view: View): List<IngredientDrinkFilter?> {
        return when (getButtonType(view)) {
            is ItemListDialogButton -> view.tag as? IngredientDrinkFilter?
            else -> super.obtainDataForView(view)
        }
    }*/

    companion object {
        fun newInstance(
            selectedIngredients: List<IngredientDrinkFilter?> = arrayListOf()
        ): FilterDrinkIngredientDialogFragment {
            val ordinals = selectedIngredients.map { it?.ordinal }
            val ordinalsArray = IntArray(ordinals.size)
            ordinals.forEachIndexed { index, element  -> ordinalsArray[index] = element!! }
            return FilterDrinkIngredientDialogFragment()
                .apply {
                    arguments = bundleOf(
                        EXTRA_KEY_BUILDER to SimpleDialogBuilder().apply {
                            titleTextResId = R.string.dialog_sort_title
                            isCancelable = true
                            isCloseButtonVisible = true
                            rightButtonText = "Ok"
                            leftButtonText = "Cancel"
                        },
                        EXTRA_KEY_SELECTED_INGREDIENTS to ordinalsArray
                    )
                }
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_SELECTED_INGREDIENTS = "EXTRA_KEY_SELECTED_INGREDIENTS"
    }

    override val onClickListener: OnMultiSelectionListClick = this

    override fun onListItemClick(v: View?) {
        if (v == null) return
        val currentElement = v.tag as IngredientDrinkFilter
        if (currentElement == IngredientDrinkFilter.NONE) {
            selectedElements.forEach {
                val noneElement: AppCompatButton? = extraContentView?.findViewWithTag(it)
                noneElement?.isSelected = false
            }
            selectedElements.clear()
            selectedElements.add(currentElement)
            (v as AppCompatButton).isSelected = true
            return
        }
        if (selectedElements.contains(currentElement)) {
            selectedElements.remove(currentElement)
            (v as AppCompatButton).isSelected = false
        } else {
            selectedElements.add(currentElement)
            (v as AppCompatButton).isSelected = true
        }
        if (selectedElements.contains(IngredientDrinkFilter.NONE) && selectedElements.size > 1) {
            selectedElements.remove(IngredientDrinkFilter.NONE)
            val noneElement: AppCompatButton? = extraContentView?.findViewWithTag(IngredientDrinkFilter.NONE)
            noneElement?.isSelected = false
        }
        if (selectedElements.isEmpty()) {
            selectedElements.add(IngredientDrinkFilter.NONE)
            val noneElement: AppCompatButton? = extraContentView?.findViewWithTag(IngredientDrinkFilter.NONE)
            noneElement?.isSelected = true
        }
    }
}