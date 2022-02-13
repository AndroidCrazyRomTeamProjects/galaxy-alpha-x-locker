package com.galaxytheme.effect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/* renamed from: com.galaxytheme.a.a */
/* loaded from: classes.dex */
public class C0003a extends View {

    /* renamed from: c */
    private int f2c;

    /* renamed from: d */
    private int f3d;

    /* renamed from: e */
    private int f4e;

    /* renamed from: g */
    private Paint f6g;

    /* renamed from: h */
    private Paint f7h;

    /* renamed from: i */
    private int f8i;

    /* renamed from: k */
    private int f10k;

    /* renamed from: l */
    private int f11l;

    /* renamed from: m */
    private Paint f12m;

    /* renamed from: n */
    private int f13n;

    /* renamed from: a */
    private final boolean f0a = true;

    /* renamed from: b */
    private final String f1b = "VisualEffectCircleUnlockEffect";

    /* renamed from: f */
    private float f5f = 0.0f;

    /* renamed from: j */
    private boolean f9j = false;

    /* renamed from: o */
    private float f14o = 0.0f;

    public C0003a(Context context, int i, int i2, int i3, int i4) {
        super(context);
        int i5 = i / 2;
        this.f4e = i5;
        this.f3d = i5;
        this.f10k = i5;
        this.f11l = i2 / 2;
        this.f2c = this.f10k - this.f11l;
        this.f13n = i3;
        this.f8i = i4;
        m192a();
    }

    /* renamed from: a */
    private void m192a() {
        this.f12m = new Paint();
        this.f12m.setAntiAlias(true);
        this.f12m.setColor(-1426063361);
        this.f12m.setStyle(Paint.Style.STROKE);
        this.f12m.setStrokeWidth(this.f13n);
        this.f7h = new Paint();
        this.f7h.setAntiAlias(true);
        this.f7h.setColor(-1);
        this.f7h.setStyle(Paint.Style.STROKE);
        this.f7h.setStrokeWidth(this.f8i);
        this.f6g = new Paint();
        this.f6g.setAntiAlias(true);
        this.f6g.setColor(1442840575);
        this.f6g.setStyle(Paint.Style.STROKE);
    }

    /* renamed from: a */
    public void m191a(float f) {
        this.f5f = f;
        invalidate();
    }

    /* renamed from: b */
    public void m190b(float f) {
        this.f14o = f;
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(this.f3d, this.f4e, (this.f11l + (this.f2c * this.f14o)) - (this.f13n / 2.0f), this.f12m);
        if (!this.f9j) {
            canvas.drawCircle(this.f3d, this.f4e, this.f11l, this.f7h);
        }
        if (this.f5f > 0.0f) {
            float f = this.f5f > this.f14o ? this.f14o : this.f5f;
            this.f6g.setStrokeWidth(this.f2c * f);
            canvas.drawCircle(this.f3d, this.f4e, ((f * this.f2c) / 2.0f) + this.f11l, this.f6g);
        }
    }

    public void setCircleMinWidth(int i) {
        this.f11l = i / 2;
        this.f2c = this.f10k - this.f11l;
    }

    public void setIsForShortcut(boolean z) {
        this.f9j = z;
    }
}
