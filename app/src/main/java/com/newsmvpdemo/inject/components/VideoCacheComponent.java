package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.inject.modules.VideoCacheModule;
import com.newsmvpdemo.module.fragment.VideoCacheFragment;

import dagger.Component;

/**
 * Created by ictcxq on 2018/4/2.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class,modules = VideoCacheModule.class)
public interface VideoCacheComponent {
    void inject(VideoCacheFragment fragment);
}
