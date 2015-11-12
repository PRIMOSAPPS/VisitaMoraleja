package com.primos.visitamoraleja.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class PrimosWebView extends WebView {
    public PrimosWebView(Context context) {
        super(context);
    }

    public PrimosWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PrimosWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }
}
