package com.lcs.joke;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class JokeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
