package com.ikvych.cocktail.presentation.dialog.regular

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.util.DRINK_FILTER_ABSENT
import com.ikvych.cocktail.presentation.filter.type.DrinkFilterType
import com.ikvych.cocktail.presentation.dialog.base.MultiSelectionListBaseDialogFragment
import com.ikvych.cocktail.presentation.dialog.type.*
import com.ikvych.cocktail.presentation.model.cocktail.IngredientModel


class ListFilterDrinkDialogFragment :
    MultiSelectionListBaseDialogFragment<List<IngredientModel>, IngredientModel, ListDialogButton, FilterListDrinkDialogType>(),
    MultiSelectionListBaseDialogFragment.OnMultiSelectionListClick {

    override val dialogType: FilterListDrinkDialogType = FilterListDrinkDialogType
    override var data: List<IngredientModel>? =
        arrayListOf(IngredientModel(DrinkFilterType.INGREDIENT,
            DRINK_FILTER_ABSENT
        ))
    override var selectedElements: ArrayList<IngredientModel> =
        arrayListOf(IngredientModel(DrinkFilterType.INGREDIENT,
            DRINK_FILTER_ABSENT
        ))
    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()
    override val onClickListener: OnMultiSelectionListClick = this

    override val dialogListDataAdapter: DialogListDataAdapter<IngredientModel> =
        object : DialogListDataAdapter<IngredientModel> {
            override fun getName(data: IngredientModel): CharSequence {
                return data.key
            }
        }

    override var listElement: List<IngredientModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
        val allKeysArray = requireArguments().getStringArray(EXTRA_KEY_ALL_INGREDIENTS)
        val keysArray = requireArguments().getStringArray(EXTRA_KEY_SELECTED_INGREDIENTS)
        selectedElements = keysArray?.map {
            IngredientModel(
                DrinkFilterType.INGREDIENT,
                it
            )
        } as? ArrayList<IngredientModel> ?: arrayListOf(
            IngredientModel(
                DrinkFilterType.INGREDIENT,
                DRINK_FILTER_ABSENT
            )
        )
        listAdapter = DialogListAdapter()
        listElement = allKeysArray?.map {
            IngredientModel(
                DrinkFilterType.INGREDIENT,
                it
            )
        } as? ArrayList<IngredientModel> ?: arrayListOf(
            IngredientModel(
                DrinkFilterType.INGREDIENT,
                DRINK_FILTER_ABSENT
            )
        )
    }



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

    override fun onListItemClick(v: View?) {
        if (v == null) return
        val currentElement = v.tag as IngredientModel
        if (currentElement.key == DRINK_FILTER_ABSENT) {
            selectedElements.forEach {
                val noneElement: AppCompatButton? = extraContentView?.findViewWithTag(it)
                noneElement?.isSelected = false
            }
            selectedElements.clear()
            selectedElements.add(currentElement)
            (v as AppCompatButton).isSelected = true
            return
        } else if (selectedElements.contains(currentElement)) {
            selectedElements.remove(currentElement)
            (v as AppCompatButton).isSelected = false
        } else {
            selectedElements.add(currentElement)
            (v as AppCompatButton).isSelected = true
        }
        if (selectedElements.contains(
                IngredientModel(DrinkFilterType.INGREDIENT,
                    DRINK_FILTER_ABSENT
                )
            ) && selectedElements.size > 1
        ) {
            selectedElements.remove(selectedElements.find {
                it.key == DRINK_FILTER_ABSENT
            })
            val noneElement: AppCompatButton? =
                extraContentView?.findViewWithTag(IngredientModel(DrinkFilterType.INGREDIENT,
                    DRINK_FILTER_ABSENT
                ))
            noneElement?.isSelected = false
        }
        if (selectedElements.isEmpty()) {
            selectedElements.add(IngredientModel(DrinkFilterType.INGREDIENT,
                DRINK_FILTER_ABSENT
            ))
            val noneElement: AppCompatButton? =
                extraContentView?.findViewWithTag(IngredientModel(DrinkFilterType.INGREDIENT,
                    DRINK_FILTER_ABSENT
                ))
            noneElement?.isSelected = true
        }
    }


    companion object {
        fun newInstance(
            selectedIngredients: List<IngredientModel> = arrayListOf(),
            allIngredients: List<IngredientModel>
        ): ListFilterDrinkDialogFragment {
            val keys = selectedIngredients.map { it.key }
            val keysArray = Array(keys.size) {
                keys[it]
            }

            val allIngredientsKey = allIngredients.map { it.key }
            val allKeysArray = Array(allIngredientsKey.size) {
                allIngredientsKey[it]
            }
            return ListFilterDrinkDialogFragment()
                .apply {
                    arguments = bundleOf(
                        EXTRA_KEY_BUILDER to SimpleDialogBuilder().apply {
                            titleTextResId = R.string.dialog_filter_title
                            isCancelable = true
                            isCloseButtonVisible = true
                            rightButtonText = "Ok"
                            leftButtonText = "Cancel"
                        },
                        EXTRA_KEY_SELECTED_INGREDIENTS to keysArray,
                        EXTRA_KEY_ALL_INGREDIENTS to allKeysArray
                    )
                }
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_SELECTED_INGREDIENTS = "EXTRA_KEY_SELECTED_INGREDIENTS"
        private const val EXTRA_KEY_ALL_INGREDIENTS = "EXTRA_KEY_ALL_INGREDIENTS"
    }
}