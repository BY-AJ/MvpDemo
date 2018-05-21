package com.newsmvpdemo.module.base;

/**
 * Created by ictcxq on 2018/3/27.
 */

public interface IBaseLoadView<T> extends IBaseView{
    void loadData(T data);

    void loadMoreData(T data);

    void loadErrorData();
}
