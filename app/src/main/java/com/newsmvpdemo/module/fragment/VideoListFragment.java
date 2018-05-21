package com.newsmvpdemo.module.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newsmvpdemo.R;
import com.newsmvpdemo.inject.components.DaggerVideoListComponent;
import com.newsmvpdemo.inject.modules.VideoListModule;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.base.BaseFragment;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.home.VideoPlayerActivity;
import com.newsmvpdemo.module.view.IVideoListView;
import com.newsmvpdemo.utils.RecyclerHelper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by yb on 2018/3/16.
 * 视频集合列表类
 */

public class VideoListFragment extends BaseFragment<IBasePresenter> implements IVideoListView<List<VideoInfo>>{

    @BindView(R.id.rv_photo_list)
    RecyclerView mRecycler;

    @Inject
    BaseQuickAdapter mAdapter;

    private static final String VIDEO_TYPE_KEY = "VideoTypeKey";
    private String mVideoId;

    public static VideoListFragment newInstance(String newsId) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VIDEO_TYPE_KEY,newsId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mVideoId = getArguments().getString(VIDEO_TYPE_KEY);
        }
    }

    @Override
    protected void initInject() {
        DaggerVideoListComponent.builder()
                .applicationComponent(getAppComponent())
                .videoListModule(new VideoListModule(this,mVideoId))
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
        RecyclerHelper.init_V(mActivity,mRecycler,false,slideAdapter);
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(final List<VideoInfo> data) {
        mAdapter.addData(data);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoPlayerActivity.launch(mActivity,data.get(position));
            }
        });
    }
}
