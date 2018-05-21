package com.newsmvpdemo.module.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newsmvpdemo.AndroidApplication;
import com.newsmvpdemo.R;
import com.newsmvpdemo.inject.components.ApplicationComponent;
import com.newsmvpdemo.widget.EmptyLayout;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yb on 2018/3/1.
 * fragment基类
 */

public abstract class BaseFragment<T extends IBasePresenter> extends RxFragment implements IBaseView,EmptyLayout.OnRetryListener{

    @Nullable
    @BindView(R.id.empty_layout)
    EmptyLayout mEmptyLayout;

    @Inject
    protected T mPresenter;

    protected FragmentActivity mActivity;
    private View mRootView;
    private boolean mIsMulti = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mRootView == null) {
            mRootView = inflater.inflate(attachLayoutRes(),null);
            ButterKnife.bind(this,mRootView);
            initInject();
            initViews();
        }
        //缓存的mRootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，
        // 要不然会发生这个mRootView已经有parent的错误。
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if(parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //相当于懒加载
        if(getUserVisibleHint() && mRootView != null && !mIsMulti) {
            mIsMulti = true;
            updateViews(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //相当于懒加载
        if(isVisibleToUser && isVisible() && mRootView != null && !mIsMulti) {
            mIsMulti = true;
            updateViews(false);
        }else {
            super.setUserVisibleHint(isVisibleToUser);
        }
    }


    @Override
    public void hideLoading() {
        if(mEmptyLayout != null) {
            mEmptyLayout.hide();
        }
    }

    @Override
    public void showLoading() {
        if(mEmptyLayout != null) {
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_LOADING);
        }
    }

    @Override
    public void showNetError() {
        if(mEmptyLayout != null) {
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_NO_NET);
            mEmptyLayout.setOnRetryListener(this);
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    @Override
    public void onRetry() {
        updateViews(false);
    }

    /**
     * 初始化toolbar
     *
     * @param toolbar
     * @param homeAsUpEnabled
     * @param title
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        ((BaseActivity)getActivity()).initToolBar(toolbar,homeAsUpEnabled,title);
    }

    /**
     * 获取 ApplicationComponent
     */
    protected ApplicationComponent getAppComponent() {
        return AndroidApplication.getAppComponent();
    }

    /*********************************抽象方法********************************************/
    protected abstract void initInject();
    protected abstract int attachLayoutRes();
    protected abstract void initViews();
    protected abstract void updateViews(boolean isRefresh);
    /*************************************************************************************/
}
