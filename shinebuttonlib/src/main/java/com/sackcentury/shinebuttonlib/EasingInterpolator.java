package com.sackcentury.shinebuttonlib;

import android.animation.TimeInterpolator;

import androidx.annotation.NonNull;

/**
 * @author xiongke
 * @date 2019-06-11
 */
public class EasingInterpolator implements TimeInterpolator {

    private final Ease ease;

    public EasingInterpolator(@NonNull Ease ease) {
        this.ease = ease;
    }

    @Override
    public float getInterpolation(float input) {
        return EasingProvider.get(this.ease, input);
    }

    public Ease getEase() {
        return ease;
    }
}