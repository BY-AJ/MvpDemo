package com.newsmvpdemo.module.view;

import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.base.IBaseView;

import java.util.List;

/**
 * Created by yb on 2018/3/24.
 */

public interface ILoveVideoView extends IBaseView{

    void loadData(List<VideoInfo> data);

    void loadNoData();
}
