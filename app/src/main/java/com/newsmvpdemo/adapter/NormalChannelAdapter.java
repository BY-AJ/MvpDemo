package com.newsmvpdemo.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newsmvpdemo.R;
import com.newsmvpdemo.local.table.NewsTypeInfo;

import java.util.List;

/**
 * Created by yb on 2018/3/8.
 * 正常频道适配器
 */

public class NormalChannelAdapter extends BaseQuickAdapter<NewsTypeInfo,BaseViewHolder>{

    public NormalChannelAdapter(@Nullable List<NewsTypeInfo> data) {
        super(R.layout.adapter_channel,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsTypeInfo item) {
        helper.setText(R.id.tv_channel_name,item.getName());
    }
}
