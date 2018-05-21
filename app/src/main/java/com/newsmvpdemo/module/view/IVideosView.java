package com.newsmvpdemo.module.view;

import com.newsmvpdemo.module.base.IBaseView;

/**
 * Created by yb on 2018/3/16.
 * video 主界面接口
 */

public interface IVideosView extends IBaseView{
    void updateLovedCount(int lovedCount);//喜爱的视频

    void updateDownloadCount(int downloadCount);//下载的视频
}
