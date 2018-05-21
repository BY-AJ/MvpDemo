package com.newsmvpdemo.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ictcxq on 2018/4/2.
 * 扩展动态控制 ViewPager 滑动使能功能
 */

public class FlexibleViewPager extends ViewPager {

    private boolean isCanControll = true;

    public FlexibleViewPager(Context context) {
        super(context);
    }

    public FlexibleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(isCanControll) {
            return super.onInterceptTouchEvent(ev);
        }else {
            return false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isCanControll) {
            return super.onTouchEvent(ev);
        }else {
            return false;
        }
    }

    public void setIsCanControll(boolean isCanControll) {
        this.isCanControll = isCanControll;
    }
}