package com.newsmvpdemo.inject.modules;

import com.newsmvpdemo.adapter.ViewPagerAdapter;
import com.newsmvpdemo.inject.PerActivity;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.home.DownloadActivity;
import com.newsmvpdemo.module.presenter.DownloadPresenter;
import com.newsmvpdemo.utils.RxBusHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ictcxq on 2018/4/2.
 */
@Module
public class DownloadModule {
    private final DownloadActivity mView;

    public DownloadModule(DownloadActivity view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public IRxBusPresenter provideDownloadPresenter(RxBusHelper rxBusHelper) {
        return new DownloadPresenter(rxBusHelper);
    }

    @PerActivity
    @Provides
    public ViewPagerAdapter provideDownloadAdapter() {
        return new ViewPagerAdapter(mView.getSupportFragmentManager());
    }
}
