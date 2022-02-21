package com.galaxytheme.common;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.galaxytheme.brilliantring.R;
import com.galaxytheme.effect.KeyguardEffectViewNone;
import com.galaxytheme.effect.KeyguardEffectViewBase;
import com.xlocker.core.sdk.Lockscreen;
import com.xlocker.core.sdk.widget.CarrierText;

public abstract class GalaxyLockscreen extends Lockscreen {
    private static final String TAG = "GalaxyLockscreen";
    private View mGalaxyView;
    private GalaxyKeyguardShortcutView mKeyguardShortcutView;
    private KeyguardUnlockView mKeyguardUnlockView;
    protected KeyguardEffectViewBase mUnLockView;

    public GalaxyLockscreen(Context context, Context context2) {
        super(context, context2);
    }

    protected abstract KeyguardEffectViewBase createUnlockView();

    protected View findViewById(int i) {
        return this.mGalaxyView.findViewById(i);
    }

    public Drawable getDefaultWallpaper() {
        return getThemeContext().getResources().getDrawable(R.drawable.default_wallpaper);
    }

    public FrameLayout getForegroundLayer() {
        return GalaxyLockscreen.super.getForegroundLayer();
    }

    public FrameLayout getHostView() {
        return GalaxyLockscreen.super.getHostView();
    }

    protected KeyguardUnlockView getKeyguardUnlockView() {
        return new KeyguardUnlockView(getThemeContext());
    }

    public void onActivityCreated() {
        GalaxyLockscreen.super.onActivityCreated();
        View inflate = LayoutInflater.from(getThemeContext()).inflate(R.layout.galaxy_lockscreen, (ViewGroup) null);
        addHomePage(inflate);
        this.mGalaxyView = inflate;
        setCameraWidget((ImageView) LayoutInflater.from(getThemeContext()).inflate(R.layout.galaxy_camera_widget, (ViewGroup) null), 84, 42, 84, 42);
        this.mKeyguardUnlockView = getKeyguardUnlockView();
        getUnlockLayer().addView(this.mKeyguardUnlockView);
        this.mKeyguardUnlockView.m118a(this, (ImageView) getWallpaperView());
        this.mKeyguardUnlockView.setFadeView((CarrierText) inflate.findViewById(R.id.carrier_text));
        Log.i("lockscreen", "GalaxyKeyguardShortcutView.classLoader = " + GalaxyKeyguardShortcutView.class.getClassLoader() + "@" + GalaxyKeyguardShortcutView.class.getClassLoader().hashCode() + ", view.classLoader = " + inflate.findViewById(R.id.keyguard_shortcutview).getClass().getClassLoader() + "@" + inflate.findViewById(R.id.keyguard_shortcutview).getClass().getClassLoader().hashCode());
        this.mKeyguardShortcutView = (GalaxyKeyguardShortcutView) inflate.findViewById(R.id.keyguard_shortcutview);
        this.mKeyguardShortcutView.setUnlockView(this.mKeyguardUnlockView);
        this.mKeyguardShortcutView.setRootContainer(getHostView());
        ((TextView) inflate.findViewById(R.id.help_text)).setVisibility(GalaxySettings.isShowHelpText(getThemeContext()) ? View.VISIBLE : View.GONE);
        this.mUnLockView = createUnlockView();
        if (this.mUnLockView != null) {
            this.mUnLockView.reset();
            this.mUnLockView.show();
            if (this.mKeyguardUnlockView != null) {
                this.mKeyguardUnlockView.setUnlockView(this.mUnLockView);
            }
            updateLockscreenWallpaper();
        }
        LayoutInflater.from(getThemeContext()).inflate(R.layout.keyguard_securityview_overlay_layout, (ViewGroup) findViewById(R.id.keyguard_security_container), true);
        SecCameraShortcut secCameraShortcut = (SecCameraShortcut) findViewById(R.id.sec_camera_shortcut);
        if (GalaxySettings.isShowCameraShortcut(getThemeContext())) {
            KeyguardEffectViewNone cVar = new KeyguardEffectViewNone(getThemeContext(), this);
            secCameraShortcut.setAdditionalUnlockView(cVar);
            getUnlockLayer().addView(cVar);
            secCameraShortcut.setUnlockView(this.mUnLockView);
            secCameraShortcut.setLockscreen(this);
            return;
        }
        findViewById(R.id.keyguard_selector_fade_container).setVisibility(View.GONE);
    }

    public void onActivityDestroyed() {
        GalaxyLockscreen.super.onActivityDestroyed();
        this.mKeyguardUnlockView.setUnlockView(null);
    }

    public void onInsetChanged(Rect rect) {
        if (this.mKeyguardUnlockView != null) {
            this.mKeyguardUnlockView.setWindowInsets(rect);
        }
    }

    public void onScreenTurnedOff() {
        if (this.mUnLockView != null) {
            this.mUnLockView.reset();
        }
        GalaxyLockscreen.super.onScreenTurnedOff();
    }

    public void onScreenTurnedOn() {
        if (this.mUnLockView != null) {
            this.mUnLockView.screenTurnedOn();
            Display defaultDisplay = ((WindowManager) getThemeContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int width = defaultDisplay.getWidth();
            int height = defaultDisplay.getHeight();
            Rect rect = new Rect();
            if (width < height) {
                rect.left = 0;
                rect.top = height / 2;
                rect.right = width;
                rect.bottom = height;
            } else {
                rect.left = width / 2;
                rect.top = 0;
                rect.right = width;
                rect.bottom = height;
            }
            this.mUnLockView.showUnlockAffordance(300L, rect);
        }
        GalaxyLockscreen.super.onScreenTurnedOn();
    }

    public void onWallpaperUpdated(Drawable drawable, boolean z) {
        if (this.mKeyguardUnlockView != null) {
            this.mKeyguardUnlockView.mo121a(drawable, z);
        }
    }
}
