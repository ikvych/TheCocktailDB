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

class IvanKvychLayout : ViewGroup {
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

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IvanKvychLayout)
        try {
            orientation =
                typedArray.getInt(
                    R.styleable.IvanKvychLayout_android_orientation,
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
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)

                // Update our size information based on the layout params.
                val lp =
                    child.layoutParams as IvanKvychLayoutParams

                when (orientation) {
                    Orientation.HORIZONTAL -> {
                        when (lp.width) {
                            LayoutParams.WRAP_CONTENT -> {
                                if (child.measuredWidth > lp.layoutMaxSize.toInt() && lp.layoutMaxSize != .0F) {
                                    lp.width = lp.layoutMaxSize.toInt()
                                    child.layoutParams = lp
                                    measureChildWithMargins(
                                        child,
                                        widthMeasureSpec,
                                        0,
                                        heightMeasureSpec,
                                        0
                                    )
                                }
                            }
                            LayoutParams.MATCH_PARENT -> {
                                if (child.measuredWidth > lp.layoutMaxSize.toInt() && lp.layoutMaxSize != .0F) {
                                    lp.width = lp.layoutMaxSize.toInt()
                                    child.layoutParams = lp
                                    measureChildWithMargins(
                                        child,
                                        widthMeasureSpec,
                                        0,
                                        heightMeasureSpec,
                                        0
                                    )
                                }
                            }
                            else -> {
                                if (lp.width > lp.layoutMaxSize.toInt() && lp.layoutMaxSize != .0F) {
                                    lp.width = lp.layoutMaxSize.toInt()
                                    child.layoutParams = lp
                                    measureChildWithMargins(
                                        child,
                                        widthMeasureSpec,
                                        0,
                                        heightMeasureSpec,
                                        0
                                    )
                                }
                            }
                        }
                    }
                    Orientation.VERTICAL -> {
                        when (lp.height) {
                            LayoutParams.WRAP_CONTENT -> {
                                if (child.measuredHeight > lp.layoutMaxSize.toInt() && lp.layoutMaxSize != .0F) {
                                    lp.height = lp.layoutMaxSize.toInt()
                                    child.layoutParams = lp
                                    measureChildWithMargins(
                                        child,
                                        widthMeasureSpec,
                                        0,
                                        heightMeasureSpec,
                                        0
                                    )
                                }
                            }
                            LayoutParams.MATCH_PARENT -> {
                                if (child.measuredHeight > lp.layoutMaxSize.toInt() && lp.layoutMaxSize != .0F) {
                                    lp.height = lp.layoutMaxSize.toInt()
                                    child.layoutParams = lp
                                    measureChildWithMargins(
                                        child,
                                        widthMeasureSpec,
                                        0,
                                        heightMeasureSpec,
                                        0
                                    )
                                }
                            }
                            else -> {
                                if (lp.height > lp.layoutMaxSize.toInt() && lp.layoutMaxSize != .0F) {
                                    lp.height = lp.layoutMaxSize.toInt()
                                    child.layoutParams = lp
                                    measureChildWithMargins(
                                        child,
                                        widthMeasureSpec,
                                        0,
                                        heightMeasureSpec,
                                        0
                                    )
                                }
                            }
                        }
                    }
                }

                maxWidth += child.measuredWidth + child.marginStart + child.marginEnd
                maxHeight += child.measuredHeight + child.marginTop + child.marginBottom
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


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val count = childCount

        // These are the far left and right edges in which we are performing layout.
        var leftPos = paddingLeft
        var rightPos = right - left - paddingRight

        // These are the top and bottom edges in which we are performing layout.
        var parentTop = paddingTop
        val parentBottom = bottom - top - paddingBottom
        when (orientation) {
            Orientation.HORIZONTAL -> {
                for (i in 0 until count) {
                    val child = getChildAt(i)
                    if (child.visibility != View.GONE) {
                        val childLp =
                            child.layoutParams as IvanKvychLayoutParams
                        val childWidth = child.measuredWidth
                        val childHeight = child.measuredHeight

                        // Compute the frame in which we are placing this child.
                        mTmpContainerRect.left = leftPos + childLp.leftMargin
                        mTmpContainerRect.right = leftPos + childWidth + childLp.rightMargin
                        leftPos = mTmpContainerRect.right

                        mTmpContainerRect.top = parentTop + childLp.topMargin
                        mTmpContainerRect.bottom = parentBottom - childLp.bottomMargin

                        // Use the child's gravity and size to determine its final
                        // frame within its container.
                        Gravity.apply(childLp.gravity, childWidth, childHeight, mTmpContainerRect, mTmpChildRect)

                        // Place the child.
                        child.layout(
                            mTmpChildRect.left, mTmpChildRect.top,
                            mTmpChildRect.right, mTmpChildRect.bottom
                        )
                    }
                }
            }
            Orientation.VERTICAL -> {
                for (i in 0 until count) {
                    val child = getChildAt(i)
                    if (child.visibility != View.GONE) {
                        val childLp =
                            child.layoutParams as IvanKvychLayoutParams
                        val childWidth = child.measuredWidth
                        val childHeight = child.measuredHeight

                        // Compute the frame in which we are placing this child.
                        mTmpContainerRect.left = leftPos + childLp.leftMargin
                        mTmpContainerRect.right = rightPos - childLp.rightMargin

                        mTmpContainerRect.top = parentTop + childLp.topMargin
                        mTmpContainerRect.bottom = parentTop + childHeight + childLp.bottomMargin
                        parentTop = mTmpContainerRect.bottom

                        // Use the child's gravity and size to determine its final
                        // frame within its container.
                        Gravity.apply(childLp.gravity, childWidth, childHeight, mTmpContainerRect, mTmpChildRect)

                        // Place the child.
                        child.layout(
                            mTmpChildRect.left, mTmpChildRect.top,
                            mTmpChildRect.right, mTmpChildRect.bottom
                        )
                    }
                }
            }
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): IvanKvychLayoutParams {
        return IvanKvychLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): IvanKvychLayoutParams {
        return IvanKvychLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): IvanKvychLayoutParams {
        return IvanKvychLayoutParams(p)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is IvanKvychLayoutParams
    }

    object Orientation {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    class IvanKvychLayoutParams : MarginLayoutParams {
        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
        constructor(c: Context, attrs: AttributeSet?) : super(
            c,
            attrs
        ) {
            init(c, attrs)
        }

        var layoutMaxSize: Float = .0F
        var gravity = Gravity.TOP or Gravity.START

        private fun init(context: Context, attrs: AttributeSet?) {

            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.IvanKvychLayout_Layout)
            try {
                layoutMaxSize =
                    typedArray.getDimension(
                        R.styleable.IvanKvychLayout_Layout_layout_max_size,
                        layoutMaxSize
                    )
                gravity = typedArray.getInt(
                    R.styleable.IvanKvychLayout_Layout_android_layout_gravity,
                    gravity
                )

            } finally {
                typedArray.recycle()
            }
        }
    }
}