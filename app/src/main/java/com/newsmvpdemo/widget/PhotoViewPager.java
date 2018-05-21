package com.newsmvpdemo.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 *
 * 解决ViewPager与第三方框架PhotoView的滑动冲突
 */

public class PhotoViewPager extends ViewPager{

    public PhotoViewPager(Context context) {
        this(context,null);
    }

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        }catch (IllegalArgumentException e) {
            return  false;
        }
    }
}
