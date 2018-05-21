package com.newsmvpdemo.module.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.SpecialAdapter;
import com.newsmvpdemo.adapter.entity.SpecialItem;
import com.newsmvpdemo.api.NewsUtils;
import com.newsmvpdemo.inject.components.DaggerSpecialComponent;
import com.newsmvpdemo.inject.modules.SpecialModule;
import com.newsmvpdemo.module.base.BaseSwipeBackActivity;
import com.newsmvpdemo.module.base.IBasePresenter;
import com.newsmvpdemo.module.bean.NewsItemInfo;
import com.newsmvpdemo.module.bean.SpecialInfo;
import com.newsmvpdemo.module.view.ISpecialView;
import com.newsmvpdemo.utils.RecyclerHelper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by yb on 2018/3/11.
 * 专题详情页
 */

public class SpecialActivity extends BaseSwipeBackActivity<IBasePresenter> implements ISpecialView{

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.fab_coping)
    FloatingActionButton fabCoping;
    @BindView(R.id.rv_news_list)
    RecyclerView mRecyclerView;

    @Inject
    SpecialAdapter mAdapter;

    private String mSpecialID;
    private LinearLayoutManager mLayoutManager;

    public static void launch(Context context, String newsId) {
        Intent intent = new Intent(context, SpecialActivity.class);
        intent.putExtra("SpecialID", newsId);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_special;
    }

    @Override
    protected void initInject() {
        mSpecialID = getIntent().getStringExtra("SpecialID");
        DaggerSpecialComponent.builder()
                .applicationComponent(getAppComponent())
                .specialModule(new SpecialModule(this,mSpecialID))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolBar,true,"");
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(mAdapter);
        RecyclerHelper.init_V(this,mRecyclerView,true,new AlphaInAnimationAdapter(animationAdapter));
        mLayoutManager = (LinearLayoutManager)mRecyclerView.getLayoutManager();
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(final List<SpecialItem> specialItems) {
        mAdapter.addData(specialItems);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsItemInfo info = specialItems.get(position).getNewsItemInfo();
                String type = info.getSkipType();
                if(NewsUtils.isNewsSpecial(type)) {
                    SpecialActivity.launch(SpecialActivity.this,info.getSpecialID());//专题详情页
                }else if(NewsUtils.isNewsPhotoSet(type)) {
                    PhotoSetActivity.launch(SpecialActivity.this,info.getPhotosetID());//图集详情页
                }else {
                    NewsArticleActivity.launch(SpecialActivity.this,info.getPostid());//正常详情页
                }
            }
        });
    }

    @Override
    public void loadBanner(SpecialInfo specialBean) {
        //加载Banner
        ImageView view = new ImageView(this);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(this).load(specialBean.getBanner()).into(view);
        mAdapter.setHeaderView(view);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_right_exit);
    }

    @OnClick(R.id.fab_coping)
    public void onClick() {
        mLayoutManager.scrollToPosition(0);
    }
}
