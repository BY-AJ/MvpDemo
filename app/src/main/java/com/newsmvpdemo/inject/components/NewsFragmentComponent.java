package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.inject.modules.NewsFragmentModule;
import com.newsmvpdemo.module.fragment.NewsFragment;

import dagger.Component;

/**
 * Created by yb on 2018/3/3.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class,modules = NewsFragmentModule.class)
public interface NewsFragmentComponent {
    void inject(NewsFragment fragment);
}
