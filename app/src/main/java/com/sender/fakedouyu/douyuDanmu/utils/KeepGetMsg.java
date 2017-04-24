package com.sender.fakedouyu.douyuDanmu.utils;

import com.sender.fakedouyu.douyuDanmu.client.DyBulletScreenClient;

/**
 * Created by XieShengda on 2017/04/11.
 */
public class KeepGetMsg extends Thread {
    @Override
    public void run()
    {
        ////获取弹幕客户端
        DyBulletScreenClient danmuClient = DyBulletScreenClient.getInstance();

        //判断客户端就绪状态
        while(danmuClient.getReadyFlag())
        {
            //获取服务器发送的弹幕信息
            danmuClient.getServerMsg();
        }
    }
}
