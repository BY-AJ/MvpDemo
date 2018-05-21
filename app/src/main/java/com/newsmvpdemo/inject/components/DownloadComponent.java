package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerActivity;
import com.newsmvpdemo.inject.modules.DownloadModule;
import com.newsmvpdemo.module.home.DownloadActivity;

import dagger.Component;

/**
 * Created by ictcxq on 2018/4/2.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = DownloadModule.class)
public interface DownloadComponent {
    void inject(DownloadActivity activity);
}
