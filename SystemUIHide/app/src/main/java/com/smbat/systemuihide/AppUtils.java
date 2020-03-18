package com.smbat.systemuihide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;

class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Hides system depended UI elements: such as navigation bar (device soft key buttons, etc..)
     */
    static void hideSystemUI(final Activity activity) {
        if (activity == null) {
            return;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }

    /**
     * Sets system UI listener to not to open navigation bar on bottom swipe, etc..
     */
    static void setSystemUIListener(final Activity activity) {
        if (activity == null) {
            return;
        }

        //listens and check if app is in immersive mode or not
        activity.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener
                (visibility -> {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        // adjustments to the UI, such as showing the action bar or
                        // other navigational controls
                        hideSystemUI(activity);
                    }
                });
    }

    /**
     * Enables draw over apps permission for drawing transparent layer over the system navigation
     * bar
     * for disabling users to tap on system bar buttons (back, home, recent list, ...)
     *
     * @param activity the main activity to draw on
     */
    static void enableDrawOverApp(final Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(activity)) {
                disableNavigation(activity.getApplicationContext());
                disableNavigation(activity.getApplicationContext());
            } else {
                final Intent intent =
                        new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivity(intent);
            }
        } else {
            disableNavigation(activity.getApplicationContext());
            disableNavigation(activity.getApplicationContext());
        }
    }

    /**
     * Disables navigation bar and status bar by drawing views using Service
     *
     */
    private static void disableNavigation(final Context appContext) {
        Intent svc = new Intent(appContext, DrawOverSystemUIService.class);
        appContext.startService(svc);
    }
}
