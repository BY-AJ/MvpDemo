package com.newsmvpdemo.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newsmvpdemo.R;
import com.newsmvpdemo.local.table.VideoInfo;

import java.util.List;

/**
 * Created by yb on 2018/3/24.
 * 收藏视频适配器
 */

public class VideoLoveAdapter extends BaseQuickAdapter<VideoInfo,BaseViewHolder>{

    public VideoLoveAdapter(@Nullable List<VideoInfo> data) {
        super(R.layout.adapter_video_love,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoInfo item) {
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_desc, item.getSectiontitle());

        ImageView icon = helper.itemView.findViewById(R.id.iv_thumb);
        Glide.with(helper.itemView.getContext())
                .load(item.getCover())
                .into(icon);
    }
}
