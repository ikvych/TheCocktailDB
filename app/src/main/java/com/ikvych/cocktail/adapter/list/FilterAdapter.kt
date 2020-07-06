package com.ikvych.cocktail.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.base.BaseRecyclerViewAdapter
import com.ikvych.cocktail.adapter.list.base.BaseViewHolder
import com.ikvych.cocktail.databinding.ItemDrinkListBinding
import com.ikvych.cocktail.databinding.ItemSelectedFilterBinding
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import java.util.HashMap


class FilterAdapter(
    private val viewModel: MainFragmentViewModel
) : BaseRecyclerViewAdapter<DrinkFilter>() {

    private val layoutId = R.layout.item_selected_filter

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
            is ItemSelectedFilterBinding -> {
                binding.obj = item
                binding.viewModel = viewModel
            }
        }
    }

}