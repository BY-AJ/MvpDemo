package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.local.table.DanmakuInfo;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.base.ILocalPresenter;

/**
 * Created by yb on 2018/3/17.
 */

public interface IVideoPlayerPresenter extends ILocalPresenter<VideoInfo>{

    /**
     * 添加一条弹幕到数据库
     * @param danmakuInfo
     */
    void addDanmaku(DanmakuInfo danmakuInfo);

    /**
     * 清空该视频所有缓存弹幕
     */
    void cleanDanmaku();
}
