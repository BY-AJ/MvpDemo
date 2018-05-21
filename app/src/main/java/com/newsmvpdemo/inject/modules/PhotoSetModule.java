package com.newsmvpdemo.inject.modules;

import com.newsmvpdemo.inject.PerActivity;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.presenter.PhotoSetPresenter;
import com.newsmvpdemo.module.view.IPhotoSetView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ictcxq on 2018/3/13.
 */
@Module
public class PhotoSetModule {

    private final IPhotoSetView mView;
    private final String mPhotoId;

    public  PhotoSetModule(IPhotoSetView view,String id) {
        this.mView = view;
        this.mPhotoId = id;
    }

    @PerActivity
    @Provides
    public IBasePresenter providePhotoSetPresenter() {
        return new PhotoSetPresenter(mView,mPhotoId);
    }


}
