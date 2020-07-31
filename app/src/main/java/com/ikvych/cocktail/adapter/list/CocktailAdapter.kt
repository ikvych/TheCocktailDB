package com.ikvych.cocktail.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.base.BaseRecyclerViewAdapter
import com.ikvych.cocktail.data.network.model.Drink
import com.ikvych.cocktail.databinding.ItemDrinkListBinding
import com.ikvych.cocktail.ui.activity.MainActivity
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel
import com.ikvych.cocktail.viewmodel.DrinkViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class CocktailAdapter(
    private val viewModel: DrinkViewModel,
    private val context: Context
) : BaseRecyclerViewAdapter<CocktailModel>() {

    private val layoutId = R.layout.item_drink_list

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
    }

    override fun bind(binding: ViewDataBinding, item: CocktailModel) {
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