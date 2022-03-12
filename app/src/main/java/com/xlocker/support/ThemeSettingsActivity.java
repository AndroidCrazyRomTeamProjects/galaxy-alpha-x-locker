package com.xlocker.support;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class ThemeSettingsActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null && applicationInfo.metaData.containsKey("com.xlocker.theme.settings")) {
                Fragment instantiate = Fragment.instantiate(this, applicationInfo.metaData.getString("com.xlocker.theme.settings"), bundle);
                FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
                beginTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                beginTransaction.replace(android.R.id.content, instantiate);
                beginTransaction.commitAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
