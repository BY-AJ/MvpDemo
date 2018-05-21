package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.inject.modules.NewsListModule;
import com.newsmvpdemo.module.fragment.NewsListFragment;

import dagger.Component;

/**
 * Created by yb on 2018/3/6.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class,modules = NewsListModule.class)
public interface NewsListComponent {
    void inject(NewsListFragment fragment);
}
