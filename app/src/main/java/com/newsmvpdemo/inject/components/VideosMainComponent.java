package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.inject.modules.VideosMainModule;
import com.newsmvpdemo.module.fragment.VideosFragment;

import dagger.Component;

/**
 * Created by yb on 2018/3/16.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class,modules = VideosMainModule.class)
public interface VideosMainComponent {
    void inject(VideosFragment fragment);
}
