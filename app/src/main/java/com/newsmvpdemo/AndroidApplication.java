package com.newsmvpdemo;

import android.app.Application;
import android.content.Context;

import com.dl7.downloaderlib.DownloadConfig;
import com.dl7.downloaderlib.FileDownloader;
import com.newsmvpdemo.api.RetrofitHelper;
import com.newsmvpdemo.engine.DownloaderWrapper;
import com.newsmvpdemo.inject.components.ApplicationComponent;
import com.newsmvpdemo.inject.components.DaggerApplicationComponent;
import com.newsmvpdemo.inject.modules.ApplicationModule;
import com.newsmvpdemo.local.dao.NewsTypeDao;
import com.newsmvpdemo.local.table.DaoMaster;
import com.newsmvpdemo.local.table.DaoSession;
import com.newsmvpdemo.utils.CrashHandler;
import com.newsmvpdemo.utils.DownloadUtils;
import com.newsmvpdemo.utils.PreferencesUtils;
import com.newsmvpdemo.utils.RxBusHelper;
import com.newsmvpdemo.utils.ToastUtils;

import org.greenrobot.greendao.database.Database;

import java.io.File;

/**
 * Created by yb on 2018/3/2.
 * 自定义应用类
 */

public class AndroidApplication extends Application{

    private static Context sContext;
    private static ApplicationComponent sAppComponent;
    private static final String DB_NAME = "news-db";
    private static DaoSession mDaoSession;
    private static RxBusHelper mRxBus = new RxBusHelper();

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        initDataBase();
        initInjector();
        initConfig();

    }

    /**
     * 初始化数据库配置
     * DevOpenHelper：创建SQLite数据库的SQLiteOpenHelper的具体实现
     * DaoMaster：GreenDao的顶级对象，作为数据库对象、用于创建表和删除表
     * DaoSession：管理所有的Dao对象，Dao对象中存在着增删改查等API
     */
    private void initDataBase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), DB_NAME);
        Database database = helper.getWritableDb();
        mDaoSession = new DaoMaster(database).newSession();
        NewsTypeDao.updateLocalData(getContext(),mDaoSession);
        DownloadUtils.init(mDaoSession.getWelfareInfoDao());
    }

    /**
     * 初始化注入依赖
     */
    private void initInjector() {
        sAppComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this,mDaoSession,mRxBus))
                .build();
    }

    /**
     * 初始化全局配置
     */
    private void initConfig() {
        CrashHandler.getInstance().init(getContext());
        RetrofitHelper.init();
        ToastUtils.init(getContext());
        DownloaderWrapper.init(mRxBus,mDaoSession.getVideoInfoDao());
        FileDownloader.init(getContext());
        DownloadConfig config = new DownloadConfig.Builder()
                .setDownloadDir(PreferencesUtils.getSavePath(getContext())+ File.separator + "video/").build();
        FileDownloader.setConfig(config);
        //MobSDK.init(this);
    }

    public static Context getContext() {
        return sContext;
    }

    public static DaoSession getDb() {
        return mDaoSession;
    }

    public static RxBusHelper getRxBusHelper() {
        return mRxBus;
    }

    public static ApplicationComponent getAppComponent() {
        return sAppComponent;
    }
}
