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
    private Context mContext;
    private int[] sounds;
    private Handler mHandler = new Handler();
    private SoundPool mSoundPool = null;
    private Runnable releaseSoundRunnable = null;
    private final long f72e = 2000;
    private long touchDownTime = 0;
    private long touchMoveDiffTime = 0;
    final int f69b = 0;
    final int f68a = 1;
    final int f70c = 2;
    private boolean f78k = true;
    private float leftVolumeMax = GlobalSettings.getSoundVolume(getContext().getApplicationContext());
    private float rightVolumeMax = GlobalSettings.getSoundVolume(getContext().getApplicationContext());
    private int dragStreamID = 0;
    long mLongPressTime = 411;
    private float dragSoudVolume = 1.0f;
    private float dragSoudMinusOffset = 0.04f;
    private boolean isFadeOutSound = false;
    private boolean isUnlocked = false;
    private boolean hasWindowFocus = true;
    private boolean f89v = false;
    private boolean f88u = false;

    public KeyguardEffectViewBrilliantRing(Context context) {
        super(context);
        this.sounds = null;
        LogUtil.d(TAG, "KeyguardEffectViewBrilliantRing Constructor");
        this.mContext = context;
        setEffect(1);
        this.sounds = new int[3];
    }

    private void playSound(int soundId) throws Settings.SettingNotFoundException {
        m142f();
        LogUtil.d(TAG, "SOUND PLAY mSoundPool = " + this.mSoundPool + ", isSystemSoundChecked = " + this.f78k);
        if (this.f78k && this.mSoundPool != null) {
            LogUtil.d(TAG, "SOUND PLAY soundId = " + soundId);
            if (soundId != 1) {
                this.mSoundPool.play(this.sounds[soundId], this.leftVolumeMax, this.rightVolumeMax, 0, 0, 1.0f);
            } else {
                this.dragStreamID = this.mSoundPool.play(this.sounds[soundId], this.leftVolumeMax, this.rightVolumeMax, 0, -1, 1.0f);
            }
        }
    }

    /* renamed from: f */
    private void m142f() throws Settings.SettingNotFoundException {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        while (Settings.System.getInt(contentResolver, "lockscreen_sounds_enabled") == 1) {
            try {
                this.f78k = true;
                return;
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.f78k = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fadeOutSound() {
        if (this.isFadeOutSound && this.mSoundPool != null) {
            if (this.dragSoudVolume < 0.0f) {
                this.dragSoudVolume = 0.0f;
            }
            this.mSoundPool.setVolume(this.dragStreamID, this.dragSoudVolume, this.dragSoudVolume);
            if (this.dragSoudVolume > 0.0f) {
                this.dragSoudVolume -= this.dragSoudMinusOffset;
                this.mHandler.postDelayed(new Runnable() { // from class: com.galaxytheme.brilliantring.effect.KeyguardEffectViewBrilliantRing.1
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
    }

    private void makeSound() {
        stopReleaseSound();
        if (this.mSoundPool == null) {
            LogUtil.d(TAG, "sound : new SoundPool");
            this.mSoundPool = new SoundPool(10, GlobalSettings.getStreamType(getContext().getApplicationContext()), 1);
            this.sounds[0] = this.mSoundPool.load(this.mContext, R.raw.brilliantring_tap, 1);
            this.sounds[1] = this.mSoundPool.load(this.mContext, R.raw.brilliantring_drag, 1);
            this.sounds[2] = this.mSoundPool.load(this.mContext, R.raw.brilliantring_unlock, 1);
            this.mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() { // from class: com.galaxytheme.brilliantring.a.2
                @Override // android.media.SoundPool.OnLoadCompleteListener
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    LogUtil.d(TAG, "sound : onLoadComplete");
                }
            });
        }
    }

    private void releaseSound() {
        Handler handler = this.mHandler;
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
        handler.postDelayed(runnable, 2000L);
    }

    private void stopReleaseSound() {
        if (this.releaseSoundRunnable != null) {
            removeCallbacks(this.releaseSoundRunnable);
            this.releaseSoundRunnable = null;
        }
    }

    private void setBitmap(Bitmap bitmap) {
        HashMap<?, ?> hashMap = new HashMap<>();
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
        HashMap<?, ?> hashMap = new HashMap<>();
        hashMap.put("StartDelay", Long.valueOf(startDelay));
        hashMap.put("Rect", rect);
        handleCustomEvent(1, hashMap);
    }

    @Override // com.galaxytheme.effect.KeyguardEffectViewBase
    /* renamed from: a */
    public void update(Bitmap bitmap) {
        LogUtil.i(TAG, "update");
        setBitmap(bitmap);
    }

    @Override // com.galaxytheme.effect.KeyguardEffectViewBase
    /* renamed from: a */
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
                this.dragSoudVolume = 1.0f;
                this.isFadeOutSound = false;
                stopReleaseSound();
                this.touchDownTime = System.currentTimeMillis();
                if (this.mSoundPool == null) {
                    LogUtil.d(TAG, "ACTION_DOWN, mSoundPool == null");
                    makeSound();
                    m142f();
                }
                LogUtil.d(TAG, "SOUND PLAY SOUND_ID_TAB");
                playSound(0);
                if (this.dragStreamID != 0) {
                    if (this.mSoundPool != null) {
                        this.mSoundPool.stop(this.dragStreamID);
                    }
                    this.dragStreamID = 0;
                }
            } else if (actionMasked == 2) {
                if (this.dragStreamID == 0) {
                    this.dragSoudVolume = 1.0f;
                    this.touchMoveDiffTime = System.currentTimeMillis() - this.touchDownTime;
                    if (this.touchMoveDiffTime > this.mLongPressTime && this.touchDownTime != 0) {
                        LogUtil.d(TAG, "SOUND PLAY SOUND_ID_DRAG touchMoveDiff = " + this.touchMoveDiffTime);
                        playSound(1);
                    }
                }
            } else if (actionMasked == 1 || actionMasked == 3 || actionMasked == 4) {
                LogUtil.i(TAG, "handleTouchEvent action : " + actionMasked);
                if (this.dragStreamID != 0) {
                    this.dragSoudMinusOffset = 0.039f;
                    this.isFadeOutSound = true;
                    if (this.dragSoudVolume == 1.0f) {
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
        handleCustomEvent(2, (HashMap<?, ?>) null);
        this.isUnlocked = true;
        this.dragSoudMinusOffset = 0.059f;
        playSound(2);
        this.isFadeOutSound = true;
        if (this.dragSoudVolume == 1.0f) {
            fadeOutSound();
        }
    }

    @Override // com.galaxytheme.effect.KeyguardEffectViewBase
    public void show() {
        LogUtil.i(TAG, "show");
        makeSound();
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
        if (!hasWindowFocus && !this.isUnlocked && this.dragStreamID != 0 && !this.isUnlocked) {
            this.dragSoudMinusOffset = 0.039f;
            this.isFadeOutSound = true;
            if (this.dragSoudVolume == 1.0f) {
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
