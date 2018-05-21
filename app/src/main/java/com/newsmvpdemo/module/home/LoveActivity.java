package com.newsmvpdemo.module.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.ViewPagerAdapter;
import com.newsmvpdemo.module.base.BaseActivity;
import com.newsmvpdemo.module.fragment.LovePhotoFragment;
import com.newsmvpdemo.module.fragment.LoveVideoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yb on 2018/3/24.
 * 收藏类
 */

public class LoveActivity extends BaseActivity{

    @BindView(R.id.tab_layout)
    TabLayout mTabLayput;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    private ViewPagerAdapter mAdapter;
    private List<Fragment> fragments;

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoveActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.zoom_out_entry, R.anim.hold);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_love;
    }

    @Override
    protected void initInject() {
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar,true,"收藏");
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        fragments = new ArrayList<>();
        fragments.add(new LovePhotoFragment());
        fragments.add(new LoveVideoFragment());
        mAdapter.setItems(fragments,new String[]{"图片","视频"});
        mViewPager.setAdapter(mAdapter);
        mTabLayput.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.zoom_out_exit);
    }
}
