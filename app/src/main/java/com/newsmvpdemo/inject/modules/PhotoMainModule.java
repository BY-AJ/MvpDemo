package com.newsmvpdemo.inject.modules;

import com.newsmvpdemo.adapter.ViewPagerAdapter;
import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.local.table.DaoSession;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.fragment.PhotosFragment;
import com.newsmvpdemo.module.presenter.PhotoMainPresenter;
import com.newsmvpdemo.utils.RxBusHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ictcxq on 2018/3/25.
 */
@Module
public class PhotoMainModule {

    private final PhotosFragment mView;

    public PhotoMainModule(PhotosFragment view) {
        mView = view;
    }

    @PerFragment
    @Provides
    public IRxBusPresenter providePhotoMainPresenter(DaoSession daoSession, RxBusHelper rxBus) {
        return new PhotoMainPresenter(mView,rxBus,daoSession.getWelfareInfoDao());
    }

    @PerFragment
    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getChildFragmentManager());
    }

}
