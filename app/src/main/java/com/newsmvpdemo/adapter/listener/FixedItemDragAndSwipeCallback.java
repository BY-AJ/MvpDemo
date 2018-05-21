package com.newsmvpdemo.adapter.listener;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.newsmvpdemo.R;

/**
 * Created by yb on 2018/3/8.
 * 禁止固定条目的滑动与拖拽
 */

public class FixedItemDragAndSwipeCallback extends ItemDragAndSwipeCallback{

    public FixedItemDragAndSwipeCallback(BaseItemDraggableAdapter adapter) {
        super(adapter);
    }

    /**
     * 重写onMove方法，当拖拽的时候，防止拖到固定的位置。
     * 可以固定条目是否互换位置
     * 源码实现是：1.先检测要拖拽的条目起始位置from和要移动的目的位置to
     *           ：2.判断from和to的大小
     *           ：3.如果from>to，则数据从后往前移动， Collections.swap(mData, i, i + 1)
     *           ：4.如果小于，则数据从前往后移动， Collections.swap(mData, i, i - 1)
     *           ：5.然后刷新适配器，更新
     *           ：6.监听器的接口回调
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        //禁止跟前3个固定条目互换位置
        int to = target.getAdapterPosition();
        if(to == 0 || to == 1 || to ==2) {
            return false;
        }
        return super.onMove(recyclerView, source, target);
    }

    /**
     * 重写getMovementFlags()方法，可以自定义那些条目不能滑动
     * 源码实现：就是对位置根据传进来的方向，进行左移处理
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        BaseViewHolder holder = (BaseViewHolder) viewHolder;
        TextView textView = holder.itemView.findViewById(R.id.tv_channel_name);
        //禁止前3个固定条目滑动
        String title = textView.getText().toString();
        if(title.equals("头条")) {
            return makeMovementFlags(0,0);
        }else if(title.equals("精选")) {
            return makeMovementFlags(0,0);
        }else if(title.equals("娱乐")) {
            return makeMovementFlags(0,0);
        }else {
            return super.getMovementFlags(recyclerView, viewHolder);
        }
    }
}
