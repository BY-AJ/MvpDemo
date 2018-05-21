package com.newsmvpdemo.inject.modules;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newsmvpdemo.adapter.NewsMultiListAdapter;
import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.fragment.NewsListFragment;
import com.newsmvpdemo.module.presenter.NewsListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yb on 2018/3/6.
 * 新闻列表 Module
 */
@Module
public class NewsListModule {
    private final NewsListFragment mView;
    private final String mNewsId;

    public NewsListModule(NewsListFragment view,String id) {
        mView = view;
        mNewsId = id;
    }

    @Provides
    @PerFragment
    public IBasePresenter provideNewsListPresenter() {
        return new NewsListPresenter(mView,mNewsId);
    }

    @Provides
    @PerFragment
    public BaseQuickAdapter provideAdapter() {
        return new NewsMultiListAdapter(null);
    }
}
