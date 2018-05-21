package com.newsmvpdemo.utils;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.newsmvpdemo.adapter.listener.FixedItemDragAndSwipeCallback;

/**
 * Created by ictcxq on 2018/3/6.
 */

public final class RecyclerHelper {

    private RecyclerHelper() {
        throw new AssertionError();
    }

    //垂直列表
    public static void init_V(Context context, RecyclerView view,boolean isDivided,RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setLayoutManager(layoutManager);//设置管理器
        view.setItemAnimator(new DefaultItemAnimator());//设置动画
        if(isDivided) {
            view.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        }
        view.setAdapter(adapter);
    }

    //瀑布列表
    public static void init_SV(Context context, RecyclerView view,int count,RecyclerView.Adapter adapter) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(count, StaggeredGridLayoutManager.VERTICAL);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    //网格列表
    public static void init_G(Context context, RecyclerView view,RecyclerView.Adapter adapter,int column) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, column, LinearLayoutManager.VERTICAL, false);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    /**
     * 打开拖拽和滑动的功能
     * @param view RecyclerView控件
     * @param adapter RecyclerView对应的适配器
     * @param dragListener 拖拽监听
     * @param swipeListener 滑动监听
     */
    public static void openDragAndSwipe(RecyclerView view, BaseItemDraggableAdapter adapter,
                                        OnItemDragListener dragListener, OnItemSwipeListener swipeListener) {

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new FixedItemDragAndSwipeCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(view);
        //打开拖拽
        adapter.enableDragItem(itemTouchHelper);
        adapter.setOnItemDragListener(dragListener);
        //打开滑动
        itemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.LEFT);
        adapter.enableSwipeItem();
        adapter.setOnItemSwipeListener(swipeListener);
    }
}
