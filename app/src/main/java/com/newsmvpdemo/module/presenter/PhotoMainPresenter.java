package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.local.table.WelfareInfoDao;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.fragment.PhotosFragment;
import com.newsmvpdemo.utils.RxBusHelper;
import com.orhanobut.logger.Logger;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by yb on 2018/3/25.
 */

public class PhotoMainPresenter implements IRxBusPresenter{

    private PhotosFragment mView;
    private RxBusHelper rxBus;
    private WelfareInfoDao mDbDao;

    public PhotoMainPresenter(PhotosFragment view, RxBusHelper rxBus, WelfareInfoDao dao) {
        this.mView = view;
        this.rxBus = rxBus;
        this.mDbDao =dao;
    }

    @Override
    public <T> void registerRxBus(Class<T> eventType, Action1<T> action) {
        Subscription subscription = rxBus.doSubscribe(eventType, action, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.d(throwable.getMessage());
            }
        });
        rxBus.addSubscription(this,subscription);
    }

    @Override
    public void unregisterRxBus() {
        rxBus.unSubscribe(this);
    }

    @Override
    public void getData(boolean isRefresh) {
        mView.updateLovedCount((int) mDbDao.queryBuilder().where(WelfareInfoDao.Properties.IsLove.eq(true)).count());
    }

    @Override
    public void getMoreData() {
    }
}
