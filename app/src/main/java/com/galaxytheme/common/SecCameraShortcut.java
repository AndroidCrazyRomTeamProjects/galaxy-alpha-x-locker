package com.galaxytheme.common;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.galaxytheme.effect.KeyguardEffectViewBase;
import com.galaxytheme.effect.KeyguardEffectViewNone;
import com.xlocker.core.sdk.KeyguardSecurityCallback;
import com.xlocker.core.sdk.LockPatternUtils;
import com.xlocker.core.sdk.Lockscreen;

/* loaded from: classes.dex */
public class SecCameraShortcut extends FrameLayout implements KeyguardSecurityCallback.OnSecurityResult {

    /* renamed from: a */
    InputMethodManager f151a;

    /* renamed from: c */
    private ImageView f153c;

    /* renamed from: f */
    private int f156f;

    /* renamed from: h */
    private OrientationEventListener f158h;

    /* renamed from: i */
    private PowerManager f159i;

    /* renamed from: j */
    private RotateAnimation f160j;

    /* renamed from: k */
    private int f161k;

    /* renamed from: l */
    private float f162l;

    /* renamed from: m */
    private float f163m;

    /* renamed from: n */
    private KeyguardEffectViewBase f164n;

    /* renamed from: o */
    private KeyguardEffectViewNone f165o;

    /* renamed from: p */
    private Context f166p;

    /* renamed from: q */
    private Lockscreen f167q;

    /* renamed from: b */
    private String TAG = "SecCameraShortcut";

    /* renamed from: d */
    private float f154d = 0.0f;

    /* renamed from: e */
    private double f155e = 0.0d;

    /* renamed from: g */
    private boolean f157g = true;

    /* renamed from: r */
    private LockPatternUtils f168r = new LockPatternUtils(getContext());

    public SecCameraShortcut(Context context) {
        super(context);
        this.f166p = context;
    }

    public SecCameraShortcut(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f166p = context;
    }

    public SecCameraShortcut(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f166p = context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: a */
    public void m116a() {
        if (this.f168r.isSecure()) {
            try {
                Intent intent = new Intent("android.media.action.STILL_IMAGE_CAMERA_SECURE");
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                getContext().startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getContext(), "No camera found on your phone.", Toast.LENGTH_SHORT);
            }
            this.f157g = true;
            return;
        }
        this.f167q.authenticate(true, this);
    }

    /* renamed from: a */
    private void m114a(View view, MotionEvent motionEvent) {
        if (this.f165o != null) {
            Log.d(this.TAG, "mAdditionalUnlockView != null");
            this.f165o.handleTouchEvent(view, motionEvent);
            return;
        }
        Log.d(this.TAG, "mAdditionalUnlockView == null");
        this.f164n.handleTouchEvent(view, motionEvent);
    }

    /* renamed from: b */
    private void m112b(View view, MotionEvent motionEvent) {
        if (this.f165o != null) {
            Log.d(this.TAG, "mAdditionalUnlockView != null");
            this.f165o.handleUnlock(view, motionEvent);
            this.f165o.reset();
            return;
        }
        Log.d(this.TAG, "mAdditionalUnlockView == null");
        this.f164n.handleUnlock(view, motionEvent);
    }

    private long getUnlockDelay() {
        if (this.f165o != null) {
            return this.f165o.getUnlockDelay();
        }
        Log.d(this.TAG, "mAdditionalUnlockView == null");
        return this.f164n.getUnlockDelay();
    }

