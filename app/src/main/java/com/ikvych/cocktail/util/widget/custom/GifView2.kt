package com.ikvych.cocktail.util.widget.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.MotionEventCompat
import com.ikvych.cocktail.R
import kotlin.math.*

class GifView2 : View {
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

    //Використовується як колір по замовчуванню для circle. Якщо заданий шейдер, даний колір ігнорується.
    var circleColor = Color.DKGRAY
        set(value) {
            if (value != field) {
                field = value
                checkIsNeedApplyShader()
                requestLayout()
            }
        }

    var minCircleSize = context.resources.getDimension(R.dimen.min_circle_size)
        set(value) {
            if (value != field) {
                field = if (value > maxCircleSize) {
                    maxCircleSize
                } else {
                    value
                }
                requestLayout()
            }
        }
    var maxCircleSize = context.resources.getDimension(R.dimen.max_circle_size)
        set(value) {
            if (value != field) {
                field = if (value < minCircleSize) {
                    minCircleSize
                } else {
                    value
                }
                field = value
                requestLayout()
            }
        }
    var cycleDuration = 3000
        set(value) {
            if (value != field) {
                field = abs(value)
                updateAnimators()
                requestLayout()
            }
        }
    private var circleCount = 10
    var sweepAngle = 0
        set(value) {
            if (value != field) {
                field = value
                if (field > 360) {
                    while (field > 360) {
                        field -= 360
                    }
                }
                updateAnimators()
                requestLayout()
            }
        }

    var fallInterpolator: Interpolator = AnticipateInterpolator()
        set(value) {
            if (value != field) {
                field = value
                updateAnimators()
/*                requestLayout()*/
            }
        }
    var upInterpolator: Interpolator = DecelerateInterpolator()
        set(value) {
            if (value != field) {
                field = value
                updateAnimators()
/*                requestLayout()*/
            }
        }

    //Кольори для налаштування шейдера
    var circleInternalColor: Int = CircleColorDefault.NO_COLOR
        set(value) {
            if (field != value) {
                field = value
                checkIsNeedApplyShader()
                requestLayout()
            }
        }
    var circleExternalColor: Int = CircleColorDefault.NO_COLOR
        set(value) {
            if (field != value) {
                field = value
                checkIsNeedApplyShader()
                requestLayout()
            }
        }

    //Визначає чи потрібно застосовувати шейдер
    private var isApplyShader: Boolean = false

    //Для коректного відображення View рекомендується зберігати співвідношення сторін при якій висота не менша ширини
    var aspectRationMode: Int = AspectRatioMode.WIDTH_MODE
        set(value) {
            if (field != value) {
                if (value == AspectRatioMode.WIDTH_MODE || value == AspectRatioMode.HEIGHT_MODE) {
                    field = value
                    requestLayout()
                }
            }
        }
    var aspectRation: Float = 1.0F
        set(value) {
            if (field != value) {
                field = value
                requestLayout()

            }
        }


    //Використовується для отримання даних з PathMeasure
    private val pos = FloatArray(2)
    private val tan = FloatArray(2)

    //Global properties of mainCircle
    private var mainCircleX = 0F
    private var mainCircleY = 0F
    private var mainCircleR = 0F
    private val mainCirclePath = Path()
    private val mainCircleCenterPath = Path()

    //Global properties of mainEllipse
    private val mainEllipseMatrix = Matrix()
    private val mainEllipseReverseMatrix = Matrix()
    private val mainEllipsePath = Path()

