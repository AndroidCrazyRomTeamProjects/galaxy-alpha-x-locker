package com.galaxytheme.effect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.galaxytheme.common.GalaxyLockscreen;
import com.xlocker.core.sdk.GlobalSettings;

public class KeyguardEffectViewNone extends FrameLayout implements KeyguardEffectViewBase {

    /* renamed from: a */
    private final boolean f62a = true;
    private final String TAG = "VisualEffectCircleUnlockEffect";

    /* renamed from: c */
    private C0004b f64c;
    private Context mContext;

    /* renamed from: e */
    private SoundPool f66e;

    /* renamed from: f */
    private int f67f;

    @SuppressLint("LongLogTag")
    public KeyguardEffectViewNone(Context context, GalaxyLockscreen aVar) {
        super(context);
        Log.d("VisualEffectCircleUnlockEffect", "KeyguardEffectViewNone : Constructor");
        this.mContext = context;
        this.f66e = new SoundPool(10, GlobalSettings.getStreamType(this.mContext.getApplicationContext()), 0);
        this.f67f = this.f66e.load(this.mContext, com.galaxytheme.common.R.raw.unlock_none_effect, 0);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        if (i < i2) {
        }
        float f = i / 1080.0f;
        Log.d("VisualEffectCircleUnlockEffect", "screenWidth : " + i);
        Log.d("VisualEffectCircleUnlockEffect", "screenHeight : " + i2);
        Log.d("VisualEffectCircleUnlockEffect", "ratio : " + f);
        this.f64c = new C0004b(this.mContext, ((int) this.mContext.getResources().getDimension(com.galaxytheme.common.R.dimen.keyguard_lockscreen_camera_shortcut_circle_max_radius)) * 2, (int) (4.0f * f), (int) (6.0f * f), new int[] {
                com.galaxytheme.common.R.drawable.keyguard_none_lock_01,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_02,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_03,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_04,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_05,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_06,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_07,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_08,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_09,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_10,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_11,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_12,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_13,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_14,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_15,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_16,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_17,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_18,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_19,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_20,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_21,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_22,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_23,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_24,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_25,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_26,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_27,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_28,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_29,
                com.galaxytheme.common.R.drawable.keyguard_none_lock_30
        }
        , com.galaxytheme.common.R.drawable.keyguard_none_arrow);
        this.f64c.setLockscreen(aVar);
        addView(this.f64c);
    }

    /* renamed from: e */
    private void m158e() {
        if (GlobalSettings.isLockscreenSoundEnabled(this.mContext.getApplicationContext()) && GlobalSettings.isUnlockSoundEnabled(this.mContext.getApplicationContext()) && this.f66e != null) {
            this.f66e.play(this.f67f, GlobalSettings.getSoundVolume(this.mContext.getApplicationContext()), GlobalSettings.getSoundVolume(this.mContext.getApplicationContext()), 0, 0, 1.0f);
        }
    }

    @SuppressLint("LongLogTag")
    @Override // com.galaxytheme.p000a.AbstractC0012e
    /* renamed from: a */
    public void reset() {
        Log.d("VisualEffectCircleUnlockEffect", "KeyguardEffectViewNone : reset");
        if (this.f64c != null) {
            this.f64c.m175b();
        }
    }

    @SuppressLint("LongLogTag")
    @Override // com.galaxytheme.p000a.AbstractC0012e
    /* renamed from: a */
    public void showUnlockAffordance(long j, Rect rect) {
        Log.d("VisualEffectCircleUnlockEffect", "KeyguardEffectViewNone : showUnlockAffordance");
        if (this.f64c != null) {
            this.f64c.m184a(j, rect);
        }
    }

    @Override // com.galaxytheme.p000a.AbstractC0012e
    public void update(Bitmap bitmap) {
    }

    @Override // com.galaxytheme.p000a.AbstractC0012e
    public boolean handleTouchEventForPatternLock(MotionEvent motionEvent) {
        return false;
    }

    @Override // com.galaxytheme.p000a.AbstractC0012e
    public boolean handleTouchEvent(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            if (!(view instanceof KeyguardEffectViewBase)) {
                this.f64c.m187a(view.getWidth());
            } else {
                this.f64c.m189a();
            }
        }
        this.f64c.m180a(view, motionEvent);
        return true;
    }

    @SuppressLint("LongLogTag")
    @Override // com.galaxytheme.p000a.AbstractC0012e
    public void screenTurnedOn() {
        Log.d("VisualEffectCircleUnlockEffect", "KeyguardEffectViewNone : screenTurnedOn");
    }

    @SuppressLint("LongLogTag")
    @Override // com.galaxytheme.p000a.AbstractC0012e
    /* renamed from: b */
    public void handleUnlock(View view, MotionEvent motionEvent) {
        Log.d("VisualEffectCircleUnlockEffect", "KeyguardEffectViewNone : handleUnlock");
        if (this.f64c != null) {
            this.f64c.m172c();
        }
        m158e();
    }

    @SuppressLint("LongLogTag")
    @Override // com.galaxytheme.p000a.AbstractC0012e
    /* renamed from: c */
    public void show() {
        Log.d("VisualEffectCircleUnlockEffect", "KeyguardEffectViewNone : show");
        if (this.f64c != null) {
            this.f64c.m175b();
        }
    }

    @Override // com.galaxytheme.p000a.AbstractC0012e
    public void playLockSound() {
    }

    @Override // com.galaxytheme.p000a.AbstractC0012e
    public long getUnlockDelay() {
        return 0L;
    }

    public void setHidden(boolean z) {
        if (this.f64c != null) {
            this.f64c.m175b();
        }
    }
}
