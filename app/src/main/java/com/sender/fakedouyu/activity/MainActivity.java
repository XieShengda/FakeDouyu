package com.sender.fakedouyu.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sender.fakedouyu.R;
import com.sender.fakedouyu.fragment.ChannelFragment;
import com.sender.fakedouyu.fragment.FavoriteFragment;
import com.sender.fakedouyu.fragment.HomeFragment;
import com.sender.fakedouyu.fragment.LiveRoomFragment;
import com.sender.fakedouyu.fragment.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements ChannelFragment.OnListFragmentInteractionListener, LiveRoomFragment.OnListFragmentInteractionListener, FavoriteFragment.OnListFragmentInteractionListener{

    private Toolbar toolbar;
    private final static Map<String, Boolean> IS_EXIST = new Hashtable<>();//Fragment是否add过
    private final static List<Fragment> FRAGMENT_LIST = new ArrayList<>();
    private final static int[] tabNames = {R.string.title_home, R.string.title_channel, R.string.title_favorites};

    //选中状态监听器
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle(tabNames[0]);
                    showFragment(FRAGMENT_LIST.get(0));
                    return true;//为true则设置当前项为选中
                case R.id.navigation_dashboard:
                    toolbar.setTitle(tabNames[1]);
                    showFragment(FRAGMENT_LIST.get(1));

                    return true;
                case R.id.navigation_favorite:
                    toolbar.setTitle(tabNames[2]);
                    showFragment(FRAGMENT_LIST.get(2));

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

        //显示首页
        initFragment();
        showFragment(FRAGMENT_LIST.get(0));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    //初始化Fragment
    private void initFragment() {

//        LiveRoomFragment liveRoomFragment = new LiveRoomFragment();
        HomeFragment homeFragment = new HomeFragment();
        FRAGMENT_LIST.add(homeFragment);
        ChannelFragment channelFragment = new ChannelFragment();
        FRAGMENT_LIST.add(channelFragment);
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        FRAGMENT_LIST.add(favoriteFragment);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    //add或切换Fragment
    private void showFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (IS_EXIST.get(fragment.getClass().getName()) == null){
            transaction.add(R.id.content, fragment);
            selectFragment(transaction, fragment);
            IS_EXIST.put(fragment.getClass().getName(), true);

        }
        else {
            selectFragment(transaction, fragment);
        }
        transaction.commit();
    }

    //隐藏所有格Fragment，仅显示选中Fragment
    private void selectFragment(FragmentTransaction transaction, Fragment fragment) {
        for (Fragment f : FRAGMENT_LIST){
            transaction.hide(f);
        }
        transaction.show(fragment);
    }
}