    /* renamed from: a */
    protected void m115a(float f) {
        float f2 = 360.0f;
        if (this.f154d != f) {
            float f3 = this.f154d;
            if (f3 == 0.0f && f == 270.0f) {
                f3 = 360.0f;
                f2 = f;
            } else if (!(f3 == 270.0f && f == 0.0f)) {
                f2 = f;
            }
            this.f160j = new RotateAnimation(f3, f2, 1, 0.5f, 1, 0.5f);
            this.f160j.setDuration(300L);
            this.f160j.setFillAfter(true);
            this.f153c.startAnimation(this.f160j);
            this.f154d = f;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.f158h == null) {
            this.f158h = new OrientationEventListener(getContext()) { // from class: com.galaxytheme.common.SecCameraShortcut.1
                @Override // android.view.OrientationEventListener
                public void onOrientationChanged(int i) {
                    if (i > 325 || 35 > i) {
                        SecCameraShortcut.this.m115a(0.0f);
                    } else if (i >= 55 && 125 > i) {
                        SecCameraShortcut.this.m115a(270.0f);
                    } else if (i >= 145 && 215 > i) {
                        SecCameraShortcut.this.m115a(180.0f);
                    } else if (i >= 235 && 305 > i) {
                        SecCameraShortcut.this.m115a(90.0f);
                    }
                }
            };
        }
        this.f158h.enable();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.f158h != null) {
            this.f158h.disable();
            this.f158h = null;
        }
    }

    public void onFailed() {
        this.f157g = true;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.f159i = (PowerManager) this.f166p.getSystemService(Context.POWER_SERVICE);
        this.f151a = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        this.f153c = (ImageView) findViewById(com.galaxytheme.common.R.id.camera_button);
        this.f156f = (int) getContext().getResources().getDimension(com.galaxytheme.common.R.dimen.keyguard_lockscreen_first_border);
        this.f161k = (int) getContext().getResources().getDimension(com.galaxytheme.common.R.dimen.keyguard_lockscreen_second_border);
    }

    public void onSuccess() {
        try {
            Intent intent = new Intent("android.media.action.STILL_IMAGE_CAMERA");
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            getContext().startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "No camera found on your phone.", Toast.LENGTH_SHORT);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Log.d(this.TAG, "touched, mIsFirst = " + this.f157g);
        if (!this.f157g) {
            return false;
        }
        float x = motionEvent.getX(0);
        float y = motionEvent.getY(0);
        switch (motionEvent.getAction()) {
            case 0:
                Log.e(this.TAG, "action down");
                this.f162l = x;
                this.f163m = y;
                this.f155e = 0.0d;
                this.f153c.setImageResource(com.galaxytheme.common.R.drawable.camera_press);
                break;
            case 1:
            case 3:
                Log.e(this.TAG, "action up/cancel mDistance: " + this.f155e);
                if ((this.f156f < this.f155e && this.f155e < this.f161k) || this.f155e >= this.f161k) {
                    this.f157g = false;
                    m112b(null, motionEvent);
                    postDelayed(new Runnable() { // from class: com.galaxytheme.common.SecCameraShortcut.2
                        @Override // java.lang.Runnable
                        public void run() {
                            SecCameraShortcut.this.m116a();
                            SecCameraShortcut.this.f153c.setImageResource(com.galaxytheme.common.R.drawable.camera_default);
                        }
                    }, this.f164n.getUnlockDelay());
                }
                this.f153c.setImageResource(com.galaxytheme.common.R.drawable.camera_default);
                break;
            case 2:
                this.f155e = Math.sqrt(Math.pow((int) (y - this.f163m), 2.0d) + Math.pow((int) (x - this.f162l), 2.0d));
                Log.d(this.TAG, "ACTION_MOVE mDistance: " + this.f155e);
                if (this.f155e >= this.f161k) {
                    this.f157g = false;
                    m112b(null, motionEvent);
                    postDelayed(new Runnable() { // from class: com.galaxytheme.common.SecCameraShortcut.3
                        @Override // java.lang.Runnable
                        public void run() {
                            SecCameraShortcut.this.m116a();
                            SecCameraShortcut.this.f153c.setImageResource(com.galaxytheme.common.R.drawable.camera_default);
                        }
                    }, this.f164n.getUnlockDelay());
                }
                if (getHeight() / 2 >= this.f155e) {
                    this.f153c.setImageResource(com.galaxytheme.common.R.drawable.camera_press);
                    break;
                } else {
                    this.f153c.setImageResource(com.galaxytheme.common.R.drawable.camera_swipe);
                    break;
                }
        }
        setTag("ShortcutWidget");
        m114a(this.f153c, motionEvent);
        return true;
    }

    public void setAdditionalUnlockView(KeyguardEffectViewNone cVar) {
        this.f165o = cVar;
    }

    public void setLockscreen(Lockscreen lockscreen) {
        this.f167q = lockscreen;
    }

    public void setUnlockView(KeyguardEffectViewBase eVar) {
        this.f164n = eVar;
    }
}
