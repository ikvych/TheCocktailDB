package com.ikvych.cocktail.prresentation.ui.widjet

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.withSave
import com.ikvych.cocktail.prresentation.R

class RoundedImageView : AppCompatImageView {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private val is26Plus = Build.VERSION.SDK_INT >= 26
    private val corners: Array<Int> = arrayOf(
        CornerFlag.CORNER_TOP_LEFT,
        CornerFlag.CORNER_TOP_RIGHT,
        CornerFlag.CORNER_BOTTOM_RIGHT,
        CornerFlag.CORNER_BOTTOM_LEFT
    )

    private var rectF = RectF()
    private var roundedPath: Path = Path()

    private var cornerFlags: Int = CornerFlag.CORNER_NONE
    var cornerRadius: Float = .0F
        set(value) {
            field = value
            configureRadii()
        }

    private var radii = FloatArray(8)

    private fun init(attrs: AttributeSet?) {
        setWillNotDraw(false)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView)
        try {
            cornerFlags =
                typedArray.getInt(
                    R.styleable.RoundedImageView_roundedCorners,
                    CornerFlag.CORNER_NONE
                )

            cornerRadius = typedArray.getDimension(
                R.styleable.RoundedImageView_cornerRadius,
                .0F
            )

        } finally {
            typedArray.recycle()
        }
    }

    private fun configureRadii() {
        for (i in 0..3) {
            if (isCornerRounded(corners[i])) {
                radii[2 * i] = cornerRadius
                radii[2 * i + 1] = cornerRadius
            }
        }
    }

    private fun isCornerRounded(corner: Int): Boolean {
        val result = cornerFlags and corner
        return (result) == corner
    }

    private val roundPaint = object : Paint() {
        init {
            isDither = true
            isAntiAlias = true
            isFilterBitmap = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        rectF.set(.0F, .0F, measuredWidth.toFloat(), measuredHeight.toFloat())
        drawPath()
    }

    private fun drawPath() {
        if (radii.any { it > 0.0F } && cornerFlags != CornerFlag.CORNER_NONE) {
            roundedPath.rewind()
            roundedPath.addRoundRect(rectF, radii, Path.Direction.CW)
            roundedPath.close()
        } else roundedPath.reset()
    }

    override fun draw(canvas: Canvas?) {
        canvas ?: return
        if (!roundedPath.isEmpty) {
            if (is26Plus) {
                canvas.withSave {
                    canvas.clipPath(roundedPath)
                    super.draw(canvas)
                }
            } else {
                canvas.saveLayer(null, null)
                super.draw(canvas)
                canvas.drawPath(roundedPath, roundPaint)
                canvas.restore()
            }
        } else super.draw(canvas)
    }

    object CornerFlag {
        const val CORNER_NONE = 0
        const val CORNER_TOP_LEFT = 1
        const val CORNER_TOP_RIGHT = 2
        const val CORNER_BOTTOM_RIGHT = 4
        const val CORNER_BOTTOM_LEFT = 8
        const val CORNER_ALL = 15
    }
}