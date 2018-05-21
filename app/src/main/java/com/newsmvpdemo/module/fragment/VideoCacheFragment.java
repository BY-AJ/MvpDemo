package com.newsmvpdemo.module.fragment;

import android.view.View;
import android.widget.TextView;

import com.dl7.downloaderlib.entity.FileInfo;
import com.newsmvpdemo.R;
import com.newsmvpdemo.inject.components.DaggerVideoCacheComponent;
import com.newsmvpdemo.inject.modules.VideoCacheModule;
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
 * video缓存列表
 */

public class VideoCacheFragment extends BaseVideoDLFragment<IRxBusPresenter> implements IBaseLoadView<List<VideoInfo>>{

    @BindView(R.id.default_bg)
    TextView mDefaultBg;

    @Override
    protected void initInject() {
        DaggerVideoCacheComponent.builder()
                .applicationComponent(getAppComponent())
                .videoCacheModule(new VideoCacheModule(this))
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
        mPresenter.registerRxBus(FileInfo.class, new Action1<FileInfo>() {
            @Override
            public void call(FileInfo fileInfo) {
                mAdapter.updateDownload(fileInfo);
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }

    @Override
    public void loadData(List<VideoInfo> data) {
        if (mDefaultBg.getVisibility() == View.VISIBLE) {
            mDefaultBg.setVisibility(View.GONE);
        }
        mAdapter.addData(data);
    }

    @Override
    public void loadMoreData(List<VideoInfo> data) {
    }

    @Override
    public void loadErrorData() {
        mDefaultBg.setVisibility(View.VISIBLE);
    }
}
