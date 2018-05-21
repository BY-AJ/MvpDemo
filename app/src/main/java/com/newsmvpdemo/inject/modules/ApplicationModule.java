package com.newsmvpdemo.inject.modules;

import android.content.Context;

import com.newsmvpdemo.AndroidApplication;
import com.newsmvpdemo.local.table.DaoSession;
import com.newsmvpdemo.utils.RxBusHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yb on 2018/3/2.
 */
// module的作用是提供在应用的生命周期中存活的对象
@Module
public class ApplicationModule {
    private final AndroidApplication mApplication;
    private final DaoSession mDaoSession;
    private final RxBusHelper mRxBus;

    public ApplicationModule(AndroidApplication application,DaoSession daoSession,RxBusHelper rxBus) {
        mApplication = application;
        mDaoSession = daoSession;
        mRxBus = rxBus;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession() {
        return mDaoSession;
    }

    @Provides
    @Singleton
    RxBusHelper provideRxBusHelper() {
        return mRxBus;
    }

}
