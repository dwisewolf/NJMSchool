package com.wisewolf.njmschool;

import android.util.Log;

import com.vimeo.networking.logging.LogProvider;


public class NetworkingLogger implements LogProvider {

    private static final String TEST_APP = "~NET TEST APP~";

    @Override
    public void e(String error) {
        Log.e(TEST_APP, error);
    }

    @Override
    public void e(String error, Exception exception) {
        Log.e(TEST_APP, error, exception);
    }

    @Override
    public void d(String debug) {
        Log.d(TEST_APP, debug);
    }

    @Override
    public void v(String verbose) {
        Log.v(TEST_APP, verbose);
    }
}
