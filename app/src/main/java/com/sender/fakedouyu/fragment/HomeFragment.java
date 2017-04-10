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
import android.view.ViewGroup;
import android.view.WindowManager;
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
public class HomeFragment extends Fragment {
    private final static String TAG = "home_fragment";
    private RecyclerView topRecyclerView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  List<Fragment> fragments;
    private List<String> titles;
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
        viewPager.setAdapter(new MyViewPagerAdapter(getFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewPager);

        topRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        topRecyclerView.addItemDecoration(new MyItemDecoration());
        TopRequestChannelListener mListener = new TopRequestChannelListener();
        NetworkRequestImpl mRequest = new NetworkRequestImpl(getContext());
        mRequest.getSubChannel(BuildUrl.getDouyuLiveChannel(),mListener);






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
            int density = (int) metrics.density;

            int itemMargin = density * 3;
            outRect.set(itemMargin, itemMargin, itemMargin, itemMargin);
        }
    }

}
