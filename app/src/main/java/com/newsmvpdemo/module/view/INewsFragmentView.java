package com.newsmvpdemo.module.view;

import com.newsmvpdemo.local.table.NewsTypeInfo;
import com.newsmvpdemo.module.base.IBaseView;

import java.util.List;

/**
 * Created by ictcxq on 2018/3/4.
 */

public interface INewsFragmentView extends IBaseView{
    /**
     * 显示数据
     * @param checkList     选中栏目
     */
    void loadData(List<NewsTypeInfo> checkList);

}
