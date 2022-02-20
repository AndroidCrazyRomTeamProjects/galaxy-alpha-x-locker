package com.galaxytheme.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.galaxytheme.brilliantring.R;
import com.xlocker.core.sdk.GlobalSettings;
import com.xlocker.core.sdk.LockPatternUtils;
import com.xlocker.core.sdk.LogUtil;
import com.xlocker.core.sdk.widget.ClockView;
import java.util.ArrayList;
import java.util.Date;

/* loaded from: classes.dex */
public class KeyguardStatusView extends GridLayout implements AdapterView.OnItemSelectedListener {

    public static final int IdleAlarm = R.drawable.ic_lock_idle_alarm;

    /* renamed from: h */
    private static Typeface f116h;

    /* renamed from: i */
    private static Typeface f117i;

    /* renamed from: b */
    private CharSequence f118b;

    /* renamed from: c */
    private TextView f119c;

    /* renamed from: d */
    private TextView f120d;

    /* renamed from: e */
    private ClockView f121e;

    /* renamed from: f */
    private View f122f;

    /* renamed from: g */
    private AutoResizeEditText f123g;
    private LockPatternUtils mLockPatternUtils;

    /* renamed from: k */
    private AbstractC0031b f125k;

    /* renamed from: l */
    private AbstractC0030a f126l;

    /* renamed from: m */
    private BroadcastReceiver f127m;

    /* renamed from: n */
    private ContentObserver f128n;

    /* renamed from: o */
    private boolean f129o;

    /* renamed from: p */
    private final Rect f130p;

    /* renamed from: q */
    private boolean f131q;

    /* renamed from: r */
    private ArrayList<C0032c> f132r;

    /* renamed from: com.galaxytheme.common.KeyguardStatusView$a */
    /* loaded from: classes.dex */
    public interface AbstractC0030a {
        /* renamed from: a */
        void m123a();
    }

    /* renamed from: com.galaxytheme.common.KeyguardStatusView$b */
    /* loaded from: classes.dex */
    public interface AbstractC0031b {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: com.galaxytheme.common.KeyguardStatusView$c */
    /* loaded from: classes.dex */
    public static class C0032c {

        /* renamed from: a */
        private int f135a;

        /* renamed from: b */
        private String f136b;

        /* renamed from: c */
        private String f137c;

        private C0032c(int i, String str, String str2) {
            this.f135a = i;
            this.f136b = str;
            this.f137c = str2;
        }
    }

    public KeyguardStatusView(Context context) {
        this(context, null, 0);
    }

