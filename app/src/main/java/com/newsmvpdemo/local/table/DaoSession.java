package com.newsmvpdemo.local.table;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.newsmvpdemo.local.table.BeautyPhotoInfo;
import com.newsmvpdemo.local.table.DanmakuInfo;
import com.newsmvpdemo.local.table.NewsTypeInfo;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.local.table.WelfareInfo;

import com.newsmvpdemo.local.table.BeautyPhotoInfoDao;
import com.newsmvpdemo.local.table.DanmakuInfoDao;
import com.newsmvpdemo.local.table.NewsTypeInfoDao;
import com.newsmvpdemo.local.table.VideoInfoDao;
import com.newsmvpdemo.local.table.WelfareInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig beautyPhotoInfoDaoConfig;
    private final DaoConfig danmakuInfoDaoConfig;
    private final DaoConfig newsTypeInfoDaoConfig;
    private final DaoConfig videoInfoDaoConfig;
    private final DaoConfig welfareInfoDaoConfig;

    private final BeautyPhotoInfoDao beautyPhotoInfoDao;
    private final DanmakuInfoDao danmakuInfoDao;
    private final NewsTypeInfoDao newsTypeInfoDao;
    private final VideoInfoDao videoInfoDao;
    private final WelfareInfoDao welfareInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        beautyPhotoInfoDaoConfig = daoConfigMap.get(BeautyPhotoInfoDao.class).clone();
        beautyPhotoInfoDaoConfig.initIdentityScope(type);

        danmakuInfoDaoConfig = daoConfigMap.get(DanmakuInfoDao.class).clone();
        danmakuInfoDaoConfig.initIdentityScope(type);

        newsTypeInfoDaoConfig = daoConfigMap.get(NewsTypeInfoDao.class).clone();
        newsTypeInfoDaoConfig.initIdentityScope(type);

        videoInfoDaoConfig = daoConfigMap.get(VideoInfoDao.class).clone();
        videoInfoDaoConfig.initIdentityScope(type);

        welfareInfoDaoConfig = daoConfigMap.get(WelfareInfoDao.class).clone();
        welfareInfoDaoConfig.initIdentityScope(type);

        beautyPhotoInfoDao = new BeautyPhotoInfoDao(beautyPhotoInfoDaoConfig, this);
        danmakuInfoDao = new DanmakuInfoDao(danmakuInfoDaoConfig, this);
        newsTypeInfoDao = new NewsTypeInfoDao(newsTypeInfoDaoConfig, this);
        videoInfoDao = new VideoInfoDao(videoInfoDaoConfig, this);
        welfareInfoDao = new WelfareInfoDao(welfareInfoDaoConfig, this);

        registerDao(BeautyPhotoInfo.class, beautyPhotoInfoDao);
        registerDao(DanmakuInfo.class, danmakuInfoDao);
        registerDao(NewsTypeInfo.class, newsTypeInfoDao);
        registerDao(VideoInfo.class, videoInfoDao);
        registerDao(WelfareInfo.class, welfareInfoDao);
    }
    
    public void clear() {
        beautyPhotoInfoDaoConfig.clearIdentityScope();
        danmakuInfoDaoConfig.clearIdentityScope();
        newsTypeInfoDaoConfig.clearIdentityScope();
        videoInfoDaoConfig.clearIdentityScope();
        welfareInfoDaoConfig.clearIdentityScope();
    }

    public BeautyPhotoInfoDao getBeautyPhotoInfoDao() {
        return beautyPhotoInfoDao;
    }

    public DanmakuInfoDao getDanmakuInfoDao() {
        return danmakuInfoDao;
    }

    public NewsTypeInfoDao getNewsTypeInfoDao() {
        return newsTypeInfoDao;
    }

    public VideoInfoDao getVideoInfoDao() {
        return videoInfoDao;
    }

    public WelfareInfoDao getWelfareInfoDao() {
        return welfareInfoDao;
    }

}
