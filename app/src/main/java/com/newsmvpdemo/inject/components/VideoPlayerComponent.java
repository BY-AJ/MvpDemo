package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerActivity;
import com.newsmvpdemo.inject.modules.VideoPlayerModule;
import com.newsmvpdemo.module.home.VideoPlayerActivity;

import dagger.Component;

/**
 * Created by ictcxq on 2018/3/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = VideoPlayerModule.class)
public interface VideoPlayerComponent {
    void inject(VideoPlayerActivity activity);
}
