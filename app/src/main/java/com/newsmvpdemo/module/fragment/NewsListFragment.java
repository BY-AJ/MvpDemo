package com.newsmvpdemo.module.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daimajia.slider.library.SliderLayout;
import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.entity.NewsMultiItem;
import com.newsmvpdemo.api.NewsUtils;
import com.newsmvpdemo.inject.components.DaggerNewsListComponent;
import com.newsmvpdemo.inject.modules.NewsListModule;
import com.newsmvpdemo.module.base.BaseFragment;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.bean.NewsInfo;
import com.newsmvpdemo.module.home.NewsArticleActivity;
import com.newsmvpdemo.module.home.PhotoSetActivity;
import com.newsmvpdemo.module.home.SpecialActivity;
import com.newsmvpdemo.module.view.INewsListView;
import com.newsmvpdemo.utils.RecyclerHelper;
import com.newsmvpdemo.utils.SliderHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

/**
 * Created by yb on 2018/3/4.
 * 新闻列表
 */

public class NewsListFragment extends BaseFragment<IBasePresenter> implements INewsListView<List<NewsMultiItem>>{

    @BindView(R.id.rv_news_list)
    RecyclerView mRecyclerView;

    @Inject
    BaseQuickAdapter mAdapter;

    private List<NewsMultiItem> mData = new ArrayList<>();

    private static final String NEWS_TYPE_KEY = "NewsTypeKey";

    private String mNewsId;
    private SliderLayout mAdSlider;

    public static NewsListFragment newInstance(String newsId) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_TYPE_KEY, newsId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mNewsId = getArguments().getString(NEWS_TYPE_KEY);
        }
    }

    @Override
    protected void initInject() {
        DaggerNewsListComponent.builder()
                .applicationComponent(getAppComponent())
                .newsListModule(new NewsListModule(this,mNewsId))
                .build()
                .inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initViews() {
        SlideInRightAnimationAdapter animAdapter = new SlideInRightAnimationAdapter(mAdapter);
        RecyclerHelper.init_V(mActivity,mRecyclerView,true,new AlphaInAnimationAdapter(animAdapter));

        //设置条目点击事件,跳转到对应的新闻详情页
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsInfo info = mData.get(position).getNewsBean();
                String type = info.getSkipType();
                if(NewsUtils.isNewsSpecial(type)) {
                    SpecialActivity.launch(mActivity,info.getSpecialID());//专题详情页
                }else if(NewsUtils.isNewsPhotoSet(type)) {
                    PhotoSetActivity.launch(mActivity,info.getPhotosetID());//图集详情页
                }else {
                    NewsArticleActivity.launch(mActivity,info.getPostid());//正常详情页
                }
            }
        });

        //加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getMoreData();
            }
        },mRecyclerView);
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(final List<NewsMultiItem> data) {
        mData.addAll(data);
        mAdapter.addData(mData);
    }

    @Override
    public void loadMoreData(List<NewsMultiItem> data) {
        mAdapter.loadMoreComplete();
        mData.addAll(data);
        mAdapter.addData(mData);
    }

    @Override
    public void loadErrorData() {
        mAdapter.loadMoreEnd();
    }

    @Override
    public void loadAdData(NewsInfo newsBean) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.head_news_list, null);
        mAdSlider = view.findViewById(R.id.slider_ads);
        SliderHelper.initAdSlider(mActivity,mAdSlider,newsBean);
        mAdapter.setHeaderView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdSlider != null) {
            mAdSlider.startAutoCycle();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAdSlider != null) {
            mAdSlider.stopAutoCycle();
        }
    }
}
