package com.newsmvpdemo.inject.modules;

import com.newsmvpdemo.adapter.VideoCacheAdapter;
import com.newsmvpdemo.adapter.BaseVideoDLAdapter;
import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.local.table.DaoSession;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.fragment.VideoCacheFragment;
import com.newsmvpdemo.module.presenter.VideoCachePresenter;
import com.newsmvpdemo.utils.RxBusHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ictcxq on 2018/4/2.
 */
@Module
public class VideoCacheModule {
    private final VideoCacheFragment mView;

    public VideoCacheModule(VideoCacheFragment view) {
        mView = view;
    }

    @PerFragment
    @Provides
    public IRxBusPresenter provideVideoCachePresenter(DaoSession daoSession, RxBusHelper helper) {
        return new VideoCachePresenter(mView,daoSession.getVideoInfoDao(),helper);
    }

    @PerFragment
    @Provides
    public BaseVideoDLAdapter provideCacheAdapter(RxBusHelper helper) {
        return new VideoCacheAdapter(null,helper);
    }
}
