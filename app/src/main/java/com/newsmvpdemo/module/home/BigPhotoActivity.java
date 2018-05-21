package com.newsmvpdemo.module.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.newsmvpdemo.AndroidApplication;
import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.PhotoPagerAdapter;
import com.newsmvpdemo.local.table.WelfareInfo;
import com.newsmvpdemo.module.base.BaseActivity;
import com.newsmvpdemo.module.base.IBaseLoadView;
import com.newsmvpdemo.module.base.ILocalPresenter;
import com.newsmvpdemo.module.presenter.BigPhotoPresenter;
import com.newsmvpdemo.utils.AnimatorHelper;
import com.newsmvpdemo.utils.CommonConstant;
import com.newsmvpdemo.utils.DownloadUtils;
import com.newsmvpdemo.utils.SnackbarUtils;
import com.newsmvpdemo.widget.PhotoViewPager;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by ictcxq on 2018/3/27.
 */

public class BigPhotoActivity extends BaseActivity<ILocalPresenter> implements IBaseLoadView<List<WelfareInfo>>{

    @BindView(R.id.vp_photo)
    PhotoViewPager mVpPhoto;
    @BindView(R.id.iv_favorite)
    ImageView mIvFavorite;
    @BindView(R.id.iv_download)
    ImageView mIvDownload;
    @BindView(R.id.iv_praise)
    ImageView mIvPraise;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ll_layout)
    LinearLayout mLlLayout;

    private static final String BIG_PHOTO_KEY = "BigPhotoKey";
    private static final String PHOTO_INDEX_KEY = "PhotoIndexKey";
    private static final String FROM_LOVE_ACTIVITY = "FromLoveActivity";

    private List<WelfareInfo> mPhotoList;
    private int mIndex; // 初始索引
    private boolean mIsFromLoveActivity;    // 是否从 LoveActivity 启动进来
    private boolean mIsHideToolbar = false; // 是否隐藏 Toolbar
    private boolean mIsInteract = false;    // 是否和 ViewPager 联动
    private int mCurPosition;   // Adapter 当前位置
    private boolean[] mIsDelLove;   // 保存被删除的收藏项
    private RxPermissions mRxPermissions;
    private PhotoPagerAdapter mAdapter;

    public static void launch(Context context, ArrayList<WelfareInfo> datas, int index) {
        Intent intent = new Intent(context, BigPhotoActivity.class);
        intent.putParcelableArrayListExtra(BIG_PHOTO_KEY, datas);
        intent.putExtra(PHOTO_INDEX_KEY, index);
        intent.putExtra(FROM_LOVE_ACTIVITY, false);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.expand_vertical_entry, R.anim.hold);
    }

    // 这个给 LoveActivity 使用，配合 setResult() 返回取消的收藏，这样做体验会好点，其实用 RxBus 会更容易做
    public static void launchForResult(Fragment fragment, ArrayList<WelfareInfo> datas, int index) {
        Intent intent = new Intent(fragment.getContext(), BigPhotoActivity.class);
        intent.putParcelableArrayListExtra(BIG_PHOTO_KEY, datas);
        intent.putExtra(PHOTO_INDEX_KEY, index);
        intent.putExtra(FROM_LOVE_ACTIVITY, true);
        fragment.startActivityForResult(intent,CommonConstant.REQUEST_CODE);
        fragment.getActivity().overridePendingTransition(R.anim.expand_vertical_entry, R.anim.hold);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_big_photo;
    }

    @Override
    protected void initInject() {
        mPhotoList = getIntent().getParcelableArrayListExtra(BIG_PHOTO_KEY);
        mIndex = getIntent().getIntExtra(PHOTO_INDEX_KEY, 0);
        mIsFromLoveActivity = getIntent().getBooleanExtra(FROM_LOVE_ACTIVITY, false);

        mPresenter = new BigPhotoPresenter(this, AndroidApplication.getDb().getWelfareInfoDao(),mPhotoList
                            ,AndroidApplication.getRxBusHelper());
        mAdapter = new PhotoPagerAdapter(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar, true, "");
        mVpPhoto.setAdapter(mAdapter);

        //TODO 图片浏览加载更多
        if(!mIsFromLoveActivity) {
            mAdapter.setLoadMoreListener(new PhotoPagerAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mPresenter.getMoreData();
                }
            });
        }else {
            mIsDelLove = new boolean[mPhotoList.size()];
        }

        //Viewpager设置页面监听
        mVpPhoto.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                mCurPosition = position;
                // 设置图标状态
                mIvFavorite.setSelected(mAdapter.isLoved(position));
                mIvDownload.setSelected(mAdapter.isDownload(position));
                mIvPraise.setSelected(mAdapter.isPraise(position));
            }
        });

        //TODO 图片点击下载
        mRxPermissions = new RxPermissions(this);
        //触发某个特定事件的权限请求，则需要在初始化阶段将您的事件设置为可观察的事件
        RxView.clicks(mIvDownload)
                .compose(mRxPermissions.ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if(granted) {
                            DownloadUtils.downloadOrDeletePhoto(BigPhotoActivity.this, mAdapter.getData(mCurPosition).getUrl(),
                                    mAdapter.getData(mCurPosition).getId(), new DownloadUtils.OnCompletedListener() {
                                        @Override
                                        public void onCompleted(String url) {
                                            mAdapter.getData(url).setIsDownload(true);
                                            mIvDownload.setSelected(true);
                                            mPresenter.insert(mAdapter.getData(url));
                                        }
                                        @Override
                                        public void onDeleted(String url) {
                                            mAdapter.getData(url).setIsDownload(false);
                                            mIvDownload.setSelected(false);
                                            mPresenter.delete(mAdapter.getData(url));
                                        }
                                    });
                        }else {
                            SnackbarUtils.showSnackbar(BigPhotoActivity.this, "权限授权失败", false);
                        }
                    }
                });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(List<WelfareInfo> data) {
        mAdapter.updateData(data);
        mVpPhoto.setCurrentItem(mIndex);
        //TODO 解决从LoveActivity跳转过来时，第一张图片不会回调 addOnPageChangeListener，所以这里要处理下
        if (mIndex == 0) {
            mIvFavorite.setSelected(mAdapter.isLoved(0));
            mIvDownload.setSelected(mAdapter.isDownload(0));
            mIvPraise.setSelected(mAdapter.isPraise(0));
        }
    }

    @Override
    public void loadMoreData(List<WelfareInfo> data) {
        mAdapter.addData(data);
        mAdapter.startUpdate(mVpPhoto);
    }

    @Override
    public void loadErrorData() {
    }

    @OnClick({R.id.iv_favorite, R.id.iv_praise, R.id.iv_share})
    public void onClick(View view) {
        final boolean isSelected = !view.isSelected();
        switch (view.getId()) {
            case R.id.iv_favorite:
                mAdapter.getData(mCurPosition).setIsLove(isSelected);
                break;
            case R.id.iv_praise:
                mAdapter.getData(mCurPosition).setIsPraise(isSelected);
                break;
            case R.id.iv_share:
                //TODO 到时一起与新闻页面分享一起实现
                Toast.makeText(this,"分享:功能没加(╯-╰)",Toast.LENGTH_SHORT).show();
                break;
        }
        // 除分享外都做动画和数据库处理
        if (view.getId() != R.id.iv_share) {
            view.setSelected(isSelected);
            AnimatorHelper.doHeartBeat(view, 500);
            if (isSelected) {
                mPresenter.insert(mAdapter.getData(mCurPosition));
            } else {
                mPresenter.delete(mAdapter.getData(mCurPosition));
            }
        }

        //记录移除喜爱的图片，若移除，则对应位置设置成True
        if(mIsFromLoveActivity && view.getId() == R.id.iv_favorite) {
            mIsDelLove[mCurPosition] = !isSelected;
        }
    }

    @Override
    public void finish() {
        if (mIsFromLoveActivity) {
            Intent intent = new Intent();
            intent.putExtra(CommonConstant.RESULT_KEY, mIsDelLove);
            // 把数据传给 LoveActivity
            setResult(RESULT_OK, intent);
        }
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.zoom_out_exit);
    }
}
