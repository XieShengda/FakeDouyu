package com.sender.fakedouyu.custom_view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by XieShengda on 2017/4/17.
 */

public class MyRecyclerView extends RecyclerView {
    private final static int OFFSET = 3;
    private OnTouchListener mListener;

    public MyRecyclerView(Context context) {
        super(context);
        this.addItemDecoration(new MyItemDecoration(OFFSET));
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.addItemDecoration(new MyItemDecoration(OFFSET));

    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.addItemDecoration(new MyItemDecoration(OFFSET));

    }
    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
        mListener = l;
    }

    //定义ItemDecoration
    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        private int offset;

        public MyItemDecoration(int offset) {
            this.offset = offset;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            WindowManager manager = (WindowManager) MyRecyclerView.this.getContext().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metrics);
            int density = (int) metrics.density;

            int itemMargin = density * offset;
            outRect.set(itemMargin, itemMargin, itemMargin, itemMargin);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mListener != null){

            mListener.onTouch(this, ev);
        }
        return super.dispatchTouchEvent(ev);
    }


}
