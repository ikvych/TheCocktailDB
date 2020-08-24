package com.ikvych.cocktail.util.widget.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.ikvych.cocktail.R
import kotlin.math.roundToInt

class AspectRationFrameLayout : FrameLayout {
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

    var aspectRationMode: Int = AspectRatioMode.WIDTH_MODE
    var aspectRation: Float = 1.0F
    var minSize: Float = .0F
    var minModeSize: Float = .0F

    private fun init(attrs: AttributeSet?) {
        setWillNotDraw(false)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AspectRationFrameLayout)
        try {
            aspectRationMode =
                typedArray.getInt(
                    R.styleable.AspectRationFrameLayout_arfl_mode,
                    AspectRatioMode.WIDTH_MODE
                )

            aspectRation = typedArray.getFloat(
                R.styleable.AspectRationFrameLayout_arfl_aspect_ratio,
                1.0F
            )

            minSize = typedArray.getDimension(
                R.styleable.AspectRationFrameLayout_arfl_min_size,
                .0F
            )

            minModeSize = typedArray.getDimension(
                R.styleable.AspectRationFrameLayout_arfl_min_mode_size,
                .0F
            )

        } finally {
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when (aspectRationMode) {
            AspectRatioMode.WIDTH_MODE -> {
                var newWidth = measuredWidth.coerceAtLeast(minSize.toInt())
                var newHeight = (newWidth / aspectRation)
                    .takeIf { it.isFinite() }
                    ?.roundToInt()
                    ?.coerceAtLeast(0)
                    ?: 0
                if (newHeight < minModeSize) {
                    newWidth = (minModeSize * aspectRation).roundToInt()
                    newHeight = minModeSize.roundToInt()
                }
                val newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    newWidth,
                    MeasureSpec.EXACTLY
                )
                val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    newHeight,
                    MeasureSpec.EXACTLY
                )
                super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
            }
            AspectRatioMode.HEIGHT_MODE -> {
                var newHeight = measuredHeight.coerceAtLeast(minSize.toInt())
                var newWidth = (newHeight / aspectRation)
                    .takeIf { it.isFinite() }
                    ?.roundToInt()
                    ?.coerceAtLeast(0)
                    ?: 0
                if (newWidth < minModeSize) {
                    newHeight = (minModeSize * aspectRation).roundToInt()
                    newWidth = minModeSize.roundToInt()
                }
                val newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    newWidth,
                    MeasureSpec.EXACTLY
                )
                val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    newHeight,
                    MeasureSpec.EXACTLY
                )
                super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
            }
        }
    }

    object AspectRatioMode {
        const val WIDTH_MODE = 0
        const val HEIGHT_MODE = 1
    }

}