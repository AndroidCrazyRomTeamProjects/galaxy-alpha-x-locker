package com.xlocker.support;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.xlocker.core.sdk.KeyguardSecurityCallback;
import com.xlocker.core.sdk.OnPageMoveListener;

/* loaded from: classes.dex */
public class LockscreenCallback extends com.xlocker.core.sdk.LockscreenCallback {
    private final ThemeMainActivity mActivity;

    public LockscreenCallback(ThemeMainActivity themeMainActivity) {
        this.mActivity = themeMainActivity;
    }

    public void addHomePage(View view) {
        getHostView().addView(view);
    }

    public void authenticate(boolean z, KeyguardSecurityCallback.OnSecurityResult onSecurityResult) {
        if (onSecurityResult != null) {
            this.mActivity.f284a = onSecurityResult;
            this.mActivity.f286c.setCancelable(true);
            this.mActivity.f286c.show();
        }
    }

    public Context getAppContext() {
        return this.mActivity;
    }

    public View getContentView() {
        return this.mActivity.findViewById(com.xlocker.support.R.id.content_view);
    }

    public FrameLayout getForegroundLayer() {
        return (FrameLayout) this.mActivity.findViewById(com.xlocker.support.R.id.foreground_layer);
    }

    public FrameLayout getHostView() {
        return (FrameLayout) this.mActivity.findViewById(com.xlocker.support.R.id.host_view);
    }

    public int getStatusBarHeight(Context context) {
        return 0;
    }

    public Context getThemeContext() {
        return this.mActivity;
    }

    public FrameLayout getUnlockLayer() {
        return (FrameLayout) this.mActivity.findViewById(com.xlocker.support.R.id.unlock_layer);
    }

    public View getWallpaperView() {
        return this.mActivity.findViewById(com.xlocker.support.R.id.wallpaper);
    }

    public void insertTopLevelView(ViewGroup viewGroup) {
        ViewGroup viewGroup2 = (ViewGroup) this.mActivity.f285b;
        View findViewById = viewGroup2.findViewById(com.xlocker.support.R.id.content_view);
        viewGroup2.removeView(findViewById);
        viewGroup.addView(findViewById);
        viewGroup2.addView(viewGroup, 0);
    }

    public void setCameraWidget(View view, int i, int i2, int i3, int i4) {
    }

    public void setOnPageMoveListener(OnPageMoveListener onPageMoveListener) {
    }

    public void updateLockscreenWallpaper() {
    }
}
