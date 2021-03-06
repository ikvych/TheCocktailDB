package com.ikvych.cocktail.presentation.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ikvych.cocktail.R
import com.ikvych.cocktail.presentation.adapter.list.base.BaseRecyclerViewAdapter
import com.ikvych.cocktail.databinding.ItemSelectedFilterListBinding
import com.ikvych.cocktail.presentation.filter.DrinkFilter
import com.ikvych.cocktail.viewmodel.cocktail.MainFragmentViewModel


class FilterAdapter(
    private val viewModel: MainFragmentViewModel
) : BaseRecyclerViewAdapter<DrinkFilter>() {

    private val layoutId = R.layout.item_selected_filter_list

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
        when (binding) {
            is ItemSelectedFilterListBinding -> {
                binding.obj = item
                binding.viewModel = viewModel
            }
        }
    }

}