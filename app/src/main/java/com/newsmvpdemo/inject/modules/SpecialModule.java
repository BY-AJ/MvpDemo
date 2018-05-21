package com.newsmvpdemo.inject.modules;

import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.SpecialAdapter;
import com.newsmvpdemo.inject.PerActivity;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.presenter.SpecialPresenter;
import com.newsmvpdemo.module.view.ISpecialView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ictcxq on 2018/3/12.
 */
@Module
public class SpecialModule {
    private final ISpecialView mView;
    private final String mSpecialId;

    public SpecialModule(ISpecialView mView,String mSpecialId) {
        this.mView = mView;
        this.mSpecialId = mSpecialId;
    }

    @Provides
    @PerActivity
    public IBasePresenter provideSpecialPresenter() {
        return new SpecialPresenter(mView,mSpecialId);
    }

    @Provides
    @PerActivity
    public SpecialAdapter provideSpecialAdapter() {
        return new SpecialAdapter(R.layout.adapter_news_list,R.layout.adapter_special_head,null);
    }
}
