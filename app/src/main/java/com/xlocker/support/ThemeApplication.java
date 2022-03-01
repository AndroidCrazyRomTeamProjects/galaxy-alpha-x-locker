package com.xlocker.support;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class ThemeApplication extends Application {

    /* renamed from: a */
    private static final String TAG = ThemeApplication.class.getSimpleName();

    /* renamed from: a */
    private Object m32a(Context context) {
        try {
            Field declaredField = Class.forName("android.app.ContextImpl").getDeclaredField("mPackageInfo");
            declaredField.setAccessible(true);
            return declaredField.get(context);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
    }

    /* renamed from: a */
    private void m31a(Object obj, ClassLoader classLoader) {
        Log.i(TAG, "setClassLoader, LoadedApk = " + obj + ", classLoader = " + classLoader + ", pcl = " + classLoader.getParent());
        try {
            Field declaredField = Class.forName("android.app.LoadedApk").getDeclaredField("mClassLoader");
            declaredField.setAccessible(true);
            declaredField.set(obj, classLoader);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private boolean LoadPreview() {
        boolean z = false;
        File b = GetPreviewDex();
        if (b.exists()) {
            b.delete();
        }
        try {
            InputStream open = getAssets().open("preview");
            FileOutputStream fileOutputStream = new FileOutputStream(b);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = open.read(bArr);
                if (read == -1) {
                    fileOutputStream.close();
                    open.close();
                    z = true;
                    return true;
                }
                fileOutputStream.write(bArr, 0, read);
                fileOutputStream.flush();
            }
        } catch (IOException e) {
            Log.e(TAG, "exception in copyDex: " + Log.getStackTraceString(e));
            return z;
        }
    }

    /* renamed from: b */
    private File GetPreviewDex() {
        return new File(getDir("dex", 0), "preview.apk");
    }

    /* renamed from: c */
    private String m29c() {
        return GetPreviewDex().getAbsolutePath();
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), 0);
        } catch (Exception e) {
            Log.i(TAG, Log.getStackTraceString(e));
        }
        if (applicationInfo != null) {
            Context baseContext = getBaseContext();
            if (LoadPreview()) {
                PathClassLoader pathClassLoader = new PathClassLoader(applicationInfo.sourceDir, applicationInfo.nativeLibraryDir, new PathClassLoader(m29c(), "", ClassLoader.getSystemClassLoader()));
                Object a = m32a(baseContext);
                Log.i(TAG, "mPackageInfo = " + a);
                m31a(a, pathClassLoader);
            }
        }
    }
}
