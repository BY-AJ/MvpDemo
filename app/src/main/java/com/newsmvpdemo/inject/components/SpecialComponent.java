package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerActivity;
import com.newsmvpdemo.inject.modules.SpecialModule;
import com.newsmvpdemo.module.home.SpecialActivity;

import dagger.Component;

/**
 * Created by ictcxq on 2018/3/12.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = SpecialModule.class)
public interface SpecialComponent {
    void inject(SpecialActivity activity);
}
