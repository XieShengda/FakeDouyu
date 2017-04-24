package com.sender.fakedouyu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sender.fakedouyu.R;
import com.sender.fakedouyu.adapter.MyRecyclerViewAdapter;
import com.sender.fakedouyu.bean.RoomInfo;
import com.sender.fakedouyu.custom_view.MyRecyclerView;
import com.sender.fakedouyu.listener.RequestSubChannelListener;
import com.sender.fakedouyu.model.NetworkRequestImpl;
import com.sender.fakedouyu.utils.BuildUrl;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity implements View.OnTouchListener{
    private int tagId;
    private ImageView channelImage;
    private MyRecyclerView roomInChannel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private List<RoomInfo> mRoomInfos;
    private long startTime, endTime;
    private float startX, startY, endX, endY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Intent intent = getIntent();
        mRoomInfos = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        channelImage = (ImageView) findViewById(R.id.channel_img);
        roomInChannel = (MyRecyclerView) findViewById(R.id.room_in_channel);
        roomInChannel.setOnTouchListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        Glide.with(this).load(intent.getStringExtra("ICON_URL")).into(channelImage);

        setSupportActionBar(toolbar);
        toolbar.setTitle(intent.getStringExtra("TAG_NAME"));

        //加载频道中的房间
        tagId = intent.getIntExtra("TAG_ID", -1);
        final String url = BuildUrl.getDouyuSubChannelBaseTag(tagId);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, mRoomInfos, url);
        roomInChannel.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        roomInChannel.addItemDecoration(new MyItemDecoration(3));
        roomInChannel.setAdapter(myRecyclerViewAdapter);
        final NetworkRequestImpl networkRequest = new NetworkRequestImpl(this);
        networkRequest.getSubChannel(url, new RequestSubChannelListener() {
            @Override
            public void onSuccess(List<RoomInfo> roomInfos) {
                mRoomInfos.addAll(roomInfos);
                myRecyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError() {
                Snackbar.make(roomInChannel, "无法加载数据", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //下拉刷新设置
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary_light));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                networkRequest.getSubChannel(url, new RequestSubChannelListener() {
                    @Override
                    public void onSuccess(List<RoomInfo> roomInfos) {
                        mRoomInfos.clear();
                        mRoomInfos.addAll(roomInfos);
                        myRecyclerViewAdapter.resetOffset();
                        myRecyclerViewAdapter.notifyDataSetChanged();
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError() {
                        if (swipeRefreshLayout.isRefreshing()) {
                            Snackbar.make(roomInChannel, "无法加载数据", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });
            }
        });

        //浮动按钮点击事件
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "收藏频道成功", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    //右划返回
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
        startX = event.getX();
        startY = event.getY();
        startTime = System.currentTimeMillis();

        break;
        case MotionEvent.ACTION_UP:
        endX = event.getX();
        endY = event.getY();
        endTime = System.currentTimeMillis();
        if (endX - startX > 200 && endTime - startTime < 500) {
            final int limitY = 50;
            if (endY > startY && endY - startY < limitY || endY < startY && startY - endY < limitY) {
                finish();
            }
        }
        break;
    }

        return false;
    }




}
