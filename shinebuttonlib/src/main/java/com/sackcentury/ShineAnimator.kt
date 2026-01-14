package com.sackcentury

import android.animation.ValueAnimator
import com.sackcentury.Ease

/**
 * @author xiongke
 * @since 2026/1/14
 **/
class ShineAnimator @JvmOverloads constructor(
    animDuration: Long = ANIM_DURATION,
    maxValue: Float = MAX_VALUE,
    startDelay: Long = DEFAULT_START_DELAY
) : ValueAnimator() {
    companion object {
        const val MAX_VALUE = 1.5f
        const val ANIM_DURATION = 1500L
        const val DEFAULT_START_DELAY = 200L
    }

    init {
        setFloatValues(1f, maxValue)
        duration = animDuration
        this.startDelay = startDelay
        interpolator = EasingInterpolator(Ease.QUART_OUT)
    }
}
