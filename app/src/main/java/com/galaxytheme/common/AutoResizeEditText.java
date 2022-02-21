package com.galaxytheme.common;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.galaxytheme.brilliantring.R;
import com.xlocker.core.sdk.LogUtil;

public class AutoResizeEditText extends TextView {

    /* renamed from: a */
    private int f94a;

    /* renamed from: b */
    private int f95b;

    /* renamed from: c */
    private int f96c;

    /* renamed from: d */
    private int f97d;

    /* renamed from: e */
    private boolean f98e;

    /* renamed from: f */
    private boolean f99f;

    /* renamed from: g */
    private TextWatcher f100g = new TextWatcher() { // from class: com.galaxytheme.common.AutoResizeEditText.1
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            AutoResizeEditText.this.f99f = false;
            AutoResizeEditText.this.f98e = false;
        }
    };

    public AutoResizeEditText(Context context) {
        super(context);
        m137a();
    }

    public AutoResizeEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        m137a();
    }

    public AutoResizeEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        m137a();
    }

    /* renamed from: a */
    private void m137a() {
        this.f94a = (int) (getResources().getDimensionPixelSize(R.dimen.kg_status_myprofile_font_max_size) * GalaxySettings.getFontSize(getContext()));
        this.f95b = (int) (getResources().getDimensionPixelSize(R.dimen.kg_status_myprofile_font_min_size) * GalaxySettings.getFontSize(getContext()));
        this.f96c = (int) (getResources().getDimensionPixelSize(R.dimen.kg_status_myprofile_font_default_size) * GalaxySettings.getFontSize(getContext()));
        this.f97d = getResources().getDimensionPixelSize(R.dimen.kg_status_myprofile_font_size_slot);
        setTextSize(0, this.f96c);
        addTextChangedListener(this.f100g);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i);
        int measuredWidth = getMeasuredWidth();
        LogUtil.i("auto", "onMeasure, widthSpec = " + size + ", measuredWidth = " + measuredWidth);
        if (size <= 0 || measuredWidth < size) {
            float textSize = getTextSize();
            if (textSize < this.f94a && !this.f98e) {
                LogUtil.i("auto", "textSize too small, increase textSize, current size = " + textSize + ", maxSize = " + this.f94a);
                this.f99f = true;
                setTextSize(0, textSize + this.f97d);
                super.measure(i, i2);
            } else if (textSize < this.f94a) {
                LogUtil.i("auto", "don't increase text size, or it will be too large.");
            }
        } else {
            float textSize2 = getTextSize();
            LogUtil.i("auto", "measuredWidth too large, decrease textSize, current size = " + textSize2);
            if (this.f99f) {
                this.f98e = true;
                LogUtil.i("auto", "too large, please stop increasing!!!");
            } else {
                this.f98e = false;
            }
            if (textSize2 > this.f95b) {
                setTextSize(0, textSize2 - this.f97d);
                super.measure(i, i2);
            }
        }
    }
}
