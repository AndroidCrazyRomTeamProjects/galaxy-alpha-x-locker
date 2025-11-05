package com.galaxytheme.effect;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.SoundPool;
import android.os.Handler;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;

import com.galaxytheme.brilliantring.R;
import com.samsung.android.visualeffect.EffectView;
import com.xlocker.core.sdk.GlobalSettings;
import com.xlocker.core.sdk.LogUtil;
import java.util.HashMap;

public class KeyguardEffectViewBrilliantRing extends EffectView implements KeyguardEffectViewBase {
    private static final String TAG = "BrilliantRing_Keyguard";
    private static final int SOUND_ID_TAP = 0;
    private static final int SOUND_ID_DRAG = 1;
    private static final int SOUND_ID_UNLOCK = 2;
    private static final long SOUND_RELEASE_DELAY_MS = 2000L;
    private static final float DRAG_FADE_STEP_DEFAULT = 0.039f;
    private static final float DRAG_FADE_STEP_UNLOCK = 0.059f;
    private static final long LONG_PRESS_THRESHOLD_MS = 411L;

    private final Handler handler = new Handler();
    private final Context mContext;

    private int[] soundIds;
    private SoundPool mSoundPool;
    private Runnable releaseSoundRunnable;
    private long touchDownTime = 0;
    private long touchMoveElapsed = 0;
    private boolean isSoundEnabled = true;
    private float leftVolumeMax = GlobalSettings.getSoundVolume(getContext().getApplicationContext());
    private float rightVolumeMax = GlobalSettings.getSoundVolume(getContext().getApplicationContext());
    private int dragStreamId = 0;
    private float dragSoundVolume = 1.0f;
    private float dragSoundFadeStep = DRAG_FADE_STEP_DEFAULT;
    private boolean isFadeOutSound = false;
    private boolean isUnlocked = false;
    private boolean hasWindowFocus = true;

    public KeyguardEffectViewBrilliantRing(Context context) {
        super(context);
        LogUtil.d(TAG, "KeyguardEffectViewBrilliantRing Constructor");
        this.mContext = context;
        setEffect(1);
        this.soundIds = new int[3];
    }

    private void playSound(int soundId) {
        refreshSystemSoundSetting();
        LogUtil.d(TAG, "SOUND PLAY mSoundPool = " + this.mSoundPool + ", isSystemSoundChecked = " + this.isSoundEnabled);
        if (this.isSoundEnabled && this.mSoundPool != null) {
            LogUtil.d(TAG, "SOUND PLAY soundId = " + soundId);
            if (soundId != 1) {
                this.mSoundPool.play(this.soundIds[soundId], this.leftVolumeMax, this.rightVolumeMax, 0, 0, 1.0f);
            } else {
                this.dragStreamId = this.mSoundPool.play(this.soundIds[soundId], this.leftVolumeMax, this.rightVolumeMax, 0, -1, 1.0f);
            }
        }
    }

