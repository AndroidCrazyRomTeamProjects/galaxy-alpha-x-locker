package com.galaxytheme.effect;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

/* renamed from: com.galaxytheme.a.e */
/* loaded from: classes.dex */
public interface KeyguardEffectViewBase {
    void reset();

    void showUnlockAffordance(long j, Rect rect);

    void update(Bitmap bitmap);

    boolean handleTouchEventForPatternLock(MotionEvent motionEvent);

    boolean handleTouchEvent(View view, MotionEvent motionEvent);

    void screenTurnedOn();

    void handleUnlock(View view, MotionEvent motionEvent);

    void show();

    /* renamed from: d */
    void playLockSound();

    long getUnlockDelay();
}
