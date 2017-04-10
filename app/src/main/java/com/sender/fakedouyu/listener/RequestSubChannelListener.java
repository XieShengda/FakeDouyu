package com.sender.fakedouyu.listener;

import com.sender.fakedouyu.bean.RoomInfo;

import java.util.List;

/**
 * 请求指定子频道观察者
 */
public interface RequestSubChannelListener {
    void onSuccess(List<RoomInfo> roomInfos);

    void onError();
}
