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

import com.galaxytheme.brilliantring.R;
import com.galaxytheme.common.GalaxyLockscreen;
import com.xlocker.core.sdk.GlobalSettings;

public class KeyguardEffectViewNone extends FrameLayout implements KeyguardEffectViewBase {

    /* renamed from: a */
    private final boolean f62a = true;
    private final String TAG = "VisualEffectCircleUnlockEffect";
    private LockSequenceImage mLockSequenceImage;
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
        this.f67f = this.f66e.load(this.mContext, R.raw.unlock_none_effect, 0);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        if (i < i2) {
        }
        float f = i / 1080.0f;
        Log.d("VisualEffectCircleUnlockEffect", "screenWidth : " + i);
        Log.d("VisualEffectCircleUnlockEffect", "screenHeight : " + i2);
        Log.d("VisualEffectCircleUnlockEffect", "ratio : " + f);
        this.mLockSequenceImage = new LockSequenceImage(this.mContext, ((int) this.mContext.getResources().getDimension(R.dimen.keyguard_lockscreen_camera_shortcut_circle_max_radius)) * 2, (int) (4.0f * f), (int) (6.0f * f), new int[] {
                R.drawable.keyguard_none_lock_01,
                R.drawable.keyguard_none_lock_02,
                R.drawable.keyguard_none_lock_03,
                R.drawable.keyguard_none_lock_04,
                R.drawable.keyguard_none_lock_05,
                R.drawable.keyguard_none_lock_06,
                R.drawable.keyguard_none_lock_07,
                R.drawable.keyguard_none_lock_08,
                R.drawable.keyguard_none_lock_09,
                R.drawable.keyguard_none_lock_10,
                R.drawable.keyguard_none_lock_11,
                R.drawable.keyguard_none_lock_12,
                R.drawable.keyguard_none_lock_13,
                R.drawable.keyguard_none_lock_14,
                R.drawable.keyguard_none_lock_15,
                R.drawable.keyguard_none_lock_16,
                R.drawable.keyguard_none_lock_17,
                R.drawable.keyguard_none_lock_18,
                R.drawable.keyguard_none_lock_19,
                R.drawable.keyguard_none_lock_20,
                R.drawable.keyguard_none_lock_21,
                R.drawable.keyguard_none_lock_22,
                R.drawable.keyguard_none_lock_23,
                R.drawable.keyguard_none_lock_24,
                R.drawable.keyguard_none_lock_25,
                R.drawable.keyguard_none_lock_26,
                R.drawable.keyguard_none_lock_27,
                R.drawable.keyguard_none_lock_28,
                R.drawable.keyguard_none_lock_29,
                R.drawable.keyguard_none_lock_30
        }
        , R.drawable.keyguard_none_arrow);
        this.mLockSequenceImage.setLockscreen(aVar);
        addView(this.mLockSequenceImage);
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
        if (this.mLockSequenceImage != null) {
            this.mLockSequenceImage.m175b();
        }
    }

    @SuppressLint("LongLogTag")
    @Override // com.galaxytheme.p000a.AbstractC0012e
    /* renamed from: a */
    public void showUnlockAffordance(long j, Rect rect) {
        Log.d("VisualEffectCircleUnlockEffect", "KeyguardEffectViewNone : showUnlockAffordance");
        if (this.mLockSequenceImage != null) {
            this.mLockSequenceImage.m184a(j, rect);
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
                this.mLockSequenceImage.m187a(view.getWidth());
            } else {
                this.mLockSequenceImage.m189a();
            }
        }
        this.mLockSequenceImage.m180a(view, motionEvent);
        return true;
    }

    @SuppressLint("LongLogTag")
    @Override // com.galaxytheme.p000a.AbstractC0012e
    public void screenTurnedOn() {
        Log.d("VisualEffectCircleUnlockEffect", "KeyguardEffectViewNone : screenTurnedOn");
    }

    @SuppressLint("LongLogTag")
    @Override // com.galaxytheme.p000a.AbstractC0012e
    public void handleUnlock(View view, MotionEvent motionEvent) {
        Log.d("VisualEffectCircleUnlockEffect", "KeyguardEffectViewNone : handleUnlock");
        if (this.mLockSequenceImage != null) {
            this.mLockSequenceImage.m172c();
        }
        m158e();
    }

    @SuppressLint("LongLogTag")
    @Override // com.galaxytheme.p000a.AbstractC0012e
    public void show() {
        Log.d("VisualEffectCircleUnlockEffect", "KeyguardEffectViewNone : show");
        if (this.mLockSequenceImage != null) {
            this.mLockSequenceImage.m175b();
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
        if (this.mLockSequenceImage != null) {
            this.mLockSequenceImage.m175b();
        }
    }
}
