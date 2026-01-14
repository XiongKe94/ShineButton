package com.sackcentury

import java.lang.Math.pow
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * @author xiongke
 * @since 2026/1/14
 **/
object EasingProvider {
    /**
     * @param ease            Easing type
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    fun get(ease: Ease, elapsedTimeRate: Float): Float {
        return when (ease) {
            Ease.LINEAR -> elapsedTimeRate
            Ease.QUAD_IN -> getPowIn(elapsedTimeRate, 2.0)
            Ease.QUAD_OUT -> getPowOut(elapsedTimeRate, 2.0)
            Ease.QUAD_IN_OUT -> getPowInOut(elapsedTimeRate, 2.0)
            Ease.CUBIC_IN -> getPowIn(elapsedTimeRate, 3.0)
            Ease.CUBIC_OUT -> getPowOut(elapsedTimeRate, 3.0)
            Ease.CUBIC_IN_OUT -> getPowInOut(elapsedTimeRate, 3.0)
            Ease.QUART_IN -> getPowIn(elapsedTimeRate, 4.0)
            Ease.QUART_OUT -> getPowOut(elapsedTimeRate, 4.0)
            Ease.QUART_IN_OUT -> getPowInOut(elapsedTimeRate, 4.0)
            Ease.QUINT_IN -> getPowIn(elapsedTimeRate, 5.0)
            Ease.QUINT_OUT -> getPowOut(elapsedTimeRate, 5.0)
            Ease.QUINT_IN_OUT -> getPowInOut(elapsedTimeRate, 5.0)
            Ease.SINE_IN -> (1f - cos(elapsedTimeRate * PI.toFloat() / 2f))
            Ease.SINE_OUT -> sin(elapsedTimeRate * PI.toFloat() / 2f)
            Ease.SINE_IN_OUT -> (-0.5f * (cos(PI.toFloat() * elapsedTimeRate) - 1f))
            Ease.BACK_IN -> (elapsedTimeRate * elapsedTimeRate * ((1.7 + 1f) * elapsedTimeRate - 1.7)).toFloat()
            Ease.BACK_OUT -> {
                val t = elapsedTimeRate - 1f
                (t * t * ((1.7 + 1f) * t + 1.7) + 1f).toFloat()
            }
            Ease.BACK_IN_OUT -> getBackInOut(elapsedTimeRate, 1.7f)
            Ease.CIRC_IN -> -(sqrt(1f - elapsedTimeRate * elapsedTimeRate) - 1)
            Ease.CIRC_OUT -> {
                val t = elapsedTimeRate - 1f
                sqrt(1f - t * t)
            }
            Ease.CIRC_IN_OUT -> {
                var t = elapsedTimeRate * 2f
                if (t < 1f) {
                    (-0.5f * (sqrt(1f - t * t) - 1f))
                } else {
                    t -= 2f
                    (0.5f * (sqrt(1f - t * t) + 1f))
                }
            }
            Ease.BOUNCE_IN -> getBounceIn(elapsedTimeRate)
            Ease.BOUNCE_OUT -> getBounceOut(elapsedTimeRate)
            Ease.BOUNCE_IN_OUT -> {
                if (elapsedTimeRate < 0.5f) {
                    getBounceIn(elapsedTimeRate * 2f) * 0.5f
                } else {
                    getBounceOut(elapsedTimeRate * 2f - 1f) * 0.5f + 0.5f
                }
            }
            Ease.ELASTIC_IN -> getElasticIn(elapsedTimeRate, 1.0, 0.3)
            Ease.ELASTIC_OUT -> getElasticOut(elapsedTimeRate, 1.0, 0.3)
            Ease.ELASTIC_IN_OUT -> getElasticInOut(elapsedTimeRate, 1.0, 0.45)
        }
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
     */
    private fun getPowIn(elapsedTimeRate: Float, pow: Double): Float {
        return pow(elapsedTimeRate.toDouble(), pow).toFloat()
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
     */
    private fun getPowOut(elapsedTimeRate: Float, pow: Double): Float {
        return (1 - pow(1 - elapsedTimeRate.toDouble(), pow)).toFloat()
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
     */
    private fun getPowInOut(elapsedTimeRate: Float, pow: Double): Float {
        var t = elapsedTimeRate * 2
        return if (t < 1) {
            (0.5 * pow(t.toDouble(), pow)).toFloat()
        } else {
            (1 - 0.5 * abs(pow(2 - t.toDouble(), pow))).toFloat()
        }
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amount          amount The strength of the ease.
     * @return easedValue
     */
    private fun getBackInOut(elapsedTimeRate: Float, amount: Float): Float {
        val a = amount * 1.525f
        var t = elapsedTimeRate * 2
        return if (t < 1) {
            (0.5 * (t * t * ((a + 1) * t - a))).toFloat()
        } else {
            t -= 2
            (0.5 * (t * t * ((a + 1) * t + a) + 2)).toFloat()
        }
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    private fun getBounceIn(elapsedTimeRate: Float): Float {
        return 1f - getBounceOut(1f - elapsedTimeRate)
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    private fun getBounceOut(elapsedTimeRate: Float): Float {
        return when {
            elapsedTimeRate < 1 / 2.75 -> (7.5625 * elapsedTimeRate * elapsedTimeRate).toFloat()
            elapsedTimeRate < 2 / 2.75 -> {
                var t = elapsedTimeRate - 1.5f / 2.75f
                (7.5625 * t * t + 0.75).toFloat()
            }
            elapsedTimeRate < 2.5 / 2.75 -> {
                var t = elapsedTimeRate - 2.25f / 2.75f
                (7.5625 * t * t + 0.9375).toFloat()
            }
            else -> {
                var t = elapsedTimeRate - 2.625f / 2.75f
                (7.5625 * t * t + 0.984375).toFloat()
            }
        }
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
     */
    private fun getElasticIn(elapsedTimeRate: Float, amplitude: Double, period: Double): Float {
        if (elapsedTimeRate == 0f || elapsedTimeRate == 1f) return elapsedTimeRate
        val pi2 = PI * 2
        val s = period / pi2 * asin(1 / amplitude)
        val t = (elapsedTimeRate - 1f).toDouble()
        val result = -(amplitude * pow(2.0, 10.0 * t) * sin((t - s) * pi2 / period))
        return result.toFloat()
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
     */
    private fun getElasticOut(elapsedTimeRate: Float, amplitude: Double, period: Double): Float {
        if (elapsedTimeRate == 0f || elapsedTimeRate == 1f) return elapsedTimeRate

        val pi2 = PI * 2
        val s = period / pi2 * asin(1 / amplitude)
        val t = elapsedTimeRate.toDouble()
        return (amplitude * pow(2.0, -10.0 * t) * sin((t - s) * pi2 / period) + 1).toFloat()
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
     */
    private fun getElasticInOut(elapsedTimeRate: Float, amplitude: Double, period: Double): Float {
        val pi2 = PI * 2
        val s = period / pi2 * asin(1 / amplitude)
        var t = (elapsedTimeRate * 2).toDouble()
        return if (t < 1) {
            t -= 1.0
            (-0.5f * (amplitude * pow(2.0, 10.0 * t) * sin((t - s) * pi2 / period))).toFloat()
        } else {
            t -= 1.0
            (amplitude * pow(2.0, -10.0 * t) * sin((t - s) * pi2 / period) * 0.5 + 1).toFloat()
        }
    }
}
