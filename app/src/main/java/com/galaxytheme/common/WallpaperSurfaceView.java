package com.galaxytheme.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.xlocker.core.sdk.LogUtil;

public class WallpaperSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private KeyguardSecurityView mKeyguardSecurityView;

    private Handler mHandler;

    /* renamed from: c */
    private final Rect f175c;

    /* renamed from: d */
    private final Rect f176d;

    /* renamed from: e */
    private final Rect f177e;

    /* renamed from: f */
    private final Rect f178f;

    /* renamed from: g */
    private final Rect f179g;

    public WallpaperSurfaceView(Context context, Handler mHandler, Rect f175c, Rect f176d, Rect f177e, Rect f178f, Rect f179g) {
        super(context);
        this.mHandler = mHandler;
        this.f175c = f175c;
        this.f176d = f176d;
        this.f177e = f177e;
        this.f178f = f178f;
        this.f179g = f179g;
    }

    /* renamed from: a */
    private void m110a() {
        this.mHandler.removeMessages(1001);
        this.mHandler.sendEmptyMessage(1001);
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void dispatchDraw(Canvas canvas) {
    }

    @Override // android.view.SurfaceView, android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        LogUtil.i("draw", "WallpaperSurfaceView, onSizeChanged, w = " + i + ", h = " + i2);
        this.f176d.set(0, 0, this.f175c.left, i2);
        this.f177e.set(0, 0, getWidth(), this.f175c.top);
        this.f178f.set(i - this.f175c.right, 0, i, i2);
        this.f179g.set(0, i2 - this.f175c.bottom, i, i2);
        m110a();
        super.onSizeChanged(i, i2, i3, i4);
    }

    public void setCallback(KeyguardSecurityView cVar) {
        this.mKeyguardSecurityView = cVar;
    }

    public void setWindowInsets(Rect rect) {
        LogUtil.i("draw", "WallpaperSurfaceView, setWindowInsets, inset = " + rect);
        if (rect != null) {
            this.f175c.set(rect);
            this.f176d.set(0, 0, rect.left, getHeight());
            this.f177e.set(0, 0, getWidth(), rect.top);
            this.f178f.set(getWidth() - rect.right, 0, getWidth(), getHeight());
            this.f179g.set(0, getHeight() - rect.bottom, getWidth(), getHeight());
            m110a();
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        m110a();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
