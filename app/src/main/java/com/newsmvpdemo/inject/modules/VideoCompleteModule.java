package com.newsmvpdemo.inject.modules;


import com.newsmvpdemo.adapter.BaseVideoDLAdapter;
import com.newsmvpdemo.adapter.VideoCompleteAdapter;
import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.local.table.DaoSession;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.fragment.VideoCompleteFragment;
import com.newsmvpdemo.module.presenter.VideoCompletePresenter;
import com.newsmvpdemo.utils.RxBusHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by long on 2016/12/16.
 * video 缓存完成 Module
 */
@Module
public class VideoCompleteModule {

    private final VideoCompleteFragment mView;

    public VideoCompleteModule(VideoCompleteFragment view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public IRxBusPresenter providePresenter(DaoSession daoSession, RxBusHelper rxBus) {
        return new VideoCompletePresenter(mView, daoSession.getVideoInfoDao(), rxBus);
    }

    @PerFragment
    @Provides
    public BaseVideoDLAdapter provideAdapter(RxBusHelper rxBus) {
        return new VideoCompleteAdapter(null,rxBus);
    }
}
