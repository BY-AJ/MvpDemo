package com.newsmvpdemo.inject.components;

import android.content.Context;

import com.newsmvpdemo.inject.modules.ApplicationModule;
import com.newsmvpdemo.local.table.DaoSession;
import com.newsmvpdemo.utils.RxBusHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yb on 2018/3/2.
 */
//保证唯一性
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Context getContext();

    DaoSession getDaoSession();

    RxBusHelper getRxBus();
}
