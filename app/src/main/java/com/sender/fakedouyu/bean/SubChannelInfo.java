package com.sender.fakedouyu.bean;

/**
 * 网络请求的频道信息bean
 */
public class SubChannelInfo {
    private int tagId;
    private String tagName;
    private String iconUrl;

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
