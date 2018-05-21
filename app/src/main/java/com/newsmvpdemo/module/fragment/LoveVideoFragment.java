package com.newsmvpdemo.module.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newsmvpdemo.R;
import com.newsmvpdemo.inject.components.DaggerLoveVideoComponent;
import com.newsmvpdemo.inject.modules.LoveVideoModule;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.module.base.BaseFragment;
import com.newsmvpdemo.module.base.ILocalPresenter;
import com.newsmvpdemo.module.home.VideoPlayerActivity;
import com.newsmvpdemo.module.view.ILoveVideoView;
import com.newsmvpdemo.utils.DialogHelper;
import com.newsmvpdemo.utils.RecyclerHelper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yb on 2018/3/24.
 * 视频收藏
 */

public class LoveVideoFragment extends BaseFragment<ILocalPresenter> implements ILoveVideoView{

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.default_bg)
    TextView mTvDefaultBg;

    @Inject
    BaseQuickAdapter mAdapter;

    private int mCurrentIndex = 0;

    @Override
    protected void initInject() {
        DaggerLoveVideoComponent.builder()
                .applicationComponent(getAppComponent())
                .loveVideoModule(new LoveVideoModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_love_video;
    }

    @Override
    protected void initViews() {
        SlideInRightAnimationAdapter animationAdapter = new SlideInRightAnimationAdapter(mAdapter);
        RecyclerHelper.init_V(mActivity,mRecyclerView,false,animationAdapter);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
        //长按删除条目
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                DialogHelper.deleteDialog(mActivity, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.delete(mAdapter.getItem(position));
                        mAdapter.remove(position);
                    }
                });
                return true;
            }
        });
        //单击点击条目调到播放视频页面
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mCurrentIndex = position;
                VideoPlayerActivity.launchForResult(LoveVideoFragment.this,(VideoInfo) mAdapter.getItem(position));
            }
        });
    }

    @Override
    public void loadData(List<VideoInfo> data) {
        if (mTvDefaultBg.getVisibility() == View.VISIBLE) {
            mTvDefaultBg.setVisibility(View.GONE);
        }
        mAdapter.addData(data);
    }

    @Override
    public void loadNoData() {
        mTvDefaultBg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == VideoPlayerActivity.VIDEO_REQUEST_CODE && resultCode == RESULT_OK) {
            mAdapter.remove(mCurrentIndex);
            //当收藏的视频为空的时候，加载空数据
            if(mAdapter.getItemCount() == 0) {
                loadNoData();
            }
        }
    }
}
