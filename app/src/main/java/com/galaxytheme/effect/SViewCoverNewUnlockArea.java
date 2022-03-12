package com.galaxytheme.effect;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.galaxytheme.common.GalaxyLockscreen;
import com.xlocker.core.sdk.widget.KeyguardShortcutView;

/* renamed from: com.galaxytheme.a.b */
/* loaded from: classes.dex */
// seams to be an s view cover thing? basted on SViewCoverNewUnlockArea.java found on SystemUI
public class SViewCoverNewUnlockArea extends FrameLayout {

    /* renamed from: C */
    private float f17C;

    /* renamed from: D */
    private int f18D;

    /* renamed from: H */
    private ImageView f22H;

    /* renamed from: I */
    private int[] f23I;

    /* renamed from: J */
    private int f24J;
    private Context mContext;

    /* renamed from: L */
    private int f26L;

    /* renamed from: M */
    private float f27M;

    /* renamed from: N */
    private float f28N;
    private GalaxyLockscreen mGalaxyLockscreen;

    /* renamed from: a */
    float f31a;

    /* renamed from: b */
    float f32b;
    /* renamed from: k */
    private ImageView f41k;

    /* renamed from: l */
    private float f42l;

    /* renamed from: n */
    private ValueAnimator f44n;

    /* renamed from: o */
    private int f45o;
    private SViewCoverUnlockCircleEffect mSViewCoverUnlockCircleEffect;

    /* renamed from: q */
    private float f47q;

    private FrameLayout mFrameLayout;
    private ValueAnimator mCameraCircleInAnimator;
    private ValueAnimator mCameraCircleOutAnimator;

    /* renamed from: v */
    private int f52v;

    /* renamed from: w */
    private int f53w;

    /* renamed from: x */
    private int f54x;

    /* renamed from: y */
    private int f55y;

    /* renamed from: z */
    private int f56z;

    /* renamed from: c */
    private final int f33c = 500;

    /* renamed from: d */
    private final boolean f34d = true;

    /* renamed from: e */
    private final int f35e = 666;

    /* renamed from: f */
    private final int f36f = 666;

    /* renamed from: g */
    private final int f37g = 333;

    /* renamed from: h */
    private final int f38h = 700;

    /* renamed from: i */
    private final int f39i = -200;
    private final String TAG = "VisualEffectCircleUnlockEffect";

    /* renamed from: m */
    private boolean f43m = false;
    private float circleAnimationMin = 0.0f;

    /* renamed from: A */
    private int f15A = 0;

    /* renamed from: B */
    private float f16B = 0.0f;

    /* renamed from: E */
    private boolean f19E = false;

    /* renamed from: F */
    private boolean f20F = false;

    /* renamed from: G */
    private boolean f21G = false;
    private float strokeAnimationValue = 0.0f;

    @SuppressLint("LongLogTag")
    public SViewCoverNewUnlockArea(Context context, int i, int i2, int i3, int[] iArr, int i4) {
        super(context);
        Log.d(TAG, "Constructor");
        this.mContext = context;
        this.f45o = i4;
        this.f54x = i;
        this.f56z = (m186a(i4, true) - i3) - 4;
        this.f26L = i2;
        this.f18D = i3;
        this.f52v = this.f54x;
        this.f55y = this.f56z / 2;
        this.f53w = this.f54x / 2;
        this.f23I = iArr;
        this.f24J = iArr.length;
        Log.d(TAG, "arrowImageId = " + i4);
        Log.d(TAG, "circleUnlockMaxWidth = " + this.f54x);
        Log.d(TAG, "circleUnlockMinWidth = " + this.f56z);
        Log.d(TAG, "outerStrokeWidth = " + i2);
        Log.d(TAG, "innerStrokeWidth = " + i3);
        Log.d(TAG, "lockSequenceTotal = " + this.f24J);
        m163g();
        setAllAnimator();
    }

