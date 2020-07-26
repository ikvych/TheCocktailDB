package com.ikvych.cocktail.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.base.BaseRecyclerViewAdapter
import com.ikvych.cocktail.constant.DRINK_FILTER_ABSENT
import com.ikvych.cocktail.databinding.ItemSelectedFilterListBinding
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel


class FilterAdapter(
    private val viewModel: MainFragmentViewModel
) : BaseRecyclerViewAdapter<DrinkFilter>() {

    private val layoutId = R.layout.item_selected_filter_list

    fun setData(list: List<DrinkFilter>) {
        //видаляю усі фільтри типу - відсутній
        listData = list.filter { it.key != DRINK_FILTER_ABSENT }
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