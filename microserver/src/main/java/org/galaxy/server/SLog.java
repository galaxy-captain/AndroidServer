package org.galaxy.server;

/**
 * Created by galaxy on 2017/2/9.
 */
class SLog {

    private static boolean isDebug = true;

    private static String TAG = "TAG";

    public static void error(String s) {
        if (isDebug) {
            System.out.println(TAG + "--" + s);
        }
    }


}
