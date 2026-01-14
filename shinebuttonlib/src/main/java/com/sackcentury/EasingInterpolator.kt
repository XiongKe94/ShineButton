package com.sackcentury

import android.animation.TimeInterpolator
import com.sackcentury.Ease

/**
 * @author xiongke
 * @since 2026/1/14
 **/
class EasingInterpolator(private val ease: Ease) : TimeInterpolator {

    override fun getInterpolation(input: Float): Float {
        return EasingProvider.get(ease, input)
    }
}
