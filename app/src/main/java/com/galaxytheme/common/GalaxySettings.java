package com.galaxytheme.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

/* loaded from: classes.dex */
public class GalaxySettings extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    private static final String CLOCK_FONT_FILE_COOLJAZZ = "fonts/Cooljazz.ttf";
    private static final String CLOCK_FONT_FILE_DEFAULT = "fonts/AndroidClock.ttf";
    private static final String KEY_CAMERA_SHORTCUT = "enable_camera_shortcut";
    private static final String KEY_CLOCK_ALIGN = "clock_align";
    private static final String KEY_CLOCK_SIZE = "clock_size";
    private static final String KEY_CLOCK_STYLE = "clock_font";
    private static final String KEY_EDIT_PROFILE = "edit_profile";
    private static final String KEY_ENABLE_PROFILE = "enable_profile";
    private static final String KEY_FONT_SIZE = "myprofile_size";
    private static final String KEY_FONT_STYLE = "myprofile_font";
    private static final String KEY_HELP_TEXT = "help_text";
    private static final String KEY_MYPROFILE_ALIGN = "myprofile_align";
    public static final int LOCKSCREEN_CLOCK_FONT_STYLE_COOLJAZZ = 2;
    public static final int LOCKSCREEN_CLOCK_FONT_STYLE_CUSTOM = 3;
    public static final int LOCKSCREEN_CLOCK_FONT_STYLE_DEFAULT = 1;
    public static final int LOCKSCREEN_FONT_STYLE_COOLJAZZ = 2;
    public static final int LOCKSCREEN_FONT_STYLE_CUSTOM = 3;
    public static final int LOCKSCREEN_FONT_STYLE_KAITI = 1;
    private static final String MYPROFILE_FONT_FILE_COOLJAZZ = "fonts/Cooljazz.ttf";
    private static final String MYPROFILE_FONT_FILE_KAITI = "fonts/kaiti.ttf";
    private static final int REQUEST_CODE_GET_FONT_FILE_CLOCK = 4;
    private static final int REQUEST_CODE_GET_FONT_FILE_PROFILE = 3;
    private static final String SP_KEY_CAMERA_SHORTCUT = "enable_camera_shortcut";
    private static final String SP_KEY_CLOCK_ALIGN = "clock_align";
    private static final String SP_KEY_ENABLE_PROFILE = "enable_profile";
    private static final String SP_KEY_HELP_TEXT = "help_text";
    private static final String SP_KEY_LOCKSCREEN_CLOCK_FONT_PATH = "clock_font_file_path";
    private static final String SP_KEY_LOCKSCREEN_CLOCK_FONT_TYPE = "clock_font";
    private static final String SP_KEY_LOCKSCREEN_CLOCK_SIZE = "clock_size";
    private static final String SP_KEY_LOCKSCREEN_FONT_PATH = "font_file_path";
    private static final String SP_KEY_LOCKSCREEN_FONT_SIZE = "myprofile_size";
    private static final String SP_KEY_LOCKSCREEN_FONT_TYPE = "myprofile_font";
    private static final String SP_KEY_MYPROFILE_ALIGN = "myprofile_align";
    private static final String SP_KEY_PROFILE = "edit_profile";
    private static final String SP_NAME = "galaxy_config";
    private static final String TAG = "Personality";
    private static Handler mHandler = new Handler();
    private static C0027a sClockTypeface;
    private static C0027a sProfileTypeface;
    private SwitchPreference mPreferenceCameraShortcut;
    private ListPreference mPreferenceClockAlign;
    private ListPreference mPreferenceClockSize;
    private ListPreference mPreferenceClockStyle;
    private EditTextPreference mPreferenceEditProfile;
    private CheckBoxPreference mPreferenceEnableProfile;
    private ListPreference mPreferenceFontSize;
    private ListPreference mPreferenceFontStyle;
    private CheckBoxPreference mPreferenceHelpText;
    private ListPreference mPreferenceMyprofileAlign;

    /* renamed from: com.galaxytheme.common.GalaxySettings$a */
    /* loaded from: classes.dex */
    private static class C0027a {

        /* renamed from: a */
        Typeface f112a;

        /* renamed from: b */
        int f113b;

        /* renamed from: c */
        String f114c;

        private C0027a() {
        }
    }

    public static int getClockAlign(Context context) {
        return context.getSharedPreferences(SP_NAME, 7).getInt("clock_align", 1);
    }

    public static Typeface getClockFont(Context context) {
        int lockscreenClockFontType = getLockscreenClockFontType(context);
        if (lockscreenClockFontType == 3) {
            String customClockFontPath = getCustomClockFontPath(context);
            if (customClockFontPath != null) {
                if (sClockTypeface == null || sClockTypeface.f112a == null || sClockTypeface.f113b != 3) {
                    File file = new File(customClockFontPath);
                    if (file.exists()) {
                        try {
                            Typeface createFromFile = Typeface.createFromFile(file);
                            sClockTypeface = new C0027a();
                            sClockTypeface.f112a = createFromFile;
                            sClockTypeface.f113b = 3;
                            sClockTypeface.f114c = customClockFontPath;
                            Log.i("font", "return new clock typeface");
                            return createFromFile;
                        } catch (Exception e) {
                        }
                    }
                } else {
                    Log.i("font", "return cached clock typeface");
                    return sClockTypeface.f112a;
                }
            }
        } else if (lockscreenClockFontType == 2) {
            if (sClockTypeface == null || sClockTypeface.f112a == null || sClockTypeface.f113b != 2) {
                Typeface createFromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Cooljazz.ttf");
                sClockTypeface = new C0027a();
                sClockTypeface.f112a = createFromAsset;
                sClockTypeface.f113b = 2;
                Log.i("font", "return new clock typeface");
                return createFromAsset;
            }
            Log.i("font", "return cached clock typeface");
            return sClockTypeface.f112a;
        }
        if (sClockTypeface == null || sClockTypeface.f112a == null || sClockTypeface.f113b != 1) {
            try {
                Typeface createFromAsset2 = Typeface.createFromAsset(context.getAssets(), CLOCK_FONT_FILE_DEFAULT);
                sClockTypeface = new C0027a();
                sClockTypeface.f112a = createFromAsset2;
                sClockTypeface.f113b = 1;
                Log.i("font", "return new clock typeface");
                return createFromAsset2;
            } catch (Exception e2) {
                return null;
            }
        } else {
            Log.i("font", "return cached clock typeface");
            return sClockTypeface.f112a;
        }
    }

    public static float getClockSize(Context context) {
        return context.getSharedPreferences(SP_NAME, 7).getFloat("clock_size", 1.0f);
    }

    private static String getCustomClockFontPath(Context context) {
        return context.getSharedPreferences(SP_NAME, 7).getString(SP_KEY_LOCKSCREEN_CLOCK_FONT_PATH, null);
    }

    private static String getCustomTypeFacePath(Context context) {
        return context.getSharedPreferences(SP_NAME, 7).getString(SP_KEY_LOCKSCREEN_FONT_PATH, null);
    }

    public static float getFontSize(Context context) {
        return context.getSharedPreferences(SP_NAME, 7).getFloat("myprofile_size", 1.0f);
    }

    public static int getLockscreenClockFontType(Context context) {
        return context.getSharedPreferences(SP_NAME, 7).getInt("clock_font", 1);
    }

    public static int getLockscreenFontStyleType(Context context) {
        return context.getSharedPreferences(SP_NAME, 7).getInt("myprofile_font", 2);
    }

    public static int getMyprofileAlign(Context context) {
        return context.getSharedPreferences(SP_NAME, 7).getInt("myprofile_align", 1);
    }

    public static String getProfile(Context context) {
        return context.getSharedPreferences(SP_NAME, 7).getString("edit_profile", context.getString(com.galaxytheme.common.R.string.keyguard_status_view_myprofile));
    }

    public static Typeface getTypeface(Context context) {
        int lockscreenFontStyleType = getLockscreenFontStyleType(context);
        if (lockscreenFontStyleType == 3) {
            String customTypeFacePath = getCustomTypeFacePath(context);
            if (customTypeFacePath != null) {
                if (sProfileTypeface == null || sProfileTypeface.f112a == null || sProfileTypeface.f113b != 3 || !customTypeFacePath.equals(sProfileTypeface.f114c)) {
                    File file = new File(customTypeFacePath);
                    if (file.exists()) {
                        try {
                            Typeface createFromFile = Typeface.createFromFile(file);
                            sProfileTypeface = new C0027a();
                            sProfileTypeface.f112a = createFromFile;
                            sProfileTypeface.f113b = 3;
                            sProfileTypeface.f114c = customTypeFacePath;
                            Log.i("font", "return new profile typeface");
                            return createFromFile;
                        } catch (Exception e) {
                        }
                    }
                } else {
                    Log.i("font", "return cached profile typeface");
                    return sProfileTypeface.f112a;
                }
            }
        } else if (lockscreenFontStyleType == 2) {
            if (sProfileTypeface == null || sProfileTypeface.f112a == null || sProfileTypeface.f113b != 2) {
                Typeface createFromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Cooljazz.ttf");
                sProfileTypeface = new C0027a();
                sProfileTypeface.f112a = createFromAsset;
                sProfileTypeface.f113b = 2;
                Log.i("font", "return new profile typeface");
                return createFromAsset;
            }
            Log.i("font", "return cached profile typeface");
            return sProfileTypeface.f112a;
        }
        if (sProfileTypeface == null || sProfileTypeface.f112a == null || sProfileTypeface.f113b != 1) {
            Typeface createFromAsset2 = Typeface.createFromAsset(context.getAssets(), MYPROFILE_FONT_FILE_KAITI);
            sProfileTypeface = new C0027a();
            sProfileTypeface.f112a = createFromAsset2;
            sProfileTypeface.f113b = 1;
            Log.i("font", "return new profile typeface");
            return createFromAsset2;
        }
        Log.i("font", "return cached profile typeface");
        return sProfileTypeface.f112a;
    }

    private void initPreferences() {
        this.mPreferenceFontStyle = (ListPreference) findPreference("myprofile_font");
        this.mPreferenceFontStyle.setOnPreferenceChangeListener(this);
        this.mPreferenceEnableProfile = (CheckBoxPreference) findPreference("enable_profile");
        this.mPreferenceEnableProfile.setOnPreferenceChangeListener(this);
        this.mPreferenceEditProfile = (EditTextPreference) findPreference("edit_profile");
        this.mPreferenceEditProfile.setOnPreferenceChangeListener(this);
        this.mPreferenceFontSize = (ListPreference) findPreference("myprofile_size");
        this.mPreferenceFontSize.setOnPreferenceChangeListener(this);
        this.mPreferenceClockStyle = (ListPreference) findPreference("clock_font");
        this.mPreferenceClockStyle.setOnPreferenceChangeListener(this);
        this.mPreferenceClockSize = (ListPreference) findPreference("clock_size");
        this.mPreferenceClockSize.setOnPreferenceChangeListener(this);
        this.mPreferenceMyprofileAlign = (ListPreference) findPreference("myprofile_align");
        this.mPreferenceMyprofileAlign.setOnPreferenceChangeListener(this);
        this.mPreferenceClockAlign = (ListPreference) findPreference("clock_align");
        this.mPreferenceClockAlign.setOnPreferenceChangeListener(this);
        this.mPreferenceCameraShortcut = (SwitchPreference) findPreference("enable_camera_shortcut");
        this.mPreferenceCameraShortcut.setOnPreferenceChangeListener(this);
        this.mPreferenceHelpText = (CheckBoxPreference) findPreference("help_text");
        this.mPreferenceHelpText.setOnPreferenceChangeListener(this);
    }

    public static boolean isEnableProfile(Context context) {
        return context.getSharedPreferences(SP_NAME, 7).getBoolean("enable_profile", context.getResources().getBoolean(com.galaxytheme.common.R.bool.default_enable_profile));
    }

    public static boolean isShowCameraShortcut(Context context) {
        return context.getSharedPreferences(SP_NAME, 7).getBoolean("enable_camera_shortcut", context.getResources().getBoolean(com.galaxytheme.common.R.bool.default_enable_camera));
    }

    public static boolean isShowHelpText(Context context) {
        return context.getSharedPreferences(SP_NAME, 7).getBoolean("help_text", context.getResources().getBoolean(com.galaxytheme.common.R.bool.default_show_help_text));
    }

    private void setClockAlign(int i) {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(SP_NAME, 7).edit();
        edit.putInt("clock_align", i);
        edit.commit();
    }

    private void setClockSize(float f) {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(SP_NAME, 7).edit();
        edit.putFloat("clock_size", f);
        edit.commit();
    }

    private void setCustomClockFontPath(String str) {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(SP_NAME, 7).edit();
        edit.putString(SP_KEY_LOCKSCREEN_CLOCK_FONT_PATH, str);
        edit.commit();
    }

    private void setCustomTypeFacePath(String str) {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(SP_NAME, 7).edit();
        edit.putString(SP_KEY_LOCKSCREEN_FONT_PATH, str);
        edit.commit();
    }

    private void setEnableProfile(boolean z) {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(SP_NAME, 7).edit();
        edit.putBoolean("enable_profile", z);
        edit.commit();
    }

    private void setFontSize(float f) {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(SP_NAME, 7).edit();
        edit.putFloat("myprofile_size", f);
        edit.commit();
    }

    private void setLockscreenClockFontType(int i) {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(SP_NAME, 7).edit();
        edit.putInt("clock_font", i);
        edit.commit();
    }

    private void setLockscreenFontStyleType(int i) {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(SP_NAME, 7).edit();
        edit.putInt("myprofile_font", i);
        edit.commit();
    }

    private void setMyprofileAlign(int i) {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(SP_NAME, 7).edit();
        edit.putInt("myprofile_align", i);
        edit.commit();
    }

    private void setProfile(String str) {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(SP_NAME, 7).edit();
        edit.putString("edit_profile", str);
        edit.commit();
    }

    private void setShowCameraShortcut(boolean z) {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(SP_NAME, 7).edit();
        edit.putBoolean("enable_camera_shortcut", z);
        edit.commit();
    }

    private void setShowHelpText(boolean z) {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(SP_NAME, 7).edit();
        edit.putBoolean("help_text", z);
        edit.commit();
    }

    private void updatePreferences() {
        this.mPreferenceFontStyle.setValue(String.valueOf(getLockscreenFontStyleType(getActivity())));
        this.mPreferenceFontStyle.setSummary(this.mPreferenceFontStyle.getEntry());
        this.mPreferenceEnableProfile.setChecked(isEnableProfile(getActivity()));
        this.mPreferenceEditProfile.setEnabled(isEnableProfile(getActivity()));
        this.mPreferenceEditProfile.setSummary(getProfile(getActivity()));
        this.mPreferenceEditProfile.setText(getProfile(getActivity()));
        this.mPreferenceMyprofileAlign.setEnabled(isEnableProfile(getActivity()));
        this.mPreferenceMyprofileAlign.setValue(String.valueOf(getMyprofileAlign(getActivity())));
        this.mPreferenceMyprofileAlign.setSummary(this.mPreferenceMyprofileAlign.getEntry());
        this.mPreferenceFontStyle.setEnabled(isEnableProfile(getActivity()));
        this.mPreferenceFontSize.setEnabled(isEnableProfile(getActivity()));
        this.mPreferenceFontSize.setSummary(this.mPreferenceFontSize.getEntry());
        this.mPreferenceClockAlign.setValue(String.valueOf(getClockAlign(getActivity())));
        this.mPreferenceClockAlign.setSummary(this.mPreferenceClockAlign.getEntry());
        this.mPreferenceClockStyle.setValue(String.valueOf(getLockscreenClockFontType(getActivity())));
        this.mPreferenceClockStyle.setSummary(this.mPreferenceClockStyle.getEntry());
        this.mPreferenceClockSize.setSummary(this.mPreferenceClockSize.getEntry());
        this.mPreferenceCameraShortcut.setChecked(isShowCameraShortcut(getActivity()));
        this.mPreferenceHelpText.setChecked(isShowHelpText(getActivity()));
    }

    @Override // android.preference.PreferenceFragment
    public void addPreferencesFromResource(int i) {
        super.addPreferencesFromResource(i);
        initPreferences();
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        Log.i(TAG, "onActivityResult, requestCode = " + i + ", resultCode = " + i2);
        super.onActivityResult(i, i2, intent);
        if (i == 3) {
            if (i2 == -1 && intent != null && intent.getData() != null) {
                String path = intent.getData().getPath();
                if (path == null || !new File(path).exists() || !path.endsWith(".ttf")) {
                    Toast.makeText(getActivity(), getText(com.galaxytheme.common.R.string.set_font_failed), Toast.LENGTH_SHORT).show();
                    return;
                }
                setCustomTypeFacePath(path);
                setLockscreenFontStyleType(3);
                this.mPreferenceFontStyle.setValue(String.valueOf(3));
                this.mPreferenceFontStyle.setSummary(this.mPreferenceFontStyle.getEntry());
                Toast.makeText(getActivity(), getText(com.galaxytheme.common.R.string.set_font_success), Toast.LENGTH_SHORT).show();
            }
        } else if (i == REQUEST_CODE_GET_FONT_FILE_CLOCK && i2 == -1 && intent != null && intent.getData() != null) {
            String path2 = intent.getData().getPath();
            if (path2 != null) {
                File file = new File(path2);
                if (file.exists() && file.getName().toLowerCase(Locale.US).endsWith(".ttf")) {
                    setCustomClockFontPath(path2);
                    setLockscreenClockFontType(3);
                    this.mPreferenceClockStyle.setValue(String.valueOf(3));
                    this.mPreferenceClockStyle.setSummary(this.mPreferenceClockStyle.getEntry());
                    Toast.makeText(getActivity(), getText(com.galaxytheme.common.R.string.set_font_success), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(getActivity(), getText(com.galaxytheme.common.R.string.set_font_failed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(com.galaxytheme.common.R.xml.galaxy_settings);
        initPreferences();
    }

    @Override // android.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (this.mPreferenceFontStyle == preference) {
            int parseInt = Integer.parseInt((String) obj);
            if (parseInt != 3) {
                setLockscreenFontStyleType(parseInt);
                mHandler.post(new Runnable() { // from class: com.galaxytheme.common.GalaxySettings.1
                    @Override // java.lang.Runnable
                    public void run() {
                        GalaxySettings.this.mPreferenceFontStyle.setSummary(GalaxySettings.this.mPreferenceFontStyle.getEntry());
                    }
                });
                Log.i("wallpaper", "wallpaper type changed, type = " + parseInt);
            } else {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("*/*");
                try {
                    startActivityForResult(intent, 3);
                    Toast.makeText(getActivity(), getText(com.galaxytheme.common.R.string.select_font_file), 2000).show();
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }
        } else if (this.mPreferenceEnableProfile == preference) {
            boolean booleanValue = ((Boolean) obj).booleanValue();
            setEnableProfile(booleanValue);
            this.mPreferenceEditProfile.setEnabled(booleanValue);
            this.mPreferenceFontStyle.setEnabled(booleanValue);
            this.mPreferenceFontSize.setEnabled(booleanValue);
            this.mPreferenceMyprofileAlign.setEnabled(booleanValue);
        } else if (this.mPreferenceEditProfile == preference) {
            setProfile((String) obj);
            this.mPreferenceEditProfile.setSummary(getProfile(getActivity()));
        } else if (this.mPreferenceFontSize == preference) {
            setFontSize(Float.parseFloat((String) obj));
            mHandler.post(new Runnable() { // from class: com.galaxytheme.common.GalaxySettings.2
                @Override // java.lang.Runnable
                public void run() {
                    GalaxySettings.this.mPreferenceFontSize.setSummary(GalaxySettings.this.mPreferenceFontSize.getEntry());
                }
            });
        } else if (this.mPreferenceMyprofileAlign == preference) {
            setMyprofileAlign(Integer.parseInt((String) obj));
            mHandler.post(new Runnable() { // from class: com.galaxytheme.common.GalaxySettings.3
                @Override // java.lang.Runnable
                public void run() {
                    GalaxySettings.this.mPreferenceMyprofileAlign.setSummary(GalaxySettings.this.mPreferenceMyprofileAlign.getEntry());
                }
            });
        } else if (this.mPreferenceClockStyle == preference) {
            int parseInt2 = Integer.parseInt((String) obj);
            if (parseInt2 != 3) {
                setLockscreenClockFontType(parseInt2);
                mHandler.post(new Runnable() { // from class: com.galaxytheme.common.GalaxySettings.4
                    @Override // java.lang.Runnable
                    public void run() {
                        GalaxySettings.this.mPreferenceClockStyle.setSummary(GalaxySettings.this.mPreferenceClockStyle.getEntry());
                    }
                });
                Log.i("wallpaper", "wallpaper type changed, type = " + parseInt2);
            } else {
                Intent intent2 = new Intent("android.intent.action.GET_CONTENT");
                intent2.setType("*/*");
                try {
                    startActivityForResult(intent2, REQUEST_CODE_GET_FONT_FILE_CLOCK);
                    Toast.makeText(getActivity(), getText(com.galaxytheme.common.R.string.select_font_file), 2000).show();
                    return false;
                } catch (Exception e2) {
                    return false;
                }
            }
        } else if (this.mPreferenceClockSize == preference) {
            setClockSize(Float.parseFloat((String) obj));
            mHandler.post(new Runnable() { // from class: com.galaxytheme.common.GalaxySettings.5
                @Override // java.lang.Runnable
                public void run() {
                    GalaxySettings.this.mPreferenceClockSize.setSummary(GalaxySettings.this.mPreferenceClockSize.getEntry());
                }
            });
        } else if (this.mPreferenceClockAlign == preference) {
            setClockAlign(Integer.parseInt((String) obj));
            mHandler.post(new Runnable() { // from class: com.galaxytheme.common.GalaxySettings.6
                @Override // java.lang.Runnable
                public void run() {
                    GalaxySettings.this.mPreferenceClockAlign.setSummary(GalaxySettings.this.mPreferenceClockAlign.getEntry());
                }
            });
        } else if (this.mPreferenceCameraShortcut == preference) {
            setShowCameraShortcut(((Boolean) obj).booleanValue());
        } else if (this.mPreferenceHelpText == preference) {
            setShowHelpText(((Boolean) obj).booleanValue());
        }
        return true;
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        updatePreferences();
    }
}
