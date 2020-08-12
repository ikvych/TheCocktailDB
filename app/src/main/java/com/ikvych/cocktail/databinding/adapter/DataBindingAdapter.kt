package com.ikvych.cocktail.databinding.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.databinding.*
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ItemDrinkIngredientListBinding
import com.ikvych.cocktail.presentation.enumeration.Page
import com.ikvych.cocktail.presentation.model.cocktail.IngredientModel
import jp.wasabeef.glide.transformations.BlurTransformation


@BindingAdapter("bind:src")
fun setImageViewResource(imageButton: ImageButton, resource: Int) {
    imageButton.setImageResource(Math.abs(resource))
}

@BindingAdapter("bind:avatar")
fun loadAvatar(imageView: ImageView, imageUrl: String?) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .placeholder(R.drawable.default_icon)
        .circleCrop()
        .into(imageView)
}

@BindingAdapter("bind:bg_avatar")
fun loadBgAvatar(imageView: ImageView, imageUrl: String?) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .placeholder(R.drawable.default_icon)
        .apply(RequestOptions.bitmapTransform(BlurTransformation(15)))
        .into(imageView)
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
        val binding: ItemDrinkIngredientListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(tableLayout.context),
            R.layout.item_drink_ingredient_list,
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

@BindingAdapter("strDrinkThumb")
fun loadImage(imageView: ImageView, imageUrl: String?) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .placeholder(R.drawable.default_icon)
        .into(imageView)
}

@BindingAdapter("bind:password_visibility")
fun passwordVisibility(view: EditText, isChecked: Boolean) {
    if (isChecked) {
        view.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        view.setSelection(view.text.length)
    } else {
        view.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        view.setSelection(view.text.length)
    }
}

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
            this.visibility = if (isVisible) {
                View.VISIBLE
            } else {
                View.GONE
            }
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
