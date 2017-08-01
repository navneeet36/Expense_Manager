package com.example.hp.expense_manager.utils;

import android.util.Log;


public class FLog {
    private static String TAG = "JMIT";
    private static boolean showLog = true;

    public static void d(String VAL) {
        if (showLog) {
            d(TAG, VAL);
        }
    }


    public static void d(String TAG, String VAL) {
        if (showLog) {
            Log.d(TAG, "" + VAL);
        }
    }
}
