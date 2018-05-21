package com.newsmvpdemo.inject.modules;

import com.newsmvpdemo.inject.PerActivity;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.presenter.NewsArticlePresenter;
import com.newsmvpdemo.module.view.INewsArticleView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yb on 2018/3/11.
 */

@Module
public class NewsArticleModule {
    private final INewsArticleView mView;
    private final String mNewsId;

    public NewsArticleModule(INewsArticleView mView,String mNewsId) {
        this.mView = mView;
        this.mNewsId = mNewsId;
    }

    @PerActivity
    @Provides
    public IBasePresenter providePresenter() {
        return new NewsArticlePresenter(mView,mNewsId);
    }
}
