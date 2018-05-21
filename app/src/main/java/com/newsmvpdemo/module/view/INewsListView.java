package com.newsmvpdemo.module.view;

import com.newsmvpdemo.module.base.IBaseView;
import com.newsmvpdemo.module.bean.NewsInfo;

/**
 * Created by ictcxq on 2018/3/5.
 */

public interface INewsListView<T> extends IBaseView{

    void loadData(T data);

    void loadMoreData(T data);

    void loadErrorData();

    /**
     * 加载广告数据
     * @param newsBean 新闻
     */
    void loadAdData(NewsInfo newsBean);
}
