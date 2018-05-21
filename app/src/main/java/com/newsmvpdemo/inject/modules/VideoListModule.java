package com.newsmvpdemo.inject.modules;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newsmvpdemo.adapter.VideoListAdapter;
import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.presenter.VideoListPresenter;
import com.newsmvpdemo.module.view.IVideoListView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yb on 2018/3/16.
 */
@Module
public class VideoListModule {

    private final IVideoListView mView;
    private final String mVideoId;

    public VideoListModule(IVideoListView view,String videoId) {
        mView = view;
        mVideoId = videoId;
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new VideoListAdapter(null);
    }

    @PerFragment
    @Provides
    public IBasePresenter provideVideoListPresenter() {
        return new VideoListPresenter(mView,mVideoId);
    }

}
