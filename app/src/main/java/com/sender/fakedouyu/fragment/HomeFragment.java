package com.sender.fakedouyu.fragment;


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sender.fakedouyu.R;
import com.sender.fakedouyu.adapter.MyRecyclerViewAdapter;
import com.sender.fakedouyu.adapter.MyViewPagerAdapter;
import com.sender.fakedouyu.bean.RoomInfo;
import com.sender.fakedouyu.listener.RequestSubChannelListener;
import com.sender.fakedouyu.model.NetworkRequestImpl;
import com.sender.fakedouyu.utils.BuildUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private final static String TAG = "home_fragment";
    private RecyclerView topRecyclerView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView topText;
    private  List<Fragment> fragments;
    private List<String> titles;
    private int currentPosition;
    private float topRecyclerHeight;
    private float mFirstY, mCurrentY;
    private boolean isHide = false, isFirstClick = true;//是否显示
    private float mTouchSlop;//获取最小滑动判断值
    private LinearLayoutManager linearLayoutManager;
    private int density;

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
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        topText = (TextView) view.findViewById(R.id.top_text);

        viewPager.setAdapter(new MyViewPagerAdapter(getFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewPager);
        linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        topRecyclerView.setLayoutManager(linearLayoutManager);
        topRecyclerView.addItemDecoration(new MyItemDecoration());
        TopRequestChannelListener mListener = new TopRequestChannelListener();
        NetworkRequestImpl mRequest = new NetworkRequestImpl(getContext());
        mRequest.getSubChannel(BuildUrl.getDouyuLiveChannel(),mListener);
        topText.setOnClickListener(this);

//        viewPager.setOnTouchListener(this);



        return view;
    }




    private class TopRequestChannelListener implements RequestSubChannelListener{
        @Override
        public void onSuccess(List<RoomInfo> roomInfos) {
            topRecyclerView.setAdapter(new MyRecyclerViewAdapter(roomInfos));

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
            density = (int) metrics.density;

            int itemMargin = density * 3;
            outRect.set(itemMargin, itemMargin, itemMargin, itemMargin);
        }
    }

    @Override
    public void onClick(View v) {
        if (isFirstClick){
            topRecyclerHeight = (float)(topRecyclerView.getHeight() + 24 * density);
            isFirstClick = false;
        }
        if (isHide){
//            showTopRecycler();

        }else {
//            hideTopRecycler();

        }
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                mFirstY = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mCurrentY = event.getY();
//                if (mCurrentY - mFirstY > mTouchSlop && !isShow){
//                    showTopRecycler();
//                }else if (mFirstY - mCurrentY > mTouchSlop && isShow){
//                    hideTopRecycler();
//                }
//        }
//        return false;
//    }

//    private void hideTopRecycler() {
//        /**
//         * height动画
//         */
////        ValueAnimator animator = ValueAnimator.ofInt(topRecyclerHeight, 0);
////        animator.setTarget(topRecyclerView);
////        animator.setDuration(1000).start();//可以看到到这边为止并没有设置动画效果
////        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
////            @Override
////            public void onAnimationUpdate(ValueAnimator animation){
////                Integer value = (Integer) animation.getAnimatedValue();//当数值变化的时候获取这个数值
////                //在这边使用这个数值设置view的属性值
////                topRecyclerView.getLayoutParams().height = value;//width属于布局参数所以需要请求布局而不是重绘
////                topRecyclerView.requestLayout();
////            }
////        });
//
//        /**
//         * 比例动画
//         */
////        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0);
//        ValueAnimator animator = ValueAnimator.ofFloat(0, -topRecyclerHeight);
//
//        animator.setTarget(topRecyclerView);
//        animator.setDuration(1000).start();//可以看到到这边为止并没有设置动画效果
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation){
////                Float value = (Float) animation.getAnimatedValue();//当数值变化的时候获取这个数值
//                //在这边使用这个数值设置view的属性值
//                Float value = (Float) animation.getAnimatedValue();
//                topRecyclerView.setY(value);
//                topRecyclerView.requestLayout();
////                topRecyclerView.setScaleY(value);//width属于布局参数所以需要请求布局而不是重绘
//            }
//        });
////        topRecyclerView.setVisibility(View.GONE);
//        isHide = true;
////        currentPosition = linearLayoutManager.findLastVisibleItemPosition();
//    }
//
//    private void showTopRecycler() {
//        /**
//         * height动画
//         */
////        ValueAnimator animator = ValueAnimator.ofInt(0, topRecyclerHeight);
////        animator.setTarget(topRecyclerView);
////        animator.setDuration(1000).start();//可以看到到这边为止并没有设置动画效果
////        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
////            @Override
////            public void onAnimationUpdate(ValueAnimator animation){
////                Integer value = (Integer)animation.getAnimatedValue();//当数值变化的时候获取这个数值
////                //在这边使用这个数值设置view的属性值
////                topRecyclerView.getLayoutParams().height = value;//width属于布局参数所以需要请求布局而不是重绘
////                topRecyclerView.requestLayout();
////            }
////        });
//        /**
//         * 比例动画显示
//         */
////        ValueAnimator animator = ValueAnimator.ofFloat(0, 1.0f);
//
//        ValueAnimator animator = ValueAnimator.ofFloat( -topRecyclerHeight, 0);
//        animator.setTarget(topRecyclerView);
//        animator.setDuration(1000).start();//可以看到到这边为止并没有设置动画效果
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation){
//                Float value = (Float) animation.getAnimatedValue();//当数值变化的时候获取这个数值
//                //在这边使用这个数值设置view的属性值
//                topRecyclerView.setY(value);//width属于布局参数所以需要请求布局而不是重绘
//                topRecyclerView.requestLayout();
//
//            }
//        });
////        topRecyclerView.setVisibility(View.VISIBLE);
//        isHide = false;
////        topRecyclerView.scrollToPosition(currentPosition);
//    }


}
