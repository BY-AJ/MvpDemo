package com.newsmvpdemo.module.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.newsmvpdemo.AndroidApplication;
import com.newsmvpdemo.R;
import com.newsmvpdemo.inject.components.ApplicationComponent;
import com.newsmvpdemo.widget.EmptyLayout;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yb on 2018/2/28.
 * activity基类
 */

public abstract class BaseActivity<T extends IBasePresenter> extends RxAppCompatActivity implements IBaseView,EmptyLayout.OnRetryListener{

    @Nullable
    @BindView(R.id.empty_layout)
    protected EmptyLayout mEmptyLayout;

    @Inject
    protected T mPresenter;
    private Unbinder unBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        unBind = ButterKnife.bind(this);
        initInject();
        initViews();
        updateViews(false);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBind.unbind();
    }

    /********************************抽象方法********************************************/
    protected abstract int attachLayoutRes();//布局文件ID
    protected abstract void initInject();//初始化注解
    protected abstract void initViews();//初始化视图控件
    protected abstract void updateViews(boolean isRefresh);//更新视图控件
    /***********************************************************************************/

    /**
     * 添加一个Fragment
     * @param containerId 该id为一个fragment或者FrameLayout
     * @param fragment 被添加的fragment
     * @param tag 被添加的fragment的标记
     */
    protected void addFragment(int containerId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();//开启事务
        fragmentTransaction.add(containerId,fragment,tag);//添加一个Fragment到containerId中。
        fragmentTransaction.addToBackStack(tag);//添加这个Fragment到后台堆栈中
        fragmentTransaction.commit();//提交事务，后台执行事务的操作
    }

    /**
     * 替换Fragment
     * @param containerId 该id为一个fragment或者FrameLayout
     * @param fragment 被替换的fragment
     * @param tag 被替换的fragment的标记
     */
    protected void replaceFragment(int containerId,Fragment fragment,String tag) {
        if(getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();//开启事务
            fragmentTransaction.replace(containerId,fragment,tag);//替换成fragment
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);//设置切换效果
            fragmentTransaction.addToBackStack(tag);//添加这个Fragment到后台堆栈中
            fragmentTransaction.commit();//提交事务，后台执行事务的操作
        }else {
            //弹出tag标记的那层以上的所有fragment
            getSupportFragmentManager().popBackStack(tag,0);
        }
    }

    /**
     *  初始化ToolBar
     * @param toolbar toolbar控件
     * @param homeAsUpEnabled home是否能点击
     * @param title 标题
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);//设置标题
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    /**
     * 获取 ApplicationComponent
     *
     * @return ApplicationComponent
     */
    protected ApplicationComponent getAppComponent() {
        return AndroidApplication.getAppComponent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
