package com.ikvych.cocktail.widget.custom

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.ikvych.cocktail.R
import kotlinx.android.synthetic.main.widget_app_toolbar.view.*

class ApplicationToolBar(
    context: Context?,
    attrs: AttributeSet?
) : LinearLayout(context, attrs) {

    var returnBtn: ImageButton
    private var frameLayout: FrameLayout
    var customBtn: ImageButton
    var indicatorView: TextView
    var sortBtn: ImageButton
    var sortIndicatorView: TextView
    var searchView: SearchView
    private var textView: TextView

    var isForSearch: Boolean = false
    set(value) {
        field = value
        if (isForSearch) {
            searchView.visibility = View.VISIBLE
            textView.visibility = View.GONE
        } else {
            searchView.visibility = View.GONE
            textView.visibility = View.VISIBLE
        }
        invalidate()
        requestLayout()
    }

    var mainTitle: String? = null
        set(value) {
            field = value
            textView.text = field
            invalidate()
            requestLayout()
        }

    var isCustomBtnEnabled: Boolean = false
    set(value) {
        field = value
        if (field) {
            customBtn.visibility = View.VISIBLE
            relativeLayoutCustomBtn.visibility = View.VISIBLE
        } else {
            customBtn.visibility = View.GONE
            relativeLayoutCustomBtn.visibility = View.GONE
        }
        invalidate()
        requestLayout()
    }

    var isReturnBtnDisabled: Boolean = false
        set(value) {
            field = value
            if (field) {
                returnBtn.visibility = View.GONE
            } else {
                returnBtn.visibility = View.VISIBLE
            }
            invalidate()
            requestLayout()
        }
    var isSortBtnEnabled: Boolean = false
        set(value) {
            field = value
            if (field) {
                sortBtn.visibility = View.VISIBLE
                rl_sort_btn_container.visibility = View.VISIBLE
            } else {
                sortBtn.visibility = View.GONE
                rl_sort_btn_container.visibility = View.GONE
            }
            invalidate()
            requestLayout()
        }
    var relativeLayoutCustomBtn: RelativeLayout
    var relativeLayoutSortBtn: RelativeLayout

    init {
        View.inflate(context, R.layout.widget_app_toolbar, this)
        this.returnBtn = findViewById(R.id.ib_return_button)
        this.customBtn = findViewById(R.id.ib_filer_btn)
        this.indicatorView = findViewById(R.id.tv_filter_indicator)
        this.frameLayout = findViewById(R.id.fl_toolbar)
        this.searchView = findViewById(R.id.sv_toolbar)
        this.textView = findViewById(R.id.tv_toolbar)
        this.sortBtn = findViewById(R.id.ib_sort_btn)
        this.sortIndicatorView = findViewById(R.id.tv_sort_indicator)
        this.relativeLayoutCustomBtn = rl_filer_btn_container
        this.relativeLayoutSortBtn = rl_sort_btn_container

        context!!.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ApplicationToolBar,
            0, 0).apply {
            try {
                isForSearch = getBoolean(R.styleable.ApplicationToolBar_tl_is_for_search, false)
                mainTitle = getString(R.styleable.ApplicationToolBar_tl_set_text) ?: context.getString(R.string.app_name)
                isCustomBtnEnabled = getBoolean(R.styleable.ApplicationToolBar_tl_enable_custom_btn, false)
                isReturnBtnDisabled = getBoolean(R.styleable.ApplicationToolBar_tl_disable_return_btn, false)
                isSortBtnEnabled = getBoolean(R.styleable.ApplicationToolBar_tl_enable_sort_btn, false)
            } finally {
                recycle()
            }
        }

        returnBtn.setOnClickListener {
            if (context is Activity) {
                context.finish()
            }
        }
    }
}