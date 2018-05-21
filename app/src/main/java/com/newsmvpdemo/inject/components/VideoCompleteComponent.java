package com.newsmvpdemo.inject.components;


import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.inject.modules.VideoCompleteModule;
import com.newsmvpdemo.module.fragment.VideoCompleteFragment;

import dagger.Component;

/**
 * Created by long on 2016/12/16.
 * video 缓存完成 Component
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = VideoCompleteModule.class)
public interface VideoCompleteComponent {
    void inject(VideoCompleteFragment fragment);
}
