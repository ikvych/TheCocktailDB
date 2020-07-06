package com.ikvych.cocktail.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.base.BaseRecyclerViewAdapter
import com.ikvych.cocktail.adapter.list.base.BaseViewHolder
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.databinding.ActivityDrinkDetailsBinding
import com.ikvych.cocktail.databinding.ItemDrinkListBinding
import com.ikvych.cocktail.databinding.ItemSelectedFilterBinding
import com.ikvych.cocktail.ui.activity.MainActivity
import com.ikvych.cocktail.ui.activity.SearchActivity
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
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