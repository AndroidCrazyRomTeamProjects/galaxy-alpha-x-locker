package com.xlocker.support.preference.colorpicker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/* renamed from: com.xlocker.support.preference.colorpicker.a */
/* loaded from: classes.dex */
public class C0078a extends Drawable {

    /* renamed from: a */
    private int f333a;

    /* renamed from: b */
    private Paint f334b = new Paint();

    /* renamed from: c */
    private Paint f335c = new Paint();

    /* renamed from: d */
    private Paint f336d = new Paint();

    /* renamed from: e */
    private int f337e;

    /* renamed from: f */
    private int f338f;

    /* renamed from: g */
    private Bitmap f339g;

    public C0078a(int i) {
        this.f333a = 10;
        this.f333a = i;
        this.f335c.setColor(-1);
        this.f336d.setColor(-3421237);
    }

    /* renamed from: a */
    private void m0a() {
        if (getBounds().width() > 0 && getBounds().height() > 0) {
            this.f339g = Bitmap.createBitmap(getBounds().width(), getBounds().height(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(this.f339g);
            Rect rect = new Rect();
            boolean z = true;
            for (int i = 0; i <= this.f338f; i++) {
                boolean z2 = z;
                for (int i2 = 0; i2 <= this.f337e; i2++) {
                    rect.top = this.f333a * i;
                    rect.left = this.f333a * i2;
                    rect.bottom = rect.top + this.f333a;
                    rect.right = rect.left + this.f333a;
                    canvas.drawRect(rect, z2 ? this.f335c : this.f336d);
                    z2 = !z2;
                }
                z = !z;
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.f339g, (Rect) null, getBounds(), this.f334b);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        int height = rect.height();
        this.f337e = (int) Math.ceil(rect.width() / this.f333a);
        this.f338f = (int) Math.ceil(height / this.f333a);
        m0a();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        throw new UnsupportedOperationException("Alpha is not supported by this drawwable.");
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        throw new UnsupportedOperationException("ColorFilter is not supported by this drawwable.");
    }
}
