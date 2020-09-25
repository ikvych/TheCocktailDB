package com.ikvych.cocktail.prresentation.ui.widjet

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.ikvych.cocktail.prresentation.R
import kotlin.math.roundToInt

class AspectRatioFrameLayout : FrameLayout {
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
    var minSizeByMode: Float = .0F
    var minSizeByReverseMode: Float = .0F

    private fun init(attrs: AttributeSet?) {
        setWillNotDraw(false)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioFrameLayout)
        try {
            aspectRationMode =
                typedArray.getInt(
                    R.styleable.AspectRatioFrameLayout_arfl_mode,
                    AspectRatioMode.WIDTH_MODE
                )

            aspectRation = typedArray.getFloat(
                R.styleable.AspectRatioFrameLayout_arfl_aspect_ratio,
                1.0F
            )

            minSizeByMode = typedArray.getDimension(
                R.styleable.AspectRatioFrameLayout_arfl_min_size_by_mode,
                .0F
            )

            minSizeByReverseMode = typedArray.getDimension(
                R.styleable.AspectRatioFrameLayout_arfl_min_size_by_reverse_mode,
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
                var newWidth = measuredWidth.coerceAtLeast(minSizeByMode.toInt())
                var newHeight = (newWidth / aspectRation)
                    .takeIf { it.isFinite() }
                    ?.roundToInt()
                    ?.coerceAtLeast(0)
                    ?: 0
                if (newHeight < minSizeByReverseMode) {
                    newWidth = (minSizeByReverseMode * aspectRation).roundToInt()
                    newHeight = minSizeByReverseMode.roundToInt()
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
                var newHeight = measuredHeight.coerceAtLeast(minSizeByMode.toInt())
                var newWidth = (newHeight / aspectRation)
                    .takeIf { it.isFinite() }
                    ?.roundToInt()
                    ?.coerceAtLeast(0)
                    ?: 0
                if (newWidth < minSizeByReverseMode) {
                    newHeight = (minSizeByReverseMode * aspectRation).roundToInt()
                    newWidth = minSizeByReverseMode.roundToInt()
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