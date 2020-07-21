package com.ikvych.cocktail.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.databinding.ItemDrinkListBinding
import com.ikvych.cocktail.ui.activity.MainActivity
import com.ikvych.cocktail.ui.activity.SearchActivity


class DrinkAdapter(
    private val context: Context
) : RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder>() {

    var drinkList: List<Drink> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val itemDrinkListBinding: ItemDrinkListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_drink_list,
            parent,
            false
        )
        return DrinkViewHolder(itemDrinkListBinding)
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        val drink: Drink = drinkList[position]
        holder.itemDrinkListBinding.drink = drink
    }

    override fun getItemCount(): Int {
        return drinkList.size
    }

    inner class DrinkViewHolder(val itemDrinkListBinding: ItemDrinkListBinding) :
        RecyclerView.ViewHolder(itemDrinkListBinding.root) {

        init {
            itemDrinkListBinding.root.setOnClickListener(context as View.OnClickListener)
            itemDrinkListBinding.root.setOnLongClickListener(context as View.OnLongClickListener)
            val favoriteCheckBox = itemDrinkListBinding.root.findViewById<CheckBox>(R.id.cb_is_favorite)
            if (context is SearchActivity) {
                favoriteCheckBox.visibility = View.GONE
            }
            if (context is MainActivity) {
                favoriteCheckBox.setOnClickListener(context as View.OnClickListener)
            }
        }
    }
}