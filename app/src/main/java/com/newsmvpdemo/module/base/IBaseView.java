package com.newsmvpdemo.module.base;

import com.trello.rxlifecycle.LifecycleTransformer;

/**
 * 基础 BaseView 接口
 * Created by yb on 2018/2/28.
 */

public interface IBaseView {
    /**
     * 显示加载动画
     */
    void hideLoading();

    /**
     * 隐藏加载
     */
    void showLoading();

    /**
     * 显示网络错误，modify 对网络异常在 BaseActivity 和 BaseFragment 统一处理
     */
    void showNetError();

    /**
     * 绑定生命周期
     * @param <T>
     * @return
     */
    <T>LifecycleTransformer<T> bindToLife();

}
