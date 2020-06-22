package com.ikvych.cocktail.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.DrinkFilter


class FilterAdapter(
    private val context: Context
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    var filterList: List<DrinkFilter> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter_list, parent, false)
        return FilterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val filter: DrinkFilter = filterList[position]
        holder.textView.text = filter.key
    }

    override fun getItemCount(): Int {
        return filterList.size
    }


    inner class FilterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.tv_filter_name)
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                textView.text = filterList.get(position).key
            }
        }
    }
}