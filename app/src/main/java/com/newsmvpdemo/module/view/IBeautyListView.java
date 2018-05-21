package com.newsmvpdemo.module.view;

import com.newsmvpdemo.module.base.IBaseView;

/**
 * Created by yb on 2018/3/27.
 */

public interface IBeautyListView<T> extends IBaseView{

    void loadData(T data);

    void loadMoreData(T data);

    void loadNoData();
}
