package com.example.fingertipsdemoapp;

import android.app.Application;

import org.scilab.forge.jlatexmath.core.AjLatexMath;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AjLatexMath.init(this);
    }

    public static String formateEscapeChar(String str) {
        String str2 = "";
        String replace = str.replace("\\", "\\\\")
                .replace("\n", str2)
                .replace("\r", str2)
                .replace("'", "\\'");
        return replace;
    }
}
