package com.galaxytheme.brilliantring;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.galaxytheme.common.KeyguardUnlockView;
import com.galaxytheme.effect.KeyguardEffectViewBase;

/**
 * Brilliant Ring specific implementation of {@link KeyguardUnlockView}.
 */
public class BrilliantRingUnlockView extends KeyguardUnlockView {

    public BrilliantRingUnlockView(Context context) {
        this(context, null);
    }

    public BrilliantRingUnlockView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BrilliantRingUnlockView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    @Override
    public void updateLockscreenWallpaper(Drawable wallpaperDrawable, boolean isLiveWallpaper) {
        super.updateLockscreenWallpaper(wallpaperDrawable, isLiveWallpaper);
        this.wallpaperImageView.setImageDrawable(wallpaperDrawable);
        if (this.mUnlockView != null && wallpaperDrawable instanceof BitmapDrawable) {
            this.mUnlockView.update(((BitmapDrawable) wallpaperDrawable).getBitmap());
        }
    }

    @Override
    public void setUnlockView(KeyguardEffectViewBase unlockView) {
        super.setUnlockView(unlockView);
        if (unlockView != null) {
            addView((View) this.mUnlockView);
        }
    }
}
