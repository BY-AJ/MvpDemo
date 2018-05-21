package com.newsmvpdemo.inject.modules;

import com.newsmvpdemo.adapter.ViewPagerAdapter;
import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.local.table.DaoSession;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.fragment.VideosFragment;
import com.newsmvpdemo.module.presenter.VideoMainPresenter;
import com.newsmvpdemo.utils.RxBusHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yb on 2018/3/16.
 */
@Module
public class VideosMainModule {

    private final VideosFragment mView;

    public VideosMainModule(VideosFragment view) {
        mView = view;
    }

    @PerFragment
    @Provides
    public IRxBusPresenter provideVideoMainPresenter(DaoSession daoSession, RxBusHelper rxBus) {
        return new VideoMainPresenter(mView,daoSession.getVideoInfoDao(),rxBus);
    }

    @Provides
    @PerFragment
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getChildFragmentManager());
    }
}
