package com.ikvych.cocktail.widget.custom

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.ikvych.cocktail.R

class ApplicationToolBar(
    context: Context?,
    attrs: AttributeSet?
) : LinearLayout(context, attrs) {

    private var returnBtn: ImageButton
    private var frameLayout: FrameLayout
    var customBtn: ImageButton
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
        } else {
            customBtn.visibility = View.GONE
        }
        invalidate()
        requestLayout()
    }

    init {
        View.inflate(context, R.layout.app_toolbar, this)
        this.returnBtn = findViewById(R.id.return_tb_btn)
        this.customBtn = findViewById(R.id.custom_tb_btn)
        this.frameLayout = findViewById(R.id.fl_toolbar)
        this.searchView = findViewById(R.id.sv_toolbar)
        this.textView = findViewById(R.id.tv_toolbar)

        context!!.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ApplicationToolBar,
            0, 0).apply {
            try {
                isForSearch = getBoolean(R.styleable.ApplicationToolBar_tl_is_for_search, false)
                mainTitle = getString(R.styleable.ApplicationToolBar_tl_set_text) ?: context.getString(R.string.app_name)
                isCustomBtnEnabled = getBoolean(R.styleable.ApplicationToolBar_tl_enable_custom_btn, false)
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