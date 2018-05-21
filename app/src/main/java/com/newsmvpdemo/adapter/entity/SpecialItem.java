package com.newsmvpdemo.adapter.entity;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.newsmvpdemo.module.bean.NewsItemInfo;

/**
 * Created by ictcxq on 2018/3/12.
 */

public class SpecialItem extends SectionEntity<NewsItemInfo>{
    private NewsItemInfo newsItemInfo;

    public SpecialItem(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SpecialItem(NewsItemInfo newsItemInfo) {
        super(newsItemInfo);
        this.newsItemInfo = newsItemInfo;
    }

    public NewsItemInfo getNewsItemInfo(){
        return newsItemInfo;
    }
}
