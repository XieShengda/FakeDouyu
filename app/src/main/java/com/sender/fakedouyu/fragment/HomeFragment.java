package com.sender.fakedouyu.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sender.fakedouyu.R;
import com.sender.fakedouyu.adapter.MyTopRecyclerViewAdapter;
import com.sender.fakedouyu.bean.RoomInfo;
import com.sender.fakedouyu.listener.RequestSubChannelListener;
import com.sender.fakedouyu.model.NetworkRequestImpl;
import com.sender.fakedouyu.utils.BuildUrl;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private final static String TAG = "home_fragment";
    private RecyclerView topRecyclerView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        topRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        TopRequestChannelListener mListner = new TopRequestChannelListener();
        NetworkRequestImpl mRequest = new NetworkRequestImpl(getContext());
        mRequest.getSubChannel(BuildUrl.getDouyuLiveChannel(),mListner);






        return view;
    }
    private class TopRequestChannelListener implements RequestSubChannelListener{
        @Override
        public void onSuccess(List<RoomInfo> roomInfos) {
            topRecyclerView.setAdapter(new MyTopRecyclerViewAdapter(roomInfos));

        }

        @Override
        public void onError() {
            Toast.makeText(getContext(), "无法加载数据", Toast.LENGTH_SHORT);
        }
    }

}
