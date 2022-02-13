package com.samsung.android.visualeffect;

import android.content.Context;
import com.samsung.android.visualeffect.lock.brilliantring.BrilliantRingEffect;

/* renamed from: com.samsung.android.visualeffect.d */
/* loaded from: classes.dex */
public class InnerViewManager {
    /* renamed from: a */
    public static IEffectView getInstance(Context context, int i) {
        return new BrilliantRingEffect(context);
    }
}
