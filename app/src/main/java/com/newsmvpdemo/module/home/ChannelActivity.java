package com.newsmvpdemo.module.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.newsmvpdemo.R;
import com.newsmvpdemo.adapter.ChannelAdapter;
import com.newsmvpdemo.adapter.NormalChannelAdapter;
import com.newsmvpdemo.inject.components.DaggerChannelComponent;
import com.newsmvpdemo.inject.modules.ChannelModule;
import com.newsmvpdemo.local.table.NewsTypeInfo;
import com.newsmvpdemo.module.base.BaseActivity;
import com.newsmvpdemo.module.presenter.IChannelPresenter;
import com.newsmvpdemo.module.view.IChannelView;
import com.newsmvpdemo.utils.RecyclerHelper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

/**
 * Created by yb on 2018/3/8.
 * 频道管理类
 */

public class ChannelActivity extends BaseActivity<IChannelPresenter>
        implements IChannelView,OnItemDragListener,OnItemSwipeListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_checked_list)
    RecyclerView mCheckRecycler;
    @BindView(R.id.rv_unchecked_list)
    RecyclerView mUnCheckRecycler;

    @Inject
    ChannelAdapter mCkeckAdapter;
    @Inject
    NormalChannelAdapter mUnCheckAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, ChannelActivity.class);
        context.startActivity(intent);
        //((Activity)context).overridePendingTransition(R.anim.fade_entry, R.anim.hold);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_channel;
    }

    @Override
    protected void initInject() {
        DaggerChannelComponent.builder()
                .applicationComponent(getAppComponent())
                .channelModule(new ChannelModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        //Toolbar的基本设置
        initToolBar(mToolbar,true,"频道管理");
        //初始化RecyclerView
        RecyclerHelper.init_G(this,mCheckRecycler,mCkeckAdapter,4);
        RecyclerHelper.init_G(this,mUnCheckRecycler,mUnCheckAdapter,4);
        RecyclerHelper.openDragAndSwipe(mCheckRecycler,mCkeckAdapter,this,this);
        // 设置条目动画,采用库recyclerview-animators
        mCheckRecycler.setItemAnimator(new ScaleInAnimator());
        mUnCheckRecycler.setItemAnimator(new FlipInBottomXAnimator());

        //未添加频道栏目的监听
        mUnCheckAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //1.获取点击条目的对象
                NewsTypeInfo item = mUnCheckAdapter.getItem(position);
                //2.未添加频道的适配器移除点击的条目
                mUnCheckAdapter.remove(position);
                //3.添加频道的适配器，添加点击的条目
                mCkeckAdapter.addData(item);
                //4.通知数据库插入一个数据
                mPresenter.insert(item);
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(List<NewsTypeInfo> checkList, List<NewsTypeInfo> uncheckList) {
        mCkeckAdapter.addData(checkList);
        mUnCheckAdapter.addData(uncheckList);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold,R.anim.fade_exit);
    }

    /*******************************************拖拽功能***********************************************/
    @Override
    public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
    }

    @Override
    public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
        //拖拽过程中，需要先更新数据，再进行位置互换
        mPresenter.update(mCkeckAdapter.getData());
//        mPresenter.swap(source.getAdapterPosition(),target.getAdapterPosition());
    }

    @Override
    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
    }
    /**************************************滑动功能*****************************************************/

    @Override
    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
    }

    @Override
    public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
    }

    @Override
    public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
        //向左滑动删除一个条目过程
        NewsTypeInfo item = mCkeckAdapter.getItem(pos);
        //1.未添加的适配器添加数据
        mUnCheckAdapter.addData(item);
        //2.从数据库中删除一个数据
        mPresenter.delete(item);
    }

    @Override
    public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
    }
}
