package com.sender.fakedouyu.listener;

/**
 *请求房间数据观察者
 */
public interface RequestStreamUrlListener {
    void onSuccess(int roomId, String url);

    void onError();
}
