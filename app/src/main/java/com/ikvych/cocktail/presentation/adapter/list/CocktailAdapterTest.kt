package com.ikvych.cocktail.presentation.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ItemDrinkListBinding
import com.ikvych.cocktail.databinding.ItemHeaderDrinkListBinding
import com.ikvych.cocktail.presentation.filter.type.SortDrinkType
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.viewmodel.cocktail.CocktailViewModel


class CocktailAdapterTest(
    private val viewModel: CocktailViewModel,
    private val context: Context,
    private val layoutManager: GridLayoutManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var sortType: SortDrinkType = SortDrinkType.RECENT
    var listData: List<Any> = arrayListOf()
        set(value) {
            field = value
            completedDataList.clear()
            generateData()
            notifyDataSetChanged()
        }


    val completedDataList: ArrayList<Pair<Any, ViewType>> = arrayListOf()
    private val layoutId = R.layout.item_drink_list
    private val headerLayoutId = R.layout.item_header_drink_list

    private val typedDataMap: HashMap<Any, ViewType> = hashMapOf()


    private val lookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return when (completedDataList[position].second) {
                ViewType.HEADER -> 2
                else -> 1
            }
        }
    }

    init {
        layoutManager.spanSizeLookup = lookup
    }

    private fun generateData() {

/*        completedDataList.addAll(listData.map { it to ViewType.ITEM })*/
        addHeaders()
    }

    private var currentHeader: Any? = null

    private fun addHeaders() {
        when (sortType) {
            SortDrinkType.RECENT -> completedDataList.addAll(listData.map { it to ViewType.ITEM })
            SortDrinkType.NAME_ASC -> listData.forEach {
                addElement(
                    it, (it as CocktailModel).names.defaultName?.getOrNull(0) ?: currentHeader
                )
            }
            SortDrinkType.NAME_DESC -> listData.forEach {
                addElement(
                    it, (it as CocktailModel).names.defaultName?.getOrNull(0) ?: currentHeader
                )
            }
            SortDrinkType.ALCOHOL_ASC -> listData.forEach {
                addElement(it, (it as CocktailModel).alcoholType.key)
            }
            SortDrinkType.ALCOHOL_DESC -> listData.forEach {
                addElement(it, (it as CocktailModel).alcoholType.key)
            }
            SortDrinkType.INGREDIENT_COUNT_ASC -> listData.forEach {
                addElement(it, (it as CocktailModel).ingredients.size)
            }
            SortDrinkType.INGREDIENT_COUNT_DESC -> listData.forEach {
                addElement(it, (it as CocktailModel).ingredients.size)
            }
        }
        currentHeader = null
    }

    private fun addElement(element: Any, newHeader: Any?) {
        if (newHeader != currentHeader) {
            currentHeader = newHeader
            completedDataList.add(newHeader!! to ViewType.HEADER)
            completedDataList.add(element to ViewType.ITEM)
        } else {
            completedDataList.add(element to ViewType.ITEM)
        }
    }

    override fun getItemCount(): Int {
        return completedDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.HEADER.ordinal -> {
                val holder = HeaderViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        headerLayoutId,
                        parent,
                        false
                    )
                )
                holder
            }
            else -> {
                CocktailViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        layoutId,
                        parent,
                        false
                    )
                )
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (completedDataList[position].second) {
            ViewType.HEADER -> {
                ViewType.HEADER.ordinal
            }
            else -> {
                ViewType.ITEM.ordinal
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CocktailViewHolder -> {
                holder.binding.obj = completedDataList[position].first as CocktailModel
                holder.binding.viewModel = viewModel
                holder.binding.cvItemDrink.setOnLongClickListener(context as? View.OnLongClickListener)
                holder.binding.cvItemDrink.setOnClickListener(context as? View.OnClickListener)
                holder.binding.executePendingBindings()
            }
            is HeaderViewHolder -> {
                holder.binding.obj = completedDataList[position].first.toString()
                holder.binding.executePendingBindings()
            }
        }
    }

    class CocktailViewHolder(val binding: ItemDrinkListBinding) :
        RecyclerView.ViewHolder(binding.root)

    class HeaderViewHolder(val binding: ItemHeaderDrinkListBinding) :
        RecyclerView.ViewHolder(binding.root)

}