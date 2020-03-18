package com.smbat.systemuihide;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class DrawOverSystemUIService extends Service {

    private WindowManager windowManager;
    private CustomViewGroup statusBarView;
    private CustomViewGroup navBarView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        statusBarView = new CustomViewGroup(this);
        navBarView = new CustomViewGroup(this);
        addOverlayView(statusBarView, Gravity.TOP, 30);
        addOverlayView(navBarView, Gravity.BOTTOM, 40);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (statusBarView != null) {
            windowManager.removeView(statusBarView);
            statusBarView = null;
        }
        if (navBarView != null) {
            windowManager.removeView(navBarView);
            navBarView = null;
        }
    }

    private void addOverlayView(CustomViewGroup view, int gravity, int height) {
        int layoutParamsType;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            layoutParamsType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        else {
            layoutParamsType = LayoutParams.TYPE_PHONE;
        }
        final WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = layoutParamsType;
        localLayoutParams.gravity = gravity;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (height * getResources()
                .getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;

        if (null != windowManager) {
            windowManager.addView(view, localLayoutParams);
        }
    }
}