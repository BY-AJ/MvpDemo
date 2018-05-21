package com.newsmvpdemo.adapter.entity;

import android.support.annotation.IntDef;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.newsmvpdemo.module.bean.NewsInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yb on 2018/3/5.
 * 复合新闻类型
 */

public class NewsMultiItem implements MultiItemEntity {

    public static final int ITEM_TYPE_NORMAL = 1;
    public static final int ITEM_TYPE_PHOTO_SET = 2;

    private NewsInfo mNewsBean;
    private int type;

    public NewsMultiItem(@NewsItemType int type ,NewsInfo newsBean) {
        this.type = type;
        this.mNewsBean = newsBean;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public NewsInfo getNewsBean() {
        return mNewsBean;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ITEM_TYPE_NORMAL,ITEM_TYPE_PHOTO_SET})
    public @interface NewsItemType{}
}
