package com.newsmvpdemo.module.view;

import com.newsmvpdemo.adapter.entity.SpecialItem;
import com.newsmvpdemo.module.base.IBaseView;
import com.newsmvpdemo.module.bean.SpecialInfo;

import java.util.List;

/**
 * Created by yb on 2018/3/12.
 */

public interface ISpecialView extends IBaseView{

    /**
     * 显示数据
     * @param specialItems 新闻
     */
    void loadData(List<SpecialItem> specialItems);

    /**
     * 添加头部
     * @param specialBean
     */
    void loadBanner(SpecialInfo specialBean);
}
