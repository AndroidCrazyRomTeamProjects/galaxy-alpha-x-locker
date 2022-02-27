package com.galaxytheme.effect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/* renamed from: com.galaxytheme.a.a */
/* loaded from: classes.dex */
public class SViewCoverUnlockCircleEffect extends View {
    private int betweenRadius;
    private int centerX;
    private int centerY;
    private Paint fillStrokePaint;
    private Paint innerStrokePaint;
    private int innerStrokeWidth;
    private int maxRadius;
    private int f11l;
    private Paint outStrokePaint;
    private int outerStrokeWidth;

    /* renamed from: a */
    private final boolean ANIMATION_DEBUG = true;
    private final String TAG = "VisualEffectCircleUnlockEffect";
    private float fillAnimationValue = 0.0f;
    private boolean isForShortcut = false;
    private float strokeAnimationValue = 0.0f;

    public SViewCoverUnlockCircleEffect(Context context, int circleMaxWidth, int circleMinWidth, int outerStrokeWidth, int innerStrokeWidth) {
        super(context);
        int coordinate = circleMaxWidth / 2;
        this.centerY = coordinate;
        this.centerX = coordinate;
        this.maxRadius = coordinate;
        this.f11l = circleMinWidth / 2;
        this.betweenRadius = this.maxRadius - this.f11l;
        this.outerStrokeWidth = outerStrokeWidth;
        this.innerStrokeWidth = innerStrokeWidth;
        setLayout();
    }

    private void setLayout() {
        this.outStrokePaint = new Paint();
        this.outStrokePaint.setAntiAlias(true);
        this.outStrokePaint.setColor(-1426063361);
        this.outStrokePaint.setStyle(Paint.Style.STROKE);
        this.outStrokePaint.setStrokeWidth(this.outerStrokeWidth);
        this.innerStrokePaint = new Paint();
        this.innerStrokePaint.setAntiAlias(true);
        this.innerStrokePaint.setColor(-1);
        this.innerStrokePaint.setStyle(Paint.Style.STROKE);
        this.innerStrokePaint.setStrokeWidth(this.innerStrokeWidth);
        this.fillStrokePaint = new Paint();
        this.fillStrokePaint.setAntiAlias(true);
        this.fillStrokePaint.setColor(1442840575);
        this.fillStrokePaint.setStyle(Paint.Style.STROKE);
    }

    /* renamed from: a */
    public void dragAnimationUpdate(float value) {
        this.fillAnimationValue = value;
        invalidate();
    }

    public void strokeAnimationUpdate(float value) {
        this.strokeAnimationValue = value;
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(this.centerX, this.centerY, (this.f11l + (this.betweenRadius * this.strokeAnimationValue)) - (this.outerStrokeWidth / 2.0f), this.outStrokePaint);
        if (!this.isForShortcut) {
            canvas.drawCircle(this.centerX, this.centerY, this.f11l, this.innerStrokePaint);
        }
        if (this.fillAnimationValue > 0.0f) {
            float f = this.fillAnimationValue > this.strokeAnimationValue ? this.strokeAnimationValue : this.fillAnimationValue;
            this.fillStrokePaint.setStrokeWidth(this.betweenRadius * f);
            canvas.drawCircle(this.centerX, this.centerY, ((f * this.betweenRadius) / 2.0f) + this.f11l, this.fillStrokePaint);
        }
    }

    public void setCircleMinWidth(int value) {
        this.f11l = value / 2;
        this.betweenRadius = this.maxRadius - this.f11l;
    }

    public void setIsForShortcut(boolean value) {
        this.isForShortcut = value;
    }
}
