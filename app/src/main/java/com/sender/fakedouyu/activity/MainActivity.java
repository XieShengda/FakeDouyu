package com.sender.fakedouyu.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.sender.fakedouyu.R;
import com.sender.fakedouyu.fragment.ChannelFragment;
import com.sender.fakedouyu.fragment.FavoriteFragment;
import com.sender.fakedouyu.fragment.HomeFragment;
import com.sender.fakedouyu.fragment.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements FavoriteFragment.OnListFragmentInteractionListener{

    private final static String TAG  = "MainActivity";
    private Toolbar toolbar;
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

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

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
}
