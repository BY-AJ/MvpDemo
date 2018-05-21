package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.inject.modules.LoveVideoModule;
import com.newsmvpdemo.module.fragment.LoveVideoFragment;

import dagger.Component;

/**
 * Created by ictcxq on 2018/3/24.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class,modules = LoveVideoModule.class)
public interface LoveVideoComponent {
    void inject(LoveVideoFragment fragment);
}
