package com.samsung.android.visualeffect.common;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.TextureView;
import com.xlocker.core.sdk.LogUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class GLTextureView extends TextureView implements TextureView.SurfaceTextureListener {

    private static final GLThreadManager sGLThreadManager = new GLThreadManager();
    private boolean mDetached;
    private EGLConfigChooser mEGLConfigChooser;
    private int mEGLContextClientVersion;
    private EGLContextFactory mEGLContextFactory;
    private EGLWindowSurfaceFactory mEGLWindowSurfaceFactory;
    private GLThread mGLThread;
    private boolean mPreserveEGLContextOnPause;
    private Renderer mRenderer;
    private final WeakReference<GLTextureView> mThisWeakRef = new WeakReference<>(this);

    private abstract class BaseConfigChooser implements EGLConfigChooser {

        protected int[] mConfigSpec;

        public BaseConfigChooser(int[] configSpec) {
            this.mConfigSpec = filterConfigSpec(configSpec);
        }

        private int[] filterConfigSpec(int[] configSpec) {
            if (GLTextureView.this.mEGLContextClientVersion != 2) {
                return configSpec;
            }
            int length = configSpec.length;
            int[] newConfigSpec = new int[length + 2];
            System.arraycopy(configSpec, 0, newConfigSpec, 0, length - 1);
            newConfigSpec[length - 1] = 12352;
            newConfigSpec[length] = 4;
            newConfigSpec[length + 1] = 12344;
            return newConfigSpec;
        }

        @Override // com.samsung.android.visualeffect.common.GLTextureView.EGLConfigChooser
        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
            int[] num_config = {0};
            if (!egl10.eglChooseConfig(eGLDisplay, this.mConfigSpec, null, 0, num_config)) {
                throw new IllegalArgumentException("eglChooseConfig failed");
            }
            int numConfigs = num_config[0];
            if (numConfigs <= 0) {
                throw new IllegalArgumentException("No configs match configSpec");
            }
            EGLConfig[] configs = new EGLConfig[numConfigs];
            if (!egl10.eglChooseConfig(eGLDisplay, this.mConfigSpec, configs, numConfigs, num_config)) {
                throw new IllegalArgumentException("eglChooseConfig#2 failed");
            }
            EGLConfig config = chooseConfig(egl10, eGLDisplay, configs);
            if (config != null) {
                return config;
            }
            throw new IllegalArgumentException("No config chosen");
        }

        abstract EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);
    }

    private class ComponentSizeChooser extends BaseConfigChooser {
        protected int mAlphaSize;
        protected int mBlueSize;
        protected int mDepthSize;
        protected int mGreenSize;
        protected int mRedSize;
        protected int mStencilSize;
        private int[] mValue = new int[1];

        public ComponentSizeChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
            super(new int[]{12324, redSize, 12323, greenSize, 12322, blueSize, 12321, alphaSize, 12325, depthSize, 12326, stencilSize, 12344});
            this.mRedSize = redSize;
            this.mGreenSize = greenSize;
            this.mBlueSize = blueSize;
            this.mAlphaSize = alphaSize;
            this.mDepthSize = depthSize;
            this.mStencilSize = stencilSize;
        }

        private int findConfigAttrib(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int attribute, int defaultValue) {
            return egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, attribute, this.mValue) ? this.mValue[0] : defaultValue;
        }

        @Override // com.samsung.android.visualeffect.p001a.TextureView$SurfaceTextureListenerC0049a.AbstractC0001a
        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] configs) {
            for (EGLConfig config : configs) {
                int d = findConfigAttrib(egl10, eGLDisplay, config, 12325, 0);
                int s = findConfigAttrib(egl10, eGLDisplay, config, 12326, 0);
                if (d >= this.mDepthSize && s >= this.mStencilSize) {
                    int r = findConfigAttrib(egl10, eGLDisplay, config, 12324, 0);
                    int g = findConfigAttrib(egl10, eGLDisplay, config, 12323, 0);
                    int b = findConfigAttrib(egl10, eGLDisplay, config, 12322, 0);
                    int a = findConfigAttrib(egl10, eGLDisplay, config, 12321, 0);
                    if (r == this.mRedSize && g == this.mGreenSize && b == this.mBlueSize && a == this.mAlphaSize) {
                        return config;
                    }
                }
            }
            return null;
        }
    }

    private class DefaultContextFactory implements EGLContextFactory {

        private int EGL_CONTEXT_CLIENT_VERSION;

        private DefaultContextFactory() {
            this.EGL_CONTEXT_CLIENT_VERSION = 12440;
        }

        @Override // com.samsung.android.visualeffect.p001a.TextureView$SurfaceTextureListenerC0049a.AbstractC0055f
        public EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
            int[] attrib_list = {this.EGL_CONTEXT_CLIENT_VERSION, GLTextureView.this.mEGLContextClientVersion, 12344};
            EGLContext eGLContext = EGL10.EGL_NO_CONTEXT;
            if (GLTextureView.this.mEGLContextClientVersion == 0) {
                attrib_list = null;
            }
            return egl10.eglCreateContext(eGLDisplay, eGLConfig, eGLContext, attrib_list);
        }

        @Override // com.samsung.android.visualeffect.p001a.TextureView$SurfaceTextureListenerC0049a.AbstractC0055f
        public void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
            if (!egl10.eglDestroyContext(eGLDisplay, eGLContext)) {
                LogUtil.e("DefaultContextFactory", "display:" + eGLDisplay + " context: " + eGLContext);
                EglHelper.throwEglException("eglDestroyContex", egl10.eglGetError());
            }
        }
    }

    private static class DefaultWindowSurfaceFactory implements EGLWindowSurfaceFactory {
        private DefaultWindowSurfaceFactory() {
        }

        @Override // com.samsung.android.visualeffect.p001a.TextureView$SurfaceTextureListenerC0049a.AbstractC0056g
        public EGLSurface createWindowSurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object nativeWindow) {
            try {
                return egl10.eglCreateWindowSurface(eGLDisplay, eGLConfig, nativeWindow, null);
            } catch (IllegalArgumentException e) {
                LogUtil.e("GLTextureView", "eglCreateWindowSurface", e);
                return null;
            }
        }

        @Override // com.samsung.android.visualeffect.p001a.TextureView$SurfaceTextureListenerC0049a.AbstractC0056g
        public void destroySurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface) {
            egl10.eglDestroySurface(eGLDisplay, eGLSurface);
        }
    }

    public interface EGLConfigChooser {
        EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay);
    }

    public interface EGLContextFactory {
        EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig);

        void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext);
    }

    public interface EGLWindowSurfaceFactory {
        EGLSurface createWindowSurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object nativeWindow);

        void destroySurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface);
    }

    private static class EglHelper {
        EGL10 mEgl;
        EGLConfig mEglConfig;
        EGLContext mEglContext;
        EGLDisplay mEglDisplay;
        EGLSurface mEglSurface;
        private WeakReference<GLTextureView> mGLTextureViewWeakRef;

        public EglHelper(WeakReference<GLTextureView> weakReference) {
            this.mGLTextureViewWeakRef = weakReference;
        }

        private void throwEglException(String function) {
            throwEglException(function, this.mEgl.eglGetError());
        }

        public static void throwEglException(String function, int error) {
            throw new RuntimeException(function + " " + error);
        }

        public static void logEglErrorAsWarning(String tag, String function, int error) {
            LogUtil.w(tag, function + " " + error);
        }

        private void destroySurfaceImp() {
            if (this.mEglSurface != null && this.mEglSurface != EGL10.EGL_NO_SURFACE) {
                this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                GLTextureView view = this.mGLTextureViewWeakRef.get();
                if (view != null) {
                    view.mEGLWindowSurfaceFactory.destroySurface(this.mEgl, this.mEglDisplay, this.mEglSurface);
                }
                this.mEglSurface = null;
            }
        }

        GL createGL() {
            return this.mEglContext.getGL();
        }

        public boolean createSurface() {
            if (this.mEgl == null) {
                throw new RuntimeException("egl not initialized");
            } else if (this.mEglDisplay == null) {
                throw new RuntimeException("eglDisplay not initialized");
            } else if (this.mEglConfig == null) {
                throw new RuntimeException("mEglConfig not initialized");
            } else {
                destroySurfaceImp();
                GLTextureView view = this.mGLTextureViewWeakRef.get();
                if (view != null) {
                    this.mEglSurface = view.mEGLWindowSurfaceFactory.createWindowSurface(this.mEgl, this.mEglDisplay, this.mEglConfig, view.getSurfaceTexture());
                } else {
                    this.mEglSurface = null;
                }
                if (this.mEglSurface == null || this.mEglSurface == EGL10.EGL_NO_SURFACE) {
                    if (this.mEgl.eglGetError() == 12299) {
                        LogUtil.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
                    }
                    return false;
                } else if (this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext)) {
                    return true;
                } else {
                    logEglErrorAsWarning("EGLHelper", "eglMakeCurrent", this.mEgl.eglGetError());
                    return false;
                }
            }
        }

        public void destroySurface() {
            destroySurfaceImp();
        }

        public void finish() {
            if (this.mEglContext != null) {
                GLTextureView view = this.mGLTextureViewWeakRef.get();
                if (view != null) {
                    view.mEGLContextFactory.destroyContext(this.mEgl, this.mEglDisplay, this.mEglContext);
                }
                this.mEglContext = null;
            }
            if (this.mEglDisplay != null) {
                this.mEgl.eglTerminate(this.mEglDisplay);
                this.mEglDisplay = null;
            }
        }

        public void start() {
            this.mEgl = (EGL10) EGLContext.getEGL();
            this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (this.mEglDisplay == EGL10.EGL_NO_DISPLAY) {
                throw new RuntimeException("eglGetDisplay failed");
            } else if (!this.mEgl.eglInitialize(this.mEglDisplay, new int[2])) {
                throw new RuntimeException("eglInitialize failed");
            } else {
                GLTextureView view = this.mGLTextureViewWeakRef.get();
                if (view == null) {
                    this.mEglConfig = null;
                    this.mEglContext = null;
                } else {
                    this.mEglConfig = view.mEGLConfigChooser.chooseConfig(this.mEgl, this.mEglDisplay);
                    this.mEglContext = view.mEGLContextFactory.createContext(this.mEgl, this.mEglDisplay, this.mEglConfig);
                }
                if (this.mEglContext == null || this.mEglContext == EGL10.EGL_NO_CONTEXT) {
                    this.mEglContext = null;
                    throwEglException("createContext");
                }
                this.mEglSurface = null;
            }
        }

        public int swap() {
            if (!this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurface)) {
                return this.mEgl.eglGetError();
            }
            return 12288;
        }
    }

    static class GLThread extends Thread {

        private EglHelper mEglHelper;
        private boolean mExited;
        private boolean mFinishedCreatingEglSurface;
        private WeakReference<GLTextureView> mGLTextureViewWeakRef;
        private boolean mHasSurface;
        private boolean mHaveEglContext;
        private boolean mHaveEglSurface;
        private boolean mPaused;
        private boolean mRenderComplete;
        private boolean mRequestPaused;
        private boolean mShouldExit;
        private boolean mShouldReleaseEglContext;
        private boolean mSurfaceIsBad;
        private boolean mWaitingForSurface;
        protected long prevFPSTime = 0;
        protected long currFPSTime = 0;
        protected long lastUpdateTime = 0;
        private ArrayList<Runnable> mEventQueue = new ArrayList<>();
        private boolean mSizeChanged = true;
        private int mWidth = 0;
        private int mHeight = 0;
        private boolean mRequestRender = true;
        private int mRenderMode = 1;

        GLThread(WeakReference<GLTextureView> weakReference) {
            this.mGLTextureViewWeakRef = weakReference;
        }

        private void guardedRun() throws InterruptedException {
            boolean z = false;
            //int w;
            //boolean createEglSurface;
            //boolean wantRenderNotification;
            boolean z4 = false;
            //int h;
            boolean z5;
            boolean z6;
            GL10 gl10;
            this.mEglHelper = new EglHelper(this.mGLTextureViewWeakRef);
            this.mHaveEglContext = false;
            this.mHaveEglSurface = false;
            GL10 gl102 = null;
            boolean createEglContext = false;
            boolean createEglSurface = false;
            boolean createGlInterface = false;
            boolean lostEglContext = false;
            boolean sizeChanged = false;
            boolean wantRenderNotification = false;
            boolean doRenderNotification = false;
            boolean askedToReleaseEglContext = false;
            int w = 0;
            int h = 0;
            Runnable runnable = null;
            while (true) {
                try {
                    synchronized (GLTextureView.sGLThreadManager) {
                        while (!this.mShouldExit) {
                            if (!this.mEventQueue.isEmpty()) {
                                runnable = this.mEventQueue.remove(0);
                                z = doRenderNotification;
                                w = w;
                                createEglSurface = createEglSurface;
                                createGlInterface = createGlInterface;
                                lostEglContext = lostEglContext;
                                sizeChanged = sizeChanged;
                                wantRenderNotification = wantRenderNotification;
                                z4 = askedToReleaseEglContext;
                                h = h;
                            } else {
                                boolean pausing = false;
                                if (this.mPaused != this.mRequestPaused) {
                                    pausing = this.mRequestPaused;
                                    this.mPaused = this.mRequestPaused;
                                    GLTextureView.sGLThreadManager.notifyAll();
                                }
                                if (this.mShouldReleaseEglContext) {
                                    stopEglSurfaceLocked();
                                    stopEglContextLocked();
                                    this.mShouldReleaseEglContext = false;
                                    askedToReleaseEglContext = true;
                                }
                                if (lostEglContext) {
                                    stopEglSurfaceLocked();
                                    stopEglContextLocked();
                                    lostEglContext = false;
                                }
                                if (pausing && this.mHaveEglSurface) {
                                    stopEglSurfaceLocked();
                                }
                                if (pausing && this.mHaveEglContext) {
                                    GLTextureView view = this.mGLTextureViewWeakRef.get();
                                    if (!(view == null ? false : view.mPreserveEGLContextOnPause) || GLTextureView.sGLThreadManager.shouldReleaseEGLContextWhenPausing()) {
                                        stopEglContextLocked();
                                    }
                                }
                                if (pausing && GLTextureView.sGLThreadManager.shouldTerminateEGLWhenPausing()) {
                                    this.mEglHelper.finish();
                                }
                                if (!this.mHasSurface && !this.mWaitingForSurface) {
                                    if (this.mHaveEglSurface) {
                                        stopEglSurfaceLocked();
                                    }
                                    this.mWaitingForSurface = true;
                                    this.mSurfaceIsBad = false;
                                    GLTextureView.sGLThreadManager.notifyAll();
                                }
                                if (this.mHasSurface && this.mWaitingForSurface) {
                                    this.mWaitingForSurface = false;
                                    GLTextureView.sGLThreadManager.notifyAll();
                                }
                                if (doRenderNotification) {
                                    wantRenderNotification = false;
                                    doRenderNotification = false;
                                    this.mRenderComplete = true;
                                    GLTextureView.sGLThreadManager.notifyAll();
                                }
                                if (readyToDraw()) {
                                    if (!this.mHaveEglContext) {
                                        if (askedToReleaseEglContext) {
                                            askedToReleaseEglContext = false;
                                        } else if (GLTextureView.sGLThreadManager.tryAcquireEglContextLocked(this)) {
                                            try {
                                                this.mEglHelper.start();
                                                this.mHaveEglContext = true;
                                                createEglContext = true;
                                                GLTextureView.sGLThreadManager.notifyAll();
                                            } catch (RuntimeException e) {
                                                GLTextureView.sGLThreadManager.releaseEglContextLocked(this);
                                                throw e;
                                            }
                                        }
                                    }
                                    if (!this.mHaveEglContext || this.mHaveEglSurface) {
                                        z5 = sizeChanged;
                                        z6 = createGlInterface;
                                    } else {
                                        this.mHaveEglSurface = true;
                                        createEglSurface = true;
                                        z6 = true;
                                        z5 = true;
                                    }
                                    if (this.mHaveEglSurface) {
                                        if (this.mSizeChanged) {
                                            sizeChanged = true;
                                            w = this.mWidth;
                                            h = this.mHeight;
                                            wantRenderNotification = true;
                                            createEglSurface = true;
                                            this.mSizeChanged = false;
                                        } else {
                                            createEglSurface = createEglSurface;
                                            wantRenderNotification = wantRenderNotification;
                                            sizeChanged = z5;
                                            h = h;
                                            w = w;
                                        }
                                        this.mRequestRender = false;
                                        GLTextureView.sGLThreadManager.notifyAll();
                                        createGlInterface = z6;
                                        z = doRenderNotification;
                                        lostEglContext = lostEglContext;
                                        z4 = askedToReleaseEglContext;
                                    } else {
                                        createGlInterface = z6;
                                        sizeChanged = z5;
                                    }
                                }
                                GLTextureView.sGLThreadManager.wait();
                            }
                        }
                        this.mGLTextureViewWeakRef.get().mRenderer.onDestroy();
                        synchronized (GLTextureView.sGLThreadManager) {
                            try {
                                stopEglSurfaceLocked();
                                stopEglContextLocked();
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                    }
                    if (runnable != null) {
                        runnable.run();
                        runnable = null;
                        h = h;
                        askedToReleaseEglContext = z4;
                        wantRenderNotification = wantRenderNotification;
                        sizeChanged = sizeChanged;
                        lostEglContext = lostEglContext;
                        createGlInterface = createGlInterface;
                        createEglSurface = createEglSurface;
                        w = w;
                        doRenderNotification = z;
                    } else {
                        if (!createEglSurface) {
                            createEglSurface = createEglSurface;
                        } else if (this.mEglHelper.createSurface()) {
                            synchronized (GLTextureView.sGLThreadManager) {
                                this.mFinishedCreatingEglSurface = true;
                                GLTextureView.sGLThreadManager.notifyAll();
                            }
                            createEglSurface = false;
                        } else {
                            synchronized (GLTextureView.sGLThreadManager) {
                                this.mFinishedCreatingEglSurface = true;
                                this.mSurfaceIsBad = true;
                                GLTextureView.sGLThreadManager.notifyAll();
                            }
                            h = h;
                            askedToReleaseEglContext = z4;
                            wantRenderNotification = wantRenderNotification;
                            sizeChanged = sizeChanged;
                            lostEglContext = lostEglContext;
                            createGlInterface = createGlInterface;
                            createEglSurface = createEglSurface;
                            runnable = runnable;
                            w = w;
                            doRenderNotification = z;
                        }
                        if (createGlInterface) {
                            GL10 gl103 = (GL10) this.mEglHelper.createGL();
                            GLTextureView.sGLThreadManager.checkGLDriver(gl103);
                            createGlInterface = false;
                            gl10 = gl103;
                        } else {
                            gl10 = gl102;
                        }
                        if (createEglContext) {
                            GLTextureView view2 = this.mGLTextureViewWeakRef.get();
                            if (view2 != null) {
                                view2.mRenderer.onSurfaceCreated(gl10, this.mEglHelper.mEglConfig);
                            }
                            createEglContext = false;
                        }
                        if (sizeChanged) {
                            GLTextureView view3 = this.mGLTextureViewWeakRef.get();
                            if (view3 != null) {
                                view3.mRenderer.onSurfaceChanged(gl10, w, h);
                            }
                            sizeChanged = false;
                        }
                        GLTextureView view4 = this.mGLTextureViewWeakRef.get();
                        if (view4 != null) {
                            view4.mRenderer.onDrawFrame(gl10);
                        }
                        int swapError = this.mEglHelper.swap();
                        switch (swapError) {
                            case 12288:
                                break;
                            case 12302:
                                lostEglContext = true;
                                break;
                            default:
                                EglHelper.logEglErrorAsWarning("GLThread", "eglSwapBuffers", swapError);
                                synchronized (GLTextureView.sGLThreadManager) {
                                    this.mSurfaceIsBad = true;
                                    GLTextureView.sGLThreadManager.notifyAll();
                                    break;
                                }
                        }
                        doRenderNotification = wantRenderNotification ? true : z;
                        runnable = runnable;
                        gl102 = gl10;
                        w = w;
                        wantRenderNotification = wantRenderNotification;
                        h = h;
                        askedToReleaseEglContext = z4;
                    }
                } catch (Throwable th2) {
                    synchronized (GLTextureView.sGLThreadManager) {
                        try {
                            stopEglSurfaceLocked();
                            stopEglContextLocked();
                            throw th2;
                        } catch (Throwable th3) {
                            throw th3;
                        }
                    }
                }
            }
        }

        private boolean readyToDraw() {
            return !this.mPaused && this.mHasSurface && !this.mSurfaceIsBad && this.mWidth > 0 && this.mHeight > 0 && (this.mRequestRender || this.mRenderMode == 1);
        }

        private void stopEglContextLocked() {
            if (this.mHaveEglContext) {
                this.mEglHelper.finish();
                this.mHaveEglContext = false;
                GLTextureView.sGLThreadManager.releaseEglContextLocked(this);
            }
        }

        private void stopEglSurfaceLocked() {
            if (this.mHaveEglSurface) {
                this.mHaveEglSurface = false;
                this.mEglHelper.destroySurface();
            }
        }

        public void setRenderMode(int renderMode) {
            if (renderMode < 0 || renderMode > 1) {
                throw new IllegalArgumentException("renderMode");
            }
            synchronized (GLTextureView.sGLThreadManager) {
                this.mRenderMode = renderMode;
                GLTextureView.sGLThreadManager.notifyAll();
            }
        }

        public void onWindowResize(int w, int h) {
            synchronized (GLTextureView.sGLThreadManager) {
                this.mWidth = w;
                this.mHeight = h;
                this.mSizeChanged = true;
                this.mRequestRender = true;
                this.mRenderComplete = false;
                GLTextureView.sGLThreadManager.notifyAll();
                while (!this.mExited && !this.mPaused && !this.mRenderComplete && ableToDraw()) {
                    try {
                        GLTextureView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void queueEvent(Runnable runnable) {
            if (runnable == null) {
                throw new IllegalArgumentException("r must not be null");
            }
            synchronized (GLTextureView.sGLThreadManager) {
                this.mEventQueue.add(runnable);
                GLTextureView.sGLThreadManager.notifyAll();
            }
        }

        public boolean ableToDraw() {
            return this.mHaveEglContext && this.mHaveEglSurface && readyToDraw();
        }

        public int getRenderMode() {
            int i;
            synchronized (GLTextureView.sGLThreadManager) {
                i = this.mRenderMode;
            }
            return i;
        }

        public void requestExitAndWait() {
            synchronized (GLTextureView.sGLThreadManager) {
                this.mShouldExit = true;
                GLTextureView.sGLThreadManager.notifyAll();
                while (!this.mExited) {
                    try {
                        GLTextureView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void requestReleaseEglContextLocked() {
            this.mShouldReleaseEglContext = true;
            GLTextureView.sGLThreadManager.notifyAll();
        }

        public void requestRender() {
            synchronized (GLTextureView.sGLThreadManager) {
                this.mRequestRender = true;
                GLTextureView.sGLThreadManager.notifyAll();
            }
        }

        public void surfaceCreated() {
            synchronized (GLTextureView.sGLThreadManager) {
                this.mHasSurface = true;
                this.mFinishedCreatingEglSurface = false;
                GLTextureView.sGLThreadManager.notifyAll();
                while (this.mWaitingForSurface && !this.mFinishedCreatingEglSurface && !this.mExited) {
                    try {
                        GLTextureView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void surfaceDestroyed() {
            synchronized (GLTextureView.sGLThreadManager) {
                this.mHasSurface = false;
                GLTextureView.sGLThreadManager.notifyAll();
                while (!this.mWaitingForSurface && !this.mExited) {
                    try {
                        GLTextureView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            setName("GLThread " + getId());
            try {
                guardedRun();
            } catch (InterruptedException e) {
            } finally {
                GLTextureView.sGLThreadManager.threadExiting(this);
            }
        }
    }

    private static class GLThreadManager {

        private static String TAG = "GLThreadManager";
        private GLThread mEglOwner;
        private boolean mGLESDriverCheckComplete;
        private int mGLESVersion;
        private boolean mGLESVersionCheckComplete;
        private boolean mLimitedGLESContexts;
        private boolean mMultipleGLESContextsAllowed;

        private GLThreadManager() {
        }

        private void checkGLESVersion() {
            if (!this.mGLESVersionCheckComplete) {
                this.mGLESVersion = 131072;
                if (this.mGLESVersion >= 131072) {
                    this.mMultipleGLESContextsAllowed = true;
                }
                this.mGLESVersionCheckComplete = true;
            }
        }

        public void releaseEglContextLocked(GLThread thread) {
            if (this.mEglOwner == thread) {
                this.mEglOwner = null;
            }
            notifyAll();
        }

        public void checkGLDriver(GL10 gl10) {
            boolean z = true;
            synchronized (this) {
                if (!this.mGLESDriverCheckComplete) {
                    checkGLESVersion();
                    String glGetString = gl10.glGetString(7937);
                    if (this.mGLESVersion < 131072) {
                        this.mMultipleGLESContextsAllowed = !glGetString.startsWith("Q3Dimension MSM7500 ");
                        notifyAll();
                    }
                    if (this.mMultipleGLESContextsAllowed) {
                        z = false;
                    }
                    this.mLimitedGLESContexts = z;
                    this.mGLESDriverCheckComplete = true;
                }
            }
        }

        public boolean shouldReleaseEGLContextWhenPausing() {
            boolean z;
            synchronized (this) {
                z = this.mLimitedGLESContexts;
            }
            return z;
        }

        public void threadExiting(GLThread thread) {
            synchronized (this) {
                thread.mExited = true;
                if (this.mEglOwner == thread) {
                    this.mEglOwner = null;
                }
                notifyAll();
            }
        }

        public boolean shouldTerminateEGLWhenPausing() {
            boolean z;
            synchronized (this) {
                checkGLESVersion();
                z = !this.mMultipleGLESContextsAllowed;
            }
            return z;
        }

        public boolean tryAcquireEglContextLocked(GLThread thread) {
            if (this.mEglOwner == thread || this.mEglOwner == null) {
                this.mEglOwner = thread;
                notifyAll();
            } else {
                checkGLESVersion();
                if (!this.mMultipleGLESContextsAllowed) {
                    if (this.mEglOwner != null) {
                        this.mEglOwner.requestReleaseEglContextLocked();
                    }
                    return false;
                }
            }
            return true;
        }
    }

    public interface Renderer {

        void onDestroy();

        void onDrawFrame(GL10 gl10);

        void onSurfaceChanged(GL10 gl10, int i, int i2);

        void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig);
    }

    private class SimpleEGLConfigChooser extends ComponentSizeChooser {
        public SimpleEGLConfigChooser(boolean withDepthBuffer) {
            super(8, 8, 8, 0, withDepthBuffer ? 16 : 0, 0);
        }
    }

    public GLTextureView(Context context) {
        super(context);
        init();
    }

    private void checkRenderThreadState() {
        if (this.mGLThread != null) {
            throw new IllegalStateException("setRenderer has already been called for this instance.");
        }
    }

    private void init() {
        setSurfaceTextureListener(this);
        setOpaque(true);
    }

    public void requestRender() {
        this.mGLThread.requestRender();
    }

    public void surfaceChanged(int w, int h) {
        this.mGLThread.onWindowResize(w, h);
    }

    public void queueEvent(Runnable runnable) {
        this.mGLThread.queueEvent(runnable);
    }

    public void surfaceCreated() {
        this.mGLThread.surfaceCreated();
    }

    public void surfaceDestroyed() {
        this.mGLThread.surfaceDestroyed();
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mGLThread != null) {
                this.mGLThread.requestExitAndWait();
            }
        } finally {
            super.finalize();
        }
    }

    public boolean getPreserveEGLContextOnPause() {
        return this.mPreserveEGLContextOnPause;
    }

    public int getRenderMode() {
        return this.mGLThread.getRenderMode();
    }

    @Override // android.view.TextureView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mDetached && this.mRenderer != null) {
            int renderMode = this.mGLThread != null ? this.mGLThread.getRenderMode() : 1;
            this.mGLThread = new GLThread(this.mThisWeakRef);
            if (renderMode != 1) {
                this.mGLThread.setRenderMode(renderMode);
            }
            this.mGLThread.start();
        }
        this.mDetached = false;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        if (this.mGLThread != null) {
            this.mGLThread.requestExitAndWait();
        }
        this.mDetached = true;
        super.onDetachedFromWindow();
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        surfaceCreated();
        surfaceChanged(width, height);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        surfaceDestroyed();
        return true;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
        surfaceChanged(width, height);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public void setEGLConfigChooser(EGLConfigChooser configChooser) {
        checkRenderThreadState();
        this.mEGLConfigChooser = configChooser;
    }

    public void setEGLConfigChooser(boolean needDepth) {
        setEGLConfigChooser(new SimpleEGLConfigChooser(needDepth));
    }

    public void setEGLContextClientVersion(int version) {
        checkRenderThreadState();
        this.mEGLContextClientVersion = version;
    }

    public void setEGLContextFactory(EGLContextFactory factory) {
        checkRenderThreadState();
        this.mEGLContextFactory = factory;
    }

    public void setEGLWindowSurfaceFactory(EGLWindowSurfaceFactory factory) {
        checkRenderThreadState();
        this.mEGLWindowSurfaceFactory = factory;
    }

    public void setPreserveEGLContextOnPause(boolean preserveOnPause) {
        this.mPreserveEGLContextOnPause = preserveOnPause;
    }

    public void setRenderMode(int renderMode) {
        this.mGLThread.setRenderMode(renderMode);
    }

    public void setRenderer(Renderer renderer) {
        checkRenderThreadState();
        if (this.mEGLConfigChooser == null) {
            this.mEGLConfigChooser = new SimpleEGLConfigChooser(true);
        }
        if (this.mEGLContextFactory == null) {
            this.mEGLContextFactory = new DefaultContextFactory();
        }
        if (this.mEGLWindowSurfaceFactory == null) {
            this.mEGLWindowSurfaceFactory = new DefaultWindowSurfaceFactory();
        }
        this.mRenderer = renderer;
        GLThread GLThread = new GLThread(this.mThisWeakRef);
        this.mGLThread = GLThread;
        GLThread.start();
    }
}