    public KeyguardStatusView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyguardStatusView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f127m = new BroadcastReceiver() { // from class: com.galaxytheme.common.KeyguardStatusView.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                KeyguardStatusView.this.m134a();
            }
        };
        this.f128n = new ContentObserver(new Handler()) { // from class: com.galaxytheme.common.KeyguardStatusView.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                KeyguardStatusView.this.f118b = KeyguardStatusView.this.getDateFormat();
                KeyguardStatusView.this.m127c();
            }
        };
        this.f130p = new Rect();
        this.f132r = new ArrayList<>();
        this.mLockPatternUtils = new LockPatternUtils(getContext());
        m124f();
    }

    /* renamed from: a */
    private int m133a(int i) {
        switch (i) {
            case 1:
            default:
                return 3;
            case 2:
                return 1;
            case 3:
                return 5;
        }
    }

    /* renamed from: a */
    private void m132a(TextView textView, CharSequence charSequence) {
        textView.setText(charSequence);
    }

    /* renamed from: d */
    private void m126d() {
        try {
            f116h = GalaxySettings.getTypeface(getContext());
        } catch (Exception e) {
        }
        if (f116h != null) {
            this.f123g.setTypeface(f116h);
        }
    }

    /* renamed from: e */
    private void m125e() {
        try {
            f117i = GalaxySettings.getClockFont(getContext());
        } catch (Exception e) {
        }
        if (f117i != null) {
            this.f121e.setTypeface(f117i);
        }
    }

    /* renamed from: f */
    private void m124f() {
        Log.i("lockscreen", "loadAllTypeface, asset = " + getContext().getResources().getAssets());
        this.f132r.add(new C0032c(1, getContext().getResources().getString(R.string.default_typeface), "fonts/kaiti.ttf"));
        this.f132r.addAll(getSavedExternalTypefaces());
        this.f132r.add(new C0032c(3, getContext().getResources().getString(R.string.custom_typeface), null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDateFormat() {
        String string = Settings.System.getString(getContext().getContentResolver(), "date_format");
        LogUtil.i("KeyguardStatusView", "date format: " + ((Object) this.f118b));
        if (TextUtils.isEmpty(string)) {
            string = getContext().getString(R.string.abbrev_wday_month_day_no_year);
        }
        return ("yyyy-MM-dd".equals(string) || "MM-dd-yyyy".equals(string)) ? getContext().getString(R.string.abbrev_wday_abbrev_month_day_no_year) : "dd-MM-yyyy".equals(string) ? getContext().getString(R.string.abbrev_wday_day_abbrev_month_no_year) : string;
    }

    private String getNextAlarm() {
        return this.mLockPatternUtils.getNextAlarm();
    }

    private ArrayList<C0032c> getSavedExternalTypefaces() {
        String[] split;
        ArrayList<C0032c> arrayList = new ArrayList<>();
        String string = getContext().getSharedPreferences("myprofile", Context.MODE_WORLD_READABLE).getString("type_face_items", null);
        if (!(string == null || (split = string.split("*#110#*")) == null)) {
            for (String str : split) {
                String[] split2 = str.split("#*#");
                if (split2 != null) {
                    arrayList.add(new C0032c(Integer.parseInt(split2[0]), split2[1], split2[2]));
                }
            }
        }
        return arrayList;
    }

    /* renamed from: a */
    protected void m134a() {
        this.f121e.updateTime();
        m127c();
        m128b();
    }

    /* renamed from: a */
    public void m129a(boolean z, Rect rect) {
        this.f129o = z;
        if (rect != null) {
            this.f130p.set(rect);
        } else {
            this.f130p.set(0, 0, 0, 0);
        }
    }

    /* renamed from: b */
    void m128b() {
        String nextAlarm = getNextAlarm();
        LogUtil.i("KeyguardStatusView", "nextAlarm: " + nextAlarm);
        if (!GlobalSettings.isAlarmEnable(getContext().getApplicationContext()) || TextUtils.isEmpty(nextAlarm)) {
            this.f120d.setVisibility(GONE);
            return;
        }
        m132a(this.f120d, nextAlarm);
        this.f120d.setCompoundDrawablesWithIntrinsicBounds(IdleAlarm, 0, 0, 0);
        this.f120d.setVisibility(VISIBLE);
    }

    /* renamed from: c */
    void m127c() {
        m132a(this.f119c, DateFormat.format(this.f118b, new Date()));
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_TICK");
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        getContext().registerReceiver(this.f127m, intentFilter);
        getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor("date_format"), false, this.f128n);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unregisterReceiver(this.f127m);
        getContext().getContentResolver().unregisterContentObserver(this.f128n);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        getContext().getResources();
        this.f118b = getDateFormat();
        this.f119c = (TextView) findViewById(R.id.date);
        this.f120d = (TextView) findViewById(R.id.alarm_status);
        this.f121e = findViewById(R.id.clock_view);
        this.f122f = findViewById(R.id.keyguard_status_area);
        this.f123g = (AutoResizeEditText) findViewById(R.id.keyguard_status_myprofile);
        ViewGroup.LayoutParams layoutParams = this.f123g.getLayoutParams();
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).gravity = m133a(GalaxySettings.getMyprofileAlign(getContext()));
        }
        this.f123g.setLayoutParams(layoutParams);
        if (GalaxySettings.isEnableProfile(getContext())) {
            this.f123g.setVisibility(VISIBLE);
            m126d();
            String profile = GalaxySettings.getProfile(getContext());
            if (profile != null) {
                this.f123g.setText(profile);
            }
        } else {
            this.f123g.setVisibility(GONE);
        }
        m125e();
        this.f121e.setTextSize(0, (GalaxySettings.isEnableProfile(getContext()) ? 1.0f : 1.25f) * GalaxySettings.getClockSize(getContext()) * getContext().getResources().getDimensionPixelSize(R.dimen.kg_status_clock_font_size));
        ViewGroup.LayoutParams layoutParams2 = this.f121e.getLayoutParams();
        if (layoutParams2 instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams2).gravity = m133a(GalaxySettings.getClockAlign(getContext()));
        }
        this.f121e.setLayoutParams(layoutParams2);
        ViewGroup.LayoutParams layoutParams3 = this.f122f.getLayoutParams();
        if (layoutParams3 instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams3).gravity = m133a(GalaxySettings.getClockAlign(getContext()));
        }
        this.f122f.setLayoutParams(layoutParams3);
        ViewGroup.LayoutParams layoutParams4 = this.f119c.getLayoutParams();
        if (layoutParams4 instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams4).gravity = m133a(GalaxySettings.getClockAlign(getContext()));
        }
        this.f119c.setLayoutParams(layoutParams4);
        ViewGroup.LayoutParams layoutParams5 = this.f120d.getLayoutParams();
        if (layoutParams5 instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams5).gravity = m133a(GalaxySettings.getClockAlign(getContext()));
        }
        this.f120d.setLayoutParams(layoutParams5);
        this.f119c.requestFocus();
        m129a(false, (Rect) null);
        View[] viewArr = {this.f119c, this.f120d};
        for (int i = 0; i < viewArr.length; i++) {
            View view = viewArr[i];
            if (view == null) {
                throw new RuntimeException("Can't find widget at index " + i);
            }
            view.setSelected(true);
        }
        m134a();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() != 0 || !this.f129o || this.f130p.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
            this.f131q = super.onInterceptTouchEvent(motionEvent);
            return this.f131q;
        }
        this.f131q = true;
        return true;
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            this.f119c.requestFocus();
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getWindowToken(), 0);
        } else if (motionEvent.getAction() == 0 && this.f131q && this.f126l != null) {
            this.f126l.m123a();
        }
        return this.f131q;
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            m134a();
        }
    }

    public void setCallback(AbstractC0030a aVar) {
        this.f126l = aVar;
    }

    public void setFocusListener(AbstractC0031b bVar) {
        this.f125k = bVar;
    }
}