    /* renamed from: a */
    private int m186a(int i, boolean z) {
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(i)).getBitmap();
        return z ? bitmap.getWidth() : bitmap.getHeight();
    }

    /* renamed from: a */
    private void m188a(float f, float f2) {
        this.mFrameLayout.setX(f - (this.f54x / 2.0f));
        this.mFrameLayout.setY(f2 - (this.f52v / 2.0f));
    }

    /* renamed from: a */
    private void m185a(long j, float f, float f2) {
        if (!this.f19E) {
            this.f19E = true;
            m189a();
            this.f16B = 0.0f;
            this.strokeAnimationValue = 0.0f;
            this.f17C = 0.0f;
            this.circleAnimationMin = 0.0f;
            this.f47q = 1.0f;
            this.f42l = 1.0f;
            this.mSViewCoverUnlockCircleEffect.dragAnimationUpdate(this.f16B);
            setImageInLockImageView(this.f16B);
            this.f41k.setAlpha(1.0f);
            m188a(f, f2);
            this.mCameraCircleInAnimator.setStartDelay(j);
            this.mCameraCircleInAnimator.setDuration(666L);
            this.mCameraCircleInAnimator.setInterpolator(new QuintEaseOut());
            this.mCameraCircleInAnimator.start();
            this.mCameraCircleOutAnimator.setStartDelay((-200) + j + 666);
            this.mCameraCircleOutAnimator.setDuration(700L);
            this.mCameraCircleOutAnimator.setInterpolator(new QuintEaseOut());
            this.mCameraCircleOutAnimator.start();
        }
    }

    /* renamed from: a */
    private void cancelAnimator(Animator animator) {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    /* renamed from: a */
    public void mCameraCircleEffect(View view, float f) {
        int i = 8;
        if (f != 0.0f) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
            view.setAlpha(f);
        } else if (view.getVisibility() != View.GONE) {
            if (view.getWidth() == 0) {
                i = View.INVISIBLE;
            }
            view.setVisibility(i);
        }
    }

    /* renamed from: a */
    private void m181a(View view, Point point) {
        int left = view.getLeft();
        int top = view.getTop();
        if (view.getParent() == null || !(view.getParent() instanceof View)) {
            point.x = left;
            point.y = top;
            return;
        }
        View view2 = (View) view.getParent();
        int scrollX = left - view2.getScrollX();
        int scrollY = top - view2.getScrollY();
        View view3 = view2;
        while (true) {
            if (view3.getId() == this.mGalaxyLockscreen.getHostView().getId()) {
                break;
            }
            int left2 = view3.getLeft() + scrollX;
            int top2 = view3.getTop() + scrollY;
            if (view3.getParent() == null) {
                scrollY = top2;
                scrollX = left2;
                break;
            } else if (!(view3.getParent() instanceof View)) {
                scrollY = top2;
                scrollX = left2;
                break;
            } else {
                View view4 = (View) view3.getParent();
                scrollX = left2 - view4.getScrollX();
                scrollY = top2 - view4.getScrollY();
                view3 = view4;
            }
        }
        point.x = scrollX;
        point.y = scrollY;
        Log.i("shortcut", "x = " + point.x + ", y = " + point.y);
    }

    private void cancelAllAnimator() {
        resetCircleAnimator();
        cancelAnimator(this.mCameraCircleInAnimator);
        cancelAnimator(this.mCameraCircleOutAnimator);
        cancelAnimator(this.f44n);
    }

    private void resetCircleAnimator() {
        if (this.f19E) {
            this.f19E = false;
            this.mCameraCircleInAnimator.end();
            this.mCameraCircleInAnimator.setStartDelay(0L);
            this.mCameraCircleInAnimator.setDuration(666L);
            this.mCameraCircleInAnimator.setInterpolator(new QuintEaseOut());
            this.mCameraCircleOutAnimator.end();
            this.mCameraCircleOutAnimator.setStartDelay(0L);
            this.mCameraCircleOutAnimator.setDuration(333L);
            this.mCameraCircleOutAnimator.setInterpolator(new QuintEaseOut());
        }
    }

    private void setAllAnimator() {
        this.mCameraCircleInAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mCameraCircleInAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.galaxytheme.a.b.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                SViewCoverNewUnlockArea.this.strokeAnimationValue = (((Float) valueAnimator.getAnimatedValue()).floatValue() * (1.0f - SViewCoverNewUnlockArea.this.circleAnimationMin)) + SViewCoverNewUnlockArea.this.circleAnimationMin;
                SViewCoverNewUnlockArea.this.mCameraCircleEffect(SViewCoverNewUnlockArea.this.mFrameLayout, SViewCoverNewUnlockArea.this.strokeAnimationValue);
                SViewCoverNewUnlockArea.this.mSViewCoverUnlockCircleEffect.strokeAnimationUpdate(SViewCoverNewUnlockArea.this.strokeAnimationValue);
            }
        });
        this.mCameraCircleOutAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        this.mCameraCircleOutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.galaxytheme.a.b.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float f;
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                SViewCoverNewUnlockArea.this.strokeAnimationValue = SViewCoverNewUnlockArea.this.f47q * floatValue;
                SViewCoverNewUnlockArea.this.f16B = SViewCoverNewUnlockArea.this.f17C * floatValue;
                SViewCoverNewUnlockArea.this.mSViewCoverUnlockCircleEffect.strokeAnimationUpdate(SViewCoverNewUnlockArea.this.strokeAnimationValue);
                SViewCoverNewUnlockArea.this.mSViewCoverUnlockCircleEffect.dragAnimationUpdate(SViewCoverNewUnlockArea.this.f16B);
                SViewCoverNewUnlockArea.this.setImageInLockImageView(SViewCoverNewUnlockArea.this.f16B);
                SViewCoverNewUnlockArea.this.mCameraCircleEffect(SViewCoverNewUnlockArea.this.mFrameLayout, SViewCoverNewUnlockArea.this.strokeAnimationValue);
                if (floatValue > 0.4f) {
                    f = ((floatValue - 0.4f) * SViewCoverNewUnlockArea.this.f42l) / 0.6f;
                } else {
                    f = 0.0f;
                }
                SViewCoverNewUnlockArea.this.f41k.setAlpha(f);
            }
        });
        this.mCameraCircleOutAnimator.addListener(new Animator.AnimatorListener() { // from class: com.galaxytheme.a.b.3
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @SuppressLint("LongLogTag")
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                Log.d("VisualEffectCircleUnlockEffect", "circleOutAnimator : onAnimationEnd");
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }
        });
        resetCircleAnimator();
        this.f44n = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f44n.setInterpolator(new LinearInterpolator());
        this.f44n.setDuration(500L);
        this.f44n.setRepeatCount(-1);
        this.f44n.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.galaxytheme.a.b.4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                if (SViewCoverNewUnlockArea.this.f43m) {
                    floatValue = 1.0f - floatValue;
                }
                SViewCoverNewUnlockArea.this.f41k.setAlpha(SViewCoverNewUnlockArea.this.f16B > 0.4f ? 0.0f : (floatValue * (0.4f - SViewCoverNewUnlockArea.this.f16B)) / 0.4f);
            }
        });
        this.f44n.addListener(new Animator.AnimatorListener() { // from class: com.galaxytheme.a.b.5
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
                SViewCoverNewUnlockArea.this.f43m = !SViewCoverNewUnlockArea.this.f43m;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }
        });
    }

    /* renamed from: g */
    private void m163g() {
        this.mFrameLayout = new FrameLayout(this.mContext);
        this.mFrameLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        addView(this.mFrameLayout, this.f54x, this.f52v);
        mCameraCircleEffect(this.mFrameLayout, 0.0f);
        this.mSViewCoverUnlockCircleEffect = new SViewCoverUnlockCircleEffect(this.mContext, this.f54x, this.f56z, this.f26L, this.f18D);
        this.mFrameLayout.addView(this.mSViewCoverUnlockCircleEffect);
        this.f41k = new ImageView(this.mContext);
        this.f41k.setImageResource(this.f45o);
        this.mFrameLayout.addView(this.f41k, -2, -2);
        this.f41k.setX((this.f54x - m186a(this.f45o, true)) / 2);
        this.f41k.setY((this.f52v - m186a(this.f45o, false)) / 2);
        this.f22H = new ImageView(this.mContext);
        this.f22H.setImageResource(this.f23I[0]);
        this.mFrameLayout.addView(this.f22H, -2, -2);
        this.f22H.setX((this.f54x - m186a(this.f23I[0], true)) / 2);
        this.f22H.setY((this.f52v - m186a(this.f23I[0], false)) / 2);
    }

    public void setImageInLockImageView(float f) {
        int i = (int) ((this.f24J - 1) * f);
        if (this.f15A != i) {
            this.f22H.setImageResource(this.f23I[i]);
            this.f15A = i;
        }
    }

    /* renamed from: a */
    public void m189a() {
        mCameraCircleEffect(this.f22H, 1.0f);
        this.f20F = false;
        if (this.mSViewCoverUnlockCircleEffect != null) {
            this.mSViewCoverUnlockCircleEffect.setIsForShortcut(this.f20F);
            this.mSViewCoverUnlockCircleEffect.setCircleMinWidth(this.f56z);
        }
    }

    /* renamed from: a */
    public void m187a(int i) {
        mCameraCircleEffect(this.f22H, 0.0f);
        this.f20F = true;
        if (this.mSViewCoverUnlockCircleEffect != null) {
            this.mSViewCoverUnlockCircleEffect.setIsForShortcut(this.f20F);
            this.mSViewCoverUnlockCircleEffect.setCircleMinWidth(i - 4);
        }
    }

    /* renamed from: a */
    @SuppressLint("LongLogTag")
    public void m184a(long j, Rect rect) {
        Log.d("VisualEffectCircleUnlockEffect", "showUnlockAffordance : " + rect.left + ", " + rect.right + ", " + rect.top + ", " + rect.bottom + ", startDelay : " + j);
        this.f31a = rect.left + ((rect.right - rect.left) / 2.0f);
        this.f32b = rect.top + ((rect.bottom - rect.top) / 2.0f);
        m185a(j, this.f31a, this.f32b);
    }

    /* renamed from: a */
    @SuppressLint("LongLogTag")
    public boolean m180a(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        Point point = new Point();
        m181a(view, point);
        if (!(view instanceof KeyguardShortcutView.ShortcutItem)) {
            x += point.x;
            y += point.y;
        }
        if (motionEvent.getActionMasked() == 0) {
            Log.d("VisualEffectCircleUnlockEffect", "handleTouchEvent : ACTION_DOWN");
            this.f21G = false;
            if (this.f20F) {
                this.f27M = point.x + (view.getWidth() / 2);
                this.f28N = point.y + (view.getHeight() / 2);
            } else {
                this.f27M = x;
                this.f28N = y;
            }
            this.f43m = false;
            cancelAllAnimator();
            m188a(this.f27M, this.f28N);
            this.mCameraCircleInAnimator.start();
            this.f44n.start();
        } else if (motionEvent.getActionMasked() == 2 && motionEvent.getActionIndex() == 0) {
            float f = x - this.f27M;
            float sqrt = (((float) Math.sqrt(Math.pow(y - this.f28N, 2.0d) + Math.pow(f, 2.0d))) - this.f55y) / (this.f53w - this.f55y);
            if (sqrt < 0.0f) {
                sqrt = 0.0f;
            }
            if (sqrt > 1.0f) {
                sqrt = 1.0f;
            }
            this.f16B = sqrt;
            this.mSViewCoverUnlockCircleEffect.dragAnimationUpdate(this.f16B);
            setImageInLockImageView(this.f16B);
        } else if (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 3) {
            cancelAllAnimator();
            this.f47q = this.strokeAnimationValue;
            this.f17C = this.f16B;
            this.f42l = this.f41k.getAlpha();
            if (!this.f21G) {
                this.mCameraCircleOutAnimator.start();
            }
        }
        return false;
    }

    /* renamed from: b */
    public void m175b() {
        this.f43m = false;
        mCameraCircleEffect(this.mFrameLayout, 0.0f);
        cancelAllAnimator();
        if (this.mSViewCoverUnlockCircleEffect != null) {
            this.mSViewCoverUnlockCircleEffect.dragAnimationUpdate(0.0f);
        }
    }

    /* renamed from: c */
    public void m172c() {
        this.f21G = true;
    }

    public void setImageResources(int i) {
    }

    public void setLockscreen(GalaxyLockscreen aVar) {
        this.mGalaxyLockscreen = aVar;
    }
}
