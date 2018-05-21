package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.inject.modules.WelfareListModule;
import com.newsmvpdemo.module.fragment.WelfareListFragment;

import dagger.Component;

/**
 * Created by ictcxq on 2018/3/27.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class,modules = WelfareListModule.class)
public interface WelfareListComponent {
    void inject(WelfareListFragment fragment);
}
