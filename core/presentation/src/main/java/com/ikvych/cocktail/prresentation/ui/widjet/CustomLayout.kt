package com.ikvych.cocktail.prresentation.ui.widjet

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews.RemoteView
import com.ikvych.cocktail.prresentation.R

// Тестовий лейаут скачаний з доків, щоб подивитися як це все працює
/**
 * Example of writing a custom layout manager.  This is a fairly full-featured
 * layout manager that is relatively general, handling all layout cases.  You
 * can simplify it for more specific cases.
 */
@RemoteView
class CustomLayout : ViewGroup {
    /** The amount of space used by children in the left gutter.  */
    private var mLeftWidth = 0

    /** The amount of space used by children in the right gutter.  */
    private var mRightWidth = 0

    /** These are used for computing child frames based on their gravity.  */
    private val mTmpContainerRect: Rect = Rect()
    private val mTmpChildRect: Rect = Rect()

    constructor(context: Context?) : super(context) {}

    @JvmOverloads
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int = 0
    ) : super(context, attrs, defStyle) {
    }

    /**
     * Any layout manager that doesn't scroll will want this.
     */
    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    /**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val count = childCount

        // These keep track of the space we are using on the left and right for
        // views positioned there; we need member variables so we can also use
        // these for layout later.
        mLeftWidth = 0
        mRightWidth = 0

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

                // Update our size information based on the layout params.  Children
                // that asked to be positioned on the left or right go in those gutters.
                val lp =
                    child.layoutParams as LayoutParams
                if (lp.position == LayoutParams.POSITION_LEFT) {
                    mLeftWidth += Math.max(
                        maxWidth,
                        child.measuredWidth + lp.leftMargin + lp.rightMargin
                    )
                } else if (lp.position == LayoutParams.POSITION_RIGHT) {
                    mRightWidth += Math.max(
                        maxWidth,
                        child.measuredWidth + lp.leftMargin + lp.rightMargin
                    )
                } else {
                    maxWidth = Math.max(
                        maxWidth,
                        child.measuredWidth + lp.leftMargin + lp.rightMargin
                    )
                }
                maxHeight +=
                    child.measuredHeight + lp.topMargin + lp.bottomMargin

                childState =
                    View.combineMeasuredStates(childState, child.measuredState)
            }
        }

        // Total width is the maximum width of all inner children plus the gutters.
        maxWidth += mLeftWidth + mRightWidth

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, suggestedMinimumHeight)
        maxWidth = Math.max(maxWidth, suggestedMinimumWidth)

        // Report our final dimensions.
        setMeasuredDimension(
            View.resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
            View.resolveSizeAndState(
                maxHeight, heightMeasureSpec,
                childState shl View.MEASURED_HEIGHT_STATE_SHIFT
            )
        )
    }

    /**
     * Position all children within this layout.
     */
    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        val count = childCount

        // These are the far left and right edges in which we are performing layout.
        var leftPos = paddingLeft
        var rightPos = right - left - paddingRight

        // This is the middle region inside of the gutter.
        val middleLeft = leftPos + mLeftWidth
        val middleRight = rightPos - mRightWidth

        // These are the top and bottom edges in which we are performing layout.
        val parentTop = paddingTop
        val parentBottom = bottom - top - paddingBottom
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                val lp =
                    child.layoutParams as LayoutParams
                val width = child.measuredWidth
                val height = child.measuredHeight

                // Compute the frame in which we are placing this child.
                if (lp.position == LayoutParams.POSITION_LEFT) {
                    mTmpContainerRect.left = leftPos + lp.leftMargin
                    mTmpContainerRect.right = leftPos + width + lp.rightMargin
                    leftPos = mTmpContainerRect.right
                } else if (lp.position == LayoutParams.POSITION_RIGHT) {
                    mTmpContainerRect.right = rightPos - lp.rightMargin
                    mTmpContainerRect.left = rightPos - width - lp.leftMargin
                    rightPos = mTmpContainerRect.left
                } else {
                    mTmpContainerRect.left = middleLeft + lp.leftMargin
                    mTmpContainerRect.right = middleRight - lp.rightMargin
                }
                mTmpContainerRect.top = parentTop + lp.topMargin
                mTmpContainerRect.bottom = parentBottom - lp.bottomMargin

                // Use the child's gravity and size to determine its final
                // frame within its container.
                Gravity.apply(lp.gravity, width, height, mTmpContainerRect, mTmpChildRect)

                // Place the child.
                child.layout(
                    mTmpChildRect.left, mTmpChildRect.top,
                    mTmpChildRect.right, mTmpChildRect.bottom
                )
            }
        }
    }

    // ----------------------------------------------------------------------
    // The rest of the implementation is for custom per-child layout parameters.
    // If you do not need these (for example you are writing a layout manager
    // that does fixed positioning of its children), you can drop all of this.
    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    /**
     * Custom per-child layout information.
     */
    class LayoutParams : MarginLayoutParams {
        /**
         * The gravity to apply with the View to which these layout parameters
         * are associated.
         */
        var gravity = Gravity.TOP or Gravity.START
        var position = POSITION_MIDDLE

        constructor(c: Context, attrs: AttributeSet?) : super(
            c,
            attrs
        ) {

            // Pull the layout param values from the layout XML during
            // inflation.  This is not needed if you don't care about
            // changing the layout behavior in XML.
            val a = c.obtainStyledAttributes(attrs, R.styleable.CustomLayoutLP)
            gravity = a.getInt(R.styleable.CustomLayoutLP_android_layout_gravity, gravity)
            position = a.getInt(R.styleable.CustomLayoutLP_layout_position, position)
            a.recycle()
        }

        constructor(width: Int, height: Int) : super(width, height) {}
        constructor(source: ViewGroup.LayoutParams?) : super(source) {}

        companion object {
            var POSITION_MIDDLE = 0
            var POSITION_LEFT = 1
            var POSITION_RIGHT = 2
        }
    }
}