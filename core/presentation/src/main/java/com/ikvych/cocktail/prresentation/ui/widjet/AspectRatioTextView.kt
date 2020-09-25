package com.ikvych.cocktail.prresentation.ui.widjet

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.ikvych.cocktail.prresentation.R
import kotlin.math.roundToInt

class AspectRatioTextView : AppCompatTextView {
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

    var aspectRationMode: Int = AspectRatioFrameLayout.AspectRatioMode.WIDTH_MODE
    var aspectRation: Float = 1.0F

    private fun init(attrs: AttributeSet?) {
        setWillNotDraw(false)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioTextView)
        try {
            aspectRationMode =
                typedArray.getInt(
                    R.styleable.AspectRatioTextView_artv_mode,
                    AspectRatioFrameLayout.AspectRatioMode.WIDTH_MODE
                )

            aspectRation = typedArray.getFloat(
                R.styleable.AspectRatioTextView_artv_aspect_ratio,
                1.0F
            )

        } finally {
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when (aspectRationMode) {
            AspectRatioFrameLayout.AspectRatioMode.WIDTH_MODE -> {
                val newHeight = (measuredWidth / aspectRation)
                    .takeIf { it.isFinite() }
                    ?.roundToInt()
                    ?.coerceAtLeast(0)
                    ?: 0
                val newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    measuredWidth,
                    MeasureSpec.EXACTLY
                )
                val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    newHeight,
                    MeasureSpec.EXACTLY
                )
                super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
            }
            AspectRatioFrameLayout.AspectRatioMode.HEIGHT_MODE -> {
                val newWidth = (measuredHeight / aspectRation)
                    .takeIf { it.isFinite() }
                    ?.roundToInt()
                    ?.coerceAtLeast(0)
                    ?: 0
                val newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    newWidth,
                    MeasureSpec.EXACTLY
                )
                val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    measuredHeight,
                    MeasureSpec.EXACTLY
                )
                super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
            }
        }
    }
}