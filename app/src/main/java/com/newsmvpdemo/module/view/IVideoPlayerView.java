package com.newsmvpdemo.module.view;

import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.base.IBaseView;

import java.io.InputStream;

/**
 * Created by yb on 2018/3/17.
 * Video接口
 */

public interface IVideoPlayerView extends IBaseView{
    /**
     * 获取Video数据
     * @param data 数据
     */
    void loadData(VideoInfo data);

    /**
     * 获取弹幕数据
     * @param inputStream 数据
     */
    void loadDanmakuData(InputStream inputStream);
}
