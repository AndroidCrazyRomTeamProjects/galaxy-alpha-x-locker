package com.xlocker.support.preference.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerView extends View {

    private static final int AREA_COLOR = 0;
    private static final int AREA_HUE = 1;
    private static final int AREA_ALPHA = 2;
    private static final float TRACKBALL_COLOR_STEP = 50.0f;
    private static final float TRACKBALL_HUE_STEP = 10.0f;
    private static final float TRACKBALL_ALPHA_STEP = 10.0f;

    private int currentTouchArea;

    private float drawingOffset;

    private RectF drawingRect;

    private RectF colorRect;

    private RectF hueRect;

    private RectF alphaRect;

    private AlphaPatternDrawable alphaPattern;

    private Point startTouchPoint;

    private float huePanelWidth;

    private float alphaPanelHeight;

    private float panelSpacing;

    private float colorPointerRadius;

    private float hueTrackerWidth;

    private float density;

    private OnColorChangedListener onColorChangedListener;

    private Paint colorPaint;

    private Paint colorPointerPaint;

    private Paint huePaint;

    private Paint sliderTrackerPaint;

    private Paint alphaPaint;

    private Paint alphaTextPaint;

    private Paint borderPaint;

    private Shader valueShader;

    private Shader saturationShader;

    private Shader hueShader;

    private Shader alphaShader;

    private int alpha;

    private float hue;

    private float saturation;

    private float value;

    private String alphaSliderText;

    private int sliderTrackerColor;

    private int borderColor;

    private boolean showAlphaPanel;

    public interface OnColorChangedListener {
        void onColorChanged(int i);
    }

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorPickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.huePanelWidth = 30.0f;
        this.alphaPanelHeight = 20.0f;
        this.panelSpacing = 10.0f;
        this.colorPointerRadius = 5.0f;
        this.hueTrackerWidth = 2.0f;
        this.density = 1.0f;
        this.alpha = 255;
        this.hue = 360.0f;
        this.saturation = 0.0f;
        this.value = 0.0f;
        this.alphaSliderText = "";
        this.sliderTrackerColor = -14935012;
        this.borderColor = -9539986;
        this.showAlphaPanel = false;
        this.currentTouchArea = AREA_COLOR;
        this.startTouchPoint = null;
        init();
    }

    private int resolveMeasuredWidth(int i, int i2) {
        return (i == Integer.MIN_VALUE || i == 1073741824) ? i2 : getPrefferedWidth();
    }

    private Point getHuePointerPosition(float f) {
        RectF rectF = this.hueRect;
        float height = rectF.height();
        Point point = new Point();
        point.y = (int) ((height - ((f * height) / 360.0f)) + rectF.top);
        point.x = (int) rectF.left;
        return point;
    }

    private Point getColorPointerPosition(float f, float f2) {
        RectF rectF = this.colorRect;
        float height = rectF.height();
        float width = rectF.width();
        Point point = new Point();
        point.x = (int) ((width * f) + rectF.left);
        point.y = (int) (rectF.top + (height * (1.0f - f2)));
        return point;
    }

    private Point getAlphaPointerPosition(int i) {
        RectF rectF = this.alphaRect;
        float width = rectF.width();
        Point point = new Point();
        point.x = (int) ((width - ((i * width) / 255.0f)) + rectF.left);
        point.y = (int) rectF.top;
        return point;
    }

    private void init() {
        this.density = getContext().getResources().getDisplayMetrics().density;
        this.colorPointerRadius *= this.density;
        this.hueTrackerWidth *= this.density;
        this.huePanelWidth *= this.density;
        this.alphaPanelHeight *= this.density;
        this.panelSpacing *= this.density;
        this.drawingOffset = calculateDrawingOffset();
        initPaintTools();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    private void drawColorPanel(Canvas canvas) {
        RectF rectF = this.colorRect;
        this.borderPaint.setColor(this.borderColor);
        canvas.drawRect(this.drawingRect.left, this.drawingRect.top, 1.0f + rectF.right, 1.0f + rectF.bottom, this.borderPaint);
        if (this.valueShader == null) {
            this.valueShader = new LinearGradient(rectF.left, rectF.top, rectF.left, rectF.bottom, -1, -16777216, Shader.TileMode.CLAMP);
        }
        this.saturationShader = new LinearGradient(rectF.left, rectF.top, rectF.right, rectF.top, -1, Color.HSVToColor(new float[]{this.hue, 1.0f, 1.0f}), Shader.TileMode.CLAMP);
        this.colorPaint.setShader(new ComposeShader(this.valueShader, this.saturationShader, PorterDuff.Mode.MULTIPLY));
        canvas.drawRect(rectF, this.colorPaint);
        Point a = getColorPointerPosition(this.saturation, this.value);
        this.colorPointerPaint.setColor(-16777216);
        canvas.drawCircle(a.x, a.y, this.colorPointerRadius - (1.0f * this.density), this.colorPointerPaint);
        this.colorPointerPaint.setColor(-2236963);
        canvas.drawCircle(a.x, a.y, this.colorPointerRadius, this.colorPointerPaint);
    }

    private boolean moveTrackersIfNeeded(MotionEvent motionEvent) {
        boolean handled = true;
        if (this.startTouchPoint == null) {
            return false;
        }
        int i = this.startTouchPoint.x;
        int i2 = this.startTouchPoint.y;
        if (this.hueRect.contains(i, i2)) {
            this.currentTouchArea = AREA_HUE;
            this.hue = calculateHue(motionEvent.getY());
        } else if (this.colorRect.contains(i, i2)) {
            this.currentTouchArea = AREA_COLOR;
            float[] b = calculateSaturationAndValue(motionEvent.getX(), motionEvent.getY());
            this.saturation = b[0];
            this.value = b[1];
        } else if (this.alphaRect == null || !this.alphaRect.contains(i, i2)) {
            handled = false;
        } else {
            this.currentTouchArea = AREA_ALPHA;
            this.alpha = calculateAlpha((int) motionEvent.getX());
        }
        return handled;
    }

    private float calculateHue(float f) {
        RectF rectF = this.hueRect;
        float height = rectF.height();
        return 360.0f - (((f < rectF.top ? 0.0f : f > rectF.bottom ? height : f - rectF.top) * 360.0f) / height);
    }

    private int calculateAlpha(int i) {
        RectF rectF = this.alphaRect;
        int width = (int) rectF.width();
        return 255 - (((((float) i) < rectF.left ? 0 : ((float) i) > rectF.right ? width : i - ((int) rectF.left)) * 255) / width);
    }

    private int resolveMeasuredHeight(int i, int i2) {
        return (i == Integer.MIN_VALUE || i == 1073741824) ? i2 : getPrefferedHeight();
    }

    private void initPaintTools() {
        this.colorPaint = new Paint();
        this.colorPointerPaint = new Paint();
        this.huePaint = new Paint();
        this.sliderTrackerPaint = new Paint();
        this.alphaPaint = new Paint();
        this.alphaTextPaint = new Paint();
        this.borderPaint = new Paint();
        this.colorPointerPaint.setStyle(Paint.Style.STROKE);
        this.colorPointerPaint.setStrokeWidth(this.density * 2.0f);
        this.colorPointerPaint.setAntiAlias(true);
        this.sliderTrackerPaint.setColor(this.sliderTrackerColor);
        this.sliderTrackerPaint.setStyle(Paint.Style.STROKE);
        this.sliderTrackerPaint.setStrokeWidth(this.density * 2.0f);
        this.sliderTrackerPaint.setAntiAlias(true);
        this.alphaTextPaint.setColor(-14935012);
        this.alphaTextPaint.setTextSize(14.0f * this.density);
        this.alphaTextPaint.setAntiAlias(true);
        this.alphaTextPaint.setTextAlign(Paint.Align.CENTER);
        this.alphaTextPaint.setFakeBoldText(true);
    }

    private void drawHuePanel(Canvas canvas) {
        RectF rectF = this.hueRect;
        this.borderPaint.setColor(this.borderColor);
        canvas.drawRect(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, 1.0f + rectF.bottom, this.borderPaint);
        if (this.hueShader == null) {
            this.hueShader = new LinearGradient(rectF.left, rectF.top, rectF.left, rectF.bottom, buildHueColorArray(), null, Shader.TileMode.CLAMP);
            this.huePaint.setShader(this.hueShader);
        }
        canvas.drawRect(rectF, this.huePaint);
        float f = (4.0f * this.density) / 2.0f;
        Point a = getHuePointerPosition(this.hue);
        RectF rectF2 = new RectF();
        rectF2.left = rectF.left - this.hueTrackerWidth;
        rectF2.right = rectF.right + this.hueTrackerWidth;
        rectF2.top = a.y - f;
        rectF2.bottom = f + a.y;
        canvas.drawRoundRect(rectF2, 2.0f, 2.0f, this.sliderTrackerPaint);
    }

    private float[] calculateSaturationAndValue(float f, float f2) {
        float f3 = 0.0f;
        RectF rectF = this.colorRect;
        float[] fArr = new float[2];
        float width = rectF.width();
        float height = rectF.height();
        float f4 = f < rectF.left ? 0.0f : f > rectF.right ? width : f - rectF.left;
        if (f2 >= rectF.top) {
            f3 = f2 > rectF.bottom ? height : f2 - rectF.top;
        }
        fArr[0] = f4 * (1.0f / width);
        fArr[1] = 1.0f - (f3 * (1.0f / height));
        return fArr;
    }

    private float calculateDrawingOffset() {
        return Math.max(Math.max(this.colorPointerRadius, this.hueTrackerWidth), 1.0f * this.density) * 1.5f;
    }

    private void drawAlphaPanel(Canvas canvas) {
        if (this.showAlphaPanel && this.alphaRect != null && this.alphaPattern != null) {
            RectF rectF = this.alphaRect;
            this.borderPaint.setColor(this.borderColor);
            canvas.drawRect(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, 1.0f + rectF.bottom, this.borderPaint);
            this.alphaPattern.draw(canvas);
            float[] fArr = {this.hue, this.saturation, this.value};
            this.alphaShader = new LinearGradient(rectF.left, rectF.top, rectF.right, rectF.top, Color.HSVToColor(fArr), Color.HSVToColor(0, fArr), Shader.TileMode.CLAMP);
            this.alphaPaint.setShader(this.alphaShader);
            canvas.drawRect(rectF, this.alphaPaint);
            if (!(this.alphaSliderText == null || this.alphaSliderText.equals(""))) {
                canvas.drawText(this.alphaSliderText, rectF.centerX(), rectF.centerY() + (this.density * 4.0f), this.alphaTextPaint);
            }
            float f = (this.density * 4.0f) / 2.0f;
            Point a = getAlphaPointerPosition(this.alpha);
            RectF rectF2 = new RectF();
            rectF2.left = a.x - f;
            rectF2.right = f + a.x;
            rectF2.top = rectF.top - this.hueTrackerWidth;
            rectF2.bottom = rectF.bottom + this.hueTrackerWidth;
            canvas.drawRoundRect(rectF2, 2.0f, 2.0f, this.sliderTrackerPaint);
        }
    }

    private int[] buildHueColorArray() {
        int[] iArr = new int[361];
        int length = iArr.length - 1;
        int i = 0;
        while (length >= 0) {
            iArr[i] = Color.HSVToColor(new float[]{length, 1.0f, 1.0f});
            length--;
            i++;
        }
        return iArr;
    }

    private void calculateColorRect() {
        RectF rectF = this.drawingRect;
        float height = rectF.height() - 2.0f;
        if (this.showAlphaPanel) {
            height -= this.panelSpacing + this.alphaPanelHeight;
        }
        float f = rectF.left + 1.0f;
        float f2 = rectF.top + 1.0f;
        this.colorRect = new RectF(f, f2, height + f, f2 + height);
    }

    private void calculateHueRect() {
        RectF rectF = this.drawingRect;
        this.hueRect = new RectF((rectF.right - this.huePanelWidth) + 1.0f, rectF.top + 1.0f, rectF.right - 1.0f, (rectF.bottom - 1.0f) - (this.showAlphaPanel ? this.panelSpacing + this.alphaPanelHeight : 0.0f));
    }

    private void calculateAlphaRect() {
        if (this.showAlphaPanel) {
            RectF rectF = this.drawingRect;
            this.alphaRect = new RectF(rectF.left + 1.0f, (rectF.bottom - this.alphaPanelHeight) + 1.0f, rectF.right - 1.0f, rectF.bottom - 1.0f);
            this.alphaPattern = new AlphaPatternDrawable((int) (5.0f * this.density));
            this.alphaPattern.setBounds(Math.round(this.alphaRect.left), Math.round(this.alphaRect.top), Math.round(this.alphaRect.right), Math.round(this.alphaRect.bottom));
        }
    }

    private int getPrefferedHeight() {
        int i = (int) (200.0f * this.density);
        return this.showAlphaPanel ? (int) (i + this.panelSpacing + this.alphaPanelHeight) : i;
    }

    private int getPrefferedWidth() {
        int prefferedHeight = getPrefferedHeight();
        if (this.showAlphaPanel) {
            prefferedHeight = (int) (prefferedHeight - (this.panelSpacing + this.alphaPanelHeight));
        }
        return (int) (prefferedHeight + this.huePanelWidth + this.panelSpacing);
    }

    public void setColorInternal(int i, boolean z) {
        int alpha = Color.alpha(i);
        float[] fArr = new float[3];
        Color.colorToHSV(i, fArr);
        this.alpha = alpha;
        this.hue = fArr[0];
        this.saturation = fArr[1];
        this.value = fArr[2];
        if (z) {
            notifyColorChanged();
        } else {
            invalidate();
        }
    }

    public String getAlphaSliderText() {
        return this.alphaSliderText;
    }

    public boolean getAlphaSliderVisible() {
        return this.showAlphaPanel;
    }

    public int getBorderColor() {
        return this.borderColor;
    }

    public int getColor() {
        return Color.HSVToColor(this.alpha, new float[]{this.hue, this.saturation, this.value});
    }

    public float getDrawingOffset() {
        return this.drawingOffset;
    }

    public int getSliderTrackerColor() {
        return this.sliderTrackerColor;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.drawingRect.width() > 0.0f && this.drawingRect.height() > 0.0f) {
            drawColorPanel(canvas);
            drawHuePanel(canvas);
            drawAlphaPanel(canvas);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int a = resolveMeasuredWidth(mode, size);
        int b = resolveMeasuredHeight(mode2, size2);
        if (!this.showAlphaPanel) {
            i4 = (int) ((a - this.panelSpacing) - this.huePanelWidth);
            if (i4 > b || getTag().equals("landscape")) {
                i3 = (int) (b + this.panelSpacing + this.huePanelWidth);
                i4 = b;
            } else {
                i3 = a;
            }
        } else {
            int i5 = (int) ((b - this.alphaPanelHeight) + this.huePanelWidth);
            if (i5 > a) {
                i4 = (int) ((a - this.huePanelWidth) + this.alphaPanelHeight);
                i3 = a;
            } else {
                i3 = i5;
                i4 = b;
            }
        }
        setMeasuredDimension(i3, i4);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.drawingRect = new RectF();
        this.drawingRect.left = this.drawingOffset + getPaddingLeft();
        this.drawingRect.right = (i - this.drawingOffset) - getPaddingRight();
        this.drawingRect.top = this.drawingOffset + getPaddingTop();
        this.drawingRect.bottom = (i2 - this.drawingOffset) - getPaddingBottom();
        calculateColorRect();
        calculateHueRect();
        calculateAlphaRect();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean a;
        switch (motionEvent.getAction()) {
            case 0:
                this.startTouchPoint = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                a = moveTrackersIfNeeded(motionEvent);
                break;
            case 1:
                this.startTouchPoint = null;
                a = moveTrackersIfNeeded(motionEvent);
                break;
            case 2:
                a = moveTrackersIfNeeded(motionEvent);
                break;
            default:
                a = false;
                break;
        }
        if (!a) {
            return super.onTouchEvent(motionEvent);
        }
        notifyColorChanged();
        return true;
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() != MotionEvent.ACTION_MOVE) {
            return super.onTrackballEvent(motionEvent);
        }
        boolean handled = false;
        float deltaX = motionEvent.getX();
        float deltaY = motionEvent.getY();
        switch (this.currentTouchArea) {
            case AREA_COLOR:
                float newSaturation = clamp(this.saturation + (deltaX / TRACKBALL_COLOR_STEP), 0.0f, 1.0f);
                float newValue = clamp(this.value - (deltaY / TRACKBALL_COLOR_STEP), 0.0f, 1.0f);
                if (newSaturation != this.saturation || newValue != this.value) {
                    this.saturation = newSaturation;
                    this.value = newValue;
                    handled = true;
                }
                break;
            case AREA_HUE:
                float newHue = clamp(this.hue - (deltaY * TRACKBALL_HUE_STEP), 0.0f, 360.0f);
                if (newHue != this.hue) {
                    this.hue = newHue;
                    handled = true;
                }
                break;
            case AREA_ALPHA:
                if (this.showAlphaPanel && this.alphaRect != null) {
                    int newAlpha = Math.round(clamp(this.alpha - (deltaX * TRACKBALL_ALPHA_STEP), 0.0f, 255.0f));
                    if (newAlpha != this.alpha) {
                        this.alpha = newAlpha;
                        handled = true;
                    }
                }
                break;
            default:
                break;
        }
        if (!handled) {
            return super.onTrackballEvent(motionEvent);
        }
        notifyColorChanged();
        return true;
    }

    public void setAlphaSliderText(int i) {
        setAlphaSliderText(getContext().getString(i));
    }

    public void setAlphaSliderText(String str) {
        this.alphaSliderText = str;
        invalidate();
    }

    public void setAlphaSliderVisible(boolean z) {
        if (this.showAlphaPanel != z) {
            this.showAlphaPanel = z;
            this.valueShader = null;
            this.saturationShader = null;
            this.hueShader = null;
            this.alphaShader = null;
            requestLayout();
        }
    }

    public void setBorderColor(int i) {
        this.borderColor = i;
        invalidate();
    }

    public void setColor(int i) {
        setColorInternal(i, false);
    }

    public void setOnColorChangedListener(OnColorChangedListener onColorChangedListener) {
        this.onColorChangedListener = onColorChangedListener;
    }

    public void setSliderTrackerColor(int i) {
        this.sliderTrackerColor = i;
        this.sliderTrackerPaint.setColor(this.sliderTrackerColor);
        invalidate();
    }

    private void notifyColorChanged() {
        if (this.onColorChangedListener != null) {
            this.onColorChangedListener.onColorChanged(Color.HSVToColor(this.alpha, new float[]{this.hue, this.saturation, this.value}));
        }
        invalidate();
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
