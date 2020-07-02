package com.ikvych.cocktail.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType


class FilterAdapter(
    private val context: Context,
    private val listener: OnClickItemFilterCloseListener
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    interface OnClickItemFilterCloseListener {
        fun onClick(drinkFilter: DrinkFilter)
    }

    var filterList: List<DrinkFilter> = arrayListOf()
        set(value) {
            field = value.filter { it.key != "None" }
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selected_filter, parent, false)
        return FilterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val filter: DrinkFilter = filterList[position]
        when (filter.type) {
            DrinkFilterType.CATEGORY -> {
                holder.filterIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_item_drink_filter_favorite
                    )
                )
                holder.linearLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.item_drink_filter_ingredient_bg
                    )
                )
                holder.closeButton.setOnClickListener { listener.onClick(filter) }
            }
            DrinkFilterType.ALCOHOL -> {
                holder.filterIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_item_drink_filter_alcohol
                    )
                )
                holder.linearLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.item_drink_filter_ingredient_bg
                    )
                )
                holder.closeButton.setOnClickListener { listener.onClick(filter) }
            }
            DrinkFilterType.INGREDIENT -> {
                holder.filterIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_item_drink_filter_ingredient
                    )
                )
                holder.linearLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.item_drink_filter_ingredient_bg
                    )
                )
                holder.closeButton.setOnClickListener { listener.onClick(filter) }
            }
            DrinkFilterType.GLASS -> {
            }
        }
        holder.textView.text = filter.key
    }

    override fun getItemCount(): Int {
        return filterList.size
    }


    inner class FilterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val linearLayout: LinearLayout
        val filterIcon: ImageButton
        val closeButton: ImageButton

        init {
            textView = view.findViewById(R.id.tv_filter_name)
            linearLayout = view.findViewById(R.id.ll_selected_filter_container)
            filterIcon = view.findViewById(R.id.ib_filter_icon)
            closeButton = view.findViewById(R.id.ib_remove_filter)
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                textView.text = filterList.get(position).key
            }
        }
    }
}