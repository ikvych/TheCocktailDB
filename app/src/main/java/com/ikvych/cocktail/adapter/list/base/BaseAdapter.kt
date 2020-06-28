package com.ikvych.cocktail.adapter.list.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.DrinkAdapter

abstract class BaseAdapter<Data, ViewHolder : BaseViewHolder>(protected val layoutId: Int) : RecyclerView.Adapter<ViewHolder>() {

    var newData: List<Data> = arrayListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return newData.size
    }

}