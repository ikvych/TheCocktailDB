package com.ikvych.cocktail.util.widget.custom

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton

class MyAppCompatButton : AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var saturation: Float = 1.0F
    set(value) {
        field = value
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(value)
        val colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
        val paint = Paint()
        paint.colorFilter = colorMatrixColorFilter
        setLayerType(View.LAYER_TYPE_HARDWARE, paint)
        invalidate()
    }


}