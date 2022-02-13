package com.galaxytheme.brilliantring;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.galaxytheme.common.KeyguardUnlockView;
import com.galaxytheme.effect.KeyguardEffectViewBase;

/* renamed from: com.galaxytheme.brilliantring.b */
/* loaded from: classes.dex */
public class C0017b extends KeyguardUnlockView {
    public C0017b(Context context) {
        this(context, null);
    }

    public C0017b(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public C0017b(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.galaxytheme.common.KeyguardUnlockView
    /* renamed from: a */
    public void mo121a(Drawable drawable, boolean z) {
        super.mo121a(drawable, z);
        this.f139b.setImageDrawable(drawable);
        if (this.mUnlockView != null) {
            this.mUnlockView.update(((BitmapDrawable) drawable).getBitmap());
        }
    }

    @Override // com.galaxytheme.common.KeyguardUnlockView
    public void setUnlockView(KeyguardEffectViewBase eVar) {
        super.setUnlockView(eVar);
        if (eVar != null) {
            addView((View) this.mUnlockView);
        }
    }
}
