package com.newsmvpdemo.module.presenter;

import com.dl7.downloaderlib.model.DownloadStatus;
import com.newsmvpdemo.local.table.VideoInfoDao;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.fragment.VideosFragment;
import com.newsmvpdemo.utils.RxBusHelper;
import com.orhanobut.logger.Logger;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by ictcxq on 2018/3/16.
 */

public class VideoMainPresenter implements IRxBusPresenter{

    private VideosFragment mView;
    private final VideoInfoDao mDbDao;
    private final RxBusHelper mRxBus;

    public VideoMainPresenter(VideosFragment mView, VideoInfoDao dao, RxBusHelper rxBus) {
        this.mView = mView;
        mDbDao = dao;
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
        mView.updateLovedCount((int) mDbDao.queryBuilder().where(VideoInfoDao.Properties.IsCollect.eq(true)).count());
        mView.updateDownloadCount((int) mDbDao.queryBuilder().where(VideoInfoDao.Properties.DownloadStatus.notIn(DownloadStatus.NORMAL,
                DownloadStatus.COMPLETE)).count());
    }

    @Override
    public void getMoreData() {
    }
}
