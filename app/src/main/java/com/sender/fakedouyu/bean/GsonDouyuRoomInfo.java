package com.sender.fakedouyu.bean;

import java.util.List;

/**
 * gson数据错误码，和包含所有房间信息的gsonArray
 */
public class GsonDouyuRoomInfo {
    private int error;

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public static class Data {
        private List<GsonSubChannel.Room> room;

        public List<GsonSubChannel.Room> getRoom() {
            return room;
        }
    }
}
