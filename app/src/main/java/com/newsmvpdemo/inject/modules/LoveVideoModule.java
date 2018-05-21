package com.newsmvpdemo.inject.modules;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newsmvpdemo.adapter.VideoLoveAdapter;
import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.local.table.DaoSession;
import com.newsmvpdemo.module.base.ILocalPresenter;
import com.newsmvpdemo.module.fragment.LoveVideoFragment;
import com.newsmvpdemo.module.presenter.LoveVideoPresenter;
import com.newsmvpdemo.utils.RxBusHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ictcxq on 2018/3/24.
 */
@Module
public class LoveVideoModule {

    private final LoveVideoFragment mView;

    public LoveVideoModule(LoveVideoFragment view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public ILocalPresenter providePresenter(DaoSession daoSession, RxBusHelper rxBus) {
        return new LoveVideoPresenter(mView, daoSession.getVideoInfoDao(), rxBus);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new VideoLoveAdapter(null);
    }
}
