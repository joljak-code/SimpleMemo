package com.lwh8762.simplememo;

import android.content.SharedPreferences;

/**
 * Created by W on 2017-02-05.
 */

public class PrefManager {
    private static SharedPreferences sharedPreferences = null;

    public static void setSharedPreferences(SharedPreferences sharedPreferences) {
        PrefManager.sharedPreferences = sharedPreferences;

        if (PrefManager.sharedPreferences.getAll().size() == 0) {
            SharedPreferences.Editor editor = PrefManager.sharedPreferences.edit();
            editor.putBoolean("power", false);
            editor.commit();
        }
    }

    public static void setPower(boolean enable) {
        SharedPreferences.Editor editor = PrefManager.sharedPreferences.edit();
        editor.putBoolean("power", enable);
        editor.commit();
    }

    public static boolean powerEnabled() {
        return PrefManager.sharedPreferences.getBoolean("power", false);
    }
}
