package com.newsmvpdemo.module.presenter;

import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.local.table.VideoInfoDao;
import com.newsmvpdemo.module.base.ILocalPresenter;
import com.newsmvpdemo.module.event.VideosEvent;
import com.newsmvpdemo.module.fragment.LoveVideoFragment;
import com.newsmvpdemo.utils.ListUtils;
import com.newsmvpdemo.utils.RxBusHelper;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by yb on 2018/3/24.
 */

public class LoveVideoPresenter implements ILocalPresenter<VideoInfo>{

    private LoveVideoFragment mView;
    private VideoInfoDao mDbDao;
    private RxBusHelper rxBus;


    public LoveVideoPresenter(LoveVideoFragment view, VideoInfoDao dao, RxBusHelper rxBus) {
        this.mView = view;
        this.mDbDao = dao;
        this.rxBus = rxBus;
    }

    @Override
    public void getData(boolean isRefresh) {
        mDbDao.queryBuilder().where(VideoInfoDao.Properties.IsCollect.eq(true))
                .rx()
                .list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<VideoInfo>>() {
                    @Override
                    public void call(List<VideoInfo> videoInfos) {
                        if(!ListUtils.isEmpty(videoInfos)) {
                            mView.loadData(videoInfos);
                        }else {
                            mView.loadNoData();
                        }
                    }
                });
    }

    @Override
    public void getMoreData() {
    }

    @Override
    public void delete(VideoInfo data) {
        data.setIsCollect(false);
        if(!data.getIsCollect()) {
            mDbDao.delete(data);
        }
        // 如果收藏为0则显示无收藏
        if(mDbDao.queryBuilder().where(VideoInfoDao.Properties.IsCollect.eq(true)).count() == 0) {
            mView.loadNoData();
        }
        rxBus.post(new VideosEvent());
    }

    @Override
    public void insert(VideoInfo data) {
    }

    @Override
    public void update(List<VideoInfo> list) {
    }
}
