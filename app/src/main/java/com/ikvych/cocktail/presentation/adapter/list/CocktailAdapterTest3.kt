package com.ikvych.cocktail.presentation.adapter.list

import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class CocktailAdapterTest3(
    private val viewModel: CocktailViewModel,
    private val context: Context,
    private val layoutManager: GridLayoutManager,
    private val isFavorite: Boolean,
    private val orientation: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    private val layoutId = R.layout.item_drink_list
    private val favoriteLayoutId = R.layout.item_favorite_drink_list
    private val headerLayoutId = R.layout.item_header_drink_list
    private val diffUtilCallback = MyDiffUtilCallback()

    var spanSizeLookup: GridLayoutManager.SpanSizeLookup = setSpanSizeLookup()

    init {
        layoutManager.spanSizeLookup = spanSizeLookup
    }

    private val num = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2

    var oldType: SortDrinkType = SortDrinkType.RECENT
    var sortType: SortDrinkType = SortDrinkType.RECENT
    set(value) {
        oldType = SortDrinkType.values().first { it.key == sortType.key}
        field = value
    }
    var listData: List<Any> = arrayListOf()
        set(value) {
            field = value
            oldDataList = completedDataList.clone() as ArrayList<Pair<Any, String>>
            completedDataList.clear()
            typedDataMap.clear()
            headersStateList.clear()
            addHeaders()
            if (oldType == sortType) {
                val headersValues = collapsedElements.keys
                headersValues.forEach {
                    collapsedElements[it]!!.clear()
                    headersStateList[it] = HeaderState.COLLAPSED
                    completedDataList.removeAll {pair ->
                        if (pair.second == it) {
                            collapsedElements[it]!!.add(pair.first)
                            true
                        } else false
                    }
                }
            } else {
                collapsedElements.clear()
            }
            DiffUtil.calculateDiff(diffUtilCallback).dispatchUpdatesTo(this)
            /*notifyDataSetChanged()*/
        }

    var oldDataList: ArrayList<Pair<Any, String>> = arrayListOf()
    val completedDataList: ArrayList<Pair<Any, String>> = arrayListOf()
    private val collapsedElements: SortedMap<String, ArrayList<Any>> = sortedMapOf()
    private val typedDataMap: SortedMap<String, ArrayList<Any>> = sortedMapOf()
    private val headersStateList: SortedMap<String, HeaderState> = sortedMapOf()

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
                completedDataList.addAll(listData.map { it to "NOTHEADER" })
                typedDataMap["NOTHEADER"] = arrayListOf()
                typedDataMap["NOTHEADER"]!!.addAll(listData)
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
            headersStateList[newHeader.toString()] = HeaderState.EXPANDED
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
                val header = completedDataList[position].second
                if (headersStateList[header] == HeaderState.COLLAPSED) {

                } else {
                    holder.binding.obj = completedDataList[position].first as CocktailModel
                    holder.binding.viewModel = viewModel
                    holder.binding.cvItemDrink.setOnLongClickListener(context as? View.OnLongClickListener)
                    holder.binding.cvItemDrink.setOnClickListener(context as? View.OnClickListener)
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
        RecyclerView.ViewHolder(binding.root)

    class FavoriteCocktailViewHolder(val binding: ItemFavoriteDrinkListBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class HeaderViewHolder(val binding: ItemHeaderDrinkListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener(this@CocktailAdapterTest3)
        }
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v) {
            is TextView -> {
                val header = v.text.toString()
                when (headersStateList[header]) {
                    HeaderState.EXPANDED -> {
                        oldDataList = completedDataList.clone() as ArrayList<Pair<Any, String>>
                        headersStateList[header] = HeaderState.COLLAPSED
                        collapsedElements[header] = arrayListOf()
                        completedDataList.removeAll {
                            if (it.second == header) {
                                collapsedElements[header]!!.add(it.first)
                                true
                            } else false
                        }
                    }
                    HeaderState.COLLAPSED -> {
                        oldDataList = completedDataList.clone() as ArrayList<Pair<Any, String>>
                        headersStateList[header] = HeaderState.EXPANDED
                        val hidedList: ArrayList<Any> = collapsedElements.remove(header)!!
                        val existenHeader = completedDataList.first { it.first.toString() == header }
                        val headerIndex = completedDataList.indexOf(existenHeader)
                        val collection = hidedList.map { it to header }
                        completedDataList.addAll(headerIndex+1, collection)
                    }
                }
                DiffUtil.calculateDiff(diffUtilCallback).dispatchUpdatesTo(this)
            }
        }
    }

    inner class MyDiffUtilCallback :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldDataList.size
        }

        override fun getNewListSize(): Int {
            return completedDataList.size
        }

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            val oldElement = oldDataList[oldItemPosition]
            val newElement = completedDataList[newItemPosition]
            var result = false
            if (oldElement.second.isBlank() && newElement.second.isBlank()) {
                result = (oldElement.first as String) == (newElement.first as String)
            }
            if (oldElement.second.isNotBlank() && newElement.second.isNotBlank()) {
                result =
                    (oldElement.first as CocktailModel).id == (newElement.first as CocktailModel).id
            }
            return result
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            val oldElement = oldDataList[oldItemPosition]
            val newElement = completedDataList[newItemPosition]
            var result = false
            if (oldElement.second.isBlank() && newElement.second.isBlank()) {
                result = (oldElement.first as String) == (newElement.first as String)
            }
            if (oldElement.second.isNotBlank() && newElement.second.isNotBlank()) {
                result =
                    (oldElement.first as CocktailModel).isFavorite == (newElement.first as CocktailModel).isFavorite
            }
            return result
        }
    }

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
                if (position == -1) return
                val currentHeader = completedDataList[position].second
                val currentElement = completedDataList[position].first
                val subList = typedDataMap[currentHeader]!!
                val sublistElementIndex = subList.indexOf(currentElement)

                if (orientation == Configuration.ORIENTATION_LANDSCAPE ||
                    sortType == SortDrinkType.INGREDIENT_COUNT_ASC || sortType == SortDrinkType.INGREDIENT_COUNT_DESC
                ) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        when (subList.size % 4) {
                            0 -> {
                                when (sublistElementIndex % 2) {
                                    0 -> {
                                        when (sublistElementIndex % 4) {
                                            2 -> setBounds(lb_8, lb_8, lb_8, lb_8, outRect)
                                            0 -> setBounds(lb_8, lb_8, lb_8, lb_16, outRect)
                                        }
                                    }
                                    1 -> {
                                        when ((sublistElementIndex - 1) % 4) {
                                            2 -> setBounds(lb_8, lb_16, lb_8, lb_8, outRect)
                                            0 -> setBounds(lb_8, lb_8, lb_8, lb_8, outRect)
                                        }
                                    }
                                }
                            }
                            3 -> {
                                when (sublistElementIndex) {
                                    0 -> setBounds(lb_8, lb_16, lb_8, lb_16, outRect)
                                    1 -> setBounds(lb_8, lb_8, lb_8, lb_16, outRect)
                                    2 -> setBounds(lb_8, lb_16, lb_8, lb_8, outRect)
                                    else -> {
                                        when (sublistElementIndex % 2) {
                                            0 -> {
                                                when (sublistElementIndex % 4) {
                                                    2 -> setBounds(lb_8, lb_16, lb_8, lb_8, outRect)
                                                    0 -> setBounds(lb_8, lb_8, lb_8, lb_8, outRect)
                                                }
                                            }
                                            1 -> {
                                                when ((sublistElementIndex - 1) % 4) {
                                                    2 -> setBounds(lb_8, lb_8, lb_8, lb_16, outRect)
                                                    0 -> setBounds(lb_8, lb_8, lb_8, lb_8, outRect)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            2 -> {
                                when (sublistElementIndex) {
                                    0 -> setBounds(lb_8, lb_8, lb_8, lb_16, outRect)
                                    1 -> setBounds(lb_8, lb_16, lb_8, lb_8, outRect)
                                    else -> {
                                        when (sublistElementIndex % 2) {
                                            0 -> {
                                                when (sublistElementIndex % 4) {
                                                    2 -> setBounds(lb_8, lb_8, lb_8, lb_16, outRect)
                                                    0 -> setBounds(lb_8, lb_8, lb_8, lb_8, outRect)
                                                }
                                            }
                                            1 -> {
                                                when ((sublistElementIndex - 1) % 4) {
                                                    2 -> setBounds(lb_8, lb_8, lb_8, lb_8, outRect)
                                                    0 -> setBounds(lb_8, lb_16, lb_8, lb_8, outRect)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            1 -> {
                                when (sublistElementIndex) {
                                    0 -> setBounds(lb_8, lb_16, lb_8, lb_16, outRect)
                                    else -> {
                                        when (sublistElementIndex % 2) {
                                            0 -> {
                                                when (sublistElementIndex % 4) {
                                                    2 -> setBounds(lb_8, lb_8, lb_8, lb_8, outRect)
                                                    0 -> setBounds(lb_8, lb_16, lb_8, lb_8, outRect)
                                                }
                                            }
                                            1 -> {
                                                when ((sublistElementIndex - 1) % 4) {
                                                    2 -> setBounds(lb_8, lb_8, lb_8, lb_8, outRect)
                                                    0 -> setBounds(lb_8, lb_8, lb_8, lb_16, outRect)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else -> {
                            }
                        }
                    } else {
                        when (subList.size % 4) {
                            0 -> {
                                when (sublistElementIndex % 2) {
                                    0 -> {
                                        when (sublistElementIndex % 4) {
                                            2 -> setBounds(ingb_4, ingb_4, ingb_4, ingb_4, outRect)
                                            0 -> setBounds(ingb_4, ingb_4, ingb_4, ingb_8, outRect)
                                        }
                                    }
                                    1 -> {
                                        when ((sublistElementIndex - 1) % 4) {
                                            2 -> setBounds(ingb_4, ingb_8, ingb_4, ingb_4, outRect)
                                            0 -> setBounds(ingb_4, ingb_4, ingb_4, ingb_4, outRect)
                                        }
                                    }
                                }
                            }
                            3 -> {
                                when (sublistElementIndex) {
                                    0 -> setBounds(ingb_4, ingb_8, ingb_4, ingb_8, outRect)
                                    1 -> setBounds(ingb_4, ingb_4, ingb_4, ingb_8, outRect)
                                    2 -> setBounds(ingb_4, ingb_8, ingb_4, ingb_4, outRect)
                                    else -> {
                                        when (sublistElementIndex % 2) {
                                            0 -> {
                                                when (sublistElementIndex % 4) {
                                                    2 -> setBounds(
                                                        ingb_4,
                                                        ingb_8,
                                                        ingb_4,
                                                        ingb_4,
                                                        outRect
                                                    )
                                                    0 -> setBounds(
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        outRect
                                                    )
                                                }
                                            }
                                            1 -> {
                                                when ((sublistElementIndex - 1) % 4) {
                                                    2 -> setBounds(
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_8,
                                                        outRect
                                                    )
                                                    0 -> setBounds(
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        outRect
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            2 -> {
                                when (sublistElementIndex) {
                                    0 -> setBounds(ingb_4, ingb_4, ingb_4, ingb_8, outRect)
                                    1 -> setBounds(ingb_4, ingb_8, ingb_4, ingb_4, outRect)
                                    else -> {
                                        when (sublistElementIndex % 2) {
                                            0 -> {
                                                when (sublistElementIndex % 4) {
                                                    2 -> setBounds(
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_8,
                                                        outRect
                                                    )
                                                    0 -> setBounds(
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        outRect
                                                    )
                                                }
                                            }
                                            1 -> {
                                                when ((sublistElementIndex - 1) % 4) {
                                                    2 -> setBounds(
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        outRect
                                                    )
                                                    0 -> setBounds(
                                                        ingb_4,
                                                        ingb_8,
                                                        ingb_4,
                                                        ingb_4,
                                                        outRect
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            1 -> {
                                when (sublistElementIndex) {
                                    0 -> setBounds(ingb_4, ingb_8, ingb_4, ingb_8, outRect)
                                    else -> {
                                        when (sublistElementIndex % 2) {
                                            0 -> {
                                                when (sublistElementIndex % 4) {
                                                    2 -> setBounds(
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        outRect
                                                    )
                                                    0 -> setBounds(
                                                        ingb_4,
                                                        ingb_8,
                                                        ingb_4,
                                                        ingb_4,
                                                        outRect
                                                    )
                                                }
                                            }
                                            1 -> {
                                                when ((sublistElementIndex - 1) % 4) {
                                                    2 -> setBounds(
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        outRect
                                                    )
                                                    0 -> setBounds(
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_4,
                                                        ingb_8,
                                                        outRect
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else -> {
                            }
                        }
                    }
                } else {
                    when (subList.size % 2) {
                        0 -> {
                            when (sublistElementIndex % 2) {
                                0 -> setBounds(pb_8, pb_8, pb_8, pb_16, outRect)
                                1 -> setBounds(pb_8, pb_16, pb_8, pb_8, outRect)
                            }
                        }
                        1 -> {
                            when (sublistElementIndex % 2) {
                                0 ->
                                    if (sublistElementIndex == 0) setBounds(
                                        pb_8,
                                        pb_16,
                                        pb_8,
                                        pb_16,
                                        outRect
                                    )
                                    else setBounds(pb_8, pb_16, pb_8, pb_8, outRect)
                                1 -> setBounds(pb_8, pb_8, pb_8, pb_16, outRect)
                            }
                        }
                        else -> {
                        }
                    }
                }
            } else {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE ||
                    sortType == SortDrinkType.INGREDIENT_COUNT_ASC || sortType == SortDrinkType.INGREDIENT_COUNT_DESC
                ) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        setBounds(lb_8, lb_0, lb_8, lb_0, outRect)
                    } else {
                        setBounds(ingb_4, ingb_0, ingb_4, ingb_0, outRect)
                    }
                } else setBounds(pb_8, pb_0, pb_8, pb_0, outRect)
            }
        }
    }

    companion object {
        private const val pb_0 = 0
        private const val pb_8 = 8
        private const val pb_16 = 16

        private const val lb_0 = 0
        private const val lb_8 = 8
        private const val lb_16 = 16

        private const val ingb_0 = 0
        private const val ingb_4 = 4
        private const val ingb_8 = 8
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