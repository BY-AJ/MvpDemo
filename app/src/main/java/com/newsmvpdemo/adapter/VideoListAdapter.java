package com.newsmvpdemo.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newsmvpdemo.R;
import com.newsmvpdemo.local.table.VideoInfo;
import com.newsmvpdemo.utils.DefIconFactory;

import java.util.List;

/**
 * Created by yb on 2018/3/16.
 * 视频列表
 */

public class VideoListAdapter extends BaseQuickAdapter<VideoInfo,BaseViewHolder>{

    public VideoListAdapter(@Nullable List<VideoInfo> data) {
        super(R.layout.adapter_video_list,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoInfo item) {
        helper.setText(R.id.tv_title,item.getTitle());
        ImageView icon = helper.itemView.findViewById(R.id.iv_photo);
        Glide.with(helper.itemView.getContext())
                .load(item.getCover())
                .dontAnimate()
                .placeholder(DefIconFactory.provideIcon())
                .into(icon);
    }
}
