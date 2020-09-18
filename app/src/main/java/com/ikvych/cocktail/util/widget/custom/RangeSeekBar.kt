package com.ikvych.cocktail.util.widget.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.LinearInterpolator
import androidx.core.graphics.ColorUtils
import com.ikvych.cocktail.R
import com.ikvych.cocktail.util.widget.custom.RangeSeekBar.RangeSeekBar.HARD_MODE
import com.ikvych.cocktail.util.widget.custom.RangeSeekBar.RangeSeekBar.LEFT_INDICATOR
import com.ikvych.cocktail.util.widget.custom.RangeSeekBar.RangeSeekBar.LONG_PRESS_MILLIS
import com.ikvych.cocktail.util.widget.custom.RangeSeekBar.RangeSeekBar.NO_COLOR
import com.ikvych.cocktail.util.widget.custom.RangeSeekBar.RangeSeekBar.RIGHT_INDICATOR
import com.ikvych.cocktail.util.widget.custom.RangeSeekBar.RangeSeekBar.SOFT_MODE
import com.ikvych.cocktail.util.widget.custom.RangeSeekBar.RangeSeekBar.TAP_PRESS_MILLIS
import kotlin.math.*

class RangeSeekBar : View {
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

    var minValue = 0
    var maxValue = 50
    var isBackgroundRoundedCorners = true
    var backgroundHeight =
        context.resources.getDimension(R.dimen.range_seek_bar_background_height)
    var foregroundHeight =
        context.resources.getDimension(R.dimen.range_seek_bar_foreground_height)
    var indicatorActiveColor = context.resources.getColor(R.color.seek_bar_indicator_active)
    var indicatorDisableColor = context.resources.getColor(R.color.seek_bar_indicator_disable)
    var indicatorActiveSize =
        context.resources.getDimension(R.dimen.range_seek_bar_indicator_active)
    var indicatorDisableSize =
        context.resources.getDimension(R.dimen.range_seek_bar_indicator_disable)
    var valueTextSize = context.resources.getDimension(R.dimen.range_seek_bar_text_size)
    var seekBarMode = SOFT_MODE
    var isToggleThumbModeOnLongClickEnabled = true
        set(value) {
            field = value
            if (!value) {
                lIndicator.mode = seekBarMode
                rIndicator.mode = seekBarMode
            }
            invalidate()
        }

    var indicatorMinTouchZone = context.resources.getDimension(R.dimen.range_seek_bar_zone)

    //Кольори для налаштування backgorund шейдера
    var backgroundLeftColor: Int = Color.LTGRAY
    var backgroundRightColor: Int = Color.BLACK
    private var isApplyBackgroundShader = false

    //Кольори для налаштування foreground шейдера
    var foregroundLeftColor: Int = context.resources.getColor(R.color.seek_bar_foreground_left)
    var foregroundRightColor: Int = Color.LTGRAY
    private var isApplyForegroundShader = false

    //Кольори для налаштування active indicator шейдера
    var indicatorActiveTopColor: Int =
        context.resources.getColor(R.color.seek_bar_active_indicator_top)
    var indicatorActiveBottomColor: Int =
        context.resources.getColor(R.color.seek_bar_active_indicator_bottom)

    //Кольори для налаштування disable indicator шейдера
    var indicatorDisableTopColor: Int =
        context.resources.getColor(R.color.seek_bar_disable_indicator_top)
    var indicatorDisableBottomColor: Int =
        context.resources.getColor(R.color.seek_bar_disable_indicator_bottom)
    private var isApplyIndicatorShader = false

