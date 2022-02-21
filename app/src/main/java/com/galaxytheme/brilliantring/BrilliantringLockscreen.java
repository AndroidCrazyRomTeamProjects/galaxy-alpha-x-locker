package com.galaxytheme.brilliantring;

import android.content.Context;
import com.galaxytheme.common.GalaxyLockscreen;
import com.galaxytheme.common.KeyguardUnlockView;
import com.galaxytheme.effect.KeyguardEffectViewBase;
import com.galaxytheme.effect.KeyguardEffectViewBrilliantRing;

public class BrilliantringLockscreen extends GalaxyLockscreen {
    public BrilliantringLockscreen(Context context, Context context2) {
        super(context, context2);
    }

    @Override // com.galaxytheme.common.GalaxyLockscreen
    protected KeyguardEffectViewBase createUnlockView() {
        return new KeyguardEffectViewBrilliantRing(getThemeContext());
    }

    @Override // com.galaxytheme.common.GalaxyLockscreen
    protected KeyguardUnlockView getKeyguardUnlockView() {
        return new C0017b(getThemeContext());
    }

    public int getLockSoundResourceId() {
        return R.raw.brilliantring_lock;
    }

    @Override // com.galaxytheme.common.GalaxyLockscreen
    public void onActivityCreated() {
        super.onActivityCreated();
    }
}
