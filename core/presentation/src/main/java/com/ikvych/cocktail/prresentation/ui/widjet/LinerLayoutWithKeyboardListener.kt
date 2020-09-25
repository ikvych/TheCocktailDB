package com.ikvych.cocktail.prresentation.ui.widjet

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.widget.LinearLayout

/**
 * For the correct operation of the method onMeasure, use this liner layout only where there is no
 * dynamic resizing of the screen or pop-up elements
 */
class LinerLayoutWithKeyboardListener(
    context: Context?,
    attrs: AttributeSet?
) : LinearLayout(context, attrs) {

    var listener: KeyBoardListener? = null
    private val rect = Rect()
    private val displayMetrics = DisplayMetrics()

    interface KeyBoardListener {
        fun onSoftKeyboardShown(isShowing: Boolean)
    }

    /**
     * Basic logic is that if the layout finds itself filling significantly less than the total
     * area of the window, then a soft keyboard is probably showing.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val activity = context as Activity
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        val statusBarHeight: Int = rect.top
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val diff = screenHeight - statusBarHeight - height

        listener?.onSoftKeyboardShown(diff > 128) // assume all soft keyboards are at least 128 pixels high

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}