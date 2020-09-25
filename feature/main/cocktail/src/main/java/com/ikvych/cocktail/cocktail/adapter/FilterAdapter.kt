package com.ikvych.cocktail.cocktail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ikvych.cocktail.cocktail.adapter.base.BaseRecyclerViewAdapter
import com.ikvych.cocktail.prresentation.filter.DrinkFilter


class FilterAdapter(
    private val viewModel: com.ikvych.cocktail.cocktail.MainFragmentViewModel
) : BaseRecyclerViewAdapter<DrinkFilter>() {

    private val layoutId = com.ikvych.cocktail.prresentation.R.layout.item_selected_filter_list

    fun setData(list: List<DrinkFilter>) {
        listData = list.filter { it.key != "None" }
    }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
    }

    override fun bind(binding: ViewDataBinding, item: DrinkFilter) {
/*        when (binding) {
            is ItemSelectedFilterListBinding -> {
                binding.obj = item
                binding.viewModel = viewModel
            }
        }*/
    }

}