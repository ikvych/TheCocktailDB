package com.ikvych.cocktail.presentation.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ItemDrinkListBinding
import com.ikvych.cocktail.databinding.ItemFavoriteDrinkListBinding
import com.ikvych.cocktail.databinding.ItemHeaderDrinkListBinding
import com.ikvych.cocktail.presentation.filter.type.SortDrinkType
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.viewmodel.cocktail.CocktailViewModel
import kotlinx.android.synthetic.*
import java.util.*
import kotlin.collections.ArrayList


class CocktailAdapterTest(
    private val viewModel: CocktailViewModel,
    private val context: Context,
    private val layoutManager: GridLayoutManager,
    private val isFavorite: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var sortType: SortDrinkType = SortDrinkType.RECENT
    var listData: List<Any> = arrayListOf()
        set(value) {
            field = value
            completedDataList.clear()
            typedDataMap.clear()
            generateData()
            notifyDataSetChanged()
        }
    var itemMaxHeight: Int? = null

    init {

    }


    val completedDataList: ArrayList<Pair<Any, ViewType>> = arrayListOf()
    private val layoutId = R.layout.item_drink_list
    private val favoriteLayoutId = R.layout.item_favorite_drink_list
    private val headerLayoutId = R.layout.item_header_drink_list

    private val typedDataMap: SortedMap<String, ArrayList<Any>> = sortedMapOf()


    private val lookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return when (completedDataList[position].second) {
                ViewType.HEADER -> 2
                else -> {
                    var currentHeader: String? = null
                    typedDataMap.forEach { entry ->
                        entry.value.forEach { element ->
                            if (completedDataList[position].first == element) {
                                currentHeader = entry.key
                                return@forEach
                            }
                        }
                        if (currentHeader != null) {
                            return@forEach
                        }
                    }
                    if (currentHeader != null) {
                        val rest = typedDataMap[currentHeader]!!.size % 2
                        if (rest == 0) {
                            1
                        } else {
                            val index =
                                typedDataMap[currentHeader]!!.indexOf(completedDataList[position].first)
                            if (index == 0) {
                                2
                            } else {
                                1
                            }
                        }
                    } else {
                        1
                    }
                }
            }
        }
    }

    init {
        if (!isFavorite) {
            layoutManager?.spanSizeLookup = lookup
        }
    }

    private fun generateData() {
        addHeaders()
        println()
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
            typedDataMap[newHeader.toString()] = arrayListOf(element)
            completedDataList.add(newHeader.toString() to ViewType.HEADER)
            completedDataList.add(element to ViewType.ITEM)
        } else {
            typedDataMap[currentHeader.toString()]?.add(element)
            completedDataList.add(element to ViewType.ITEM)
        }
    }

    override fun getItemCount(): Int {
        return completedDataList.size
    }

    var rcWidth: Int? = null
    var maxHeight: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (itemMaxHeight == null) {
            val spans = layoutManager.spanCount
            val layoutWidth = layoutManager.width / spans
            val bottomTitleHeight = (context.resources.getDimension(R.dimen.offset_32)).toInt()
            itemMaxHeight = layoutWidth + bottomTitleHeight
        }
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
                if (isFavorite) {
                    FavoriteCocktailViewHolder(
                        DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            favoriteLayoutId,
                            parent,
                            false
                        )
                    )
                } else {

                    val binding: ItemDrinkListBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        layoutId,
                        parent,
                        false
                    )
                    CocktailViewHolder(
                        binding
                    )
                }
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

/*    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        when (holder) {
            is CocktailViewHolder -> {
                if (itemMaxHeight == null) {

                    val itemView = holder.binding.root
                    itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    maxHeight = itemView.measuredHeight
                }
            }
        }
    }*/

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val spans = layoutManager.spanCount
        val layoutWidth = layoutManager.width / spans
        when (holder) {
            is CocktailViewHolder -> {
                holder.binding.obj = completedDataList[position].first as CocktailModel
                holder.binding.viewModel = viewModel
                holder.binding.cvItemDrink.setOnLongClickListener(context as? View.OnLongClickListener)
                holder.binding.cvItemDrink.setOnClickListener(context as? View.OnClickListener)
                holder.binding.ivDrinkImage.maxHeight = layoutWidth
                holder.binding.ivDrinkImage.adjustViewBounds = true

/*                if (lookup.getSpanSize(position) == 2) {
                    holder.binding.ivDrinkImage.visibility = View.GONE
                    holder.binding.ivDrinkImage2.visibility = View.VISIBLE
                    holder.binding.ivDrinkImage2.maxHeight = layoutWidth
                    holder.binding.ivDrinkImage2.adjustViewBounds = true
                    holder.binding.ivDrinkImage2.scaleType = ImageView.ScaleType.CENTER_CROP
                } else {
                    holder.binding.ivDrinkImage.visibility = View.VISIBLE
                    holder.binding.ivDrinkImage2.visibility = View.GONE
                    holder.binding.ivDrinkImage.maxHeight = layoutWidth
                    holder.binding.ivDrinkImage.adjustViewBounds = true
                    holder.binding.ivDrinkImage.scaleType = ImageView.ScaleType.CENTER_CROP
                }*/


                if (!isFavorite) {
                        val itemView = holder.binding.root
                        itemView.measure(
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                        )
                        val itemLayoutParam = itemView.layoutParams
                        itemLayoutParam.height = itemMaxHeight!!
                        itemView.layoutParams = itemLayoutParam
                }
                holder.binding.executePendingBindings()
            }
            is FavoriteCocktailViewHolder -> {
                holder.binding.obj = completedDataList[position].first as CocktailModel
                holder.binding.viewModel = viewModel
                holder.binding.ibPopupMenu.setOnLongClickListener(context as? View.OnLongClickListener)
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
        RecyclerView.ViewHolder(binding.root) {
    }


    class CocktailViewHolder1(val binding: ItemDrinkListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    class FavoriteCocktailViewHolder(val binding: ItemFavoriteDrinkListBinding) :
        RecyclerView.ViewHolder(binding.root)

    class HeaderViewHolder(val binding: ItemHeaderDrinkListBinding) :
        RecyclerView.ViewHolder(binding.root)

}