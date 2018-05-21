package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerActivity;
import com.newsmvpdemo.inject.modules.PhotoSetModule;
import com.newsmvpdemo.module.home.PhotoSetActivity;

import dagger.Component;

/**
 * Created by ictcxq on 2018/3/13.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = PhotoSetModule.class)
public interface PhotoSetComponent {
    void inject(PhotoSetActivity activity);
}
