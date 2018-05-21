package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.inject.modules.VideoListModule;
import com.newsmvpdemo.module.fragment.VideoListFragment;

import dagger.Component;

/**
 * Created by ictcxq on 2018/3/16.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class,modules = VideoListModule.class)
public interface VideoListComponent {
    void inject(VideoListFragment fragment);
}
