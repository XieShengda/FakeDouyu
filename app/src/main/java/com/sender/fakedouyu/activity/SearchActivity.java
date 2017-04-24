package com.sender.fakedouyu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.sender.fakedouyu.R;
import com.sender.fakedouyu.fragment.LiveRoomFragment;
import com.sender.fakedouyu.utils.BuildUrl;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        LiveRoomFragment fragment = LiveRoomFragment.newInstance(2, BuildUrl.getDouyuSearchUrl(getIntent().getStringExtra("KEY_WORD")));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
