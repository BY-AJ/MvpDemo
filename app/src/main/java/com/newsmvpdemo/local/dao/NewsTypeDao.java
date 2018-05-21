package com.newsmvpdemo.local.dao;

import android.content.Context;

import com.newsmvpdemo.local.table.DaoSession;
import com.newsmvpdemo.local.table.NewsTypeInfo;
import com.newsmvpdemo.local.table.NewsTypeInfoDao;
import com.newsmvpdemo.utils.AssetsHelper;
import com.newsmvpdemo.utils.GsonHelper;

import java.util.List;

/**
 * Created by yb on 2018/3/4.
 * 新闻分类数据访问
 */

public class NewsTypeDao {
    // 所有栏目
    private static List<NewsTypeInfo> mAllChannels;


    private NewsTypeDao() {
    }

    /**
     * 更新本地数据，如果数据库新闻列表栏目为 0 则添加头 4 个栏目
     * @param context
     * @param daoSession
     */
    public static void updateLocalData(Context context, DaoSession daoSession) {
        mAllChannels = GsonHelper.convertEntities(AssetsHelper.readData(context,"NewsChannel"),NewsTypeInfo.class);
        NewsTypeInfoDao dao = daoSession.getNewsTypeInfoDao();
        if(dao.count() == 0) {
            //将给定的实体插入数据库
            dao.insertInTx(mAllChannels.subList(0,3));
        }
    }

    /**
     * 获取所有栏目
     * @return
     */
    public static List<NewsTypeInfo> getAllChannels() {
        return mAllChannels;
    }
}
