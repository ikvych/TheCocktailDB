package com.ikvych.cocktail.adapter.list

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.drawable.toAdaptiveIcon
import androidx.core.graphics.scale
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.*
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.databinding.ItemDrinkListBinding
import com.ikvych.cocktail.ui.activity.DrinkDetailActivity
import com.ikvych.cocktail.ui.activity.MainActivity
import com.ikvych.cocktail.ui.activity.SearchActivity
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.util.*


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
            val favorite = itemDrinkListBinding.root.findViewById<CheckBox>(R.id.cb_favorite)
            if (context is SearchActivity) {
                favorite.visibility = View.GONE
            }
            favorite.setOnClickListener(context as View.OnClickListener)
        }
    }
}