package com.newsmvpdemo.module.view;

import com.newsmvpdemo.local.table.NewsTypeInfo;

import java.util.List;

/**
 * Created by ictcxq on 2018/3/8.
 * 栏目管理接口
 */

public interface IChannelView {
    /**
     * 显示数据
     * @param checkList     选中栏目
     * @param uncheckList   未选中栏目
     */
    void loadData(List<NewsTypeInfo> checkList, List<NewsTypeInfo> uncheckList);
}
