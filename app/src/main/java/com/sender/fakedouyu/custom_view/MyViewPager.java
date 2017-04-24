package com.sender.fakedouyu.custom_view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by XieShengda on 2017/4/12.
 */

public class MyViewPager extends ViewPager {
    private OnTouchListener listener;
    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
        listener = l;
    }

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        listener.onTouch(this, ev);
        return super.dispatchTouchEvent(ev);
    }
}
