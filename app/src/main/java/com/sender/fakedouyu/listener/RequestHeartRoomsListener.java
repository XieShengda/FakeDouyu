package com.sender.fakedouyu.listener;

import com.sender.fakedouyu.bean.RoomInfo;

/**
 * 请求收藏房间观察者
 */
public interface RequestHeartRoomsListener {
    void onSuccess(RoomInfo roomInfo);

    void onError();
}
