package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerActivity;
import com.newsmvpdemo.inject.modules.ChannelModule;
import com.newsmvpdemo.module.home.ChannelActivity;

import dagger.Component;

/**
 * Created by yb on 2018/3/8.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = ChannelModule.class)
public interface ChannelComponent {
    void inject(ChannelActivity activity);
}
