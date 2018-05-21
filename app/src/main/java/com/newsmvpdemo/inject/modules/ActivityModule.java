package com.newsmvpdemo.inject.modules;

import android.app.Activity;

import com.newsmvpdemo.inject.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yb on 2018/3/2.
 */
@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @PerActivity
    Activity getActivity() {
        return mActivity;
    }
}
