package com.newsmvpdemo.module.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newsmvpdemo.R;
import com.newsmvpdemo.inject.components.DaggerPhotoNewsComponent;
import com.newsmvpdemo.inject.modules.PhotoNewsModule;
import com.newsmvpdemo.module.base.BaseFragment;
import com.newsmvpdemo.module.base.IBaseLoadView;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.bean.PhotoInfo;
import com.newsmvpdemo.module.home.PhotoSetActivity;
import com.newsmvpdemo.utils.RecyclerHelper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by yb on 2018/3/25.
 * 生活
 */

public class PhotoNewsFragment extends BaseFragment<IBasePresenter> implements IBaseLoadView<List<PhotoInfo>>{

    @BindView(R.id.rv_photo_list)
    RecyclerView mRecycler;

    @Inject
    BaseQuickAdapter mAdapter;

    private final static String PREFIX_PHOTO_ID = "0096";

    @Override
    protected void initInject() {
        DaggerPhotoNewsComponent.builder()
                .applicationComponent(getAppComponent())
                .photoNewsModule(new PhotoNewsModule(this))
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
        RecyclerHelper.init_V(mActivity,mRecycler,true,slideAdapter);
        //上拉加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getMoreData();
            }
        },mRecycler);

        //条目点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PhotoInfo item = (PhotoInfo) mAdapter.getItem(position);
                PhotoSetActivity.launch(mActivity,mergerString(item.getSetid()));
            }
        });
    }

    private String mergerString(String setid) {
        return PREFIX_PHOTO_ID + "|" + setid;
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(List<PhotoInfo> data) {
        mAdapter.addData(data);
    }

    @Override
    public void loadMoreData(List<PhotoInfo> data) {
        mAdapter.loadMoreComplete();
        mAdapter.addData(data);
    }

    @Override
    public void loadErrorData() {
        mAdapter.loadMoreEnd();
    }
}
