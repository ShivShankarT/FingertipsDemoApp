package com.example.fingertipsdemoapp.custom;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

public class FingertipsWebView extends WebView {
    public static void a() {
    }

    public FingertipsWebView(Context context) {
        super(context);
        if (!isInEditMode()) {
            a();
        }
    }

    public FingertipsWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            a();
        }
    }

    public FingertipsWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (!isInEditMode()) {
            a();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FingertipsWebView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        if (!isInEditMode()) {
            a();
        }
    }

    public FingertipsWebView(Context context, AttributeSet attributeSet, int i, boolean z) {
        super(context, attributeSet, i, z);
        if (!isInEditMode()) {
            a();
        }
    }
}
