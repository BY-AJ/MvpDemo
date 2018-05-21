package com.newsmvpdemo.module.view;

import com.newsmvpdemo.module.base.IBaseView;
import com.newsmvpdemo.module.bean.PhotoSetInfo;

/**
 * Created by ictcxq on 2018/3/13.
 */

public interface IPhotoSetView extends IBaseView{
    /**
     * 显示数据
     */
    void loadData(PhotoSetInfo photoSetInfo);
}
