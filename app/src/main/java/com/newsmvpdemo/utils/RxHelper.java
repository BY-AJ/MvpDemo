package com.newsmvpdemo.utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * RxJava 帮助类
 * Created by yb on 2018/3/1.
 */

public final class RxHelper {

    private RxHelper() {
        throw new AssertionError();
    }

    /**
     * 倒计时
     */
    public static Observable<Integer> countdown(int time) {
        if(time < 0) {
            time = 0;
        }
        final int countTime = time;
        return Observable.interval(0,1, TimeUnit.SECONDS)//创建一个按照给定的时间间隔发射从0开始的整数序列的
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long aLong) {
                        return countTime - aLong.intValue();
                    }
                })
                .take(countTime+1)//指定最多输出的数量
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
