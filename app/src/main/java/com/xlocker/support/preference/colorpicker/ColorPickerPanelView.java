package com.xlocker.support.preference.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: classes.dex */
public class ColorPickerPanelView extends View {

    /* renamed from: a */
    private float f291a;

    /* renamed from: b */
    private int f292b;

    /* renamed from: c */
    private int f293c;

    /* renamed from: d */
    private Paint f294d;

    /* renamed from: e */
    private Paint f295e;

    /* renamed from: f */
    private RectF f296f;

    /* renamed from: g */
    private RectF f297g;

    /* renamed from: h */
    private C0078a f298h;

    public ColorPickerPanelView(Context context) {
        this(context, null);
    }

    public ColorPickerPanelView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorPickerPanelView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f291a = 1.0f;
        this.f292b = -9539986;
        this.f293c = -16777216;
        m23a();
    }

    /* renamed from: a */
    private void m23a() {
        this.f294d = new Paint();
        this.f295e = new Paint();
        this.f291a = getContext().getResources().getDisplayMetrics().density;
    }

    /* renamed from: b */
    private void m22b() {
        RectF rectF = this.f296f;
        this.f297g = new RectF(rectF.left + 1.0f, rectF.top + 1.0f, rectF.right - 1.0f, rectF.bottom - 1.0f);
        this.f298h = new C0078a((int) (5.0f * this.f291a));
        this.f298h.setBounds(Math.round(this.f297g.left), Math.round(this.f297g.top), Math.round(this.f297g.right), Math.round(this.f297g.bottom));
    }

    public int getBorderColor() {
        return this.f292b;
    }

    public int getColor() {
        return this.f293c;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        RectF rectF = this.f297g;
        this.f294d.setColor(this.f292b);
        canvas.drawRect(this.f296f, this.f294d);
        if (this.f298h != null) {
            this.f298h.draw(canvas);
        }
        this.f295e.setColor(this.f293c);
        canvas.drawRect(rectF, this.f295e);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.f296f = new RectF();
        this.f296f.left = getPaddingLeft();
        this.f296f.right = i - getPaddingRight();
        this.f296f.top = getPaddingTop();
        this.f296f.bottom = i2 - getPaddingBottom();
        m22b();
    }

    public void setBorderColor(int i) {
        this.f292b = i;
        invalidate();
    }

    public void setColor(int i) {
        this.f293c = i;
        invalidate();
    }
}
