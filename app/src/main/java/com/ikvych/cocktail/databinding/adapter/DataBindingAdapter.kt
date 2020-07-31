package com.ikvych.cocktail.databinding.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.*
import androidx.viewpager2.widget.ViewPager2
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ItemDrinkIngredientBinding
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.ui.model.cocktail.IngredientModel
import com.ikvych.cocktail.util.Page


@BindingAdapter("bind:src")
fun setImageViewResource(imageButton: ImageButton, resource: Int) {
    imageButton.setImageResource(Math.abs(resource))
}

@BindingAdapter("bind:bg_color")
fun setBackgroundTint(view: View, resource: String) {
    val stateList =
        ColorStateList.valueOf(Color.parseColor(resource))
    view.backgroundTintList = stateList
}

/*Using getIngredients() method fills the tableLayout in activity_drink_details with ingredients and measure*/
@BindingAdapter("ingredients", "measures")
fun getIngredients(
    tableLayout: TableLayout,
    ingredients: List<IngredientModel>?,
    measures: List<String>?
) {
    var count = 1
    if (ingredients == null || measures == null) {
        return
    }
    ingredients.forEachIndexed { index, ingredient ->
        val binding: ItemDrinkIngredientBinding = DataBindingUtil.inflate(
            LayoutInflater.from(tableLayout.context),
            R.layout.item_drink_ingredient,
            tableLayout,
            false
        )
        val numberedIngredient = "$count. ${ingredient.key}"
        binding.tvIngredient.text = numberedIngredient
        if (measures.size - 1 >= index) {
            binding.tvMeasure.text = measures[index]
        } else {
            binding.tvMeasure.text = ""
        }
        tableLayout.addView(
            binding.root,
            TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        )
        count++
    }
}

/*Using getIngredients() method fills the tableLayout in activity_drink_details with ingredients and measure*//*
@BindingAdapter("ingredients", "measures")
fun getIngredients(
    tableLayout: TableLayout,
    ingredients: List<IngredientDrinkFilter>?,
    measures: List<String>?
) {
    var count = 1
    if (ingredients == null || measures == null) {
        return
    }
    ingredients.forEachIndexed { index, ingredientDrinkFilter ->
        val binding: ItemDrinkIngredientBinding = DataBindingUtil.inflate(
            LayoutInflater.from(tableLayout.context),
            R.layout.item_drink_ingredient,
            tableLayout,
            false
        )
        val numberedIngredient = "$count. ${ingredientDrinkFilter.key}"
        binding.tvIngredient.text = numberedIngredient
        if (measures.size - 1 >= index) {
            binding.tvMeasure.text = measures[index]
        } else {
            binding.tvMeasure.text = ""
        }
        tableLayout.addView(
            binding.root,
            TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        )
        count++
    }
}*/


class DataBindingAdapter {

    object SwitchBindingAdapter {

        @BindingAdapter("bind:cb_checked")
        @JvmStatic
        fun Switch.setIsChecked(newValue: Boolean) {
            if (isChecked != newValue) {
                isChecked = newValue
            }
        }

        @InverseBindingAdapter(attribute = "bind:cb_checked", event = "bind:cb_checkedAttrChanged")
        @JvmStatic
        fun Switch.getIsChecked(): Boolean? {
            return isChecked
        }

        @BindingAdapter("bind:cb_checkedAttrChanged")
        @JvmStatic
        fun Switch.setListener(
            attrChange: InverseBindingListener?
        ) {
            if (attrChange != null) {
                setOnCheckedChangeListener { _, _ -> attrChange.onChange() }
            }
        }
    }

    object ViewVisibilityBindingAdapter {
        @BindingAdapter("bind:v_isVisible")
        @JvmStatic
        fun View.isVisibleView(isVisible: Boolean) {
            this.isVisible = isVisible
        }
    }

    object ViewPagerBindingAdapter {

        @BindingAdapter("bind:vp_page")
        @JvmStatic
        fun ViewPager2.setPage(page: Int) {
            if (currentItem != page) {
                currentItem = page
            }
        }

        @InverseBindingAdapter(attribute = "bind:vp_page", event = "bind:vp_pageAttrChanged")
        @JvmStatic
        fun ViewPager2.getPage(): Int? {
            return currentItem
        }

        @BindingAdapter("bind:vp_pageAttrChanged")
        @JvmStatic
        fun ViewPager2.setListener(
            attrChange: InverseBindingListener?
        ) {
            if (attrChange != null) {
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        attrChange.onChange()
                    }
                })
            }
        }
    }

    object DataBindingConverter {

        @InverseMethod("convertPageToInt")
        @JvmStatic
        fun convertIntToPage(page: Int): Page = Page.values()[page]

        @BindingConversion
        @JvmStatic
        fun convertPageToInt(page: Page): Int = page.ordinal

    }

}
