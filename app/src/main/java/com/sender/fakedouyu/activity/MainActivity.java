package com.sender.fakedouyu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sender.fakedouyu.R;
import com.sender.fakedouyu.fragment.ChannelFragment;
import com.sender.fakedouyu.fragment.FavoriteFragment;
import com.sender.fakedouyu.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity{

    private final static String TAG  = "MainActivity";
    private Toolbar toolbar;
    private ImageView welImg;
//    private CollapsingToolbarLayout toolbar;
    private Map<String, Boolean> isExist;//Fragment是否add过
    private List<Fragment> fragmentList;
    private final static int[] tabNames = {R.string.title_home, R.string.title_channel, R.string.title_favorites};

    //选中状态监听器
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle(tabNames[0]);
//                    toolbar.setTitle("主页");
                    showFragment(fragmentList.get(0));
                    return true;//为true则设置当前项为选中
                case R.id.navigation_dashboard:
                    toolbar.setTitle(tabNames[1]);
//                    toolbar.setTitle("频道");
                    showFragment(fragmentList.get(1));

                    return true;
                case R.id.navigation_favorite:
                    toolbar.setTitle(tabNames[2]);
//                    toolbar.setTitle("收藏");
                    showFragment(fragmentList.get(2));

                    return true;
//                case R.id.navigation_notifications:
//                    toolbar.setTitle(R.string.title_notifications);
//                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        isExist = new Hashtable<>();
        fragmentList = new ArrayList<>();
        //显示首页
        initFragment();
        showFragment(fragmentList.get(0));

        toolbar =(Toolbar) findViewById(R.id.toolbar);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null){
                    Snackbar.make(searchView, "没有输入数据，再试试吧", Snackbar.LENGTH_LONG).setAction("Action", null ).show();
                    return false;
                }else{
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("KEY_WORD", query);
                    startActivity(intent);
                    return true;
                }

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        welImg = (ImageView) findViewById(R.id.wel_img);
        final FrameLayout welBg = (FrameLayout) findViewById(R.id.bg_wel);
        hideWel(welBg);
    }

    //  隐藏欢迎界面
    private void hideWel(final FrameLayout frameLayout) {
        AnimationSet outSet = getOutSet();
        outSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                frameLayout.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.content_out));
                frameLayout.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        welImg.startAnimation(outSet);
    }

    //  获取欢迎页面动画
    private AnimationSet getOutSet() {
       AnimationSet outSet = new AnimationSet(true);

        TranslateAnimation animOut3 =
                new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT,-1.0f,
                        Animation.RELATIVE_TO_PARENT,1.0f,
                        Animation.RELATIVE_TO_PARENT,0f,
                        Animation.RELATIVE_TO_PARENT,0f);


//        RotateAnimation animOut1 = new RotateAnimation(
//                0.0f, 360.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//
//        ScaleAnimation animOut2 = new ScaleAnimation(
//                1.0f, 0.0f, 1.0f, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);

//        outSet.addAnimation(animOut1);
//        outSet.addAnimation(animOut2);
        outSet.addAnimation(animOut3);
        outSet.setDuration(2000);
        outSet.setInterpolator(new AccelerateInterpolator());
//        outSet.setStartOffset(500);
        outSet.setFillAfter(true);
        return outSet;
    }

    //初始化Fragment
    private void initFragment() {

        // TODO: 2017/4/10 使用工厂方法
//        LiveRoomFragment liveRoomFragment = new LiveRoomFragment();
        HomeFragment homeFragment = new HomeFragment();
        fragmentList.add(homeFragment);
        ChannelFragment channelFragment = new ChannelFragment();
        fragmentList.add(channelFragment);
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        fragmentList.add(favoriteFragment);
    }


    //add或切换Fragment
    private void showFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isExist.get(fragment.getClass().getName()) == null){
            transaction.add(R.id.content, fragment);
            selectFragment(transaction, fragment);
            isExist.put(fragment.getClass().getName(), true);

        }
        else {
            selectFragment(transaction, fragment);
        }
        transaction.commit();
    }

    //隐藏所有格Fragment，仅显示选中Fragment
    private void selectFragment(FragmentTransaction transaction, Fragment fragment) {
        for (Fragment f : fragmentList){
            transaction.hide(f);
        }
        transaction.show(fragment);
    }

    /**
     * 以下用于查看生命周期
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            dblClickExit();
        }
        return false;
    }

    private boolean isExit;
    private void dblClickExit() {
        if (!isExit) {
            isExit = true;
            Snackbar.make(toolbar, "再按一次退出", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            Timer tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
