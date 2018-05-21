package com.newsmvpdemo.module.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newsmvpdemo.AndroidApplication;
import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.WelfareListAdapter;
import com.newsmvpdemo.local.table.WelfareInfo;
import com.newsmvpdemo.module.base.BaseFragment;
import com.newsmvpdemo.module.base.IBaseLoadView;
import com.newsmvpdemo.module.base.ILocalPresenter;
import com.newsmvpdemo.module.presenter.LovePhotoPresenter;
import com.newsmvpdemo.utils.CommonConstant;
import com.newsmvpdemo.utils.DialogHelper;
import com.newsmvpdemo.utils.RecyclerHelper;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yb on 2018/3/24.
 * 图片收藏
 */

public class LovePhotoFragment extends BaseFragment<ILocalPresenter> implements IBaseLoadView<List<WelfareInfo>>{

    @BindView(R.id.rv_love_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.default_bg)
    TextView mDefaultBg;
    private WelfareListAdapter mAdapter;


    @Override
    protected void initInject() {
        mPresenter = new LovePhotoPresenter(this, AndroidApplication.getDb().getWelfareInfoDao(),
                AndroidApplication.getRxBusHelper());
        mAdapter = new WelfareListAdapter(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_love_photo;
    }

    @Override
    protected void initViews() {
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        RecyclerHelper.init_SV(mActivity,mRecyclerView,2,slideAdapter);
        mRecyclerView.setItemAnimator(new FlipInLeftYAnimator());

        //长按删除条目
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                DialogHelper.deleteDialog(getContext(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.delete(mAdapter.getItem(position));
                        mAdapter.remove(position);
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(List<WelfareInfo> data) {
        if (mDefaultBg.getVisibility() == View.VISIBLE) {
            mDefaultBg.setVisibility(View.GONE);
        }
        mAdapter.addData(data);
    }

    @Override
    public void loadMoreData(List<WelfareInfo> data) {
    }

    @Override
    public void loadErrorData() {
        mDefaultBg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CommonConstant.REQUEST_CODE && resultCode == RESULT_OK) {
            final boolean[] delLove = data.getBooleanArrayExtra(CommonConstant.RESULT_KEY);
            // 延迟 500MS 做删除操作，不然退回来看不到动画效果
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = delLove.length - 1; i >0; i--) {
                        if (delLove[i]) {
                            mAdapter.remove(i);
                        }
                    }
                }
            }, 500);
        }
    }
}
