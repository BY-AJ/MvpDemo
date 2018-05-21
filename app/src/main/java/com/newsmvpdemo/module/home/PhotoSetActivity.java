package com.newsmvpdemo.module.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.PhotoSetAdapter;
import com.newsmvpdemo.inject.components.DaggerPhotoSetComponent;
import com.newsmvpdemo.inject.modules.PhotoSetModule;
import com.newsmvpdemo.module.base.BaseActivity;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.bean.PhotoSetInfo;
import com.newsmvpdemo.module.view.IPhotoSetView;
import com.newsmvpdemo.widget.PhotoViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ictcxq on 2018/3/11.
 */

public class PhotoSetActivity extends BaseActivity<IBasePresenter> implements IPhotoSetView{

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.vp_Photo)
    PhotoViewPager vp_Photo;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_index)
    TextView mTvIndex;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_content)
    TextView mTvContent;

    private String mPhotosetID;
    private PhotoSetAdapter mAdapter;
    private List<PhotoSetInfo.PhotosEntity> mPhotosEntities;

    public static void launch(Context context, String newsId) {
        Intent intent = new Intent(context, PhotoSetActivity.class);
        intent.putExtra("PhotosetID", newsId);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_photoset;
    }

    @Override
    protected void initInject() {
        mPhotosetID = getIntent().getStringExtra("PhotosetID");
        DaggerPhotoSetComponent.builder()
                .applicationComponent(getAppComponent())
                .photoSetModule(new PhotoSetModule(this,mPhotosetID))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolBar,true,"");
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(PhotoSetInfo photoSetInfo) {
        mPhotosEntities = photoSetInfo.getPhotos();
        List<String> mImgUrl = new ArrayList<>();
        //取出图集Url地址
        for (PhotoSetInfo.PhotosEntity bean: mPhotosEntities) {
            mImgUrl.add(bean.getImgurl());
        }
        //设置适配器
        mAdapter = new PhotoSetAdapter(this, mImgUrl);
        vp_Photo.setAdapter(mAdapter);
        //设置图集相关内容
        mTvTitle.setText(photoSetInfo.getSetname());
        mTvIndex.setText(1+"/");
        mTvCount.setText(mPhotosEntities.size() + "");
        mTvContent.setText(mPhotosEntities.get(0).getNote());
        //设置ViewPager页面选择监听
        vp_Photo.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                mTvIndex.setText((position+1)+"/");
                mTvContent.setText(mPhotosEntities.get(position).getNote());
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_right_exit);
    }
}
