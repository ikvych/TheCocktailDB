package com.ikvych.cocktail.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.LinearLayout


class CustomLinerLayout(
    context: Context?,
    attrs: AttributeSet?
) : LinearLayout(context, attrs) {

    private var listener: KeyBoardListener? = null
    fun setListener(listener: KeyBoardListener?) {
        this.listener = listener
    }

    interface KeyBoardListener {
        fun onSoftKeyboardShown(isShowing: Boolean)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val activity = context as Activity
        val rect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        val statusBarHeight: Int = rect.top
        val screenHeight = activity.windowManager.defaultDisplay.height
        val diff = screenHeight - statusBarHeight - height

        listener!!.onSoftKeyboardShown(diff > 128) // assume all soft keyboards are at least 128 pixels high

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}