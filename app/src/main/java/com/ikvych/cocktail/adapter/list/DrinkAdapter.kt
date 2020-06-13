package com.ikvych.cocktail.adapter.list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.DRINK
import com.ikvych.cocktail.constant.MAIN_MODEL_TYPE
import com.ikvych.cocktail.constant.SEARCH_MODEL_TYPE
import com.ikvych.cocktail.constant.VIEW_MODEL_TYPE
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.databinding.ItemDrinkListBinding
import com.ikvych.cocktail.ui.activity.DrinkDetailActivity


class DrinkAdapter(
    private val context: Context,
    private val activityName: String
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


    inner class DrinkViewHolder(val itemDrinkListBinding: ItemDrinkListBinding) : RecyclerView.ViewHolder(itemDrinkListBinding.root) {
        init {
            itemDrinkListBinding.root.setOnClickListener {
                val position = adapterPosition

                if (position != RecyclerView.NO_POSITION) {
                    val drink: Drink = drinkList[position]
                    val intent = Intent(context, DrinkDetailActivity::class.java)
                    when (activityName) {
                        MAIN_MODEL_TYPE -> intent.putExtra(
                            VIEW_MODEL_TYPE,
                            MAIN_MODEL_TYPE
                        )
                        SEARCH_MODEL_TYPE -> intent.putExtra(
                            VIEW_MODEL_TYPE,
                            SEARCH_MODEL_TYPE
                        )
                    }
                    intent.putExtra(DRINK, drink)
                    context.startActivity(intent)
                }
            }
        }
    }
}