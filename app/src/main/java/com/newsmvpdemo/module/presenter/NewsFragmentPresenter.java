package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.local.table.NewsTypeInfo;
import com.newsmvpdemo.local.table.NewsTypeInfoDao;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.view.INewsFragmentView;
import com.newsmvpdemo.utils.RxBusHelper;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by yb on 2018/3/4.
 */

public class NewsFragmentPresenter implements IRxBusPresenter{

    private final INewsFragmentView mView;
    private final NewsTypeInfoDao mDbDao;
    private final RxBusHelper mRxBus;

    public NewsFragmentPresenter(INewsFragmentView view, NewsTypeInfoDao dbDao , RxBusHelper rxBus) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
    }

    @Override
    public <T> void registerRxBus(Class<T> eventType, Action1<T> action) {
        Subscription subscription = mRxBus.doSubscribe(eventType, action, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.d(throwable.getMessage());
            }
        });
        mRxBus.addSubscription(this,subscription);
    }

    @Override
    public void unregisterRxBus() {
        mRxBus.unSubscribe(this);
    }

    @Override
    public void getData(boolean isRefresh) {
        mDbDao.queryBuilder().rx().list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<NewsTypeInfo>>() {
                    @Override
                    public void call(List<NewsTypeInfo> newsTypeInfos) {
                        mView.loadData(newsTypeInfos);
                    }
                });
    }

    @Override
    public void getMoreData() {
    }
}
