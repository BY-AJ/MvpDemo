package com.newsmvpdemo.module.event;

import android.support.annotation.IntDef;

import com.newsmvpdemo.local.table.NewsTypeInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yb on 2018/3/4.
 * 数据库更新事件
 */

public class ChannelEvent {
    /**
     * 频道事件：添加、删除和交换位置
     */
    public static final int ADD_EVENT = 301;
    public static final int DEL_EVENT = 302;
    public static final int SWAP_EVENT = 303;
    public static final int UPDATE_EVENT = 304;

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    @IntDef({ADD_EVENT, DEL_EVENT, SWAP_EVENT,UPDATE_EVENT})
    public @interface ChannelEventType{}

    public int eventType;
    public NewsTypeInfo newsInfo;
    public int fromPos = -1;
    public int toPos = -1;
    public List<NewsTypeInfo> list=new ArrayList<>();

    public ChannelEvent(@ChannelEventType int eventType, NewsTypeInfo newsInfo) {
        this.eventType = eventType;
        this.newsInfo = newsInfo;
    }

    public ChannelEvent(@ChannelEventType int eventType) {
        this.eventType = eventType;
    }

    public ChannelEvent(@ChannelEventType int eventType, int fromPos, int toPos) {
        this.eventType = eventType;
        this.fromPos = fromPos;
        this.toPos = toPos;
    }

    public ChannelEvent(@ChannelEventType int eventType, List<NewsTypeInfo> list) {
        this.eventType = eventType;
        this.list = list;
    }
}
