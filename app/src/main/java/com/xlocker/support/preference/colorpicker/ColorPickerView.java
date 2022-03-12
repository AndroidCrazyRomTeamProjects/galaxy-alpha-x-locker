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

/* loaded from: classes.dex */
public class ColorPickerView extends View {

    /* renamed from: A */
    private int f299A;

    /* renamed from: B */
    private float f300B;

    /* renamed from: C */
    private RectF f301C;

    /* renamed from: D */
    private RectF f302D;

    /* renamed from: E */
    private RectF f303E;

    /* renamed from: F */
    private RectF f304F;

    /* renamed from: G */
    private C0078a f305G;

    /* renamed from: H */
    private Point f306H;

    /* renamed from: a */
    private float f307a;

    /* renamed from: b */
    private float f308b;

    /* renamed from: c */
    private float f309c;

    /* renamed from: d */
    private float f310d;

    /* renamed from: e */
    private float f311e;

    /* renamed from: f */
    private float f312f;

    /* renamed from: g */
    private AbstractC0077a f313g;

    /* renamed from: h */
    private Paint f314h;

    /* renamed from: i */
    private Paint f315i;

    /* renamed from: j */
    private Paint f316j;

    /* renamed from: k */
    private Paint f317k;

    /* renamed from: l */
    private Paint f318l;

    /* renamed from: m */
    private Paint f319m;

    /* renamed from: n */
    private Paint f320n;

    /* renamed from: o */
    private Shader f321o;

    /* renamed from: p */
    private Shader f322p;

    /* renamed from: q */
    private Shader f323q;

    /* renamed from: r */
    private Shader f324r;

    /* renamed from: s */
    private int f325s;

    /* renamed from: t */
    private float f326t;

    /* renamed from: u */
    private float f327u;

    /* renamed from: v */
    private float f328v;

    /* renamed from: w */
    private String f329w;

    /* renamed from: x */
    private int f330x;

    /* renamed from: y */
    private int f331y;

    /* renamed from: z */
    private boolean f332z;

