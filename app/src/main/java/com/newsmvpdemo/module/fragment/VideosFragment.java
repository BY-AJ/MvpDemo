package com.newsmvpdemo.module.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.ViewPagerAdapter;
import com.newsmvpdemo.inject.components.DaggerVideosMainComponent;
import com.newsmvpdemo.inject.modules.VideosMainModule;
import com.newsmvpdemo.module.base.BaseFragment;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.event.VideosEvent;
import com.newsmvpdemo.module.home.DownloadActivity;
import com.newsmvpdemo.module.home.LoveActivity;
import com.newsmvpdemo.module.view.IVideosView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by yb on 2018/3/1.
 * 视频
 */

public class VideosFragment extends BaseFragment<IRxBusPresenter> implements IVideosView{

    private final String[] VIDEO_ID = new String[]{
            "V9LG4B3A0", "V9LG4E6VR", "V9LG4CHOR", "00850FRB"
    };
    private final String[] VIDEO_TITLE = new String[]{
            "热点", "搞笑", "娱乐", "精品"
    };

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.iv_love_count)
    TextView mTvLovedCount;
    @BindView(R.id.tv_download_count)
    TextView mTvDownloadCount;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Inject
    ViewPagerAdapter mAdapter;

    @Override
    protected void initInject() {
        DaggerVideosMainComponent.builder()
                .applicationComponent(getAppComponent())
                .videosMainModule(new VideosMainModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_video_main;
    }

    @Override
    protected void initViews() {
        initToolBar(mToolBar,true,"视频");
        mPresenter.registerRxBus(VideosEvent.class, new Action1<VideosEvent>() {
            @Override
            public void call(VideosEvent videosEvent) {
                if(videosEvent.checkStatus == VideosEvent.CHECK_INVALID) {
                    mPresenter.getData(false);
                }
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        List<Fragment> fragments = new ArrayList<>();
        for (int i=0; i<VIDEO_ID.length; i++) {
            fragments.add(VideoListFragment.newInstance(VIDEO_ID[i]));
        }
        mAdapter.setItems(fragments,VIDEO_TITLE);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mPresenter.getData(isRefresh);
    }

    @Override
    public void updateLovedCount(int lovedCount) {
        mTvLovedCount.setText(lovedCount +"");
    }

    @Override
    public void updateDownloadCount(int downloadCount) {
        mTvDownloadCount.setVisibility(downloadCount > 0 ? View.VISIBLE : View.GONE);
        mTvDownloadCount.setText(downloadCount + "");
    }

    @OnClick({R.id.fl_love_layout, R.id.fl_download_layout})
    public void onClick(View view)  {
        switch (view.getId()) {
            case R.id.fl_love_layout:
                LoveActivity.launch(mActivity);
                break;
            case R.id.fl_download_layout:
                DownloadActivity.launch(mActivity,0);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }
}
