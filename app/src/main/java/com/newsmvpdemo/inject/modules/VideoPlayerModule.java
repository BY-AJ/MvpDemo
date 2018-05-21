package com.newsmvpdemo.inject.modules;

import com.newsmvpdemo.inject.PerActivity;
import com.newsmvpdemo.local.table.DaoSession;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.presenter.IVideoPlayerPresenter;
import com.newsmvpdemo.module.presenter.VideoPlayerPresenter;
import com.newsmvpdemo.module.view.IVideoPlayerView;
import com.newsmvpdemo.utils.RxBusHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yb on 2018/3/17.
 */
@Module
public class VideoPlayerModule {

    private final IVideoPlayerView mView;
    private final VideoInfo mData;

    public  VideoPlayerModule(IVideoPlayerView view,VideoInfo data) {
        mView = view;
        mData = data;
    }

    @PerActivity
    @Provides
    public IVideoPlayerPresenter provideVideoPlayerPresenter(DaoSession daoSession, RxBusHelper rxBus) {
        return new VideoPlayerPresenter(mView,mData,daoSession.getVideoInfoDao(),rxBus,daoSession.getDanmakuInfoDao());
    }
}
