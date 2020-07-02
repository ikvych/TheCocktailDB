package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.button.MaterialButton
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.base.BaseAdapter
import com.ikvych.cocktail.adapter.list.base.BaseViewHolder
import com.ikvych.cocktail.data.entity.Ingredient
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.ui.base.*


class FilterDrinkIngredientDialogFragment :
    ListBaseDialogFragment<IngredientDrinkFilter?, ListDialogButton, IngredientDrinkType>() {

    override val dialogType: IngredientDrinkType = IngredientDrinkType
    override var data: IngredientDrinkFilter? = IngredientDrinkFilter.NONE
    private val selectedAlcoholDrinkFilter: IngredientDrinkFilter? = null
    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()
    override val listAdapter = SortDrinkListAdapter()

    override val dialogListDataAdapter: DialogListDataAdapter<IngredientDrinkFilter?> =
        object : DialogListDataAdapter<IngredientDrinkFilter?> {
            override fun getName(data: IngredientDrinkFilter?): CharSequence {
                return data?.key ?: ""
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
    }

    override var listData: List<IngredientDrinkFilter?> = mutableListOf<IngredientDrinkFilter?>().apply {
        addAll(IngredientDrinkFilter.values())
    }.toList()

    inner class SortDrinkListAdapter :
        BaseAdapter<IngredientDrinkFilter?, BaseViewHolder>(R.layout.item_filter_type),
        View.OnClickListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val itemView: View = LayoutInflater.from(parent.context)
                .inflate(layoutId, parent, false)
            return BaseViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            with(holder.itemView as MaterialButton) {
                text = dialogListDataAdapter.getName(newData[position])
                tag = newData[position]
                isSelected = (data == selectedAlcoholDrinkFilter)
                setOnClickListener(this@FilterDrinkIngredientDialogFragment)
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun onClick(v: View?) {
            /**
             * be sure to override method [obtainDataForView] and handle your model [Data]
             */
            callOnClick(v ?: return, getButtonType(v))
        }
    }

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
            ingredientList: List<Ingredient>? = null,
            selectedAlcohol: AlcoholDrinkFilter? = null
        ): FilterDrinkIngredientDialogFragment {
            return FilterDrinkIngredientDialogFragment().apply {
                arguments = bundleOf(
                    EXTRA_KEY_BUILDER to SimpleDialogBuilder().apply {
                        titleTextResId = R.string.dialog_sort_title
                        isCancelable = true
                    },
                    EXTRA_KEY_SELECTED_INGREDIENT to selectedAlcohol
                )
/*                arguments.also {bundle ->
                    ingredientList.forEach {
                        bundle!!.putString(it.id.toString(), it.strIngredient1)
                    }
                    bundle!!.putInt(EXTRA_KEY_INGREDIENT_LIST_SIZE, ingredientList.size)
                }*/
            }
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_SELECTED_INGREDIENT = "EXTRA_KEY_SELECTED_INGREDIENT"
        private const val EXTRA_KEY_INGREDIENT_LIST_SIZE = "EXTRA_KEY_INGREDIENT_LIST_SIZE"
    }
}