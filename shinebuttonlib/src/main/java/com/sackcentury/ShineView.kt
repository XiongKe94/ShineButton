package com.sackcentury

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.sackcentury.Ease
import java.util.Random
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * @author xiongke
 * @since 2026/1/14
 **/
class ShineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val FRAME_REFRESH_DELAY: Long = 25 //default 10ms ,change to 25ms for saving cpu.

    private var shineAnimator: ShineAnimator? = null
    private var clickAnimator: ValueAnimator? = null

    var shineButton: ShineButton? = null
        private set

    private var paint: Paint? = null
    private var paint2: Paint? = null
    private var paintSmall: Paint? = null

    private val colorCount = 10

    //Customer property
    private var shineCount: Int = 0
    private var smallOffsetAngle: Float = 0f
    private var turnAngle: Float = 0f
    private var animDuration: Long = 0
    private var clickAnimDuration: Long = 0
    private var shineDistanceMultiple: Float = 0f
    private var smallShineColor: Int = 0
    private var bigShineColor: Int = 0

    private var shineSize: Int = 0

    private var allowRandomColor: Boolean = false
    private var enableFlashing: Boolean = false

    private val rectF = RectF()
    private val rectFSmall = RectF()

    private val random = Random()
    private var centerAnimX: Int = 0
    private var centerAnimY: Int = 0
    private var btnWidth: Int = 0
    private var btnHeight: Int = 0

    private var thirdLength: Double = 0.0
    private var value: Float = 0f
    private var clickValue: Float = 0f
    private var isRun: Boolean = false
    private val distanceOffset: Float = 0.2f
    private var maskColor: Int = 0

    constructor(context: Context, shineButton: ShineButton, shineParams: ShineParams) : this(context) {
        initShineParams(shineParams, shineButton)

        this.shineAnimator = ShineAnimator(animDuration, shineDistanceMultiple, clickAnimDuration)
        ValueAnimator.setFrameDelay(FRAME_REFRESH_DELAY)
        this.shineButton = shineButton

        paint = Paint().apply {
            color = bigShineColor
            strokeWidth = 20f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }

        paint2 = Paint().apply {
            color = this@ShineView.maskColor
            strokeWidth = 20f
            strokeCap = Paint.Cap.ROUND
        }

        paintSmall = Paint().apply {
            color = smallShineColor
            strokeWidth = 10f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }

        clickAnimator = ValueAnimator.ofFloat(0f, 1.1f).apply {
            ValueAnimator.setFrameDelay(FRAME_REFRESH_DELAY)
            duration = clickAnimDuration
            interpolator = EasingInterpolator(Ease.QUART_OUT)
            addUpdateListener { valueAnimator ->
                clickValue = valueAnimator.animatedValue as Float
                invalidate()
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    clickValue = 0f
                    invalidate()
                }
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
        }

        shineAnimator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                visibility = View.GONE
                post {
                    shineButton.removeView(this@ShineView)
                }
            }
            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
    }

    fun showAnimation(shineButton: ShineButton) {
        val activity = shineButton.activity ?: return
        btnWidth = shineButton.width
        btnHeight = shineButton.height
        thirdLength = getThirdLength(btnHeight, btnWidth)

        val rootView = activity.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT) ?: return
        val buttonLocation = IntArray(2)
        val rootLocation = IntArray(2)
        shineButton.getLocationInWindow(buttonLocation)
        rootView.getLocationInWindow(rootLocation)

        centerAnimX = buttonLocation[0] - rootLocation[0] + btnWidth / 2
        centerAnimY = buttonLocation[1] - rootLocation[1] + btnHeight / 2
        shineAnimator?.addUpdateListener { valueAnimator ->
            value = valueAnimator.animatedValue as Float
            if (shineSize != 0 && shineSize > 0) {
                paint?.strokeWidth = (shineSize * (shineDistanceMultiple - value))
                paintSmall?.strokeWidth = ((shineSize / 3f * 2) * (shineDistanceMultiple - value))
            } else {
                paint?.strokeWidth = (btnWidth / 2f * (shineDistanceMultiple - value))
                paintSmall?.strokeWidth = (btnWidth / 3f * (shineDistanceMultiple - value))
            }

            rectF.set(
                centerAnimX - (btnWidth / (3 - shineDistanceMultiple) * value),
                centerAnimY - (btnHeight / (3 - shineDistanceMultiple) * value),
                centerAnimX + (btnWidth / (3 - shineDistanceMultiple) * value),
                centerAnimY + (btnHeight / (3 - shineDistanceMultiple) * value)
            )
            rectFSmall.set(
                centerAnimX - (btnWidth / ((3 - shineDistanceMultiple) + distanceOffset) * value),
                centerAnimY - (btnHeight / ((3 - shineDistanceMultiple) + distanceOffset) * value),
                centerAnimX + (btnWidth / ((3 - shineDistanceMultiple) + distanceOffset) * value),
                centerAnimY + (btnHeight / ((3 - shineDistanceMultiple) + distanceOffset) * value)
            )

            invalidate()
        }
        shineAnimator?.start()
        clickAnimator?.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint?.let { p ->
            if (p.strokeWidth < 0.5f) {
                return@let
            }
            for (i in 0 until shineCount) {
                if (allowRandomColor) {
                    p.color = ShineParams.colorRandom[if (abs(colorCount / 2 - i) >= colorCount) colorCount - 1 else abs(colorCount / 2 - i)]
                }
                canvas.drawArc(rectF, 360f / shineCount * i + 1 + ((value - 1) * turnAngle), 0.1f, false, getConfigPaint(p))
            }
        }

        paintSmall?.let { ps ->
            if (ps.strokeWidth < 0.5f) {
                return@let
            }
            for (i in 0 until shineCount) {
                if (allowRandomColor) {
                    ps.color = ShineParams.colorRandom[if (abs(colorCount / 2 - i) >= colorCount) colorCount - 1 else abs(colorCount / 2 - i)]
                }
                canvas.drawArc(rectFSmall, 360f / shineCount * i + 1 - smallOffsetAngle + ((value - 1) * turnAngle), 0.1f, false, getConfigPaint(ps))
            }
        }

        //paint.setStrokeWidth(btnWidth * (clickValue) * (shineDistanceMultiple - distanceOffset));
        paint2?.let { p2 ->
            if (clickValue != 0f) {
                p2.strokeWidth = btnWidth * clickValue * (shineDistanceMultiple - distanceOffset) - 8
            } else {
                p2.strokeWidth = 0f
            }
        }
        // 注释掉中间的点，只保留四周的闪光点
        // canvas.drawPoint(centerAnimX, centerAnimY, paint);
        // canvas.drawPoint(centerAnimX, centerAnimY, paint2);
        if (shineAnimator != null && !isRun) {
            isRun = true
            shineButton?.let { showAnimation(it) }
        }
    }

    private fun getConfigPaint(paint: Paint): Paint {
        if (enableFlashing) {
            paint.color = ShineParams.colorRandom[random.nextInt(colorCount - 1)]
        }
        return paint
    }

    private fun getThirdLength(btnHeight: Int, btnWidth: Int): Double {
        val all = btnHeight * btnHeight + btnWidth * btnWidth
        return sqrt(all.toDouble())
    }

    class ShineParams {
        companion object {
            val colorRandom = IntArray(10).apply {
                this[0] = Color.parseColor("#FFFF99")
                this[1] = Color.parseColor("#FFCCCC")
                this[2] = Color.parseColor("#996699")
                this[3] = Color.parseColor("#FF6666")
                this[4] = Color.parseColor("#FFFF66")
                this[5] = Color.parseColor("#F44336")
                this[6] = Color.parseColor("#666666")
                this[7] = Color.parseColor("#CCCC00")
                this[8] = Color.parseColor("#666666")
                this[9] = Color.parseColor("#999933")
            }
        }

        var allowRandomColor: Boolean = false
        var animDuration: Long = 1500
        var bigShineColor: Int = 0
        var clickAnimDuration: Long = 200
        var enableFlashing: Boolean = false
        var shineCount: Int = 7
        var shineTurnAngle: Float = 20f
        var shineDistanceMultiple: Float = 1.5f
        var smallShineOffsetAngle: Float = 20f
        var smallShineColor: Int = 0
        var shineSize: Int = 0
        var maskColor: Int = Color.WHITE
    }

    private fun initShineParams(shineParams: ShineParams, shineButton: ShineButton) {
        shineCount = shineParams.shineCount
        turnAngle = shineParams.shineTurnAngle
        smallOffsetAngle = shineParams.smallShineOffsetAngle
        enableFlashing = shineParams.enableFlashing
        allowRandomColor = shineParams.allowRandomColor
        shineDistanceMultiple = shineParams.shineDistanceMultiple
        animDuration = shineParams.animDuration
        clickAnimDuration = shineParams.clickAnimDuration
        smallShineColor = shineParams.smallShineColor
        bigShineColor = shineParams.bigShineColor
        shineSize = shineParams.shineSize
        maskColor = shineParams.maskColor
        if (smallShineColor == 0) {
            smallShineColor = ShineParams.colorRandom[6]
        }

        if (bigShineColor == 0) {
            bigShineColor = shineButton.getColor()
        }
    }
}