    private val rangeSeekBarRectF = RectF()
    private val rangeSeekBarPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = this@RangeSeekBar.valueTextSize
        textAlign = Paint.Align.CENTER
    }

    private val background = Background()
    private val foreground = Foreground()
    private val lIndicator = Indicator(type = LEFT_INDICATOR)
    private val rIndicator = Indicator(type = RIGHT_INDICATOR)
    private val divisionCount = maxValue - minValue
    private val divisions: Array<Float> = Array(divisionCount + 1) { 0.0F }
    private var divisionSegmentLength: Float = 0.0F

    private val listeners: MutableSet<OnIndicatorChanged> = mutableSetOf()

    private val rAnimatorListenerAdapter = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) = saveParameters(rIndicator)
        override fun onAnimationCancel(animation: Animator?) = saveParameters(rIndicator)
    }
    private val lAnimatorListenerAdapter = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) = saveParameters(lIndicator)
        override fun onAnimationCancel(animation: Animator?) = saveParameters(lIndicator)
    }
    private val lToActiveSizeIndicatorAnimator =
        ValueAnimator.ofFloat(0.0F, 1F).apply {
            interpolator = LinearInterpolator()
            addUpdateListener { toActiveSize(it.animatedValue as Float, lIndicator) }
            addListener(lAnimatorListenerAdapter)
        }
    private val rToActiveSizeIndicatorAnimator =
        ValueAnimator.ofFloat(0.0F, 1F).apply {
            interpolator = LinearInterpolator()
            addUpdateListener { toActiveSize(it.animatedValue as Float, rIndicator) }
            addListener(rAnimatorListenerAdapter)
        }
    private val lToDisableSizeIndicatorAnimator =
        ValueAnimator.ofFloat(1.0F, 0.0F).apply {
            interpolator = LinearInterpolator()
            addUpdateListener { toDisableSize(it.animatedValue as Float, lIndicator) }
            addListener(lAnimatorListenerAdapter)
        }
    private val rToDisableSizeIndicatorAnimator =
        ValueAnimator.ofFloat(1.0F, 0.0F).apply {
            interpolator = LinearInterpolator()
            addUpdateListener { toDisableSize(it.animatedValue as Float, rIndicator) }
            addListener(rAnimatorListenerAdapter)
        }

    private val lIndicatorMoveAnimator = ValueAnimator.ofFloat(0.0F, 1F).apply {
        interpolator = LinearInterpolator()
        duration = 200L
        addUpdateListener {
            moveIndicatorToNewPosition(it.animatedValue as Float, lIndicator)
        }
    }

    private val rIndicatorMoveAnimator = ValueAnimator.ofFloat(0.0F, 1F).apply {
        interpolator = LinearInterpolator()
        duration = 200L
        addUpdateListener {
            moveIndicatorToNewPosition(it.animatedValue as Float, rIndicator)
        }
    }

    private val vc: ViewConfiguration = ViewConfiguration.get(context)
    private val mSlop: Int = vc.scaledTouchSlop
    private val mDownEventsX = Array(2) { -1.0F }
    private val singleTapRunnableArray = Array(2) {
        Runnable {
            if (foreground.isPressed && !lIndicator.isPressed && !rIndicator.isPressed) {
                moveNearestIndicatorToNewPosition(foreground.onDownX)
                processIndicatorsResizing(foreground.pointerId)
            }
        }
    }

    private val longPressRunnableArray = Array(2) {
        Runnable {
            if (lIndicator.pointerId == it) {
                changeIndicatorMode(lIndicator)
            }
            if (rIndicator.pointerId == it) {
                changeIndicatorMode(rIndicator)
            }
        }
    }


    private fun init(attrs: AttributeSet?) {
        setWillNotDraw(false)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RangeSeekBar)
        try {
            minValue =
                abs(
                    typedArray.getInt(
                        R.styleable.RangeSeekBar_mrsb_min_value,
                        minValue
                    )
                )
            maxValue =
                abs(
                    typedArray.getInt(
                        R.styleable.RangeSeekBar_mrsb_max_value,
                        maxValue
                    )
                )
            backgroundHeight = abs(
                typedArray.getDimension(
                    R.styleable.RangeSeekBar_mrsb_background_height,
                    backgroundHeight
                )
            )
            isBackgroundRoundedCorners =
                typedArray.getBoolean(
                    R.styleable.RangeSeekBar_mrsb_background_is_rounded,
                    isBackgroundRoundedCorners
                )
            backgroundLeftColor =
                typedArray.getColor(
                    R.styleable.RangeSeekBar_mrsb_background_gradient_left,
                    backgroundLeftColor
                )
            backgroundRightColor =
                typedArray.getColor(
                    R.styleable.RangeSeekBar_mrsb_background_gradient_right,
                    backgroundRightColor
                )
            foregroundHeight =
                abs(
                    typedArray.getDimension(
                        R.styleable.RangeSeekBar_mrsb_foreground_height,
                        foregroundHeight
                    )
                )
            foregroundLeftColor =
                typedArray.getColor(
                    R.styleable.RangeSeekBar_mrsb_foreground_gradient_left,
                    foregroundLeftColor
                )
            foregroundRightColor =
                typedArray.getColor(
                    R.styleable.RangeSeekBar_mrsb_foreground_gradient_right,
                    foregroundRightColor
                )
            indicatorActiveColor =
                typedArray.getColor(
                    R.styleable.RangeSeekBar_mrsb_thumb_color_active,
                    indicatorActiveColor
                )
            indicatorActiveTopColor =
                typedArray.getColor(
                    R.styleable.RangeSeekBar_mrsb_thumb_color_gradient_top_active,
                    indicatorActiveTopColor
                )
            indicatorActiveBottomColor =
                typedArray.getColor(
                    R.styleable.RangeSeekBar_mrsb_thumb_color_gradient_bottom_active,
                    indicatorActiveBottomColor
                )
            indicatorDisableTopColor =
                typedArray.getColor(
                    R.styleable.RangeSeekBar_mrsb_thumb_color_gradient_top_disable,
                    indicatorDisableTopColor
                )
            indicatorDisableBottomColor =
                typedArray.getColor(
                    R.styleable.RangeSeekBar_mrsb_thumb_color_gradient_bottom_disable,
                    indicatorDisableBottomColor
                )
            indicatorDisableColor =
                typedArray.getColor(
                    R.styleable.RangeSeekBar_mrsb_thumb_color_disable,
                    indicatorDisableColor
                )
            indicatorActiveSize =
                abs(
                    typedArray.getDimension(
                        R.styleable.RangeSeekBar_mrsb_thumb_size_active,
                        indicatorActiveSize
                    )
                )
            indicatorDisableSize =
                abs(
                    typedArray.getDimension(
                        R.styleable.RangeSeekBar_mrsb_thumb_size_disable,
                        indicatorDisableSize
                    )
                )
            valueTextSize =
                    typedArray.getDimension(
                        R.styleable.RangeSeekBar_mrsb_value_text_size,
                        valueTextSize
                    )


            isApplyBackgroundShader =
                checkIsAppliedShader(backgroundLeftColor, backgroundRightColor)
            isApplyForegroundShader =
                checkIsAppliedShader(foregroundLeftColor, foregroundRightColor)
            isApplyIndicatorShader =
                indicatorDisableTopColor != NO_COLOR &&
                        indicatorDisableBottomColor != NO_COLOR &&
                        indicatorActiveTopColor != NO_COLOR &&
                        indicatorActiveBottomColor != NO_COLOR
        } finally {
            typedArray.recycle()
        }
    }


    private fun checkIsAppliedShader(firstColor: Int, secondColor: Int): Boolean {
        return firstColor != NO_COLOR && secondColor != NO_COLOR
    }

    private fun processIndicatorsResizing(pointerId: Int) {
        when {
            lIndicator.isPressed && lIndicator.pointerId == pointerId -> {
                lToActiveSizeIndicatorAnimator.cancel()
                lToDisableSizeIndicatorAnimator.start()
                lIndicator.isPressed = false
                if (foreground.isPressed) {
                    processIndicatorsResizing(foreground.pointerId)
                } else {
                    moveIndicatorToNearestDivision(lIndicator)
                    moveIndicatorToNearestDivision(rIndicator)
                }
            }
            rIndicator.isPressed && rIndicator.pointerId == pointerId -> {
                rToActiveSizeIndicatorAnimator.cancel()
                rToDisableSizeIndicatorAnimator.start()
                rIndicator.isPressed = false
                if (foreground.isPressed) {
                    processIndicatorsResizing(foreground.pointerId)
                } else {
                    moveIndicatorToNearestDivision(lIndicator)
                    moveIndicatorToNearestDivision(rIndicator)
                }
            }
            foreground.isPressed && foreground.pointerId == pointerId -> {
                foreground.isPressed = false
                foreground.resetStartDragsCoordinates()
                if (rIndicator.isPressed) {
                    processIndicatorsResizing(rIndicator.pointerId)
                }
                if (lIndicator.isPressed) {
                    processIndicatorsResizing(lIndicator.pointerId)
                }
                if (!lIndicatorMoveAnimator.isRunning && !rIndicatorMoveAnimator.isRunning) {
                    moveIndicatorToNearestDivision(lIndicator)
                    moveIndicatorToNearestDivision(rIndicator)
                }
            }
            background.isPressed && background.pointerId == pointerId -> {
                background.isPressed = false
            }
        }
    }


    private fun changeIndicatorMode(indicator: Indicator) {
        if (isToggleThumbModeOnLongClickEnabled) {
            if (indicator.isPressed) {
                indicator.mode = if (indicator.mode == HARD_MODE) {
                    SOFT_MODE
                } else HARD_MODE
                invalidate()
            }
        }
    }

    private fun onActionDown(pointerId: Int, x: Float, y: Float): Boolean {
        if (pointerId <= 1) {
            determinateIsPressedAnyIndicator(x, y, pointerId)
            determinateIsForegroundPressed(x, y, pointerId)
            determinateIsBackgroundPressed(x, y, pointerId)
            if (canScrollIndicators()) {
                mDownEventsX[pointerId] = x
                postDelayed(longPressRunnableArray[pointerId], LONG_PRESS_MILLIS)
            }
            if (isBackgroundPressed()) {
                moveNearestIndicatorToNewPosition(x)
            }
            if (canDragForeground()) {
                mDownEventsX[pointerId] = x
                postDelayed(singleTapRunnableArray[pointerId], TAP_PRESS_MILLIS)
            }
            lIndicator.xOnStartMove = lIndicator.x
            rIndicator.xOnStartMove = rIndicator.x
        }
        return true
    }

    private fun calculateScrollDelta(event: MotionEvent, pointerId: Int) =
        abs(event.getX(event.findPointerIndex(pointerId)) - mDownEventsX[pointerId])

    private fun canScrollIndicators() =
        (lIndicator.isPressed || rIndicator.isPressed) && !foreground.isPressed

    private fun canScrollIndicator(indicator: Indicator) =
        indicator.isPressed

    private fun canDragForeground() =
        foreground.isPressed && !lIndicator.isPressed && !rIndicator.isPressed

    private fun canResizeForeground() =
        foreground.isPressed && (lIndicator.isPressed || rIndicator.isPressed)

    private fun isBackgroundPressed() =
        background.isPressed && !foreground.isPressed && !lIndicator.isPressed && !rIndicator.isPressed

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                return onActionDown(
                    event.getPointerId(event.actionIndex),
                    event.getX(event.actionIndex),
                    event.getY(event.actionIndex)
                )
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                return onActionDown(
                    event.getPointerId(event.actionIndex),
                    event.getX(event.actionIndex),
                    event.getY(event.actionIndex)
                )
            }
            MotionEvent.ACTION_MOVE -> {
                when {
                    canScrollIndicators() -> {
                        if (canScrollIndicator(lIndicator)) {
                            if (calculateScrollDelta(event, lIndicator.pointerId) >= mSlop) {
                                removeCallbacks(longPressRunnableArray[lIndicator.pointerId])
                            }
                            dragLeftIndicator(event.getX(event.findPointerIndex(lIndicator.pointerId)))
                        }
                        if (canScrollIndicator(rIndicator)) {
                            if (calculateScrollDelta(event, rIndicator.pointerId) >= mSlop) {
                                removeCallbacks(longPressRunnableArray[rIndicator.pointerId])
                            }
                            dragRightIndicator(event.getX(event.findPointerIndex(rIndicator.pointerId)))
                        }
                    }
                    canDragForeground() -> {
                        if (calculateScrollDelta(event, foreground.pointerId) >= mSlop) {
                            removeCallbacks(singleTapRunnableArray[foreground.pointerId])
                        }
                        foreground.updateCachedCoordinates()
                        dragForegroundRectangle(
                            foreground.onDownX,
                            event.getX(event.findPointerIndex(foreground.pointerId))
                        )
                    }
                    canResizeForeground() -> {
                        foreground.updateCachedCoordinates()
                        dragForegroundRectangle(
                            foreground.onDownX,
                            event.getX(event.findPointerIndex(foreground.pointerId)),
                            true
                        )
                        if (canScrollIndicator(rIndicator)) {
                            if (foreground.pointerId < rIndicator.pointerId) {
                                val newXForLIndicator =
                                    foreground.left - (event.getX(event.findPointerIndex(rIndicator.pointerId)) - foreground.right)
                                dragLeftIndicator(newXForLIndicator)
                                dragRightIndicator(event.getX(event.findPointerIndex(rIndicator.pointerId)))
                            }
                        } else {
                            if (foreground.pointerId < lIndicator.pointerId) {
                                val newXForRIndicator =
                                    foreground.right + (foreground.left - event.getX(
                                        event.findPointerIndex(
                                            lIndicator.pointerId
                                        )
                                    ))
                                dragRightIndicator(newXForRIndicator)
                                dragLeftIndicator(event.getX(event.findPointerIndex(lIndicator.pointerId)))
                            }
                        }
                    }
                    isBackgroundPressed() -> {
                        if (calculateScrollDelta(event, background.pointerId) >= mSlop) {
                            val newX = event.getX(event.findPointerIndex(background.pointerId))
                            if (newX <= lIndicator.touchZoneRectF.right) {
                                if (!lIndicator.touchZoneRectF.contains(newX, background.axisY)) {
                                    if (!lIndicatorMoveAnimator.isRunning) {
                                        moveNearestIndicatorToNewPosition(newX)
                                    }
                                    continueLeftIndicatorToCoordinate(newX)
                                } else {
                                    lIndicatorMoveAnimator.cancel()
                                    lIndicator.isPressed = true
                                    toProcessLeftIndicatorResizing(background.pointerId)
                                    dragLeftIndicator(newX)
                                }
                            }
                            if (newX >= rIndicator.touchZoneRectF.left) {
                                if (!rIndicator.touchZoneRectF.contains(newX, background.axisY)) {
                                    if (!rIndicatorMoveAnimator.isRunning) {
                                        moveNearestIndicatorToNewPosition(newX)
                                    }
                                    continueRightIndicatorToCoordinate(newX)
                                } else {
                                    rIndicatorMoveAnimator.cancel()
                                    rIndicator.isPressed = true
                                    toProcessRightIndicatorResizing(background.pointerId)
                                    dragRightIndicator(newX)
                                }
                            }
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE, MotionEvent.ACTION_POINTER_UP -> {
                if (event.getPointerId(event.actionIndex) > 1) {
                    return true
                } else {
                    removeCallbacks(longPressRunnableArray[event.getPointerId(event.actionIndex)])
                    removeCallbacks(singleTapRunnableArray[event.getPointerId(event.actionIndex)])
                    processIndicatorsResizing(event.getPointerId(event.actionIndex))
                }
            }
        }
        performClick()
        return true
    }

    private fun moveNearestIndicatorToNewPosition(x: Float) {
        if (lIndicator.x > rIndicator.x) {
            lIndicator.x = rIndicator.x
        }
        val distanceToLeftIndicator = abs(x - lIndicator.x)
        val distanceToRightIndicator = abs(x - rIndicator.x)

        if (distanceToLeftIndicator == distanceToRightIndicator) {
            if (canLeftIndicatorMove() && canRightIndicatorMove()) {
                if (x < lIndicator.x) {
                    moveIndicatorToNearestDivision(lIndicator, x)
                } else if (x > rIndicator.x) {
                    moveIndicatorToNearestDivision(rIndicator, x)
                }
            } else if (canRightIndicatorMove()) {
                moveIndicatorToNearestDivision(rIndicator, x)
            } else {
                moveIndicatorToNearestDivision(lIndicator, x)
            }
        } else {
            if (distanceToLeftIndicator > distanceToRightIndicator &&
                canRightIndicatorMove()
            ) {
                moveIndicatorToNearestDivision(rIndicator, x)
            } else if (distanceToRightIndicator > distanceToLeftIndicator
                && canLeftIndicatorMove()
            ) {
                moveIndicatorToNearestDivision(lIndicator, x)
            }
        }
    }

    private fun determinateIsForegroundPressed(x: Float, y: Float, ownerId: Int) {
        if (!foreground.isPressed) {
            foreground.isPressed = foreground.contains(x, y) &&
                    !lIndicator.touchZoneRectF.contains(x, y) &&
                    !rIndicator.touchZoneRectF.contains(x, y)
            foreground.pointerId = ownerId
            foreground.onDownX = x
        }
    }

    private fun determinateIsBackgroundPressed(x: Float, y: Float, ownerId: Int) {
        if (!background.isPressed) {
            background.isPressed = background.contains(x, y) &&
                    !foreground.contains(x, y) &&
                    !lIndicator.touchZoneRectF.contains(x, y) &&
                    !rIndicator.touchZoneRectF.contains(x, y)
            background.pointerId = ownerId
            background.onDownX = x
        }
    }

    private fun determinateIsPressedAnyIndicator(x: Float, y: Float, ownerId: Int) {
        val leftIndicatorSelected = if (!lIndicator.isPressed) {
            lIndicator.touchZoneRectF.contains(x, y)
        } else false
        val rightIndicatorSelected = if (!rIndicator.isPressed) {
            rIndicator.touchZoneRectF.contains(x, y)
        } else false

        if (leftIndicatorSelected && rightIndicatorSelected) {
            val canLeftIndicatorMove = canLeftIndicatorMove()
            val canRightIndicatorMove = canRightIndicatorMove()
            if (canLeftIndicatorMove && canRightIndicatorMove) {
                val leftDifference = abs(x - lIndicator.x)
                val rightDifference = abs(x - rIndicator.x)
                if (leftDifference > rightDifference) {
                    toProcessRightIndicatorResizing(ownerId)
                } else if (leftDifference < rightDifference) {
                    toProcessLeftIndicatorResizing(ownerId)
                } else {
                    if (x <= lIndicator.x) {
                        toProcessLeftIndicatorResizing(ownerId)
                    } else {
                        toProcessRightIndicatorResizing(ownerId)
                    }
                }
            } else if (canLeftIndicatorMove) {
                toProcessLeftIndicatorResizing(ownerId)
            } else {
                toProcessRightIndicatorResizing(ownerId)
            }
            invalidate()
        } else if (rightIndicatorSelected) {
            toProcessRightIndicatorResizing(ownerId)
            invalidate()
        } else if (leftIndicatorSelected) {
            toProcessLeftIndicatorResizing(ownerId)
            invalidate()
        }
    }

    private fun toProcessLeftIndicatorResizing(ownerId: Int) {
        lIndicator.isPressed = true
        lIndicator.pointerId = ownerId
        if (lToDisableSizeIndicatorAnimator.isRunning) {
            lToDisableSizeIndicatorAnimator.cancel()
        }
        lToActiveSizeIndicatorAnimator.start()
    }

    private fun toProcessRightIndicatorResizing(ownerId: Int) {
        rIndicator.isPressed = true
        rIndicator.pointerId = ownerId
        if (rToDisableSizeIndicatorAnimator.isRunning) {
            rToDisableSizeIndicatorAnimator.cancel()
        }
        rToActiveSizeIndicatorAnimator.start()
    }

    private fun canLeftIndicatorMove(): Boolean {
        return if (lIndicator.mode == SOFT_MODE && rIndicator.mode == SOFT_MODE) {
            true
        } else {
            !(lIndicator.x == divisions[0] &&
                    rIndicator.x == divisions[0] &&
                    rIndicator.mode == HARD_MODE)
        }
    }

    private fun canRightIndicatorMove(): Boolean {
        return if (rIndicator.mode == SOFT_MODE && lIndicator.mode == SOFT_MODE) {
            true
        } else {
            !(lIndicator.x == divisions[divisions.size - 1] &&
                    rIndicator.x == divisions[divisions.size - 1] &&
                    lIndicator.mode == HARD_MODE)
        }
    }

    private fun configureRoundCornersBackground() {
        val backgroundPadding = getRoundBackgroundPadding()
        background.left =
            rangeSeekBarRectF.left + backgroundPadding - backgroundHeight / 2
        background.top = background.axisY - (backgroundHeight / 2)
        background.bottom = background.axisY + (backgroundHeight / 2)
        background.right = rangeSeekBarRectF.right - backgroundPadding + backgroundHeight / 2
        background.leftX = rangeSeekBarRectF.left + backgroundPadding
        background.rightX = rangeSeekBarRectF.right - backgroundPadding
        background.rangeLength = background.rightX - background.leftX
        background.path.reset()
        background.path.addRoundRect(
            background.rectF,
            backgroundHeight / 2,
            backgroundHeight / 2,
            Path.Direction.CW
        )
    }

    private fun getRoundBackgroundPadding(): Float {
        return if (indicatorActiveSize / 2 >= indicatorDisableSize / 2 &&
            indicatorActiveSize / 2 >= backgroundHeight / 2
        ) {
            indicatorActiveSize / 2
        } else {
            if (indicatorDisableSize / 2 >= backgroundHeight / 2 &&
                indicatorDisableSize / 2 >= indicatorActiveSize / 2
            ) {
                indicatorDisableSize / 2
            } else {
                backgroundHeight / 2
            }
        }
    }

    private fun configureDefaultBackground() {
        val backgroundPadding = getDefaultBackgroundPadding()
        background.left = rangeSeekBarRectF.left + backgroundPadding
        background.top = background.axisY - (backgroundHeight / 2)
        background.bottom = background.axisY + (backgroundHeight / 2)
        background.right = rangeSeekBarRectF.right - backgroundPadding
        background.rangeLength = background.right - background.left
        background.leftX = background.left
        background.rightX = background.right
        background.path.reset()
        background.path.addRect(background.rectF, Path.Direction.CW)
    }

    private fun getDefaultBackgroundPadding(): Float {
        return if (indicatorActiveSize / 2 >= indicatorDisableSize / 2) {
            indicatorActiveSize / 2
        } else {
            indicatorDisableSize / 2
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var newWidth = if (measuredWidth < indicatorMinTouchZone.roundToInt()) {
            indicatorMinTouchZone.roundToInt()
        } else measuredWidth
        var newHeight = if (measuredHeight < indicatorMinTouchZone.roundToInt()) {
            indicatorMinTouchZone.roundToInt()
        } else measuredHeight
/*        setMeasuredDimension(
            newWidth,
            newHeight
        )*/

        //Clickable zone
        rangeSeekBarRectF.left = paddingLeft.toFloat()
        rangeSeekBarRectF.top = paddingTop.toFloat()
        rangeSeekBarRectF.right = measuredWidth - paddingRight.toFloat()
        rangeSeekBarRectF.bottom = measuredHeight - paddingBottom.toFloat()

        //Main Y axis
        background.axisY =
            (rangeSeekBarRectF.bottom - rangeSeekBarRectF.top) / 2 + rangeSeekBarRectF.top

        //Background Shape
        if (isBackgroundRoundedCorners) {
            configureRoundCornersBackground()
        } else {
            configureDefaultBackground()
        }

        divisionSegmentLength = background.rangeLength / (divisionCount)
        divisions.forEachIndexed { index, _ ->
            divisions[index] = index * divisionSegmentLength + background.leftX
        }

        //left indicator coordinate
        lIndicator.y = background.axisY
        lIndicator.x = background.leftX
        //right indicator coordinate
        rIndicator.y = background.axisY
        rIndicator.x = background.rightX

        //Foreground
        foreground.left = lIndicator.x
        foreground.right = rIndicator.x
        foreground.top = background.axisY - (foregroundHeight / 2)
        foreground.bottom = background.axisY + (foregroundHeight / 2)

        //Left indicator zone
        lIndicator.touchZoneRectF.left = lIndicator.x - indicatorMinTouchZone / 2
        lIndicator.touchZoneRectF.right = lIndicator.x + indicatorMinTouchZone / 2
        lIndicator.touchZoneRectF.top = lIndicator.y - indicatorMinTouchZone / 2
        lIndicator.touchZoneRectF.bottom = lIndicator.y + indicatorMinTouchZone / 2

        //Right indicator zone
        rIndicator.touchZoneRectF.left = rIndicator.x - indicatorMinTouchZone / 2
        rIndicator.touchZoneRectF.right = rIndicator.x + indicatorMinTouchZone / 2
        rIndicator.touchZoneRectF.top = rIndicator.y - indicatorMinTouchZone / 2
        rIndicator.touchZoneRectF.bottom = rIndicator.y + indicatorMinTouchZone / 2

        applyShader()
    }

    private fun applyShader() {
        if (isApplyBackgroundShader) {
            background.paint.shader = LinearGradient(
                background.leftX,
                0.0F,
                background.rightX,
                0.0F,
                backgroundLeftColor,
                backgroundRightColor,
                Shader.TileMode.MIRROR
            )
        }
        if (isApplyForegroundShader) {
            foreground.paint.shader = LinearGradient(
                foreground.left,
                0.0F,
                foreground.right,
                0.0F,
                foregroundLeftColor,
                foregroundRightColor,
                Shader.TileMode.CLAMP
            )
        }
        if (isApplyIndicatorShader) {
            lIndicator.paint.shader = LinearGradient(
                0.0F,
                background.axisY - indicatorDisableSize / 2,
                0.0F,
                background.axisY + indicatorDisableSize / 2,
                indicatorDisableTopColor,
                indicatorDisableBottomColor,
                Shader.TileMode.CLAMP
            )
            rIndicator.paint.shader = LinearGradient(
                0.0F,
                background.axisY - indicatorDisableSize / 2,
                0.0F,
                background.axisY + indicatorDisableSize / 2,
                indicatorDisableTopColor,
                indicatorDisableBottomColor,
                Shader.TileMode.CLAMP
            )
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
/*        canvas.drawRect(barContainer, barContainerPaint)*/
        canvas.drawPath(background.path, background.paint)
        canvas.drawRect(foreground.rectF, foreground.paint)
        canvas.drawCircle(
            lIndicator.x,
            lIndicator.y,
            lIndicator.currentSize / 2,
            lIndicator.paint
        )
        canvas.drawCircle(
            rIndicator.x,
            rIndicator.y,
            rIndicator.currentSize / 2,
            rIndicator.paint
        )
/*        canvas.drawRect(lIndicator.touchZoneRectF, rangeSeekBarPaint)
        canvas.drawRect(rIndicator.touchZoneRectF, rangeSeekBarPaint)*/
/*        divisions.forEachIndexed { index, fl ->
            canvas.drawLine(fl, 0.0F, fl, background.axisY, Paint().apply {
                color = Color.BLACK
                style = Paint.Style.STROKE
                strokeWidth = 5F
            })
        }*/
        if (isToggleThumbModeOnLongClickEnabled) {
            if (lIndicator.mode == HARD_MODE) {
                lIndicator.updateBoundsForLock()
                lIndicator.lockDrawable.draw(canvas)
            }
            if (rIndicator.mode == HARD_MODE) {
                rIndicator.updateBoundsForLock()
                rIndicator.lockDrawable.draw(canvas)
            }
        }
        canvas.drawText(
            lIndicator.currentDivision.toString(),
            background.leftX,
            background.axisY - (indicatorActiveSize / 2),
            textPaint
        )
        canvas.drawText(
            rIndicator.currentDivision.toString(),
            background.rightX,
            background.axisY - (indicatorActiveSize / 2),
            textPaint
        )
    }

    private fun dragForegroundRectangle(startX: Float, x: Float, ignoreBorder: Boolean = false) {
        val currentBias = x - startX
        val newLeft = foreground.leftXOnDragStarts + currentBias
        val newRight = foreground.rightXOnDragStarts + currentBias
        if (ignoreBorder) {
            foreground.left = newLeft
            foreground.right = newRight
        } else {
            if (newLeft >= background.leftX && newRight <= background.rightX) {
                foreground.left = newLeft
                foreground.right = newRight
            } else {
                if (newLeft >= background.leftX) {
                    foreground.left = background.rightX - foreground.getWidth()
                    foreground.right = background.rightX
                } else {
                    foreground.right = background.leftX + foreground.getWidth()
                    foreground.left = background.leftX
                }
            }
            lIndicator.x = foreground.left
            rIndicator.x = foreground.right
            invalidate()
        }
    }

    private fun dragLeftIndicator(newX: Float, ignoreSecondIndicator: Boolean = false): Float {
        var currentX = newX
        if (newX.compareTo(background.leftX) >= 0 && newX.compareTo(rIndicator.x) <= 0) {
            lIndicator.x = newX
        } else {
            // Потрібно для дотягування індикатора до його позиції, оскільки onScroll скіпає проміжні значення
            if (newX.compareTo(rIndicator.x) > 0) {
                if (ignoreSecondIndicator) {
                    lIndicator.x = newX
                } else {
                    if (rIndicator.mode == HARD_MODE) {
                        lIndicator.x = rIndicator.x
                        currentX = lIndicator.x
                    } else {
                        currentX = dragRightIndicator(newX)
                        lIndicator.x = currentX
                    }
                }
            } else {
                lIndicator.x = background.leftX
                currentX = lIndicator.x
            }
        }
        resizeForeground()
        invalidate()
        return currentX
    }

    private fun dragRightIndicator(newX: Float, ignoreSecondIndicator: Boolean = false): Float {
        var currentX = newX
        if (newX.compareTo(lIndicator.x) >= 0 && newX.compareTo(background.rightX) <= 0) {
            rIndicator.x = newX
        } else {
            // Потрібно для дотягування індикатора до його позиції оскільки onScroll, скіпає проміжні значення
            if (newX.compareTo(lIndicator.x) < 0) {
                if (ignoreSecondIndicator) {
                    rIndicator.x = newX
                } else {
                    if (lIndicator.mode == HARD_MODE) {
                        rIndicator.x = lIndicator.x
                        currentX = rIndicator.x
                    } else {
                        currentX = dragLeftIndicator(newX)
                        rIndicator.x = currentX
                    }
                }
            } else {
                rIndicator.x = background.rightX
                currentX = rIndicator.x
            }
        }
        resizeForeground()
        invalidate()
        return currentX
    }

    private fun resizeForeground() {
        foreground.left = lIndicator.x
        foreground.right = rIndicator.x
    }

    private fun getNearestDivisionToX(x: Float): Int {
        var nearestDivision = -1
        val destinationXOnBackgroundScale = abs(x - background.leftX)
        val lessDivisionIndex =
            max(floor(destinationXOnBackgroundScale / abs(divisionSegmentLength)), 0.0F)
        val biggestDivisionIndex = min(
            ceil(destinationXOnBackgroundScale / abs(divisionSegmentLength)),
            (divisions.size - 1).toFloat()
        )
        val lessX = divisions[lessDivisionIndex.toInt()]
        val biggestX = divisions[biggestDivisionIndex.toInt()]
        nearestDivision = if (x - lessX > biggestX - x) {
            biggestDivisionIndex.toInt()
        } else {
            lessDivisionIndex.toInt()
        }
        return nearestDivision
    }

    private fun moveIndicatorToNearestDivision(
        indicator: Indicator,
        destinationX: Float = indicator.x
    ) {
        val nearestDivision = getNearestDivisionToX(destinationX)
        indicator.deltaMove = indicator.x - divisions[nearestDivision]
        indicator.previousFractionDeltaMove = 0.0F

        if (indicator.type == LEFT_INDICATOR) {
            lIndicatorMoveAnimator.start()
        } else rIndicatorMoveAnimator.start()
    }

    private fun continueLeftIndicatorToCoordinate(x: Float) {
        lIndicator.deltaMove = abs(divisions[getNearestDivisionToX(x)] - lIndicator.xOnStartMove)
    }

    private fun continueRightIndicatorToCoordinate(x: Float) {
        rIndicator.deltaMove = rIndicator.xOnStartMove - divisions[getNearestDivisionToX(x)]
    }

    private fun moveIndicatorToNewPosition(fraction: Float, indicator: Indicator) {
        val newFractionDeltaMove = fraction * indicator.deltaMove
        val newX = indicator.x - (newFractionDeltaMove - indicator.previousFractionDeltaMove)
        if (indicator.type == LEFT_INDICATOR) {
            dragLeftIndicator(newX, true)
        } else {
            dragRightIndicator(newX, true)
        }
        indicator.previousFractionDeltaMove = newFractionDeltaMove
    }

    private fun toActiveSize(fraction: Float, indicator: Indicator) {
        val difference = indicatorActiveSize - indicator.stateChangeSize
        indicator.currentSize = indicator.stateChangeSize + (difference * fraction)

        if (isApplyIndicatorShader) {
            indicator.currentTopColor = ColorUtils.blendARGB(
                indicator.stateChangeTopColor,
                indicatorActiveTopColor,
                fraction
            )
            indicator.currentBottomColor = ColorUtils.blendARGB(
                indicator.stateChangeBottomColor,
                indicatorActiveBottomColor,
                fraction
            )
            val shader = LinearGradient(
                0.0F,
                background.axisY - indicator.currentSize / 2,
                0.0F,
                background.axisY + indicator.currentSize / 2,
                indicator.currentTopColor,
                indicator.currentBottomColor,
                Shader.TileMode.CLAMP
            )
            indicator.paint.shader = shader
        } else {
            indicator.currentColor = ColorUtils.blendARGB(
                indicator.stateChangeColor,
                indicatorActiveColor,
                fraction
            )
            indicator.paint.color = indicator.currentColor
        }
        invalidate()
    }

    private fun toDisableSize(fraction: Float, indicator: Indicator) {
        val difference = indicator.stateChangeSize - indicatorDisableSize
        indicator.currentSize = indicatorDisableSize + (difference * fraction)

        if (isApplyIndicatorShader) {
            indicator.currentTopColor = ColorUtils.blendARGB(
                indicatorDisableTopColor,
                indicator.stateChangeTopColor,
                fraction
            )
            indicator.currentBottomColor = ColorUtils.blendARGB(
                indicatorDisableBottomColor,
                indicator.stateChangeBottomColor,
                fraction
            )
            val shader = LinearGradient(
                0.0F,
                background.axisY - indicator.currentSize / 2,
                0.0F,
                background.axisY + indicator.currentSize / 2,
                indicator.currentTopColor,
                indicator.currentBottomColor,
                Shader.TileMode.CLAMP
            )
            indicator.paint.shader = shader
        } else {
            indicator.currentColor = ColorUtils.blendARGB(
                indicatorDisableColor,
                indicator.stateChangeColor,
                fraction
            )
            indicator.paint.color = indicator.currentColor
        }
        invalidate()
    }

    private fun saveParameters(indicator: Indicator) {
        indicator.stateChangeSize = indicator.currentSize
        indicator.stateChangeBottomColor = indicator.currentBottomColor
        indicator.stateChangeTopColor = indicator.currentTopColor
        indicator.stateChangeColor = indicator.currentColor
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    interface OnIndicatorChanged {
        fun onLeftIndicatorChanged(value: Int)
        fun onRightIndicatorChanged(value: Int)
    }

    fun setOnIndicatorChangedListener(listener: OnIndicatorChanged) {
        listeners.add(listener)
    }

    fun removeOnIndicatorChangedListener(listener: OnIndicatorChanged) {
        listeners.remove(listener)
    }

    fun setRange(start: Int, end: Int, animate: Boolean): Boolean {
        if (start < end && start >= minValue && end <= maxValue) {
            if (animate) {
                moveIndicatorToNearestDivision(lIndicator, divisions[start])
                moveIndicatorToNearestDivision(rIndicator, divisions[end])
            } else {
                lIndicator.x = divisions[start]
                rIndicator.x = divisions[end]
                foreground.left = divisions[start]
                foreground.right = divisions[end]
                invalidate()
            }
            return true
        } else {
            return false
        }
    }

    override fun onDetachedFromWindow() {
        listeners.clear()
        super.onDetachedFromWindow()
    }

    private inner class Indicator(
        val touchZoneRectF: RectF = RectF(),
        var currentSize: Float = indicatorDisableSize,
        var paint: Paint = Paint(),
        var isPressed: Boolean = false,
        var deltaMove: Float = -1.0F,
        var previousFractionDeltaMove: Float = 0.0F,
        val type: Int,
        var mode: Int = seekBarMode,
        var lockDrawable: Drawable = context.resources.getDrawable(R.drawable.ic_idnicator_locked),
        var pointerId: Int = -1
    ) {

        var stateChangeSize = indicatorDisableSize
        var stateChangeBottomColor = indicatorDisableBottomColor
        var stateChangeTopColor = indicatorDisableBottomColor
        var stateChangeColor = indicatorDisableColor

        var currentBottomColor = 0
        var currentTopColor = 0
        var currentColor = 0

        var xOnStartMove = -1.0F

        var currentDivision = 0

        var x = 0.0F
            set(value) {
                field = value
                configureTouchZone()
                onDivisionChange()
            }
        var y = 0.0F

        fun onDivisionChange() {
            val newDivision = getNearestDivisionToX(x)
            if (newDivision != currentDivision) {
                if (type == LEFT_INDICATOR) {
                    listeners.forEach {
                        it.onLeftIndicatorChanged(newDivision)
                    }
                } else {
                    listeners.forEach {
                        it.onRightIndicatorChanged(newDivision)
                    }
                }
                currentDivision = newDivision
            }
        }

        init {
            paint.color = indicatorDisableColor
            paint.style = Paint.Style.FILL
        }

        fun configureTouchZone() {
            touchZoneRectF.left = x - indicatorMinTouchZone / 2
            touchZoneRectF.right = x + indicatorMinTouchZone / 2
        }

        fun updateBoundsForLock() {
            lockDrawable.setBounds(
                (x - currentSize / 2).roundToInt(),
                (y - currentSize / 2).roundToInt(),
                (x + currentSize / 2).roundToInt(),
                (y + currentSize / 2).roundToInt()
            )
        }
    }

    private inner class Background(
        val rectF: RectF = RectF(),
        var rangeLength: Float = 0.0F,
        val paint: Paint = Paint(),
        var axisY: Float = 0.0F,
        val path: Path = Path(),
        var leftX: Float = 0.0F,
        var rightX: Float = 0.0F,
        var isPressed: Boolean = false,
        var onDownX: Float = -1.0F,
        var pointerId: Int = -1
    ) {
        var left: Float = 0.0F
            set(value) {
                field = value
                rectF.left = value
            }
        var top: Float = 0.0F
            set(value) {
                field = value
                rectF.top = value
            }
        var right: Float = 0.0F
            set(value) {
                field = value
                rectF.right = value
            }
        var bottom: Float = 0.0F
            set(value) {
                field = value
                rectF.bottom = value
            }

        init {
            paint.color = Color.RED
            paint.style = Paint.Style.FILL
        }

        fun contains(x: Float, y: Float): Boolean {
            return rectF.contains(x, y)
        }
    }

    private inner class Foreground(
        val rectF: RectF = RectF(),
        val paint: Paint = Paint(),
        var isPressed: Boolean = false,
        var leftXOnDragStarts: Float = -1F,
        var rightXOnDragStarts: Float = -1F,
        var pointerId: Int = -1,
        var onDownX: Float = -1.0F
    ) {
        var left: Float = 0.0F
            set(value) {
                field = value
                rectF.left = value
            }
        var top: Float = 0.0F
            set(value) {
                field = value
                rectF.top = value
            }
        var right: Float = 0.0F
            set(value) {
                field = value
                rectF.right = value
            }
        var bottom: Float = 0.0F
            set(value) {
                field = value
                rectF.bottom = value
            }

        init {
            paint.color = Color.BLUE
            paint.style = Paint.Style.FILL
        }

        fun getWidth(): Float = right - left
        fun contains(x: Float, y: Float) = rectF.contains(x, y)
        fun resetStartDragsCoordinates() {
            leftXOnDragStarts = -1F
            rightXOnDragStarts = -1F
        }

        fun updateCachedCoordinates() {
            if (leftXOnDragStarts == -1F &&
                rightXOnDragStarts == -1F
            ) {
                leftXOnDragStarts = left
                rightXOnDragStarts = right
            }
        }
    }

    object RangeSeekBar {
        const val HARD_MODE = 0
        const val SOFT_MODE = 1

        const val NO_COLOR = -2

        const val LEFT_INDICATOR = 1
        const val RIGHT_INDICATOR = 2

        const val LONG_PRESS_MILLIS = 500L
        const val TAP_PRESS_MILLIS = 200L
    }
}