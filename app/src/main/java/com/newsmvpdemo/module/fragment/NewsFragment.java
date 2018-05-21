package com.newsmvpdemo.module.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.ViewPagerAdapter;
import com.newsmvpdemo.inject.components.DaggerNewsFragmentComponent;
import com.newsmvpdemo.inject.modules.NewsFragmentModule;
import com.newsmvpdemo.local.table.NewsTypeInfo;
import com.newsmvpdemo.module.base.BaseFragment;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.module.event.ChannelEvent;
import com.newsmvpdemo.module.home.ChannelActivity;
import com.newsmvpdemo.module.view.INewsFragmentView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by yb on 2018/3/1.
 * 新闻首页
 */

public class NewsFragment extends BaseFragment<IRxBusPresenter> implements INewsFragmentView{

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Inject
    ViewPagerAdapter mPagerAdapter;

    @Override
    protected void initInject() {
        DaggerNewsFragmentComponent.builder()
                .applicationComponent(getAppComponent())
                .newsFragmentModule(new NewsFragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_news_main;
    }

    @Override
    protected void initViews() {
        initToolBar(mToolBar, true, "新闻");
        setHasOptionsMenu(true);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mPresenter.registerRxBus(ChannelEvent.class, new Action1<ChannelEvent>() {
            @Override
            public void call(ChannelEvent channelEvent) {
                handleChannelEvent(channelEvent);
            }
        });
    }

    /**
     * 处理频道事件
     */
    private void handleChannelEvent(ChannelEvent channelEvent) {
        switch (channelEvent.eventType) {
            case ChannelEvent.ADD_EVENT:
                mPagerAdapter.addItem(NewsListFragment.newInstance(channelEvent.newsInfo.getTypeId()),
                        channelEvent.newsInfo.getName());
                break;
            case ChannelEvent.DEL_EVENT:
                // 如果是删除操作直接切换第一项，不然容易出现加载到不存在的Fragment
                mViewPager.setCurrentItem(0);
                mPagerAdapter.deleteItem(channelEvent.newsInfo.getName());
                break;
            case ChannelEvent.UPDATE_EVENT:
                List<Fragment> fragments = new ArrayList<>();
                List<String> titles = new ArrayList<>();
                for (NewsTypeInfo bean:channelEvent.list) {
                    titles.add(bean.getName());
                    fragments.add(NewsListFragment.newInstance(bean.getTypeId()));
                }
                mPagerAdapter.setItems(fragments, titles);
                break;
        }
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_channel,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.item_channel) {
            ChannelActivity.launch(mActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadData(List<NewsTypeInfo> checkList) {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (NewsTypeInfo bean:checkList) {
            titles.add(bean.getName());
            fragments.add(NewsListFragment.newInstance(bean.getTypeId()));
        }
        mPagerAdapter.setItems(fragments, titles);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }
}
