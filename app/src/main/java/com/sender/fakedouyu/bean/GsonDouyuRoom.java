package com.sender.fakedouyu.bean;

/**
 * 错误码和直播流链接
 */

public class GsonDouyuRoom {
    private int error;
    private Data data;//直播流链接

    public int getError() {
        return error;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private int room_id;
        private String live_url;

        public int getRoom_id() {
            return room_id;
        }

        public String getLive_url() {
            return live_url;
        }

        @Override
        public String toString() {
            return "GsonDouyuRoom [room_id=" + room_id + ", live_url" + live_url + "]";
        }
    }
}
