package com.newsmvpdemo.module.fragment;

import android.view.View;
import android.widget.TextView;

import com.newsmvpdemo.R;
import com.newsmvpdemo.inject.components.DaggerVideoCompleteComponent;
import com.newsmvpdemo.inject.modules.VideoCompleteModule;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.base.BaseVideoDLFragment;
import com.newsmvpdemo.module.base.IBaseLoadView;
import com.newsmvpdemo.module.base.IRxBusPresenter;
import com.newsmvpdemo.utils.RecyclerHelper;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import rx.functions.Action1;

/**
 * Created by ictcxq on 2018/4/2.
 * video 缓存完成列表
 */

public class VideoCompleteFragment extends BaseVideoDLFragment<IRxBusPresenter> implements IBaseLoadView<List<VideoInfo>>{

    @BindView(R.id.default_bg)
    TextView mDefaultBg;

    @Override
    protected void initInject() {
        DaggerVideoCompleteComponent.builder()
                .applicationComponent(getAppComponent())
                .videoCompleteModule(new VideoCompleteModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_download;
    }

    @Override
    protected void initViews() {
        RecyclerHelper.init_V(mActivity,mRvVideoList,true,mAdapter);
        mRvVideoList.setItemAnimator(new SlideInLeftAnimator());
        initItemLongClick();
        mPresenter.registerRxBus(VideoInfo.class, new Action1<VideoInfo>() {
            @Override
            public void call(VideoInfo info) {
                mPresenter.getData(false);
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(List<VideoInfo> data) {
        if (mDefaultBg.getVisibility() == View.VISIBLE) {
            mDefaultBg.setVisibility(View.GONE);
        }
        mAdapter.replaceData(data);
    }

    @Override
    public void loadMoreData(List<VideoInfo> data) {

    }

    @Override
    public void loadErrorData() {
        mDefaultBg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }
}
