package com.galaxytheme.common;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.galaxytheme.brilliantring.R;
import com.galaxytheme.effect.KeyguardEffectViewBase;
import com.xlocker.core.sdk.KeyguardSecurityCallback;
import com.xlocker.core.sdk.LogUtil;

public class KeyguardUnlockView extends FrameLayout implements KeyguardSecurityView, KeyguardSecurityCallback.OnSecurityResult {
    protected KeyguardEffectViewBase mUnlockView;
    protected ImageView wallpaperImageView;
    protected WallpaperSurfaceView mWallpaperSurfaceView;
    protected GalaxyLockscreen mGalaxyLockscreen;
    protected Context mContext;

    private View fadeView;
    private ObjectAnimator fadeAnimator;
    private float downX;
    private float downY;
    private boolean hasTriggeredUnlock;
    private int unlockDistance;
    private int unlockPreviewDistance;

    public KeyguardUnlockView(Context context) {
        this(context, null);
    }

    public KeyguardUnlockView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyguardUnlockView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
        this.unlockDistance = (int) context.getResources().getDimension(R.dimen.keyguard_lockscreen_first_border);
        this.unlockPreviewDistance = (int) context.getResources().getDimension(R.dimen.keyguard_lockscreen_second_border);
    }

    private void animateViewAlpha(View view, float targetAlpha) {
        if (view != null) {
            if (this.fadeAnimator != null) {
                this.fadeAnimator.cancel();
            }
            this.fadeAnimator = ObjectAnimator.ofFloat(view, "alpha", targetAlpha);
            this.fadeAnimator.start();
        }
    }

    private void triggerUnlock() {
        postDelayed(new Runnable() { // from class: com.galaxytheme.common.KeyguardUnlockView.1
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.i("KeyguardUnlockView", "finish, this = " + KeyguardUnlockView.this);
                KeyguardUnlockView.this.mGalaxyLockscreen.authenticate(true, KeyguardUnlockView.this);
            }
        }, getUnlockDelay());
    }

    public void updateLockscreenWallpaper(Drawable drawable, boolean isLiveWallpaper) {
        LogUtil.i("KeyguardUnlockView", "setWallpaper, drawable = " + drawable + ", isLiveWallpaper = " + isLiveWallpaper);
    }

    public void initialize(GalaxyLockscreen galaxyLockscreen, ImageView wallpaperImageView) {
        this.mGalaxyLockscreen = galaxyLockscreen;
        this.wallpaperImageView = wallpaperImageView;
    }

    public boolean hasTriggeredUnlock() {
        return this.hasTriggeredUnlock;
    }

    public boolean handleTouchEvent(View view, MotionEvent motionEvent) throws Settings.SettingNotFoundException {
        if (motionEvent.getAction() == 0) {
            animateViewAlpha(this.fadeView, 0.0f);
            this.downX = motionEvent.getX();
            this.downY = motionEvent.getY();
            if (this.hasTriggeredUnlock) {
                return true;
            }
        } else if (2 == motionEvent.getAction() && motionEvent.getActionIndex() == 0) {
            if (this.hasTriggeredUnlock) {
                return true;
            }
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            LogUtil.i("KeyguardUnlockView", "distance = " + Math.sqrt(((x - this.downX) * (x - this.downX)) + ((y - this.downY) * (y - this.downY))));
            if (Math.sqrt(((x - this.downX) * (x - this.downX)) + ((y - this.downY) * (y - this.downY))) > this.unlockPreviewDistance) {
                this.mUnlockView.handleUnlock(this, motionEvent);
                this.hasTriggeredUnlock = true;
                triggerUnlock();
                return true;
            }
        } else if (1 == motionEvent.getAction()) {
            if (this.hasTriggeredUnlock) {
                return true;
            }
            float x2 = motionEvent.getX();
            float y2 = motionEvent.getY();
            LogUtil.i("KeyguardUnlockView", "distance = " + Math.sqrt(((x2 - this.downX) * (x2 - this.downX)) + ((y2 - this.downY) * (y2 - this.downY))));
            if (Math.sqrt(((x2 - this.downX) * (x2 - this.downX)) + ((y2 - this.downY) * (y2 - this.downY))) > this.unlockDistance) {
                this.mUnlockView.handleUnlock(this, motionEvent);
                this.hasTriggeredUnlock = true;
                triggerUnlock();
                return true;
            }
            animateViewAlpha(this.fadeView, 1.0f);
        }
        return this.mUnlockView != null ? this.mUnlockView.handleTouchEvent(view, motionEvent) : super.onTouchEvent(motionEvent);
    }

    public long getUnlockDelay() {
        if (this.mUnlockView == null) {
            return 0L;
        }
        return this.mUnlockView.getUnlockDelay();
    }

    public void onFailed() {
        if (this.mUnlockView != null) {
            this.mUnlockView.reset();
        }
        this.hasTriggeredUnlock = false;
        animateViewAlpha(this.fadeView, 1.0f);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        if (this.mUnlockView != null) {
            return this.mUnlockView.handleTouchEventForPatternLock(motionEvent);
        }
        return false;
    }

    public void onSuccess() {
        LogUtil.i("KeyguardUnlockView", "onSuccess, this = " + this);
        if (this.mUnlockView != null) {
            this.mUnlockView.playLockSound();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        try {
            return handleTouchEvent((View) this.mUnlockView, motionEvent);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setFadeView(View view) {
        this.fadeView = view;
    }

    public void setUnlockView(KeyguardEffectViewBase unlockView) {
        if (!(unlockView == null || ((View) unlockView).getParent() == null)) {
            View view = (View) unlockView;
            ((ViewGroup) view.getParent()).removeView(view);
        }
        this.mUnlockView = unlockView;
    }

    public void setWindowInsets(Rect rect) {
        LogUtil.i("draw", "KeyguardUnlockView, setWindowInsets, mWallpaperSurface = " + this.mWallpaperSurfaceView);
        if (this.mWallpaperSurfaceView != null) {
            this.mWallpaperSurfaceView.setWindowInsets(rect);
        }
    }
}