    private void refreshSystemSoundSetting() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        try {
            this.isSoundEnabled = Settings.System.getInt(contentResolver, "lockscreen_sounds_enabled", 1) == 1;
        } catch (Settings.SettingNotFoundException e) {
            LogUtil.w(TAG, "System setting lockscreen_sounds_enabled not found", e);
            this.isSoundEnabled = true;
        }
    }

    private void fadeOutSound() {
        if (!this.isFadeOutSound || this.mSoundPool == null) {
            return;
        }
        this.dragSoundVolume = Math.max(0.0f, this.dragSoundVolume);
        this.mSoundPool.setVolume(this.dragStreamId, this.dragSoundVolume, this.dragSoundVolume);
        if (this.dragSoundVolume > 0.0f) {
            this.dragSoundVolume = Math.max(0.0f, this.dragSoundVolume - this.dragSoundFadeStep);
            this.handler.postDelayed(new Runnable() { // from class: com.galaxytheme.brilliantring.effect.KeyguardEffectViewBrilliantRing.1
                @Override // java.lang.Runnable
                public void run() {
                    KeyguardEffectViewBrilliantRing.this.fadeOutSound();
                }
            }, 10L);
            return;
        }
        LogUtil.d(TAG, "SOUND STOP because UP or Unlock");
        stopReleaseSound();
        releaseSound();
    }

    private void ensureSoundPool() {
        stopReleaseSound();
        if (this.mSoundPool == null) {
            LogUtil.d(TAG, "sound : new SoundPool");
            this.mSoundPool = new SoundPool(10, GlobalSettings.getStreamType(getContext().getApplicationContext()), 1);
            this.soundIds[SOUND_ID_TAP] = this.mSoundPool.load(this.mContext, R.raw.brilliantring_tap, 1);
            this.soundIds[SOUND_ID_DRAG] = this.mSoundPool.load(this.mContext, R.raw.brilliantring_drag, 1);
            this.soundIds[SOUND_ID_UNLOCK] = this.mSoundPool.load(this.mContext, R.raw.brilliantring_unlock, 1);
            this.mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() { // from class: com.galaxytheme.brilliantring.a.2
                @Override // android.media.SoundPool.OnLoadCompleteListener
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    LogUtil.d(TAG, "sound : onLoadComplete");
                }
            });
        }
    }

    private void releaseSound() {
        Handler handler = this.handler;
        Runnable runnable = new Runnable() { // from class: com.galaxytheme.brilliantring.effect.KeyguardEffectViewBrilliantring.3
            @Override // java.lang.Runnable
            public void run() {
                if (KeyguardEffectViewBrilliantRing.this.mSoundPool != null) {
                    LogUtil.d(TAG, "BrilliantRing sound : release SoundPool");
                    KeyguardEffectViewBrilliantRing.this.mSoundPool.release();
                    KeyguardEffectViewBrilliantRing.this.mSoundPool = null;
                }
                KeyguardEffectViewBrilliantRing.this.releaseSoundRunnable = null;
            }
        };
        this.releaseSoundRunnable = runnable;
        handler.postDelayed(runnable, SOUND_RELEASE_DELAY_MS);
    }

    private void stopReleaseSound() {
        if (this.releaseSoundRunnable != null) {
            this.handler.removeCallbacks(this.releaseSoundRunnable);
            this.releaseSoundRunnable = null;
        }
    }

    private void setBitmap(Bitmap bitmap) {
        HashMap<String, Bitmap> hashMap = new HashMap<>();
        hashMap.put("Bitmap", bitmap);
        handleCustomEvent(0, hashMap);
    }

    @Override // com.galaxytheme.effect.KeyguardEffectViewBase
    public void reset() {
        LogUtil.i(TAG, "reset");
        this.isUnlocked = false;
        clearScreen();
    }

    @Override // com.galaxytheme.effect.KeyguardEffectViewBase
    public void showUnlockAffordance(long startDelay, Rect rect) {
        LogUtil.i(TAG, "showUnlockAffordance");
        this.isUnlocked = false;
        HashMap<String, Long> hashMap = new HashMap<>();
        hashMap.put("StartDelay", startDelay);
        HashMap<String, Rect> hashMap2 = new HashMap<>();
        hashMap2.put("Rect", rect);
        handleCustomEvent(1, hashMap);
    }

    @Override // com.galaxytheme.effect.KeyguardEffectViewBase
    public void update(Bitmap bitmap) {
        LogUtil.i(TAG, "update");
        setBitmap(bitmap);
    }

    @Override // com.galaxytheme.effect.KeyguardEffectViewBase
    public boolean handleTouchEventForPatternLock(MotionEvent motionEvent) {
        return false;
    }

    @Override // com.galaxytheme.effect.KeyguardEffectViewBase
    public boolean handleTouchEvent(View view, MotionEvent motionEvent) throws Settings.SettingNotFoundException {
        if (this.isUnlocked) {
            LogUtil.i(TAG, "handleTouchEvent isUnlocked : " + this.isUnlocked);
        } else {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                LogUtil.i(TAG, "handleTouchEvent action : " + actionMasked);
                this.dragSoundVolume = 1.0f;
                this.isFadeOutSound = false;
                stopReleaseSound();
                this.touchDownTime = System.currentTimeMillis();
                if (this.mSoundPool == null) {
                    LogUtil.d(TAG, "ACTION_DOWN, mSoundPool == null");
                    ensureSoundPool();
                    refreshSystemSoundSetting();
                }
                LogUtil.d(TAG, "SOUND PLAY SOUND_ID_TAP");
                playSound(SOUND_ID_TAP);
                if (this.dragStreamId != 0) {
                    if (this.mSoundPool != null) {
                        this.mSoundPool.stop(this.dragStreamId);
                    }
                    this.dragStreamId = 0;
                }
            } else if (actionMasked == 2) {
                if (this.dragStreamId == 0) {
                    this.dragSoundVolume = 1.0f;
                    this.touchMoveElapsed = System.currentTimeMillis() - this.touchDownTime;
                    if (this.touchMoveElapsed > LONG_PRESS_THRESHOLD_MS && this.touchDownTime != 0) {
                        LogUtil.d(TAG, "SOUND PLAY SOUND_ID_DRAG touchMoveDiff = " + this.touchMoveElapsed);
                        playSound(SOUND_ID_DRAG);
                    }
                }
            } else if (actionMasked == 1 || actionMasked == 3 || actionMasked == 4) {
                LogUtil.i(TAG, "handleTouchEvent action : " + actionMasked);
                if (this.dragStreamId != 0) {
                    this.dragSoundFadeStep = DRAG_FADE_STEP_DEFAULT;
                    this.isFadeOutSound = true;
                    if (this.dragSoundVolume == 1.0f) {
                        fadeOutSound();
                    }
                }
            }
            handleTouchEvent(motionEvent, view);
        }
        return true;
    }

    @Override // com.galaxytheme.effect.KeyguardEffectViewBase
    public void screenTurnedOn() {
        LogUtil.i(TAG, "screenTurnedOn");
        this.isUnlocked = false;
        clearScreen();
    }

    @Override // com.galaxytheme.effect.KeyguardEffectViewBase
    public void handleUnlock(View view, MotionEvent motionEvent) throws Settings.SettingNotFoundException {
        LogUtil.i(TAG, "handleUnlock");
        handleCustomEvent(2, null);
        this.isUnlocked = true;
        this.dragSoundFadeStep = DRAG_FADE_STEP_UNLOCK;
        playSound(SOUND_ID_UNLOCK);
        this.isFadeOutSound = true;
        if (this.dragSoundVolume == 1.0f) {
            fadeOutSound();
        }
    }

    @Override // com.galaxytheme.effect.KeyguardEffectViewBase
    public void show() {
        LogUtil.i(TAG, "show");
        ensureSoundPool();
        clearScreen();
        this.isUnlocked = false;
    }

    @Override // com.galaxytheme.effect.KeyguardEffectViewBase
    public void playLockSound() {
    }

    @Override // com.galaxytheme.effect.KeyguardEffectViewBase
    public long getUnlockDelay() {
        return 250L;
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        this.hasWindowFocus = hasWindowFocus;
        LogUtil.d(TAG, "onWindowFocusChanged - " + hasWindowFocus);
        if (!hasWindowFocus && !this.isUnlocked && this.dragStreamId != 0 && !this.isUnlocked) {
            this.dragSoundFadeStep = DRAG_FADE_STEP_DEFAULT;
            this.isFadeOutSound = true;
            if (this.dragSoundVolume == 1.0f) {
                fadeOutSound();
            }
        }
    }

    public void setContextualWallpaper(Bitmap bitmap) {
        LogUtil.d(TAG, "setContextualWallpaper");
        if (bitmap == null) {
            LogUtil.d(TAG, "bmp is null");
        } else {
            setBitmap(bitmap);
        }
    }

    public void setHidden(boolean z) {
    }
}
