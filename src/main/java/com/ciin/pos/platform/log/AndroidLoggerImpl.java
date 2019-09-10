package com.ciin.pos.platform.log;

import android.util.Log;

public class AndroidLoggerImpl implements Logger {

    private static final String TAG = "escpos";

    @Override
    public void debug(String message) {
        Log.d(TAG, message);
    }

    @Override
    public void info(String message) {
        Log.i(TAG, message);
    }

    @Override
    public void error(String message) {
        Log.e(TAG, message);
    }

    @Override
    public void warn(String message) {
        Log.w(TAG, message);
    }

}