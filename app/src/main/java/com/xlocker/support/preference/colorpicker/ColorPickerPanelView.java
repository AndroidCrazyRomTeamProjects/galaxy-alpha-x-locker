package com.xlocker.support.preference.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ColorPickerPanelView extends View {

    private float density;

    private int borderColor;

    private int color;

    private Paint borderPaint;

    private Paint colorPaint;

    private RectF drawingRect;

    private RectF colorRect;

    private AlphaPatternDrawable alphaPattern;

    public ColorPickerPanelView(Context context) {
        this(context, null);
    }

    public ColorPickerPanelView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorPickerPanelView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.density = 1.0f;
        this.borderColor = -9539986;
        this.color = -16777216;
        init();
    }

    private void init() {
        this.borderPaint = new Paint();
        this.colorPaint = new Paint();
        this.density = getContext().getResources().getDisplayMetrics().density;
    }

    private void initAlphaPattern() {
        RectF rectF = this.drawingRect;
        this.colorRect = new RectF(rectF.left + 1.0f, rectF.top + 1.0f, rectF.right - 1.0f, rectF.bottom - 1.0f);
        this.alphaPattern = new AlphaPatternDrawable((int) (5.0f * this.density));
        this.alphaPattern.setBounds(Math.round(this.colorRect.left), Math.round(this.colorRect.top), Math.round(this.colorRect.right), Math.round(this.colorRect.bottom));
    }

    public int getBorderColor() {
        return this.borderColor;
    }

    public int getColor() {
        return this.color;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        RectF rectF = this.colorRect;
        this.borderPaint.setColor(this.borderColor);
        canvas.drawRect(this.drawingRect, this.borderPaint);
        if (this.alphaPattern != null) {
            this.alphaPattern.draw(canvas);
        }
        this.colorPaint.setColor(this.color);
        canvas.drawRect(rectF, this.colorPaint);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.drawingRect = new RectF();
        this.drawingRect.left = getPaddingLeft();
        this.drawingRect.right = i - getPaddingRight();
        this.drawingRect.top = getPaddingTop();
        this.drawingRect.bottom = i2 - getPaddingBottom();
        initAlphaPattern();
    }

    public void setBorderColor(int i) {
        this.borderColor = i;
        invalidate();
    }

    public void setColor(int i) {
        this.color = i;
        invalidate();
    }
}
