package com.ikvych.cocktail.prresentation.ui.widjet

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class GifView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val pathSrc = Path()
    private val pathDst = Path()
    private val rect = RectF()
    private val paint = Paint()
    private lateinit var pointsDst: FloatArray
    private lateinit var pointsSrc: FloatArray
    private val polyMatrix = Matrix()

    init {
        paint.strokeWidth = 3F
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        canvas.drawARGB(80, 102, 204, 255)

        rect.set(0F, 0F, measuredWidth.toFloat(), measuredWidth.toFloat())

        pathSrc.reset()
        pathSrc.addRect(rect, Path.Direction.CW)
        pathSrc.addCircle(measuredWidth / 2F, measuredWidth / 2F, measuredWidth / 2F, Path.Direction.CW)
/*        canvas.drawRect(rect, paint)
        canvas.drawCircle(measuredWidth / 2F, measuredWidth / 2F, measuredWidth / 2F, paint)*/
        /*canvas.drawPath(pathSrc, paint)*/
        pointsSrc = floatArrayOf(
            0.0F, measuredWidth.toFloat(),
            measuredWidth.toFloat(), measuredWidth.toFloat(),
            0.0F,  0.0F,
            measuredWidth.toFloat(),  0.0F
        )
        pointsDst = floatArrayOf(
            0.0F, measuredWidth.toFloat(),
            measuredWidth.toFloat(), measuredWidth.toFloat(),
            96F,  measuredWidth.toFloat() / 2,
            measuredWidth.toFloat() - 96F,  measuredWidth.toFloat() / 2
        )

        val result = matrix.setPolyToPoly(pointsSrc, 0, pointsDst, 0, 4)
        pathSrc.transform(matrix, pathDst)
        /*canvas.drawPath(pathDst, paint)*/

    }
}