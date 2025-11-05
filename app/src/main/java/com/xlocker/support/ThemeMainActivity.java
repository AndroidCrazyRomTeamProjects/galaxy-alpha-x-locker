package com.xlocker.support;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.galaxytheme.brilliantring.R;
import com.xlocker.core.sdk.GlobalIntent;
import com.xlocker.core.sdk.ILockscreenCallback;
import com.xlocker.core.sdk.KeyguardSecurityCallback;
import com.xlocker.core.sdk.Lockscreen;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

public class ThemeMainActivity extends Activity implements DialogInterface.OnCancelListener {

    private static final String TAG = ThemeMainActivity.class.getSimpleName();

    private KeyguardSecurityCallback.OnSecurityResult pendingSecurityResult;

    View rootView;

    DownloadDialog downloadDialog;

    private Lockscreen lockscreen;

    class DownloadDialog extends AlertDialog implements View.OnClickListener {

        private Context mContext;

        public DownloadDialog(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getId() == R.id.ok) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + ThemeMainActivity.this.getString(R.string.locker_app_package) + "&referrer=utm_source%3Dtheme%26utm_medium%3Dbangding%26utm_content%3D" + ThemeMainActivity.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ThemeMainActivity.this.startActivity(Intent.createChooser(intent, ThemeMainActivity.this.getResources().getString(R.string.locker_app_name)));
                ThemeMainActivity.this.finish();
            }
        }

        @Override // android.app.AlertDialog, android.app.Dialog
        protected void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this.mContext).inflate(R.layout.download_dialog, null);
            setContentView(linearLayout);
            String string = ThemeMainActivity.this.getString(R.string.locker_app_name);
            ((TextView) linearLayout.findViewById(R.id.message)).setText(ThemeMainActivity.this.getString(R.string.need_locker_app, new Object[]{string, string}));
            (linearLayout.findViewById(R.id.ok)).setOnClickListener(this);
        }
    }

    private String getLockscreenMetadata() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            if (!(packageInfo == null || packageInfo.applicationInfo == null)) {
                XmlResourceParser loadXmlMetaData = packageInfo.applicationInfo.loadXmlMetaData(getPackageManager(), "com.xlocker.theme.lockscreen");
                if (loadXmlMetaData == null) {
                    return null;
                }
                while (loadXmlMetaData.getEventType() != 1) {
                    if (loadXmlMetaData.getEventType() == 2 && "theme-info".equals(loadXmlMetaData.getName())) {
                        return loadXmlMetaData.getAttributeValue(null, "theme_lockscreen_class");
                    }
                    try {
                        loadXmlMetaData.next();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        } catch (Exception e3) {
        }
        return "";
    }

    private void initializeLockscreen() {
        try {
            String lockscreenClassName = getLockscreenMetadata();
            if (!TextUtils.isEmpty(lockscreenClassName)) {
                Log.i(TAG, "get lockscreen class name: " + lockscreenClassName);
                this.lockscreen = (Lockscreen) Class.forName(lockscreenClassName)
                        .getConstructor(Context.class, Context.class)
                        .newInstance(this, this);
            }
        } catch (Throwable th) {
            Log.i(TAG, Log.getStackTraceString(th));
        }
    }

    private void configureWindowInsets() {
        if (Build.VERSION.SDK_INT >= 19) {
            View findViewById = findViewById(R.id.host_view);
            findViewById.setFitsSystemWindows(true);
            if (Build.VERSION.SDK_INT >= 21) {
                findViewById.setSystemUiVisibility(1536);
            }
        }
    }

    private ILockscreenCallback createLockscreenCallback() {
        return new LockscreenCallback(this);
    }

    void showDownloadDialog(KeyguardSecurityCallback.OnSecurityResult securityResult) {
        this.pendingSecurityResult = securityResult;
        this.downloadDialog.setCancelable(true);
        this.downloadDialog.show();
    }

    public boolean isPackageInstalled(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(str, PackageManager.MATCH_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        if (this.lockscreen != null) {
            this.lockscreen.onActivityFinished();
        }
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        if (dialogInterface == this.downloadDialog && this.pendingSecurityResult != null) {
            this.pendingSecurityResult.onFailed();
            this.pendingSecurityResult = null;
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.downloadDialog = new DownloadDialog(this);
        this.downloadDialog.setOnCancelListener(this);
        if (!isPackageInstalled(this, getString(R.string.locker_app_package))) {
            initializeLockscreen();
            if (this.lockscreen != null) {
                setContentView(R.layout.preview);
                this.rootView = findViewById(R.id.root_view);
                configureWindowInsets();
                this.lockscreen.setCallback(createLockscreenCallback());
                Drawable defaultWallpaper = this.lockscreen.getDefaultWallpaper();
                ((ImageView) findViewById(R.id.wallpaper)).setImageDrawable(defaultWallpaper);
                this.lockscreen.onActivityCreated();
                this.lockscreen.onWallpaperUpdated(defaultWallpaper, false);
                return;
            }
            this.downloadDialog.setCancelable(false);
            this.downloadDialog.show();
            return;
        }
        Intent intent = new Intent(GlobalIntent.ACTION_THEME_DETAIL);
        intent.putExtra(GlobalIntent.EXTRA_THEME_PACKAGE, getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.lockscreen != null) {
            this.lockscreen.onActivityDestroyed();
        }
        System.exit(0);
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        if (this.lockscreen != null) {
            this.lockscreen.onActivityPaused();
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.lockscreen != null) {
            this.lockscreen.onActivityResumed();
        }
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        if (this.lockscreen != null) {
            this.lockscreen.onActivityStarted();
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        if (this.lockscreen != null) {
            this.lockscreen.onActivityStopped();
        }
    }
}
