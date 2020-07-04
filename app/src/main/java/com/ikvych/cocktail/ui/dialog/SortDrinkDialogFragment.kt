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
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.ui.base.*


class SortDrinkDialogFragment :
    ListBaseDialogFragment<SortDrinkType?, ListDialogButton, SortDrinkDrinkDialogType>() {

    override val dialogType: SortDrinkDrinkDialogType = SortDrinkDrinkDialogType
    override var data: SortDrinkType? = SortDrinkType.RECENT
    private var selectedSortType: SortDrinkType? = SortDrinkType.RECENT
    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()
    override val listAdapter = SortDrinkListAdapter()

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
    }

    override var listData: List<SortDrinkType?> = mutableListOf<SortDrinkType?>().apply {
        addAll(SortDrinkType.values())
    }.toList()

    inner class SortDrinkListAdapter :
        BaseAdapter<SortDrinkType?, BaseViewHolder>(R.layout.item_filter_type) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val itemView: View = LayoutInflater.from(parent.context)
                .inflate(layoutId, parent, false)
            return BaseViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            with(holder.itemView as MaterialButton) {
                text = dialogListDataAdapter.getName(newData[position])
                tag = newData[position]
                isEnabled = (tag != selectedSortType)
                setOnClickListener(this@SortDrinkDialogFragment)
            }
        }
    }

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
            return SortDrinkDialogFragment().apply {
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