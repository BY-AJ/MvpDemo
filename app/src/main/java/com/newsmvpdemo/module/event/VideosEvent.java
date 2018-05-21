package com.newsmvpdemo.module.event;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yb on 2018/3/16.
 * 视频事件
 */

public class VideosEvent {
    /**
     * Video 缓存列表选中事件：全没选、选中部分、全选
     */
    public static final int CHECK_INVALID = 400;
    public static final int CHECK_NONE = 401;
    public static final int CHECK_SOME = 402;
    public static final int CHECK_ALL = 403;

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    @IntDef({CHECK_INVALID ,CHECK_NONE,CHECK_SOME,CHECK_ALL })
    public @interface CheckStatus{
    }

    public int checkStatus = CHECK_INVALID;

    public VideosEvent(){
    }

    public VideosEvent(@CheckStatus int checkStatus) {
        this.checkStatus = checkStatus;
    }
}
