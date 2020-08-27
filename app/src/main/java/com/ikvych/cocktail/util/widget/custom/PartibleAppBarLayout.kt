package com.ikvych.cocktail.util.widget.custom

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.AppBarLayout
import com.ikvych.cocktail.R
import kotlin.math.roundToInt

//Можливість встановлювати висоту віджета в певну частину від загальної висоти
//Для деталізації напою використовується щоб відображати картинку на 1/3 від висоти екрану
class PartibleAppBarLayout : AppBarLayout {
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
    var portion: Float = 1.0F

    private fun init(attrs: AttributeSet?) {
        setWillNotDraw(false)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PartibleAppBarLayout)
        try {
            aspectRationMode =
                typedArray.getInt(
                    R.styleable.PartibleAppBarLayout_pabl_mode,
                    AspectRatioMode.WIDTH_MODE
                )

            portion = typedArray.getFloat(
                R.styleable.PartibleAppBarLayout_pabl_part,
                1.0F
            )

        } finally {
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when (aspectRationMode) {
            AspectRatioMode.WIDTH_MODE -> {
                val newWidth = (measuredWidth / portion)
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
            AspectRatioMode.HEIGHT_MODE -> {
                val newHeight = (measuredHeight / portion)
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
        }
    }

    object AspectRatioMode {
        const val WIDTH_MODE = 0
        const val HEIGHT_MODE = 1
    }
}