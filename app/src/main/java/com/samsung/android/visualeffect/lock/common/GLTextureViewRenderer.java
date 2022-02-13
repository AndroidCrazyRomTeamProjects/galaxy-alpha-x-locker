package com.samsung.android.visualeffect.lock.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.samsung.android.visualeffect.IEffectListener;
import com.samsung.android.visualeffect.common.GLTextureView;
import com.xlocker.core.sdk.LogUtil;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLTextureViewRenderer implements GLTextureView.Renderer {
    protected String TAG;
    protected IEffectListener callBackListener;
    protected boolean isAffordanceOccur;
    protected Context mContext;
    protected GLTextureView mGlView;
    protected String mLibName;
    protected int mWidth = 0;
    protected int mHeight = 0;
    protected int mAffordancePosX = 0;
    protected int mAffordancePosY = 0;
    protected long timeStart = 0;
    protected int framecounter = -1;
    protected int[] mBackgroundPixels = null;
    protected int mBackgroundWidth = 0;
    protected int mBackgroundHeight = 0;
    protected boolean mIsNeedToReinit = false;
    protected boolean mIsNeedToReinit2 = false;
    private String mLibDir = null;
    protected final Native mNative = new Native();
    protected int drawCount = 0;
    protected int drawInitNum = 3;
    String[] mSpecialTextures = null;

    @Override // com.samsung.android.visualeffect.common.GLTextureView.Renderer
    public void onDestroy() {
        LogUtil.i(this.TAG, "onDestroy()");
        if (this.drawCount > 0) {
            this.mNative.destroy();
        } else {
            LogUtil.i(this.TAG, "Ignore! because isRendered is false");
        }
    }

    public void showUnlockAffordance(int posX, int posY) {
        LogUtil.i(this.TAG, "showUnlockAffordance");
        this.mAffordancePosX = posX;
        this.mAffordancePosY = posY;
        this.isAffordanceOccur = true;
        this.mGlView.setRenderMode(1);
    }

    public void handleTouchEvent(int action, int x, int y) {
        if (this.drawCount <= this.drawInitNum) {
            LogUtil.d(this.TAG, "drawCount = " + this.drawCount + ", Touch Return");
            return;
        }
        if (!(action == 2 || action == 7)) {
            LogUtil.i(this.TAG, "Renderer handleTouchEvent action = " + action);
        }
        switch (action) {
            case 0:
                this.mNative.onTouch(x, y, 0);
                break;
            case 1:
                this.mNative.onTouch(x, y, 1);
                break;
            case 2:
                this.mNative.onTouch(x, y, 2);
                break;
            case 7:
                this.mNative.onTouch(x, y, 5);
                break;
            case 9:
                this.mNative.onTouch(x, y, 3);
                break;
            case 10:
                this.mNative.onTouch(x, y, 4);
                break;
        }
        this.mGlView.setRenderMode(1);
    }

    public void setListener(IEffectListener listener) {
        this.callBackListener = listener;
    }

    @Override // com.samsung.android.visualeffect.common.GLTextureView.Renderer
    /* renamed from: a */
    public void onDrawFrame(GL10 gl10) {
        boolean z = true;
        if (this.mIsNeedToReinit) {
            this.mSpecialTextures = this.mNative.loadEffect(this.mLibName);
            this.framecounter = -1;
        }
        if (this.framecounter == -1) {
            this.timeStart = System.currentTimeMillis();
        }
        if (this.mBackgroundPixels != null) {
            LogUtil.d(this.TAG, "mNative.loadTexture");
            this.mNative.loadTexture("bg", this.mBackgroundPixels, this.mBackgroundWidth, this.mBackgroundHeight);
            this.mBackgroundPixels = null;
        }
        if (this.mIsNeedToReinit) {
            this.mNative.drawBgOnly(this.mWidth, this.mHeight);
            this.mIsNeedToReinit = false;
            this.mIsNeedToReinit2 = true;
            return;
        }
        if (this.mIsNeedToReinit2) {
            loadSpecialTexture(this.mSpecialTextures);
            this.mNative.init(this.mWidth, this.mHeight, true);
            this.mIsNeedToReinit2 = false;
        }
        if (this.isAffordanceOccur) {
            this.mNative.showAffordance(this.mAffordancePosX, this.mAffordancePosY);
            this.isAffordanceOccur = false;
        }
        if (this.mNative.draw() || this.drawCount <= this.drawInitNum) {
            z = false;
        } else {
            LogUtil.i(this.TAG, "dirty mode");
            this.mGlView.setRenderMode(0);
        }
        this.framecounter++;
        float currentTimeMillis = (float) (System.currentTimeMillis() - this.timeStart);
        if (currentTimeMillis >= 1000.0f || z) {
            if (this.framecounter > 2) {
                LogUtil.i(this.TAG, "fps " + ((this.framecounter * 1000.0f) / currentTimeMillis));
            }
            this.framecounter = -1;
        }
        if (this.drawCount <= this.drawInitNum) {
            if (this.drawCount == 0) {
                LogUtil.i(this.TAG, "onDrawFrame, First Rendering!");
            }
            if (this.drawCount == this.drawInitNum && this.callBackListener != null) {
                LogUtil.i(this.TAG, "callBackListener.onReceive(EffectStatus.READY");
                this.callBackListener.onReceive(0, null);
            }
            this.drawCount++;
        }
    }

    @Override // com.samsung.android.visualeffect.common.GLTextureView.Renderer
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        int max = Math.max(this.mWidth, this.mHeight) / 5;
        if (width < max || height < max) {
            LogUtil.i(this.TAG, "onSurfaceChanged problem width " + width + " " + height + "  disp " + this.mWidth + " " + this.mHeight);
            return;
        }
        if (!(this.mWidth == width && this.mHeight == height)) {
            this.mIsNeedToReinit = true;
        }
        this.mWidth = width;
        this.mHeight = height;
        this.mGlView.setRenderMode(1);
        LogUtil.i(this.TAG, "onSurfaceChanged " + width + " " + height);
    }

    @Override // com.samsung.android.visualeffect.common.GLTextureView.Renderer
    @SuppressLint({"NewApi"})
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        LogUtil.i(this.TAG, "onSurfaceCreated");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics);
        this.mWidth = displayMetrics.widthPixels;
        this.mHeight = displayMetrics.heightPixels;
        this.mIsNeedToReinit = true;
        this.drawCount = 0;
        this.mLibDir = this.mContext.getApplicationInfo().nativeLibraryDir;
        this.mLibName = this.mLibDir + "/" + this.mLibName;
        LogUtil.d(this.TAG, "mLibName = " + this.mLibName);
    }

    public void setBackgroundBitmap(int[] pixels, int width, int height) {
        LogUtil.i(this.TAG, "setBackgroundBitmap");
        this.mBackgroundPixels = pixels;
        this.mBackgroundWidth = width;
        this.mBackgroundHeight = height;
        this.mGlView.setRenderMode(1);
    }

    public void setParameters(int[] aNums, float[] aValues) {
        this.mNative.setParameters(aNums, aValues);
    }

    public void loadSpecialTexture(String[] aTexture) {
        if (aTexture != null) {
            new BitmapFactory.Options().inScaled = false;
            if (this.mContext != null) {
                Resources resources = this.mContext.getResources();
                for (int i = 0; i < aTexture.length; i++) {
                    try {
                        Bitmap decodeResource = BitmapFactory.decodeResource(resources, resources.getIdentifier(aTexture[i], "drawable", this.mContext.getPackageName()));
                        int width = decodeResource.getWidth();
                        int height = decodeResource.getHeight();
                        int[] pixels = new int[width * height];
                        decodeResource.getPixels(pixels, 0, width, 0, 0, width, height);
                        LogUtil.i("", "Adding texture Width'" + width + "'");
                        LogUtil.i("", "Adding texture Height'" + height + "'");
                        this.mNative.loadTexture(aTexture[i], pixels, width, height);
                        decodeResource.recycle();
                    } catch (Exception e) {
                        LogUtil.e("", "There is no image '" + aTexture[i] + "'");
                    }
                }
                this.mGlView.setRenderMode(1);
            }
        }
    }

    public void clearEffect() {
        LogUtil.i(this.TAG, "clearEffect()");
        if (this.drawCount > this.drawInitNum) {
            this.mNative.clear();
            this.mGlView.setRenderMode(1);
            return;
        }
        LogUtil.i(this.TAG, "Ignore! because isRendered is false");
    }

    /* renamed from: c */
    public void showUnlock() {
        LogUtil.i(this.TAG, "showUnlock()");
        if (this.drawCount > this.drawInitNum) {
            this.mNative.showUnlock();
            this.mGlView.setRenderMode(1);
            return;
        }
        LogUtil.i(this.TAG, "Ignore! because isRendered is false");
    }
}
