package com.uav.mandiratepe.constant;

import android.app.Application;

public class GlobalApplication extends Application {

    private static boolean activityVisible;

    public static int SessionOutTime; // sec
    public static int ScreenOffSessionOutTime;//min
    public static String updateMsg ="A New Update is Available";

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }


}