package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.comparator.type.SortOrder
import com.ikvych.cocktail.ui.base.LeftDialogButton
import com.ikvych.cocktail.ui.base.RegularDialogButton
import com.ikvych.cocktail.ui.base.RegularDialogType
import com.ikvych.cocktail.ui.base.RightDialogButton


class SortDrinkDialogFragment :
    SimpleBaseDialogFragment<SortDrinkType, RegularDialogButton, RegularDialogType, SimpleBaseDialogFragment.SimpleDialogBuilder>() {

    lateinit var recentSort: TextView

    lateinit var nameSort: TextView
    lateinit var nameOrder: CheckBox
    lateinit var alcoholSort: TextView
    lateinit var alcoholOrder: CheckBox
    lateinit var ingredientCountSort: TextView
    lateinit var ingredientCountOrder: CheckBox

    val sortDrinksType: List<SortDrinkType> = SortDrinkType.values().toList()

    override val dialogType: RegularDialogType = RegularDialogType
    override val extraContentLayoutResId: Int = R.layout.layout_dialog_drink_sort

    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()
    override var data: SortDrinkType? = SortDrinkType.RECENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
    }

    override fun obtainDataForView(view: View): SortDrinkType? {
        return data
    }

    override fun getButtonType(view: View): RegularDialogButton {
        return when (view.id) {
            R.id.lb_dialog_bs_left -> LeftDialogButton
            R.id.lb_dialog_bs_right -> RightDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }


    override fun configureExtraContent(container: FrameLayout, savedInstanceState: Bundle?) {
        this.data = requireArguments().get(EXTRA_KEY_DATA) as? SortDrinkType ?: SortDrinkType.RECENT

        recentSort = container.findViewById(R.id.tv_recent_order)
        recentSort.setOnClickListener {
            val index = sortDrinksType.indexOf(SortDrinkType.RECENT)
            data = sortDrinksType[index]
        }

        nameOrder = container.findViewById(R.id.cb_sort_order_name)
        nameSort = container.findViewById(R.id.tv_name_order)
        nameSort.setOnClickListener {
            val index = sortDrinksType.indexOf(SortDrinkType.NAME)
            val currentData = sortDrinksType[index]
            if (nameOrder.isChecked) {
                currentData.sortOrder = SortOrder.Descending
            } else {
                currentData.sortOrder = SortOrder.Ascending
            }
            data = currentData
        }

        nameOrder.setOnCheckedChangeListener { buttonView, isChecked ->
            val index = sortDrinksType.indexOf(SortDrinkType.NAME)
            if (isChecked) {
                sortDrinksType[index].sortOrder = SortOrder.Descending
            } else {
                sortDrinksType[index].sortOrder = SortOrder.Ascending
            }
        }

        alcoholSort = container.findViewById(R.id.tv_alcohol_order)
        alcoholSort.setOnClickListener {
            val index = sortDrinksType.indexOf(SortDrinkType.ALCOHOL)
            data = sortDrinksType[index]
        }
        alcoholOrder = container.findViewById(R.id.cb_sort_order_alcohol)
        alcoholOrder.setOnClickListener {
            val index = sortDrinksType.indexOf(SortDrinkType.ALCOHOL)
            if (it.isSelected) {
                sortDrinksType[index].sortOrder = SortOrder.Descending
            } else {
                sortDrinksType[index].sortOrder = SortOrder.Ascending
            }
        }

        ingredientCountSort = container.findViewById(R.id.tv_ingredient_order)
        ingredientCountSort.setOnClickListener {
            val index = sortDrinksType.indexOf(SortDrinkType.INGREDIENT_COUNT)
            data = sortDrinksType[index]
        }
        ingredientCountOrder = container.findViewById(R.id.cb_sort_order_ingredient)
        ingredientCountOrder.setOnClickListener {
            val index = sortDrinksType.indexOf(SortDrinkType.INGREDIENT_COUNT)
            if (it.isSelected) {
                sortDrinksType[index].sortOrder = SortOrder.Descending
            } else {
                sortDrinksType[index].sortOrder = SortOrder.Ascending
            }
        }
    }

    companion object {
        fun newInstance(data: SortDrinkType? = null, builder: SimpleDialogBuilder.() -> Unit): SortDrinkDialogFragment {
            return getInstance(data, builder)
        }

        /**
         * Note trick that data can be used as tag to distinguish dialogs
         * if multiple instances exists within one context (activity/fragment)
         */
        fun getInstance(
            data: SortDrinkType? = null,
            builder: SimpleDialogBuilder.() -> Unit
        ): SortDrinkDialogFragment {
            val fragment = SortDrinkDialogFragment()
            fragment.arguments = bundleOf(
                EXTRA_KEY_BUILDER to (SimpleDialogBuilder().apply(builder)),
                EXTRA_KEY_DATA to data
            )
            return fragment
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_DATA = "EXTRA_KEY_DATA"
    }
}