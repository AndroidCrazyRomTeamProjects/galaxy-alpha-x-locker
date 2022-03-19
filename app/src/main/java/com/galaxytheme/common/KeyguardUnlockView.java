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

    /* renamed from: b */
    protected ImageView f139b;
    protected WallpaperSurfaceView mWallpaperSurfaceView;
    protected GalaxyLockscreen mGalaxyLockscreen;
    protected Context mContext;

    /* renamed from: f */
    private View f143f;

    /* renamed from: g */
    private ObjectAnimator f144g;

    /* renamed from: h */
    private float f145h;

    /* renamed from: i */
    private float f146i;

    /* renamed from: j */
    private boolean f147j;

    /* renamed from: k */
    private int f148k;

    /* renamed from: l */
    private int f149l;

    public KeyguardUnlockView(Context context) {
        this(context, null);
    }

    public KeyguardUnlockView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyguardUnlockView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
        this.f148k = (int) context.getResources().getDimension(R.dimen.keyguard_lockscreen_first_border);
        this.f149l = (int) context.getResources().getDimension(R.dimen.keyguard_lockscreen_second_border);
    }

    /* renamed from: a */
    private void m120a(View view, float f) {
        if (view != null) {
            if (this.f144g != null) {
                this.f144g.cancel();
            }
            this.f144g = ObjectAnimator.ofFloat(view, "alpha", f);
            this.f144g.start();
        }
    }

    /* renamed from: b */
    private void m117b() {
        postDelayed(new Runnable() { // from class: com.galaxytheme.common.KeyguardUnlockView.1
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.i("KeyguardUnlockView", "finish, this = " + KeyguardUnlockView.this);
                KeyguardUnlockView.this.mGalaxyLockscreen.authenticate(true, KeyguardUnlockView.this);
            }
        }, getUnlockDelay());
    }

    /* renamed from: a */
    public void mo121a(Drawable drawable, boolean z) {
        LogUtil.i("KeyguardUnlockView", "setWallpaper, drawable = " + drawable + ", isLiveWallpaper = " + z);
    }

    /* renamed from: a */
    public void m118a(GalaxyLockscreen aVar, ImageView imageView) {
        this.mGalaxyLockscreen = aVar;
        this.f139b = imageView;
    }

    /* renamed from: a */
    public boolean m122a() {
        return this.f147j;
    }

    /* renamed from: a */
    public boolean m119a(View view, MotionEvent motionEvent) throws Settings.SettingNotFoundException {
        if (motionEvent.getAction() == 0) {
            m120a(this.f143f, 0.0f);
            this.f145h = motionEvent.getX();
            this.f146i = motionEvent.getY();
            if (this.f147j) {
                return true;
            }
        } else if (2 == motionEvent.getAction() && motionEvent.getActionIndex() == 0) {
            if (this.f147j) {
                return true;
            }
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            LogUtil.i("KeyguardUnlockView", "distance = " + Math.sqrt(((x - this.f145h) * (x - this.f145h)) + ((y - this.f146i) * (y - this.f146i))));
            if (Math.sqrt(((x - this.f145h) * (x - this.f145h)) + ((y - this.f146i) * (y - this.f146i))) > this.f149l) {
                this.mUnlockView.handleUnlock(this, motionEvent);
                this.f147j = true;
                m117b();
                return true;
            }
        } else if (1 == motionEvent.getAction()) {
            if (this.f147j) {
                return true;
            }
            float x2 = motionEvent.getX();
            float y2 = motionEvent.getY();
            LogUtil.i("KeyguardUnlockView", "distance = " + Math.sqrt(((x2 - this.f145h) * (x2 - this.f145h)) + ((y2 - this.f146i) * (y2 - this.f146i))));
            if (Math.sqrt(((x2 - this.f145h) * (x2 - this.f145h)) + ((y2 - this.f146i) * (y2 - this.f146i))) > this.f148k) {
                this.mUnlockView.handleUnlock(this, motionEvent);
                this.f147j = true;
                m117b();
                return true;
            }
            m120a(this.f143f, 1.0f);
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
        this.f147j = false;
        m120a(this.f143f, 1.0f);
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
            return m119a((View) this.mUnlockView, motionEvent);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
        }

    public void setFadeView(View view) {
        this.f143f = view;
    }

    public void setUnlockView(KeyguardEffectViewBase eVar) {
        if (!(eVar == null || ((View) eVar).getParent() == null)) {
            View view = (View) eVar;
            ((ViewGroup) view.getParent()).removeView(view);
        }
        this.mUnlockView = eVar;
    }

    public void setWindowInsets(Rect rect) {
        LogUtil.i("draw", "KeyguardUnlockView, setWindowInsets, mWallpaperSurface = " + this.mWallpaperSurfaceView);
        if (this.mWallpaperSurfaceView != null) {
            this.mWallpaperSurfaceView.setWindowInsets(rect);
        }
    }
}
