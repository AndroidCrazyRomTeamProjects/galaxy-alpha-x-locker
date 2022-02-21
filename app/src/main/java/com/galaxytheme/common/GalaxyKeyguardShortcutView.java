package com.galaxytheme.common;

import android.content.Context;
import android.graphics.Point;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.xlocker.core.sdk.widget.KeyguardShortcutView;

public class GalaxyKeyguardShortcutView extends KeyguardShortcutView {
    private static final long LAUNCH_DELAY = -100;
    private View mRootContainer;
    private Point mTmpPosition = new Point();
    private KeyguardUnlockView mUnlockView;

    public GalaxyKeyguardShortcutView(Context context) {
        super(context, (AttributeSet) null);
    }

    public GalaxyKeyguardShortcutView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void getPositionInContainer(View view, Point point) {
        int left = view.getLeft();
        int top = view.getTop();
        View view2 = (View) view.getParent();
        int scrollX = left - view2.getScrollX();
        int scrollY = top - view2.getScrollY();
        while (view2 != this.mRootContainer) {
            int left2 = scrollX + view2.getLeft();
            int top2 = scrollY + view2.getTop();
            view2 = (View) view2.getParent();
            scrollX = left2 - view2.getScrollX();
            scrollY = top2 - view2.getScrollY();
        }
        point.x = scrollX;
        point.y = scrollY;
    }

    public boolean handleTouchItem(final KeyguardShortcutView.ShortcutItem shortcutItem, MotionEvent motionEvent) {
        if (!this.mIsFirst) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        getPositionInContainer(shortcutItem, this.mTmpPosition);
        motionEvent.setLocation(this.mTmpPosition.x + x, this.mTmpPosition.y + y);
        try {
            this.mUnlockView.m119a((View) shortcutItem, motionEvent);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        motionEvent.setLocation(x, y);
        switch (motionEvent.getAction()) {
            case 1:
                if (this.mUnlockView.m122a()) {
                    this.mIsFirst = false;
                    this.mUnlockView.postDelayed(new Runnable() { // from class: com.galaxytheme.common.GalaxyKeyguardShortcutView.2
                        @Override // java.lang.Runnable
                        public void run() {
                            GalaxyKeyguardShortcutView.this.launchShortcut(shortcutItem);
                        }
                    }, this.mUnlockView.getUnlockDelay() + LAUNCH_DELAY);
                    return false;
                }
                break;
            case 2:
                if (this.mUnlockView.m122a()) {
                    this.mIsFirst = false;
                    this.mUnlockView.postDelayed(new Runnable() { // from class: com.galaxytheme.common.GalaxyKeyguardShortcutView.1
                        @Override // java.lang.Runnable
                        public void run() {
                            GalaxyKeyguardShortcutView.this.launchShortcut(shortcutItem);
                        }
                    }, this.mUnlockView.getUnlockDelay() + LAUNCH_DELAY);
                    break;
                }
                break;
        }
        return GalaxyKeyguardShortcutView.super.handleTouchItem(shortcutItem, motionEvent);
    }

    public void setRootContainer(View view) {
        this.mRootContainer = view;
    }

    public void setUnlockView(KeyguardUnlockView keyguardUnlockView) {
        this.mUnlockView = keyguardUnlockView;
    }
}
