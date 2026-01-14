package com.sackcentury

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.sackcentury.shinebuttonlib.R

/**
 * @author xiongke
 * @since 2026/1/14
 **/
class ShineButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var activity: Activity? = null
        private set
    private var isChecked = false
    private var unCheckColor: Int = Color.GRAY
    private var checkColor: Int = Color.BLACK
    private var bottomHeight: Int = 0
    private var realBottomHeight: Int = 0
    private var checkDrawable: Drawable? = null
    private var unCheckDrawable: Drawable? = null
    private val metrics = DisplayMetrics()
    private var shakeAnimator: ValueAnimator? = null
    private val shineParams = ShineView.ShineParams()

    init {
        if (context is Activity) {
            init(context)
        }
        if (attrs != null) {
            initButton(context, attrs)
        }
    }

    private fun initButton(context: Context, attrs: AttributeSet) {
        if (context is Activity) {
            init(context)
        }
        val a = context.obtainStyledAttributes(attrs, R.styleable.ShineButton)
        unCheckColor = a.getColor(R.styleable.ShineButton_unCheckColor, Color.GRAY)
        checkColor = a.getColor(R.styleable.ShineButton_checkColor, Color.BLACK)
        shineParams.allowRandomColor = a.getBoolean(R.styleable.ShineButton_allow_random_color, false)
        shineParams.animDuration = a.getInteger(R.styleable.ShineButton_shine_animation_duration, shineParams.animDuration.toInt()).toLong()
        shineParams.bigShineColor = a.getColor(R.styleable.ShineButton_big_shine_color, shineParams.bigShineColor)
        shineParams.clickAnimDuration = a.getInteger(R.styleable.ShineButton_click_animation_duration, shineParams.clickAnimDuration.toInt()).toLong()
        shineParams.enableFlashing = a.getBoolean(R.styleable.ShineButton_enable_flashing, false)
        shineParams.shineCount = a.getInteger(R.styleable.ShineButton_shine_count, shineParams.shineCount)
        shineParams.shineDistanceMultiple = a.getFloat(R.styleable.ShineButton_shine_distance_multiple, shineParams.shineDistanceMultiple)
        shineParams.shineTurnAngle = a.getFloat(R.styleable.ShineButton_shine_turn_angle, shineParams.shineTurnAngle)
        shineParams.smallShineColor = a.getColor(R.styleable.ShineButton_small_shine_color, shineParams.smallShineColor)
        shineParams.smallShineOffsetAngle = a.getFloat(R.styleable.ShineButton_small_shine_offset_angle, shineParams.smallShineOffsetAngle)
        shineParams.shineSize = a.getDimensionPixelSize(R.styleable.ShineButton_shine_size, shineParams.shineSize)
        shineParams.maskColor = a.getColor(R.styleable.ShineButton_mask_color, shineParams.maskColor)
        checkDrawable = a.getDrawable(R.styleable.ShineButton_checkDrawable)
        unCheckDrawable = a.getDrawable(R.styleable.ShineButton_unCheckDrawable)
        a.recycle()
        updateViewState()
    }

    fun init(activity: Activity) {
        this.activity = activity
    }

    fun getBottomHeight(real: Boolean): Int {
        return if (real) {
            realBottomHeight
        } else {
            bottomHeight
        }
    }

    fun getColor(): Int = checkColor

    fun isChecked(): Boolean = isChecked

    fun setUnCheckColor(unCheckColor: Int) {
        this.unCheckColor = unCheckColor
    }

    fun setCheckColor(checkColor: Int) {
        this.checkColor = checkColor
    }

    fun setChecked(checked: Boolean, anim: Boolean) {
        isChecked = checked
        if (checked) {
            if (anim) {
                showAnim()
            }
        } else {
            if (anim) {
                cancelAnim()
            }
        }
    }

    private fun showAnim() {
        activity?.let { act ->
            val rootView = act.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
            val shineView = ShineView(act, this, shineParams)
            rootView.addView(shineView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            doShareAnim()
        }
    }

    private fun cancelAnim() {
        shakeAnimator?.let {
            it.end()
            it.cancel()
        }
    }

    fun setAllowRandomColor(allowRandomColor: Boolean) {
        shineParams.allowRandomColor = allowRandomColor
    }

    fun setAnimDuration(durationMs: Int) {
        shineParams.animDuration = durationMs.toLong()
    }

    fun setBigShineColor(color: Int) {
        shineParams.bigShineColor = color
    }

    fun setClickAnimDuration(durationMs: Int) {
        shineParams.clickAnimDuration = durationMs.toLong()
    }

    fun enableFlashing(enable: Boolean) {
        shineParams.enableFlashing = enable
    }

    fun setShineCount(count: Int) {
        shineParams.shineCount = count
    }

    fun setShineDistanceMultiple(multiple: Float) {
        shineParams.shineDistanceMultiple = multiple
    }

    fun setShineTurnAngle(angle: Float) {
        shineParams.shineTurnAngle = angle
    }

    fun setSmallShineColor(color: Int) {
        shineParams.smallShineColor = color
    }

    fun setSmallShineOffAngle(angle: Float) {
        shineParams.smallShineOffsetAngle = angle
    }

    fun setShineSize(size: Int) {
        shineParams.shineSize = size
    }

    fun setMaskColor(maskColor: Int) {
        shineParams.maskColor = maskColor
    }

    fun setShapeResource(checkRaw: Int, unCheckRaw: Int) {
        checkDrawable = activity?.let { ContextCompat.getDrawable(it, checkRaw) }
        unCheckDrawable = activity?.let { ContextCompat.getDrawable(it, unCheckRaw) }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        calPixels()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = if (widthMeasureSpec == 0) 50 else widthMeasureSpec
        val height = if (heightMeasureSpec == 0) 50 else heightMeasureSpec
        super.onMeasure(width, height)
    }

    fun removeView(view: View) {
        activity?.let { act ->
            val rootView = act.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
            rootView.removeView(view)
        }
    }

    private fun doShareAnim() {
        shakeAnimator = ValueAnimator.ofFloat(0.4f, 1f, 0.9f, 1f).apply {
            interpolator = LinearInterpolator()
            duration = 500
            startDelay = 180
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                scaleX = value
                scaleY = value
            }
        }
        invalidate()
        shakeAnimator?.start()
    }

    private fun calPixels() {
        activity?.let { act ->
            act.windowManager.defaultDisplay.getMetrics(metrics)
            val location = IntArray(2)
            getLocationInWindow(location)
            val visibleFrame = Rect()
            act.window.decorView.getWindowVisibleDisplayFrame(visibleFrame)
            realBottomHeight = visibleFrame.height() - location[1]
            bottomHeight = metrics.heightPixels - location[1]
        }
    }

    fun updateViewState() {
        if (isChecked) {
            checkDrawable?.let {
                tintImageViewDrawable(it, checkColor)
            }
        } else {
            unCheckDrawable?.let {
                tintImageViewDrawable(it, unCheckColor)
            }
        }
    }

    private fun tintImageViewDrawable(drawable: Drawable, tintColor: Int) {
        val drawable1 = DrawableCompat.wrap(drawable.mutate())
        if (tintColor != 0) {
            DrawableCompat.setTint(drawable, tintColor)
        }
        setImageDrawable(drawable1)
    }
}