    //Координати центру еліпса, потрібний саме Path, так як його можна використати при трансформації перспективи
    private val mainEllipseCenterPath = Path()
    private val mainEllipsePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
    }
    private val mainEllipseCenterPathMeasure = PathMeasure()
    private var mainEllipseCompressionRatio = 1.5F
    private val mainEllipsePerspectivePadding = 150.0F
    private var mainEllipseY = 0.0F
    private var mainEllipseTop = 0.0F
    private var mainEllipseBottom = 0.0F

    //Різниці між центрами mainEllipse і mainCircle по y координаті
    private var mainEllipseYCorrection = 0.0F

    private lateinit var pointsDst: FloatArray
    private lateinit var pointsSrc: FloatArray

    //Global properties кілець на mainEllipse і на mainCircle (тобто кілець які відображаються на екрані)

    //Координати кілець які розташовані на mainCircle
    private val circles = Array(circleCount) { Path() }

    //Кути повороту для кожного кільця розташованого на mainCircle
    private val circlesAngles = Array(circleCount) { 0.0F }

    //Проекція кожного елементу circles на еліпс
    private val perspectiveCircles = Array(circleCount) { Path() }
    private val perspectiveCircleMeasure = PathMeasure()

    //Координати кілець на екрані
    private val points = Array(circleCount) { PointF() }

    //Координати кілець на mainEllipse
    private val pointsPerspective = Array(circleCount) { PointF() }

    private val circlesPaint = Array(circleCount) {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = circleColor
            style = Paint.Style.FILL
        }
    }

    private var mainEllipseVerticalPadding = 0.0F
    private var heightEllipseDifference = 0.0F


    //Визначення і обробка часу потрібного для проходження одного циклу, з врахуванням sweepAngle

    //Для зручності роботи отримую градуси кута на якому буде відбуватися рух кілець
    private var convertedSweepAngle = 0

    //Процент convertedSweepAngle від 360 градусів
    private var sweepAnglePercent: Float = 0.0F

    //Час потрібний на виконнаня анімації для падіння circle і його повернення на місце
    private var verticalAnimationTime: Float = 0.0F

    //Час відведений на роботу одного інтерполятора
    private var interpolatorWorkTime: Long = 0L

    //Період з яким буде починати анімуватися кожний наступний circle
    private var delayInterpolatorCalls: Long = 0L

    init {
        calculateConvertedSweepAngle()
    }

    //Координати першого дотику до екрану під час жесту ACTION_DOWN
    private var cachedTouchX = -1.0F
    private var cachedTouchY = -1.0F

    private var currentAnimatorIndex: Int = 1
    private var stopAnimation = true

    private val animationRunnable = object : Runnable {
        override fun run() {
            if (stopAnimation) {
                return
            }
            currentAnimatorIndex -= 1
            if (currentAnimatorIndex < 0) {
                currentAnimatorIndex = circleCount - 1
            }
            fallAnimators[currentAnimatorIndex].start()
            postDelayed(this, delayInterpolatorCalls)
        }
    }

    private val fallAnimators = Array(circleCount) { index ->
        ValueAnimator.ofFloat(0.0F, 1F).apply {
            interpolator = fallInterpolator
            duration = interpolatorWorkTime
            addUpdateListener {
                fallCircle((it.animatedValue as Float), index)
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    upAnimators[index].start()
                }
            })
        }
    }

    private val upAnimators = Array(circleCount) { index ->
        ValueAnimator.ofFloat(0.0F, 1F).apply {
            interpolator = upInterpolator
            duration = interpolatorWorkTime
            addUpdateListener {
                upCircle(it.animatedValue as Float, index)
            }
        }
    }

    object AspectRatioMode {
        const val WIDTH_MODE = 0
        const val HEIGHT_MODE = 1
    }

    object CircleColorDefault {
        const val NO_COLOR = -2
    }

    private fun updateAnimators() {
        calculateConvertedSweepAngle()
        upAnimators.forEach {
            it.interpolator = upInterpolator
            it.duration = interpolatorWorkTime
        }
        fallAnimators.forEach {
            it.interpolator = fallInterpolator
            it.duration = interpolatorWorkTime
        }
    }

    private fun calculateConvertedSweepAngle() {
        convertedSweepAngle = 360 - sweepAngle
        sweepAnglePercent = convertedSweepAngle * 100 / 360F
        verticalAnimationTime = sweepAnglePercent * cycleDuration / 100
        interpolatorWorkTime = (verticalAnimationTime / 2).roundToLong()
        delayInterpolatorCalls = (cycleDuration / circleCount).toLong()
    }

    private fun init(attrs: AttributeSet?) {
        setWillNotDraw(false)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GifView2)
        try {
            aspectRationMode =
                typedArray.getInt(
                    R.styleable.GifView2_gv_mode,
                    AspectRatioMode.WIDTH_MODE
                )

            aspectRation = typedArray.getFloat(
                R.styleable.GifView2_gv_aspect_ratio,
                1.0F
            )

            circleColor =
                typedArray.getColor(
                    R.styleable.GifView2_gv_circle_color,
                    circleColor
                )

            circleInternalColor =
                typedArray.getColor(
                    R.styleable.GifView2_gv_circle_internal_color,
                    circleInternalColor
                )

            circleExternalColor =
                typedArray.getColor(
                    R.styleable.GifView2_gv_circle_external_color,
                    circleExternalColor
                )
            checkIsNeedApplyShader()

            minCircleSize = typedArray.getDimension(
                R.styleable.GifView2_gv_min_circle_size,
                minCircleSize
            )

            maxCircleSize = typedArray.getDimension(
                R.styleable.GifView2_gv_max_circle_size,
                maxCircleSize
            )

            if (minCircleSize > maxCircleSize) {
                maxCircleSize = minCircleSize
            }

            cycleDuration = abs(
                typedArray.getInteger(
                    R.styleable.GifView2_gv_cycle_duration,
                    cycleDuration
                )
            )

            circleCount = abs(
                typedArray.getInteger(
                    R.styleable.GifView2_gv_circle_count,
                    circleCount
                )
            )

            sweepAngle = abs(
                typedArray.getInteger(
                    R.styleable.GifView2_gv_sweep_angle,
                    sweepAngle
                )
            )

            mainEllipseCompressionRatio = typedArray.getFloat(
                R.styleable.GifView2_gv_compress_ratio,
                mainEllipseCompressionRatio
            )

            configureSweepAngle()

        } finally {
            typedArray.recycle()
        }
    }

    private fun checkIsNeedApplyShader() {
        //Якщо задано хоча б один із кольорів, застосовуємо для Paint шейдер із даними кольорами
        if (circleInternalColor != CircleColorDefault.NO_COLOR || circleExternalColor != CircleColorDefault.NO_COLOR) {
            if (circleInternalColor == CircleColorDefault.NO_COLOR) {
                circleInternalColor = Color.TRANSPARENT
            }
            if (circleExternalColor == CircleColorDefault.NO_COLOR) {
                circleExternalColor = Color.TRANSPARENT
            }
            isApplyShader = true
        }
    }

    private fun configureSweepAngle() {
        if (sweepAngle > 360) {
            while (sweepAngle > 360) {
                sweepAngle -= 360
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when (aspectRationMode) {
            AspectRatioMode.WIDTH_MODE -> {
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
            AspectRatioMode.HEIGHT_MODE -> {
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

        mainCircleX = ((measuredWidth.toFloat() - paddingStart - paddingEnd) / 2) + paddingStart
        mainCircleY = ((measuredHeight.toFloat() - paddingBottom - paddingTop) / 2) + paddingTop
        mainCircleR = (measuredWidth.toFloat() - paddingStart - paddingEnd) / 2

        mainCirclePath.reset()
        mainCirclePath.addCircle(mainCircleX, mainCircleY, mainCircleR, Path.Direction.CCW)
        mainCircleCenterPath.reset()
        mainCircleCenterPath.addCircle(mainCircleX, mainCircleY, 0.5F, Path.Direction.CW)

        pointsSrc = getPointsSrc()

        mainEllipseVerticalPadding =
            (mainCircleY - mainCircleR - paddingTop + (mainCircleR / 2)) * mainEllipseCompressionRatio

        pointsDst = getPointsDst()

        mainEllipseReverseMatrix.setPolyToPoly(pointsDst, 0, pointsSrc, 0, 4)

        //Отримую координати центру еліпса
        mainEllipseMatrix.setPolyToPoly(pointsSrc, 0, pointsDst, 0, 4)
        mainCircleCenterPath.transform(mainEllipseMatrix, mainEllipseCenterPath)
        mainEllipseCenterPathMeasure.setPath(mainEllipseCenterPath, false)
        mainEllipseCenterPathMeasure.getPosTan(0.0F, pos, tan)
        mainEllipseY = pos[1]
        mainEllipseYCorrection = mainCircleY - pos[1]

        mainEllipseTop = mainEllipseVerticalPadding + paddingTop + mainEllipseYCorrection
        mainEllipseBottom =
            measuredHeight - paddingBottom - mainEllipseVerticalPadding + mainEllipseYCorrection
        heightEllipseDifference = mainEllipseBottom - mainEllipseTop

        val modelAngle = 360 / circleCount
        points.forEachIndexed { index, pointF ->
            //Координати потчного Circle на mainCircle
            val pointX =
                (mainCircleR) * cos(Math.toRadians(index * modelAngle.toDouble())).toFloat() + mainCircleX
            val pointY =
                (mainCircleR) * sin(Math.toRadians(index * modelAngle.toDouble())).toFloat() + mainCircleY

            //Кут потчного Circle на mainCircle
            circlesAngles[index] = index * modelAngle.toFloat()

            val circle = circles[index]
            circle.reset()
            //Зберіг координати потчного Circle
            circle.addCircle(pointX, pointY, 0.5F, Path.Direction.CW)

            val perspectiveCircle = perspectiveCircles[index]
            perspectiveCircle.reset()
            //Перевів поточні координати Circle в перспективу
            circle.transform(mainEllipseMatrix, perspectiveCircle)

            perspectiveCircleMeasure.setPath(perspectiveCircle, false)
            perspectiveCircleMeasure.getPosTan(0.0F, pos, tan)

            val currentCircleSize = calculateCircleRadius(pos[1])

            //Circle який буде видно на екрані
            perspectiveCircle.addCircle(pos[0], pos[1], currentCircleSize, Path.Direction.CW)

            val pointPerspective = pointsPerspective[index]

            //Зберіг координати Circle на mainEllipse
            pointPerspective.x = pos[0]
            pointPerspective.y = pos[1]

            //Зберіг справжні координати Circle(ті по яких відмальовується Circle на екрані)
            pointF.x = pos[0]
            pointF.y = pos[1]

        }

        mainEllipsePath.reset()
        //Перевів mainCircle в перспективу
        mainCirclePath.transform(mainEllipseMatrix, mainEllipsePath)

        //Оновлюю шейдери якщо потрібно
        //У цьому методі створюються обєкти, і я знаю що створення обєктів не бажене в методі onMeasure
        //Але іншого способу поки не придумав
        //Крім того цей метод викликаєьться не так вже й часто
        if (isApplyShader) {
            updateCirclesShader()
        }
    }

    private fun getPointsSrc(): FloatArray {
        return floatArrayOf(
            paddingStart.toFloat(), mainCircleY - mainCircleR,
            measuredWidth.toFloat() - paddingEnd, mainCircleY - mainCircleR,
            paddingStart.toFloat(), mainCircleY + mainCircleR,
            measuredWidth.toFloat() - paddingEnd, mainCircleY + mainCircleR
        )
    }

    private fun getPointsDst(): FloatArray {
        return floatArrayOf(
            paddingStart.toFloat() + mainEllipsePerspectivePadding,
            mainEllipseVerticalPadding + paddingTop + mainEllipseYCorrection,
            measuredWidth.toFloat() - paddingEnd - mainEllipsePerspectivePadding,
            mainEllipseVerticalPadding + paddingTop + mainEllipseYCorrection,
            paddingStart.toFloat(),
            measuredHeight - paddingBottom - mainEllipseVerticalPadding + mainEllipseYCorrection,
            measuredWidth.toFloat() - paddingEnd,
            measuredHeight - paddingBottom - mainEllipseVerticalPadding + mainEllipseYCorrection
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mDetector.onTouchEvent(event)
        //Свариться якщо не викликати цей метод
        performClick()
        //Обнуляю координати
        when (MotionEventCompat.getActionMasked(event)) {
            MotionEvent.ACTION_UP -> {
                cachedTouchX = -1.0F
                cachedTouchY = -1.0F
            }
            MotionEvent.ACTION_CANCEL -> {
                cachedTouchX = -1.0F
                cachedTouchY = -1.0F
            }
            MotionEvent.ACTION_OUTSIDE -> {
                cachedTouchX = -1.0F
                cachedTouchY = -1.0F
            }
        }
        return true
    }

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        private val FLING_DURATION_COEFFICIENT = 4
        private var fling_duration = 500L

        //Кут повороту при Fling
        var flingAngle = 0.0F

        //Координати в якій відпустили відбувся ACTION_UP
        var flingY = 0.0F
        private val flingRunnable = Runnable { flingAnimators.start() }
        private val flingAnimators = ValueAnimator.ofFloat(1.0F, 0.0F).apply {
            interpolator = DecelerateInterpolator()
            duration = fling_duration * FLING_DURATION_COEFFICIENT
            addUpdateListener {
                rotateCircles(flingY, flingAngle * it.animatedValue as Float)
            }
        }

        private val perspectiveLine = Path()
        private val currentLine = Path()
        private val measurePerspectiveLine = PathMeasure()
        private val position = FloatArray(2)

        override fun onDown(event: MotionEvent): Boolean {
            flingAnimators.end()
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e2 == null || e1 == null) return false
            fling_duration = velocityX.toLong()
            flingY = e2.y
            post(flingRunnable)
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (e2 == null || e1 == null) return false
            if (cachedTouchX == -1.0F && cachedTouchY == -1.0F) {
                initCachedTouchCoordinates(e1.x, e1.y)
            }
            val angle = calculateRotateAngle(e2.x, e2.y)
            calculateDemonstrateLine(e2.x, e2.y)
            rotateCircles(cachedTouchY, angle)
            return true
        }

        private fun calculateDemonstrateLine(x: Float, y: Float) {
            demonstrateLine.reset()
            demonstrateLine.moveTo(mainCircleX, mainCircleY)
            demonstrateLine.lineTo(x, y)
        }

        private fun initCachedTouchCoordinates(x: Float, y: Float) {
            currentLine.reset()
            perspectiveLine.reset()
            currentLine.moveTo(x, y)
            currentLine.lineTo(mainCircleX, mainCircleY)

            currentLine.transform(mainEllipseReverseMatrix, perspectiveLine)

            measurePerspectiveLine.setPath(perspectiveLine, false)
            measurePerspectiveLine.getPosTan(0.0F, position, tan)

            cachedTouchX = position[0]
            cachedTouchY = position[1]
        }


        private fun calculateRotateAngle(newX: Float, newY: Float): Float {
            var oldAngle = 0.0F

            val oldSideWidth = abs(mainCircleX - cachedTouchX)
            val secondOldSideWidth = abs(mainCircleY - cachedTouchY)
            val oldHypotenuse =
                sqrt((oldSideWidth * oldSideWidth) + (secondOldSideWidth * secondOldSideWidth))
            val oldCos = oldSideWidth / oldHypotenuse
            oldAngle = Math.toDegrees(acos(oldCos.toDouble())).toFloat()

            oldAngle = when (getQuarter(cachedTouchX, cachedTouchY)) {
                Quarter.I -> oldAngle
                Quarter.II -> 90 - oldAngle + 90
                Quarter.III -> 180 - oldAngle
                Quarter.IV -> oldAngle
                else -> oldAngle
            }

            val perspectiveAngle = calculateRotateAngleInPerspective(newX, newY)

            if (perspectiveAngle.isNaN() || oldAngle.isNaN()) {
                return 0.0F
            }
            flingAngle = perspectiveAngle - oldAngle
            return perspectiveAngle - oldAngle
        }

        private fun calculateRotateAngleInPerspective(x: Float, y: Float): Float {
            currentLine.reset()
            perspectiveLine.reset()
            currentLine.moveTo(x, y)
            currentLine.lineTo(mainCircleX, mainCircleY)

            currentLine.transform(mainEllipseReverseMatrix, perspectiveLine)
            measurePerspectiveLine.setPath(perspectiveLine, false)
            measurePerspectiveLine.getPosTan(0.0F, position, tan)
            val newX = position[0]
            val newY = position[1]

            val sideWidth = abs(mainCircleX - newX)
            val secondSideWidth = abs(mainCircleY - newY)
            val hypotenuse = sqrt((sideWidth * sideWidth) + (secondSideWidth * secondSideWidth))
            val newCos = sideWidth / hypotenuse
            var newAngle = Math.toDegrees(acos(newCos.toDouble())).toFloat()

            newAngle = when (getQuarter(newX, newY)) {
                Quarter.I -> newAngle
                Quarter.II -> 90 - newAngle + 90
                Quarter.III -> 180 - newAngle
                Quarter.IV -> newAngle
                else -> newAngle
            }

            cachedTouchX = newX
            cachedTouchY = newY

            return newAngle
        }

        private fun rotateCircles(y: Float, angle: Float) {
            if (y >= mainCircleY) {
                rotateCircles(-1 * angle)
            } else {
                rotateCircles(angle)
            }
        }

        private fun getQuarter(x: Float, y: Float): Int {
            return if (y <= mainCircleY) {
                if (x >= mainCircleX) {
                    Quarter.I
                } else {
                    Quarter.II
                }
            } else {
                if (x > mainCircleX) {
                    Quarter.IV
                } else {
                    Quarter.III
                }
            }
        }
    }

    private val mDetector = GestureDetectorCompat(context, gestureListener)

    private object Quarter {
        const val I = 1
        const val II = 2
        const val III = 3
        const val IV = 4
    }

    // Визначає розмір кружечка в залежності від розташування на еліпсі
    private fun calculateCircleRadius(y: Float): Float {
        val current = y - mainEllipseTop
        val newSizePercent = current * 100 / heightEllipseDifference
        return ((maxCircleSize - minCircleSize) * newSizePercent / 100) + minCircleSize
    }

    private fun rotateCircles(angle: Float = 1F) {
        points.forEachIndexed { index, pointF ->
            val currentRotateAngle = circlesAngles[index] - angle
            circlesAngles[index] = currentRotateAngle

            // нові координати на колі
            val pointX =
                (mainCircleR) * cos(Math.toRadians(currentRotateAngle.toDouble())).toFloat() + mainCircleX
            val pointY =
                (mainCircleR) * sin(Math.toRadians(currentRotateAngle.toDouble())).toFloat() + mainCircleY

            val circle = circles[index]
            circle.reset()
            // перемалювали координати центру для поточного Circle на mainCircle
            circle.addCircle(pointX, pointY, 0.5F, Path.Direction.CW)

            val perspectiveCircle = perspectiveCircles[index]
            perspectiveCircle.reset()
            // перезаписали центр для mainEllipse
            circle.transform(mainEllipseMatrix, perspectiveCircle)

            perspectiveCircleMeasure.setPath(perspectiveCircle, false)
            perspectiveCircleMeasure.getPosTan(0.0F, pos, tan)
            val currentCircleRadius = calculateCircleRadius(pos[1])
            // стерли координати центра кола
            perspectiveCircle.reset()
            // додали саме коло на еліпс

            val pointPerspective = pointsPerspective[index]
            val oldCircleY = pointF.y
            val oldPerspectiveCircleY = pointPerspective.y
            val newCircleDistance = oldPerspectiveCircleY - oldCircleY
            val newCircleY = pos[1] - newCircleDistance

            //Кружечок який намалюється на екрані
            perspectiveCircle.addCircle(pos[0], newCircleY, currentCircleRadius, Path.Direction.CW)

            // зберегли нові справжні координати(ті що видно на екрані)
            pointF.x = pos[0]
            pointF.y = newCircleY

            // зберегли нові координати поточного кружечка нроектовані на mainEllipse
            pointPerspective.x = pos[0]
            pointPerspective.y = pos[1]
        }
        if (isApplyShader) {
            updateCirclesShader()
        }
        invalidate()

    }

    private fun upCircle(fraction: Float, position: Int) {
        val perspectiveCircle = perspectiveCircles[position]
        val point = points[position]
        val perspectivePoint = pointsPerspective[position]
        val currentCircleRadius = calculateCircleRadius(perspectivePoint.y)
        val generalFallWidth = perspectivePoint.y + currentCircleRadius
        val currentCirclePosition = generalFallWidth * fraction - currentCircleRadius

        perspectiveCircle.reset()
        perspectiveCircle.addCircle(
            point.x,
            currentCirclePosition,
            currentCircleRadius,
            Path.Direction.CW
        )
        point.y = currentCirclePosition

        if (isApplyShader) {
            updateCirclesShader()
        }

        invalidate()
    }


    private fun fallCircle(fraction: Float, position: Int) {
        val perspectiveCircle = perspectiveCircles[position]
        val point = points[position]
        val perspectivePoint = pointsPerspective[position]
        val currentCircleRadius = calculateCircleRadius(perspectivePoint.y)
        val generalFallWidth = bottom - perspectivePoint.y + currentCircleRadius
        val currentCirclePosition = perspectivePoint.y + generalFallWidth * fraction

        perspectiveCircle.reset()
        perspectiveCircle.addCircle(
            point.x,
            currentCirclePosition,
            currentCircleRadius,
            Path.Direction.CW
        )
        point.y = currentCirclePosition

        if (isApplyShader) {
            updateCirclesShader()
        }

        invalidate()
    }

    //Лінія яка зїднує центри еліпса з місцем дотику на екрані
    private val demonstrateLine = Path()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        perspectiveCircles.forEachIndexed { index, circle ->
            canvas.drawPath(circle, circlesPaint[index])
        }
        canvas.drawPath(mainEllipsePath, mainEllipsePaint)
        canvas.drawPath(demonstrateLine, mainEllipsePaint)
    }

    private fun updateCirclesShader() {
        circlesPaint.forEachIndexed { index, paint ->
            paint.shader = RadialGradient(
                points[index].x,
                points[index].y,
                calculateCircleRadius(pointsPerspective[index].y),
                circleInternalColor,
                circleExternalColor,
                Shader.TileMode.MIRROR
            )
        }
    }

    fun startAnim() {
        if (circleCount >= 1 && stopAnimation) {
            stopAnimation = false
            post(animationRunnable)
        }
    }

    fun stopAnim() {
        stopAnimation = true
    }

    //Перевизначив бо свариться студія
    override fun performClick(): Boolean {
        return super.performClick()
    }
}