    /* renamed from: com.xlocker.support.preference.colorpicker.ColorPickerView$a */
    /* loaded from: classes.dex */
    public interface AbstractC0077a {
        /* renamed from: a */
        void m1a(int i);
    }

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorPickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f307a = 30.0f;
        this.f308b = 20.0f;
        this.f309c = 10.0f;
        this.f310d = 5.0f;
        this.f311e = 2.0f;
        this.f312f = 1.0f;
        this.f325s = 255;
        this.f326t = 360.0f;
        this.f327u = 0.0f;
        this.f328v = 0.0f;
        this.f329w = "";
        this.f330x = -14935012;
        this.f331y = -9539986;
        this.f332z = false;
        this.f299A = 0;
        this.f306H = null;
        m21a();
    }

    /* renamed from: a */
    private int m17a(int i, int i2) {
        return (i == Integer.MIN_VALUE || i == 1073741824) ? i2 : getPrefferedWidth();
    }

    /* renamed from: a */
    private Point m20a(float f) {
        RectF rectF = this.f303E;
        float height = rectF.height();
        Point point = new Point();
        point.y = (int) ((height - ((f * height) / 360.0f)) + rectF.top);
        point.x = (int) rectF.left;
        return point;
    }

    /* renamed from: a */
    private Point m19a(float f, float f2) {
        RectF rectF = this.f302D;
        float height = rectF.height();
        float width = rectF.width();
        Point point = new Point();
        point.x = (int) ((width * f) + rectF.left);
        point.y = (int) (rectF.top + (height * (1.0f - f2)));
        return point;
    }

    /* renamed from: a */
    private Point m18a(int i) {
        RectF rectF = this.f304F;
        float width = rectF.width();
        Point point = new Point();
        point.x = (int) ((width - ((i * width) / 255.0f)) + rectF.left);
        point.y = (int) rectF.top;
        return point;
    }

    /* renamed from: a */
    private void m21a() {
        this.f312f = getContext().getResources().getDisplayMetrics().density;
        this.f310d *= this.f312f;
        this.f311e *= this.f312f;
        this.f307a *= this.f312f;
        this.f308b *= this.f312f;
        this.f309c *= this.f312f;
        this.f300B = m7c();
        m13b();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    /* renamed from: a */
    private void m15a(Canvas canvas) {
        RectF rectF = this.f302D;
        this.f320n.setColor(this.f331y);
        canvas.drawRect(this.f301C.left, this.f301C.top, 1.0f + rectF.right, 1.0f + rectF.bottom, this.f320n);
        if (this.f321o == null) {
            this.f321o = new LinearGradient(rectF.left, rectF.top, rectF.left, rectF.bottom, -1, -16777216, Shader.TileMode.CLAMP);
        }
        this.f322p = new LinearGradient(rectF.left, rectF.top, rectF.right, rectF.top, -1, Color.HSVToColor(new float[]{this.f326t, 1.0f, 1.0f}), Shader.TileMode.CLAMP);
        this.f314h.setShader(new ComposeShader(this.f321o, this.f322p, PorterDuff.Mode.MULTIPLY));
        canvas.drawRect(rectF, this.f314h);
        Point a = m19a(this.f327u, this.f328v);
        this.f315i.setColor(-16777216);
        canvas.drawCircle(a.x, a.y, this.f310d - (1.0f * this.f312f), this.f315i);
        this.f315i.setColor(-2236963);
        canvas.drawCircle(a.x, a.y, this.f310d, this.f315i);
    }

    /* renamed from: a */
    private boolean m14a(MotionEvent motionEvent) {
        boolean z = true;
        if (this.f306H == null) {
            return false;
        }
        int i = this.f306H.x;
        int i2 = this.f306H.y;
        if (this.f303E.contains(i, i2)) {
            this.f299A = 1;
            this.f326t = m12b(motionEvent.getY());
        } else if (this.f302D.contains(i, i2)) {
            this.f299A = 0;
            float[] b = m11b(motionEvent.getX(), motionEvent.getY());
            this.f327u = b[0];
            this.f328v = b[1];
        } else if (this.f304F == null || !this.f304F.contains(i, i2)) {
            z = false;
        } else {
            this.f299A = 2;
            this.f325s = m10b((int) motionEvent.getX());
        }
        return z;
    }

    /* renamed from: b */
    private float m12b(float f) {
        RectF rectF = this.f303E;
        float height = rectF.height();
        return 360.0f - (((f < rectF.top ? 0.0f : f > rectF.bottom ? height : f - rectF.top) * 360.0f) / height);
    }

    /* renamed from: b */
    private int m10b(int i) {
        RectF rectF = this.f304F;
        int width = (int) rectF.width();
        return 255 - (((((float) i) < rectF.left ? 0 : ((float) i) > rectF.right ? width : i - ((int) rectF.left)) * 255) / width);
    }

    /* renamed from: b */
    private int m9b(int i, int i2) {
        return (i == Integer.MIN_VALUE || i == 1073741824) ? i2 : getPrefferedHeight();
    }

    /* renamed from: b */
    private void m13b() {
        this.f314h = new Paint();
        this.f315i = new Paint();
        this.f316j = new Paint();
        this.f317k = new Paint();
        this.f318l = new Paint();
        this.f319m = new Paint();
        this.f320n = new Paint();
        this.f315i.setStyle(Paint.Style.STROKE);
        this.f315i.setStrokeWidth(this.f312f * 2.0f);
        this.f315i.setAntiAlias(true);
        this.f317k.setColor(this.f330x);
        this.f317k.setStyle(Paint.Style.STROKE);
        this.f317k.setStrokeWidth(this.f312f * 2.0f);
        this.f317k.setAntiAlias(true);
        this.f319m.setColor(-14935012);
        this.f319m.setTextSize(14.0f * this.f312f);
        this.f319m.setAntiAlias(true);
        this.f319m.setTextAlign(Paint.Align.CENTER);
        this.f319m.setFakeBoldText(true);
    }

    /* renamed from: b */
    private void m8b(Canvas canvas) {
        RectF rectF = this.f303E;
        this.f320n.setColor(this.f331y);
        canvas.drawRect(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, 1.0f + rectF.bottom, this.f320n);
        if (this.f323q == null) {
            this.f323q = new LinearGradient(rectF.left, rectF.top, rectF.left, rectF.bottom, m5d(), null, Shader.TileMode.CLAMP);
            this.f316j.setShader(this.f323q);
        }
        canvas.drawRect(rectF, this.f316j);
        float f = (4.0f * this.f312f) / 2.0f;
        Point a = m20a(this.f326t);
        RectF rectF2 = new RectF();
        rectF2.left = rectF.left - this.f311e;
        rectF2.right = rectF.right + this.f311e;
        rectF2.top = a.y - f;
        rectF2.bottom = f + a.y;
        canvas.drawRoundRect(rectF2, 2.0f, 2.0f, this.f317k);
    }

    /* renamed from: b */
    private float[] m11b(float f, float f2) {
        float f3 = 0.0f;
        RectF rectF = this.f302D;
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

    /* renamed from: c */
    private float m7c() {
        return Math.max(Math.max(this.f310d, this.f311e), 1.0f * this.f312f) * 1.5f;
    }

    /* renamed from: c */
    private void m6c(Canvas canvas) {
        if (this.f332z && this.f304F != null && this.f305G != null) {
            RectF rectF = this.f304F;
            this.f320n.setColor(this.f331y);
            canvas.drawRect(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, 1.0f + rectF.bottom, this.f320n);
            this.f305G.draw(canvas);
            float[] fArr = {this.f326t, this.f327u, this.f328v};
            this.f324r = new LinearGradient(rectF.left, rectF.top, rectF.right, rectF.top, Color.HSVToColor(fArr), Color.HSVToColor(0, fArr), Shader.TileMode.CLAMP);
            this.f318l.setShader(this.f324r);
            canvas.drawRect(rectF, this.f318l);
            if (!(this.f329w == null || this.f329w.equals(""))) {
                canvas.drawText(this.f329w, rectF.centerX(), rectF.centerY() + (this.f312f * 4.0f), this.f319m);
            }
            float f = (this.f312f * 4.0f) / 2.0f;
            Point a = m18a(this.f325s);
            RectF rectF2 = new RectF();
            rectF2.left = a.x - f;
            rectF2.right = f + a.x;
            rectF2.top = rectF.top - this.f311e;
            rectF2.bottom = rectF.bottom + this.f311e;
            canvas.drawRoundRect(rectF2, 2.0f, 2.0f, this.f317k);
        }
    }

    /* renamed from: d */
    private int[] m5d() {
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

    /* renamed from: e */
    private void m4e() {
        RectF rectF = this.f301C;
        float height = rectF.height() - 2.0f;
        if (this.f332z) {
            height -= this.f309c + this.f308b;
        }
        float f = rectF.left + 1.0f;
        float f2 = rectF.top + 1.0f;
        this.f302D = new RectF(f, f2, height + f, f2 + height);
    }

    /* renamed from: f */
    private void m3f() {
        RectF rectF = this.f301C;
        this.f303E = new RectF((rectF.right - this.f307a) + 1.0f, rectF.top + 1.0f, rectF.right - 1.0f, (rectF.bottom - 1.0f) - (this.f332z ? this.f309c + this.f308b : 0.0f));
    }

    /* renamed from: g */
    private void m2g() {
        if (this.f332z) {
            RectF rectF = this.f301C;
            this.f304F = new RectF(rectF.left + 1.0f, (rectF.bottom - this.f308b) + 1.0f, rectF.right - 1.0f, rectF.bottom - 1.0f);
            this.f305G = new C0078a((int) (5.0f * this.f312f));
            this.f305G.setBounds(Math.round(this.f304F.left), Math.round(this.f304F.top), Math.round(this.f304F.right), Math.round(this.f304F.bottom));
        }
    }

    private int getPrefferedHeight() {
        int i = (int) (200.0f * this.f312f);
        return this.f332z ? (int) (i + this.f309c + this.f308b) : i;
    }

    private int getPrefferedWidth() {
        int prefferedHeight = getPrefferedHeight();
        if (this.f332z) {
            prefferedHeight = (int) (prefferedHeight - (this.f309c + this.f308b));
        }
        return (int) (prefferedHeight + this.f307a + this.f309c);
    }

    /* renamed from: a */
    public void m16a(int i, boolean z) {
        int alpha = Color.alpha(i);
        float[] fArr = new float[3];
        Color.colorToHSV(i, fArr);
        this.f325s = alpha;
        this.f326t = fArr[0];
        this.f327u = fArr[1];
        this.f328v = fArr[2];
        if (z && this.f313g != null) {
            this.f313g.m1a(Color.HSVToColor(this.f325s, new float[]{this.f326t, this.f327u, this.f328v}));
        }
        invalidate();
    }

    public String getAlphaSliderText() {
        return this.f329w;
    }

    public boolean getAlphaSliderVisible() {
        return this.f332z;
    }

    public int getBorderColor() {
        return this.f331y;
    }

    public int getColor() {
        return Color.HSVToColor(this.f325s, new float[]{this.f326t, this.f327u, this.f328v});
    }

    public float getDrawingOffset() {
        return this.f300B;
    }

    public int getSliderTrackerColor() {
        return this.f330x;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.f301C.width() > 0.0f && this.f301C.height() > 0.0f) {
            m15a(canvas);
            m8b(canvas);
            m6c(canvas);
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
        int a = m17a(mode, size);
        int b = m9b(mode2, size2);
        if (!this.f332z) {
            i4 = (int) ((a - this.f309c) - this.f307a);
            if (i4 > b || getTag().equals("landscape")) {
                i3 = (int) (b + this.f309c + this.f307a);
                i4 = b;
            } else {
                i3 = a;
            }
        } else {
            int i5 = (int) ((b - this.f308b) + this.f307a);
            if (i5 > a) {
                i4 = (int) ((a - this.f307a) + this.f308b);
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
        this.f301C = new RectF();
        this.f301C.left = this.f300B + getPaddingLeft();
        this.f301C.right = (i - this.f300B) - getPaddingRight();
        this.f301C.top = this.f300B + getPaddingTop();
        this.f301C.bottom = (i2 - this.f300B) - getPaddingBottom();
        m4e();
        m3f();
        m2g();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean a;
        switch (motionEvent.getAction()) {
            case 0:
                this.f306H = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                a = m14a(motionEvent);
                break;
            case 1:
                this.f306H = null;
                a = m14a(motionEvent);
                break;
            case 2:
                a = m14a(motionEvent);
                break;
            default:
                a = false;
                break;
        }
        if (!a) {
            return super.onTouchEvent(motionEvent);
        }
        if (this.f313g != null) {
            this.f313g.m1a(Color.HSVToColor(this.f325s, new float[]{this.f326t, this.f327u, this.f328v}));
        }
        invalidate();
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x001e  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTrackballEvent(android.view.MotionEvent r10) {
        /*
            r9 = this;
            r8 = 1092616192(0x41200000, float:10.0)
            r3 = 0
            r1 = 1065353216(0x3f800000, float:1.0)
            r4 = 1
            r0 = 0
            float r2 = r10.getX()
            float r6 = r10.getY()
            int r5 = r10.getAction()
            r7 = 2
            if (r5 != r7) goto L_0x001b
            int r5 = r9.f299A
            switch(r5) {
                case 0: goto L_0x0042;
                case 1: goto L_0x0069;
                case 2: goto L_0x007f;
                default: goto L_0x001b;
            }
        L_0x001b:
            r0 = r3
        L_0x001c:
            if (r0 == 0) goto L_0x009e
            com.xlocker.support.preference.colorpicker.ColorPickerView$a r0 = r9.f313g
            if (r0 == 0) goto L_0x003d
            com.xlocker.support.preference.colorpicker.ColorPickerView$a r0 = r9.f313g
            int r1 = r9.f325s
            r2 = 3
            float[] r2 = new float[r2]
            float r5 = r9.f326t
            r2[r3] = r5
            float r3 = r9.f327u
            r2[r4] = r3
            r3 = 2
            float r5 = r9.f328v
            r2[r3] = r5
            int r1 = android.graphics.Color.HSVToColor(r1, r2)
            r0.m1a(r1)
        L_0x003d:
            r9.invalidate()
            r0 = r4
        L_0x0041:
            return r0
        L_0x0042:
            float r5 = r9.f327u
            r7 = 1112014848(0x42480000, float:50.0)
            float r2 = r2 / r7
            float r5 = r5 + r2
            float r2 = r9.f328v
            r7 = 1112014848(0x42480000, float:50.0)
            float r6 = r6 / r7
            float r2 = r2 - r6
            int r6 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1))
            if (r6 >= 0) goto L_0x005d
            r5 = r0
        L_0x0053:
            int r6 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
            if (r6 >= 0) goto L_0x0063
        L_0x0057:
            r9.f327u = r5
            r9.f328v = r0
            r0 = r4
            goto L_0x001c
        L_0x005d:
            int r6 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r6 <= 0) goto L_0x0053
            r5 = r1
            goto L_0x0053
        L_0x0063:
            int r0 = (r2 > r1 ? 1 : (r2 == r1 ? 0 : -1))
            if (r0 <= 0) goto L_0x00a5
            r0 = r1
            goto L_0x0057
        L_0x0069:
            float r1 = r9.f326t
            float r2 = r6 * r8
            float r1 = r1 - r2
            int r2 = (r1 > r0 ? 1 : (r1 == r0 ? 0 : -1))
            if (r2 >= 0) goto L_0x0076
        L_0x0072:
            r9.f326t = r0
            r0 = r4
            goto L_0x001c
        L_0x0076:
            r0 = 1135869952(0x43b40000, float:360.0)
            int r0 = (r1 > r0 ? 1 : (r1 == r0 ? 0 : -1))
            if (r0 <= 0) goto L_0x00a3
            r0 = 1135869952(0x43b40000, float:360.0)
            goto L_0x0072
        L_0x007f:
            boolean r0 = r9.f332z
            if (r0 == 0) goto L_0x0087
            android.graphics.RectF r0 = r9.f304F
            if (r0 != 0) goto L_0x0089
        L_0x0087:
            r0 = r3
            goto L_0x001c
        L_0x0089:
            int r0 = r9.f325s
            float r0 = (float) r0
            float r1 = r2 * r8
            float r0 = r0 - r1
            int r0 = (int) r0
            if (r0 >= 0) goto L_0x0097
            r0 = r3
        L_0x0093:
            r9.f325s = r0
            r0 = r4
            goto L_0x001c
        L_0x0097:
            r1 = 255(0xff, float:3.57E-43)
            if (r0 <= r1) goto L_0x0093
            r0 = 255(0xff, float:3.57E-43)
            goto L_0x0093
        L_0x009e:
            boolean r0 = super.onTrackballEvent(r10)
            goto L_0x0041
        L_0x00a3:
            r0 = r1
            goto L_0x0072
        L_0x00a5:
            r0 = r2
            goto L_0x0057
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xlocker.support.preference.colorpicker.ColorPickerView.onTrackballEvent(android.view.MotionEvent):boolean");
    }

    public void setAlphaSliderText(int i) {
        setAlphaSliderText(getContext().getString(i));
    }

    public void setAlphaSliderText(String str) {
        this.f329w = str;
        invalidate();
    }

    public void setAlphaSliderVisible(boolean z) {
        if (this.f332z != z) {
            this.f332z = z;
            this.f321o = null;
            this.f322p = null;
            this.f323q = null;
            this.f324r = null;
            requestLayout();
        }
    }

    public void setBorderColor(int i) {
        this.f331y = i;
        invalidate();
    }

    public void setColor(int i) {
        m16a(i, false);
    }

    public void setOnColorChangedListener(AbstractC0077a aVar) {
        this.f313g = aVar;
    }

    public void setSliderTrackerColor(int i) {
        this.f330x = i;
        this.f317k.setColor(this.f330x);
        invalidate();
    }
}
