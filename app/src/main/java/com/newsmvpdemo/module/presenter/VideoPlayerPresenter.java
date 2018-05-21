package com.newsmvpdemo.module.presenter;

import com.dl7.downloaderlib.model.DownloadStatus;
import com.newsmvpdemo.local.table.DanmakuInfo;
import com.newsmvpdemo.local.table.DanmakuInfoDao;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.local.table.VideoInfoDao;
import com.newsmvpdemo.module.event.VideosEvent;
import com.newsmvpdemo.module.view.IVideoPlayerView;
import com.newsmvpdemo.utils.GsonHelper;
import com.newsmvpdemo.utils.RxBusHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by yb on 2018/3/17.
 */

public class VideoPlayerPresenter implements IVideoPlayerPresenter{

    private final IVideoPlayerView mView;
    private final VideoInfoDao mDbDao;
    private final RxBusHelper mRxBus;
    private final VideoInfo mVideoData;
    private final DanmakuInfoDao mDanmakuDao;

    // 是否数据库有记录
    private boolean mIsContains = false;

    public VideoPlayerPresenter(IVideoPlayerView view, VideoInfo data, VideoInfoDao videoInfoDao,
                                RxBusHelper rxBus, DanmakuInfoDao danmakuInfoDao) {
        mView = view;
        mVideoData = data;
        mDbDao = videoInfoDao;
        mRxBus = rxBus;
        mDanmakuDao = danmakuInfoDao;
        mIsContains = mDbDao.queryBuilder().list().contains(data);
    }

    @Override
    public void getData(boolean isRefresh) {
        mDbDao.queryBuilder().rx()
                .oneByOne()//一次只发送一个时间事件
                .filter(new Func1<VideoInfo, Boolean>() {
                    @Override
                    public Boolean call(VideoInfo videoInfo) {
                        return mVideoData.equals(videoInfo);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.<VideoInfo>bindToLife())
                .subscribe(new Action1<VideoInfo>() {
                    @Override
                    public void call(VideoInfo videoInfo) {
                        mIsContains = true;
                        mView.loadData(videoInfo);
                    }
                });
        mDanmakuDao.queryBuilder().where(DanmakuInfoDao.Properties.Vid.eq(mVideoData.getVid()))
                .rx()
                .list()
                .map(new Func1<List<DanmakuInfo>, InputStream>() {
                    @Override
                    public InputStream call(List<DanmakuInfo> danmakuInfos) {
                        String jsonStr = GsonHelper.object2JsonStr(danmakuInfos);
                        // 将 String 转为 InputStream
                        InputStream inputStream = new ByteArrayInputStream(jsonStr.getBytes());
                        return inputStream;
                    }
                })
                .compose(mView.<InputStream>bindToLife())
                .subscribe(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        mView.loadDanmakuData(inputStream);
                    }
                });
    }

    @Override
    public void getMoreData() {
    }

    @Override
    public void addDanmaku(DanmakuInfo danmakuInfo) {
        mDanmakuDao.insert(danmakuInfo);//插入一条弹幕到数据库中
    }

    @Override
    public void cleanDanmaku() {

    }

    @Override
    public void insert(VideoInfo data) {
        if(mIsContains) {
            mDbDao.update(data);
        }else {
            mDbDao.insert(data);
        }
        mRxBus.post(new VideosEvent());
    }

    @Override
    public void delete(VideoInfo data) {
        if(!data.getIsCollect() && data.getDownloadStatus() == DownloadStatus.NORMAL) {
            mDbDao.delete(data);
            mIsContains = false;
        }else {
            mDbDao.update(data);
        }
        mRxBus.post(new VideosEvent());
    }

    @Override
    public void update(List<VideoInfo> list) {
    }

}
