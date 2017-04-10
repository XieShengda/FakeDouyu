package com.sender.fakedouyu.listener;

/**
 * 网络请求主题
 */
public interface NetworkRequest {
    void getSubChannel(String url, RequestSubChannelListener listener);

    void getStreamUrl(int roomId, RequestStreamUrlListener listener);

    void getAllSubChannels(RequestAllSubChannelsListener listener);

    void getHeartRooms(RequestHeartRoomsListener listener);
}
