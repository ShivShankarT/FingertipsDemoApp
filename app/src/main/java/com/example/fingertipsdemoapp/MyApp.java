package com.example.fingertipsdemoapp;

import android.app.Application;

import org.scilab.forge.jlatexmath.core.AjLatexMath;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AjLatexMath.init(this);
    }
}
