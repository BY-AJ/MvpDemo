package com.newsmvpdemo.module.view;

import com.newsmvpdemo.module.base.IBaseView;

/**
 * Created by yb on 2018/3/16.
 *
 */

public interface IVideoListView<T> extends IBaseView{
    void loadData(T data);
}
