package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.view.IBeautyListView;

/**
 * Created by yb on 2018/3/27.
 * 美图
 */

public class BeautyListPresenter implements IBasePresenter{

    private IBeautyListView mView;

    public BeautyListPresenter(IBeautyListView view) {
        mView = view;
    }

    @Override
    public void getData(boolean isRefresh) {

    }

    @Override
    public void getMoreData() {

    }
}
