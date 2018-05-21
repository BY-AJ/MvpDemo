package com.newsmvpdemo.inject.modules;

import com.newsmvpdemo.adapter.ChannelAdapter;
import com.newsmvpdemo.adapter.NormalChannelAdapter;
import com.newsmvpdemo.inject.PerActivity;
import com.newsmvpdemo.local.table.DaoSession;
import com.newsmvpdemo.module.home.ChannelActivity;
import com.newsmvpdemo.module.presenter.ChannelPresenter;
import com.newsmvpdemo.module.presenter.IChannelPresenter;
import com.newsmvpdemo.utils.RxBusHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yb on 2018/3/8.
 */
@Module
public class ChannelModule {
    private final ChannelActivity mView;

    public ChannelModule(ChannelActivity view) {
        this.mView = view;
    }

    @PerActivity
    @Provides
    public ChannelAdapter provideChannelAdapter() {
        return new ChannelAdapter(null);
    }

    @PerActivity
    @Provides
    public NormalChannelAdapter provideNormalChannelAdapter() {
        return new NormalChannelAdapter(null);
    }

    @PerActivity
    @Provides
    public IChannelPresenter provideChannelPresenter(DaoSession daoSession, RxBusHelper rxBus) {
        return new ChannelPresenter(mView,daoSession,rxBus);
    }
}
