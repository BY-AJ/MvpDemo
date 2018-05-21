package com.newsmvpdemo.module.presenter;

import com.dl7.downloaderlib.model.DownloadStatus;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.local.table.VideoInfoDao;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.fragment.VideoCacheFragment;
import com.newsmvpdemo.utils.ListUtils;
import com.newsmvpdemo.utils.RxBusHelper;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by ictcxq on 2018/4/2.
 */

public class VideoCachePresenter implements IRxBusPresenter{

    private VideoCacheFragment mView;
    private VideoInfoDao mDbDao;
    private RxBusHelper mHelper;

    public VideoCachePresenter(VideoCacheFragment view, VideoInfoDao dao, RxBusHelper helper) {
        mView = view;
        mDbDao = dao;
        mHelper = helper;
    }

    @Override
    public <T> void registerRxBus(Class<T> eventType, Action1<T> action) {
        Subscription subscription = mHelper.doSubscribe(eventType, action, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.d(throwable.getMessage());
            }
        });
        mHelper.addSubscription(this,subscription);
    }

    @Override
    public void unregisterRxBus() {
        mHelper.unSubscribe(this);
    }

    @Override
    public void getData(boolean isRefresh) {
        mDbDao.queryBuilder().rx()
                .oneByOne()
                .filter(new Func1<VideoInfo, Boolean>() {
                    @Override
                    public Boolean call(VideoInfo info) {
                        return (info.getDownloadStatus() != DownloadStatus.NORMAL &&
                                info.getDownloadStatus() != DownloadStatus.COMPLETE);
                    }
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<VideoInfo>>() {
                    @Override
                    public void call(List<VideoInfo> videoInfos) {
                        if(ListUtils.isEmpty(videoInfos)) {
                            mView.loadErrorData();
                        }else {
                            mView.loadData(videoInfos);
                        }
                    }
                });
    }

    @Override
    public void getMoreData() {

    }
}
