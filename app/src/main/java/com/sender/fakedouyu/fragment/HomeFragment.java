package com.sender.fakedouyu.fragment;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sender.fakedouyu.R;
import com.sender.fakedouyu.adapter.MyRecyclerViewAdapter;
import com.sender.fakedouyu.adapter.MyViewPagerAdapter;
import com.sender.fakedouyu.bean.RoomInfo;
import com.sender.fakedouyu.custom_view.MyViewPager;
import com.sender.fakedouyu.listener.RequestSubChannelListener;
import com.sender.fakedouyu.model.NetworkRequestImpl;
import com.sender.fakedouyu.utils.BuildUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnTouchListener{
    private final static String TAG = "home_fragment";
    private final static int SHOW = 1, HIDE = -1;
    private RecyclerView topRecyclerView;
    private TabLayout tabLayout;
    private MyViewPager viewPager;
    private LinearLayout topRecyclerContainer;
    private  List<Fragment> fragments;
    private List<String> titles;
    private float topContainerHeight, viewPagerHeight;
    private float mFirstX, mCurrentX, mFirstY, mCurrentY;
    private boolean mIsHide = false, isFirstTouch = true;//是否显示
    private float mTouchSlop;//获取最小滑动判断值

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建Fragment表
        fragments = new ArrayList<>();
        fragments.add(LiveRoomFragment.newInstance(2, BuildUrl.getDouyuLOLSubChannel()));
        fragments.add(LiveRoomFragment.newInstance(2, BuildUrl.getDouyuFurnaceStoneSubChannel()));
        fragments.add(LiveRoomFragment.newInstance(2, BuildUrl.getDouyuDota2SubChannel()));
        //创建标题表
        titles = new ArrayList<>();
        titles.add("LOL");
        titles.add("炉石传说");
        titles.add("Dota2");
        Log.d(TAG, "onCreate");
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        topRecyclerView = (RecyclerView) view.findViewById(R.id.top_recycler);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (MyViewPager) view.findViewById(R.id.view_pager);
        topRecyclerContainer = (LinearLayout) view.findViewById(R.id.top_recycler_container);

        viewPager.setAdapter(new MyViewPagerAdapter(getFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewPager);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        topRecyclerView.setLayoutManager(linearLayoutManager);
        topRecyclerView.addItemDecoration(new MyItemDecoration());
        TopRequestChannelListener mListener = new TopRequestChannelListener();
        NetworkRequestImpl mRequest = new NetworkRequestImpl(getContext());
        mRequest.getSubChannel(BuildUrl.getDouyuLiveChannel(),mListener);

        viewPager.setOnTouchListener(this);



        return view;
    }




    private class TopRequestChannelListener implements RequestSubChannelListener{
        @Override
        public void onSuccess(List<RoomInfo> roomInfos) {
            topRecyclerView.setAdapter(new MyRecyclerViewAdapter(getContext(), roomInfos, BuildUrl.getDouyuLiveChannel()));

        }

        @Override
        public void onError() {
            Toast.makeText(getContext(), "无法加载数据", Toast.LENGTH_SHORT);
        }
    }

    private class MyItemDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            Context context = getContext();
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metrics);
            int density = (int) metrics.density;

            int itemMargin = density * 3;
            outRect.set(itemMargin, itemMargin, itemMargin, itemMargin);
        }
    }

//    @Override
//    public void onClick(View v) {
//        if (isFirstTouch){
//            topRecyclerHeight = (float)(topRecyclerView.getHeight() + 24 * density);
//            isFirstTouch = false;
//        }
//        if (mIsHide){
////            showTopRecycler();
//
//        }else {
////            hideTopRecycler();
//
//        }
//    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isFirstTouch){
//            topContainerHeight = topRecyclerContainer.getHeight();
            viewPagerHeight = viewPager.getHeight();
            isFirstTouch = false;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                topContainerHeight = topRecyclerContainer.getHeight();
                mFirstX = event.getX();
                mFirstY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                mCurrentY = event.getY();
                if (mCurrentY - mFirstY > mTouchSlop && mIsHide
                        && (mCurrentX > mFirstX && mCurrentX - mFirstX < mTouchSlop || mFirstX > mCurrentX && mFirstX - mCurrentX < mTouchSlop)){
                    showAnimation(SHOW);
                }else if (mFirstY - mCurrentY > mTouchSlop && !mIsHide
                        && (mCurrentX > mFirstX && mCurrentX - mFirstX < mTouchSlop || mFirstX > mCurrentX && mFirstX - mCurrentX < mTouchSlop)){
                    showAnimation(HIDE);
                }
                break;
        }
        return false;
    }


    private void showAnimation(int showOrHide){
        ValueAnimator animator = null;
        if (HIDE == showOrHide) {
            animator = ValueAnimator.ofFloat(0, -topContainerHeight);
        }else if (SHOW == showOrHide) {
            animator = ValueAnimator.ofFloat(-topContainerHeight, 0);
        }
        if (animator != null) {

            animator.setDuration(400).start();//可以看到到这边为止并没有设置动画效果
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
                @Override
                public void onAnimationUpdate(ValueAnimator animation){
//                Float value = (Float) animation.getAnimatedValue();//当数值变化的时候获取这个数值
                    //在这边使用这个数值设置view的属性值
                    Float value = (Float) animation.getAnimatedValue();
                    topRecyclerContainer.setTranslationY(value);
                    tabLayout.setTranslationY(value);
                    viewPager.setTranslationY(value);
                    viewPager.getLayoutParams().height = (int) (viewPagerHeight - value);
                    viewPager.requestLayout();
                }
            });
            mIsHide = !mIsHide;
        }
    }




}
