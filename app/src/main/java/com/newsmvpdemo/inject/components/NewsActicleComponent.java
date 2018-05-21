package com.newsmvpdemo.inject.components;

import com.newsmvpdemo.inject.PerActivity;
import com.newsmvpdemo.inject.modules.NewsArticleModule;
import com.newsmvpdemo.module.home.NewsArticleActivity;

import dagger.Component;

/**
 * Created by ictcxq on 2018/3/11.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = NewsArticleModule.class)
public interface NewsActicleComponent {
    void inject(NewsArticleActivity activity);
}
