package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.utils.RxBusHelper;
import com.orhanobut.logger.Logger;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by ictcxq on 2018/4/2.
 */

public class DownloadPresenter implements IRxBusPresenter{

    private RxBusHelper mRxbus;

    public DownloadPresenter(RxBusHelper helper) {
        mRxbus = helper;
    }

    @Override
    public <T> void registerRxBus(Class<T> eventType, Action1<T> action) {
        Subscription subscription = mRxbus.doSubscribe(eventType, action, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.d(throwable.getMessage());
            }
        });
        mRxbus.addSubscription(this,subscription);
    }

    @Override
    public void unregisterRxBus() {
        mRxbus.unSubscribe(this);
    }

    @Override
    public void getData(boolean isRefresh) {
    }

    @Override
    public void getMoreData() {
    }
}
