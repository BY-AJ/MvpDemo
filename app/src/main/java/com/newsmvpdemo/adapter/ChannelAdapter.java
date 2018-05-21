package com.newsmvpdemo.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newsmvpdemo.R;
import com.newsmvpdemo.local.table.NewsTypeInfo;

import java.util.List;

/**
 * Created by yb on 2018/3/8.
 * 频道拖拽与滑动适配器
 */

public class ChannelAdapter extends BaseItemDraggableAdapter<NewsTypeInfo,BaseViewHolder>{


    public ChannelAdapter(List<NewsTypeInfo> data) {
        super(R.layout.adapter_channel,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsTypeInfo item) {
        TextView tvChannel = helper.itemView.findViewById(R.id.tv_channel_name);
        //固定条目的显示的背景颜色
        if(item.getName().equals("头条") || item.getName().equals("精选") || item.getName().equals("娱乐")) {
            tvChannel.setBackgroundResource(R.drawable.shape_channel_fixed);
        }
        tvChannel.setText(item.getName());

    }


}
