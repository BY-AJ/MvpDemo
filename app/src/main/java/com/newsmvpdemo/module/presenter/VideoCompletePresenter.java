package com.newsmvpdemo.module.presenter;

import com.dl7.downloaderlib.model.DownloadStatus;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.local.table.VideoInfoDao;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.fragment.VideoCompleteFragment;
import com.newsmvpdemo.utils.ListUtils;
import com.newsmvpdemo.utils.RxBusHelper;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ictcxq on 2018/4/2.
 */

public class VideoCompletePresenter implements IRxBusPresenter{

    private VideoCompleteFragment mView;
    private VideoInfoDao mDbDao;
    private RxBusHelper mRxBus;


    public VideoCompletePresenter(VideoCompleteFragment view, VideoInfoDao dao, RxBusHelper helper) {
        mView = view;
        mDbDao = dao;
        mRxBus = helper;
    }

    @Override
    public <T> void registerRxBus(Class<T> eventType, Action1<T> action) {
        Subscription subscription = mRxBus.doSubscribe(eventType, action, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.e(throwable.toString());
            }
        });
        mRxBus.addSubscription(this, subscription);
    }

    @Override
    public void unregisterRxBus() {
        mRxBus.unSubscribe(this);
    }

    @Override
    public void getData(boolean isRefresh) {
        mDbDao.queryBuilder()
                .where(VideoInfoDao.Properties.DownloadStatus.eq(DownloadStatus.COMPLETE))
                .rx()
                .list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<VideoInfo>>() {
                    @Override
                    public void call(List<VideoInfo> videoList) {
                        if (ListUtils.isEmpty(videoList)) {
                            mView.loadErrorData();
                        } else {
                            mView.loadData(videoList);
                        }
                    }
                });
    }

    @Override
    public void getMoreData() {

    }
}
