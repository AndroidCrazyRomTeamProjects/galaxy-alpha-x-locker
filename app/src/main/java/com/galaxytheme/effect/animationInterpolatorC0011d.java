package com.galaxytheme.effect;

import android.view.animation.Interpolator;

/* renamed from: com.galaxytheme.a.d  reason: invalid class name */
/* loaded from: classes.dex */
public class animationInterpolatorC0011d implements Interpolator {
    /* renamed from: a */
    private float m157a(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2 * f2 * f2) + 1.0f;
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float f) {
        return m157a(f);
    }
}
