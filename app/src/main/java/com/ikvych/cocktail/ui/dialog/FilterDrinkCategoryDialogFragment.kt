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
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.ui.base.ItemListDialogButton
import com.ikvych.cocktail.ui.base.ListDialogButton
import com.ikvych.cocktail.ui.base.CategoryDrinkType
import com.ikvych.cocktail.ui.base.ListBaseDialogFragment


class FilterDrinkCategoryDialogFragment :
    ListBaseDialogFragment<CategoryDrinkFilter?, ListDialogButton, CategoryDrinkType>() {

    override val dialogType: CategoryDrinkType = CategoryDrinkType
    override var data: CategoryDrinkFilter? = CategoryDrinkFilter.NONE
    private val selectedAlcoholDrinkFilter: CategoryDrinkFilter? = null
    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()
    override val listAdapter = SortDrinkListAdapter()

    override val dialogListDataAdapter: DialogListDataAdapter<CategoryDrinkFilter?> =
        object : DialogListDataAdapter<CategoryDrinkFilter?> {
            override fun getName(data: CategoryDrinkFilter?): CharSequence {
                return data?.key ?: ""
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
    }

    override var listData: List<CategoryDrinkFilter?> = mutableListOf<CategoryDrinkFilter?>().apply {
        addAll(CategoryDrinkFilter.values())
    }.toList()

    inner class SortDrinkListAdapter :
        BaseAdapter<CategoryDrinkFilter?, BaseViewHolder>(R.layout.item_dialog_filter_list),
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
                setOnClickListener(this@FilterDrinkCategoryDialogFragment)
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
            R.id.tv_item_sort_list -> ItemListDialogButton
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
        fun newInstance(selectedAlcohol: CategoryDrinkFilter? = null): FilterDrinkCategoryDialogFragment {
            return FilterDrinkCategoryDialogFragment().apply {
                arguments = bundleOf(
                    EXTRA_KEY_BUILDER to SimpleDialogBuilder().apply {
                        titleTextResId = R.string.dialog_sort_title
                        isCancelable = true
                    },
                    EXTRA_KEY_SELECTED_CATEGORY to selectedAlcohol
                )
            }
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_SELECTED_CATEGORY = "EXTRA_KEY_SELECTED_SEX"
    }
}