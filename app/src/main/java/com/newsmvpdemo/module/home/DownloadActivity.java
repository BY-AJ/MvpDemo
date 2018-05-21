package com.newsmvpdemo.module.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.ViewPagerAdapter;
import com.newsmvpdemo.inject.components.DaggerDownloadComponent;
import com.newsmvpdemo.inject.modules.DownloadModule;
import com.newsmvpdemo.module.base.BaseActivity;
import com.newsmvpdemo.module.base.BaseVideoDLFragment;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.event.VideosEvent;
import com.newsmvpdemo.module.fragment.VideoCacheFragment;
import com.newsmvpdemo.module.fragment.VideoCompleteFragment;
import com.newsmvpdemo.widget.FlexibleViewPager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.newsmvpdemo.utils.CommonConstant.INDEX_KEY;

/**
 * Created by ictcxq on 2018/4/2.
 */

public class DownloadActivity extends BaseActivity<IRxBusPresenter>{

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    FlexibleViewPager mViewPager;
    @BindView(R.id.btn_select_all)
    TextView mBtnSelectAll;
    @BindView(R.id.btn_select_del)
    TextView mBtnSelectDel;
    @BindView(R.id.fl_del_layout)
    FrameLayout mFlDelLayout;
    @BindView(R.id.tv_close_edit)
    TextView mTvCloseEdit;

    @Inject
    ViewPagerAdapter mAdapter;

    private int mIndex;
    private BaseVideoDLFragment mCompleteFragment;
    private BaseVideoDLFragment mCacheFragment;

    public static void launch(Context context, int index) {
        Intent intent = new Intent(context, DownloadActivity.class);
        intent.putExtra(INDEX_KEY, index);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.zoom_in_entry, R.anim.hold);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_download;
    }

    @Override
    protected void initInject() {
        DaggerDownloadComponent.builder()
                .applicationComponent(getAppComponent())
                .downloadModule(new DownloadModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        mIndex = getIntent().getIntExtra(INDEX_KEY, 0);
        initToolBar(mToolBar, true, "下载管理");
        mViewPager.setAdapter(mAdapter);
        mPresenter.registerRxBus(VideosEvent.class, new Action1<VideosEvent>() {
            @Override
            public void call(VideosEvent videosEvent) {
                if (videosEvent.checkStatus != VideosEvent.CHECK_INVALID) {
                    handleVideoEvent(videosEvent);
                }
            }
        });
    }

    /**
     * 处理 VideoEvent，来改变编辑状态UI
     */
    private void handleVideoEvent(VideosEvent videosEvent) {
        mBtnSelectDel.setEnabled(videosEvent.checkStatus != VideosEvent.CHECK_NONE);
        mBtnSelectAll.setText(videosEvent.checkStatus == VideosEvent.CHECK_ALL ? "取消全选" : "全选");
        mBtnSelectAll.setSelected(videosEvent.checkStatus == VideosEvent.CHECK_ALL);
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        List<String> titles = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();
        mCompleteFragment = new VideoCompleteFragment();
        mCacheFragment = new VideoCacheFragment();
        fragments.add(mCompleteFragment);
        fragments.add(mCacheFragment);
        titles.add("已缓存");
        titles.add("缓存中");
        mAdapter.setItems(fragments,titles);
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(mIndex);
    }

    @OnClick({R.id.btn_select_all, R.id.btn_select_del, R.id.tv_close_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_all:
                if (mCompleteFragment.isEditMode()) {
                    mCompleteFragment.checkAllOrNone(!mBtnSelectAll.isSelected());
                }
                if (mCacheFragment.isEditMode()) {
                    mCacheFragment.checkAllOrNone(!mBtnSelectAll.isSelected());
                }
                break;
            case R.id.btn_select_del:
                if (mCompleteFragment.isEditMode()) {
                    mCompleteFragment.deleteChecked();
                }
                if (mCacheFragment.isEditMode()) {
                    mCacheFragment.deleteChecked();
                }
                break;
            case R.id.tv_close_edit:
                if (mCompleteFragment.exitEditMode() || mCacheFragment.exitEditMode()) {
                    enableEditMode(false);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }

    @Override
    public void onBackPressed() {
        if (mCompleteFragment.exitEditMode() || mCacheFragment.exitEditMode()) {
            enableEditMode(false);
            return;
        }
        super.onBackPressed();
    }

    /**
     * 使能编辑状态
     *
     * @param isEnable
     */
    public void enableEditMode(boolean isEnable) {
        mViewPager.setIsCanControll(!isEnable);
        mFlDelLayout.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        mTvCloseEdit.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        setTabLayoutCanClick(!isEnable);
    }

    private void setTabLayoutCanClick(boolean canClick) {
        LinearLayout tabStrip= (LinearLayout) mTabLayout.getChildAt(0);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            View tabView = tabStrip.getChildAt(i);
            if(tabView !=null){
                tabView.setClickable(canClick);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && mCompleteFragment.exitEditMode() || mCacheFragment.exitEditMode()) {
            enableEditMode(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.zoom_in_exit);
    }
}
