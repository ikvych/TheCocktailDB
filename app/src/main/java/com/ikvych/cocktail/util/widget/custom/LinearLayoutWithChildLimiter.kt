package com.ikvych.cocktail.util.widget.custom

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.ikvych.cocktail.R
import com.ikvych.cocktail.util.widget.custom.LinearLayoutWithChildLimiter.LinearLayoutWithChildLimiterLayoutParams.PercentSize.NONE_PERCENT

class LinearLayoutWithChildLimiter : ViewGroup {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    var orientation: Int = Orientation.HORIZONTAL

    /** These are used for computing child frames based on their gravity.  */
    private val mTmpContainerRect: Rect = Rect()
    private val mTmpChildRect: Rect = Rect()

    private fun init(attrs: AttributeSet?) {
        setWillNotDraw(false)

        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.LinearLayoutWithChildLimiter)
        try {
            orientation =
                typedArray.getInt(
                    R.styleable.LinearLayoutWithChildLimiter_android_orientation,
                    orientation
                )

        } finally {
            typedArray.recycle()
        }
    }

    /**
     * Any layout manager that doesn't scroll will want this.
     */
    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val count = childCount

        // Measurement will ultimately be computing these values.
        var maxHeight = 0
        var maxWidth = 0
        var childState = 0

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                // Measure the child.
                measureCurrentChild(child, widthMeasureSpec, heightMeasureSpec)

                // Update our size information based on the layout params.
                val lp =
                    child.layoutParams as LinearLayoutWithChildLimiterLayoutParams

                when (orientation) {
                    Orientation.HORIZONTAL -> {
                        if (lp.layoutPercentSize != NONE_PERCENT) {
                            lp.width = measuredWidth * lp.layoutPercentSize / 100
                            measureCurrentChild(child, widthMeasureSpec, heightMeasureSpec)
                        }
                        when (lp.width) {
                            LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT -> {
                                if (child.measuredWidth > lp.layoutMaxSize.toInt() && lp.layoutMaxSize != LayoutParams.WRAP_CONTENT.toFloat()) {
                                    lp.width = lp.layoutMaxSize.toInt()
                                    measureCurrentChild(child, widthMeasureSpec, heightMeasureSpec)
                                }
                            }
                            else -> {
                                if (lp.width > lp.layoutMaxSize.toInt() && lp.layoutMaxSize != LayoutParams.WRAP_CONTENT.toFloat()) {
                                    lp.width = lp.layoutMaxSize.toInt()
                                    measureCurrentChild(child, widthMeasureSpec, heightMeasureSpec)
                                }
                            }
                        }
                        maxHeight = (child.measuredHeight + child.marginTop + child.marginBottom).coerceAtLeast(maxHeight)
                        maxWidth += child.measuredWidth + child.marginStart + child.marginEnd
                    }
                    Orientation.VERTICAL -> {
                        if (lp.layoutPercentSize != NONE_PERCENT) {
                            lp.height = measuredWidth * lp.layoutPercentSize / 100
                            measureCurrentChild(child, widthMeasureSpec, heightMeasureSpec)
                        }
                        when (lp.height) {
                            LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT -> {
                                if (child.measuredHeight > lp.layoutMaxSize.toInt() && lp.layoutMaxSize != LayoutParams.WRAP_CONTENT.toFloat()) {
                                    lp.height = lp.layoutMaxSize.toInt()
                                    measureCurrentChild(child, widthMeasureSpec, heightMeasureSpec)
                                }
                            }
                            else -> {
                                if (lp.height > lp.layoutMaxSize.toInt() && lp.layoutMaxSize != LayoutParams.WRAP_CONTENT.toFloat()) {
                                    lp.height = lp.layoutMaxSize.toInt()
                                    measureCurrentChild(child, widthMeasureSpec, heightMeasureSpec)
                                }
                            }
                        }
                        maxHeight += child.measuredHeight + child.marginTop + child.marginBottom
                        maxWidth = (child.measuredWidth + child.marginStart + child.marginEnd).coerceAtLeast(maxWidth)
                    }
                }
                childState =
                    View.combineMeasuredStates(childState, child.measuredState)
            }
        }

        // Check against our minimum height and width
        maxHeight = maxHeight.coerceAtLeast(suggestedMinimumHeight)
        maxWidth = maxWidth.coerceAtLeast(suggestedMinimumWidth)

        // Report our final dimensions.
        setMeasuredDimension(
            View.resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
            View.resolveSizeAndState(
                maxHeight, heightMeasureSpec,
                childState shl View.MEASURED_HEIGHT_STATE_SHIFT
            )
        )
    }

    private fun measureCurrentChild(child: View, widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildWithMargins(
            child,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0
        )
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val count = childCount

        // These are the far left and right edges in which we are performing layout.
        var leftPos = paddingLeft
        var rightPos = right/* - left*/ - paddingRight

        // These are the top and bottom edges in which we are performing layout.
        var parentTop = paddingTop
        val parentBottom = bottom/* - top*/ - paddingBottom

        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                val childLp =
                    child.layoutParams as LinearLayoutWithChildLimiterLayoutParams
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight

                when (orientation) {
                    Orientation.HORIZONTAL -> {
                        // Compute the frame in which we are placing this child.
                        mTmpContainerRect.left = leftPos + childLp.leftMargin
                        mTmpContainerRect.right = mTmpContainerRect.left + childWidth + childLp.rightMargin
                        leftPos = mTmpContainerRect.right

                        mTmpContainerRect.top = parentTop + childLp.topMargin
                        mTmpContainerRect.bottom = parentBottom - childLp.bottomMargin
                    }
                    Orientation.VERTICAL -> {
                        // Compute the frame in which we are placing this child.
                        mTmpContainerRect.left = leftPos + childLp.leftMargin
                        mTmpContainerRect.right = rightPos - childLp.rightMargin

                        mTmpContainerRect.top = parentTop + childLp.topMargin
                        mTmpContainerRect.bottom =
                            mTmpContainerRect.top + childHeight + childLp.bottomMargin
                        parentTop = mTmpContainerRect.bottom
                    }
                }

                // Use the child's gravity and size to determine its final
                // frame within its container.
                Gravity.apply(
                    childLp.gravity,
                    childWidth,
                    childHeight,
                    mTmpContainerRect,
                    mTmpChildRect
                )

                // Place the child.
                child.layout(
                    mTmpChildRect.left, mTmpChildRect.top,
                    mTmpChildRect.right, mTmpChildRect.bottom
                )
            }
        }
    }


    override fun generateLayoutParams(attrs: AttributeSet): LinearLayoutWithChildLimiterLayoutParams {
        return LinearLayoutWithChildLimiterLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LinearLayoutWithChildLimiterLayoutParams {
        return LinearLayoutWithChildLimiterLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): LinearLayoutWithChildLimiterLayoutParams {
        return LinearLayoutWithChildLimiterLayoutParams(p)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LinearLayoutWithChildLimiterLayoutParams
    }

    object Orientation {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    class LinearLayoutWithChildLimiterLayoutParams : MarginLayoutParams {
        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
        constructor(c: Context, attrs: AttributeSet?) : super(
            c,
            attrs
        ) {
            init(c, attrs)
        }

        var layoutMaxSize: Float = LayoutParams.WRAP_CONTENT.toFloat()
        var layoutPercentSize: Int = NONE_PERCENT
        var gravity = Gravity.TOP or Gravity.START

        private fun init(context: Context, attrs: AttributeSet?) {

            val typedArray =
                context.obtainStyledAttributes(
                    attrs,
                    R.styleable.LinearLayoutWithChildLimiter_Layout
                )
            try {
                layoutMaxSize =
                    typedArray.getDimension(
                        R.styleable.LinearLayoutWithChildLimiter_Layout_layout_max_size,
                        layoutMaxSize
                    )
                layoutPercentSize =
                    typedArray.getInteger(
                        R.styleable.LinearLayoutWithChildLimiter_Layout_layout_percent_size,
                        layoutPercentSize
                    )
                gravity = typedArray.getInt(
                    R.styleable.LinearLayoutWithChildLimiter_Layout_android_layout_gravity,
                    gravity
                )

            } finally {
                typedArray.recycle()
            }
        }

        object PercentSize {
            const val NONE_PERCENT = -1
        }
    }
}