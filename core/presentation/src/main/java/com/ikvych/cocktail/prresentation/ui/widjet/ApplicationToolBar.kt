package com.ikvych.cocktail.prresentation.ui.widjet

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.ikvych.cocktail.prresentation.R
import kotlinx.android.synthetic.main.widget_app_toolbar.view.*

class ApplicationToolBar(
    context: Context?,
    attrs: AttributeSet?
) : LinearLayout(context, attrs) {

    var returnBtn: ImageButton
    private var frameLayout: FrameLayout
    var customBtn2: ImageButton
    var customBtnIndicatorView2: TextView
    var customBtn1: ImageButton
    var customBtnIndicatorView1: TextView
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
                customBtn2.visibility = View.VISIBLE
                relativeLayoutCustomBtn.visibility = View.VISIBLE
            } else {
                customBtn2.visibility = View.GONE
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
                customBtn1.visibility = View.VISIBLE
                rl_first_btn_container.visibility = View.VISIBLE
            } else {
                customBtn1.visibility = View.GONE
                rl_first_btn_container.visibility = View.GONE
            }
            invalidate()
            requestLayout()
        }
    var relativeLayoutCustomBtn: RelativeLayout
    var relativeLayoutSortBtn: RelativeLayout

    var isTransparent: Boolean = false
        set(value) {
            field = value
            if (field) {
                this.background = context.getDrawable(R.drawable.shape_app_backgound_gradient)
                textView.setTextColor(Color.WHITE)
                returnBtn.setColorFilter(Color.WHITE)
                customBtn2.setColorFilter(Color.WHITE)
            } else {
                this.background = context.getDrawable(R.drawable.shape_app_bar_bg)
                textView.setTextColor(Color.BLACK)
                returnBtn.setColorFilter(Color.BLACK)
                customBtn2.setColorFilter(Color.BLACK)
            }
            invalidate()
            requestLayout()
        }

    init {
        View.inflate(context, R.layout.widget_app_toolbar, this)
        this.returnBtn = findViewById(R.id.ib_return_button)
        this.customBtn2 = findViewById(R.id.ib_secondary_btn)
        this.customBtnIndicatorView2 = findViewById(R.id.tv_secondary_btn_indicator)
        this.frameLayout = findViewById(R.id.fl_toolbar)
        this.searchView = findViewById(R.id.sv_toolbar)
        this.textView = findViewById(R.id.tv_toolbar)
        this.customBtn1 = findViewById(R.id.ib_first_btn)
        this.customBtnIndicatorView1 = findViewById(R.id.tv_first_btn_indicator)
        this.relativeLayoutCustomBtn = rl_secondary_btn_container
        this.relativeLayoutSortBtn = rl_first_btn_container

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
                isTransparent = getBoolean(R.styleable.ApplicationToolBar_tl_set_transparent, false)
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