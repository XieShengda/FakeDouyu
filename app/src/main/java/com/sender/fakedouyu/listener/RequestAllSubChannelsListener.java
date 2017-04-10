package com.sender.fakedouyu.listener;

import com.sender.fakedouyu.bean.SubChannelInfo;

import java.util.List;

/**
 * 请求所有子频道观察者
 */
public interface RequestAllSubChannelsListener {
    void onSuccess(List<SubChannelInfo> subChannelInfos);

    void onError();
}
