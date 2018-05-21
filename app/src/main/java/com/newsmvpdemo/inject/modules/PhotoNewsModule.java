package com.newsmvpdemo.inject.modules;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newsmvpdemo.adapter.PhotoNewsAdapter;
import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.fragment.PhotoNewsFragment;
import com.newsmvpdemo.module.presenter.PhotoNewsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ictcxq on 2018/3/27.
 */
@Module
public class PhotoNewsModule {

    private final PhotoNewsFragment mView;

    public PhotoNewsModule(PhotoNewsFragment view) {
        mView = view;
    }

    @PerFragment
    @Provides
    public IBasePresenter provideIBasePresenter() {
        return new PhotoNewsPresenter(mView);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new PhotoNewsAdapter(null);
    }
}
