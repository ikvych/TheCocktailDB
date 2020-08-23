package com.ikvych.cocktail.presentation.adapter.list

import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ItemDrinkListBinding
import com.ikvych.cocktail.databinding.ItemFavoriteDrinkListBinding
import com.ikvych.cocktail.databinding.ItemHeaderDrinkListBinding
import com.ikvych.cocktail.presentation.activity.MainActivity
import com.ikvych.cocktail.presentation.filter.type.SortDrinkType
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.viewmodel.cocktail.CocktailViewModel
import java.util.*

class CocktailAdapter(
    private val viewModel: CocktailViewModel,
    private val context: Context,
    private val itemViewType: ItemViewLook,
    private val recyclerView: RecyclerView,
    private val spacingForCustomLayoutManager: Int = 8
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    private val cardItemLayoutId = R.layout.item_drink_list
    private val listItemLayoutId = R.layout.item_favorite_drink_list
    private val headerLayoutId = R.layout.item_header_drink_list

    /**Змінна, значення якої залежить від того, чи recyclerView цього адаптера уже містить LayoutManager.
     *Якщо містить, тоді адаптер пристосовується під переданий LayoutManager, якщо ні, тоді використовується дефолтний
     *LayoutManager, а саме GridLayoutManager з spanCount = 4 і його реалізація відповідає Заняття 14. Recycler View
     */
    private val isDefaultLayoutManagerEnabled: Boolean
    private val orientation: Int = context.resources.configuration.orientation

    //На основі цієї змінної визначається однакова висота для всіх елементів
    private val defaultItemSpanSize =
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2

    private var spanSizeLookup: GridLayoutManager.SpanSizeLookup? = null
    private var layoutManager: GridLayoutManager? = null
    private var diffUtilCallback: DefaultDiffUtilCallback? = null

    init {
        isDefaultLayoutManagerEnabled = if (recyclerView.layoutManager == null) {
            //Налаштування LayoutManager по Заняттю 14. Recycler View
            spanSizeLookup = setSpanSizeLookup()
            layoutManager = GridLayoutManager(context, 4)
            layoutManager!!.spanSizeLookup = spanSizeLookup
            recyclerView.layoutManager = layoutManager
            diffUtilCallback = DefaultDiffUtilCallback()
            if (itemViewType == ItemViewLook.CARD_ITEM)
                recyclerView.addItemDecoration(CardItemDecorator())
            if (itemViewType == ItemViewLook.LIST_ITEM)
                recyclerView.addItemDecoration(ListItemDecorator())
            //Тільки MainActivity підтримує видалення елементів з списку
            if (context is MainActivity) {
                //Використовується для видалення і повернення елементів у список, при чому ці маніпуляції відбуваються
                //через базу даних
                ItemTouchHelper(SimpleItemTouchHelperCallback()).attachToRecyclerView(recyclerView)
            }
            (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            true
        } else {
            //Для забезпечення однакових відступів для усіх елементів, додаю марджіни для самого recyclerView.
            //Це застосовується тільки для recyclerView у якого уже є свій LayoutManager
            val marginLayoutParams =
                recyclerView.layoutParams as MarginLayoutParams
            val margin = convertDpToPx(spacingForCustomLayoutManager).toInt()
            marginLayoutParams.setMargins(margin, margin, margin, margin)
            recyclerView.layoutParams = marginLayoutParams
            recyclerView.addItemDecoration(ItemDecorator(spacingForCustomLayoutManager))
            false
        }
    }

    private var oldSortType: SortDrinkType = SortDrinkType.RECENT
    var sortType: SortDrinkType = SortDrinkType.RECENT
        set(value) {
            oldSortType = SortDrinkType.values().first { it.key == sortType.key }
            field = value
        }

    //Основний список з даними для recyclerView який має свій власний LayoutManager,
    private var listData: List<Any> = arrayListOf()
    fun getListData(): List<Any> {
        return listData
    }

    //старі дані які використовуються для порівняння в DiffUtil
    private var oldDefaultDataList: ArrayList<Pair<Any, String>> = arrayListOf()

    //Основний список з даними для дефолтної реалізації LayoutManager,
    //Містить в собі парами - елемент списку з його хідером, це забезпечує, швидкий пошук елемента по його хідеру
    //Якщо самим елементом являється хідер, то його пара дорівнює Blank
    private val defaultDataList: ArrayList<Pair<Any, String>> = arrayListOf()

    //Мапа в якій містяться елементи головного списку з їхніми хідерами в якості ключа
    //Використовується для визначення spanCount і ItemOffset кожного елемента
    private val headersSortedDataMap: SortedMap<String, ArrayList<Any>> = sortedMapOf()

    //Мапа в якій містяться згорнуті елементи(HeaderState.COLLAPSED) головного списку з їхніми хідерами в якості ключа
    //При згортанні секції, дані з головного списку видаляються і поміщаються сюди,
    //При розгортанні, дані видаляються з цього списку і повертаються в свою позицію в головний список
    private val collapsedData: SortedMap<String, ArrayList<Any>> = sortedMapOf()

    //Мапа в якій містяться усі хідерами з їхніми станами(HeaderState.COLLAPSED, HeaderState.EXPANDED)
    //Використовується при відмальовці списку, для визначення чи розгорнута чи згорнута конкретна секція
    private val headersStateMap: SortedMap<String, HeaderState> = sortedMapOf()

    /**
     * Головний метод для засечування і формування нових даних, які повинні відображатися
     *
     * Оскільки, в адаптер уже приходять відсортовані списки, то достатньо просто отримати тип сортування SortDrinkType
     */
    fun updateListData(newData: List<CocktailModel>) {
        if (isDefaultLayoutManagerEnabled) {
            //Виконується якщо реалізація LayoutManager дефолтна
            listData = newData
            clearOldData()
            configureNewData()
            //окремо відтворюємно дані згорнутих хідерів
            configureCollapsedData()
            DiffUtil.calculateDiff(diffUtilCallback!!).dispatchUpdatesTo(this)
            //Потрібно оновити усі видимі елементи, оскільки є випадки при яких позиції елементів мінялися
            //але їхні відступи не перевираховувались, і таким чином відображалися неправильні offsets
            //Наприклад видалення елемента за допомогою swipe
            updateVisibleElements()
        } else {
            //Виконується якщо recyclerView уже містив у собі LayoutManager
            listData = newData
            notifyDataSetChanged()
        }
    }

    private var currentHeader: Any? = null
    private fun configureNewData() {
        when (sortType) {
            SortDrinkType.RECENT -> {
                defaultDataList.addAll(listData.map { it to SortDrinkType.RECENT.key })
                headersSortedDataMap[SortDrinkType.RECENT.key] = arrayListOf()
                headersSortedDataMap[SortDrinkType.RECENT.key]!!.addAll(listData)
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
            headersSortedDataMap[newHeader.toString()] = arrayListOf(element)
            headersStateMap[newHeader.toString()] = HeaderState.EXPANDED
            defaultDataList.add(newHeader.toString() to "")
            defaultDataList.add(element to newHeader.toString())
        } else {
            headersSortedDataMap[currentHeader.toString()]?.add(element)
            defaultDataList.add(element to currentHeader.toString())
        }
    }

    private fun updateVisibleElements() {
        val first = layoutManager!!.findFirstVisibleItemPosition()
        val last = layoutManager!!.findLastVisibleItemPosition()
        for (i in first..last) {
            notifyItemChanged(i)
        }
    }

    private fun clearOldData() {
        oldDefaultDataList = defaultDataList.clone() as ArrayList<Pair<Any, String>>
        defaultDataList.clear()
        headersSortedDataMap.clear()
        headersStateMap.clear()
    }

    private fun configureCollapsedData() {
        if (oldSortType == sortType) {
            val headersValues = collapsedData.keys
            collapsedData.clear()
            headersValues.forEach { header ->
                headersStateMap[header] = HeaderState.COLLAPSED
                defaultDataList.removeAll { pair ->
                    if (pair.second == header) {
                        collapsedData[header]!!.add(pair.first)
                        true
                    } else false
                }
            }
        } else {
            collapsedData.clear()
        }
    }

    override fun getItemCount(): Int {
        return if (isDefaultLayoutManagerEnabled) {
            defaultDataList.size
        } else {
            listData.size
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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
                if (itemViewType == ItemViewLook.LIST_ITEM) {
                    FavoriteCocktailViewHolder(
                        DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            listItemLayoutId,
                            parent,
                            false
                        )
                    )
                } else {
                    CocktailViewHolder(
                        DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            cardItemLayoutId,
                            parent,
                            false
                        )
                    )
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isDefaultLayoutManagerEnabled) {
            when (defaultDataList[position].first) {
                is String -> {
                    ViewType.HEADER.ordinal
                }
                else -> {
                    ViewType.ITEM.ordinal
                }
            }
        } else {
            ViewType.ITEM.ordinal
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isDefaultLayoutManagerEnabled) {
            onBind(holder, position, defaultDataList[position].first)
        } else {
            onBind(holder, position, listData[position])
        }
    }

    private fun onBind(holder: RecyclerView.ViewHolder, position: Int, model: Any) {
        when (holder) {
            is CocktailViewHolder -> {
                holder.binding.obj = model as CocktailModel
                holder.binding.viewModel = viewModel
                holder.binding.cvItemDrink.setOnLongClickListener(context as? View.OnLongClickListener)
                holder.binding.cvItemDrink.setOnClickListener(context as? View.OnClickListener)
                if (isDefaultLayoutManagerEnabled) {
                    // на деяких девайсах не встановлювалась коректно min і max height, тому прийшлося заміряти і встановлювати конкретну
                    //висоту для кожного елемента
                    holder.binding.ivDrinkImage.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    )
                    val itemLayoutParam = holder.binding.ivDrinkImage.layoutParams
                    val layoutWidth = layoutManager!!.width / defaultItemSpanSize
                    itemLayoutParam.height = layoutWidth
                    holder.binding.ivDrinkImage.layoutParams = itemLayoutParam
                }
                holder.binding.executePendingBindings()
            }
            is FavoriteCocktailViewHolder -> {
                holder.binding.obj = model as CocktailModel
                holder.binding.viewModel = viewModel
                holder.binding.ibPopupMenu.setOnClickListener(context as? View.OnClickListener)
                holder.binding.cvItemDrink.setOnClickListener(context as? View.OnClickListener)
                holder.binding.executePendingBindings()
            }
            is HeaderViewHolder -> {
                holder.binding.obj = model.toString()
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
            if (isDefaultLayoutManagerEnabled) {
                binding.root.setOnClickListener(this@CocktailAdapter)
            }
        }
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v) {
            is TextView -> {
                val header = v.text.toString()
                when (headersStateMap[header]) {
                    HeaderState.EXPANDED -> {
                        oldDefaultDataList = defaultDataList.clone() as ArrayList<Pair<Any, String>>
                        headersStateMap[header] = HeaderState.COLLAPSED
                        collapsedData[header] = arrayListOf()
                        defaultDataList.removeAll {
                            if (it.second == header) {
                                collapsedData[header]!!.add(it.first)
                                true
                            } else false
                        }
                    }
                    HeaderState.COLLAPSED -> {
                        oldDefaultDataList = defaultDataList.clone() as ArrayList<Pair<Any, String>>
                        headersStateMap[header] = HeaderState.EXPANDED
                        val hidedList: ArrayList<Any> = collapsedData.remove(header)!!
                        val existedHeader =
                            defaultDataList.first { it.first.toString() == header }
                        val headerIndex = defaultDataList.indexOf(existedHeader)
                        val collection = hidedList.map { it to header }
                        defaultDataList.addAll(headerIndex + 1, collection)
                    }
                }
                DiffUtil.calculateDiff(diffUtilCallback!!).dispatchUpdatesTo(this)
            }
        }
    }

    inner class DefaultDiffUtilCallback :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldDefaultDataList.size
        }

        override fun getNewListSize(): Int {
            return defaultDataList.size
        }

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            val oldElement = oldDefaultDataList[oldItemPosition]
            val newElement = defaultDataList[newItemPosition]
            var result = false
            //Оскільки у списках є різні типи елементів, то я порівнюю їх по їхніх хідерах
            //Якщо хідер являються Blank це означає, що наразі я порівню в якості елемента сам хідер
            if (oldElement.second.isBlank() && newElement.second.isBlank()) {
                result = (oldElement.first as String) == (newElement.first as String)
            }
            //Якщо хідер не являються Blank це означає, що наразі я порівню в якості елемента CocktailModel
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
            val oldElement = oldDefaultDataList[oldItemPosition]
            val newElement = defaultDataList[newItemPosition]
            var result = false
            //Оскільки у списках є різні типи елементів, то я порівнюю їх по їхніх хідерах
            //Якщо хідер являються Blank це означає, що наразі я порівню в якості елемента сам хідер
            if (oldElement.second.isBlank() && newElement.second.isBlank()) {
                result = (oldElement.first as String) == (newElement.first as String)
            }
            //Якщо хідер не являються Blank це означає, що наразі я порівню в якості елемента CocktailModel
            if (oldElement.second.isNotBlank() && newElement.second.isNotBlank()) {
                result =
                    (oldElement.first as CocktailModel).isFavorite == (newElement.first as CocktailModel).isFavorite
            }
            return result
        }
    }

    //Реалізація ItemDecorator для recyclerView  у якого уже є свій власний LayoutManager
    inner class ItemDecorator(private val offset: Int = 8) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            setBounds(offset, offset, offset, offset, outRect)
        }
    }

    //Реалізація ItemDecorator для дефолтної реалізації LayoutManager з ItemViewLook.LIST_ITEM
    inner class ListItemDecorator : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            if (view is LinearLayout) {
                setBounds(ingb_4, ingb_8, ingb_4, ingb_8, outRect)
            }
            if (view is TextView) {
                setBounds(ingb_4, ingb_0, ingb_4, ingb_0, outRect)
            }
        }
    }

    //Реалізація ItemDecorator для дефолтної реалізації LayoutManager з ItemViewLook.CARD_ITEM
    inner class CardItemDecorator : RecyclerView.ItemDecoration() {

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
                val currentHeader = defaultDataList[position].second
                val currentElement = defaultDataList[position].first
                val subList = headersSortedDataMap[currentHeader]!!
                val sublistElementIndex = subList.indexOf(currentElement)

                if (orientation == Configuration.ORIENTATION_LANDSCAPE ||
                    sortType == SortDrinkType.INGREDIENT_COUNT_ASC || sortType == SortDrinkType.INGREDIENT_COUNT_DESC
                ) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        setBoundsForLandscapeOrientation(
                            outRect,
                            lb_8,
                            lb_16,
                            subList,
                            sublistElementIndex
                        )
                    } else {
                        setBoundsForLandscapeOrientation(
                            outRect,
                            ingb_4,
                            ingb_8,
                            subList,
                            sublistElementIndex
                        )
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
            } else if (view is TextView) {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE ||
                    sortType == SortDrinkType.INGREDIENT_COUNT_ASC || sortType == SortDrinkType.INGREDIENT_COUNT_DESC
                ) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        setBounds(lb_8, lb_0, lb_8, lb_0, outRect)
                    } else {
                        setBounds(ingb_4, ingb_0, ingb_4, ingb_0, outRect)
                    }
                } else setBounds(pb_8, pb_0, pb_8, pb_0, outRect)
            } else {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE ||
                    sortType == SortDrinkType.INGREDIENT_COUNT_ASC || sortType == SortDrinkType.INGREDIENT_COUNT_DESC
                ) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        setBounds(lb_8, lb_8, lb_8, lb_8, outRect)
                    } else {
                        setBounds(ingb_4, ingb_4, ingb_4, ingb_4, outRect)
                    }
                } else setBounds(pb_8, pb_8, pb_8, pb_8, outRect)
            }
        }
    }

    private fun setBoundsForLandscapeOrientation(
        outRect: Rect,
        min: Int,
        max: Int,
        subList: ArrayList<Any>,
        sublistElementIndex: Int
    ) {
        when (subList.size % 4) {
            0 -> {
                when (sublistElementIndex % 2) {
                    0 -> {
                        when (sublistElementIndex % 4) {
                            2 -> setBounds(min, min, min, min, outRect)
                            0 -> setBounds(min, min, min, max, outRect)
                        }
                    }
                    1 -> {
                        when ((sublistElementIndex - 1) % 4) {
                            2 -> setBounds(min, max, min, min, outRect)
                            0 -> setBounds(min, min, min, min, outRect)
                        }
                    }
                }
            }
            3 -> {
                when (sublistElementIndex) {
                    0 -> setBounds(min, max, min, max, outRect)
                    1 -> setBounds(min, min, min, max, outRect)
                    2 -> setBounds(min, max, min, min, outRect)
                    else -> {
                        when (sublistElementIndex % 2) {
                            0 -> {
                                when (sublistElementIndex % 4) {
                                    2 -> setBounds(min, max, min, min, outRect)
                                    0 -> setBounds(min, min, min, min, outRect)
                                }
                            }
                            1 -> {
                                when ((sublistElementIndex - 1) % 4) {
                                    2 -> setBounds(min, min, min, max, outRect)
                                    0 -> setBounds(min, min, min, min, outRect)
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                when (sublistElementIndex) {
                    0 -> setBounds(min, min, min, max, outRect)
                    1 -> setBounds(min, max, min, min, outRect)
                    else -> {
                        when (sublistElementIndex % 2) {
                            0 -> {
                                when (sublistElementIndex % 4) {
                                    2 -> setBounds(min, min, min, max, outRect)
                                    0 -> setBounds(min, min, min, min, outRect)
                                }
                            }
                            1 -> {
                                when ((sublistElementIndex - 1) % 4) {
                                    2 -> setBounds(min, min, min, min, outRect)
                                    0 -> setBounds(min, max, min, min, outRect)
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                when (sublistElementIndex) {
                    0 -> setBounds(min, max, min, max, outRect)
                    else -> {
                        when (sublistElementIndex % 2) {
                            0 -> {
                                when (sublistElementIndex % 4) {
                                    2 -> setBounds(min, min, min, min, outRect)
                                    0 -> setBounds(min, max, min, min, outRect)
                                }
                            }
                            1 -> {
                                when ((sublistElementIndex - 1) % 4) {
                                    2 -> setBounds(min, min, min, min, outRect)
                                    0 -> setBounds(min, min, min, max, outRect)
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

    //Для зменшення кількості перевірок при взятті spanSize, створив декілька заточених під певну конфігурацію SpanSizeLookup
    private fun setSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
        return if (itemViewType == ItemViewLook.LIST_ITEM) {
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return 4
                }
            }
        } else {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val currentElementPair = defaultDataList[position]
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
                        val currentElementPair = defaultDataList[position]
                        return when (currentElementPair.first) {
                            is CocktailModel -> {
                                if (sortType == SortDrinkType.INGREDIENT_COUNT_ASC || sortType == SortDrinkType.INGREDIENT_COUNT_DESC) {
                                    //у завданні описано вигляд для сортування по кількості інгредієнтів, і він у мене виглядає
                                    //так само як при ORIENTATION_LANDSCAPE, тому отримую спани такі ж як і для горизонтальної орієниації
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

    private fun getSpanForPortraitOrientation(element: Any, header: String): Int {
        val subList = headersSortedDataMap[header]!!
        return when (subList.size % 2) {
            0 -> 2
            1 -> if (subList.indexOf(element) == 0) 4 else 2
            else -> 4
        }
    }

    private fun getSpanForLandscapeOrientation(element: Any, header: String): Int {
        val subList = headersSortedDataMap[header]!!
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
            else -> 4
        }
    }

    private var mRecentlyDeletedItem: Pair<Any, String>? = null
    private var mRecentlyDeletedItemPosition: Int? = null

    fun deleteItem(position: Int, view: View) {
        mRecentlyDeletedItem = defaultDataList[position]
        mRecentlyDeletedItemPosition = position
        viewModel.removeCocktail(mRecentlyDeletedItem!!.first as CocktailModel)
        showUndoSnackBar(view)
    }

    private fun showUndoSnackBar(view: View) {
        val snackBar: Snackbar = Snackbar.make(
            view,
            "${(mRecentlyDeletedItem!!.first as CocktailModel).names.defaultName} removed from history",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction(R.string.all_undo_button) { undoDelete() }
        snackBar.show()
    }

    private fun undoDelete() {
        viewModel.saveCocktail(mRecentlyDeletedItem!!.first as CocktailModel)
    }

    inner class SimpleItemTouchHelperCallback :
        ItemTouchHelper.Callback() {

        override fun isLongPressDragEnabled(): Boolean {
            return false
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return when (viewHolder) {
                is CocktailViewHolder -> {
                    val dragFlags = ItemTouchHelper.ACTION_STATE_IDLE
                    val swipeFlags = ItemTouchHelper.END
                    makeMovementFlags(
                        dragFlags,
                        swipeFlags
                    )
                }
                is FavoriteCocktailViewHolder -> {
                    val dragFlags = ItemTouchHelper.ACTION_STATE_IDLE
                    val swipeFlags = ItemTouchHelper.START
                    makeMovementFlags(
                        dragFlags,
                        swipeFlags
                    )
                }
                else -> {
                    val dragFlags = ItemTouchHelper.ACTION_STATE_IDLE
                    val swipeFlags = ItemTouchHelper.ACTION_STATE_IDLE
                    makeMovementFlags(
                        dragFlags,
                        swipeFlags
                    )
                }
            }
        }

        override fun onMove(
            recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(
            viewHolder: RecyclerView.ViewHolder,
            direction: Int
        ) {
            this@CocktailAdapter.deleteItem(viewHolder.adapterPosition, viewHolder.itemView)
        }
    }
}