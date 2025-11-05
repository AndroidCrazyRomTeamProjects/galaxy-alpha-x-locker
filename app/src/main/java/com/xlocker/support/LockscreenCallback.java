package com.xlocker.support;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.galaxytheme.brilliantring.R;
import com.xlocker.core.sdk.KeyguardSecurityCallback;
import com.xlocker.core.sdk.OnPageMoveListener;

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
            this.mActivity.showDownloadDialog(onSecurityResult);
        }
    }

    public Context getAppContext() {
        return this.mActivity;
    }

    public View getContentView() {
        return this.mActivity.findViewById(R.id.content_view);
    }

    public FrameLayout getForegroundLayer() {
        return this.mActivity.findViewById(R.id.foreground_layer);
    }

    public FrameLayout getHostView() {
        return this.mActivity.findViewById(R.id.host_view);
    }

    public int getStatusBarHeight(Context context) {
        return 0;
    }

    public Context getThemeContext() {
        return this.mActivity;
    }

    public FrameLayout getUnlockLayer() {
        return this.mActivity.findViewById(R.id.unlock_layer);
    }

    public View getWallpaperView() {
        return this.mActivity.findViewById(R.id.wallpaper);
    }

    public void insertTopLevelView(ViewGroup viewGroup) {
        ViewGroup viewGroup2 = (ViewGroup) this.mActivity.rootView;
        View findViewById = viewGroup2.findViewById(R.id.content_view);
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
