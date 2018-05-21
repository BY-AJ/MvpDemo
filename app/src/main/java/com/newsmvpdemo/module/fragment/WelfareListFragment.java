package com.newsmvpdemo.module.fragment;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newsmvpdemo.R;
import com.newsmvpdemo.inject.components.DaggerWelfareListComponent;
import com.newsmvpdemo.inject.modules.WelfareListModule;
import com.newsmvpdemo.local.table.WelfareInfo;
import com.newsmvpdemo.module.base.BaseFragment;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.view.IWelfareListView;
import com.newsmvpdemo.utils.RecyclerHelper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by ictcxq on 2018/3/25.
 */

public class WelfareListFragment extends BaseFragment<IBasePresenter> implements IWelfareListView<List<WelfareInfo>>{

    @BindView(R.id.rv_photo_list)
    RecyclerView mRecyclerView;

    @Inject
    BaseQuickAdapter mAdapter;

    @Override
    protected void initInject() {
        DaggerWelfareListComponent.builder()
                .applicationComponent(getAppComponent())
                .welfareListModule(new WelfareListModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_photo_list;
    }

    @Override
    protected void initViews() {
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        RecyclerHelper.init_SV(mActivity,mRecyclerView,2,slideAdapter);

        //上拉加载更多
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
    public void loadData(List<WelfareInfo> data) {
        mAdapter.addData(data);
    }

    @Override
    public void loadMoreData(List<WelfareInfo> data) {
        mAdapter.loadMoreComplete();
        mAdapter.addData(data);
    }

    @Override
    public void loadErrorData() {
        mAdapter.loadMoreEnd();
    }
}
