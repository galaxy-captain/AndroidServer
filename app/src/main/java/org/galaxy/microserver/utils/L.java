package org.galaxy.microserver.utils;

import android.util.Log;

/**
 * Created by OoO on 2017/1/21.
 */

public class L {

    public static boolean isDebug = true;

    private static String TAG = "TAG";

    public static void error(String s) {
        if (isDebug) {
            Log.e(TAG, s);
        }
    }

}
