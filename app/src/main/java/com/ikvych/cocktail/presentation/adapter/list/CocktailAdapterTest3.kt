package com.ikvych.cocktail.presentation.adapter.list

import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ItemDrinkListBinding
import com.ikvych.cocktail.databinding.ItemFavoriteDrinkListBinding
import com.ikvych.cocktail.databinding.ItemHeaderDrinkListBinding
import com.ikvych.cocktail.presentation.filter.type.SortDrinkType
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.viewmodel.cocktail.CocktailViewModel
import java.util.*


class CocktailAdapterTest3(
    private val viewModel: CocktailViewModel,
    private val context: Context,
    private val layoutManager: GridLayoutManager,
    private val isFavorite: Boolean,
    private val orientation: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val layoutId = R.layout.item_drink_list
    private val favoriteLayoutId = R.layout.item_favorite_drink_list
    private val headerLayoutId = R.layout.item_header_drink_list

    var spanSizeLookup: GridLayoutManager.SpanSizeLookup = setSpanSizeLookup()

    init {
        layoutManager.spanSizeLookup = spanSizeLookup
    }

    private val num = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2

    var sortType: SortDrinkType = SortDrinkType.RECENT
    var listData: List<Any> = arrayListOf()
        set(value) {
            field = value
            completedDataList.clear()
            typedDataMap.clear()
            addHeaders()
            notifyDataSetChanged()
        }

    val completedDataList: ArrayList<Pair<Any, String>> = arrayListOf()
    private val typedDataMap: SortedMap<String, ArrayList<Any>> = sortedMapOf()

    private fun getSpanForPortraitOrientation(element: Any, header: String): Int {
        val subList = typedDataMap[header]!!
        return when (subList.size % 2) {
            0 -> 2
            1 -> if (subList.indexOf(element) == 0) 4 else 2
            else -> 0
        }
    }

    private fun getSpanForLandscapeOrientation(element: Any, header: String): Int {
        val subList = typedDataMap[header]!!
        return when (subList.size % 4) {
            0 -> 1
            3 -> {
                val sublistElementIndex = subList.indexOf(element)
                if (sublistElementIndex == 0) 4
                else if (sublistElementIndex == 1 || sublistElementIndex == 2) 2
                else 1
            }
            2 -> {
                val sublistElementIndex = subList.indexOf(element)
                if (sublistElementIndex == 0 || sublistElementIndex == 1) 2 else 1
            }
            1 -> {
                val sublistElementIndex = subList.indexOf(element)
                if (sublistElementIndex == 0) 4 else 1
            }
            else -> 0
        }
    }

    private var currentHeader: Any? = null
    private fun addHeaders() {
        when (sortType) {
            SortDrinkType.RECENT -> {
                completedDataList.addAll(listData.map { it to "" })
                typedDataMap[""] = arrayListOf()
                typedDataMap[""]!!.addAll(listData)
            }
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
            completedDataList.add(newHeader.toString() to "")
            completedDataList.add(element to newHeader.toString())
        } else {
            typedDataMap[currentHeader.toString()]?.add(element)
            completedDataList.add(element to currentHeader.toString())
        }
    }

    override fun getItemCount(): Int {
        return completedDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutWidth = layoutManager.width / num
        return when (viewType) {
            ViewType.HEADER.ordinal -> {
                HeaderViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        headerLayoutId,
                        parent,
                        false
                    )
                )
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
                    binding.ivDrinkImage.minimumHeight = layoutWidth
                    CocktailViewHolder(
                        binding
                    )
                }
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (completedDataList[position].first) {
            is String -> {
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
        RecyclerView.ViewHolder(binding.root)

    class FavoriteCocktailViewHolder(val binding: ItemFavoriteDrinkListBinding) :
        RecyclerView.ViewHolder(binding.root)

    class HeaderViewHolder(val binding: ItemHeaderDrinkListBinding) :
        RecyclerView.ViewHolder(binding.root)


    inner class MyItemDecorator(
        private val context: Context
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            if (view is CardView) {
                val position = parent.getChildAdapterPosition(view)
                val spanCount = spanSizeLookup.getSpanSize(position)
                val currentHeader = completedDataList[position].second
                val currentElement = completedDataList[position].first
                val subList = typedDataMap[currentHeader]!!
                val sublistElementIndex = subList.indexOf(currentElement)

                if (orientation == Configuration.ORIENTATION_LANDSCAPE ||
                    sortType == SortDrinkType.INGREDIENT_COUNT_ASC || sortType == SortDrinkType.INGREDIENT_COUNT_DESC
                ) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

                    } else {
                        when (subList.size % 4) {
                            0 -> {
                                when (sublistElementIndex % 2) {
                                    0 -> {
                                        when (sublistElementIndex % 4) {
                                            2 -> setBounds(4, 4, 4, 4, outRect)
                                            0 -> setBounds(4, 4, 4, 8, outRect)
                                        }
                                    }
                                    1 -> {
                                        when ((sublistElementIndex - 1) % 4) {
                                            2 -> setBounds(4, 8, 4, 4, outRect)
                                            0 -> setBounds(4, 4, 4, 4, outRect)
                                        }
                                    }
                                }
                            }
                            3 -> {
                                when (sublistElementIndex) {
                                    0 -> setBounds(4, 8, 4, 8, outRect)
                                    1 -> setBounds(4, 4, 4, 8, outRect)
                                    2 -> setBounds(4, 8, 4, 4, outRect)
                                    else -> {
                                        when (sublistElementIndex % 2) {
                                            0 -> {
                                                when (sublistElementIndex % 4) {
                                                    2 -> setBounds(4, 8, 4, 4, outRect)
                                                    0 -> setBounds(4, 4, 4, 4, outRect)
                                                }
                                            }
                                            1 -> {
                                                when ((sublistElementIndex - 1) % 4) {
                                                    2 -> setBounds(4, 4, 4, 8, outRect)
                                                    0 -> setBounds(4, 4, 4, 4, outRect)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            2 -> {
                                when (sublistElementIndex) {
                                    0 -> setBounds(4, 4, 4, 8, outRect)
                                    1 -> setBounds(4, 8, 4, 4, outRect)
                                    else -> {
                                        when (sublistElementIndex % 2) {
                                            0 -> {
                                                when (sublistElementIndex % 4) {
                                                    2 -> setBounds(4, 4, 4, 8, outRect)
                                                    0 -> setBounds(4, 4, 4, 4, outRect)
                                                }
                                            }
                                            1 -> {
                                                when ((sublistElementIndex - 1) % 4) {
                                                    2 -> setBounds(4, 4, 4, 4, outRect)
                                                    0 -> setBounds(4, 8, 4, 4, outRect)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            1 -> {
                                when (sublistElementIndex) {
                                    0 -> setBounds(4, 8, 4, 8, outRect)
                                    else -> {
                                        when (sublistElementIndex % 2) {
                                            0 -> {
                                                when (sublistElementIndex % 4) {
                                                    2 -> setBounds(4, 4, 4, 4, outRect)
                                                    0 -> setBounds(4, 8, 4, 4, outRect)
                                                }
                                            }
                                            1 -> {
                                                when ((sublistElementIndex - 1) % 4) {
                                                    2 -> setBounds(4, 4, 4, 4, outRect)
                                                    0 -> setBounds(4, 4, 4, 8, outRect)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else -> {}
                        }
                    }
                } else {
                    when (subList.size % 2) {
                        0 -> {
                            when (sublistElementIndex % 2) {
                                0 -> setBounds(8, 8, 8, 16, outRect)
                                1 -> setBounds(8, 16, 8, 8, outRect)
                            }
                        }
                        1 -> {
                            when (sublistElementIndex % 2) {
                                0 ->
                                    if (sublistElementIndex == 0) setBounds(8, 16, 8, 16, outRect)
                                    else setBounds(8, 16, 8, 8, outRect)
                                1 -> setBounds(8, 8, 8, 16, outRect)
                            }
                        }
                        else -> {}
                    }
                }
            } else {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE ||
                    sortType == SortDrinkType.INGREDIENT_COUNT_ASC || sortType == SortDrinkType.INGREDIENT_COUNT_DESC
                ) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

                    } else {
                        setBounds(4, 0, 4, 0, outRect)
                    }
                } else setBounds(8, 0, 8, 0, outRect)
            }
        }
    }

    private fun setBounds(top: Int, right: Int, bottom: Int, left: Int, outRect: Rect) {
        outRect.top = convertDpToPx(top).toInt()
        outRect.bottom = convertDpToPx(bottom).toInt()
        outRect.right = convertDpToPx(right).toInt()
        outRect.left = convertDpToPx(left).toInt()
    }

    private fun convertDpToPx(dp: Int): Float {
        return dp * context.resources.displayMetrics.density
    }

    private fun setSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
        return if (isFavorite) {
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return 4
                }
            }
        } else {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val currentElementPair = completedDataList[position]
                        return when (currentElementPair.first) {
                            is CocktailModel -> getSpanForLandscapeOrientation(
                                currentElementPair.first,
                                currentElementPair.second
                            )
                            else -> 4
                        }
                    }
                }
            } else {
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val currentElementPair = completedDataList[position]
                        return when (currentElementPair.first) {
                            is CocktailModel -> {
                                if (sortType == SortDrinkType.INGREDIENT_COUNT_ASC || sortType == SortDrinkType.INGREDIENT_COUNT_DESC) {
                                    getSpanForLandscapeOrientation(
                                        currentElementPair.first,
                                        currentElementPair.second
                                    )
                                } else {
                                    getSpanForPortraitOrientation(
                                        currentElementPair.first,
                                        currentElementPair.second
                                    )
                                }
                            }
                            else -> 4
                        }
                    }
                }
            }
        }
    }

}