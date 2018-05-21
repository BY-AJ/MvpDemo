package com.newsmvpdemo.inject.modules;

import com.newsmvpdemo.adapter.ViewPagerAdapter;
import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.local.table.DaoSession;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.fragment.NewsFragment;
import com.newsmvpdemo.module.presenter.NewsFragmentPresenter;
import com.newsmvpdemo.utils.RxBusHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yb on 2018/3/3.
 */
@Module
public class NewsFragmentModule {
    private final NewsFragment mView;

    public NewsFragmentModule(NewsFragment view) {
        mView = view;
    }

    @Provides
    @PerFragment
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getChildFragmentManager());
    }

    @Provides
    @PerFragment
    public IRxBusPresenter provideNewsFragmentPresenter(DaoSession daoSession, RxBusHelper rxBus) {
        return new NewsFragmentPresenter(mView,daoSession.getNewsTypeInfoDao(),rxBus);
    }
}
