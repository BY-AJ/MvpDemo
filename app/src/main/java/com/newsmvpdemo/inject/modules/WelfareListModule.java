package com.newsmvpdemo.inject.modules;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newsmvpdemo.adapter.WelfareListAdapter;
import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.fragment.WelfareListFragment;
import com.newsmvpdemo.module.presenter.WelfareListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ictcxq on 2018/3/27.
 */
@Module
public class WelfareListModule {

    private final WelfareListFragment mView;

    public WelfareListModule(WelfareListFragment view) {
        mView = view;
    }

    @PerFragment
    @Provides
    public IBasePresenter provideIBasePresenter() {
        return new WelfareListPresenter(mView);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new WelfareListAdapter(null,mView.getContext());
    }
}
