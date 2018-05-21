package com.newsmvpdemo.module.fragment;

import android.animation.Animator;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.ViewPagerAdapter;
import com.newsmvpdemo.inject.components.DaggerPhotoMainComponent;
import com.newsmvpdemo.inject.modules.PhotoMainModule;
import com.newsmvpdemo.module.base.BaseFragment;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.event.LoveEvent;
import com.newsmvpdemo.module.home.LoveActivity;
import com.newsmvpdemo.module.view.IPhotoMainView;
import com.newsmvpdemo.utils.AnimatorHelper;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by yb on 2018/3/1.
 * 图片
 */

public class PhotosFragment extends BaseFragment<IRxBusPresenter> implements IPhotoMainView{

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.iv_count)
    TextView mTvCount;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Inject
    ViewPagerAdapter mAdapter;

    private Animator mLovedAnimator;

    @Override
    protected void initInject() {
        DaggerPhotoMainComponent.builder()
                .applicationComponent(getAppComponent())
                .photoMainModule(new PhotoMainModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_photo_main;
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar,true,"图片");
        ArrayList<Fragment> fragments = new ArrayList<>();
        //fragments.add(new BeautyListFragment());
        fragments.add(new WelfareListFragment());
        fragments.add(new PhotoNewsFragment());
        mAdapter.setItems(fragments,new String[]{"福利","生活"});
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mPresenter.registerRxBus(LoveEvent.class, new Action1<LoveEvent>() {
            @Override
            public void call(LoveEvent loveEvent) {
                mPresenter.getData(false);
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void updateLovedCount(int lovedCount) {
        mTvCount.setText(lovedCount+"");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLovedAnimator == null) {
            mTvCount.post(new Runnable() {
                @Override
                public void run() {
                    mLovedAnimator = AnimatorHelper.doHappyJump(mTvCount, mTvCount.getHeight() * 2/3, 3000);
                }
            });
        } else {
            AnimatorHelper.startAnimator(mLovedAnimator);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        AnimatorHelper.stopAnimator(mLovedAnimator);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }

    @OnClick(R.id.fl_layout)
    public void onClick() {
        LoveActivity.launch(mActivity);
    }
}
