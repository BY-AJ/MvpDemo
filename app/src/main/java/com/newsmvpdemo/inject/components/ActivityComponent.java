package com.newsmvpdemo.inject.components;

import android.app.Activity;

import com.newsmvpdemo.inject.PerActivity;
import com.newsmvpdemo.inject.modules.ActivityModule;

import dagger.Component;

/**
 * Created by yb on 2018/3/2.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class , modules = ActivityModule.class)
public interface ActivityComponent {
    Activity getActivity();
}
