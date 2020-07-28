package com.ikvych.cocktail.databinding.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.*
import androidx.viewpager2.widget.ViewPager2
import com.ikvych.cocktail.R
import com.ikvych.cocktail.util.Page


@BindingAdapter("bind:src")
fun setImageViewResource(imageButton: ImageButton, resource: Int) {
    imageButton.setImageResource(resource)
}

@BindingAdapter("bind:bg_color")
fun setBackgroundTint(view: View, resource: String) {
    val stateList =
        ColorStateList.valueOf(Color.parseColor(resource))
    view.backgroundTintList = stateList
}

class DataBindingAdapter {

    object ViewVisibilityBindingAdapter {
        @BindingAdapter("bind:v_isVisible")
        @JvmStatic
        fun View.isVisibleView(isVisible: Boolean) {
            this.isVisible = isVisible
        }
    }

    object CheckBoxBindingAdapter {

        @BindingAdapter("bind:cb_checked")
        @JvmStatic
        fun CheckBox.setIsChecked(newValue: Boolean) {
            if (isChecked != newValue) {
                isChecked = newValue
            }
        }

        @InverseBindingAdapter(attribute = "bind:cb_checked", event = "bind:cb_checkedAttrChanged")
        @JvmStatic
        fun CheckBox.getIsChecked(): Boolean? {
            return isChecked
        }

        @BindingAdapter("bind:cb_checkedAttrChanged")
        @JvmStatic
        fun CheckBox.setListener(
            attrChange: InverseBindingListener?
        ) {
            if (attrChange != null) {
                setOnCheckedChangeListener { _, _ -> attrChange.onChange() }
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
