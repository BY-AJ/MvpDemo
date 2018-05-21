package com.newsmvpdemo.module.view;

import com.newsmvpdemo.module.base.IBaseView;
import com.newsmvpdemo.module.bean.NewsDetailInfo;

/**
 * Created by yb on 2018/3/11.
 *  新闻详情接口
 */

public interface INewsArticleView extends IBaseView{
    /**
     * 显示数据
     * @param newsDetailBean 新闻详情
     */
    void loadData(NewsDetailInfo newsDetailBean);
}
