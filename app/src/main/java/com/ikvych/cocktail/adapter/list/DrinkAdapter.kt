package com.ikvych.cocktail.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.base.BaseRecyclerViewAdapter
import com.ikvych.cocktail.data.db.model.Drink
import com.ikvych.cocktail.databinding.ItemDrinkListBinding
import com.ikvych.cocktail.ui.activity.MainActivity
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class DrinkAdapter(
    private val viewModel: BaseViewModel,
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
                if (context is MainActivity) {
                    binding.root.setOnLongClickListener(context as View.OnLongClickListener)
                }
            }
        }
    }
}