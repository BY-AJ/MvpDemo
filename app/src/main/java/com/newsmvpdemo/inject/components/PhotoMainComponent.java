package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerFragment;
import com.newsmvpdemo.inject.modules.PhotoMainModule;
import com.newsmvpdemo.module.fragment.PhotosFragment;

import dagger.Component;

/**
 * Created by ictcxq on 2018/3/25.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class,modules = PhotoMainModule.class)
public interface PhotoMainComponent {
    void inject(PhotosFragment fragment);
}
