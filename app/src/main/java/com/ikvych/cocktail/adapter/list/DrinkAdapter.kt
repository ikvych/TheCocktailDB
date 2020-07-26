package com.ikvych.cocktail.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.base.BaseRecyclerViewAdapter
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.databinding.ItemDrinkListBinding
import com.ikvych.cocktail.viewmodel.DrinkViewModel


class DrinkAdapter(
    private val viewModel: DrinkViewModel,
    private val context: Context
) : BaseRecyclerViewAdapter<Drink>() {

    private val layoutId = R.layout.item_drink_list

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
    }

    override fun bind(binding: ViewDataBinding, item: Drink) {
        when (binding) {
            is ItemDrinkListBinding -> {
                binding.obj = item
                binding.viewModel = viewModel
                binding.cvItemDrink.setOnLongClickListener(context as? View.OnLongClickListener)
                binding.cvItemDrink.setOnClickListener(context as? View.OnClickListener)
            }
        }
    }
}