package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.inject.modules.PhotoNewsModule;
import com.newsmvpdemo.module.fragment.PhotoNewsFragment;

import dagger.Component;

/**
 * Created by ictcxq on 2018/3/27.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class,modules = PhotoNewsModule.class)
public interface PhotoNewsComponent {
    void inject(PhotoNewsFragment fragment);
}
