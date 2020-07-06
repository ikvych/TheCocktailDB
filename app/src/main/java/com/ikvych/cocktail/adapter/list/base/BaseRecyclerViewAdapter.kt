package com.ikvych.cocktail.adapter.list.base

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<Data>(val layoutResId: Int? = null) : RecyclerView.Adapter<BaseViewHolder>() {

    private val viewHolders: MutableList<BaseViewHolder> = mutableListOf()

    var listData: List<Data> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = createBinding(parent, viewType)
        val viewHolder = BaseViewHolder(binding)
        binding.lifecycleOwner = viewHolder
        viewHolder.markCreated()
        viewHolders.add(viewHolder)
        return viewHolder
    }

    protected abstract fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int
    ) {
        if (position < itemCount) {
            bind(holder.binding, listData[position])
            holder.binding.executePendingBindings()
        }
    }

    protected abstract fun bind(binding: ViewDataBinding, item: Data)

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.markAttach()
    }


    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.markDetach()
    }

    fun setLifecycleDestroyed() {
        viewHolders.forEach {
            it.markDestroyed()
        }
    }
}