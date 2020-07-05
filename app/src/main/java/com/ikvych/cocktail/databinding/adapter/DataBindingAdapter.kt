package com.ikvych.cocktail.databinding.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.ikvych.cocktail.R
import com.ikvych.cocktail.util.Page


class DataBindingAdapter {

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

    interface OnViewPagerChangeListener {
        fun onTabChanged(position: Int)
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